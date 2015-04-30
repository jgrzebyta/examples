package org.yournamehere.shared.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Make Spring {@link ApplicationContext} available in GWT environment.
 *
 *
 * @author Jacek Grzebyta
 */
public class SpringRemoteServiceServlet extends RemoteServiceServlet {

    private static final long serialVersionUID = 7839399906483792119L;
    private static final int YEAR_IN_SEC = 31556926;

    private static final String TOKEN_NAME = "validation-token";

    private WebApplicationContext context;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // load spring web application context
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
    }

    /**
     * Exposes Spring's {@link WebApplicationContext} for subclasses.
     *
     * @return Spring application context.
     */
    protected WebApplicationContext getContext() {
        return context;
    }

    public HttpSession getSession(boolean create) {
        return this.getThreadLocalRequest().getSession(create);
    }

    public String getToken() {
        log.debug("get token");
        if (getSession(false) != null) {
            Cookie[] cookies = this.getThreadLocalRequest().getCookies();

            for (Cookie c : cookies) {
                if (TOKEN_NAME.equals(c.getName()) && c.getMaxAge() > -1) {
                    String token = c.getValue();
                    log.debug("token value: {}", token);
                    return token;
                }
            }
        }
        return null;
    }

    /**
     * Create/delete cookie. If token is {@code null} than cookie is deleted.
     * 
     * @param token 
     */
    public void setToken(String token) {
        Cookie c = new Cookie(TOKEN_NAME, token);
        
        // set up cookie max age for 1 year. If value is null than 0
        if (token != null) {
            c.setMaxAge(YEAR_IN_SEC);
        } else {
            c.setMaxAge(0);
        }
        
        this.getThreadLocalResponse().addCookie(c);
    }

    public String getUserAgent() {
        HttpServletRequest httpRequest = this.getThreadLocalRequest();
        String toReturn = httpRequest.getHeader("User-Agent");
        if (toReturn == null) {
            toReturn = httpRequest.getHeader("user-agent");
        }
        
        return toReturn;
    }
}
