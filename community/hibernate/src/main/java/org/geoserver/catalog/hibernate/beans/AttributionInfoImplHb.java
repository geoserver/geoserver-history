/*
 */

package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.AttributionInfo;
import org.geoserver.catalog.impl.AttributionInfoImpl;
import org.geoserver.hibernate.Hibernable;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class AttributionInfoImplHb
    extends AttributionInfoImpl
    implements AttributionInfo, Hibernable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5316010422239218288L;

}
