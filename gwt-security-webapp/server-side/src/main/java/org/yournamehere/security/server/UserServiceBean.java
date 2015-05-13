package org.yournamehere.security.server;

import java.util.HashMap;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.yournamehere.server.utils.AnonymousTokenFactory;
import org.yournamehere.server.utils.GenerateHash;
import org.yournamehere.server.utils.GrantedAuthoritiesUtils;
import org.yournamehere.server.utils.SimpleDAO;
import org.yournamehere.shared.exceptions.OtherAuthenticationException;
import org.yournamehere.shared.exceptions.WrongCredentialsException;
import org.yournamehere.shared.schema.Token;

/**
 *
 * @author Jacek Grzebyta
 *
 *
 * <p>
 * Taken from
 * <a href="http://blog.solidcraft.eu/2011/03/spring-security-by-example-user-in.html">Solid
 * Craft: Spring Security by example</a>.
 * </p>
 */
@Service
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class UserServiceBean implements UserService {

    @Resource
    private SimpleDAO dao;
    @Resource
    private AuthenticationManager authenticationManager;

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String login(final String username, final String password, final String useragent) throws WrongCredentialsException {
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(username, password);
        userToken.setDetails(new HashMap<String, String>() {
            {
                put("user-agent", useragent);
            }
        });

        Authentication auth;

        try {
            auth = authenticationManager.authenticate(userToken);
            // save authentication in context
            //ProviderManager

            SecurityContext ctx = SecurityContextHolder.getContext();
            log.debug("Security Context: {}", ctx);
            ctx.setAuthentication(auth);
        } catch (AuthenticationException e) {
            throw new WrongCredentialsException();
        }

        boolean isAuthenticated = isAuthenticated(auth);

        if (!isAuthenticated) {
            throw new WrongCredentialsException();
        }

        String tokenString = GenerateHash.getRandomHash(auth, isAuthenticated);

        // create Token and store in database
        Token token = new Token(dao.getLocalUser(((User) auth.getPrincipal()).getUsername()), tokenString, useragent);
        dao.saveToken(token);

        return tokenString;
    }

    @Override
    public String login(String tokenId, String useragent) throws OtherAuthenticationException {
        log.debug("token based login");

        Token token = dao.validateToken(tokenId);
        if (token == null) {
            throw new OtherAuthenticationException();
        }
        
        /* 
            in case if useragent is wrong
            logout the user
        */
        if (!token.getUserAgent().equals(useragent)) {
            // create authorised user and load it into context
            User user= new User(token.getUser().getUsername(), null, GrantedAuthoritiesUtils.getAuthorities());
            UsernamePasswordAuthenticationToken authenticatedUser = new UsernamePasswordAuthenticationToken(user, null, GrantedAuthoritiesUtils.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            logout(tokenId);
            return null;
        }

        // authenticated User
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(token.getUser().getUsername(),
                null, GrantedAuthoritiesUtils.getAuthorities());

        if (!isAuthenticated(userToken)) {
            throw new OtherAuthenticationException();
        }
        // assume the user is authenticated so directly load into context
        SecurityContextHolder.getContext().setAuthentication(userToken);

        return tokenId;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public boolean logout(String tokenId) throws OtherAuthenticationException {
        Token token = dao.validateToken(tokenId);
        boolean result = false;
        
        if (token != null) {
            result  = dao.removeToken(tokenId);
        }
        // remove user from context
        SecurityContextHolder.getContext().setAuthentication(AnonymousTokenFactory.makeAnonymous());

        return result;
    }

    private boolean isAuthenticated(Authentication a) {
        log.trace("is aythenticated: {}", a);
        return a != null && !(a instanceof AnonymousAuthenticationToken) && a.isAuthenticated();
    }
}
