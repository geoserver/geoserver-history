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


public class Update
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
        w.start("Update");
        w.end("Update");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Update");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        xs.takeEnd("Update");
    }

    public static Update unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static Update unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static Update unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((Update) d.unmarshal(xs, (Update.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Update)) {
            return false;
        }
        Update tob = ((Update) ob);
        return true;
    }

    public int hashCode() {
        int h = 0;
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Update");
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Delete.newDispatcher();
    }

}
