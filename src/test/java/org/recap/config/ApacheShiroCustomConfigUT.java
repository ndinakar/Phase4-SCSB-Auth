package org.recap.config;

import org.apache.shiro.authz.AuthorizationException;
import org.junit.Test;

import org.apache.shiro.subject.Subject;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCase;
import org.springframework.ui.Model;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
public class ApacheShiroCustomConfigUT extends BaseTestCase {

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
