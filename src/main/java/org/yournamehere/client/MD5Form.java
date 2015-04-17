package org.yournamehere.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.logging.Logger;
import org.yournamehere.client.utils.CursorUtils;

/**
 *
 * @author temp_jacek
 */
public class MD5Form extends Composite {

    private static MD5FormUiBinder uiBinder = GWT.create(MD5FormUiBinder.class);
    private Logger log = Logger.getLogger(getClass().getName());
    private MD5CheckingServiceAsync service = GWT.create(MD5CheckingService.class);

    public @UiField
    TextBox messageBox;
    public @UiField
    Button button;
    
    @UiField
    public Label response;
    
    interface MD5FormUiBinder extends UiBinder<Widget, MD5Form> {
    }

    public MD5Form() {
        log.finer("start uibinder");
        initWidget(uiBinder.createAndBindUi(this));
    }

    public String getMessage() {
        return messageBox.getText();
    }

    public void setMessage(String message) {
        this.messageBox.setText(message);
    }
    
    public void setResponse(String message) {
        response.setText(message);
    }

    @UiHandler("button")
    public void handleClick(ClickEvent evt) {
        CursorUtils.cursorWait();
        service.computeMD5(getMessage(), new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("error: " + caught.getMessage());
            }

            @Override
            public void onSuccess(String result) {
                CursorUtils.cursorNormal();
                log.fine("success");
                setResponse(result);
            }
        });
    }
}
