package org.vfny.geoserver.config.configuration;
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


public class GML2
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
        w.start("GML2");
        w.end("GML2");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("GML2");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        xs.takeEnd("GML2");
    }

    public static GML2 unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static GML2 unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static GML2 unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((GML2) d.unmarshal(xs, (GML2 .class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof GML2)) {
            return false;
        }
        GML2 tob = ((GML2) ob);
        return true;
    }

    public int hashCode() {
        int h = 0;
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<GML2");
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Database.newDispatcher();
    }

}
