/*
 * Created on Jun 28, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.vfny.geoserver.action.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.Repository;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.ValidationResults;
import org.geotools.validation.Validator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;


/**
 * @author rgould
 * @author bowens
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ValidationRunnable implements Runnable {
    /** Standard logging instance for class */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses");
    private Map testSuites;
    private Map plugins;
    private DataConfig dataConfig;
    private ServletContext context;
    private HttpServletRequest request;
    Validator validator;
    ValidationProcessor gv;
    public TestValidationResults results; // I get filled up with goodies
    Repository repository;
    public final static String KEY = "validationTestDoItThread.key";

    //public ValidationRunnable(Map ts, Map plugins, DataConfig dataConfig, ServletContext context, HttpServletRequest request) throws Exception
    public ValidationRunnable(HttpServletRequest request) {
        //		this.testSuites = ts;		// what tests we are actually running
        //		this.plugins = plugins;		// every possible plugin
        //		this.dataConfig = dataConfig;
        //		this.context = context;
        this.request = request;

        /*
                        LOGGER.finer("testSuites.size() = " + testSuites.size());
                        LOGGER.finer("plugins.size() = " + plugins.size());
                        LOGGER.finer("" + (TestSuiteDTO) testSuites.values().toArray()[0]);

                        //DataConfig dataConfig = (DataConfig) getDataConfig();
                        Map dataStoreConfigs = dataConfig.getDataStores();
                        DefaultRepository dataRepository = new DefaultRepository();
                        Iterator it = dataStoreConfigs.keySet().iterator();

                        // get all the data stores and build up our dataRepository
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
                        */
    }

    public void setup(TestValidationResults vr, Repository repo, Map plugins, Map testSuites)
        throws Exception {
        gv = new ValidationProcessor();
        gv.load(plugins, testSuites);
        results = vr;
        repository = repo;
        validator = new Validator(repository, gv);
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        //GeoValidator gv = new GeoValidator(testSuites,plugins);
        //Map dataStores = dataConfig.getDataStores();
        //TestValidationResults vr = runTransactions(dataStores,gv,context);
        request.getSession().setAttribute(TestValidationResults.CURRENTLY_SELECTED_KEY, results);

        Map dataStores = repository.getFeatureSources();
        Iterator it = dataStores.entrySet().iterator();

        // Go through each data store and run the featureValidation test on
        //  each feature type
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String typeRef = (String) entry.getKey();
            FeatureSource <SimpleFeatureType, SimpleFeature> featureSource = (FeatureSource) entry.getValue();
            String dataStoreId = typeRef.split(":")[0];

            try {
                LOGGER.finer(dataStoreId + ": feature validation, " + featureSource);

                FeatureCollection<SimpleFeatureType, SimpleFeature> features = featureSource.getFeatures();
                validator.featureValidation(dataStoreId, features, results);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        /** ------------------------------------------------------------------ */

        /** run INTEGRITY validations */

        // this is stupid
        ReferencedEnvelope env = new ReferencedEnvelope(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE,
                Integer.MAX_VALUE,null);

        // a map of typeref -> DataSource
        try {
            Map featureSources = repository.getFeatureSources();
            LOGGER.finer("integrity tests entry for " + featureSources.size() + " dataSources.");
            validator.integrityValidation(featureSources, env, results);
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
            System.err.println("An error occured: Cannot run without a ValidationProcessor.");

            return null;
        }

        TestValidationResults vr = new TestValidationResults();
        Iterator i = dsm.keySet().iterator();

        //TODO: we only get one datastore here when we may need more than that
        // do another pass through it and get those typesNames
        List dataStores = new ArrayList();
        try {
            while (i.hasNext()) {
                Map sources = new HashMap();
                String key = i.next().toString();
                DataStoreConfig dsc = (DataStoreConfig) dsm.get(key);
    
                try {
                    DataStore ds = dsc.findDataStore(sc);
                    dataStores.add(ds);
                    String[] ss = ds.getTypeNames();
    
                    for (int j = 0; j < ss.length; j++) {
                        FeatureSource<SimpleFeatureType, SimpleFeature> fs = ds.getFeatureSource(ss[j]);
                        sources.put(dsc.getId() + ":" + ss[j], fs);
    
                        //v.runFeatureTests(dsc.getId(),fs.getSchema(),
                        //    fs.getFeatures().collection(), (ValidationResults) vr);
                        System.out.println("Feature Test Results for " + key + ":" + ss[j]);
                        System.out.println(vr.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
    
                ReferencedEnvelope env = new ReferencedEnvelope(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE,
                        Integer.MAX_VALUE, null);
    
                try {
                    v.runIntegrityTests(sources.keySet(), sources, env, (ValidationResults) vr);
                    System.out.println("Feature Integrety Test Results");
                    System.out.println(vr.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {
            for (Iterator it = dataStores.iterator(); it.hasNext();) {
                DataStore ds = (DataStore) it.next();
                ds.dispose();
            }
        }

        return vr;
    }
}
