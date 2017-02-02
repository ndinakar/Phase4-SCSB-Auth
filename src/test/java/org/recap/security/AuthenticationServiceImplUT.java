package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.UserForm;
import org.recap.repository.InstitutionDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * Created by dharmendrag on 1/2/17.
 */
public class AuthenticationServiceImplUT extends BaseTestCase{

    @Autowired
    public InstitutionDetailsRepository institutionDetailsRepository;

    @Autowired
    public AuthenticationService authenticationService;

    @Autowired
    public AuthenticationServiceImpl authenticationServiceImpl;

    @Test
    public void testAuthentication()throws Exception{
        UserForm userForm = new UserForm();
        userForm.setUsername("superadmin");
        userForm.setInstitution(1);
        userForm.setPassword("12345");
        UsernamePasswordToken token=new UsernamePasswordToken(userForm.getUsername()+ UserManagement.TOKEN_SPLITER.getValue()+userForm.getInstitution(),userForm.getPassword(),true);
        UserForm returnForm=authenticationService.doAuthentication(token);

        assertEquals(Integer.valueOf(1),returnForm.getUserId());


    }


}
