package org.recap.security;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.recap.model.UserForm;

/**
 * Created by dharmendrag on 21/12/16.
 */
@FunctionalInterface
public interface AuthenticationService {

    UserForm doAuthentication(UsernamePasswordToken token);

}
