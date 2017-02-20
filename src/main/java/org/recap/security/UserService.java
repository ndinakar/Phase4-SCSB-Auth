package org.recap.security;

import java.util.Map;

/**
 * Created by dharmendrag on 29/11/16.
 */
@FunctionalInterface
public interface UserService {


    Map<Integer,String> getPermissions();

}
