package org.recap.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dharmendrag on 10/1/17.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthorizationServiceImpl authorizationService;


    @RequestMapping(value="/search",method= RequestMethod.POST)
    @ApiOperation(value="search authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "search authentication success")})
    public boolean searchRecords(HttpServletRequest request, @RequestBody UsernamePasswordToken token) {
        return authorizationService.checkPrivilege(token, UserManagement.SCSB_SEARCH_EXPORT.getPermissionId());
    }

    @RequestMapping(value="/request",method= RequestMethod.POST)
    @ApiOperation(value="request authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "request authentication success")})
    public Boolean request(@RequestBody UsernamePasswordToken token) {
        return authorizationService.checkPrivilege(token, UserManagement.REQUEST_PLACE_ID);
    }

    @RequestMapping(value = "/collection", method = RequestMethod.POST)
    @ApiOperation(value="collection authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "collection authentication success")})
    public Boolean collection(@RequestBody UsernamePasswordToken token) {
        return authorizationService.checkPrivilege(token, UserManagement.EDIT_CGD_ID);

    }
    @RequestMapping(value="/reports",method= RequestMethod.POST)
    @ApiOperation(value="reports authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "reports authentication success")})
    public boolean reports(@RequestBody UsernamePasswordToken usernamePasswordToken) {
        return authorizationService.checkPrivilege(usernamePasswordToken,UserManagement.VIEW_PRINT_REPORTS.getPermissionId());

    }


}
