package org.yournamehere.server.web;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Prevents using JSESSIONID in the URL.
 * 
 * <p>
 * Add the filter into {@code web.xml} file.
 * </p>
 *
 * <p>
 * Taken from <a href="https://randomcoder.org/articles/jsessionid-considered-harmful">randomCoder</a>.
 * </p>
 * 
 * @author Jacek Grzebyta
 */
public class NoSessionInUrlFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // do filtering if ServletRequest is HttpServletRequest
        if (!(request instanceof HttpServletRequest)) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // do filtering
        if (httpRequest.isRequestedSessionIdFromURL()) {
            HttpSession session = httpRequest.getSession(false);
            if (session != null) session.invalidate();
        }
        
    }

    @Override
    public void destroy() {
        // do nothing
    }
    
}
