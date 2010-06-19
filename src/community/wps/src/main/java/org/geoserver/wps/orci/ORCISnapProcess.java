/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.orci;

import static org.geoserver.wps.orci.ORCIProcessFactory.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.measure.converter.UnitConverter;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.geoserver.wps.WPSException;
import org.geotools.data.Parameter;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.measure.Measure;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.text.Text;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.operation.distance.DistanceOp;

public class ORCISnapProcess implements Process, IORCIProcess {
	private static final Logger LOGGER = Logging.getLogger(ORCISnapProcess.class);

	private GeometryFactory geometryFactory = new GeometryFactory();
	
	public InternationalString getDescription() {
		return Text.text("Snap to nearest feature");
	}

	public InternationalString getTitle() {
		return Text.text("Snap");
	}

	@SuppressWarnings("unchecked")
	public Map<String, Parameter<?>> getParameterInfo() {
		Map<String, Parameter<?>> paramInfo = new HashMap<String, Parameter<?>>();
		paramInfo.put("features", new Parameter("features", FeatureCollection.class, Text.text("Features"), Text.text("The FeatureCollection to search"), MandatoryParameter));
		paramInfo.put("point", new Parameter("point", String.class, Text.text("Point"), Text.text("The point to search from"), MandatoryParameter));
		paramInfo.put("crs", new Parameter("crs", String.class, Text.text("CRS"), Text.text("The coordinate reference system"), OptionalParameter));
		return paramInfo;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Parameter<?>> getResultInfo(Map<String, Object> inputs) {
		Map<String, Parameter<?>> outputInfo = new HashMap<String, Parameter<?>>();
		outputInfo.put("result", new Parameter("result", FeatureCollection.class, Text.text("Result"), Text.text("The nearest feature")));
		return outputInfo;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> execute(Map<String, Object> input, ProgressListener monitor) throws ProcessException {
		if (input == null || !input.containsKey("features")) {
			LOGGER.warning("Input parameter \"features\" is missing");
			throw new WPSException("MissingParameterValue", "features");
		}
		FeatureCollection featureCollection = (FeatureCollection)input.get("features");
		if (featureCollection == null || featureCollection.size() == 0) {
			LOGGER.warning("No features to search");
			throw new WPSException("No features to search", "InvalidParameterValue", "features");
		}

		String crsCode = "EPSG:4326";
		if (input.containsKey("crs")) crsCode = (String)input.get("crs");
		CoordinateReferenceSystem crs;
		try {
			crs = CRS.decode(crsCode);
		} catch (Exception e) {
			LOGGER.warning("Unknown CRS code: " + crsCode);
			throw new WPSException("Unknown CRS code: " + crsCode, "InvalidParameterValue", "crs");
		}

		if (!input.containsKey("point")) {
			LOGGER.warning("Input parameter \"point\" is missing");
			throw new WPSException("MissingParameterValue", "point");
		}
		String pointString = (String)input.get("point");
		String[] pointParts = pointString.split(",");
		Point point = geometryFactory.createPoint(new Coordinate(Double.parseDouble(pointParts[0]), Double.parseDouble(pointParts[1])));

		FeatureCollection resultFeatures = process(featureCollection, crs, point);
		HashMap<String, Object> results = new HashMap<String, Object>();
		results.put("result", resultFeatures);
		return results;
	}

	/**
	 * Process the input data set.
	 * @param featureCollection the data set
	 * @param crs the CRS
	 * @param point the given point
	 * @return the snapped to feature
	 * @throws ProcessException error
	 */
	@SuppressWarnings("unchecked")
	private FeatureCollection process(FeatureCollection featureCollection, CoordinateReferenceSystem crs, Point point) throws ProcessException {
		try {
			CoordinateReferenceSystem epsg4326;
			try {
				epsg4326 = CRS.decode("EPSG:4326");
			} catch (Exception e) {
				throw new WPSException("Unknown CRS code: EPSG:4326", e);
			}
			MathTransform crsTransform = CRS.findMathTransform(crs, epsg4326);

			FeatureCollection results = FeatureCollections.newCollection();
			FeatureType targetFeatureType = createTargetFeatureType(featureCollection.getSchema());
			Unit fromUnit = SI.METER;
			Unit toUnit = Unit.valueOf("mi");
			UnitConverter unitConvert = fromUnit.getConverterTo(toUnit);
			Feature nearestFeature = null;
			double nearestDistance = 9e9;
			double nearestBearing = 0;
			Iterator featureIterator = null;
			double[] nearestPoint = new double[2];
			try {
				featureIterator = featureCollection.iterator();
				while (featureIterator.hasNext()) {
					SimpleFeature f = (SimpleFeature)featureIterator.next();
					if (f.getDefaultGeometryProperty().getValue() == null) continue;
					DistanceOp op = new DistanceOp(point, (Geometry)f.getDefaultGeometryProperty().getValue());
					Coordinate[] co = op.closestPoints();
					double[] co0 = new double[] { co[0].x, co[0].y, };
					double[] co1 = new double[] { co[1].x, co[1].y, };
					double[] geo0 = new double[2];
					double[] geo1 = new double[2];
					crsTransform.transform(co0, 0, geo0, 0, 1);
					crsTransform.transform(co1, 0, geo1, 0, 1);

					// get distance
					Measure m = DefaultGeographicCRS.WGS84.distance(geo0, geo1);
					if (m.doubleValue() > nearestDistance) continue;
					nearestFeature = f;
					nearestDistance = m.doubleValue();
					nearestBearing = calcBearing(co);
					nearestPoint[0] = geo1[0];
					nearestPoint[1] = geo1[1];
				}
			} finally {
				if (featureIterator != null) featureCollection.close(featureIterator);
			}
			if (nearestFeature != null) {
				nearestDistance = unitConvert.convert(nearestDistance);
				results.add(createTargetFeature(nearestFeature, (SimpleFeatureType)targetFeatureType, nearestPoint, nearestDistance, nearestBearing));
			}
			return results;
		} catch (WPSException e) {
			throw e;
		} catch (Throwable e) {
			LOGGER.warning("Error executing method: " + e);
			throw new WPSException("Error executing method: " + e, e);
		}
	}

	/**
	 * Create the modified feature type.
	 * @param sourceFeatureType the source feature type
	 * @return the modified feature type 
	 * @throws ProcessException errror
	 */
	private SimpleFeatureType createTargetFeatureType(FeatureType sourceFeatureType) throws ProcessException {
		try {
			SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
			typeBuilder.setName(sourceFeatureType.getName().getLocalPart());
			typeBuilder.setNamespaceURI(sourceFeatureType.getName().getNamespaceURI());
			AttributeDescriptor geomAttbType = sourceFeatureType.getGeometryDescriptor();
			for (PropertyDescriptor attbType : sourceFeatureType.getDescriptors()) {
				if (attbType.equals(geomAttbType)) {
					typeBuilder.add(geomAttbType.getLocalName(), Point.class);
				} else {
					typeBuilder.add((AttributeDescriptor)attbType);
				}
			}
			typeBuilder.minOccurs(1).maxOccurs(1).nillable(false).add("nearest_distance", Double.class);
			typeBuilder.minOccurs(1).maxOccurs(1).nillable(false).add("nearest_bearing", Double.class);
			typeBuilder.setDefaultGeometry(sourceFeatureType.getGeometryDescriptor().getLocalName());
			return typeBuilder.buildFeatureType();
		} catch (Exception e) {
			LOGGER.warning("Error creating type: " + e);
			throw new WPSException("Error creating type: " + e, e);
		}
	}

	/**
	 * Create the modified feature.
	 * @param feature the source feature
	 * @param targetFeatureType the modified feature type
	 * @param nearestDistance the snap distance
	 * @param nearestBearing the snap bearing
	 * @return the modified feature
	 * @throws ProcessException error
	 */
	private SimpleFeature createTargetFeature(Feature feature, SimpleFeatureType targetFeatureType, double[] nearestPoint, Double nearestDistance, Double nearestBearing) throws ProcessException {
		try {
			AttributeDescriptor geomAttbType = targetFeatureType.getGeometryDescriptor();
			AttributeDescriptor distanceAttbType = targetFeatureType.getDescriptor("nearest_distance");
			AttributeDescriptor bearingAttbType = targetFeatureType.getDescriptor("nearest_bearing");
			Object[] attributes = new Object[targetFeatureType.getAttributeCount()];
			for (int i = 0; i < attributes.length; i++) {
				AttributeDescriptor attbType = targetFeatureType.getAttributeDescriptors().get(i);
				if (attbType.equals(geomAttbType)) {
					attributes[i] = geometryFactory.createPoint(new Coordinate(nearestPoint[0], nearestPoint[1]));
				} else if (attbType.equals(distanceAttbType)) {
					attributes[i] = nearestDistance;
				} else if (attbType.equals(bearingAttbType)) {
					attributes[i] = nearestBearing;
				} else {
					attributes[i] = feature.getProperty(attbType.getName()).getValue();
				}
			}
			return SimpleFeatureBuilder.build(targetFeatureType, attributes, feature.getIdentifier().getID());
		} catch (Exception e) {
			LOGGER.warning("Error creating feature: " + e);
			throw new WPSException("Error creating feature: " + e, e);
		}
	}

	/**
	 * Calculate the bearing between two points.
	 * @param coords the points
	 * @return the bearing
	 */
	private double calcBearing(Coordinate[] coords) {
		double y = Math.sin(coords[1].x - coords[0].x) * Math.cos(coords[1].y);
		double x = Math.cos(coords[0].y) * Math.sin(coords[1].y) - Math.sin(coords[0].y) * Math.cos(coords[1].y) * Math.cos(coords[1].x - coords[0].x);
		double brng = ((Math.atan2(y, x) * 180.0 / Math.PI) + 360) % 360;
		return brng;
	}

}
