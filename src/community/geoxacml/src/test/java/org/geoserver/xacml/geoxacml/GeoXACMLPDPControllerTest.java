package org.geoserver.xacml.geoxacml;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;


import org.geoserver.test.GeoServerTestSupport;

public class GeoXACMLPDPControllerTest extends GeoServerTestSupport {

    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        File dir = new File( testData.getDataDirectoryRoot(), "geoxacml" );
        deleteDirectory(dir);
        File srcDir = new File("src/test/resources/geoserverdatadir/geoxacml");
        copyDirectory(srcDir, dir);    
        
    }
    
    public void testDirExists() throws Exception {
        File dir = new File( testData.getDataDirectoryRoot(), "geoxacml" );
        assertTrue( dir.exists() );
        
    }
    
    public void testAlice() throws Exception {
        File request = new File ("src/test/resources/requestAlice.xml");
        String xml = getXMLRequest(request);
        InputStream resp = post("geoxacml",xml);
        
        
        System.out.println("RESPONSE");       
        byte[] bytes = new byte[512];
        while (resp.read(bytes)!=-1) {
            System.out.println(new String(bytes));
        }
    }

    
    private String getXMLRequest(File f) {
        try {
            StringBuffer buff = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String line;
            while ((line=reader.readLine())!=null)
                buff.append(line);        
            reader.close();
            return buff.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    
}
