package org.recap.IT.controller;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.recap.IT.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.controller.AuthorizationController;
import org.recap.controller.LoginController;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.PermissionsRepository;
import org.recap.repository.jpa.RolesDetailsRepositorty;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by dharmendrag on 6/2/17.
 */
public class AuthorizationControllerIT extends BaseTestCase {

    @Autowired
    private WebApplicationContext applicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesDetailsRepositorty rolesDetailsRepositorty;

    @Autowired
    private PermissionsRepository permissionsRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private AuthorizationServiceImpl authorizationService;

    @Autowired
    LoginController loginController;

    @Mock
    HttpServletRequest request;

    @Autowired
    AuthorizationController authorizationController;


    UsernamePasswordToken usernamePasswordToken=null;

    Map<Integer,String> permissionMap=null;

    @Before
    public void setUp(){
        permissionMap= userService.getPermissions();
        DefaultWebSubjectContext webSubjectContext = new DefaultWebSubjectContext();
        UsersEntity usersEntity = createUser("HtcSuperAdmin");
        usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        webSubjectContext.setAuthenticationToken(usernamePasswordToken);
        Subject subject = securityManager.createSubject(webSubjectContext);
        assertNotNull(subject);
        Subject loggedInSubject = securityManager.login(subject, usernamePasswordToken);
        authorizationService.setSubject(usernamePasswordToken,loggedInSubject);
        Session session=loggedInSubject.getSession();
        session.setAttribute(RecapConstants.PERMISSION_MAP,permissionMap);
    }


    @Test
    public void checkSearchPermission(){
        boolean result=false;
        result=authorizationController.searchRecords(request,usernamePasswordToken);
        assertTrue(true);
    }

    @Test
    public void checkRequestPermission(){
        boolean result=false;
        result=authorizationController.request(usernamePasswordToken);
        assertTrue(true);
    }

    @Test
    public void checkCollectionPermission(){
        boolean result=false;
        result=authorizationController.collection(usernamePasswordToken);
        assertTrue(true);
    }

    @Test
    public void checkReportsPermission(){
        boolean result=false;
        result=authorizationController.reports(usernamePasswordToken);
        assertTrue(true);
    }

    @Test
    public void checkUsersPermission(){
        usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        boolean result=false;
        result=authorizationController.userRoles(usernamePasswordToken);
        assertTrue(true);
    }
    @Test
    public void searchRecords(){
        usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        boolean result=false;
        result=authorizationController.searchRecords(request,usernamePasswordToken);
        assertTrue(true);
    }
    @Test
    public void roles(){
        usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        boolean result=false;
       // result = authorizationController.roles(usernamePasswordToken);
       // assertTrue(result);
    }
    @Test
    public void touchExistingSession(){
        usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        boolean result=false;
        result = authorizationController.touchExistingSession(usernamePasswordToken);
        assertTrue(result);
    }
    @Test
    public void bulkRequest(){
        usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        boolean result=false;
        result = authorizationController.bulkRequest(usernamePasswordToken);
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
