package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.junit.Test;
import org.recap.BaseTestCase;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dharmendrag on 1/2/17.
 */
public class AuthorizationServiceImplUT extends BaseTestCase {



    @Autowired
    public AuthorizationService authorizationService;

    @Autowired
    public AuthorizationServiceImpl authorizationServiceimpl;

    @Test
    public void setSubject(){
        String loginUser="superadmin:1";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");

        loginSubject(loginUser);

        Subject testSubject=authorizationServiceimpl.getSubject(usernamePasswordToken);
        assertEquals(1,testSubject.getPrincipal());

    }

    @Test
    public void authorizationinfo(){
        String loginUser="superadmin:1";
        Subject loggedInSubject = loginSubject(loginUser);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        AuthorizationInfo authorizationInfo=authorizationServiceimpl.doAuthorizationInfo(simpleAuthorizationInfo,1);
        Set<String> permissions= (Set<String>) authorizationInfo.getStringPermissions();
        assertTrue(permissions.contains("Create User"));

    }


    public Subject loginSubject(String loginUser){
        DefaultWebSubjectContext webSubjectContext = new DefaultWebSubjectContext();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        webSubjectContext.setAuthenticationToken(usernamePasswordToken);
        Subject subject = securityManager.createSubject(webSubjectContext);
        assertNotNull(subject);

        Subject loggedInSubject = securityManager.login(subject, usernamePasswordToken);
        authorizationServiceimpl.setSubject(usernamePasswordToken,loggedInSubject);
        return loggedInSubject;
    }
}
