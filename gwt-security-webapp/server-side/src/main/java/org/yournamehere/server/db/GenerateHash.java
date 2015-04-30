package org.yournamehere.server.db;

import gnu.crypto.Registry;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import org.springframework.security.crypto.codec.Hex;

/**
 *
 * @author Jacek Grzebyta
 */
public class GenerateHash {
    private static final String SALT = "4593240890620586765971411905971437903624710618968522063610863765183";
    
    public static final String hashPasswd(String password) {
        IMessageDigest h = HashFactory.getInstance(Registry.TIGER_HASH);
        

        h.update(SALT.getBytes(),0,SALT.length());
        byte[] passwordBytes;
        try{
            passwordBytes = password.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            passwordBytes = password.getBytes();
        }
        
        h.update(passwordBytes,0,passwordBytes.length);
        byte[] inter = h.digest();
        
        for (int i=0; i<1500; i++) {
            h.reset();
            h.update(inter,0, inter.length);
            inter = h.digest();
        }
        
       return new String(Hex.encode(inter));
    }
    
    /**
     * 
     * @param salt
     * @return 48 bytes length random hash
     */
    public static final String getRandomHash(Object... salt) {
        IMessageDigest h = HashFactory.getInstance(Registry.TIGER_HASH);
        Random rand = new SecureRandom(new Date().toString().getBytes());
        String out = Long.toString(rand.nextLong());
        
        h.update(out.getBytes(), 0, out.getBytes().length);
        
        for (Object o: salt) {
            byte[] oBytes = o.toString().getBytes();
            h.update(oBytes, 0, oBytes.length);
        }
        
        byte[] inter = h.digest();
        for (int i = 0; i< 1500; i++) {
            h.reset();
            h.update(inter, 0, inter.length);
            inter = h.digest();
        }
        
        return new String(Hex.encode(inter));
    }
}
