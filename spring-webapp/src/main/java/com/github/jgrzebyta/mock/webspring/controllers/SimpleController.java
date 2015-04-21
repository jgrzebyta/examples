package com.github.jgrzebyta.mock.webspring.controllers;

import com.github.jgrzebyta.mock.webspring.model.Session;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Jacek Grzebyta
 */
@RestController
public class SimpleController implements Serializable {
    private static final long serialVersionUID = -5526027449580263766L;
    
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @RequestMapping("sessionInformation")
    public @ResponseBody Session getSessionInformation(HttpServletRequest request) {
        log.info("get basic information about session");
        
        return new Session(request.getSession());
    }
}
