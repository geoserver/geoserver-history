package org.geotools.validation;

/**
 * Validation purpose.
 * <p>
 * Description of Validation ...
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * Validation x = new Validation(...);
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: Validation.java,v 1.1.2.3 2003/11/23 07:42:29 jive Exp $
 */
public interface Validation
{
	
	static final String ALL[] = null;	// test all featureTypes
	
	
	/**
	 * setName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param name
	 * @return
	 */
	void 	setName( String name );
	
	/**
	 * getName purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	String 	getName();
	
	
	
	/**
	 * setDescription purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param description
	 */
	void 	setDescription( String description );
	
	/**
	 * getDescription purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	String 	getDescription();
	
	
	
	/**
	 * getPriority purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	int 	getPriority();
	
	
	
	/**
	 * setTypeNames purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param names
	 */
	void 	setTypeNames(String[] names);
	
	/**
	 * getTypeNames purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	String[] getTypeNames();
}
