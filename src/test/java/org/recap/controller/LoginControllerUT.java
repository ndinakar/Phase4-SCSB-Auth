package org.recap.controller;

import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.recap.BaseTestCase;
import org.recap.RecapConstants;
import org.recap.model.LoginValidator;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.security.AuthenticationService;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagementService;
import org.recap.security.UserService;
import org.recap.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;



import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by dharmendrag on 6/2/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityUtils.class)
public class LoginControllerUT {

    @Mock
    AuthenticationService authenticationService;

    @Mock
    private AuthorizationServiceImpl authorizationService;

    @InjectMocks
    LoginController loginController;

    private String recapAssistanceEmailTo;

    private String superAdminPermissionForInstitution;

    @Mock
    private InstitutionDetailsRepository institutionDetailsRepository;

    @Mock
    private UserService userService;

    @Mock
    private DefaultWebSubjectContext defaultWebSubjectContext;

    protected SecurityManager securityManager;

    /**
     * The User details repository.
     */
    @Mock
    UserDetailsRepository userDetailsRepository;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    BindingResult bindingResult;

    @Mock
    HelperUtil helperUtil;

    @Mock
    Subject subject;

    @Mock
    Session session;

    @Mock
    UserManagementService userManagementService;

    UsernamePasswordToken usernamePasswordToken = null;

    @Before
    public  void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateSession() {
        String loginUser = "rajeshtest:HTC";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        recapAssistanceEmailTo = "jancy@test.com";
        superAdminPermissionForInstitution = "HTC";
        LoginValidator loginValidator = new LoginValidator();
        Map<Integer, String> permissionMap = getPermissionMap();
        UserForm userForm = new UserForm();
        userForm.setUsername("rajeshtest");
        userForm.setPassword("rajesh123");
        userForm.setUserInstitution("CUL");
        userForm.setInstitution(2);
        userForm.setPasswordMatcher(true);
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(2);
        List<Integer> roleId = new ArrayList<>();
        roleId.add(2);
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setInstitutionId(institutionEntity.getId());
        usersEntity.setInstitutionEntity(institutionEntity);
        Object userId = 9;

        String values[] = userManagementService.userAndInstitution(usernamePasswordToken.getUsername());
        Mockito.when(helperUtil.getInstitutionIdByCode(values[1])).thenReturn(institutionEntity);
        PowerMockito.mockStatic(SecurityUtils.class);
        PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);
        Mockito.doNothing().when(subject).login(usernamePasswordToken);

