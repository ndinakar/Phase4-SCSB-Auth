package org.recap.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dharmendrag on 29/11/16.
 */
@ApiModel(value="userAuthRequest",description = "Model to show user details")
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

    public boolean isPasswordMatcher() {
        return passwordMatcher;
    }

    public void setPasswordMatcher(boolean passwordMatcher) {
        this.passwordMatcher = passwordMatcher;
    }

    public String getWrongCredentials() {
        return wrongCredentials;
    }

    public void setWrongCredentials(String wrongCredentials) {
        this.wrongCredentials = wrongCredentials;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<String> permissions) {
        this.permissions = permissions;
    }

    public int getInstitution() {
        return institution;
    }

    public void setInstitution(int institution) {
        this.institution = institution;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserInstitution() {
        return userInstitution;
    }

    public void setUserInstitution(String userInstitution) {
        this.userInstitution = userInstitution;
    }
}
