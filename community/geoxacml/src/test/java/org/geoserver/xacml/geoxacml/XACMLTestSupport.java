/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.geoxacml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.ProviderManager;
import org.acegisecurity.providers.TestingAuthenticationProvider;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerTestSupport;

public abstract class XACMLTestSupport extends GeoServerTestSupport {
    
    
    
    
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
                                                
        ProviderManager providerManager = (ProviderManager) GeoServerExtensions.bean("authenticationManager");
        List<AuthenticationProvider> list = new ArrayList<AuthenticationProvider>();
        list.add(new TestingAuthenticationProvider());
        providerManager.setProviders(list);
        
    }

    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
      }
    
    private void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }

    protected void geoserverDataDirFromTest() throws IOException{
        geoserverDataDirFromPath("src/test/resources/geoserverdatadir/");
    }
    
    protected void geoserverDataDirFromMain() throws IOException{
        geoserverDataDirFromPath("src/main/resources/geoserverdatadir/");
    }

    
    protected void geoserverDataDirFromPath(String path) throws IOException{
        File dir = new File( testData.getDataDirectoryRoot(), DataDirPolicyFinderModlule.BASE_DIR );
        deleteDirectory(dir);
        File srcDir = new File(path+DataDirPolicyFinderModlule.BASE_DIR);
        copyDirectory(srcDir, dir);
    }    

            
}
