package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.junit.Test;
import org.recap.BaseTestCase;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.RolesDetailsRepositorty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by dharmendrag on 1/2/17.
 */
public class AuthorizationServiceImplUT extends BaseTestCase {



    @Autowired
    public AuthorizationService authorizationService;

    @Autowired
    public AuthorizationServiceImpl authorizationServiceimpl;

    @Autowired
    RolesDetailsRepositorty rolesDetailsRepositorty;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void setSubject(){
        UsersEntity usersEntity = createUser("HTCSuperadmin");
        String loginUser="HtcSuperAdmin:PUL";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "123");

        loginSubject(loginUser);

        Subject testSubject=authorizationServiceimpl.getSubject(usernamePasswordToken);
        assertNotNull(testSubject.getPrincipal());

    }

    @Test
    public void authorizationinfo(){
        UsersEntity usersEntity = createUser("HtcSuperAdmin");
        String loginUser="HtcSuperAdmin:PUL";
        Subject loggedInSubject = loginSubject(loginUser);
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        AuthorizationInfo authorizationInfo=authorizationServiceimpl.doAuthorizationInfo(simpleAuthorizationInfo,1);
        Set<String> permissions= (Set<String>) authorizationInfo.getStringPermissions();
        assertTrue(permissions.contains("Create User"));

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
        List<RoleEntity> roleList=new ArrayList<RoleEntity>();
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setRoleName("patron");
        roleEntity.setRoleDescription("patron from CUL");
        roleEntity.setCreatedDate(new Date());
        roleEntity.setCreatedBy("superadmin");
        roleEntity.setLastUpdatedDate(new Date());
        roleEntity.setLastUpdatedBy("superadmin");
        RoleEntity savedRole=rolesDetailsRepositorty.saveAndFlush(roleEntity);
        entityManager.refresh(savedRole);
        roleList.add(savedRole);
        usersEntity.setUserRole(roleList);

    }
}
