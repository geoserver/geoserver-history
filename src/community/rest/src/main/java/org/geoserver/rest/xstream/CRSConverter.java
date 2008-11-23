package org.geoserver.rest.xstream;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.referencing.CRS;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * Converter for CRS's using the CRS database to avoid storing the entire CRS every time.
 * @author David Winslow <dwinslow@openplans.org>
 */
public class CRSConverter implements SingleValueConverter {
    public Object fromString(String str){
        try{
            return CRS.decode(str);
        } catch (Exception e){
            throw new ConversionException(e);
        }
    }

    public String toString(Object obj){
        try{
            return CRS.lookupIdentifier((CoordinateReferenceSystem)obj, true);
        } catch (Exception e){
            throw new ConversionException(e);
        }
    }

    public boolean canConvert(Class cls){
        return CoordinateReferenceSystem.class.isAssignableFrom(cls);
    }
}
