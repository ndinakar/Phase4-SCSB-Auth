package org.recap.security;

import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.recap.ScsbConstants;
import org.recap.model.UserForm;
import org.recap.model.jpa.PermissionEntity;
import org.recap.model.jpa.RoleEntity;
import org.recap.model.jpa.UsersEntity;
import org.recap.repository.jpa.PermissionsRepository;
import org.recap.repository.jpa.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by akulak on 27/2/17.
 */
@Service
public class UserManagementService {

    /**
     * The User details repository.
     */
    @Autowired
    UserDetailsRepository userDetailsRepository;

    /**
     * The Permissions repository.
     */
    @Autowired
    PermissionsRepository permissionsRepository;

    /**
     * Get roles for user Id.
     *
     * @param userId the user id
     * @return the list of role ids
     */
    public List<Integer> getRolesForUser(Integer userId){
        List<Integer> roleIds = new ArrayList<>();
        Optional<UsersEntity> usersEntity = userDetailsRepository.findById(userId);
        if(usersEntity.isPresent()) {
            List<RoleEntity> userRole = usersEntity.get().getUserRole();
            for (RoleEntity roleEntity : userRole) {
                Integer roleId = roleEntity.getId();
                roleIds.add(roleId);
            }
        }
        return roleIds;
    }

    /**
     * Get permission id integer.
     *
     * @param permissionName the permission name
     * @return the permission id
     */
    public Integer getPermissionId(String permissionName){
        PermissionEntity permissionEntity = permissionsRepository.findByPermissionName(permissionName);
        return  permissionEntity.getId();
    }

    /**
     * Split the User and institution from the token.
     *
     * @param token the token
     * @return the array with the value of user and institution.
     */
    public static final String[] userAndInstitution(String token)
    {
        String[] values=new String[2];
        if(token.contains(ScsbConstants.TOKEN_SPLITER))
        {
            values=token.split(ScsbConstants.TOKEN_SPLITER);
        }else
        {
            return null;
        }
        return values;
    }


    /**
     * Create UserForm with details.
     *
     * @param userEntity the user entity
     * @param userForm   the user form
     * @return the UserForm
     */
    public static UserForm toUserForm(UsersEntity userEntity, UserForm userForm) {
        if (userEntity == null) {
            throw new UnknownAccountException(ScsbConstants.ERROR_USER_NOT_AVAILABLE);
        }
        if (userForm == null) {
            userForm = new UserForm();
        }
        userForm.setUserId(userEntity.getId());
        userForm.setUsername(userEntity.getLoginId());
        userForm.setInstitution(userEntity.getInstitutionEntity().getId());
        userForm.setUserInstitution(userEntity.getInstitutionEntity().getInstitutionCode());
        return userForm;
    }

    /**
     * Get permissions details
     *
     * @param subject the subject
     * @return the map of permission values
     */
    public static Map<Integer,String> getPermissions(Subject subject){
        Session session=subject.getSession();
        return (Map<Integer,String>)session.getAttribute(ScsbConstants.PERMISSION_MAP);
    }
}
