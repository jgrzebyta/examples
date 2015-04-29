package org.yournamehere.shared.schema;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 *
 * @author Jacek Grzebyta
 */
public class Token implements Serializable {
    private static final long serialVersionUID = -7134220404407853101L;
    
    public LocalUser user;
    public String token;
    public Date expired;
    public String userAgent;

    public Token(LocalUser user, String token, Date expired, String userAgent) {
        this.user = user;
        this.token = token;
        this.expired = expired;
        this.userAgent = userAgent;
    }

    public Token(LocalUser user, String token, String userAgent) {
        this.user = user;
        this.token = token;
        this.userAgent = userAgent;
        
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_MONTH, 3);
        this.expired = cal.getTime();
    }

    public LocalUser getUser() {
        return user;
    }

    public void setUser(LocalUser user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.user);
        hash = 71 * hash + Objects.hashCode(this.token);
        hash = 71 * hash + Objects.hashCode(this.expired);
        hash = 71 * hash + Objects.hashCode(this.userAgent);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Token other = (Token) obj;
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.token, other.token)) {
            return false;
        }
        if (!Objects.equals(this.expired, other.expired)) {
            return false;
        }
        if (!Objects.equals(this.userAgent, other.userAgent)) {
            return false;
        }
        return true;
    }
}
