/*
 */

package org.geoserver.catalog.hibernate.beans;

import org.geoserver.catalog.CoverageDimensionInfo;
import org.geoserver.catalog.impl.CoverageDimensionImpl;
import org.geoserver.hibernate.Hibernable;

/**
 *
 * @author ETj <etj at geo-solutions.it>
 */
public class CoverageDimensionInfoImplHb
    extends CoverageDimensionImpl
    implements CoverageDimensionInfo, Hibernable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -859886165039126157L;

}
