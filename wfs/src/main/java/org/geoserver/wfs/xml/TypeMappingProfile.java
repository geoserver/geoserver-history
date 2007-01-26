package org.geoserver.wfs.xml;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.geotools.feature.AttributeType;
import org.geotools.feature.type.ProfileImpl;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Schema;

/**
 * A special profile of a pariticular {@link Schema} which maintains a unique
 * mapping of java class to xml schema type.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class TypeMappingProfile extends ProfileImpl {

	public TypeMappingProfile( Schema schema, Set profile ) {
		super( schema, profile );
	}

	/**
	 * Obtains the {@link AttributeType} mapped to a particular class.
	 * 
	 * @param clazz The class.
	 * 
	 * @return The AttributeType, or <code>null</code> if no atttribute 
	 * type mapped to <code>clazz</code>
	 */
	public AttributeType type( Class clazz ) {
		for ( Iterator i = values().iterator(); i.hasNext(); ) {
			AttributeType type = (AttributeType) i.next();
			if ( clazz.equals( type.getType() ) )
				return type;
		}
		
		return null;
	}
	
	/**
	 * Obtains the {@link Name} of the {@link AttributeType} mapped 
	 * to a particular class.
	 * 
	 * @param clazz The class.
	 * 
	 * @return The Name, or <code>null</code> if no atttribute type mapped
	 * to <code>clazz</code>
	 */
	public Name name( Class clazz ) {
		for ( Iterator i = entrySet().iterator(); i.hasNext(); ) {
			Map.Entry entry = (Map.Entry) i.next();
			AttributeType type = (AttributeType) entry.getValue();
			if ( clazz.equals( type.getType() ) )
				return (Name) entry.getKey();
		}
		
		return null;
	}
	
	
}
