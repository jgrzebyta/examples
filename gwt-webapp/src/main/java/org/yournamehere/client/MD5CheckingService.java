/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.yournamehere.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 * @author temp_jacek
 */
@RemoteServiceRelativePath("md5sum")
public interface MD5CheckingService extends RemoteService {

    public String computeMD5(String message);
}
