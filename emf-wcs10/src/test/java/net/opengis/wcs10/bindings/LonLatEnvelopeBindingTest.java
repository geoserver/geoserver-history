/**
 * 
 */
package net.opengis.wcs10.bindings;

import java.util.HashMap;
import java.util.Map;

import net.opengis.wcs10.WCS10TestSupport;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.extent.ExtentImpl;
import org.geotools.referencing.crs.DefaultCompoundCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.crs.DefaultTemporalCRS;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.geotools.wcs.WCS;
import org.geotools.xml.Binding;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Alessio
 *
 */
public class LonLatEnvelopeBindingTest extends WCS10TestSupport {
    public void testType() {
        assertEquals(GeneralEnvelope.class, binding(WCS.LonLatEnvelopeType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.AFTER, binding(WCS.LonLatEnvelopeType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<lonLatEnvelope srsName=\"WGS84(DD)\"><pos>2.242145683135523 32.76741502178249 0.0</pos><pos>24.676219720759967 50.37735290297434 0.0</pos><timePosition>2006-05-07T00:00:00.000Z</timePosition><timePosition>2006-05-10T00:00:00.000Z</timePosition></lonLatEnvelope>");

        GeneralEnvelope envelope = (GeneralEnvelope) parse();

        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(CoordinateReferenceSystem.NAME_KEY, "WGS84");
        properties.put(CoordinateReferenceSystem.DOMAIN_OF_VALIDITY_KEY, ExtentImpl.WORLD);
        
        CoordinateReferenceSystem crs = new DefaultCompoundCRS(properties, new CoordinateReferenceSystem[] {DefaultGeographicCRS.WGS84_3D, DefaultTemporalCRS.TRUNCATED_JULIAN});

        GeneralEnvelope testEnvelope = new GeneralEnvelope(crs);
        testEnvelope.setEnvelope(
                2.242145683135523, 
                32.76741502178249, 
                0.0,
                DefaultTemporalCRS.TRUNCATED_JULIAN.toValue(new DefaultPosition(new SimpleInternationalString("2006-05-07T00:00:00.000Z")).getDate()),
                24.676219720759967, 
                50.37735290297434, 
                0.0,
                DefaultTemporalCRS.TRUNCATED_JULIAN.toValue(new DefaultPosition(new SimpleInternationalString("2006-05-10T00:00:00.000Z")).getDate()));

        assertEquals(envelope, testEnvelope);
        
        print(encode(envelope, WCS.lonLatEnvelope));
    }
}