package org.vfny.geoserver.config.configuration;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class Service
    extends MarshallableObject
    implements Element
{

    private String _Name;
    private String _Title;
    private String _Abstract;
    private String _Keywords;
    private String _OnlineResource;
    private String _Fees;
    private String _AccessConstraints;
    private String _URL;
    private String _Maintainer;

    public String getName() {
        return _Name;
    }

    public void setName(String _Name) {
        this._Name = _Name;
        if (_Name == null) {
            invalidate();
        }
    }

    public String getTitle() {
        return _Title;
    }

    public void setTitle(String _Title) {
        this._Title = _Title;
        if (_Title == null) {
            invalidate();
        }
    }

    public String getAbstract() {
        return _Abstract;
    }

    public void setAbstract(String _Abstract) {
        this._Abstract = _Abstract;
        if (_Abstract == null) {
            invalidate();
        }
    }

    public String getKeywords() {
        return _Keywords;
    }

    public void setKeywords(String _Keywords) {
        this._Keywords = _Keywords;
        if (_Keywords == null) {
            invalidate();
        }
    }

    public String getOnlineResource() {
        return _OnlineResource;
    }

    public void setOnlineResource(String _OnlineResource) {
        this._OnlineResource = _OnlineResource;
        if (_OnlineResource == null) {
            invalidate();
        }
    }

    public String getFees() {
        return _Fees;
    }

    public void setFees(String _Fees) {
        this._Fees = _Fees;
        if (_Fees == null) {
            invalidate();
        }
    }

    public String getAccessConstraints() {
        return _AccessConstraints;
    }

    public void setAccessConstraints(String _AccessConstraints) {
        this._AccessConstraints = _AccessConstraints;
        if (_AccessConstraints == null) {
            invalidate();
        }
    }

    public String getURL() {
        return _URL;
    }

    public void setURL(String _URL) {
        this._URL = _URL;
        if (_URL == null) {
            invalidate();
        }
    }

    public String getMaintainer() {
        return _Maintainer;
    }

    public void setMaintainer(String _Maintainer) {
        this._Maintainer = _Maintainer;
        if (_Maintainer == null) {
            invalidate();
        }
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Name == null) {
            throw new MissingContentException("Name");
        }
        if (_Title == null) {
            throw new MissingContentException("Title");
        }
        if (_OnlineResource == null) {
            throw new MissingContentException("OnlineResource");
        }
        if (_URL == null) {
            throw new MissingContentException("URL");
        }
        if (_Maintainer == null) {
            throw new MissingContentException("Maintainer");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("Service");
        w.leaf("Name", _Name.toString());
        w.leaf("Title", _Title.toString());
        if (_Abstract!= null) {
            w.leaf("Abstract", _Abstract.toString());
        }
        if (_Keywords!= null) {
            w.leaf("Keywords", _Keywords.toString());
        }
        w.leaf("OnlineResource", _OnlineResource.toString());
        if (_Fees!= null) {
            w.leaf("Fees", _Fees.toString());
        }
        if (_AccessConstraints!= null) {
            w.leaf("AccessConstraints", _AccessConstraints.toString());
        }
        w.leaf("URL", _URL.toString());
        w.leaf("Maintainer", _Maintainer.toString());
        w.end("Service");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Service");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            throw new InvalidAttributeException(an);
        }
        if (xs.atStart("Name")) {
            xs.takeStart("Name");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Name = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Name", x);
            }
            xs.takeEnd("Name");
        }
        if (xs.atStart("Title")) {
            xs.takeStart("Title");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Title = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Title", x);
            }
            xs.takeEnd("Title");
        }
        if (xs.atStart("Abstract")) {
            xs.takeStart("Abstract");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Abstract = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Abstract", x);
            }
            xs.takeEnd("Abstract");
        }
        if (xs.atStart("Keywords")) {
            xs.takeStart("Keywords");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Keywords = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Keywords", x);
            }
            xs.takeEnd("Keywords");
        }
        if (xs.atStart("OnlineResource")) {
            xs.takeStart("OnlineResource");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _OnlineResource = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("OnlineResource", x);
            }
            xs.takeEnd("OnlineResource");
        }
        if (xs.atStart("Fees")) {
            xs.takeStart("Fees");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Fees = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Fees", x);
            }
            xs.takeEnd("Fees");
        }
        if (xs.atStart("AccessConstraints")) {
            xs.takeStart("AccessConstraints");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _AccessConstraints = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("AccessConstraints", x);
            }
            xs.takeEnd("AccessConstraints");
        }
        if (xs.atStart("URL")) {
            xs.takeStart("URL");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _URL = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("URL", x);
            }
            xs.takeEnd("URL");
        }
        if (xs.atStart("Maintainer")) {
            xs.takeStart("Maintainer");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Maintainer = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Maintainer", x);
            }
            xs.takeEnd("Maintainer");
        }
        xs.takeEnd("Service");
    }

    public static Service unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static Service unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static Service unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((Service) d.unmarshal(xs, (Service.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Service)) {
            return false;
        }
        Service tob = ((Service) ob);
        if (_Name!= null) {
            if (tob._Name == null) {
                return false;
            }
            if (!_Name.equals(tob._Name)) {
                return false;
            }
        } else {
            if (tob._Name!= null) {
                return false;
            }
        }
        if (_Title!= null) {
            if (tob._Title == null) {
                return false;
            }
            if (!_Title.equals(tob._Title)) {
                return false;
            }
        } else {
            if (tob._Title!= null) {
                return false;
            }
        }
        if (_Abstract!= null) {
            if (tob._Abstract == null) {
                return false;
            }
            if (!_Abstract.equals(tob._Abstract)) {
                return false;
            }
        } else {
            if (tob._Abstract!= null) {
                return false;
            }
        }
        if (_Keywords!= null) {
            if (tob._Keywords == null) {
                return false;
            }
            if (!_Keywords.equals(tob._Keywords)) {
                return false;
            }
        } else {
            if (tob._Keywords!= null) {
                return false;
            }
        }
        if (_OnlineResource!= null) {
            if (tob._OnlineResource == null) {
                return false;
            }
            if (!_OnlineResource.equals(tob._OnlineResource)) {
                return false;
            }
        } else {
            if (tob._OnlineResource!= null) {
                return false;
            }
        }
        if (_Fees!= null) {
            if (tob._Fees == null) {
                return false;
            }
            if (!_Fees.equals(tob._Fees)) {
                return false;
            }
        } else {
            if (tob._Fees!= null) {
                return false;
            }
        }
        if (_AccessConstraints!= null) {
            if (tob._AccessConstraints == null) {
                return false;
            }
            if (!_AccessConstraints.equals(tob._AccessConstraints)) {
                return false;
            }
        } else {
            if (tob._AccessConstraints!= null) {
                return false;
            }
        }
        if (_URL!= null) {
            if (tob._URL == null) {
                return false;
            }
            if (!_URL.equals(tob._URL)) {
                return false;
            }
        } else {
            if (tob._URL!= null) {
                return false;
            }
        }
        if (_Maintainer!= null) {
            if (tob._Maintainer == null) {
                return false;
            }
            if (!_Maintainer.equals(tob._Maintainer)) {
                return false;
            }
        } else {
            if (tob._Maintainer!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_Name!= null)?_Name.hashCode(): 0));
        h = ((127 *h)+((_Title!= null)?_Title.hashCode(): 0));
        h = ((127 *h)+((_Abstract!= null)?_Abstract.hashCode(): 0));
        h = ((127 *h)+((_Keywords!= null)?_Keywords.hashCode(): 0));
        h = ((127 *h)+((_OnlineResource!= null)?_OnlineResource.hashCode(): 0));
        h = ((127 *h)+((_Fees!= null)?_Fees.hashCode(): 0));
        h = ((127 *h)+((_AccessConstraints!= null)?_AccessConstraints.hashCode(): 0));
        h = ((127 *h)+((_URL!= null)?_URL.hashCode(): 0));
        h = ((127 *h)+((_Maintainer!= null)?_Maintainer.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Service");
        if (_Name!= null) {
            sb.append(" Name=");
            sb.append(_Name.toString());
        }
        if (_Title!= null) {
            sb.append(" Title=");
            sb.append(_Title.toString());
        }
        if (_Abstract!= null) {
            sb.append(" Abstract=");
            sb.append(_Abstract.toString());
        }
        if (_Keywords!= null) {
            sb.append(" Keywords=");
            sb.append(_Keywords.toString());
        }
        if (_OnlineResource!= null) {
            sb.append(" OnlineResource=");
            sb.append(_OnlineResource.toString());
        }
        if (_Fees!= null) {
            sb.append(" Fees=");
            sb.append(_Fees.toString());
        }
        if (_AccessConstraints!= null) {
            sb.append(" AccessConstraints=");
            sb.append(_AccessConstraints.toString());
        }
        if (_URL!= null) {
            sb.append(" URL=");
            sb.append(_URL.toString());
        }
        if (_Maintainer!= null) {
            sb.append(" Maintainer=");
            sb.append(_Maintainer.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Database.newDispatcher();
    }

}
