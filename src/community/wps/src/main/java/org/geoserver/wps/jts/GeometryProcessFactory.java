package org.geoserver.wps.jts;

import org.geotools.util.SimpleInternationalString;

/**
 * A process factory exposing all the annotated methods in
 * {@link GeometryFunctions}
 * 
 * @author Andrea Aime - OpenGeo
 * 
 */
public class GeometryProcessFactory extends
		StaticMethodsProcessFactory<GeometryFunctions> {

	public GeometryProcessFactory() {
		super(new SimpleInternationalString(
				"Simple JTS based spatial analysis methods"), "JTS",
				GeometryFunctions.class);
	}

}
