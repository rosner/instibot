package edu.potsdam.instibot.configuration;

import java.net.URI;
import java.net.URL;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.aitools.programd.CoreSettings;

@Data @EqualsAndHashCode(callSuper=true)
public class SimpleSettings extends CoreSettings {

    @Override
    protected void initialize() {
	init();
    }

    public void init() {
	
    }
    
    protected URI AIMLNamespaceURI;

    protected URL botConfigURL;

    protected URL pluginConfigURL;

    protected URL gossipURL;

    protected String predicateEmptyDefault;

    protected String clientNamePredicate;

    protected String botNameProperty;

    protected int predicateFlushPeriod;

    protected String predicateManagerImplementation;

    // flat file predicate matcher
    protected URL ffpmDirectory;

    protected String databaseURL;

    protected String databaseDriver;
 
    protected int databaseMaximumConnections;

    protected String databaseUsername;

    protected String databasePassword;

    /**
     * What to do when a category is loaded whose pattern:that:topic path is
     * identical to one already loaded (for the same bot).
     */
    protected MergePolicy mergePolicy;

    /** Produce a note in the console/log for each merge? */
    protected boolean noteEachMerge;

    /**
     * If the append merge policy is used, what text content (if any) should be
     * inserted between the contents of the two templates? (The default value is
     * a space.)
     */
    protected String appendMergeSeparatorString;

    /** The maximum allowable time (in milliseconds) to get a response. */
    protected int responseTimeout;

    /** The input to match if an infinite loop is found. */
    protected String infiniteLoopInput;

    /** Whether to print a stack trace on uncaught exceptions. */
    protected boolean printStackTraceOnUncaughtExceptions;

    /** The Pulse implementation to use. */
    protected String pulseImplementation;

    /** The pulse rate for the heart (beats per minute). */
    protected int heartPulseRate;

    /** Enable the heart? */
    protected boolean heartEnabled;

    /** The delay period when checking changed AIML (milliseconds). */
    protected int AIMLWatcherTimer;

    /** Use the AIML watcher? */
    protected boolean useAIMLWatcher;

    /** The JavaScript interpreter. */
    protected String javascriptInterpreterClassname;

    /** Allow the use of JavaScript? */
    protected boolean allowJavaScript;

    /** The directory in which to execute <system/> element contents. */
    protected URL systemInterpreterDirectory;

    /**
     * The string to prepend to all <system/> calls (platform-specific). Windows
     * requires something like "cmd /c "; Linux doesn't (just leave empty).
     */
    protected String systemInterpreterPrefix;

    /** Allow access to the OS via the system element? */
    protected boolean allowOSAccess;

    /** How frequently (in categories) to notify as categories are being loaded. */
    protected int categoryLoadNotificationInterval;

    /** Produce a notification message for each file that is loaded. */
    protected boolean noteEachLoadedFile;

    /** After all bots have been loaded, exit immediately (useful for timing). */
    protected boolean exitImmediatelyOnStartup;

    /**
     * The string to send when first connecting to the bot. If this value is
     * empty, no value will be sent.
     */
    protected String connectString;

    /** How to interpret random elements. */
    protected RandomStrategy randomStrategy;

    /** The Graphmapper implementation to use. */
    protected String graphmapperImplementation;

    /** The Nodemapper implementation to use. */
    protected String nodemapperImplementation;

    /** Use interactive command-line shell? */
    protected boolean useShell;

    /** http://xml.org/sax/features/use-entity-resolver2 */
    protected boolean xmlParserUseEntityResolver2;

    /** http://xml.org/sax/features/validation */
    protected boolean xmlParserUseValidation;

    /** http://apache.org/xml/features/validation/schema */
    protected boolean xmlParserUseSchemaValidation;

    /** http://apache.org/xml/features/honour-all-schemaLocations */
    protected boolean xmlParserHonourAllSchemaLocations;

    /** http://apache.org/xml/features/xinclude */
    protected boolean xmlParserUseXInclude;

    /** http://apache.org/xml/features/validate-annotations */
    protected boolean xmlParserValidateAnnotations;
}
