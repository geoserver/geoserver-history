/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/*
 * Created on Jun 28, 2004
 *
 */
package org.vfny.geoserver.action.validation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataRepository;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.Validator;

import com.vividsolutions.jts.geom.Envelope;

/**
 * ValidatorRunnable<br>
 * @author bowens<br>
 * Created Jun 28, 2004<br>
 * @version <br>
 * 
 * <b>Puropse:</b><br>
 * <p>
 * This is a thread that will do the work of gathering the proper plugIns and
 * calling the Validator.
 * </p>
 */
public class ValidatorRunnable implements Runnable
{
	Validator validator;
	Map dataStores;
	DataRepository dataRepository;
	
	public ValidatorRunnable(ValidationProcessor vp, DataRepository dataRepo)
	{
		validator = new Validator(dataRepo, vp);
		dataRepository = dataRepo;
		Set dataStoreSet = dataRepo.getDataStores();
		Map stores = new HashMap();
		//stores.put()
		try
		{	
			dataRepo.getDataStores();
			dataStores = dataRepo.types();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
		if (validator == null)
			return;
		
		/** run FEATURE validations */
		Set keys = dataStores.keySet();
		Iterator it = keys.iterator();
		while (it.hasNext())
		{
			String key = it.next().toString();
			DataStore ds = (DataStore)dataStores.get(key);
			
			try {
				String[] ss = ds.getTypeNames();
				for (int j = 0; j < ss.length; j++)
				{
					FeatureReader fReader = ds.getFeatureSource(ss[j]).getFeatures().reader();
					//sources.put(dsc.getId() +":"+ss[j], fReader);
		
					validator.featureValidation(key, fReader);
					//vProcessor.runFeatureTests(dsc.getId(),fs.getSchema(),
					//	fs.getFeatures().reader(), (ValidationResults) vResults);
//					System.out.println("Feature Test Results for " + dsKey + ":"
//						+ ss[j]);
//					System.out.println(vResults.toString());
				} 
			}catch (Exception e) {
			   e.printStackTrace();
			}
			
		}
		/** ------------------------------------------------------------------ */
		
		
		/** run INTEGRITY validations */
		Envelope env = new Envelope(Integer.MIN_VALUE, Integer.MIN_VALUE,
									Integer.MAX_VALUE, Integer.MAX_VALUE);
		try
		{
			validator.integrityValidation(dataStores, env);
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
	}
}
