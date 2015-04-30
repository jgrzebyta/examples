package org.yournamehere.server.db;

import java.util.Collection;
import java.util.HashSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author Jacek Grzebyta
 */
public class GrantedAuthoritiesUtils {
    
    @SuppressWarnings("serial")
    public static Collection<GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>(){{
            add(new SimpleGrantedAuthority("USER"));
        }};
    }
}
