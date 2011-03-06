package edu.potsdam.instibot.bot;

import java.io.IOException;

import org.aitools.programd.Core;
import org.aitools.programd.XMLCoreSettings;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class Main {

    public static void main(String[] args) throws IOException {

	ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
		"edu/potsdam/instibot/applicationContext.xml");

//	Bot sampleBot = applicationContext.getBean("sampleBot", Bot.class);
//	System.out.println(sampleBot.getAimlResources());
	
	PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
	Resource resource = resourceResolver
		.getResource("classpath:org/aitools/programd/AIML.xsd");
	Resource settingsResource = resourceResolver
		.getResource("classpath:org/aitools/configuration/core.xml");
	XMLCoreSettings xmlCoreSettings = new XMLCoreSettings(
		settingsResource.getURL(), Logger.getLogger(Main.class));
	
	Core core = new Core(resource.getURL(), xmlCoreSettings);
	
    }
}
