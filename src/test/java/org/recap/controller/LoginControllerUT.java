package org.recap.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.security.AuthenticationService;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagementService;
import org.recap.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;



import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by dharmendrag on 6/2/17.
 */
public class LoginControllerUT extends BaseTestCase {

    @Mock
    AuthenticationService authenticationService;

    @Autowired
    LoginController loginController;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    BindingResult bindingResult;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationServiceImpl authorizationService;

    UsernamePasswordToken usernamePasswordToken=null;

    Map<Integer,String> permissionMap=null;
    @Before
    public void setUp(){
        permissionMap= userService.getPermissions();
        DefaultWebSubjectContext webSubjectContext = new DefaultWebSubjectContext();
        usernamePasswordToken = new UsernamePasswordToken("rajeshtest:HTC", "rajesh123");
        webSubjectContext.setAuthenticationToken(usernamePasswordToken);
        Subject subject = securityManager.createSubject(webSubjectContext);
        Subject subject1;
        Assert.assertNotNull(subject);
        Subject loggedInSubject = securityManager.login(subject, usernamePasswordToken);
        authorizationService.setSubject(usernamePasswordToken,loggedInSubject);
        authenticationService.doAuthentication(usernamePasswordToken);
        Session session=loggedInSubject.getSession();
        session.setAttribute(RecapConstants.PERMISSION_MAP,permissionMap);
    }
    @Test
    public void testCreateSession(){
        String loginUser="rajeshtest:HTC";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");

        /*UserForm userForm = new UserForm();
        userForm.setUsername("rajeshtest");
        userForm.setPassword("rajesh123");
        userForm.setUserInstitution("1");
        userForm.setPasswordMatcher(true);
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(1);
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setInstitutionId(institutionEntity.getId());
        usersEntity.setInstitutionEntity(institutionEntity);*/

        //Mockito.when(userDetailsRepository.findByLoginIdAndInstitutionEntity(userForm.getUsername(), institutionEntity)).thenReturn(usersEntity);
       // Mockito.when(authenticationService.doAuthentication(usernamePasswordToken)).thenReturn(userForm);
        Map<String,Object> map = loginController.createSession(usernamePasswordToken,httpServletRequest,bindingResult);
        assertNotNull(map);
    }
    @Test
    public void testCreateSessionIncorrectCredentialsException(){
        String loginUser="rajeshtest";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        Map<String,Object> map = loginController.createSession(usernamePasswordToken,httpServletRequest,bindingResult);
        assertNotNull(map);
    }
    @Test
    public void logoutUser(){
        String loginUser="rajeshtest:HTC";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        loginController.logoutUser(usernamePasswordToken);
    }

    public AuthenticationService authenticationService() {
        return authenticationService;
    }
}
