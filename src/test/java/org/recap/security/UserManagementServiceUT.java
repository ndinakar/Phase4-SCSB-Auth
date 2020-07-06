package org.recap.security;

import org.junit.Test;
import org.mockito.Mock;
import org.recap.BaseTestCase;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertNull;
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
    @Test
    public void toUserForm(){
        UserManagementService userManagementService = new UserManagementService();
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(1);
        usersEntity.setInstitutionId(1);
        usersEntity.setLastUpdatedDate(new Date());
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(1);
        usersEntity.setInstitutionId(institutionEntity.getId());
        usersEntity.setInstitutionEntity(institutionEntity);
        UserForm userForm = null;
        userManagementService.toUserForm(usersEntity,userForm);
    }
    @Test
    public void toUserFormUnknownAccountException(){
        UserManagementService userManagementService = new UserManagementService();
        try{
            UsersEntity usersEntity = null;
            UserForm userForm = new UserForm();
            userForm.setUsername("john");
            userForm.setPassword("123");
            userManagementService.toUserForm(usersEntity,userForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void userAndInstitutionWithoutSpliter(){
        UserManagementService userManagementService = new UserManagementService();
        String token = "john";
        String[] values = userManagementService.userAndInstitution(token);
        assertNull(values);
    }
}
