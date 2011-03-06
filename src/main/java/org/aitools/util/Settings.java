/*
 * aitools utilities
 * Copyright (C) 2006 Noel Bush
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.aitools.util;

/**
 * A Settings object collects settings for any purpose. It contains getter and
 * setter methods for every setting. Usually the subclasses of Settings will be
 * generated automatically from some other structure, since it's annoying to
 * create and maintain these by hand.
 * 
 * @author <a href="mailto:noel@aitools.org">Noel Bush</a>
 */
abstract public class Settings {

    public static final String AIML_SCHEMA_NAMESPACE_URI_KEY = "programd.aiml-schema.namespace-uri";
    public static final String AIML_SCHEMA_NAMESPACE_URI_DEFAULT_VALUE = "http://alicebot.org/2001/AIML-1.0.1";
    
    public static final String STARTUP_FILE_PATH_KEY = "programd.startup-file-path";
    public static final String STARTUP_FILE_PATH_DEFAULT_VALUE = "classpath:org/aitools/configuration/bots.xml";
    
    public static final String MERGE_POLICY_KEY = "programd.merge.policy";
    
    public static final String MERGE_POLICY_APPEND_SEPARATOR_KEY = "programd.merge.append.separator-string";
    
    public static final String MERGE_NOTE_EACH_KEY = "programd.merge.note-each";
    
    public static final String PREDICATE_UNDEFINED_DEFAULT_VALUE_KEY = "programd.predicate-empty-default";
    public static final String PREDICATE_UNDEFINED_DEFAULT_VALUE = "undefined";    
    
    public static final String RESPONSE_TIMEOUT_KEY = "programd.response-timeout";
    
    public static final String CATEGORY_LOAD_NOTIFY_INTERVAL_KEY = "programd.category-load-notify-interval";
    
    public static final String CATEGORY_LOAD_NOTIFY_EACH_FILE_KEY = "programd.load.notify-each-file";
    
    public static final String INFINITE_LOOP_VALUE_KEY = "programd.infinite-loop-input";
    
    public static final String BOT_PREDICATE_CLIENT_NAME_KEY = "programd.client-name-predicate";
    
    public static final String BOT_PREDICATE_BOT_NAME_KEY = "programd.bot-name-predicate";
    
    public static final String PRINT_STACKTRACE_UNCAUGHT_EXCEPTIONS_KEY = "programd.on-uncaught-exceptions.print-stack-trace";

    public static final String OS_ACCESS_ALLOWED_KEY = "programd.os-access-allowed";
    
    public static final String JAVASCRIPT_ALLOWED_KEY = "programd.javascript-allowed";
    
    public static final String GOSSIP_PATH_KEY = "programd.gossip.path";
    
    public static final String FFM_DIR_KEY = "programd.multiplexor.ffm-dir";
    
    public static final String PREDICATE_CACHE_SIZE_KEY = "programd.predicate-cache.max";
    
    /**
     * Initializes the Settings object with values from properties as read, or
     * defaults (if properties are not provided).
     */
    abstract protected void initialize();
}
