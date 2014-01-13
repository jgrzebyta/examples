package org.yournamehere.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Main entry point.
 */
public class MainEntryPoint implements EntryPoint {
    
    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    @Override
    public void onModuleLoad() {
        GWT.log("my module");
        MD5Form form = new MD5Form();
        
        RootPanel root = RootPanel.get();
        root.add(form.asWidget());        
    }    
}
