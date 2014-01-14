package org.yournamehere.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.logging.Logger;

/**
 * Main entry point.
 */
public class MainEntryPoint implements EntryPoint {
    
    private static Logger log = Logger.getLogger(MainEntryPoint.class.getName());    
    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    @Override
    public void onModuleLoad() {
        log.fine("my module");
        MD5Form form = new MD5Form();
        
        RootPanel root = RootPanel.get();
        root.add(form.asWidget());        
    }
}
