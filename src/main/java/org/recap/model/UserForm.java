package org.recap.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dharmendrag on 29/11/16.
 */
@ApiModel(value="userAuthRequest",description = "Model to show user details")
@Data
public class UserForm {

    @ApiModelProperty(name="userId",value="primary key against each user",position = 0)
    private Integer userId;

    @ApiModelProperty(name="username",value="Login Id or login name",position = 1)
    private String username;

    @ApiModelProperty(name="password",value="password for login",position = 2)
    private String password;

    @ApiModelProperty(name="institution",value="User's Institution",position = 3 , allowableValues = "1,2,3")
    private int institution;

    private String userInstitution;

    private Set<String> permissions=new HashSet<>();

    private String wrongCredentials;

    private boolean passwordMatcher;

    /**
     * Gets permissions.
     *
     * @return the permissions
     */
    public Set<String> getPermissions() {
        return permissions;
    }

    /**
     * Sets permissions.
     *
     * @param permissions the permissions
     */
    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

}
