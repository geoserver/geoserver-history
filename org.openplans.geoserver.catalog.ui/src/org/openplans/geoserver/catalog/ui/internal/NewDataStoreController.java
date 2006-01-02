package org.openplans.geoserver.catalog.ui.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.openplans.geoserver.catalog.ui.DataStoreUIHelp;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class NewDataStoreController extends AbstractController {

	/** model key for data store factory list **/
	public static final String DATASTORES = "datastores";
	
	protected ModelAndView handleRequestInternal(
		HttpServletRequest request, HttpServletResponse response
	) throws Exception {
		
		//populate model with available datastores
		Iterator itr = DataStoreFinder.getAvailableDataStores();
		ArrayList list = new ArrayList();
		while(itr.hasNext()) {
			DataStoreFactorySpi factory = (DataStoreFactorySpi) itr.next();
			
			//look for an overode
			DataStoreFactorySpi overide = findOveride(factory);
			
			if (overide != null) {
				list.add(overide);	
			}
			else {
				list.add(factory);
			}
		}
		
		//sort list by display name
		Collections.sort(
			list, new Comparator() {

				public int compare(Object o1, Object o2) {
					DataStoreFactorySpi ds1 = (DataStoreFactorySpi)o1;
					DataStoreFactorySpi ds2 = (DataStoreFactorySpi)o2;
					
					String name1 = ds1.getDisplayName();
					String name2 = ds2.getDisplayName();
					
					if (name1 != null)
						return name1.compareTo(name2);
					if (name2 != null) 
						return -1*name2.compareTo(name1);
					
					return 0;
				}
			}
		);
		
		HashMap model = new HashMap();
		model.put(DATASTORES,list);
		
		return new ModelAndView("newDataStore",model);
	}
	
	/**
	 * Process beans of type {@link DataStoreUIHelp} to overide ui names, 
	 * description,etc...
	 * 
	 */
	protected DataStoreFactorySpi findOveride(DataStoreFactorySpi factory) {
		ApplicationContext cxt = getApplicationContext();
		
		Collection helps = cxt.getBeansOfType(DataStoreUIHelp.class).values();
		for (Iterator itr = helps.iterator(); itr.hasNext();) {
			DataStoreUIHelp help = (DataStoreUIHelp) itr.next();
			try {
				Class clazz = help.getDataStoreFactoryClass();
				if (clazz != null && clazz.equals(factory.getClass())) {
					return new DelegateDataStoreFactorySpi(factory,help);
				}
			}
			catch(Throwable t) {
				//TODO: log this
			}
		}
			
		return null;
	}
}
