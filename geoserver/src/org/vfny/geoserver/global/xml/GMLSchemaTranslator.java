/*
 * Created on Feb 5, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.global.xml;

import java.util.HashSet;
import java.util.Set;

/**
 * XMLSchemaTranslator purpose.
 * <p>
 * Description of XMLSchemaTranslator ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: GMLSchemaTranslator.java,v 1.1 2004/02/06 19:10:50 dmzwiers Exp $
 */
public class GMLSchemaTranslator extends NameSpaceTranslator {

	private HashSet elements;
	
	/**
	 * XMLSchemaTranslator constructor.
	 * <p>
	 * Description
	 * </p>
	 * @param prefix
	 */
	public GMLSchemaTranslator(String prefix) {
		super(prefix);
		elements = new HashSet();
		elements.add(new PointElement(prefix));
		elements.add(new LineStringElement(prefix));
		elements.add(new LinearRingElement(prefix));
		elements.add(new BoxElement(prefix));
		elements.add(new PolygonElement(prefix));
		elements.add(new GeometryCollectionElement(prefix));
		elements.add(new MultiPointElement(prefix));
		elements.add(new MultiLineStringElement(prefix));
		elements.add(new MultiPolygonElement(prefix));
		elements.add(new CoordElement(prefix));
		elements.add(new CoordinatesElement(prefix));
		elements.add(new PointPropertyElement(prefix));
		elements.add(new PolygonPropertyElement(prefix));
		elements.add(new LineStringPropertyElement(prefix));
		elements.add(new MultiPointPropertyElement(prefix));
		elements.add(new MultiLineStringPropertyElement(prefix));
		elements.add(new MultiPolygonPropertyElement(prefix));
		elements.add(new MultiGeometryPropertyElement(prefix));
		elements.add(new NullElement(prefix));
		elements.add(new AbstractFeatureElement(prefix));
		elements.add(new AbstractFeatureCollectionBaseElement(prefix));
		elements.add(new AbstractFeatureCollectionElement(prefix));
		elements.add(new GeometryPropertyElement(prefix));
		elements.add(new FeatureAssociationElement(prefix));
		elements.add(new BoundingShapeElement(prefix));
		elements.add(new AbstractGeometryElement(prefix));
		elements.add(new AbstractGeometryCollectionBaseElement(prefix));
		elements.add(new AssociationAttributeGroupElement(prefix));
		elements.add(new GeometryAssociationElement(prefix));
		elements.add(new PointMemberElement(prefix));
		elements.add(new LineStringMemberElement(prefix));
		elements.add(new PolygonMemberElement(prefix));
		elements.add(new LinearRingMemberElement(prefix));
	}

	/**
	 * Implementation of getElements.
	 * 
	 * @see org.vfny.geoserver.global.xml.NameSpaceTranslator#getElements()
	 * 
	 * @return
	 */
	public Set getElements() {
		return elements;
	}

	/**
	 * Implementation of getNameSpace.
	 * 
	 * @see org.vfny.geoserver.global.xml.NameSpaceTranslator#getNameSpace()
	 * 
	 * @return
	 */
	public String getNameSpace() {
		return "http://www.opengis.net/gml";
	}

}
