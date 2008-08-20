/**
 * 
 */
package net.opengis.wcs10.bindings;

import java.util.List;

import net.opengis.wcs10.WCS10TestSupport;

import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.geotools.wcs.WCS;
import org.geotools.xml.Binding;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;
import org.opengis.temporal.Position;

/**
 * @author Alessio
 * 
 */
public class TimeSequenceBindingTest extends WCS10TestSupport {
    public void testType() {
        assertEquals(List.class, binding(WCS.TimeSequenceType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(WCS.TimeSequenceType)
                .getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<temporalDomain><timePosition>2006-05-07T03:00:00.000Z</timePosition><timePosition>2006-05-07T06:00:00.000Z</timePosition><timePosition>2006-05-07T09:00:00.000Z</timePosition></temporalDomain>");

        List timeSequence1 = (List) parse();

        Position position1 = new DefaultPosition(new SimpleInternationalString("2006-05-07T03:00:00.000+0000"));

        assertEquals(position1.getDate(), ((Position)timeSequence1.get(0)).getDate());

        buildDocument("<temporalDomain><timePeriod><beginPosition>2006-05-07T00:00:00.000Z</beginPosition><endPosition>2006-05-10T00:00:00.000Z</endPosition></timePeriod></temporalDomain>");

        List timeSequence2 = (List) parse();

        Instant beginning2 = new DefaultInstant(new DefaultPosition(new SimpleInternationalString("2006-05-07T00:00:00.000+0000")));
        Instant ending2 = new DefaultInstant(new DefaultPosition(new SimpleInternationalString("2006-05-10T00:00:00.000+0000")));

        assertEquals(beginning2.toString(), ((Period)timeSequence2.get(0)).getBeginning().toString());
        assertEquals(ending2.toString(), ((Period)timeSequence2.get(0)).getEnding().toString());
        
        print(encode(timeSequence1, WCS.temporalDomain));

        print(encode(timeSequence2, WCS.temporalDomain));
    }
}
