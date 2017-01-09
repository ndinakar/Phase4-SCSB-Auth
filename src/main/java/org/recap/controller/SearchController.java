package org.recap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.recap.security.AuthorizationServiceImpl;
import org.recap.security.UserManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dharmendrag on 4/1/17.
 */
@RestController
@RequestMapping("/search")
@Api(value="search authentication", description="To check the user for search privilege", position = 5)
public class SearchController {

    Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private AuthorizationServiceImpl authorizationService;

    @RequestMapping(value="/auth",method= RequestMethod.POST)
    @ApiOperation(value="search authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "search authentication success")})
    public boolean searchRecords(HttpServletRequest request,@RequestBody UsernamePasswordToken token) {
        return authorizationService.checkPrivilege(token,UserManagement.SCSB_SEARCH_EXPORT.getPermissionId());
    }

}
