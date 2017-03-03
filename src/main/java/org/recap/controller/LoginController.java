package org.recap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.recap.RecapConstants;
import org.recap.model.LoginValidator;
import org.recap.model.UserForm;
import org.recap.repository.InstitutionDetailsRepository;
import org.recap.repository.UserDetailsRepository;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagementService;
import org.recap.security.UserService;
import org.recap.util.HelperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by dharmendrag on 25/11/16.
 */
@RestController
@RequestMapping(value="/userAuth")
@Api(value="userAuth", description="To authenticate user", position = 1)
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;

    private LoginValidator loginValidator=new LoginValidator();

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationServiceImpl authorizationService;

    @Autowired
    private DefaultWebSubjectContext defaultWebSubjectContext;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    HelperUtil helperUtil;


    @RequestMapping(value="/authService",method= RequestMethod.POST)
    @ApiOperation(value="authService",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Session created successfully")})
    public Map<String,Object> createSession(@RequestBody UsernamePasswordToken token, HttpServletRequest request, BindingResult error){
        UserForm userForm=new UserForm();
        Map<String,Object> authMap=new HashMap<String,Object>();
        boolean superAdminUser=false;
        boolean recapUser=false;
        Map<Integer,String> permissionMap=null;
        try
        {
            if (token == null) {
                authMap.put(RecapConstants.USER_AUTHENTICATION, false);
                throw new AuthenticationException("User Name Password token is empty");
            }
            String[] values = UserManagementService.userAndInstitution(token.getUsername());
            if (values != null) {
                userForm.setUsername(values[0]);
                Integer institutionIdByCode = helperUtil.getInstitutionIdByCode(values[1]);
                userForm.setInstitution(institutionIdByCode);
                loginValidator.validate(userForm, error);
            }

            if (error.hasErrors()) {
                logger.debug("Login Screen validation failed");
                authMap.put("authentication", false);
                throw new AuthenticationException("User Name Password token validation fails");
            }

            Subject subject = SecurityUtils.getSubject();
            subject.login(token);

            if (!subject.isAuthenticated()) {
                throw new AuthenticationException("Subject Authtentication Failed");
            }
            authorizationService.setSubject(token,subject);
            permissionMap= userService.getPermissions();
            getPermissionsForUI(subject,authMap,permissionMap);
            authMap.put(RecapConstants.USER_AUTHENTICATION, true);
            authMap.put("userName", userForm.getUsername());
            authMap.put(RecapConstants.USER_INSTITUTION, userForm.getInstitution());
            authMap.put(RecapConstants.USER_ID, subject.getPrincipal());
            List<Integer> roleId = userManagementService.getRolesForUser((Integer) subject.getPrincipal());
            if(roleId.contains(1)){
                superAdminUser = true;
            }
            recapUser=subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.BARCODE_RESTRICTED)));
            authMap.put(RecapConstants.SUPER_ADMIN_USER, superAdminUser);
            authMap.put(RecapConstants.ReCAP_USER, recapUser);
            Collections.unmodifiableMap(authMap);
            Session session=subject.getSession();
            session.setAttribute(RecapConstants.PERMISSION_MAP,permissionMap);
            session.setAttribute(RecapConstants.USER_ID,subject.getPrincipal());
        }
        catch(AuthenticationException e)
        {
            logger.debug("Authentication exception");
            logger.error("Exception in authentication : "+e.getMessage());
            authMap.put(RecapConstants.USER_AUTHENTICATION,false);
            authMap.put(RecapConstants.USER_AUTH_ERRORMSG,e.getMessage());
        }
        catch(Exception e)
        {
            logger.error("Exception occured in authentication : "+e.getLocalizedMessage());
            authMap.put(RecapConstants.USER_AUTHENTICATION,false);
            authMap.put(RecapConstants.USER_AUTH_ERRORMSG,e.getMessage());
        }

            return authMap;

    }

    @RequestMapping(value="/logout",method=RequestMethod.POST)
    public void logoutUser(@RequestBody UsernamePasswordToken token){
        logger.info("Subject Logged out");
        authorizationService.unAuthorized(token);
    }

    private void getPermissionsForUI(Subject subject,Map<String,Object> authMap,Map<Integer,String> permissionMap) {
        authMap.put(RecapConstants.REQUEST_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.REQUEST_PLACE))));
        authMap.put(RecapConstants.COLLECTION_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.WRITE_GCD))));
        authMap.put(RecapConstants.REPORTS_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.VIEW_PRINT_REPORTS))));
        authMap.put(RecapConstants.SEARCH_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.SCSB_SEARCH_EXPORT))));
        authMap.put(RecapConstants.USER_ROLE_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.CREATE_USER))));
        authMap.put(RecapConstants.REQUEST_ALL_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.REQUEST_PLACE_ALL))));
        authMap.put(RecapConstants.REQUEST_ITEM_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.REQUEST_ITEMS))));
        authMap.put(RecapConstants.BARCODE_RESTRICTED_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.BARCODE_RESTRICTED))));
        authMap.put(RecapConstants.DEACCESSION_PRIVILEGE, subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(RecapConstants.DEACCESSION))));

    }
}
