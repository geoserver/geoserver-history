/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.geotools.validation.attributes;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ResourceBundle;

import org.geotools.validation.DefaultFeatureValidationBeanInfo;

/**
 * RangeValidationBeanInfo purpose.
 * <p>
 * Description of RangeValidationBeanInfo ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: SingleValueValidationBeanInfo.java,v 1.1 2004/02/10 18:29:03 dmzwiers Exp $
 */
public class SingleValueValidationBeanInfo extends DefaultFeatureValidationBeanInfo {
	
	/**
	 * GazetteerNameValidationBeanInfo constructor.
	 * <p>
	 * Description
	 * </p>
	 * 
	 */
	public SingleValueValidationBeanInfo() {
		super();
	}
	
	/**
	 * Implementation of getPropertyDescriptors.
	 * 
	 * @see java.beans.BeanInfo#getPropertyDescriptors()
	 * 
	 * @return
	 */
	public PropertyDescriptor[] getPropertyDescriptors(){
		return super.getPropertyDescriptors();
	}
}
