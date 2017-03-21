package org.recap.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by dharmendrag on 7/12/16.
 */
public class LoginValidator {

    public boolean validate(UserForm userForm) {
        if (StringUtils.isBlank(userForm.getUsername()) || userForm.getInstitution() == 0) {
            return false;
        }
        return true;
    }
}
