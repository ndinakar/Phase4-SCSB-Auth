package org.recap.UT.model;

import org.junit.Test;
import org.recap.model.LoginValidator;
import org.recap.model.UserForm;

import static org.junit.Assert.assertNotNull;

public class LoginValidatorUT {



    @Test
    public void validate(){
        LoginValidator loginValidator = new LoginValidator();
        UserForm userForm = new UserForm();
        userForm.setInstitution(1);
        userForm.setPassword("123");
        userForm.setUsername("john");
        userForm.setUserId(1);
        userForm.setUserInstitution("1");
        Boolean result = loginValidator.validate(userForm);
        assertNotNull(result);
    }
}
