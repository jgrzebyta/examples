package org.yournamehere.shared.schema;

import java.io.Serializable;

/**
 *
 * @author Jacek Grzebyta
 */
public class LocalUser implements Serializable {
    private static final long serialVersionUID = -9002190142776259579L;
    
    private String id;
    private String username;
    private String password;
    
    
    public static final LocalUser getInstance(String id, String username, String password) {        
        return new LocalUser(id, username,password);
    };

    protected LocalUser(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
