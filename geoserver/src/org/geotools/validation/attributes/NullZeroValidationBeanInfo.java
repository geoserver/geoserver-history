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
 * GazetteerNameValidationBeanInfo purpose.
 * <p>
 * Description of GazetteerNameValidationBeanInfo ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: NullZeroValidationBeanInfo.java,v 1.1 2004/02/03 21:40:52 dmzwiers Exp $
 */
public class NullZeroValidationBeanInfo extends DefaultFeatureValidationBeanInfo {
	
	/**
	 * GazetteerNameValidationBeanInfo constructor.
	 * <p>
	 * Description
	 * </p>
	 * 
	 */
	public NullZeroValidationBeanInfo() {
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
			PropertyDescriptor[] pd2 = super.getPropertyDescriptors();
			ResourceBundle resourceBundle = getResourceBundle(GazetteerNameValidation.class);
			if(pd2 == null)
				pd2 = new PropertyDescriptor[0];
			PropertyDescriptor[] pd = new PropertyDescriptor[pd2.length + 2];
			int i=0;
			for(;i<pd2.length;i++)
				pd[i] = pd2[i];
			try{
				pd[i] = createPropertyDescriptor("attributeName",GazetteerNameValidation.class,resourceBundle);
				pd[i].setExpert(false);
			}catch(IntrospectionException e){
				pd = pd2;
				// TODO error, log here
				e.printStackTrace();
			}
		return pd;
	}
}
