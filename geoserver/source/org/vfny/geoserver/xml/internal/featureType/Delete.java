package org.vfny.geoserver.xml.internal.featureType;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class Delete
    extends MarshallableObject
    implements Element
{


    public void validateThis()
        throws LocalValidationException
    {
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("Delete");
        w.end("Delete");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Delete");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        xs.takeEnd("Delete");
    }

    public static Delete unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static Delete unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static Delete unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((Delete) d.unmarshal(xs, (Delete.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Delete)) {
            return false;
        }
        Delete tob = ((Delete) ob);
        return true;
    }

    public int hashCode() {
        int h = 0;
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Delete");
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        Dispatcher d = new Dispatcher();
        d.register("Delete", (Delete.class));
        d.register("FeatureType", (FeatureType.class));
        d.register("Insert", (Insert.class));
        d.register("LatLonBoundingBox", (LatLonBoundingBox.class));
        d.register("Lock", (Lock.class));
        d.register("MetadataURL", (MetadataURL.class));
        d.register("Operations", (Operations.class));
        d.register("Query", (Query.class));
        d.register("Update", (Update.class));
        d.freezeElementNameMap();
        return d;
    }

}
