package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.security.realm.SimpleAuthorizationRealm;
import org.springframework.beans.factory.annotation.Autowired;

public class SimpleAuthorizationRealmUT extends BaseTestCase {
@Autowired
SimpleAuthorizationRealm simpleAuthorizationRelam;
    @Test
    public void doGetAuthenticationInfo(){

        String loginUser="john:CUL";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        //SimpleAuthorizationRealm simpleAuthorizationRelam = new SimpleAuthorizationRealm();
        //simpleAuthorizationRelam.doGetAuthenticationInfo(usernamePasswordToken);
    }
}
