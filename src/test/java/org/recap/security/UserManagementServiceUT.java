package org.recap.security;

import org.junit.Test;
import org.mockito.Mock;
import org.recap.BaseTestCase;
import org.springframework.beans.factory.annotation.Autowired;

import javax.security.auth.Subject;
import java.util.List;

import static org.junit.Assert.assertNotNull;
public class UserManagementServiceUT extends BaseTestCase {

    @Autowired
    UserManagementService userManagementService;

    @Test
    public void getPermissionId(){
        String permissionName = "Create User";
        int id = userManagementService.getPermissionId(permissionName);
        assertNotNull(id);
    }
    @Test
    public void getRolesForUser(){
        int userId = 2;
        List<Integer> roleIds =  userManagementService.getRolesForUser(userId);
        assertNotNull(roleIds);
    }
}
