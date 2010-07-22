package org.geoserver.wps.gs;

import org.geoserver.wps.jts.AnnotatedBeanProcessFactory;
import org.geotools.util.SimpleInternationalString;

public class GeoServerProcessFactory extends AnnotatedBeanProcessFactory {

	public GeoServerProcessFactory() {
		super(new SimpleInternationalString("GeoServer custom processes"),
				"gs", BoundsProcess.class, NearestProcess.class, SnapProcess.class);
	}

}
