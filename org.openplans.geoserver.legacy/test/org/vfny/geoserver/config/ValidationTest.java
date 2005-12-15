/*
 * Created on Jan 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.config;

import java.beans.PropertyDescriptor;

import org.geotools.validation.attributes.GazetteerNameValidation;
import org.vfny.geoserver.config.validation.ArgumentConfig;
import org.vfny.geoserver.config.validation.PlugInConfig;
import org.vfny.geoserver.config.validation.TestConfig;

/**
 * ValidationTest purpose.
 * <p>
 * Description of ValidationTest ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ValidationTest.java,v 1.1 2004/01/31 00:17:53 jive Exp $
 */
public class ValidationTest {

	public static void main(String[] args) {
		TestConfig testConfig = new TestConfig();
		PlugInConfig pluginConfig = new PlugInConfig();
		// the plugin to test the bean info for.
		//pluginConfig.setClassName(PolygonBoundaryCoveredByPolygonValidation.class.getName());
		pluginConfig.setClassName(GazetteerNameValidation.class.getName());
		testConfig.setPlugIn(pluginConfig);

		System.out.println(testConfig.toString());
		System.out.println("--------------------------------------");
		for (int i = 0; i < testConfig.getPropertyDescriptors().length; i++) {
			
			System.out.println(testConfig.getPropertyDescriptors()[i].getClass().getName());
			System.out.println(testConfig.getPropertyDescriptors()[i].getDisplayName());
			System.out.println(testConfig.getPropertyDescriptors()[i].getShortDescription());
			
			System.out.println(testConfig.getPropertyDescriptors()[i].attributeNames());
			System.out.println("--------------------------------------");
		}

		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		System.out.println("--------------------------------------");
		
		
		testConfig = new TestConfig();
		pluginConfig = new PlugInConfig();
		// the plugin to test the bean info for.
		//pluginConfig.setClassName(PolygonBoundaryCoveredByPolygonValidation.class.getName());
		pluginConfig.setClassName(GazetteerNameValidation.class.getName());
		testConfig.setPlugIn(pluginConfig);

		PropertyDescriptor [] pd = pluginConfig.getPropertyDescriptors();
		
		System.out.println(pluginConfig.toString());
		System.out.println("--------------------------------------");
		for (int i = 0; i < pluginConfig.getPropertyDescriptors().length; i++) {
			
			System.out.println(pd[i].getClass().getName());
			System.out.println(ArgumentConfig.getDisplayName(pd[i]));
			System.out.println(ArgumentConfig.getDescription(pd[i]));
			
			System.out.println("--------------------------------------");
		}
		


		/*System.out.println(pluginConfig.toString());
		System.out.println("--------------------------------------");
		for (int i = 0; i < pluginConfig.getPropertyDescriptors().length; i++) {
			
			System.out.println(pluginConfig.getPropertyDescriptors()[i].getDisplayName());
			System.out.println(pluginConfig.getPropertyDescriptors()[i].getShortDescription());
			
			System.out.println(pluginConfig.getPropertyDescriptors()[i].attributeNames());
			System.out.println("--------------------------------------");
		}*/
	}
}
