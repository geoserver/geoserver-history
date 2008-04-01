package org.geoserver.rest;

import org.geoserver.test.GeoServerTestSupport;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap; import java.util.Iterator;

import org.restlet.resource.Representation;

/**
 * A set of generic tests for output formats to just check that they can read their own output. 
 * Format classes should probably still have their own custom tests to verify the format itself is correct.
 */
public class FormatTest extends GeoServerTestSupport {
    List formats;

    public void setUp(){
        formats = new ArrayList();
        formats.add(new AutoXMLFormat());
        formats.add(new JSONFormat());
    }

    public void tearDown(){
        formats.clear();
    }

    public void testFormatMap(){
        Iterator it = formats.iterator();
        while (it.hasNext()){
            try{
                DataFormat format = (DataFormat)it.next();
                Map input = new HashMap();
                input.put("Hello", "Goodbye");

                Map result = (Map)format.readRepresentation(format.makeRepresentation(input));
                assertEquals(result.size(), input.size());
                Iterator mapIt = input.entrySet().iterator();
                while (mapIt.hasNext()){
                    Map.Entry ent = (Map.Entry)mapIt.next();
                    assertEquals(result.get(ent.getKey()), ent.getValue());
                }
            } catch (Exception e){
                // should log this or something? does JUnit let you log a failure without quitting the test?
            }
        }
    }

    public void testFormatList(){
        Iterator it = formats.iterator();
        while (it.hasNext()){
            try{
                DataFormat format = (DataFormat)it.next();
                List input = new ArrayList();
                input.add("Hello");

                List result = (List)format.readRepresentation(format.makeRepresentation(input));
                assertEquals(result.size(), input.size());
                for (int i = 0; i < result.size(); i++){
                    assertEquals(input.get(i), result.get(i)); }
            } catch (Exception e){
                // should log this or something? does JUnit let you log a failure without quitting the test?
            }
        }
    }

    // TODO: Should we worry about serializing single strings?  Arbitrary objects?
    public void dontTestFormatScalar(){
        Iterator it = formats.iterator();

        while (it.hasNext()){
            DataFormat format = (DataFormat)it.next();
            String input = "Hello";

            String result = (String) format.readRepresentation(format.makeRepresentation(input));
            assertEquals(input, result);
        }
    }
}
