package org.recap.UT.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.ScsbConstants;
import org.recap.UT.BaseTestCaseUT;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.PermissionsRepository;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.security.UserManagementService;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class UserManagementServiceUT extends BaseTestCaseUT {

    @InjectMocks
    UserManagementService userManagementService;

    @Mock
    PermissionsRepository permissionsRepository;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Mock
    Subject subject;

    @Mock
    Session session;

    @Test
    public void getPermissionId(){
        String permissionName = "Create User";
        PermissionEntity permissionEntity = getPermissionEntity();
        Mockito.when(permissionsRepository.findByPermissionName(permissionName)).thenReturn(permissionEntity);
        int id = userManagementService.getPermissionId(permissionName);
        assertNotNull(id);
    }

    private PermissionEntity getPermissionEntity() {
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setId(1);
        permissionEntity.setPermissionDesc("admin");
        permissionEntity.setPermissionName("admin");
        permissionEntity.setRoleEntityList(Arrays.asList(new RoleEntity()));
        return permissionEntity;
    }

    @Test
    public void getRolesForUser(){
        int userId = 2;
        UsersEntity usersEntity = getUsersEntity();
        Mockito.when(userDetailsRepository.findById(userId)).thenReturn(Optional.of(usersEntity));
        List<Integer> roleIds =  userManagementService.getRolesForUser(userId);
        assertNotNull(roleIds);
    }
    @Test
    public void toUserForm(){
        UserManagementService userManagementService = new UserManagementService();
        UsersEntity usersEntity = getUsersEntity();
        UserForm userForm = null;
        userManagementService.toUserForm(usersEntity,userForm);
    }
    @Test
    public void toUserFormWithoutUsersEntity(){
        UserManagementService userManagementService = new UserManagementService();
        UsersEntity usersEntity = null;
        UserForm userForm = null;
        try {
            userManagementService.toUserForm(usersEntity, userForm);
        }catch (Exception e){
            assertNotNull(e);
        }
    }

    @Test
    public void getPermissions(){
        Map<Integer, String> permissionMap = getPermissionMap();
        Mockito.when(subject.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute(ScsbConstants.PERMISSION_MAP)).thenReturn(permissionMap);
        Map<Integer,String> map = userManagementService.getPermissions(subject);
        assertNotNull(map);
    }
    private Map<Integer, String> getPermissionMap() {
        Map<Integer, String> permissionMap = new HashMap<>();
        permissionMap.put(1, ScsbConstants.REQUEST_PLACE);
        permissionMap.put(2, ScsbConstants.WRITE_GCD);
        permissionMap.put(3, ScsbConstants.VIEW_PRINT_REPORTS);
        permissionMap.put(4, ScsbConstants.SCSB_SEARCH_EXPORT);
        permissionMap.put(5, ScsbConstants.CREATE_USER);
        permissionMap.put(6, ScsbConstants.REQUEST_PLACE_ALL);
        permissionMap.put(7, ScsbConstants.REQUEST_ITEMS);
        permissionMap.put(8, ScsbConstants.BARCODE_RESTRICTED);
        permissionMap.put(9, ScsbConstants.DEACCESSION);
        permissionMap.put(10, ScsbConstants.BULK_REQUEST);
        permissionMap.put(11, ScsbConstants.RESUBMIT_REQUEST);
        return permissionMap;
    }
    private UsersEntity getUsersEntity() {
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setId(1);
        usersEntity.setInstitutionId(1);
        usersEntity.setLastUpdatedDate(new Date());
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(1);
        usersEntity.setInstitutionId(institutionEntity.getId());
        usersEntity.setInstitutionEntity(institutionEntity);
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setRoleName("admin");
        roleEntity.setRoleDescription("admin");
        roleEntity.setCreatedDate(new Date());
        usersEntity.setUserRole(Arrays.asList(roleEntity));
        return usersEntity;
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
