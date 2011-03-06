package edu.potsdam.instibot.bot;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.aitools.programd.graph.Nodemapper;
import org.aitools.programd.predicates.PredicateInfo;
import org.aitools.programd.predicates.PredicateMap;
import org.aitools.programd.processor.Processor;
import org.aitools.programd.util.InputNormalizer;
import org.aitools.programd.util.Substituter;
import org.aitools.util.Lists;
import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;

public class Bot {
    
    private static final Logger LOGGER = Logger.getLogger(Bot.class);
    
    @Getter
    protected String botID;
    
    @Getter @Setter
    protected boolean enabled;
    
    @Getter @Setter
    protected List<Resource> aimlResources;

    /** The files loaded for the bot. */
    protected Map<URL, Set<Nodemapper>> loadedFiles = new HashMap<URL, Set<Nodemapper>>();

    @Getter @Setter
    protected Map<String, String> properties = Collections.checkedMap(
	    new HashMap<String, String>(), String.class, String.class);

    /** The bot's predicate infos. */
    protected Map<String, PredicateInfo> predicatesInfo = Collections
	    .checkedMap(new HashMap<String, PredicateInfo>(), String.class,
		    PredicateInfo.class);

    /** The bot's processor-specific substitution maps. */
    protected Map<Class<? extends Processor>, LinkedHashMap<Pattern, String>> substitutionMaps = new HashMap<Class<? extends Processor>, LinkedHashMap<Pattern, String>>();

    /** The bot's input substitution map. */
    protected Map<Pattern, String> inputSubstitutions = Collections.checkedMap(
	    new LinkedHashMap<Pattern, String>(), Pattern.class, String.class);

    @Setter
    protected List<String> sentenceSplitters = new ArrayList<String>();

    /** The bot's sentence splitters, as a compiled pattern. */
    protected Pattern sentenceSplitterPattern;

    /** Holds cached predicates, keyed by userid. */
    protected Map<String, PredicateMap> predicateCache = Collections
	    .synchronizedMap(new HashMap<String, PredicateMap>());

    /** The predicate empty default. */
    protected String predicateEmptyDefault = "undefined";
    
    public Bot(String botID) {
	this.botID = botID;
    }

    @PostConstruct
    public void postConstruct() {
	String regexString = Lists.asRegexAlternatives(sentenceSplitters, false);
	sentenceSplitterPattern = Pattern.compile(regexString, Pattern.DOTALL);
	LOGGER.debug("Compiled sentence splitter pattern: " + regexString);
    }
    /**
     * Returns whether the bot has loaded the given file(name).
     * 
     * @param filename
     *            the filename to check
     * @return whether the bot has loaded the given file(name)
     */
    public boolean hasLoaded(String filename) {
	return this.loadedFiles.containsKey(filename);
    }

    /**
     * Adds a nodemapper to the path map.
     * 
     * @param path
     *            the path
     * @param nodemapper
     *            the mapper for the node to add
     */
    public void addToPathMap(URL path, Nodemapper nodemapper) {
	Set<Nodemapper> nodemappers = this.loadedFiles.get(path);
	if (nodemappers == null) {
	    nodemappers = new HashSet<Nodemapper>();
	    this.loadedFiles.put(path, nodemappers);
	}
	nodemappers.add(nodemapper);
    }

    /**
     * Retrieves the value of a named bot property.
     * 
     * @param name
     *            the name of the bot property to get
     * @return the value of the bot property
     */
    public String getPropertyValue(String name) {
	// Don't bother with empty property names.
	if (name == null || "".equals(name)) {
	    return this.predicateEmptyDefault;
	}

	// Retrieve the contents of the property.
	String value = this.properties.get(name);
	if (value != null) {
	    return value;
	}
	// (otherwise...)
	return this.predicateEmptyDefault;
    }

