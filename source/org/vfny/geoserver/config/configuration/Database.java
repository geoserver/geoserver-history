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


public class Database
    extends MarshallableObject
    implements Element
{

    private String _Name;
    private String _User;
    private String _Password;
    private String _Port;

    public String getName() {
        return _Name;
    }

    public void setName(String _Name) {
        this._Name = _Name;
        if (_Name == null) {
            invalidate();
        }
    }

    public String getUser() {
        return _User;
    }

    public void setUser(String _User) {
        this._User = _User;
        if (_User == null) {
            invalidate();
        }
    }

    public String getPassword() {
        return _Password;
    }

    public void setPassword(String _Password) {
        this._Password = _Password;
        if (_Password == null) {
            invalidate();
        }
    }

    public String getPort() {
        return _Port;
    }

    public void setPort(String _Port) {
        this._Port = _Port;
        if (_Port == null) {
            invalidate();
        }
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Name == null) {
            throw new MissingContentException("Name");
        }
        if (_User == null) {
            throw new MissingContentException("User");
        }
        if (_Password == null) {
            throw new MissingContentException("Password");
        }
        if (_Port == null) {
            throw new MissingContentException("Port");
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
        w.start("Database");
        w.leaf("Name", _Name.toString());
        w.leaf("User", _User.toString());
        w.leaf("Password", _Password.toString());
        w.leaf("Port", _Port.toString());
        w.end("Database");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("Database");
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
        if (xs.atStart("User")) {
            xs.takeStart("User");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _User = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("User", x);
            }
            xs.takeEnd("User");
        }
        if (xs.atStart("Password")) {
            xs.takeStart("Password");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Password = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Password", x);
            }
            xs.takeEnd("Password");
        }
        if (xs.atStart("Port")) {
            xs.takeStart("Port");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Port = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("Port", x);
            }
            xs.takeEnd("Port");
        }
        xs.takeEnd("Database");
    }

    public static Database unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static Database unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static Database unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((Database) d.unmarshal(xs, (Database.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof Database)) {
            return false;
        }
        Database tob = ((Database) ob);
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
        if (_User!= null) {
            if (tob._User == null) {
                return false;
            }
            if (!_User.equals(tob._User)) {
                return false;
            }
        } else {
            if (tob._User!= null) {
                return false;
            }
        }
        if (_Password!= null) {
            if (tob._Password == null) {
                return false;
            }
            if (!_Password.equals(tob._Password)) {
                return false;
            }
        } else {
            if (tob._Password!= null) {
                return false;
            }
        }
        if (_Port!= null) {
            if (tob._Port == null) {
                return false;
            }
            if (!_Port.equals(tob._Port)) {
                return false;
            }
        } else {
            if (tob._Port!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_Name!= null)?_Name.hashCode(): 0));
        h = ((127 *h)+((_User!= null)?_User.hashCode(): 0));
        h = ((127 *h)+((_Password!= null)?_Password.hashCode(): 0));
        h = ((127 *h)+((_Port!= null)?_Port.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<Database");
        if (_Name!= null) {
            sb.append(" Name=");
            sb.append(_Name.toString());
        }
        if (_User!= null) {
            sb.append(" User=");
            sb.append(_User.toString());
        }
        if (_Password!= null) {
            sb.append(" Password=");
            sb.append(_Password.toString());
        }
        if (_Port!= null) {
            sb.append(" Port=");
            sb.append(_Port.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        Dispatcher d = new Dispatcher();
        d.register("Database", (Database.class));
        d.register("FreeFS_Configuration", (FreeFSConfiguration.class));
        d.register("GML2", (GML2 .class));
        d.register("SXSDL", (SXSDL.class));
        d.register("Service", (Service.class));
        d.register("XMLSCHEMA", (XMLSCHEMA.class));
        d.freezeElementNameMap();
        return d;
    }

}
