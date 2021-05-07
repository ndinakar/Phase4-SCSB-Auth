package org.recap.UT.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.UT.BaseTestCaseUT;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserService;

import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dharmendrag on 1/2/17.
 */
public class AuthorizationServiceImplUT extends BaseTestCaseUT {

    @InjectMocks
    private AuthorizationServiceImpl authorizationServiceimpl;

    @Mock
    Subject subject;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Mock
    Session session;


    @Mock
    private UserService userService;

    Map<Integer,String> permissionMap=null;


    @Test
    public void getSubject(){
        String loginUser="HtcSuperAdmin:PUL";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        Subject testSubject=authorizationServiceimpl.getSubject(usernamePasswordToken);
        assertNull(testSubject);
    }

    @Test
    public void setSubject(){
        String loginUser="HtcSuperAdmin:PUL";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        authorizationServiceimpl.setSubject(usernamePasswordToken,subject);
    }

    @Test
    public void authorizationinfo(){
        UsersEntity usersEntity = createUser("HtcSuperAdmin");
        String loginUser="HtcSuperAdmin:PUL";
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        Mockito.when(userDetailsRepository.findById(usersEntity.getId())).thenReturn(Optional.of(usersEntity));
        AuthorizationInfo authorizationInfo=authorizationServiceimpl.doAuthorizationInfo(simpleAuthorizationInfo,usersEntity.getId());
        Set<String> permissions= (Set<String>) authorizationInfo.getStringPermissions();
        assertTrue(permissions.contains("EditUser"));

    }

    @Test
    public void unAuthorized(){
        UsersEntity usersEntity = createUser("HtcSuperAdmin");
        String loginUser = "HtcSuperAdmin:PUL";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        authorizationServiceimpl.unAuthorized(usernamePasswordToken);
    }
    @Test
    public void checkPrivilege(){
        int permissionId =2;
        UsersEntity usersEntity = createUser("HtcSuperAdmin");
        String loginUser = "HtcSuperAdmin:PUL";
        try{
        UsernamePasswordToken usernamePasswordToken1 = new UsernamePasswordToken(loginUser, "123");
        usernamePasswordToken1.setRememberMe(true);
        authorizationServiceimpl.checkPrivilege(usernamePasswordToken1,permissionId);
    } catch (Exception e) {
        e.printStackTrace();
    }
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