    /**
     * Sets the value of a bot property.
     * 
     * @param name
     *            the name of the bot predicate to set
     * @param value
     *            the value to set
     */
    public void setPropertyValue(String name, String value) {
	// Property name must not be empty.
	if (name == null || "".equals(name)) {
	    return;
	}

	// Store the property.
	this.properties.put(name, value);
    }

    /**
     * Registers some information about a predicate in advance. Not required;
     * just used when it is necessary to specify a default value for a predicate
     * and/or specify its type as return-name-when-set.
     * 
     * @param name
     *            the name of the predicate
     * @param defaultValue
     *            the default value (if any) for the predicate
     * @param returnNameWhenSet
     *            whether the predicate should return its name when set
     */
    public void addPredicateInfo(String name, String defaultValue,
	    boolean returnNameWhenSet) {
	PredicateInfo info = new PredicateInfo(name, defaultValue, returnNameWhenSet);
	this.predicatesInfo.put(name, info);
    }

    /**
     * Returns the map of predicates for a userid if it is cached, or a new map
     * if it is not cached.
     * 
     * @param userid
     * @return the map of predicates for the given userid
     */
    public PredicateMap predicatesFor(String userid) {
	PredicateMap userPredicates;

	// Find out if any predicates for this userid are cached.
	if (!this.predicateCache.containsKey(userid)) {
	    // Create them if not.
	    userPredicates = new PredicateMap();
	    this.predicateCache.put(userid, userPredicates);
	} else {
	    userPredicates = this.predicateCache.get(userid);
	    assert userPredicates != null : "userPredicates is null!";
	}
	return userPredicates;
    }

    /**
     * Adds a substitution to the indicated map. If the map does not yet exist,
     * it is created. The <code>find</code> parameter is stored in uppercase, to
     * do case-insensitive comparisons. The <code>replace</code> parameter is
     * stored as is.
     * 
     * @param processor
     *            the processor with which the map is associated
     * @param find
     *            the find-string part of the substitution
     * @param replace
     *            the replace-string part of the substitution
     */
    public void addSubstitution(Class<? extends Processor> processor,
	    Pattern find, String replace) {
	if (!this.substitutionMaps.containsKey(processor)) {
	    this.substitutionMaps.put(processor,
		    new LinkedHashMap<Pattern, String>());
	}
	this.substitutionMaps.get(processor).put(find, replace);
    }

    /**
     * Adds an input substitution. The <code>find</code> parameter is stored in
     * uppercase, to do case-insensitive comparisons. The <code>replace</code>
     * parameter is stored as is.
     * 
     * @param find
     *            the find-string part of the substitution
     * @param replace
     *            the replace-string part of the substitution
     */
    public void addInputSubstitution(Pattern find, String replace) {
	this.inputSubstitutions.put(find, replace);
    }

    /**
     * Adds a sentence splitter to the sentence splitters list.
     * 
     * @param splitter
     *            the string on which to divide sentences
     */
    public void addSentenceSplitter(String splitter) {
	if (splitter != null) {
	    this.sentenceSplitterPattern = null;
	    this.sentenceSplitters.add(".+?" + splitter);
	}
    }

    /**
     * @param processor
     *            the processor whose substitution map is desired
     * @return the substitution map associated with the given processor class.
     */
    public Map<Pattern, String> getSubstitutionMap(
	    Class<? extends Processor> processor) {
	return this.substitutionMaps.get(processor);
    }

    /**
     * Splits the given input into sentences.
     * 
     * @param input
     *            the input to split
     * @return the sentences of the input
     */
    public List<String> sentenceSplit(String input) {
	if (this.sentenceSplitters.size() == 0) {
	    return Lists.singleItem(input);
	}
	
	return InputNormalizer.sentenceSplit(this.sentenceSplitterPattern,
		input);
    }

    /**
     * Applies input substitutions to the given input
     * 
     * @param input
     *            the input to which to apply substitutions
     * @return the processed input
     */
    public String applyInputSubstitutions(String input) {
	return Substituter.applySubstitutions(this.inputSubstitutions, input);
    }
}
