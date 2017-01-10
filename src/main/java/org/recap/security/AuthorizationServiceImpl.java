package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.Subject;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.UserDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static Map<String,Subject> tokenMap=new HashMap<String,Subject>();

    public Subject getSubject(UsernamePasswordToken usernamePasswordToken)
    {
        return tokenMap.get(usernamePasswordToken.getUsername());
    }

    public void setSubject(UsernamePasswordToken usernamePasswordToken,Subject subject)
    {
        tokenMap.put(usernamePasswordToken.getUsername(),subject);
    }
    public UserDetailsRepository getUserDetailsRepository() {
        return userDetailsRepository;
    }

    public void setUserDetailsRepository(UserDetailsRepository userDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
    }

    public AuthorizationInfo doAuthorizationInfo(SimpleAuthorizationInfo authorizationInfo, Integer loginId)
    {
        UsersEntity usersEntity = userDetailsRepository.findByUserId(loginId);
        String spliter= UserManagement.TOKEN_SPLITER.getValue();
        String institution=null;
        if (usersEntity == null) {
            return null;
        } else {
            for (RoleEntity role : usersEntity.getUserRole()) {
                institution=usersEntity.getInstitutionEntity().getInstitutionCode();
                authorizationInfo.addRole(role.getRoleName());
                if(role.getRoleName().equals(UserManagement.ReCAP.getValue()))
                {
                    institution="*";
                }
                for (PermissionEntity permissionEntity : role.getPermissions()) {
                    //authorizationInfo.addStringPermission(permissionEntity.getPermissionName()+spliter+institution);
                    authorizationInfo.addStringPermission(permissionEntity.getPermissionName());
                }
            }
        }
        return authorizationInfo;
    }

    public void unAuthorized(UsernamePasswordToken token)
    {
        System.out.println("Session Time Out Call");
        Subject subject=(Subject)tokenMap.get(token.getUsername());
        tokenMap.remove(token.getUsername());
        if(subject!=null && subject.getSession()!=null)
        {
        subject.logout();
        }
    }

    public boolean checkPrivilege(UsernamePasswordToken token,Integer permissionId)
    {
        Subject subject=getSubject(token);
        logger.debug("Authorization call for : "+permissionId+" & User "+token);
        Map<Integer,String> permissions= UserManagement.getPermissions(subject);
        boolean authorized=false;
        try {
            authorized = subject.isPermitted(permissions.get(permissionId));

            if (!authorized) {
                unAuthorized(token);
            }
        }catch(Exception sessionExcp)
        {
            timeOutExceptionCatch(token);
        }

        return authorized;
    }

    public boolean checkRequestPrivilege(UsernamePasswordToken token)
    {
        Subject subject=getSubject(token);
        try {
            logger.info("Authorization for request " + subject.getPrincipal());
            Map<Integer, String> permissions = UserManagement.getPermissions(subject);
            if (subject.isPermitted(permissions.get(UserManagement.REQUEST_PLACE.getPermissionId())) || subject.isPermitted(permissions.get(UserManagement.REQUEST_PLACE_ALL.getPermissionId())) ||
                    subject.isPermitted(permissions.get(UserManagement.REQUEST_ITEMS.getPermissionId()))) {
                return true;

            } else {
                unAuthorized(token);
            }
        }catch(Exception exp)
        {
            timeOutExceptionCatch(token);
        }
        return false;
    }

    public boolean checkCollectionPrivilege(UsernamePasswordToken token)
    {
        Subject subject=getSubject(token);
        try {
            logger.info("Authorization for request " + subject.getPrincipal());
            Map<Integer, String> permissions = UserManagement.getPermissions(subject);
            if (subject.isPermitted(permissions.get(UserManagement.WRITE_GCD.getPermissionId())) || subject.isPermitted(permissions.get(UserManagement.DEACCESSION.getPermissionId()))) {
                return true;
            } else {
                unAuthorized(token);
            }
        }catch(Exception sessionExcp)
        {
            timeOutExceptionCatch(token);
        }
        return false;
    }

    private void timeOutExceptionCatch(UsernamePasswordToken token){
        logger.debug("Time out Exception thrown for token "+token);
        unAuthorized(token);
    }

}
