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
import org.recap.model.LoginValidator;
import org.recap.model.UserForm;
import org.recap.security.UserManagement;
import org.recap.security.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by dharmendrag on 25/11/16.
 */
@RestController
@RequestMapping(value="/authentication")
@Api(value="authentication", description="To authenticate user", position = 1)
public class LoginController {

    Logger logger = LoggerFactory.getLogger(LoginController.class);

    private LoginValidator loginValidator=new LoginValidator();

    @Autowired
    private UserService userService;



    @RequestMapping(value="/login",method= RequestMethod.POST)
    @ApiOperation(value="authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Session created successfully")})
    public Map<String,Object> createSession(@RequestBody UserForm userForm, HttpServletRequest request, Model model, BindingResult error){
        loginValidator.validate(userForm,error);
        Map<Integer,String> permissionMap=null;
        Map<String,Object> authMap=new HashMap<String,Object>();
        if(userForm==null){
            authMap.put(UserManagement.USER_AUTHENTICATION,false);
        }
        try
        {
            if(error.hasErrors())
            {
                logger.debug("Login Screen validation failed");
                authMap.put("authentication",false);
                return authMap;
            }
            UsernamePasswordToken token = new UsernamePasswordToken(userForm.getUsername()+ UserManagement.TOKEN_SPLITER.getValue()+userForm.getInstitution(),userForm.getPassword(),false);
            Subject subject=SecurityUtils.getSubject();
            subject.login(token);
            if(!subject.isAuthenticated())
            {
                throw new AuthenticationException("Subject Authtentication Failed");
            }
            permissionMap=userService.getPermissions();
            authMap.put(UserManagement.USER_AUTHENTICATION,true);
            authMap.put("userName",userForm.getUsername());
            authMap.put(UserManagement.USER_INSTITUTION,userForm.getInstitution());
            authMap.put(UserManagement.USER_ID,subject.getPrincipal());
            authMap.put(UserManagement.REQUEST_PRIVILEGE,subject.isPermitted(permissionMap.get(UserManagement.REQUEST_PLACE.getPermissionId())));
            authMap.put(UserManagement.COLLECTION_PRIVILEGE,subject.isPermitted(permissionMap.get(UserManagement.WRITE_GCD.getPermissionId())));
            authMap.put(UserManagement.REPORTS_PRIVILEGE,subject.isPermitted(permissionMap.get(UserManagement.VIEW_PRINT_REPORTS.getPermissionId())));
            authMap.put(UserManagement.SEARCH_PRIVILEGE,subject.isPermitted(permissionMap.get(UserManagement.SCSB_SEARCH_EXPORT.getPermissionId())));
            authMap.put(UserManagement.USER_ROLE_PRIVILEGE,subject.isPermitted(permissionMap.get(UserManagement.CREATE_USER.getPermissionId())));

            Session session=subject.getSession(true);
            session.setAttribute("userName",userForm.getUsername());
            session.setAttribute(UserManagement.USER_INSTITUTION,userForm.getInstitution());
            session.setAttribute("userForm",userForm);
            session.setAttribute(UserManagement.USER_ID,subject.getPrincipal());
            session.setAttribute(UserManagement.permissionsMap, Collections.unmodifiableMap(permissionMap));

        }
        catch(AuthenticationException e)
        {
            logger.debug("Authentication exception");
            logger.error("Exception in authentication : "+e.getMessage());
            error.rejectValue("wrongCredentials","error.invalid.credentials","Invalid Credentials");
            authMap.put(UserManagement.USER_AUTHENTICATION,false);
        }
        catch(Exception e)
        {
            logger.error("Exception occured in authentication : "+e.getLocalizedMessage());
            authMap.put(UserManagement.USER_AUTHENTICATION,false);
        }

            return authMap;

    }

    @RequestMapping(value="/logout",method=RequestMethod.GET)
    public void logoutUser(){
        logger.info("Subject Logged out");
        SecurityUtils.getSubject().logout();
    }


}
