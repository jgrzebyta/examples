package org.yournamehere.security.server;

import java.security.NoSuchAlgorithmException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.yournamehere.shared.server.SpringRemoteServiceServlet;

/**
 *
 * @author Jacek Grzebyta
 */
public class MD5checkingServiceImpl extends SpringRemoteServiceServlet implements MD5CheckingService {
    private static final long serialVersionUID = 2558607118890543590L;
    
    MD5CheckingService bean;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bean = getContext().getBean(MD5CheckingService.class);
    }
    
    @Override
    public String computeMD5(String message, String method) throws NoSuchAlgorithmException {
       return  bean.computeMD5(message, method);
    }

    @Override
    public String[] listSupported() {
        return bean.listSupported();
    }
    
}
