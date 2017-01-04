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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dharmendrag on 4/1/17.
 */
@RestController
@RequestMapping("/collection")
@Api(value="collection authentication", description="To check the user for collection privilege", position = 2)
public class CollectionController {

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    @ApiOperation(value="collection authentication",notes="Used to Authenticate User",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "collection authentication success")})
    public boolean collection(Model model) {
        Subject subject = SecurityUtils.getSubject();
        Map<Integer, String> permissions = UserManagement.getPermissions(subject);
        if (subject.isPermitted(permissions.get(UserManagement.WRITE_GCD.getPermissionId())) ||
                subject.isPermitted(permissions.get(UserManagement.DEACCESSION.getPermissionId()))) {
            return true;
        } else {
            return UserManagement.unAuthorized(subject);
        }

    }

    private UserDetailsForm getPermissions(Subject subject, Map<Integer,String> permissions){
        UserDetailsForm userDetailsForm=new UserDetailsForm();
        userDetailsForm.setRecapUser(subject.isPermitted(permissions.get(UserManagement.REQUEST_ITEMS.getPermissionId())));
        Session session=subject.getSession();
        Integer userId=(Integer)session.getAttribute(UserManagement.USER_ID);
        userDetailsForm.setSuperAdmin(userId.equals(UserManagement.SUPER_ADMIN.getIntegerValues()));
        userDetailsForm.setLoginInstitutionId((Integer) session.getAttribute(UserManagement.USER_INSTITUTION));
        return userDetailsForm;
    }

    @RequestMapping(value="/checkCGDDeaccession",method= RequestMethod.GET)
    @ApiOperation(value="checkCGDDeaccession authentication",notes="Used to Authenticate User for collection items",position = 0,consumes = "application/json")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "reports authentication success")})
    public Map<String,Object> checkCGDDeaccession(Model model) {
        Map<String,Object> userDetail=new HashMap<String,Object>();
        Subject subject= SecurityUtils.getSubject();
        Map<Integer,String> permissions= UserManagement.getPermissions(subject);
        UserDetailsForm userDetailsForm=getPermissions(subject,permissions);
        userDetail.put("superAdmin",userDetailsForm.isSuperAdmin());
        userDetail.put("recapUser",userDetailsForm.isRecapUser());
        userDetail.put("loginInstitutionId",userDetailsForm.getLoginInstitutionId());
        return userDetail;
    }

}
