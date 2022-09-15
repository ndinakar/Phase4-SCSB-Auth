package org.recap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbConstants;
import org.recap.model.LoginValidator;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagementService;
import org.recap.security.UserService;
import org.recap.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.*;


/**
 * Created by dharmendrag on 25/11/16.
 */
@Slf4j
@RestController
@RequestMapping(value = "/userAuth")
@Api(value = "userAuth")
public class LoginController {


    @Value("${" + PropertyKeyConstants.SCSB_EMAIL_ASSIST_TO + "}")
    private String scsbAssistanceEmailTo;

    @Value("${" + PropertyKeyConstants.SUPERADMIN_PERMISSION_INSTITUTION + "}")
    private String superAdminPermissionForInstitution;

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;

    private LoginValidator loginValidator = new LoginValidator();

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationServiceImpl authorizationService;

    @Autowired
    private DefaultWebSubjectContext defaultWebSubjectContext;

    /**
     * The User details repository.
     */
    @Autowired
    UserDetailsRepository userDetailsRepository;

    /**
     * The User management service.
     */
    @Autowired
    UserManagementService userManagementService;

    /**
     * The Helper util.
     */
    @Autowired
    HelperUtil helperUtil;

    /**
     * Create session for the requested user
     *
     * @param token   the token
     * @param request the request
     * @param error   the error
     * @return the map of the authorization values
     */
    @PostMapping(value = "/authService")
    @ApiOperation(value = "authService", notes = "Used to Authenticate User", consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Session created successfully")})
    public Map<String, Object> createSession(@RequestBody UsernamePasswordToken token, HttpServletRequest request, BindingResult error) {
        UserForm userForm = new UserForm();
        Map<String, Object> authMap = new HashMap<>();
        try {
            if (token == null) {
                throw new CredentialsException(ScsbConstants.ERROR_USER_TOKEN_EMPTY);
            }
            String[] values = UserManagementService.userAndInstitution(token.getUsername());
            boolean isValid = false;
            if (values != null) {
                userForm.setUsername(values[0]);
                InstitutionEntity institutionEntity = helperUtil.getInstitutionIdByCode(values[1]);
                userForm.setInstitution(institutionEntity.getId());
                userForm.setUserInstitution(institutionEntity.getInstitutionCode());
                isValid = loginValidator.validate(userForm);
            }
            if (!isValid) {
                throw new IncorrectCredentialsException(ScsbConstants.ERROR_USER_TOKEN_VALIDATION_FAILED);
            }
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            if (!subject.isAuthenticated()) {
                throw new AuthenticationException(ScsbConstants.ERROR_SUBJECT_AUTHENTICATION_FAILED);
            }
            authorizationService.setSubject(token, subject);
            Map<Integer, String> permissionMap = userService.getPermissions();
            getPermissionsForUI(subject, authMap, permissionMap);
            authMap.put(ScsbConstants.USER_AUTHENTICATION, true);
            authMap.put(ScsbConstants.USER_NAME, userForm.getUsername());
            authMap.put(ScsbConstants.USER_INSTITUTION, userForm.getInstitution());
            authMap.put(ScsbConstants.USER_ID, subject.getPrincipal());
            List<String> rolesList = userManagementService.getRolesListForUser((Integer) subject.getPrincipal());
            boolean superAdminUser = (rolesList.contains(ScsbConstants.SUPER_ADMIN_ROLE) && superAdminPermissionForInstitution.contains(userForm.getUserInstitution())) ? Boolean.TRUE : Boolean.FALSE;
            authMap.put(ScsbConstants.SUPER_ADMIN_USER, superAdminUser);
            authMap.put(ScsbConstants.REPOSITORY,rolesList.contains(ScsbConstants.REPOSITORY_ROLE) );
            authMap.put(ScsbConstants.USER_ADMINISTRATOR,rolesList.contains(ScsbConstants.USER_ADMINISTRATOR_ROLE));
            Collections.unmodifiableMap(authMap);
            Session session = subject.getSession();
            session.setAttribute(ScsbConstants.PERMISSION_MAP, permissionMap);
            session.setAttribute(ScsbConstants.USER_ID, subject.getPrincipal());
        } catch (UnknownAccountException uae) {
            log.debug("Unknown Account Exception");
            log.error(ScsbConstants.EXCEPTION_IN_AUTHENTICATION, uae);
            authMap.put(ScsbConstants.USER_AUTHENTICATION, false);
            authMap.put(ScsbConstants.USER_AUTH_ERRORMSG, MessageFormat.format(ScsbConstants.ERROR_MESSAGE_USER_NOT_AVAILABLE, scsbAssistanceEmailTo, scsbAssistanceEmailTo));
        } catch (IncorrectCredentialsException ice) {
            log.debug("Unknown Account Exception");
            log.error(ScsbConstants.EXCEPTION_IN_AUTHENTICATION, ice);
            authMap.put(ScsbConstants.USER_AUTHENTICATION, false);
            authMap.put(ScsbConstants.USER_AUTH_ERRORMSG, ScsbConstants.ERROR_AUTHENTICATION_FAILED);
        } catch (CredentialsException ce) {
            log.debug("Credentials exception");
            log.error(ScsbConstants.EXCEPTION_IN_AUTHENTICATION, ce);
            authMap.put(ScsbConstants.USER_AUTHENTICATION, false);
            authMap.put(ScsbConstants.USER_AUTH_ERRORMSG, ScsbConstants.ERROR_AUTHENTICATION_FAILED);
        } catch (AuthenticationException ae) {
            log.debug("Authentication exception");
            log.error(ScsbConstants.EXCEPTION_IN_AUTHENTICATION, ae);
            authMap.put(ScsbConstants.USER_AUTHENTICATION, false);
            authMap.put(ScsbConstants.USER_AUTH_ERRORMSG, ScsbConstants.ERROR_AUTHENTICATION_FAILED);
        } catch (RuntimeException e) {
            log.error(ScsbConstants.EXCEPTION_IN_AUTHENTICATION, e);
            authMap.put(ScsbConstants.USER_AUTHENTICATION, false);
            authMap.put(ScsbConstants.USER_AUTH_ERRORMSG, e.getMessage());
        }
        return authMap;
    }

    /**
     * Logout user from the Shiro.
     *
     * @param token the token
     */
    @PostMapping(value = "/logout")
    public boolean logoutUser(@RequestBody UsernamePasswordToken token) {
        log.info("Subject Logged out");
        return authorizationService.unAuthorized(token);
    }


    private void getPermissionsForUI(Subject subject,Map<String,Object> authMap,Map<Integer,String> permissionMap){
        authMap.put(ScsbConstants.REQUEST_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.REQUEST_PLACE))));
        authMap.put(ScsbConstants.COLLECTION_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.WRITE_GCD))));
        authMap.put(ScsbConstants.REPORTS_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.VIEW_PRINT_REPORTS))));
        authMap.put(ScsbConstants.SEARCH_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.SCSB_SEARCH_EXPORT))));
        authMap.put(ScsbConstants.USER_ROLE_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.CREATE_USER))));
        authMap.put(ScsbConstants.REQUEST_ALL_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.REQUEST_PLACE_ALL))));
        authMap.put(ScsbConstants.REQUEST_ITEM_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.REQUEST_ITEMS))));
        authMap.put(ScsbConstants.BARCODE_RESTRICTED_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.BARCODE_RESTRICTED))));
        authMap.put(ScsbConstants.DEACCESSION_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.DEACCESSION))));
        authMap.put(ScsbConstants.BULK_REQUEST_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.BULK_REQUEST))));
        authMap.put(ScsbConstants.RESUBMIT_REQUEST_PRIVILEGE,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.RESUBMIT_REQUEST))));
        authMap.put(ScsbConstants.MONITORING,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.MONITORING_PERMISSION_NAME))));
        authMap.put(ScsbConstants.LOGGING,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.LOGGING_PERMISSION_NAME))));
        authMap.put(ScsbConstants.DATA_EXPORT,subject.isPermitted(permissionMap.get(userManagementService.getPermissionId(ScsbConstants.DATAEXPORT_PERMISSION_NAME))));
    }

}
