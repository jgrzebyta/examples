/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.yournamehere.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author temp_jacek
 */
public class MD5Form extends Composite {
    
    private static MD5FormUiBinder uiBinder = GWT.create(MD5FormUiBinder.class);
    
    @UiField InputElement message;
    @UiField Button button;
    
    
    interface MD5FormUiBinder extends UiBinder<Widget, MD5Form> {
    }
    
    public MD5Form() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    public String getMessage() {
        return message.getValue();
    }
    
    @UiHandler("button")
    public void handleClick(ClickEvent evt) {
        MD5CheckingServiceAsync service = GWT.create(MD5CheckingService.class);
        service.computeMD5(getMessage(), new MD5Callback());
    }
}
