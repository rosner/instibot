package edu.potsdam.instibot.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import org.springframework.stereotype.Component;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Component
@Provider
public class InstibotJAXBContextResolver implements
	ContextResolver<JAXBContext> {

    private JAXBContext context;
    private Class<?>[] types = { BotResponse.class, QuestionAnswerPair.class, UserCredentials.class };

    public InstibotJAXBContextResolver() throws Exception {
	this.context = new JSONJAXBContext(JSONConfiguration.natural().build(), types);
    }

    public JAXBContext getContext(Class<?> objectType) {
	return context;
    }
}