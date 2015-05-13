package org.yournamehere.security.server;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.yournamehere.shared.server.SpringRemoteServiceServlet;
import org.yournamehere.shared.exceptions.OtherAuthenticationException;
import org.yournamehere.shared.exceptions.WrongCredentialsException;

/**
 *
 * @author Jacek Grzebyta
 */
public class UserServiceImpl extends SpringRemoteServiceServlet implements UserService {
    private static final long serialVersionUID = -6479080759978492522L;
    
    private UserService bean;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bean = getContext().getBean(UserService.class);
    }

    @Override
    public String login(String username, String password, String useragent) throws WrongCredentialsException {
        String userAgent = getUserAgent();
        // useragent ignores and taken from RequestContext
        String token = bean.login(username, password, userAgent);
        setToken(token); // save in cookie
        return token;
    }

    @Override
    public String login(String tokenId, String useragent) throws OtherAuthenticationException {
        // useragent and token are ignored from arguments and taken from RequestContext
        tokenId = getToken();
        useragent = getUserAgent();
        return bean.login(tokenId, useragent);
    }

    @Override
    public boolean logout(String tokenId) throws OtherAuthenticationException {
        bean.logout(tokenId);
        // whatever happens above delete token anyway!! Database will be cleaned afeter.
        setToken(null); // delete cookie
        
        return true;
    }
}
