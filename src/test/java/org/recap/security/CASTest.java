package org.recap.security;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

/**
 * Created by sheiks on 11/01/17.
 */
public class CASTest {

    private static final Logger LOGGER = Logger.getLogger(CASTest.class.getName());
    private static final String CAS_URL = "https://servername/service";

    @Test
    public void testLogon() throws IOException {

        String userName = "";
        String password = "";

        CasLogin casLogin = new CasLogin(userName, password, CAS_URL);
        String ticketGrantingTicket = casLogin.getTicketGrantingTicket(userName, password);
        assertTrue(StringUtils.isNotBlank(ticketGrantingTicket));
        System.out.println("Granting Ticket : " + ticketGrantingTicket);
    }

}
