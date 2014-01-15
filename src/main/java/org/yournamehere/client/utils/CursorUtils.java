package org.yournamehere.client.utils;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

/**
 *
 * @author temp_jacek
 */
public class CursorUtils {
     public static void cursorWait() {
         DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
     }
     
     public static void sursorNormal() {
         DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
     }
}
