package org.recap.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by dharmendrag on 4/1/17.
 */
public class LoginControllerTest extends BaseControllerUT {

    @Autowired
    private LoginController loginController;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        this.mockMvc= MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    public void loginWithEmptyValues ()throws Exception{
        String param="";
        MvcResult mvcResult=this.mockMvc.perform(post("/authentication/login").param("requestJson",param)).andReturn();
        int status=mvcResult.getResponse().getStatus();
        String contentType=mvcResult.getResponse().getContentType();
        System.out.println("Content Type :"+contentType);

    }
}
