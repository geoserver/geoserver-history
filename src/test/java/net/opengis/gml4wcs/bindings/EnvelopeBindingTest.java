/**
 * 
 */
package net.opengis.gml4wcs.bindings;

import java.util.HashMap;
import java.util.Map;

import net.opengis.gml4wcs.GML4WCSTestSupport;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml4wcs.GML;
import org.geotools.metadata.iso.extent.ExtentImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.geotools.xml.Binding;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Alessio
 *
 */
public class EnvelopeBindingTest extends GML4WCSTestSupport {
    public void testType() {
        assertEquals(GeneralEnvelope.class, binding(GML.EnvelopeWithTimePeriodType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.AFTER, binding(GML.EnvelopeWithTimePeriodType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<EnvelopeWithTimePeriod srsName=\"EPSG:4326\"><pos>2.242145683135523 32.76741502178249</pos><pos>24.676219720759967 50.37735290297434</pos><timePosition>2006-05-07T00:00:00.000Z</timePosition><timePosition>2006-05-10T00:00:00.000Z</timePosition></EnvelopeWithTimePeriod>");

        GeneralEnvelope envelope = (GeneralEnvelope) parse();

        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(CoordinateReferenceSystem.NAME_KEY, "Compound");
        properties.put(CoordinateReferenceSystem.DOMAIN_OF_VALIDITY_KEY, ExtentImpl.WORLD);
        
        CoordinateReferenceSystem crs = new DefaultCompoundCRS(properties, new CoordinateReferenceSystem[] {CRS.decode("EPSG:4326"), DefaultTemporalCRS.TRUNCATED_JULIAN});

        GeneralEnvelope testEnvelope = new GeneralEnvelope(crs);
        testEnvelope.setEnvelope(
                2.242145683135523, 
                32.76741502178249, 
                DefaultTemporalCRS.TRUNCATED_JULIAN.toValue(new DefaultPosition(new SimpleInternationalString("2006-05-07T00:00:00.000Z")).getDate()),
                24.676219720759967, 
                50.37735290297434, 
                DefaultTemporalCRS.TRUNCATED_JULIAN.toValue(new DefaultPosition(new SimpleInternationalString("2006-05-10T00:00:00.000Z")).getDate()));

        assertEquals(envelope, testEnvelope);
        
        print(encode(envelope, GML.EnvelopeWithTimePeriod));
    }
}