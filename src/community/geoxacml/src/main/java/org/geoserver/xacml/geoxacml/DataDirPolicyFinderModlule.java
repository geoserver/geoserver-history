/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.geoxacml;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.xacml.geoxacml.config.GeoXACML;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import com.sun.xacml.AbstractPolicy;
import com.sun.xacml.EvaluationCtx;
import com.sun.xacml.ParsingException;
import com.sun.xacml.PolicyMetaData;
import com.sun.xacml.VersionConstraints;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.PolicyFinderModule;
import com.sun.xacml.finder.PolicyFinderResult;
import com.sun.xacml.support.finder.BasicPolicyFinderModule;
import com.sun.xacml.support.finder.PolicyCollection;
import com.sun.xacml.support.finder.PolicyReader;
import com.sun.xacml.support.finder.TopLevelPolicyException;

public class DataDirPolicyFinderModlule extends PolicyFinderModule {

    protected PolicyCollection policiesByReference;
    protected PolicyCollection policiesByRequest;
    
    private static final Logger logger =
        Logger.getLogger(BasicPolicyFinderModule.class.getName());

    
    public DataDirPolicyFinderModlule() {
        this.policiesByReference = new PolicyCollection();
        this.policiesByRequest = new PolicyCollection();
    }
    
    @Override
    public void init(PolicyFinder finder) {
        
        PolicyReader reader=null;
//        try {
            // TODO , enable Validation
            //reader = new PolicyReader(finder, logger,new File(GeoXACML.getSchemaValidationURL().toURI()));
            reader = new PolicyReader(finder, logger);
//        } catch (URISyntaxException e) {
//            // should not happen
//        }
        readPolicies(policiesByReference, "byId", reader);
        readPolicies(policiesByRequest, "byRequest", reader);
        
    }
    
    private void readPolicies(PolicyCollection coll, String subdir, PolicyReader reader) {
        List<String> fileNames = getXMLFileNames(subdir);
        for (String fileName : fileNames) {
            try {
                AbstractPolicy policy = reader.readPolicy(new File(fileName));
                if (! coll.addPolicy(policy))
                    if (logger.isLoggable(Level.WARNING))
                        logger.log(Level.WARNING, "tried to load the same " +
                                   "policy multiple times: " + fileName);                
            } catch (ParsingException e) {
                if (logger.isLoggable(Level.WARNING))
                    logger.log(Level.WARNING, "Error reading policy: " + fileName,e);
            }
        }
    }
    
    
    private List<String> getXMLFileNames(String subdir) {
        String parent = "file:geoxacml/"+subdir;        
        File parentDir = GeoserverDataDirectory.findDataFile(parent);
        List<String> fileNames = new ArrayList<String>();
        collectXMLFiles(parentDir, fileNames);
        return fileNames;
    }
    
    private void collectXMLFiles(File f, List<String> fileNames) {
        if (f.isFile()) {
            if (f.getName().endsWith(".xml") || f.getName().endsWith(".XML"))
                fileNames.add(f.getAbsolutePath());
        }
        if (f.isDirectory()) {
            File[] children = f.listFiles();
            if (children==null) return;
            for (File child : children) {
                collectXMLFiles(child, fileNames);
            }
        }
    }
    

    @Override
    public PolicyFinderResult findPolicy(EvaluationCtx context) {
        try {
            AbstractPolicy policy = policiesByRequest.getPolicy(context);

            if (policy == null)
                return new PolicyFinderResult();
            else
                return new PolicyFinderResult(policy);
        } catch (TopLevelPolicyException tlpe) {
            return new PolicyFinderResult(tlpe.getStatus());
        }

    }

    @Override
    public PolicyFinderResult findPolicy(URI idReference, int type, VersionConstraints constraints,
            PolicyMetaData parentMetaData) {
        
        AbstractPolicy policy = policiesByReference.getPolicy(idReference.toString(),type, constraints);

        if (policy == null)
             return new PolicyFinderResult();
        else
            return new PolicyFinderResult(policy);                
    }


    @Override
    public void invalidateCache() {
        // TODO
        super.invalidateCache();
    }

    @Override
    public boolean isIdReferenceSupported() {
        return true;
    }

    @Override
    public boolean isRequestSupported() {
        return false;
    }

}
