package org.recap.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.subject.Subject;
import org.recap.RecapConstants;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by dharmendrag on 10/1/17.
 */
@RestController
@RequestMapping("/auth")
public class AuthorizationController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationController.class);

    @Autowired
    private AuthorizationServiceImpl authorizationService;

    /**
     * The User management service.
     */
    @Autowired
    UserManagementService userManagementService;


    /**
     * Check the privilege for the search record screen
     *
     * @param request the request
     * @param token   the token
     * @return the boolean
     */
    @PostMapping(value="/search")
    @ApiOperation(value="search authentication",notes="Used to Authenticate User",consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "search authentication success")})
    public boolean searchRecords(HttpServletRequest request, @RequestBody UsernamePasswordToken token) {
        return authorizationService.checkPrivilege(token, userManagementService.getPermissionId(RecapConstants.SCSB_SEARCH_EXPORT));
    }

    /**
     * Check the privilege for the request screen
     *
     * @param token the token
     * @return the boolean
     */
    @PostMapping(value="/request")
    @ApiOperation(value="request authentication",notes="Used to Authenticate User",consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "request authentication success")})
    public Boolean request(@RequestBody UsernamePasswordToken token) {
        return authorizationService.checkPrivilege(token,userManagementService.getPermissionId(RecapConstants.REQUEST_PLACE));
    }

    /**
     * Check the privilege for the collection screen
     *
     * @param token the token
     * @return the boolean
     */
    @PostMapping(value = "/collection")
    @ApiOperation(value="collection authentication",notes="Used to Authenticate User",consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "collection authentication success")})
    public Boolean collection(@RequestBody UsernamePasswordToken token) {
        return authorizationService.checkPrivilege(token, userManagementService.getPermissionId(RecapConstants.WRITE_GCD));

    }

    /**
     * Check the privilege for the report screen
     *
     * @param usernamePasswordToken the username password token
     * @return the boolean
     */
    @PostMapping(value="/reports")
    @ApiOperation(value="reports authentication",notes="Used to Authenticate User",consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "reports authentication success")})
    public boolean reports(@RequestBody UsernamePasswordToken usernamePasswordToken) {
        return authorizationService.checkPrivilege(usernamePasswordToken,userManagementService.getPermissionId(RecapConstants.VIEW_PRINT_REPORTS));

    }

    /**
     * Check the privilege for the user screen
     *
     * @param usernamePasswordToken the username password token
     * @return the boolean
     */
    @PostMapping(value="/userRoles")
    @ApiOperation(value="user authentication",notes="Used to Authorizer User for Users",consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "User & Role authentication success")})
    public boolean userRoles(@RequestBody UsernamePasswordToken usernamePasswordToken) {
        return authorizationService.checkPrivilege(usernamePasswordToken,userManagementService.getPermissionId(RecapConstants.CREATE_USER));

    }

    /**
     * Check the privilege for the roles screen
     *
     * @param usernamePasswordToken the username password token
     * @return the boolean
     */
    @PostMapping(value="/roles")
    @ApiOperation(value="roles authentication",notes="Used to Authorizer User for Roles",consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Role authentication success")})
    public boolean roles(@RequestBody UsernamePasswordToken usernamePasswordToken) {
        Subject subject = SecurityUtils.getSubject();
        subject.login(usernamePasswordToken);
        List<Integer> roleId = userManagementService.getRolesForUser((Integer) subject.getPrincipal());
        return roleId.contains(1);
    }

    /**
     * Check the privilege for the roles screen
     *
     * @param usernamePasswordToken the username password token
     * @return the boolean
     */
    @PostMapping(value="/touchExistingSession")
    @ApiOperation(value="touch existing session",notes="Used to touch existing session for the user",consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successfully extended the session")})
    public boolean touchExistingSession(@RequestBody UsernamePasswordToken usernamePasswordToken) {
        Subject subject = authorizationService.getSubject(usernamePasswordToken);
        try {
            subject.getSession().touch();
            return true;
        } catch (InvalidSessionException e) {
           logger.error("Invalid Session Exception",e);
        }
        return false;
    }

    @PostMapping(value="/bulkRequest")
    public boolean bulkRequest(@RequestBody UsernamePasswordToken usernamePasswordToken) {
        return authorizationService.checkPrivilege(usernamePasswordToken,userManagementService.getPermissionId(RecapConstants.BULK_REQUEST));

    }
    @PostMapping(value="/monitoring")
    public boolean monitoring(@RequestBody UsernamePasswordToken usernamePasswordToken){
        return authorizationService.checkPrivilege(usernamePasswordToken,userManagementService.getPermissionId(RecapConstants.MONITORING_REQUEST));
    }
    @PostMapping(value="/logging")
    public boolean logging(@RequestBody UsernamePasswordToken usernamePasswordToken){
        return authorizationService.checkPrivilege(usernamePasswordToken,userManagementService.getPermissionId(RecapConstants.LOGGING_REQUEST));
    }
}
