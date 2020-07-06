package org.recap.security;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by dharmendrag on 2/2/17.
 */
public class UserServiceImplUT extends BaseTestCase{

    @Autowired
    public UserService userService;
    @Mock
    public UserServiceImpl userServiceImpl;

    @Mock
    UsersEntity usersEntity;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Test
    public void testPermissions(){
        Map<Integer,String> permissionsMap=userService.getPermissions();
        assertTrue(permissionsMap.containsKey(1));
        assertTrue(permissionsMap.containsValue("Create User"));
        assertEquals("Create User",permissionsMap.get(1));
    }
    @Test
    public void findUser() throws Exception{
        String loginId = "john";
        UserForm userForm = getUserForm();
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
        Mockito.when(userDetailsRepository.findByLoginId(loginId)).thenReturn(usersEntity);
        Mockito.doCallRealMethod().when(userServiceImpl).findUser(loginId,userForm);
        //userServiceImpl.findUser(loginId,userForm);
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
}
