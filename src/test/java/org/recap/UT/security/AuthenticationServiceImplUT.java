package org.recap.UT.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.ScsbConstants;
import org.recap.UT.BaseTestCaseUT;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.security.AuthenticationServiceImpl;
import org.recap.security.UserManagementService;
import org.recap.util.HelperUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * Created by dharmendrag on 1/2/17.
 */
public class AuthenticationServiceImplUT extends BaseTestCaseUT {

    @InjectMocks
    public AuthenticationServiceImpl authenticationServiceImpl;

    @Mock
    HelperUtil helperUtil;

    @Mock
    UserDetailsRepository userDetailsRepository;

    @Test
    public void doAuthentication()throws Exception{
        UsersEntity usersEntity = createUser();
        UserForm userForm = getUserForm();
        InstitutionEntity institutionEntity = getInstitutionEntity();
        UsernamePasswordToken token=new UsernamePasswordToken(userForm.getUsername()+ ScsbConstants.TOKEN_SPLITER+"PUL",userForm.getPassword(),true);
        String[] user = UserManagementService.userAndInstitution(token.getUsername());
        Mockito.when(helperUtil.getInstitutionIdByCode(user[1])).thenReturn(institutionEntity);
        Mockito.when(userDetailsRepository.findByLoginIdAndInstitutionEntity(any(), any())).thenReturn(usersEntity);
        UserForm returnForm=authenticationServiceImpl.doAuthentication(token);

        assertEquals(userForm.getUsername(),returnForm.getUsername());
        assertEquals(userForm.getInstitution(),returnForm.getInstitution());
        assertEquals(userForm.getWrongCredentials(),returnForm.getWrongCredentials());
        assertEquals(userForm.getPermissions(),returnForm.getPermissions());
    }

    private InstitutionEntity getInstitutionEntity() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(1);
        institutionEntity.setInstitutionName("PUL");
        institutionEntity.setInstitutionCode("PUL");
        return institutionEntity;
    }

    private UserForm getUserForm() {
        UserForm userForm = new UserForm();
        userForm.setUsername("htcsuperadmin");
        userForm.setInstitution(1);
        userForm.setPassword("12345");
        return userForm;
    }

    public UsersEntity createUser(){
        UsersEntity usersEntity=new UsersEntity();
        usersEntity.setLoginId("htcsuperadmin");
        usersEntity.setEmailId("julius@example.org");
        usersEntity.setUserDescription("super admin");
        usersEntity.setInstitutionId(1);
        usersEntity.setCreatedBy("superadmin");
        usersEntity.setCreatedDate(new Date());
        usersEntity.setLastUpdatedBy("superadmin");
        usersEntity.setLastUpdatedDate(new Date());
        usersEntity.setInstitutionEntity(getInstitutionEntity());
        userRoles(usersEntity);
        return usersEntity;

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
        roleList.add(roleEntity);
        usersEntity.setUserRole(roleList);

    }


}
