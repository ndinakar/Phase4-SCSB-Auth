package org.recap.UT.model;

import org.junit.Test;
import org.recap.IT.BaseTestCase;
import org.recap.model.UserForm;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserFormUT {

    @Test
    public void testUserForm(){
        UserForm userForm = new UserForm();
        userForm.setInstitution(1);
        userForm.setPassword("admin");
        userForm.setPasswordMatcher(true);
        userForm.setUsername("john");
        userForm.setUserId(1);
        userForm.setPermissions(Set.of());
        userForm.setUserInstitution("1");
        userForm.setWrongCredentials("test");
        userForm.hashCode();
        userForm.equals(userForm.getUserId());
        userForm.toString();
        assertNotNull(userForm.getInstitution());
        assertNotNull(userForm.getPassword());
        assertNotNull(userForm.getWrongCredentials());
        assertNotNull(userForm.getUserId());
        assertNotNull(userForm.getUserInstitution());
        assertNotNull(userForm.getUsername());
        assertEquals(true,userForm.isPasswordMatcher());

    }

}
