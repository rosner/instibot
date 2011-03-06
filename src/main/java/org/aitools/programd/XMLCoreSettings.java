/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
package org.aitools.programd;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.Set;

import org.aitools.programd.interpreter.RhinoInterpreter;
import org.aitools.util.Settings;
import org.aitools.util.resource.URLTools;
import org.aitools.util.runtime.DeveloperError;
import org.aitools.util.runtime.UserError;
import org.apache.log4j.Logger;
import org.jdom.IllegalNameException;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class XMLCoreSettings extends CoreSettings {

    /** The path to the settings file. */
    private URL path;

    private static final Logger LOGGER = Logger
	    .getLogger(XMLCoreSettings.class);

    /**
     * Creates a <code>XMLCoreSettings</code> with the XML-formatted settings
     * file located at the given path.
     * 
     * @param path
     *            the path to the settings file
     * @param logger
     */
    public XMLCoreSettings(URL path, Logger logger) {
	this.path = path;
	initialize();
    }

    /**
     * Creates a new XPath object with the given path, with the Program D
     * configuration namespace associated with prefix "d".
     * 
     * @param path
     * @return XPath object
     */
    protected static XPath getXPath(String path) {
	final String CONFIG_NS_URI = "http://aitools.org/programd/4.7/programd-configuration";
	XPath xpath;

	try {
	    xpath = XPath.newInstance(path);
	} catch (JDOMException e) {
	    throw new UserError("Error in settings.", e);
	}
	try {
	    xpath.addNamespace("d", CONFIG_NS_URI);
	} catch (IllegalNameException e) {
	    throw new DeveloperError(String.format("Illegal namespace \"%s\".",
		    CONFIG_NS_URI), e);
	}
	return xpath;
    }

    /**
     * Returns the string value of the given XPath expression, evaluated from
     * the given node.
     * 
     * @param path
     * @param context
     * @return value
     */
    protected String getXPathStringValue(String path, Object context) {
	XPath xpath = getXPath(path);
	try {
	    return xpath.valueOf(context);
	} catch (JDOMException e) {
	    throw new UserError("Failed to evaluate XPath expression.", e);
	}
    }

    /**
     * Returns the number value of the given XPath expression, evaluated from
     * the given node.
     * 
     * @param path
     * @param context
     * @return value
     */
    protected Number getXPathNumberValue(String path, Object context) {
	XPath xpath = getXPath(path);
	try {
	    return xpath.numberValueOf(context);
	} catch (JDOMException e) {
	    throw new UserError("Failed to evaluate XPath expression.", e);
	}
    }

    /**
     * Initializes the Settings with values from the XML settings file.
     */
    @Override
    protected void initialize() {
	/*
	 * SAXBuilder saxBuilder = new SAXBuilder(); Document document; try {
	 * document = saxBuilder.build(_path); String xPathStringValue =
	 * getXPathStringValue( "/d:programd/d:aiml.namespace-uri", document);
	 * // Initialize AIMLNamespaceURI.
	 * setAIMLNamespaceURI(URITools.createValidURI(
	 * getXPathStringValue("/d:programd/d:aiml.namespace-uri", document),
	 * false));
	 * 
	 * // bot config url setBotConfigURL(URLTools.createValidURL(
	 * getXPathStringValue("/d:programd/d:paths/d:bot-config", document),
	 * this._path, false));
	 * 
	 * // plugin config url setPluginConfigURL(URLTools.createValidURL(
	 * getXPathStringValue("/d:programd/d:paths/d:plugin-config", document),
	 * this._path, false));
	 * 
	 * // gossip url setGossipURL(URLTools.createValidURL(
	 * getXPathStringValue("/d:programd/d:paths/d:gossip", document),
	 * this._path, false));
	 * 
	 * // Initialize predicateEmptyDefault.
	 * setPredicateEmptyDefault(getXPathStringValue(
	 * "/d:programd/d:predicates/d:empty-default", document));
	 * 
	 * // Initialize clientNamePredicate.
	 * setClientNamePredicate(getXPathStringValue(
	 * "/d:programd/d:predicates/d:client-name-predicate", document));
	 * 
	 * // Initialize botNameProperty.
	 * setBotNameProperty(getXPathStringValue(
	 * "/d:programd/d:predicates/d:bot-name-property", document));
	 * 
	 * // Initialize predicateFlushPeriod.
	 * setPredicateFlushPeriod(getXPathNumberValue(
	 * "/d:programd/d:predicates/d:predicate-flush-period",
	 * document).intValue());
	 * 
	 * // Initialize predicateManagerImplementation.
	 * setPredicateManagerImplementation(getXPathStringValue(
	 * "/d:programd/d:predicate-manager/d:implementation", document));
	 * 
	 * setFfpmDirectory(URLTools.createValidURL( getXPathStringValue(
	 * "/d:programd/d:predicate-manager/d:ffpm-dir", document), this._path,
	 * false));
	 * 
	 * // Initialize databaseURL.
	 * setDatabaseURL(getXPathStringValue("/d:programd/d:database/d:url",
	 * document));
	 * 
	 * // Initialize databaseDriver. setDatabaseDriver(getXPathStringValue(
	 * "/d:programd/d:database/d:driver", document));
	 * 
	 * // Initialize databaseMaximumConnections.
	 * setDatabaseMaximumConnections(getXPathNumberValue(
	 * "/d:programd/d:database/d:maximum-connections", document)
	 * .intValue());
	 * 
	 * // Initialize databaseUsername.
	 * setDatabaseUsername(getXPathStringValue(
	 * "/d:programd/d:database/d:username", document));
	 * 
	 * // Initialize databasePassword.
	 * setDatabasePassword(getXPathStringValue(
	 * "/d:programd/d:database/d:password", document));
	 * 
	 * String mergePolicyValue = getXPathStringValue(
	 * "/d:programd/d:merge/d:policy", document); if
	 * (mergePolicyValue.equals("skip")) { setMergePolicy(MergePolicy.SKIP);
	 * } else if (mergePolicyValue.equals("overwrite")) {
	 * setMergePolicy(MergePolicy.OVERWRITE); } else if
	 * (mergePolicyValue.equals("append")) {
	 * setMergePolicy(MergePolicy.APPEND); } else if
	 * (mergePolicyValue.equals("combine")) {
	 * setMergePolicy(MergePolicy.COMBINE); }
	 * 
	 * // Initialize noteEachMerge.
	 * setNoteEachMerge(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:merge/d:note-each", document)));
	 * 
	 * // Initialize appendMergeSeparatorString.
	 * setAppendMergeSeparatorString(getXPathStringValue(
	 * "/d:programd/d:merge/d:append-policy.separator-string", document));
	 * 
	 * // Initialize responseTimeout.
	 * setResponseTimeout(getXPathNumberValue(
	 * "/d:programd/d:exceptions/d:response-timeout", document)
	 * .intValue());
	 * 
	 * // Initialize infiniteLoopInput.
	 * setInfiniteLoopInput(getXPathStringValue(
	 * "/d:programd/d:exceptions/d:infinite-loop-input", document));
	 * 
	 * // Initialize printStackTraceOnUncaughtExceptions.
	 * setPrintStackTraceOnUncaughtExceptions(Boolean
	 * .parseBoolean(getXPathStringValue(
	 * "/d:programd/d:exceptions/d:on-uncaught-exceptions.print-stack-trace"
	 * , document)));
	 * 
	 * // Initialize pulseImplementation.
	 * setPulseImplementation(getXPathStringValue(
	 * "/d:programd/d:heart/d:pulse.implementation", document));
	 * 
	 * // Initialize heartPulseRate. setHeartPulseRate(getXPathNumberValue(
	 * "/d:programd/d:heart/d:pulse.rate", document).intValue());
	 * 
	 * // Initialize heartEnabled.
	 * setHeartEnabled(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:heart/@enabled", document)));
	 * 
	 * // Initialize AIMLWatcherTimer.
	 * setAIMLWatcherTimer(getXPathNumberValue(
	 * "/d:programd/d:watchers/d:AIML/d:timer", document) .intValue());
	 * 
	 * // Initialize useAIMLWatcher.
	 * setUseAIMLWatcher(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:watchers/d:AIML/@enabled", document)));
	 * 
	 * // Initialize javascriptInterpreterClassname.
	 * setJavascriptInterpreterClassname(getXPathStringValue(
	 * "/d:programd/d:interpreters/d:javascript/d:interpreter-classname",
	 * document));
	 * 
	 * // Initialize allowJavaScript.
	 * setAllowJavaScript(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:interpreters/d:javascript/@allowed", document)));
	 * 
	 * setSystemInterpreterDirectory(URLTools.createValidURL(
	 * getXPathStringValue(
	 * "/d:programd/d:interpreters/d:system/d:directory", document),
	 * this._path, false));
	 * 
	 * // Initialize systemInterpreterPrefix.
	 * setSystemInterpreterPrefix(getXPathStringValue(
	 * "/d:programd/d:interpreters/d:system/d:prefix", document));
	 * 
	 * // Initialize allowOSAccess.
	 * setAllowOSAccess(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:interpreters/d:system/@allowed", document)));
	 * 
	 * // Initialize categoryLoadNotificationInterval.
	 * setCategoryLoadNotificationInterval(getXPathNumberValue(
	 * "/d:programd/d:loading/d:category-load-notification-interval",
	 * document).intValue());
	 * 
	 * // Initialize noteEachLoadedFile.
	 * setNoteEachLoadedFile(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:loading/d:note-each-loaded-file", document)));
	 * 
	 * // Initialize exitImmediatelyOnStartup.
	 * setExitImmediatelyOnStartup(Boolean
	 * .parseBoolean(getXPathStringValue(
	 * "/d:programd/d:loading/d:exit-immediately-on-startup", document)));
	 * 
	 * // Initialize connectString. setConnectString(getXPathStringValue(
	 * "/d:programd/d:connect-string", document));
	 * 
	 * // Initialize randomStrategy.
	 * 
	 * String randomStrategyValue = getXPathStringValue(
	 * "/d:programd/d:random-strategy", document); if
	 * (randomStrategyValue.equals("pure-random")) {
	 * setRandomStrategy(RandomStrategy.PURE_RANDOM); } else if
	 * (randomStrategyValue.equals("non-repeating")) {
	 * setRandomStrategy(RandomStrategy.NON_REPEATING); }
	 * 
	 * // Initialize graphmapperImplementation.
	 * setGraphmapperImplementation(getXPathStringValue(
	 * "/d:programd/d:graphmapper.implementation", document));
	 * 
	 * // Initialize nodemapperImplementation.
	 * setNodemapperImplementation(getXPathStringValue(
	 * "/d:programd/d:nodemapper.implementation", document));
	 * 
	 * // Initialize useShell.
	 * setUseShell(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:use-shell", document)));
	 * 
	 * // Initialize xmlParserUseEntityResolver2.
	 * setXmlParserUseEntityResolver2(Boolean
	 * .parseBoolean(getXPathStringValue(
	 * "/d:programd/d:xml-parsing/d:use-entity-resolver2", document)));
	 * 
	 * // Initialize xmlParserUseValidation.
	 * setXmlParserUseValidation(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:xml-parsing/d:use-validation", document)));
	 * 
	 * // Initialize xmlParserUseSchemaValidation.
	 * setXmlParserUseSchemaValidation(Boolean
	 * .parseBoolean(getXPathStringValue(
	 * "/d:programd/d:xml-parsing/d:use-schema-validation", document)));
	 * 
	 * // Initialize xmlParserHonourAllSchemaLocations.
	 * setXmlParserHonourAllSchemaLocations(Boolean
	 * .parseBoolean(getXPathStringValue(
	 * "/d:programd/d:xml-parsing/d:honour-all-schema-locations",
	 * document)));
	 * 
	 * // Initialize xmlParserUseXInclude.
	 * setXmlParserUseXInclude(Boolean.parseBoolean(getXPathStringValue(
	 * "/d:programd/d:xml-parsing/d:use-xinclude", document)));
	 * 
	 * // Initialize xmlParserValidateAnnotations.
	 * setXmlParserValidateAnnotations(Boolean
	 * .parseBoolean(getXPathStringValue(
	 * "/d:programd/d:xml-parsing/d:validate-annotations", document))); }
	 * catch (JDOMException e1) { e1.printStackTrace(); } catch (IOException
	 * e1) { throw new UserError("Error in settings.", e1); }
	 */
	try {
	    LOGGER.debug("Beginning to load settings from path: '" + path + "'");

	    PathMatchingResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
	    Properties properties = new Properties();

	    properties.loadFromXML(path.openStream());

	    String aimlNamespace = properties.getProperty(
		    Settings.AIML_SCHEMA_NAMESPACE_URI_KEY,
		    Settings.AIML_SCHEMA_NAMESPACE_URI_DEFAULT_VALUE);
	    URI aimlNamespaceUri = new URL(aimlNamespace).toURI();
	    setAIMLNamespaceURI(aimlNamespaceUri);

	    String botConfigFilePath = properties.getProperty(
		    Settings.STARTUP_FILE_PATH_KEY,
		    Settings.STARTUP_FILE_PATH_DEFAULT_VALUE);
	    Resource botConfigResource = resourceResolver
		    .getResource(botConfigFilePath);
	    setBotConfigURL(botConfigResource.getURL());

	    // plugin config url

	    String gossipFilePath = properties
		    .getProperty(Settings.GOSSIP_PATH_KEY);
	    Resource gossipResource = resourceResolver
		    .getResource(gossipFilePath);
	    File gossipFile = gossipResource.getFile();
	    if (!gossipFile.exists()) {
		new File(gossipFile.getName());
	    }
	    setGossipURL(gossipResource.getURL());

	    String defaultPredicate = properties.getProperty(
		    Settings.PREDICATE_UNDEFINED_DEFAULT_VALUE_KEY,
		    Settings.PREDICATE_UNDEFINED_DEFAULT_VALUE);
	    setPredicateEmptyDefault(defaultPredicate);

	    String clientNamePredicateName = properties
		    .getProperty(Settings.BOT_PREDICATE_CLIENT_NAME_KEY);
	    setClientNamePredicate(clientNamePredicateName);

	    String botNamePredicate = properties
		    .getProperty(Settings.BOT_PREDICATE_BOT_NAME_KEY);
	    setBotNameProperty(botNamePredicate);

	    // // Initialize predicateFlushPeriod.
	    // setPredicateFlushPeriod(getXPathNumberValue(
	    // "/d:programd/d:predicates/d:predicate-flush-period",
	    // document).intValue());

	    // Initialize predicateManagerImplementation.
	    // setPredicateManagerImplementation(getXPathStringValue(
	    // "/d:programd/d:predicate-manager/d:implementation",
	    // document));

	    String ffmDirectoryPath = properties
		    .getProperty(Settings.FFM_DIR_KEY);
	    Resource ffmDirectoryResource = resourceResolver
		    .getResource(ffmDirectoryPath);
	    setFfpmDirectory(ffmDirectoryResource.getURL());

	    String mergePolicyValue = properties
		    .getProperty(Settings.MERGE_POLICY_KEY);
	    MergePolicy mergePolicy = MergePolicy.COMBINE;
	    if (mergePolicyValue.equalsIgnoreCase("skip")) {
		mergePolicy = MergePolicy.SKIP;
	    } else if (mergePolicyValue.equalsIgnoreCase("overwrite")) {
		mergePolicy = MergePolicy.OVERWRITE;
	    } else if (mergePolicyValue.equalsIgnoreCase("append")) {
		mergePolicy = mergePolicy.APPEND;
	    }
	    setMergePolicy(mergePolicy);

	    String noteEachMerge = properties.getProperty(
		    Settings.MERGE_NOTE_EACH_KEY, "true");
	    boolean noteEach = Boolean.parseBoolean(noteEachMerge);
	    setNoteEachMerge(noteEach);

	    // Initialize appendMergeSeparatorString.
	    setAppendMergeSeparatorString(" ");

	    // Initialize responseTimeout.
	    setResponseTimeout(1000);

	    // Initialize infiniteLoopInput.
	    setInfiniteLoopInput("INFINITE LOOP");

	    // Initialize printStackTraceOnUncaughtExceptions.
	    setPrintStackTraceOnUncaughtExceptions(true);

	    // Initialize pulseImplementation.
	    setPulseImplementation("false");

	    // Initialize heartPulseRate.
	    setHeartPulseRate(10);

	    setHeartEnabled(true);

	    // Initialize javascriptInterpreterClassname.
	    setJavascriptInterpreterClassname("org.aitools.programd.interpreter.RhinoInterpreter");

	    // Initialize allowJavaScript.
	    setAllowJavaScript(true);

	    // setSystemInterpreterDirectory();

	    // Initialize systemInterpreterPrefix.
	    // setSystemInterpreterPrefix(getXPathStringValue(
	    // "/d:programd/d:interpreters/d:system/d:prefix", document));

	    // Initialize allowOSAccess.
	    setAllowOSAccess(false);

	    // Initialize categoryLoadNotificationInterval.
	    // setCategoryLoadNotificationInterval();

	    // Initialize noteEachLoadedFile.
	    setNoteEachLoadedFile(true);

	    // Initialize exitImmediatelyOnStartup.
	    setExitImmediatelyOnStartup(false);

	    // Initialize connectString.
	    setConnectString("CONNECT");

	    // Initialize randomStrategy.

	    setRandomStrategy(RandomStrategy.PURE_RANDOM);

	    // Initialize graphmapperImplementation.
	    setGraphmapperImplementation("org.aitools.programd.graph.Graphmapper");

	    // Initialize nodemapperImplementation.
	    setNodemapperImplementation("org.aitools.programd.graph.ThreeOptimalMemoryNodeMapper");

	    // Initialize useShell.
	    setUseShell(false);
	    /*
	     * // Initialize xmlParserUseEntityResolver2.
	     * setXmlParserUseEntityResolver2(Boolean
	     * .parseBoolean(getXPathStringValue(
	     * "/d:programd/d:xml-parsing/d:use-entity-resolver2", document)));
	     * 
	     * // Initialize xmlParserUseValidation.
	     * setXmlParserUseValidation(Boolean
	     * .parseBoolean(getXPathStringValue(
	     * "/d:programd/d:xml-parsing/d:use-validation", document)));
	     * 
	     * // Initialize xmlParserUseSchemaValidation.
	     * setXmlParserUseSchemaValidation(Boolean
	     * .parseBoolean(getXPathStringValue(
	     * "/d:programd/d:xml-parsing/d:use-schema-validation", document)));
	     * 
	     * // Initialize xmlParserHonourAllSchemaLocations.
	     * setXmlParserHonourAllSchemaLocations(Boolean
	     * .parseBoolean(getXPathStringValue(
	     * "/d:programd/d:xml-parsing/d:honour-all-schema-locations",
	     * document)));
	     * 
	     * // Initialize xmlParserUseXInclude.
	     * setXmlParserUseXInclude(Boolean.parseBoolean(getXPathStringValue(
	     * "/d:programd/d:xml-parsing/d:use-xinclude", document)));
	     * 
	     * // Initialize xmlParserValidateAnnotations.
	     * setXmlParserValidateAnnotations(Boolean
	     * .parseBoolean(getXPathStringValue(
	     * "/d:programd/d:xml-parsing/d:validate-annotations", document)));
	     */

	} catch (InvalidPropertiesFormatException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (URISyntaxException e) {
	    e.printStackTrace();
	}
    }
}
