package org.recap;


import org.apache.shiro.mgt.SecurityManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.recap.repository.PermissionsRepository;
import org.recap.repository.RolesDetailsRepositorty;
import org.recap.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@Transactional
@Rollback()
public class BaseTestCase {

    @Autowired
    ApplicationContext applicationContext;

    protected SecurityManager securityManager;

    @Before
    public void loadContexts() {
        assertNotNull(applicationContext);
        securityManager = (SecurityManager) applicationContext.getBean("securityManager");
        assertNotNull(securityManager);
    }
}