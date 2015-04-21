package com.github.jgrzebyta.mock.webspring.model;

import java.io.Serializable;
import java.util.Date;
import javax.servlet.http.HttpSession;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Jacek Grzebyta
 */
@XmlRootElement
public class Session implements Serializable {
    private static final long serialVersionUID = -2589659156137957623L;
    
    
    private String sessionId;
    private Date startDate;

    public Session() {
    }

    public Session(HttpSession s) {
        sessionId = s.getId();
        startDate = new Date(s.getCreationTime());
    }

    @XmlElement
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @XmlElement
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
