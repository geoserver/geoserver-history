/*
 * Created on Jun 28, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.vfny.geoserver.action.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;

import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.ValidationResults;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.global.GeoValidator;

import com.vividsolutions.jts.geom.Envelope;

/**
 * @author rgould
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValidationRunnable implements Runnable {
	
	private Map ts;
	private Map plugins;
	private DataConfig dataConfig;
	private ServletContext context;
	private HttpServletRequest request;
	
	public final static String KEY = "validationTestDoItThread.key";

	public void setup(Map ts, Map plugins, DataConfig dataConfig, ServletContext context, HttpServletRequest request) {
		this.ts = ts;
		this.plugins = plugins;
		this.dataConfig = dataConfig;
		this.context = context;
		this.request = request;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		GeoValidator gv = new GeoValidator(ts,plugins);
        Map dataStores = dataConfig.getDataStores();
		//TestValidationResults vr = runTransactions(dataStores,gv,context);
        TestValidationResults vr = new TestValidationResults();

		request.getSession().setAttribute(TestValidationResults.CURRENTLY_SELECTED_KEY,vr);        
        
        for (int i = 0; i < 100000; i++) {
            vr.errors.put(null, "Bork"+i);
            System.out.println("bork"+i);
            try {
                this.wait(100000);
            } catch (Exception e) {}
        }
        vr.run = true;

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

                    v.runFeatureTests(dsc.getId(),fs.getSchema(),
                        fs.getFeatures().collection(), (ValidationResults) vr);
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
