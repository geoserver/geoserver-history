/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wps.gs;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.wps.WPSException;
import org.geoserver.wps.jts.DescribeParameter;
import org.geoserver.wps.jts.DescribeProcess;
import org.geoserver.wps.jts.DescribeResult;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.visitor.AverageVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MedianVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.StandardDeviationVisitor;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.process.ProcessException;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.util.ProgressListener;

/**
 * Will reproject the features to another CRS. Can also be used to force a known
 * CRS onto a dataset that does not have ones
 * 
 * @author Andrea Aime
 */
@DescribeProcess(title = "reprojectFeatures", description = "Reprojects the specified features to another CRS, can also be used to force a known CRS onto a set of feaures that miss one (or that have a wrong one)")
public class AggregateProcess implements GeoServerProcess {
	// the functions this process can handle
	public enum AggregationFunction {
		Average, Max, Median, Min, StdDev, Sum;
	}

	@DescribeResult(name = "result", description = "The reprojected features")
	public Number execute(
			@DescribeParameter(name = "features", description = "The feature collection that will be aggregate") SimpleFeatureCollection features,
			@DescribeParameter(name = "aggregationAttribute", min = 0, description = "The attribute used for aggregation") String aggAttribute,
			@DescribeParameter(name = "function", description = "The aggregation function to be used") AggregationFunction function,
			ProgressListener progressListener) throws Exception {

		int attIndex = -1;
		List<AttributeDescriptor> atts = features.getSchema().getAttributeDescriptors();
		for (int i = 0; i < atts.size(); i++) {
			if(atts.get(i).getLocalName().equals(aggAttribute)) {
				attIndex = i;
				break;
			}
		}
		
		if(attIndex == -1) {
			throw new ProcessException("Could not find attribute " + atts + " the valid values are " + attNames(atts));
		}
		
		FeatureCalc calc;
		if (function == AggregationFunction.Average) {
			calc = new AverageVisitor(attIndex, features.getSchema());
		} else if (function == AggregationFunction.Max) {
			calc = new MaxVisitor(attIndex, features.getSchema());
		} else if (function == AggregationFunction.Median) {
			calc = new MedianVisitor(attIndex, features.getSchema());
		} else if (function == AggregationFunction.Min) {
			calc = new MinVisitor(attIndex, features.getSchema());
		} else if (function == AggregationFunction.StdDev) {
			// this approach is a tragedy, when you have time rewrite everything
			// using the numerically stable std dev computation algorithm listed
			// at http://www.johndcook.com/standard_deviation.html
			calc = new AverageVisitor(attIndex, features.getSchema());
			features.accepts(calc, null);
			calc = new StandardDeviationVisitor(CommonFactoryFinder.getFilterFactory(null).property(aggAttribute), 
					calc.getResult().toDouble());
		} else if (function == AggregationFunction.Sum) {
			calc = new SumVisitor(attIndex, features.getSchema());
		} else {
			throw new WPSException("Uknown method " + function);
		}

		features.accepts(calc, progressListener);

		return (Number) calc.getResult().getValue();
	}

	private List<String> attNames(List<AttributeDescriptor> atts) {
		List<String> result = new ArrayList<String>();
		for (AttributeDescriptor ad : atts) {
			result.add(ad.getLocalName());
		}
		return result;
	}

}
