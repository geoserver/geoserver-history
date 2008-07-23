/**
 * 
 */
package net.opengis.wcs.bindings;

import net.opengis.wcs.WCS10TestSupport;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.wcs.WCS;
import org.geotools.xml.Binding;

/**
 * @author Alessio
 *
 */
public class LonLatEnvelopeBaseBindingTest extends WCS10TestSupport {
    public void testType() {
        assertEquals(GeneralEnvelope.class, binding(WCS.LonLatEnvelopeBaseType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(WCS.LonLatEnvelopeBaseType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<lonLatEnvelope srsName=\"WGS84(DD)\"><pos>2.242145683135523 32.76741502178249 0.0</pos><pos>24.676219720759967 50.37735290297434 0.0</pos></lonLatEnvelope>");

        GeneralEnvelope envelope = (GeneralEnvelope) parse();
        
        GeneralEnvelope testEnvelope = new GeneralEnvelope(DefaultGeographicCRS.WGS84_3D);
        testEnvelope.setEnvelope(2.242145683135523, 32.76741502178249, 0.0, 24.676219720759967, 50.37735290297434, 0.0);

        assertEquals(envelope, testEnvelope);
        
        print(encode(envelope, WCS.lonLatEnvelope));
    }
}
