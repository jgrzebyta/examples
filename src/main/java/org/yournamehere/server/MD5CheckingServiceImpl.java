/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.yournamehere.client.MD5CheckingService;

/**
 *
 * @author temp_jacek
 */
public class MD5CheckingServiceImpl extends RemoteServiceServlet implements MD5CheckingService {

    private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public String computeMD5(String message) {
        log.info(String.format("compute MD5 for message: '%s'", message));
        log.fine("fine message");
        log.finer("finer message");
        log.finest("finest message");
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] out = md.digest(message.getBytes(Charset.forName("UTF-8")));
            try {
                // emulate long work
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                log.log(Level.WARNING, "error:" + ex);
            }
            
            return new BigInteger(1, out).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return "no such algorithm";
        }
    }

}
