/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import java.util.logging.Logger;


/**
 * Parses a key-value pair into a key-object pair.
 * <p>
 * This class is intended to be subclassed. Subclasses need declare the key in
 * which they parse, and the type of object they parse into.
 * </p>
 * <p>
 * Instances need to be declared in a spring context like the following:
 * <pre>
 *         <code>
 *  &lt;bean id="myKvpParser" class="org.xzy.MyKvpParser"/&gt;
 *         </code>
 * </pre>
 * Where <code>com.xzy.MyKvpParser</code> could be something like:
 * <pre>
 *         <code>
 *  public class MyKvpParser extends KvpParser {
 *
 *     public MyKvpParser() {
 *        super( "MyKvp", MyObject.class )l
 *     }
 *
 *     public Object parse( String value ) {
 *        return new MyObject( value );
 *     }
 *  }
 *         </code>
 * </pre>
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class KvpParser {
    /**
     * logger
     */
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.ows");

    /**
     * The key.
     */
    String key;

    /**
     * The class of parsed objects.
     */
    Class binding;

    public KvpParser(String key, Class binding) {
        this.key = key;
        this.binding = binding;
    }

    /**
     * @return The name of the key the parser binds to.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return The type of parsed objects.
     */
    protected Class getBinding() {
        return binding;
    }

    /**
     * Parses the string representation into the object representation.
     *
     * @param value The string value.
     *
     * @return The parsed object, or null if it could not be parsed.
     *
     * @throws Exception In the event of an unsuccesful parse.
     */
    public abstract Object parse(String value) throws Exception;
}
