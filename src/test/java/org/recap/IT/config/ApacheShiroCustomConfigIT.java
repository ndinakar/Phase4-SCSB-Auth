package org.recap.IT.config;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.IT.BaseTestCase;
import org.recap.config.ApacheShiroCustomConfig;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
public class ApacheShiroCustomConfigIT extends BaseTestCase {

@Mock
Model model;
@Mock
AuthorizationException authorizationException;
@Mock
ApacheShiroCustomConfig apacheShiroCustomConfig;
    @Test
    public void subject(){
        ApacheShiroCustomConfig apacheShiroCustomConfig = new ApacheShiroCustomConfig();
        Subject subject = apacheShiroCustomConfig.subject();
        assertNotNull(subject);
    }
    @Test
    public void subjectException(){
        Mockito.doCallRealMethod().when(apacheShiroCustomConfig).subject();
        Subject subject = apacheShiroCustomConfig.subject();
        assertNotNull(subject);
    }
    @Test
    public void handleException(){
        ApacheShiroCustomConfig apacheShiroCustomConfig = new ApacheShiroCustomConfig();
        String error = apacheShiroCustomConfig.handleException(authorizationException,model);
        assertNotNull(error);
        assertEquals("Error","error",error);
    }

}
