package org.vfny.geoserver.xml.internal.featureType;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateAttributeException;
import javax.xml.bind.Element;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingAttributeException;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class MetadataURL
    extends MarshallableObject
    implements Element
{

    private metadataTypeList _Type;
    private metadataFormatList _Format;
    private String _Content;

    public metadataTypeList getType() {
        return _Type;
    }

    public void setType(metadataTypeList _Type) {
        this._Type = _Type;
        if (_Type == null) {
            invalidate();
        }
    }

    public metadataFormatList getFormat() {
        return _Format;
    }

    public void setFormat(metadataFormatList _Format) {
        this._Format = _Format;
        if (_Format == null) {
            invalidate();
        }
    }

    public String getContent() {
        return _Content;
    }

    public void setContent(String _Content) {
        this._Content = _Content;
        if (_Content == null) {
            invalidate();
        }
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Type == null) {
            throw new MissingAttributeException("type");
        }
        if (_Format == null) {
            throw new MissingAttributeException("format");
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
        w.start("MetadataURL");
        w.attribute("type", _Type.toString());
        w.attribute("format", _Format.toString());
        if (_Content!= null) {
            w.chars(_Content.toString());
        }
        w.end("MetadataURL");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("MetadataURL");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("type")) {
                if (_Type!= null) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _Type = metadataTypeList.parse(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                continue;
            }
            if (an.equals("format")) {
                if (_Format!= null) {
                    throw new DuplicateAttributeException(an);
                }
                try {
                    _Format = metadataFormatList.parse(xs.takeAttributeValue());
                } catch (Exception x) {
                    throw new ConversionException(an, x);
                }
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        {
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _Content = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("content", x);
            }
        }
        xs.takeEnd("MetadataURL");
    }

    public static MetadataURL unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static MetadataURL unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static MetadataURL unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((MetadataURL) d.unmarshal(xs, (MetadataURL.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof MetadataURL)) {
            return false;
        }
        MetadataURL tob = ((MetadataURL) ob);
        if (_Type!= null) {
            if (tob._Type == null) {
                return false;
            }
            if (!_Type.equals(tob._Type)) {
                return false;
            }
        } else {
            if (tob._Type!= null) {
                return false;
            }
        }
        if (_Format!= null) {
            if (tob._Format == null) {
                return false;
            }
            if (!_Format.equals(tob._Format)) {
                return false;
            }
        } else {
            if (tob._Format!= null) {
                return false;
            }
        }
        if (_Content!= null) {
            if (tob._Content == null) {
                return false;
            }
            if (!_Content.equals(tob._Content)) {
                return false;
            }
        } else {
            if (tob._Content!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_Type!= null)?_Type.hashCode(): 0));
        h = ((127 *h)+((_Format!= null)?_Format.hashCode(): 0));
        h = ((127 *h)+((_Content!= null)?_Content.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<MetadataURL");
        if (_Type!= null) {
            sb.append(" type=");
            sb.append(_Type.toString());
        }
        if (_Format!= null) {
            sb.append(" format=");
            sb.append(_Format.toString());
        }
        if (_Content!= null) {
            sb.append(" content=");
            sb.append(_Content.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Delete.newDispatcher();
    }

}
