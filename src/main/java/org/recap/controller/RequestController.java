package org.recap.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.recap.model.UserDetailsForm;
import org.recap.security.UserManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rajeshbabuk on 13/10/16.
 */

@RestController
@RequestMapping("/request")
@Api(value="request authentication", description="To check the user for request privilege", position = 4)
public class RequestController {

    Logger logger = LoggerFactory.getLogger(RequestController.class);


    @RequestMapping(value="/auth",method= RequestMethod.GET)
    @ApiOperation(value="request authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "request authentication success")})
    public boolean request( Model model) {
        Subject subject= SecurityUtils.getSubject();
        Map<Integer,String> permissions= UserManagement.getPermissions(subject);
        UserDetailsForm userDetailsForm=getPermissions(subject,permissions);
        if(subject.isPermitted(permissions.get(UserManagement.REQUEST_PLACE.getPermissionId())) || subject.isPermitted(permissions.get(UserManagement.REQUEST_PLACE_ALL.getPermissionId())) ||
                userDetailsForm.isRecapUser()) {
            return true;

        }else{
            return UserManagement.unAuthorized(subject);
        }
    }

    private UserDetailsForm getPermissions(Subject subject,Map<Integer,String> permissions){
        UserDetailsForm userDetailsForm=new UserDetailsForm();
        userDetailsForm.setRecapUser(subject.isPermitted(permissions.get(UserManagement.REQUEST_ITEMS.getPermissionId())));
        Session session=subject.getSession();
        Integer userId=(Integer)session.getAttribute(UserManagement.USER_ID);
        userDetailsForm.setSuperAdmin(userId.equals(UserManagement.SUPER_ADMIN.getIntegerValues()));
        userDetailsForm.setLoginInstitutionId((Integer) session.getAttribute(UserManagement.USER_INSTITUTION));
        return userDetailsForm;
    }

    @RequestMapping(value="/checkRequest",method= RequestMethod.GET)
    @ApiOperation(value="checkRequest authentication",notes="Used to Authenticate User for request items",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "reports authentication success")})
    public Map<String,Object> checkRequest(Model model) {
        Map<String,Object> userDetail=new HashMap<String,Object>();
        Subject subject= SecurityUtils.getSubject();
        Map<Integer,String> permissions= UserManagement.getPermissions(subject);
        UserDetailsForm userDetailsForm=getPermissions(subject,permissions);
        userDetail.put("privateCGD",3);
        userDetail.put("superAdmin",userDetailsForm.isSuperAdmin());
        userDetail.put("recapUser",userDetailsForm.isRecapUser());
        userDetail.put("loginInstitutionId",userDetailsForm.getLoginInstitutionId());
        return userDetail;
    }



}
