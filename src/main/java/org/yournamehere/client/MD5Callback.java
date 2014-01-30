/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.yournamehere.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.logging.Logger;
import org.yournamehere.client.utils.CursorUtils;

/**
 *
 * @author temp_jacek
 */
public class MD5Callback implements AsyncCallback<String>{
    
    private Logger log = Logger.getLogger(MD5Callback.class.getName());

    @Override
    public void onFailure(Throwable caught) {
        Window.alert("error: " + caught.getMessage());
    }

    @Override
    public void onSuccess(String result) {
        CursorUtils.cursorNormal();
        log.fine("success");
        Document.get().getElementById("response").setInnerText(result);
    }
    
}
