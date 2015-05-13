package org.yournamehere.server.utils;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yournamehere.shared.schema.LocalUser;

/**
 * This class run the main authorisation precess.
 * <p>
 *
 * </p> @author Jacek Grzebyta
 */
public class CustomAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Resource
    private SimpleDAO dao;

    private Logger log = LoggerFactory.getLogger(getClass());
    private PasswordEncoder passwordEncoder;

    /**
     * Here I put proper authentication process.
     *
     * @param userDetails is created from database
     * @param authentication has values from login form
     * @throws AuthenticationException
     */
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.debug("additional checks");
        if (passwordEncoder == null) {
            throw new RuntimeException("password encoder must not be null");
        }

        /* 
         *   *NB*
         * userDetails was created from database. 
         * The authentication has values from login form
         */
        // check if username is valid
        if (!userDetails.getUsername().equals(authentication.getPrincipal())) {
            throw new BadCredentialsException("username is not valid");
        }
        
        if (!passwordEncoder.matches((String) authentication.getCredentials(), userDetails.getPassword())) {
            throw new BadCredentialsException("wrong password");
        }
        
    }

    /**
     * That is only retrieving user from database based on the username
     * parameter.
     *
     * @param username
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        log.debug("retrieve username '{}'", username);

        log.debug("authentication token data:\n\t username: {}\n\t password: {}\n\tis authenticated: {}", 
                                                                        authentication.getPrincipal(), 
                                                                        authentication.getCredentials(),
                                                                        authentication.isAuthenticated());

        LocalUser user = dao.getLocalUser(username);
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), GrantedAuthoritiesUtils.getAuthorities());
        return userDetails;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
