/*
 * #%L
 * Mock Spring MVC
 * %%
 * Copyright (C) 2015 Jacek Grzebyta
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package com.github.jgrzebyta.mock.webspring;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.servlet.view.xml.MarshallingView;

/**
 * Main configuration
 *
 * @author Jacek Grzebyta
 */
@Configuration
@EnableWebMvc
@ComponentScan({"com.github.jgrzebyta.mock.webspring.controllers"})
public class WebConfig implements Serializable {

    private static final long serialVersionUID = -7581525633873897176L;

    @Bean
    public ContentNegotiationManager contentManager() {
        ContentNegotiationManagerFactoryBean factory = new ContentNegotiationManagerFactoryBean();
        factory.setFavorPathExtension(true);
        factory.addMediaTypes(new HashMap<String, MediaType>() {
            private static final long serialVersionUID = 1L;

            {
                put("xml", MediaType.APPLICATION_XML);
                put("json", MediaType.APPLICATION_JSON);
            }
        });

        return factory.getObject();
    }
    
    @Bean
    public List<View> defaultViews() {
        List<View> toReturn = new ArrayList<>();
        
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        jsonView.setPrettyPrint(true);
        toReturn.add(jsonView);
        
        MarshallingView xmlView = new MarshallingView();
        
        // prepare marshaller
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.github.jgrzebyta.mock.webspring.model");
        
        xmlView.setMarshaller(marshaller);
        toReturn.add(xmlView);
        
        return toReturn;
    }

    @Bean
    public ViewResolver viewResolver() {
        ContentNegotiatingViewResolver toReturn = new ContentNegotiatingViewResolver();

        toReturn.setContentNegotiationManager(contentManager());
        toReturn.setDefaultViews(defaultViews());
        
        
        return toReturn;
    }
}
