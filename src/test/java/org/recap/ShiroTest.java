package org.recap;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.junit.Test;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by peris on 1/5/17.
 */

public class ShiroTest extends BaseTestCase {
    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void loginSingleUser() throws Exception {

        DefaultWebSubjectContext webSubjectContext = new DefaultWebSubjectContext();
        UsersEntity usersEntity = createUser("HtcSuperAdmin");
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        webSubjectContext.setAuthenticationToken(usernamePasswordToken);
        Subject subject = securityManager.createSubject(webSubjectContext);
        assertNotNull(subject);


        Subject loggedInSubject = securityManager.login(subject, usernamePasswordToken);
        boolean loggedInSubjectAuthenticated = loggedInSubject.isAuthenticated();
        assertNotNull(loggedInSubject);
        assertTrue(loggedInSubjectAuthenticated);
        boolean permitted = loggedInSubject.isPermitted("RequestPlace");
        loggedInSubject.getSession().setTimeout(1000);

        Thread.sleep(2000);

        try {
            loggedInSubject.isPermitted("RequestPlace");
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginConcurrentUser() throws Exception {
        DefaultWebSubjectContext webSubjectContext = new DefaultWebSubjectContext();
        UsersEntity usersEntity = createUser("HtcSuperAdmin");
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("HtcSuperAdmin:PUL", "123");
        webSubjectContext.setAuthenticationToken(usernamePasswordToken);
        Subject subject = securityManager.createSubject(webSubjectContext);
        assertNotNull(subject);
        UsernamePasswordToken usernamePasswordToken1 = new UsernamePasswordToken("Test:PUL", "12345");
        webSubjectContext.setAuthenticationToken(usernamePasswordToken1);
        Subject subject1 = securityManager.createSubject(webSubjectContext);
        assertNotNull(subject1);


        Subject loggedInSubject1 = securityManager.login(subject, usernamePasswordToken);
        assertNotNull(loggedInSubject1);
        Subject loggedInSubject2 = securityManager.login(subject1, usernamePasswordToken1);
        assertNotNull(loggedInSubject2);

        boolean loggedInSubject1Authenticated = loggedInSubject1.isAuthenticated();
        assertTrue(loggedInSubject1Authenticated);


        boolean loggedInSubject2Authenticated = loggedInSubject2.isAuthenticated();
        assertTrue(loggedInSubject2Authenticated);

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
    private PermissionEntity getPermissionEntity(){
        PermissionEntity permissionEntity = new PermissionEntity();
        permissionEntity.setPermissionDesc("Permission to edit user");
        permissionEntity.setPermissionName("EditUser");
        PermissionEntity savedPermission = permissionsRepository.saveAndFlush(permissionEntity);
        entityManager.refresh(savedPermission);
        return savedPermission;
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
}
