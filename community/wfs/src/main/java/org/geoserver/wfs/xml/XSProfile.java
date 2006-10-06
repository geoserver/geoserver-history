package org.geoserver.wfs.xml;

import java.util.HashSet;
import java.util.Set;

import org.geotools.feature.Name;
import org.geotools.xs.XSSchema;
import org.geotools.xs.bindings.XS;

/**
 * A profile of {@link XSSchema} which makes the java class to type 
 * mapping unique.
 */
public class XSProfile extends TypeMappingProfile {

	static Set profile = new HashSet();
	static {
		profile.add( new Name(  XS.NAMESPACE, "byte" ) );
		profile.add( new Name(  XS.NAMESPACE, "short" ) );
		profile.add( new Name(  XS.NAMESPACE, "int" ) );
		profile.add( new Name(  XS.NAMESPACE, "float" ) );
		profile.add( new Name(  XS.NAMESPACE, "long" ) );
		profile.add( new Name(  XS.NAMESPACE, "QName" ) );
		profile.add( new Name(  XS.NAMESPACE, "date" ) );
		profile.add( new Name(  XS.NAMESPACE, "boolean" ) );
		profile.add( new Name(  XS.NAMESPACE, "double" ) );
		profile.add( new Name(  XS.NAMESPACE, "string" ) );
	}
	
	public XSProfile() {
		super( new XSSchema(), profile );
	}

	
}
