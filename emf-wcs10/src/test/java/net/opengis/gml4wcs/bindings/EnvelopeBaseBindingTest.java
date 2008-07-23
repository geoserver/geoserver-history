/**
 * 
 */
package net.opengis.gml4wcs.bindings;

import net.opengis.gml4wcs.GML4WCSTestSupport;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.gml4wcs.GML;
import org.geotools.referencing.CRS;
import org.geotools.xml.Binding;

/**
 * @author Alessio
 *
 */
public class EnvelopeBaseBindingTest extends GML4WCSTestSupport {
    public void testType() {
        assertEquals(GeneralEnvelope.class, binding(GML.EnvelopeType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GML.EnvelopeType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<Envelope srsName=\"EPSG:4326\"><pos>2.242145683135523 32.76741502178249</pos><pos>24.676219720759967 50.37735290297434</pos></Envelope>");

        GeneralEnvelope envelope = (GeneralEnvelope) parse();
        
        GeneralEnvelope testEnvelope = new GeneralEnvelope(
                new double[] {2.242145683135523, 32.76741502178249},
                new double[] {24.676219720759967, 50.37735290297434}
        );
        testEnvelope.setCoordinateReferenceSystem(CRS.decode("EPSG:4326"));

        assertTrue(CRS.equalsIgnoreMetadata(envelope.getCoordinateReferenceSystem(), testEnvelope.getCoordinateReferenceSystem()));

        assertEquals(envelope.getLowerCorner().getOrdinate(0), testEnvelope.getLowerCorner().getOrdinate(0));
        assertEquals(envelope.getLowerCorner().getOrdinate(1), testEnvelope.getLowerCorner().getOrdinate(1));

        assertEquals(envelope.getUpperCorner().getOrdinate(0), testEnvelope.getUpperCorner().getOrdinate(0));
        assertEquals(envelope.getUpperCorner().getOrdinate(1), testEnvelope.getUpperCorner().getOrdinate(1));

        print(encode(envelope, GML.Envelope));
    }
}
