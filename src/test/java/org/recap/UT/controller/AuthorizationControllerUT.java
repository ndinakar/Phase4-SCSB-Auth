package org.recap.UT.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.RecapConstants;
import org.recap.UT.BaseTestCaseUT;
import org.recap.controller.AuthorizationController;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagementService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by dharmendrag on 6/2/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class AuthorizationControllerUT extends BaseTestCaseUT {


    @InjectMocks
    AuthorizationController mockAuthorizationController;

    @Mock
    AuthorizationServiceImpl authorizationService;

    @Mock
    HttpServletRequest request;

    @Mock
    Session session;

    @Mock
    UserManagementService userManagementService;

    @Mock
    Subject subject;

    UsernamePasswordToken usernamePasswordToken=null;

    Map<Integer,String> permissionMap=null;


    @Test
    public void searchRecords(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.SCSB_SEARCH_EXPORT)).thenReturn(1);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,1)).thenReturn(true);
        result=mockAuthorizationController.searchRecords(request,usernamePasswordToken);
        assertTrue(result);
    }

    @Test
    public void request(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.REQUEST_PLACE)).thenReturn(2);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,2)).thenReturn(true);
        result=mockAuthorizationController.request(usernamePasswordToken);
        assertTrue(result);
    }

    @Test
    public void collection(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.WRITE_GCD)).thenReturn(3);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,3)).thenReturn(true);
        result=mockAuthorizationController.collection(usernamePasswordToken);
        assertTrue(result);
    }

    @Test
    public void checkReportsPermission(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.VIEW_PRINT_REPORTS)).thenReturn(4);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,4)).thenReturn(true);
        result=mockAuthorizationController.reports(usernamePasswordToken);
        assertTrue(result);
    }

    @Test
    public void userRoles(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.CREATE_USER)).thenReturn(5);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,5)).thenReturn(true);
        result=mockAuthorizationController.userRoles(usernamePasswordToken);
        assertTrue(result);
    }

    @Test
    public void roles(){
        boolean result=false;
        PowerMockito.mockStatic(SecurityUtils.class);
        PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);
        Mockito.doNothing().when(subject).login(usernamePasswordToken);
        Mockito.when(subject.getPrincipal()).thenReturn(9);
        Mockito.when(userManagementService.getRolesForUser((Integer) subject.getPrincipal())).thenReturn(Arrays.asList(6));
        result = mockAuthorizationController.roles(usernamePasswordToken);
        assertFalse(result);
    }
    @Test
    public void touchExistingSession(){
        boolean result=false;
        Mockito.when(authorizationService.getSubject(usernamePasswordToken)).thenReturn(subject);
        Mockito.when(subject.getSession()).thenReturn(session);
        result = mockAuthorizationController.touchExistingSession(usernamePasswordToken);
        assertTrue(result);
    }
    @Test
    public void touchExistingSessionException(){
        boolean result=false;
        Mockito.when(authorizationService.getSubject(usernamePasswordToken)).thenReturn(subject);
        Mockito.when(subject.getSession()).thenThrow(new InvalidSessionException());
        result = mockAuthorizationController.touchExistingSession(usernamePasswordToken);
        assertFalse(result);
    }
    @Test
    public void bulkRequest(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.BULK_REQUEST)).thenReturn(7);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,7)).thenReturn(true);
        result = mockAuthorizationController.bulkRequest(usernamePasswordToken);
        assertTrue(result);
    }
    @Test
    public void monitoring(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.MONITORING_REQUEST)).thenReturn(8);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,8)).thenReturn(true);
        result = mockAuthorizationController.monitoring(usernamePasswordToken);
        assertTrue(result);
    }
    @Test
    public void logging(){
        boolean result=false;
        Mockito.when(userManagementService.getPermissionId(RecapConstants.LOGGING_REQUEST)).thenReturn(9);
        Mockito.when(authorizationService.checkPrivilege(usernamePasswordToken,9)).thenReturn(true);
        result = mockAuthorizationController.logging(usernamePasswordToken);
        assertTrue(result);
    }
    public UsersEntity createUser(String loginId){
        UsersEntity usersEntity=new UsersEntity();
        usersEntity.setLoginId(loginId);
        usersEntity.setEmailId("julius@example.org");
        usersEntity.setUserDescription("super admin");
        usersEntity.setInstitutionId(1);
        usersEntity.setCreatedBy("superadmin");
        usersEntity.setCreatedDate(new Date());
        usersEntity.setLastUpdatedBy("superadmin");
        usersEntity.setLastUpdatedDate(new Date());
        userRoles(usersEntity);
        return usersEntity;
    }


    private void userRoles(UsersEntity usersEntity){
        PermissionEntity permissionEntity = getPermissionEntity();
        Set<PermissionEntity> permissionEntitySet = new HashSet<>();
        permissionEntitySet.add(permissionEntity);
        List<RoleEntity> roleList=new ArrayList<RoleEntity>();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("patron");
        roleEntity.setRoleDescription("patron from CUL");
        roleEntity.setCreatedDate(new Date());
        roleEntity.setCreatedBy("superadmin");
        roleEntity.setLastUpdatedDate(new Date());
        roleEntity.setLastUpdatedBy("superadmin");
        roleEntity.setPermissions(permissionEntitySet);
        roleList.add(roleEntity);
        usersEntity.setUserRole(roleList);

    }

    private PermissionEntity getPermissionEntity(){
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setPermissionDesc("Permission to edit user");
        permissionEntity.setPermissionName("EditUser");
        return permissionEntity;
    }

}
