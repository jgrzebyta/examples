package org.yournamehere.security.db;

import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yournamehere.security.db.GenerateHash;

/**
 *
 * @author Jacek Grzebyta
 */
public class GenerateHashTest {
    private Logger log = LoggerFactory.getLogger(getClass());
    
    @Test
    public void testGenerator() throws Exception {
        log.info("test generator");
        Set<String> tester = new HashSet<>(1000);
        for (int i = 0 ; i<1000; i++) {
            String value = GenerateHash.getRandomHash(log);
            Assert.assertFalse(tester.contains(value));
            tester.add(value);
            log.debug("\t{}", value);
        }
        
        String someValue = tester.iterator().next();
        log.info("hash {} with length {} in bytes {}", someValue, someValue.length(), someValue.getBytes().length);
    }
}
