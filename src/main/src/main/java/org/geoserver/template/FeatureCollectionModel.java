/* Copyright (c) 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.template;

import java.util.Iterator;

import org.geotools.feature.FeatureCollection;

import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.IteratorModel;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

/**
 * This is a template collection model that will try and clean up
 * after any iterator produced using a finalizer.
 * <p>
 * This class is needed as our FeatureCollection requires an iterator
 * to be close after use.
 * @author Jody Garnett (Refractions Research, Inc.)
 */
public class FeatureCollectionModel implements TemplateCollectionModel {
	@SuppressWarnings("unchecked")
	private FeatureCollection features;	
	@SuppressWarnings("unchecked")
	public Iterator iterator;
	private BeansWrapper bean;
	
	public FeatureCollectionModel( FeatureCollection<?,?> features, BeansWrapper bean) {
		this.features = features;
		this.bean = bean;
	}
	public TemplateModelIterator iterator() throws TemplateModelException {
		if( iterator != null ){
			// only one iterator at a time
			features.close( iterator );
		}
		iterator = features.iterator();
		return new IteratorModel( iterator, bean );
	}
	@Override
	protected void finalize() throws Throwable {
		if( features != null && iterator != null ){
			features.close( iterator );
			features = null;
			iterator = null;
		}
		super.finalize();
	}

}
