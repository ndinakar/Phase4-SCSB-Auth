package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.recap.model.UserForm;
import org.recap.model.jpa.InstitutionEntity;
import org.recap.repository.InstitutionDetailsRepository;
import org.recap.repository.UserDetailsRepository;
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
    UserManagementService userManagementService;

    @Autowired
    private InstitutionDetailsRepository institutionDetailsRepository;

    public UserForm doAuthentication(UsernamePasswordToken token) throws Exception {
        UserForm userForm = new UserForm();
        String[] user = userManagementService.userAndInstitution(token.getUsername());
        userForm.setUsername(user[0]);
        userForm.setInstitution(Integer.valueOf(user[1]));
        userForm = (getCredential(Integer.valueOf(user[1]), user[0], userForm));
        return userForm;
    }

    private UserForm getCredential(Integer institution,String username,UserForm userForm) throws Exception {
        try {
            InstitutionEntity institutionEntity = new InstitutionEntity();
            institutionEntity.setInstitutionId(institution);
            userForm = UserManagementService.toUserForm(userDetailsRepository.findByLoginIdAndInstitutionEntity(username, institutionEntity), userForm);
            userForm.setPasswordMatcher(true);
        }catch(Exception e)
        {
            throw new Exception(username+":"+institution+" was not available in SCSB database");
        }
        return userForm;
    }



}
