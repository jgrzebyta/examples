/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.yournamehere.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author temp_jacek
 */
public class GwtTestSimple extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "org.yournamehere.test.MainJUnit";
    }
    
    public void testObject() {
        assertNotNull(new Object());
    }
    
    public void testRootPanel(){
        RootPanel panel  = RootPanel.get();
        
        assertNotNull("panel is null", panel);
    }
}
