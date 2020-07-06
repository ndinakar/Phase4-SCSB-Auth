package org.recap.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.Test;
import org.mockito.Mock;
import org.recap.BaseTestCase;
import org.recap.security.realm.SimpleAuthorizationRealm;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleAuthorizationRealmUT extends BaseTestCase {
@Autowired
SimpleAuthorizationRealm simpleAuthorizationRelam;
    @Test
    public void doGetAuthenticationInfo(){

        String loginUser="john:CUL";
        AuthenticationToken authenticationToken = null;
        AuthenticationInfo authenticationInfo;
       // authenticationInfo = simpleAuthorizationRelam.doGetAuthenticationInfo(authenticationToken);
    }
}
