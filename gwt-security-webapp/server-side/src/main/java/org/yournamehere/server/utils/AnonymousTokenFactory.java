package org.yournamehere.server.utils;

import java.util.ArrayList;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Return {@link AnonymousAuthenticationToken}.
 *
 * @author Jacek Grzebyta
 */
public class AnonymousTokenFactory {

    private static final String keyName = "anonymous";
    private static final String role = "ROLE_ANONYMOUS";
    
    private static AnonymousAuthenticationToken _instance = null;
    
    private static final User anonymousUser = new User(keyName, keyName, new ArrayList<GrantedAuthority>(){{
            add(new SimpleGrantedAuthority(role));
        }});

    public static final AnonymousAuthenticationToken makeAnonymous() {
        if (_instance ==null) {
            _instance = new AnonymousAuthenticationToken(keyName, anonymousUser, anonymousUser.getAuthorities());
        }
        
        return _instance;
    }
}
