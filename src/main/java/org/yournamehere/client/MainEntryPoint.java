package org.yournamehere.client;

import com.google.common.base.Strings;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import java.util.logging.Logger;

/**
 * Main entry point.
 */
public class MainEntryPoint implements EntryPoint {
    
    private static Logger log = Logger.getLogger(MainEntryPoint.class.getName());
    private MD5Form form;
    /** 
     * The entry point method, called automatically by loading a module
     * that declares an implementing class as an entry-point
     */
    @Override
    public void onModuleLoad() {
        log.fine("my module");
        form = new MD5Form();
        
        RootPanel root = RootPanel.get();
        root.add(form.asWidget());
        
        // get parameter request
        String reqPara = Window.Location.getParameter("request");
        
        if (!Strings.isNullOrEmpty(reqPara)) {
            handleParameter(reqPara);
        }
    }
    
    public void handleParameter(String request) {
        log.fine("have request : " + request);
        form.setMessage(request);
        MD5CheckingServiceAsync service = GWT.create(MD5CheckingService.class);
        service.computeMD5(request, new MD5Callback());
    }
}
