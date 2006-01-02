package org.openplans.geoserver.catalog.ui;

import java.beans.PropertyEditor;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFactorySpi.Param;

/**
 * Provides help or hints for providing a data store ui.
 * <p>
 * Implementations must declare the {@link org.geotools.data.DataStoreFactorySpi}
 * class for which it provides help for. This can be done declarativley in a 
 * spring context file. The following bean declaration specifies help for the
 * {@link org.geotools.data.shapefile.ShapefileDataStoreFactory}. 
 * 
 * <pre>
 * 		<code>
 *	&lt;bean id="shapefileDataStoreUIHelp" class="ShapefileDataStoreUIHelp"&gt;
 * 		&lt;constructor-arg type="java.lang.Class" 
 * 			value="org.geotools.data.shapefile.ShapefileDataStoreFactory"/&gt;
 *	&lt;/bean&gt;
 * 		</code>
 *  </pre>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DataStoreUIHelp {

	private Class dataStoreFactoryClass;
	
	public DataStoreUIHelp(Class dataStoreFactoryClass) {
		if (!DataStoreFactorySpi.class.isAssignableFrom(dataStoreFactoryClass))
			throw new IllegalArgumentException("Class must be instance of " + 
					DataStoreFactorySpi.class.getName());
		
		this.dataStoreFactoryClass = dataStoreFactoryClass;
	}
	
	public final Class getDataStoreFactoryClass() {
		return dataStoreFactoryClass;
	}
	
	/**
	 * Returns a display name of the datastore. Default implementation returns
	 * null.
	 * 
	 * @return Display name of the datastore.
	 */
	public String getDisplayName() {
		return null;
	}
	
	/**
	 * Returns a description of the datastore. Default implementation returns
	 * null.
	 * 
	 * @return Description of the datastore.
	 */
	public String getDescription() {
		return null;
	}
	
	/**
	 * Returns the key to be displayed for a particular parameter. Default 
	 * implementation returns null.
	 *  
	 * @param factory The instance of the factory which declares the paramter.
	 * @param param The declared paramter.
	 * 
	 * @return The key of the param, otherwise null. 
	 */
	public String getKey(DataStoreFactorySpi factory, Param param) {
		return null;
	}
	
	/**
	 * Returns the description to be displayed for a particular parameter. 
	 * Default implementation returns null.
	 *  
	 * @param factory The instance of the factory which declares the paramter.
	 * @param param The declared paramter.
	 * 
	 * @return The description of the param, otherwise null. 
	 */
	public String getDescription(DataStoreFactorySpi factory, Param param) {
		return null;
	}
	
	/**
	 * Returns the sample to be displayed for a particular parameter. 
	 * Default implementation returns null.
	 *  
	 * @param factory The instance of the factory which declares the paramter.
	 * @param param The declared paramter.
	 * 
	 * @return The sample of the p, otherwise null. 
	 */
	public Object getSample(DataStoreFactorySpi factory, Param param) {
		return null;
	}
	
	/**
	 * Returns the description to be displayed for a particular parameter. 
	 * Default implementation returns null.
	 *  
	 * @param factory The instance of the factory which declares the paramter.
	 * @param param The declared paramter.
	 * 
	 * @return The description of the key, otherwise null. 
	 */
	public PropertyEditor getPropertyEditor(DataStoreFactorySpi factory, Param param) {
		return null;
	}
}
