package org.vfny.geoserver.xml.internal.featureType;

import java.io.IOException;
import java.io.InputStream;
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


public class LatLonBoundingBox
    extends MarshallableObject
    implements Element
{

    private String _Minx;
    private String _Miny;
    private String _Maxx;
    private String _Maxy;

    public String getMinx() {
        return _Minx;
    }

    public void setMinx(String _Minx) {
        this._Minx = _Minx;
        if (_Minx == null) {
            invalidate();
        }
    }

    public String getMiny() {
        return _Miny;
    }

    public void setMiny(String _Miny) {
        this._Miny = _Miny;
        if (_Miny == null) {
            invalidate();
        }
    }

    public String getMaxx() {
        return _Maxx;
    }

    public void setMaxx(String _Maxx) {
        this._Maxx = _Maxx;
        if (_Maxx == null) {
            invalidate();
        }
    }

    public String getMaxy() {
        return _Maxy;
    }

    public void setMaxy(String _Maxy) {
        this._Maxy = _Maxy;
        if (_Maxy == null) {
            invalidate();
        }
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Minx == null) {
            throw new MissingAttributeException("minx");
        }
        if (_Miny == null) {
            throw new MissingAttributeException("miny");
        }
        if (_Maxx == null) {
            throw new MissingAttributeException("maxx");
        }
        if (_Maxy == null) {
            throw new MissingAttributeException("maxy");
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
        w.start("LatLonBoundingBox");
        w.attribute("minx", _Minx.toString());
        w.attribute("miny", _Miny.toString());
        w.attribute("maxx", _Maxx.toString());
        w.attribute("maxy", _Maxy.toString());
        w.end("LatLonBoundingBox");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("LatLonBoundingBox");
        while (xs.atAttribute()) {
            String an = xs.takeAttributeName();
            if (an.equals("minx")) {
                if (_Minx!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Minx = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("miny")) {
                if (_Miny!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Miny = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("maxx")) {
                if (_Maxx!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Maxx = xs.takeAttributeValue();
                continue;
            }
            if (an.equals("maxy")) {
                if (_Maxy!= null) {
                    throw new DuplicateAttributeException(an);
                }
                _Maxy = xs.takeAttributeValue();
                continue;
            }
            throw new InvalidAttributeException(an);
        }
        xs.takeEnd("LatLonBoundingBox");
    }

    public static LatLonBoundingBox unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static LatLonBoundingBox unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static LatLonBoundingBox unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((LatLonBoundingBox) d.unmarshal(xs, (LatLonBoundingBox.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof LatLonBoundingBox)) {
            return false;
        }
        LatLonBoundingBox tob = ((LatLonBoundingBox) ob);
        if (_Minx!= null) {
            if (tob._Minx == null) {
                return false;
            }
            if (!_Minx.equals(tob._Minx)) {
                return false;
            }
        } else {
            if (tob._Minx!= null) {
                return false;
            }
        }
        if (_Miny!= null) {
            if (tob._Miny == null) {
                return false;
            }
            if (!_Miny.equals(tob._Miny)) {
                return false;
            }
        } else {
            if (tob._Miny!= null) {
                return false;
            }
        }
        if (_Maxx!= null) {
            if (tob._Maxx == null) {
                return false;
            }
            if (!_Maxx.equals(tob._Maxx)) {
                return false;
            }
        } else {
            if (tob._Maxx!= null) {
                return false;
            }
        }
        if (_Maxy!= null) {
            if (tob._Maxy == null) {
                return false;
            }
            if (!_Maxy.equals(tob._Maxy)) {
                return false;
            }
        } else {
            if (tob._Maxy!= null) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int h = 0;
        h = ((127 *h)+((_Minx!= null)?_Minx.hashCode(): 0));
        h = ((127 *h)+((_Miny!= null)?_Miny.hashCode(): 0));
        h = ((127 *h)+((_Maxx!= null)?_Maxx.hashCode(): 0));
        h = ((127 *h)+((_Maxy!= null)?_Maxy.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<LatLonBoundingBox");
        if (_Minx!= null) {
            sb.append(" minx=");
            sb.append(_Minx.toString());
        }
        if (_Miny!= null) {
            sb.append(" miny=");
            sb.append(_Miny.toString());
        }
        if (_Maxx!= null) {
            sb.append(" maxx=");
            sb.append(_Maxx.toString());
        }
        if (_Maxy!= null) {
            sb.append(" maxy=");
            sb.append(_Maxy.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Delete.newDispatcher();
    }

}
