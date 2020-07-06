package org.recap.config;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertTrue;

/**
 * Created by hemalathas on 25/1/17.
 */
public class SwaggerInterceptorUT extends BaseTestCase {

    @Autowired
    SwaggerInterceptor swaggerInterceptor;

    @Mock
    HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Test
    public void testPreHandle() throws Exception {
        httpServletRequest.setAttribute("api_key","recap");
        boolean continueExport = swaggerInterceptor.preHandle(httpServletRequest,httpServletResponse,new Object());
        assertTrue(!continueExport);
    }
    @Test
    public void testPreHandleRecap() throws Exception {
        httpServletRequest.setAttribute("api_key","recap");
        Mockito.when(httpServletRequest.getHeader("api_key")).thenReturn("recap");
        boolean continueExport = swaggerInterceptor.preHandle(httpServletRequest,httpServletResponse,new Object());
        assertTrue(continueExport);
    }
    @Test
    public void postHandle() throws Exception{
        Object handler = new Object() ;
        swaggerInterceptor.postHandle(httpServletRequest,httpServletResponse,handler,new ModelAndView());
    }
    @Test
    public void afterCompletion() throws Exception{
        Object handler = new Object() ;
        swaggerInterceptor.afterCompletion(httpServletRequest,httpServletResponse,handler,new Exception());
    }


}