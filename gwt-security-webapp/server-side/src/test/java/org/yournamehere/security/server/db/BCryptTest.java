package org.yournamehere.security.server.db;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Jacek Grzebyta
 */
@RunWith(Parameterized.class)
public class BCryptTest {

    private static Logger log = LoggerFactory.getLogger(BCryptTest.class);

    @Parameterized.Parameters
    public static Collection<String[]> displayPasswords() throws Exception {
        return Arrays.asList(new String[][]{
            {"password1"},
            {"super secret password2"},
            {"password3 and blah blah"},
            {"password4"}
        });
    }

    public BCryptTest(String password) {
        this.password = password;
    }

    private String password;

    @Test
    public void simpleTest() throws Exception {
        log.info("test password hash");

        for (int i = 0; i < 10; i++) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashed = encoder.encode(password);
            
            if (i ==0 ) {
                log.info("=============");
            }
            log.info("run {} hash: {}  hash size: {}", i, hashed, hashed.getBytes().length);
            
            // create separate instance of encoder for validation
            BCryptPasswordEncoder validator = new BCryptPasswordEncoder();
            boolean isValid = validator.matches(password, hashed);
            log.info("is valid: {}", isValid);
        }
    }
}
