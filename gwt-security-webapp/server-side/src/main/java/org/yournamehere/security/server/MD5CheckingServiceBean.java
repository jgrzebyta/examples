package org.yournamehere.security.server;

import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

/**
 *
 * @author temp_jacek
 */
@Service
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class MD5CheckingServiceBean implements MD5CheckingService {

    private static final long serialVersionUID = -3422873731573926749L;

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PreAuthorize("hasRole('ROLE_USER')")
    @Override
    public String computeMD5(String message, String method) throws NoSuchAlgorithmException {
        log.info(String.format("compute MD5 for message: '%s'", message));

        IMessageDigest md = HashFactory.getInstance(message);
        md.update(message.getBytes(Charset.forName("UTF-8")), 0, message.length());
        byte[] out = md.digest();
        try {
            // emulate long work
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
            log.debug("error:" + ex);
        }

        return new String(Hex.encode(out));
    }

    @Override
    public String[] listSupported() {
        return new String[]{"MD5", "SHA1", "tiger"};
    }

}
