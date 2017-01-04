package org.recap;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.recap.repository.PermissionsRepository;
import org.recap.repository.RolesDetailsRepositorty;
import org.recap.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebAppConfiguration
@Transactional
@Rollback()
public class BaseTestCase {



    @Autowired
    public UserService userService;

    @Autowired
    public RolesDetailsRepositorty roleRepository;

    @Autowired
    public PermissionsRepository permissionsRepository;


    @Test
    public void loadContexts() {
        System.out.println();
    }
}