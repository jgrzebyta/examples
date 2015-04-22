package org.yournamehere.client.utils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;

/**
 *
 * @author temp_jacek
 */
public class CursorUtils {
     public static void cursorWait() {
         Document.get().getBody().getStyle().setCursor(Style.Cursor.WAIT);
     }
     
     public static void cursorNormal() {
         Document.get().getBody().getStyle().setCursor(Style.Cursor.DEFAULT);
     }
}
