/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.yournamehere.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.logging.Logger;

/**
 *
 * @author temp_jacek
 */
public class GwtTestSimple extends GWTTestCase {
    
    private Logger log = Logger.getLogger(getClass().getName());

    @Override
    public String getModuleName() {
        return "org.yournamehere.Main";
    }
    
    public void testSimple() throws Exception {
        log.info("some simple test");
        
        RootPanel root = RootPanel.get();
        log.info("root : " + root);
        assertNotNull(root);
    }
}
