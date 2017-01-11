package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.UserDetailsRepository;
import org.recap.security.realm.SCSBUserRealm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dharmendrag on 21/12/16.
 */
@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    Logger logger = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    private static Map<String, SCSBUserRealm> tokenMap = new HashMap<String, SCSBUserRealm>();

    public Subject getSubject(UsernamePasswordToken usernamePasswordToken) {
        SCSBUserRealm scsbUserRealm = tokenMap.get(usernamePasswordToken.getUsername());
        return scsbUserRealm.getSubject();
    }

    public SCSBUserRealm getSCSBUserRealm(UsernamePasswordToken usernamePasswordToken) {
        return tokenMap.get(usernamePasswordToken.getUsername());
    }

    public void setSubject(UsernamePasswordToken usernamePasswordToken, Subject subject) {

        SCSBUserRealm scsbUserRealm = new SCSBUserRealm();
        scsbUserRealm.setSubject(subject);
        scsbUserRealm.setLoggedInTime(new Date(System.currentTimeMillis()));

        tokenMap.put(usernamePasswordToken.getUsername(), scsbUserRealm);
    }

    public UserDetailsRepository getUserDetailsRepository() {
        return userDetailsRepository;
    }

    public void setUserDetailsRepository(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public AuthorizationInfo doAuthorizationInfo(SimpleAuthorizationInfo authorizationInfo, Integer loginId) {
        UsersEntity usersEntity = userDetailsRepository.findByUserId(loginId);
        if (usersEntity == null) {
            return null;
        } else {
            for (RoleEntity role : usersEntity.getUserRole()) {
                authorizationInfo.addRole(role.getRoleName());
                for (PermissionEntity permissionEntity : role.getPermissions()) {
                    authorizationInfo.addStringPermission(permissionEntity.getPermissionName());
                }
            }
        }
        return authorizationInfo;
    }

    public void unAuthorized(UsernamePasswordToken token) {
        logger.debug("Session Time Out Call");
        SCSBUserRealm scsbUserRealm = getSCSBUserRealm(token);
        Subject currentSubject = scsbUserRealm.getSubject();
        tokenMap.remove(token.getUsername());
        if (currentSubject != null && currentSubject.getSession() != null) {
            currentSubject.logout();
        }
    }

    public boolean checkPrivilege(UsernamePasswordToken token, Integer permissionId) {
        SCSBUserRealm scsbUserRealm = getSCSBUserRealm(token);
        Subject currentSubject = scsbUserRealm.getSubject();
        logger.debug("Authorization call for : " + permissionId + " & User " + token);
        Map<Integer, String> permissions = UserManagement.getPermissions(currentSubject);
        boolean authorized = false;
        try {
            currentSubject.getSession().touch();
            switch(permissionId){

                case UserManagement.EDIT_CGD_ID:{//to check Edit CGD & Deaccession
                    if (currentSubject.isPermitted(permissions.get(UserManagement.WRITE_GCD.getPermissionId())) || currentSubject.isPermitted(permissions.get(UserManagement.DEACCESSION.getPermissionId()))) {
                        authorized=true;
                    }
                    break;
                }

                case UserManagement.REQUEST_PLACE_ID:{//to check Request
                    if (currentSubject.isPermitted(permissions.get(UserManagement.REQUEST_PLACE.getPermissionId())) || currentSubject.isPermitted(permissions.get(UserManagement.REQUEST_PLACE_ALL.getPermissionId())) ||
                            currentSubject.isPermitted(permissions.get(UserManagement.REQUEST_ITEMS.getPermissionId()))) {
                        authorized=true;
                    }
                    break;
                }

                default:{
                    authorized = currentSubject.isPermitted(permissions.get(permissionId));
                    break;
                }

            }

            if (!authorized) {
                unAuthorized(token);
            }
        } catch (Exception sessionExcp) {
            timeOutExceptionCatch(token);
        }

        return authorized;
    }

    private void timeOutExceptionCatch(UsernamePasswordToken token) {
        logger.debug("Time out Exception thrown for token " + token);
        unAuthorized(token);
    }

}
