package org.vfny.geoserver.xml.internal.featureType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class Operations
    extends MarshallableObject
    implements Element
{

    private List _OperationsTypes = PredicatedLists.createInvalidating(this, new OperationsTypesPredicate(), new ArrayList());
    private PredicatedLists.Predicate pred_OperationsTypes = new OperationsTypesPredicate();

    public List getOperationsTypes() {
        return _OperationsTypes;
    }

    public void deleteOperationsTypes() {
        _OperationsTypes = null;
        invalidate();
    }

    public void emptyOperationsTypes() {
        _OperationsTypes = PredicatedLists.createInvalidating(this, pred_OperationsTypes, new ArrayList());
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_OperationsTypes == null) {
            throw new MissingContentException("OperationsTypes");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
        for (Iterator i = _OperationsTypes.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("Operations");
        for (Iterator i = _OperationsTypes.iterator(); i.hasNext(); ) {
            m.marshal(((MarshallableObject) i.next()));
        }
        w.end("Operations");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Operations");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        {
            List l = PredicatedLists.create(this, pred_OperationsTypes, new ArrayList());
            while ((((xs.atStart("Lock")||xs.atStart("Query"))||xs.atStart("Insert"))||xs.atStart("Delete"))||xs.atStart("Update")) {
                l.add(((MarshallableObject) u.unmarshal()));
            }
            _OperationsTypes = PredicatedLists.createInvalidating(this, pred_OperationsTypes, l);
        }
        xs.takeEnd("Operations");
    }

    public static Operations unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static Operations unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static Operations unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((Operations) d.unmarshal(xs, (Operations.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Operations)) {
            return false;
        }
        Operations tob = ((Operations) ob);
        if (_OperationsTypes!= null) {
            if (tob._OperationsTypes == null) {
                return false;
            }
            if (!_OperationsTypes.equals(tob._OperationsTypes)) {
                return false;
            }
        } else {
            if (tob._OperationsTypes!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_OperationsTypes!= null)?_OperationsTypes.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Operations");
        if (_OperationsTypes!= null) {
            sb.append(" OperationsTypes=");
            sb.append(_OperationsTypes.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Delete.newDispatcher();
    }


    private static class OperationsTypesPredicate
        implements PredicatedLists.Predicate
    {


        public void check(Object ob) {
            if (!(ob instanceof MarshallableObject)) {
                throw new InvalidContentObjectException(ob, (MarshallableObject.class));
            }
        }

    }

}
