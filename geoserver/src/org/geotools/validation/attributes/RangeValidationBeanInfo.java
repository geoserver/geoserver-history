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
 * @version $Id: RangeValidationBeanInfo.java,v 1.1 2004/02/10 18:29:03 dmzwiers Exp $
 */
public class RangeValidationBeanInfo extends DefaultFeatureValidationBeanInfo {
	
	/**
	 * GazetteerNameValidationBeanInfo constructor.
	 * <p>
	 * Description
	 * </p>
	 * 
	 */
	public RangeValidationBeanInfo() {
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
			ResourceBundle resourceBundle = getResourceBundle(NullZeroValidation.class);
			if(pd2 == null)
				pd2 = new PropertyDescriptor[0];
			PropertyDescriptor[] pd = new PropertyDescriptor[pd2.length + 3];
			int i=0;
			for(;i<pd2.length;i++)
				pd[i] = pd2[i];
			try{
				pd[i] = createPropertyDescriptor("path",NullZeroValidation.class,resourceBundle);
				pd[i].setExpert(false);
                                pd[i] = createPropertyDescriptor("max",NullZeroValidation.class,resourceBundle);
                                pd[i].setExpert(false);
                                pd[i] = createPropertyDescriptor("min",NullZeroValidation.class,resourceBundle);
                                pd[i].setExpert(false);

			}catch(IntrospectionException e){
				pd = pd2;
				// TODO error, log here
				e.printStackTrace();
			}
		return pd;
	}
}
