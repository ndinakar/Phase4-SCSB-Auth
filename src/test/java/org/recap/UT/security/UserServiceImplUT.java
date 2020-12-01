package org.recap.UT.security;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.UT.BaseTestCaseUT;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.PermissionsRepository;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.security.UserServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by dharmendrag on 2/2/17.
 */
public class UserServiceImplUT extends BaseTestCaseUT {

    @InjectMocks
    public UserServiceImpl userServiceImpl;

    @Mock
    UsersEntity usersEntity;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Mock
    PermissionsRepository permissionsRepository;

    @Test
    public void testPermissions(){
        PermissionEntity permissionEntity = getPermissionEntity();
        Mockito.when(permissionsRepository.findAll()).thenReturn(Arrays.asList(permissionEntity));
        Map<Integer,String> permissionsMap=userServiceImpl.getPermissions();
        assertNotNull(permissionsMap);
    }
    @Test
    public void findUser() throws Exception{
        String loginId = "john";
        UserForm userForm = getUserForm();
        UsersEntity usersEntity = getUsersEntity();
        Mockito.when(userDetailsRepository.findByLoginId(loginId)).thenReturn(usersEntity);
        UserForm userForm1 = userServiceImpl.findUser(loginId,userForm);
        assertNotNull(userForm1);
    }

    private UsersEntity getUsersEntity() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setInstitutionId(1);
        usersEntity.setCreatedBy("test");
        usersEntity.setCreatedDate(new Date());
        usersEntity.setEmailId("test@gmail.com");
        usersEntity.setLastUpdatedBy("admin");
        usersEntity.setLastUpdatedDate(new Date());
        usersEntity.setId(1);
        usersEntity.setUserDescription("user");
        usersEntity.setLoginId("1");
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(1);
        institutionEntity.setInstitutionCode("PUL");
        institutionEntity.setInstitutionName("PUL");
        usersEntity.setInstitutionEntity(institutionEntity);
        return usersEntity;
    }

    public UserForm getUserForm() {
        UserForm userForm = new UserForm();
        userForm.setInstitution(1);
        userForm.setPassword("");
        userForm.setPasswordMatcher(true);
        userForm.setUserId(1);
        userForm.setPermissions(Set.of());
        userForm.setUserInstitution("1");
        userForm.setWrongCredentials("test");
        return userForm;
    }
    private PermissionEntity getPermissionEntity() {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setId(1);
        permissionEntity.setPermissionDesc("admin");
        permissionEntity.setPermissionName("admin");
        permissionEntity.setRoleEntityList(Arrays.asList(new RoleEntity()));
        return permissionEntity;
    }

}
