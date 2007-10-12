package org.openplans.security.test;

import org.geoserver.test.GeoServerTestSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class LoginTest extends GeoServerTestSupport{

    private final String secret;

    public LoginTest() throws IOException{
        File f = new File("/usr/lib/secret.txt");

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        secret = br.readLine();
    }

    public void testLoggedInToAdminPage(){
//         System.out.println("What's going on?");
//         assertTrue(true);
    }
}
