package org.yournamehere.security.server;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.yournamehere.shared.exceptions.OtherAuthenticationException;
import org.yournamehere.shared.exceptions.WrongCredentialsException;

/**
 *
 * @author Jacek Grzebyta
 */
@RemoteServiceRelativePath("user-service")
public interface UserService extends RemoteService {
    
    /**
     * User authentication.
     * @param username
     * @param password
     * @param useragent
     * @return tokenId
     * @throws WrongCredentialsException 
     */
    public String login(final String username, final String password, final String useragent) throws WrongCredentialsException;
    
    /**
     * User authentication based on ticket Id.
     * @param tokenId
     * @param useragent
     * @return tokenId if token is valid
     * @throws OtherAuthenticationException 
     */
    public String login(String tokenId, String useragent) throws OtherAuthenticationException ;
    
    /**
     * Logout user based on token.
     * 
     * @param tokenId
     * @return
     * @throws OtherAuthenticationException 
     */
    public boolean logout(String tokenId) throws OtherAuthenticationException;
}
