package org.recap.IT.config;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.IT.BaseTestCase;
import org.recap.config.SwaggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertTrue;

/**
 * Created by hemalathas on 25/1/17.
 */
public class SwaggerInterceptorIT extends BaseTestCase {

    @Autowired
    SwaggerInterceptor swaggerInterceptor;

    @Mock
    HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Test
    public void testPreHandle() throws Exception {
        httpServletRequest.setAttribute("api_key","test");
        boolean continueExport = swaggerInterceptor.preHandle(httpServletRequest,httpServletResponse,new Object());
        assertTrue(!continueExport);
    }
    @Test
    public void testPreHandleTest() throws Exception {
        httpServletRequest.setAttribute("api_key","test");
        Mockito.when(httpServletRequest.getHeader("api_key")).thenReturn("test");
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