/*
 * Created on Jun 28, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.vfny.geoserver.action.validation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultRepository;
import org.geotools.data.FeatureSource;

import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.ValidationResults;
import org.geotools.validation.Validator;
import org.geotools.validation.dto.TestDTO;
import org.geotools.validation.dto.TestSuiteDTO;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.global.GeoValidator;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author rgould
 * @author bowens
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValidationRunnable implements Runnable {
	
	/** Standard logging instance for class */
	private static final Logger LOGGER = Logger.getLogger(
			"org.vfny.geoserver.responses");
	
	private Map testSuites;
	private Map plugins;
	private DataConfig dataConfig;
	private ServletContext context;
	private HttpServletRequest request;
	Validator validator;
	public TestValidationResults results;	// I get filled up with goodies
	
	public final static String KEY = "validationTestDoItThread.key";

	public ValidationRunnable(TestValidationResults vr, Map ts, Map plugins, DataConfig dataConfig, ServletContext context, HttpServletRequest request) throws Exception 
	{
		this(ts, plugins, dataConfig, context, request);
		results = vr;
	}
	
	public ValidationRunnable(Map ts, Map plugins, DataConfig dataConfig, ServletContext context, HttpServletRequest request) throws Exception 
	{
		this.testSuites = ts;		// what tests we are actually running
		this.plugins = plugins;		// every possible plugin
		this.dataConfig = dataConfig;
		this.context = context;
		this.request = request;
		
		if (results == null)
			results = new TestValidationResults();
		
		ValidationProcessor gv = new ValidationProcessor();

		gv.load(plugins, testSuites);

		LOGGER.finer("testSuites.size() = " + testSuites.size());
		LOGGER.finer("plugins.size() = " + plugins.size());
		LOGGER.finer("" + (TestSuiteDTO) testSuites.values().toArray()[0]);
		
		//DataConfig dataConfig = (DataConfig) getDataConfig();
		Map dataStoreConfigs = dataConfig.getDataStores();
		DefaultRepository dataRepository = new DefaultRepository();
		Iterator it = dataStoreConfigs.keySet().iterator();
		
		/** get all the data stores and build up our dataRepository */
		while (it.hasNext())
		{
			String dsKey = it.next().toString();
			DataStoreConfig dsc = (DataStoreConfig) dataStoreConfigs.get(dsKey);
			DataStore ds = null;
			try {
				ds = dsc.findDataStore(context);
				dataRepository.register(dsKey, ds);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		validator = new Validator(dataRepository, gv);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		//GeoValidator gv = new GeoValidator(testSuites,plugins);
        Map dataStores = dataConfig.getDataStores();
		//TestValidationResults vr = runTransactions(dataStores,gv,context);

		request.getSession().setAttribute(TestValidationResults.CURRENTLY_SELECTED_KEY,results);        
        
		/** run FEATURE validations */
		Iterator it = dataStores.keySet().iterator();
		while (it.hasNext())
		{
			String key = (String) it.next();
			LOGGER.finer("=========================================");
			LOGGER.finer("key = " + key);
			LOGGER.finer("=========================================");
			DataStoreConfig ds = (DataStoreConfig) dataStores.get(key);
			String dsID = ds.getId();
			LOGGER.finer("dsID = " + dsID);
			DataStore store = null;
			String[] typeNames = null;
			try {
				store = ds.findDataStore(context);
				typeNames = store.getTypeNames();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			for (int i=0; i<typeNames.length; i++)
			{
				try {
					LOGGER.finer("feature validation, typeName[" + i + "] = " + typeNames[i]);
					validator.featureValidation(dsID, store.getFeatureSource(typeNames[i]).getFeatures().reader(), results);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		/** ------------------------------------------------------------------ */
		
		/** run INTEGRITY validations */
		
		// this is stupid
		Envelope env = new Envelope(Integer.MIN_VALUE, Integer.MIN_VALUE,
									Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		try {
			validator.integrityValidation(dataStores, env, results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        results.run = true;

	}
	
    private TestValidationResults runTransactions(Map dsm, ValidationProcessor v, ServletContext sc) {
        if ((dsm == null) || (dsm.size() == 0)) {
            System.out.println("No Datastores were defined.");

            return null;
        }

        if (v == null) {
            System.err.println(
                "An error occured: Cannot run without a ValidationProcessor.");

            return null;
        }

        TestValidationResults vr = new TestValidationResults();
        Iterator i = dsm.keySet().iterator();

		//TODO: we only get one datastore here when we may need more than that
		// do another pass through it and get those typesNames
        while (i.hasNext()) {
            Map sources = new HashMap();
            String key = i.next().toString();
            DataStoreConfig dsc = (DataStoreConfig) dsm.get(key);
            try {
            	DataStore ds = dsc.findDataStore(sc);
            	String[] ss = ds.getTypeNames();
            	for (int j = 0; j < ss.length; j++) {
                    FeatureSource fs = ds.getFeatureSource(ss[j]);
                    sources.put(dsc.getId() +":"+ss[j], fs);

                    //v.runFeatureTests(dsc.getId(),fs.getSchema(),
                    //    fs.getFeatures().collection(), (ValidationResults) vr);
                    System.out.println("Feature Test Results for " + key + ":"
                        + ss[j]);
                    System.out.println(vr.toString());
                } 
            }catch (Exception e) {
               e.printStackTrace();
            }

            Envelope env = new Envelope(Integer.MIN_VALUE, Integer.MIN_VALUE,
                        Integer.MAX_VALUE, Integer.MAX_VALUE);

            try {
                v.runIntegrityTests(sources.keySet(), sources, env, (ValidationResults) vr);
                System.out.println("Feature Integrety Test Results");
                System.out.println(vr.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return vr;
    }
}
