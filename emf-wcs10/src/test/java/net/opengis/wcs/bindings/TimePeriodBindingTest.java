/**
 * 
 */
package net.opengis.wcs.bindings;

import net.opengis.wcs.WCS10TestSupport;

import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.SimpleInternationalString;
import org.geotools.wcs.WCS;
import org.geotools.xml.Binding;
import org.opengis.temporal.Instant;
import org.opengis.temporal.Period;

/**
 * @author Alessio
 *
 */
public class TimePeriodBindingTest extends WCS10TestSupport {
    public void testType() {
        assertEquals(Period.class, binding(WCS.TimePeriodType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(WCS.TimePeriodType).getExecutionMode());
    }

    public void testParse() throws Exception {
        buildDocument("<timePeriod><beginPosition>2006-05-07T00:00:00.000Z</beginPosition><endPosition>2006-05-10T00:00:00.000Z</endPosition></timePeriod>");

        Period timePeriod = (Period) parse();

        Instant beginning = new DefaultInstant(new DefaultPosition(new SimpleInternationalString("2006-05-07T00:00:00.000+0000")));
        Instant ending = new DefaultInstant(new DefaultPosition(new SimpleInternationalString("2006-05-10T00:00:00.000+0000")));
        
        assertEquals(beginning.toString(), timePeriod.getBeginning().toString());
        assertEquals(ending.toString(), timePeriod.getEnding().toString());
        
        print(encode(timePeriod, WCS.timePeriod));
    }
}
