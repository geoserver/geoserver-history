/*
 * Created on Jan 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.xml;

/**
 * ValidationException purpose.
 * <p>
 * An exception used to collect and generalize errors in the validation system.
 * </p>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * ValidationException x = new ValidationException(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: ValidationException.java,v 1.1 2004/01/19 23:54:56 dmzwiers Exp $
 */
public class ValidationException extends Exception {
	public ValidationException(){super();}
	public ValidationException(String s){super(s);}
	public ValidationException(Throwable e){super(e);}
	public ValidationException(String s, Throwable e){super(s,e);}
}
