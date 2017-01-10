package org.recap.security.realm;

import org.apache.shiro.subject.Subject;

import java.util.Date;

/**
 * Created by peris on 1/10/17.
 */
public class SCSBUserRealm {
    private Subject subject;
    private Date loggedInTime;

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getLoggedInTime() {
        return loggedInTime;
    }

    public void setLoggedInTime(Date date) {
        this.loggedInTime = date;
    }
}
