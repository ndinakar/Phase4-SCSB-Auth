package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.jpa.InstitutionDetailsRepository;
import org.recap.repository.jpa.UserDetailsRepository;
import org.recap.util.HelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dharmendrag on 21/12/16.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;

    @Autowired
    private HelperUtil helperUtil;

    @Override
    public UserForm doAuthentication(UsernamePasswordToken token) {
        UserForm userForm = new UserForm();
        String[] user = UserManagementService.userAndInstitution(token.getUsername());
        userForm.setUsername(user[0]);
        InstitutionEntity institutionEntity = helperUtil.getInstitutionIdByCode(user[1]);
        userForm.setInstitution(institutionEntity.getId());
        userForm = getCredential(institutionEntity.getId(), user[0], userForm);
        return userForm;
    }

    private UserForm getCredential(Integer institution, String username, UserForm userForm) {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(institution);
        userForm = UserManagementService.toUserForm(userDetailsRepository.findByLoginIdAndInstitutionEntity(username, institutionEntity), userForm);
        userForm.setPasswordMatcher(true);
        return userForm;
    }



}
