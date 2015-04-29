package org.yournamehere.server;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author Jacek Grzebyta
 */
@RemoteServiceRelativePath("md5check")
public interface MD5CheckingService extends RemoteService {
    
    /**
     * Proper method.
     * 
     * Only for proper users.
     * 
     * @param message
     * @param method
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public String computeMD5(String message, String method) throws NoSuchAlgorithmException;
    
    /**
     * List supported hash methods. 
     * 
     * <p>
     * Method useful for building web page.
     * <p>
     * @return 
     */
    public String[] listSupported();
    
}
