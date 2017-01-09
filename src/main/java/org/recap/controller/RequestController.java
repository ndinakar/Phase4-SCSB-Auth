package org.recap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.recap.security.AuthorizationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by rajeshbabuk on 13/10/16.
 */

@RestController
@RequestMapping("/request")
@Api(value="request authentication", description="To check the user for request privilege", position = 4)
public class RequestController {

    Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private AuthorizationServiceImpl authorizationService;


    @RequestMapping(value="/auth",method= RequestMethod.POST)
    @ApiOperation(value="request authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "request authentication success")})
    public Boolean request(@RequestBody UsernamePasswordToken token) {
        return authorizationService.checkRequestPrivilege(token);
    }




}