        Mockito.when(subject.getPrincipal()).thenReturn(userId);
        Mockito.when(subject.getSession()).thenReturn(session);
        Mockito.when(subject.isAuthenticated()).thenReturn(true);
        Mockito.doNothing().when(authorizationService).setSubject(usernamePasswordToken, subject);
        Mockito.when(userService.getPermissions()).thenReturn(permissionMap);
        Mockito.when(userDetailsRepository.findById(9)).thenReturn(Optional.of(usersEntity));
        Mockito.when(userManagementService.getRolesForUser((Integer) subject.getPrincipal())).thenReturn(roleId);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.BARCODE_RESTRICTED)).thenReturn(1);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.REQUEST_PLACE)).thenReturn(1);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.WRITE_GCD)).thenReturn(2);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.VIEW_PRINT_REPORTS)).thenReturn(3);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.SCSB_SEARCH_EXPORT)).thenReturn(4);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.CREATE_USER)).thenReturn(5);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.REQUEST_PLACE_ALL)).thenReturn(6);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.REQUEST_ITEMS)).thenReturn(7);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.BARCODE_RESTRICTED)).thenReturn(8);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.DEACCESSION)).thenReturn(9);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.BULK_REQUEST)).thenReturn(10);
        Mockito.when(userManagementService.getPermissionId(RecapConstants.RESUBMIT_REQUEST)).thenReturn(11);
        Map<String, Object> map = loginController.createSession(usernamePasswordToken, httpServletRequest, bindingResult);
        assertNotNull(map);
    }

    private Map<Integer, String> getPermissionMap() {
        Map<Integer, String> permissionMap = new HashMap<>();
        permissionMap.put(1, RecapConstants.REQUEST_PLACE);
        permissionMap.put(2,RecapConstants.WRITE_GCD);
        permissionMap.put(3,RecapConstants.VIEW_PRINT_REPORTS);
        permissionMap.put(4,RecapConstants.SCSB_SEARCH_EXPORT);
        permissionMap.put(5,RecapConstants.CREATE_USER);
        permissionMap.put(6,RecapConstants.REQUEST_PLACE_ALL);
        permissionMap.put(7,RecapConstants.REQUEST_ITEMS);
        permissionMap.put(8,RecapConstants.BARCODE_RESTRICTED);
        permissionMap.put(9,RecapConstants.DEACCESSION);
        permissionMap.put(10,RecapConstants.BULK_REQUEST);
        permissionMap.put(11,RecapConstants.RESUBMIT_REQUEST);
        return permissionMap;
    }
    @Test
    public void testCreateSessionAuthenticationException() {
        String loginUser = "rajeshtest:HTC";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        recapAssistanceEmailTo = "jancy@test.com";
        superAdminPermissionForInstitution = "HTC";
        Map<Integer, String> permissionMap = getPermissionMap();
        UserForm userForm = new UserForm();
        userForm.setUsername("rajeshtest");
        userForm.setPassword("rajesh123");
        userForm.setUserInstitution("CUL");
        userForm.setInstitution(2);
        userForm.setPasswordMatcher(true);
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(2);
        List<Integer> roleId = new ArrayList<>();
        roleId.add(2);
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setInstitutionId(institutionEntity.getId());
        usersEntity.setInstitutionEntity(institutionEntity);
        Object userId = 9;

        String values[] = userManagementService.userAndInstitution(usernamePasswordToken.getUsername());
        Mockito.when(helperUtil.getInstitutionIdByCode(values[1])).thenReturn(institutionEntity);
        PowerMockito.mockStatic(SecurityUtils.class);
        PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);
        Mockito.doNothing().when(subject).login(usernamePasswordToken);
        Mockito.when(subject.getPrincipal()).thenReturn(userId);
        Mockito.when(subject.getSession()).thenReturn(session);
        Mockito.when(subject.isAuthenticated()).thenReturn(false);
        Map<String, Object> map = loginController.createSession(usernamePasswordToken, httpServletRequest, bindingResult);
        assertNotNull(map);
    }
    @Test
    public void testCreateSessionException() {
        String loginUser = "rajeshtest:HTC";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        recapAssistanceEmailTo = "jancy@test.com";
        superAdminPermissionForInstitution = "HTC";
        Map<Integer, String> permissionMap = null;
        UserForm userForm = new UserForm();
        userForm.setUsername("rajeshtest");
        userForm.setPassword("rajesh123");
        userForm.setUserInstitution("CUL");
        userForm.setInstitution(2);
        userForm.setPasswordMatcher(true);
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(2);
        List<Integer> roleId = new ArrayList<>();
        roleId.add(2);
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setInstitutionId(institutionEntity.getId());
        usersEntity.setInstitutionEntity(institutionEntity);
        Object userId = 9;

        String values[] = userManagementService.userAndInstitution(usernamePasswordToken.getUsername());
        Mockito.when(helperUtil.getInstitutionIdByCode(values[1])).thenReturn(institutionEntity);
        PowerMockito.mockStatic(SecurityUtils.class);
        PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);
        Mockito.doNothing().when(subject).login(usernamePasswordToken);

        Mockito.when(subject.getPrincipal()).thenReturn(userId);
        Mockito.when(subject.getSession()).thenReturn(session);
        Mockito.when(subject.isAuthenticated()).thenReturn(true);
        Mockito.doNothing().when(authorizationService).setSubject(usernamePasswordToken, subject);
        Mockito.when(userService.getPermissions()).thenReturn(permissionMap);
        Mockito.when(userDetailsRepository.findById(9)).thenReturn(Optional.of(usersEntity));
        Map<String, Object> map = loginController.createSession(usernamePasswordToken, httpServletRequest, bindingResult);
        assertNotNull(map);
    }
    @Test
    public void testCreateSessionIncorrectCredentialsException() {
        String loginUser = "rajeshtest";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        Map<String, Object> map = loginController.createSession(usernamePasswordToken, httpServletRequest, bindingResult);
        assertNotNull(map);
    }
    @Test
    public void testCreateSessionCredentialsException() {
        String loginUser = "rajeshtest";
        UsernamePasswordToken usernamePasswordToken = null;
        Map<String, Object> map = loginController.createSession(usernamePasswordToken, httpServletRequest, bindingResult);
        assertNotNull(map);
    }
    @Test
    public void testCreateSessionUnknownAccountException() {
        String loginUser = "rajeshtest:HTC";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        recapAssistanceEmailTo = "jancy@test.com";
        superAdminPermissionForInstitution = "HTC";
        Map<Integer, String> permissionMap = getPermissionMap();
        UserForm userForm = new UserForm();
        userForm.setUsername("rajeshtest");
        userForm.setPassword("rajesh123");
        userForm.setUserInstitution("CUL");
        userForm.setInstitution(2);
        userForm.setPasswordMatcher(true);
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(2);
        List<Integer> roleId = new ArrayList<>();
        roleId.add(2);
        UsersEntity usersEntity = new UsersEntity();
        usersEntity.setInstitutionId(institutionEntity.getId());
        usersEntity.setInstitutionEntity(institutionEntity);
        Object userId = 9;

        String values[] = userManagementService.userAndInstitution(usernamePasswordToken.getUsername());
        Mockito.when(helperUtil.getInstitutionIdByCode(values[1])).thenReturn(institutionEntity);
        PowerMockito.mockStatic(SecurityUtils.class);
        PowerMockito.when(SecurityUtils.getSubject()).thenReturn(subject);
        Mockito.doThrow(new UnknownAccountException()).when(subject).login(usernamePasswordToken);
        Map<String, Object> map = loginController.createSession(usernamePasswordToken, httpServletRequest, bindingResult);
        assertNotNull(map);
    }
    @Test
    public void logoutUser() {
        String loginUser = "rajeshtest:HTC";
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginUser, "rajesh123");
        Mockito.doNothing().when(authorizationService).unAuthorized(usernamePasswordToken);
        loginController.logoutUser(usernamePasswordToken);
    }
}
