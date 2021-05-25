package org.recap.UT.config;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.UT.BaseTestCaseUT;
import org.recap.config.SwaggerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.Writer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by hemalathas on 25/1/17.
 */
public class SwaggerInterceptorUT extends BaseTestCaseUT {

    @InjectMocks
    SwaggerInterceptor swaggerInterceptor;

    @Mock
    HttpServletRequest httpServletRequest;

    @Mock
    HttpServletResponse httpServletResponse;

    @Mock
    Writer writer;

    @Test
    public void testPreHandle() throws Exception {
        PrintWriter printWriter = new PrintWriter(writer);
        httpServletRequest.setAttribute("api_key","test");
        Mockito.when(httpServletResponse.getWriter()).thenReturn(printWriter);
        boolean continueExport = swaggerInterceptor.preHandle(httpServletRequest,httpServletResponse,new Object());
        assertTrue(!continueExport);
    }
    @Test
    public void testPreHandleTest() throws Exception {
        PrintWriter printWriter = new PrintWriter(writer);
        httpServletRequest.setAttribute("api_key","test");
        Mockito.when(httpServletResponse.getWriter()).thenReturn(printWriter);
        boolean continueExport = swaggerInterceptor.preHandle(httpServletRequest,httpServletResponse,new Object());
        assertNotNull(continueExport);
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