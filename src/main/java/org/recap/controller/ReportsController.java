package org.recap.controller;

import io.swagger.annotations.Api;
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

/**
 * Created by dharmendrag on 4/1/17.
 */
@RestController
@RequestMapping("/reports")
@Api(value="reports authentication", description="To check the user for report privilege", position = 3)
public class ReportsController {

    @Autowired
    private AuthorizationServiceImpl authorizationService;

    @RequestMapping(value="/auth",method= RequestMethod.POST)
    @ApiOperation(value="reports authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "reports authentication success")})
    public boolean reports(@RequestBody UsernamePasswordToken usernamePasswordToken) {
        return authorizationService.checkPrivilege(usernamePasswordToken,UserManagement.VIEW_PRINT_REPORTS.getPermissionId());

    }

}
