package org.vfny.geoserver.xml.internal.configuration;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.FixedValueException;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidFixedValueException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableRootElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.RootElement;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class FreeFSConfiguration
    extends MarshallableRootElement
    implements RootElement
{

    private String _Version;
    private final static String FIXED_VERSION = String.valueOf("0.0.13");
    private String _UpdateSequence;
    private boolean isDefaulted_UpdateSequence = true;
    private final static String DEFAULT_UPDATESEQUENCE = String.valueOf("0");
    private Service _Service;
    private Database _Database;

    public String getVersion() {
        return _Version;
    }

    public final void setVersion(String _Version) {
        if ((_Version!= null)&&(!_Version.equals(FIXED_VERSION))) {
            throw new FixedValueException(_Version.toString(), FIXED_VERSION.toString());
        }
        this._Version = _Version;
        if (_Version == null) {
            invalidate();
        }
    }

    public boolean defaultedUpdateSequence() {
        return (_UpdateSequence!= null);
    }

    public String getUpdateSequence() {
        if (_UpdateSequence == null) {
            return DEFAULT_UPDATESEQUENCE;
        }
        return _UpdateSequence;
    }

    public void setUpdateSequence(String _UpdateSequence) {
        this._UpdateSequence = _UpdateSequence;
        if (_UpdateSequence == null) {
            invalidate();
        }
    }

    public Service getService() {
        return _Service;
    }

    public void setService(Service _Service) {
        this._Service = _Service;
        if (_Service == null) {
            invalidate();
        }
    }

    public Database getDatabase() {
        return _Database;
    }

    public void setDatabase(Database _Database) {
        this._Database = _Database;
        if (_Database == null) {
            invalidate();
        }
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Service == null) {
            throw new MissingContentException("Service");
        }
        if (_Database == null) {
            throw new MissingContentException("Database");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
        v.validate(_Service);
        v.validate(_Database);
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("FreeFS_Configuration");
        if (_Version!= null) {
            w.attribute("version", _Version.toString());
        }
        if (_UpdateSequence!= null) {
            w.attribute("updateSequence", _UpdateSequence.toString());
        }
        m.marshal(_Service);
        m.marshal(_Database);
        w.end("FreeFS_Configuration");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("FreeFS_Configuration");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("version")) {
                if (_Version!= null) {
                    throw new DuplicateAttributeException(an);
                }
                String s = xs.takeAttributeValue();
                if (!s.equals(FIXED_VERSION)) {
                    throw new InvalidFixedValueException(s, FIXED_VERSION);
                }
                _Version = s;
                continue;
            }
            if (an.equals("updateSequence")) {
                if (_UpdateSequence!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _UpdateSequence = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        _Service = ((Service) u.unmarshal());
        _Database = ((Database) u.unmarshal());
        xs.takeEnd("FreeFS_Configuration");
    }

    public static FreeFSConfiguration unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static FreeFSConfiguration unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static FreeFSConfiguration unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((FreeFSConfiguration) d.unmarshal(xs, (FreeFSConfiguration.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof FreeFSConfiguration)) {
            return false;
        }
        FreeFSConfiguration tob = ((FreeFSConfiguration) ob);
        if (_Version!= null) {
            if (tob._Version == null) {
                return false;
            }
            if (!_Version.equals(tob._Version)) {
                return false;
            }
        } else {
            if (tob._Version!= null) {
                return false;
            }
        }
        if (_UpdateSequence!= null) {
            if (tob._UpdateSequence == null) {
                return false;
            }
            if (!_UpdateSequence.equals(tob._UpdateSequence)) {
                return false;
            }
        } else {
            if (tob._UpdateSequence!= null) {
                return false;
            }
        }
        if (_Service!= null) {
            if (tob._Service == null) {
                return false;
            }
            if (!_Service.equals(tob._Service)) {
                return false;
            }
        } else {
            if (tob._Service!= null) {
                return false;
            }
        }
        if (_Database!= null) {
            if (tob._Database == null) {
                return false;
            }
            if (!_Database.equals(tob._Database)) {
                return false;
            }
        } else {
            if (tob._Database!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_Version!= null)?_Version.hashCode(): 0));
        h = ((127 *h)+((_UpdateSequence!= null)?_UpdateSequence.hashCode(): 0));
        h = ((127 *h)+((_Service!= null)?_Service.hashCode(): 0));
        h = ((127 *h)+((_Database!= null)?_Database.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<FreeFS_Configuration");
        if (_Version!= null) {
            sb.append(" version=");
            sb.append(_Version.toString());
        }
        sb.append(" updateSequence=");
        sb.append(getUpdateSequence().toString());
        if (_Service!= null) {
            sb.append(" Service=");
            sb.append(_Service.toString());
        }
        if (_Database!= null) {
            sb.append(" Database=");
            sb.append(_Database.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Database.newDispatcher();
    }

}
