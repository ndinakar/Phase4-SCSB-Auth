package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.PermissionsRepository;
import org.recap.repository.jpa.RolesDetailsRepositorty;
import org.recap.repository.jpa.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dharmendrag on 1/2/17.
 */
public class AuthorizationServiceImplUT extends BaseTestCase {



    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private AuthorizationServiceImpl authorizationServiceimpl;

    @Mock
    private AuthorizationServiceImpl mockedauthorizationServiceimpl;

    @Autowired
    private RolesDetailsRepositorty rolesDetailsRepositorty;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Mock
    Subject subject;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Mock
    Session session;

    @Mock
    UsernamePasswordToken usernamePasswordToken;

    @Test
    public void setSubject(){
        String loginUser="john";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        Subject testSubject=authorizationServiceimpl.getSubject(usernamePasswordToken);

    }

    @Test
    public void authorizationinfo(){
        int loginId = 2;
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setLoginId("John");
        usersEntity.setUserDescription("test");
        usersEntity.setId(2);
        usersEntity.setLastUpdatedDate(new Date());
        usersEntity.setInstitutionId(1);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        Mockito.when(userDetailsRepository.findById(loginId)).thenReturn(Optional.empty());
        Mockito.doCallRealMethod().when(mockedauthorizationServiceimpl).doAuthorizationInfo(simpleAuthorizationInfo,loginId);
      //  AuthorizationInfo authorizationInfo=mockedauthorizationServiceimpl.doAuthorizationInfo(simpleAuthorizationInfo,loginId);
        //Set<String> permissions= (Set<String>) authorizationInfo.getStringPermissions();
       // assertTrue(permissions.contains("EditUser"));

    }

    @Test
    public void unAuthorized(){
        String loginUser = "john";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        authorizationServiceimpl.unAuthorized(usernamePasswordToken);
    }
    @Test
    public void checkPrivilege(){
        int permissionId =2;
        String loginUser = "john";
        UsernamePasswordToken usernamePasswordToken1 = new UsernamePasswordToken(loginUser, "123");
        usernamePasswordToken1.setRememberMe(true);
        Mockito.when(subject.getSession()).thenReturn(session);
        Mockito.when(session.getAttribute(RecapConstants.PERMISSION_MAP)).thenReturn("permissionsMap");
//        authorizationServiceimpl.checkPrivilege(usernamePasswordToken1,permissionId);
    }
    public Subject loginSubject(String loginUser){
        DefaultWebSubjectContext webSubjectContext = new DefaultWebSubjectContext();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");
        webSubjectContext.setAuthenticationToken(usernamePasswordToken);
        Subject subject = securityManager.createSubject(webSubjectContext);
        assertNotNull(subject);

        Subject loggedInSubject = securityManager.login(subject, usernamePasswordToken);
        authorizationServiceimpl.setSubject(usernamePasswordToken,loggedInSubject);
        return loggedInSubject;
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

        UsersEntity savedUser=userRepo.saveAndFlush(usersEntity);
        entityManager.refresh(savedUser);

        assertEquals(usersEntity.getLoginId(),savedUser.getLoginId());

        UsersEntity byLoginId=userRepo.findByLoginId("HtcSuperAdmin");
        return byLoginId;

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
        RoleEntity savedRole=rolesDetailsRepositorty.saveAndFlush(roleEntity);
        entityManager.refresh(savedRole);
        roleList.add(savedRole);
        usersEntity.setUserRole(roleList);

    }

    private PermissionEntity getPermissionEntity(){
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setPermissionDesc("Permission to edit user");
        permissionEntity.setPermissionName("EditUser");
        PermissionEntity savedPermission = permissionsRepository.saveAndFlush(permissionEntity);
        entityManager.refresh(savedPermission);
        return savedPermission;
    }
}
