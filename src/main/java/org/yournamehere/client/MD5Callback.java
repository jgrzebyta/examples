/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.logging.Logger;
import org.yournamehere.client.utils.CursorUtils;

/**
 *
 * @author temp_jacek
 */
public class MD5Callback implements AsyncCallback<String> {

    public static String COOKIE_NAME = "md5sum";
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
        String oldValue = getCookie();
        saveCookie(result);
        Window.alert("old value = "+oldValue);
    }

    public void saveCookie(String value) {
        if (Cookies.isCookieEnabled()) {
            log.fine("cookies enabled");
            Cookies.setCookie(COOKIE_NAME, value);
        }
    }

    public String getCookie() {
        String out = null;
        if (Cookies.isCookieEnabled()) {
            out = Cookies.getCookie(COOKIE_NAME);
            if (out!=null) {
                Cookies.removeCookie(COOKIE_NAME);
            }
        }
        log.fine("old value: " + out);
        return out;
    }
}
