package org.geoserver.rest.client.datatypes;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "CoverageStoreType")
public enum CoverageStoreType {

	ArcGrid,
	WorldImage,
	GeoTIFF;
}
