package org.recap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.recap.security.UserManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by dharmendrag on 4/1/17.
 */
@RestController
@RequestMapping("/reports")
@Api(value="reports authentication", description="To check the user for report privilege", position = 3)
public class ReportsController {

    @RequestMapping(value="/auth",method= RequestMethod.GET)
    @ApiOperation(value="reports authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "reports authentication success")})
    public boolean reports(Model model) {
        Subject subject= SecurityUtils.getSubject();
        Map<Integer,String> permissions= UserManagement.getPermissions(subject);
        if(subject.isPermitted(permissions.get(UserManagement.VIEW_PRINT_REPORTS.getPermissionId()))) {
            return true;
        }else{
            return UserManagement.unAuthorized(subject);
        }

    }

}
