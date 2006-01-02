package org.openplans.geoserver.catalog.ui.internal;

import java.io.IOException;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.openplans.geoserver.catalog.ui.DataStoreUIHelp;

/**
 * DataStoreFactorySpi which delegates / overides information in a delegate.
 * <p>
 * This class is not public api and should not be used by clients.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DelegateDataStoreFactorySpi implements DataStoreFactorySpi {

	/** the delegate **/
	DataStoreFactorySpi delegate;
	/** help / overrides **/
	DataStoreUIHelp help;
	/** param overrides **/
	Param[] params;
	
	public DelegateDataStoreFactorySpi(DataStoreFactorySpi delegate,DataStoreUIHelp help) {
		this.delegate = delegate;
		this.help = help != null ? help : new DataStoreUIHelp(delegate.getClass());
	}
	
	public DataStore createDataStore(Map params) throws IOException {
		return delegate.createDataStore(params);
	}

	public DataStore createNewDataStore(Map params) throws IOException {
		return delegate.createNewDataStore(params);
	}
	
	public DataStoreFactorySpi getDelegate() {
		return delegate;
	}
	
	public String getDisplayName() {
		if (help.getDisplayName() != null) 
			return help.getDisplayName();
		
		return delegate.getDisplayName();
	}

	public String getDescription() {
		if (help.getDescription() != null)
			return help.getDescription();
		
		return delegate.getDescription();
	}

	public Param[] getParametersInfo() {
		if (params != null) 
			return params;
		
		//create copy of params overriding necessary stuff
		Param[] dparams = delegate.getParametersInfo();
		if (dparams != null) {
			params = new Param[dparams.length];
			
			for (int i = 0; i < dparams.length; i++) {
				Param old = dparams[i];
				
				String key = help.getKey(delegate,old);
				if (key == null) 
					key = old.key;
					
				String desc = help.getDescription(delegate,old);
				if (desc == null) 
					desc = old.description;
				
				Object sample = help.getSample(delegate,old);
				if (sample == null) 
					sample = old.sample; 
					
				params[i] = new Param(key,old.type,desc,old.required,sample);
			}	
		}
		
		return null;
	}

	public boolean canProcess(Map params) {
		return delegate.canProcess(params);
	}

	public boolean isAvailable() {
		return delegate.isAvailable();
	}

	public Map getImplementationHints() {
		return delegate.getImplementationHints();
	}
}




