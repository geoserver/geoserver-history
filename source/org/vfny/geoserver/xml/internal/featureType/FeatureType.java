package org.vfny.geoserver.xml.internal.featureType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.ConversionException;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.InvalidAttributeException;
import javax.xml.bind.InvalidContentObjectException;
import javax.xml.bind.LocalValidationException;
import javax.xml.bind.MarshallableObject;
import javax.xml.bind.MarshallableRootElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.MissingContentException;
import javax.xml.bind.PredicatedLists;
import javax.xml.bind.PredicatedLists.Predicate;
import javax.xml.bind.RootElement;
import javax.xml.bind.StructureValidationException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidatableObject;
import javax.xml.bind.Validator;
import javax.xml.marshal.XMLScanner;
import javax.xml.marshal.XMLWriter;


public class FeatureType
    extends MarshallableRootElement
    implements RootElement
{

    private String _Name;
    private String _Title;
    private String _Abstract;
    private String _Keywords;
    private String _SRS;
    private Operations _Operations;
    private LatLonBoundingBox _LatLonBoundingBox;
    private List _MetadataURL = PredicatedLists.createInvalidating(this, new MetadataURLPredicate(), new ArrayList());
    private PredicatedLists.Predicate pred_MetadataURL = new MetadataURLPredicate();

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

    public String getSRS() {
        return _SRS;
    }

    public void setSRS(String _SRS) {
        this._SRS = _SRS;
        if (_SRS == null) {
            invalidate();
        }
    }

    public Operations getOperations() {
        return _Operations;
    }

    public void setOperations(Operations _Operations) {
        this._Operations = _Operations;
        if (_Operations == null) {
            invalidate();
        }
    }

    public LatLonBoundingBox getLatLonBoundingBox() {
        return _LatLonBoundingBox;
    }

    public void setLatLonBoundingBox(LatLonBoundingBox _LatLonBoundingBox) {
        this._LatLonBoundingBox = _LatLonBoundingBox;
        if (_LatLonBoundingBox == null) {
            invalidate();
        }
    }

    public List getMetadataURL() {
        return _MetadataURL;
    }

    public void deleteMetadataURL() {
        _MetadataURL = null;
        invalidate();
    }

    public void emptyMetadataURL() {
        _MetadataURL = PredicatedLists.createInvalidating(this, pred_MetadataURL, new ArrayList());
    }

    public void validateThis()
        throws LocalValidationException
    {
        if (_Name == null) {
            throw new MissingContentException("Name");
        }
        if (_SRS == null) {
            throw new MissingContentException("SRS");
        }
        if (_LatLonBoundingBox == null) {
            throw new MissingContentException("LatLonBoundingBox");
        }
    }

    public void validate(Validator v)
        throws StructureValidationException
    {
        v.validate(_Operations);
        v.validate(_LatLonBoundingBox);
        for (Iterator i = _MetadataURL.iterator(); i.hasNext(); ) {
            v.validate(((ValidatableObject) i.next()));
        }
    }

    public void marshal(Marshaller m)
        throws IOException
    {
        XMLWriter w = m.writer();
        w.start("FeatureType");
        w.leaf("Name", _Name.toString());
        if (_Title!= null) {
            w.leaf("Title", _Title.toString());
        }
        if (_Abstract!= null) {
            w.leaf("Abstract", _Abstract.toString());
        }
        if (_Keywords!= null) {
            w.leaf("Keywords", _Keywords.toString());
        }
        w.leaf("SRS", _SRS.toString());
        if (_Operations!= null) {
            m.marshal(_Operations);
        }
        m.marshal(_LatLonBoundingBox);
        if (_MetadataURL.size()> 0) {
            for (Iterator i = _MetadataURL.iterator(); i.hasNext(); ) {
                m.marshal(((MarshallableObject) i.next()));
            }
        }
        w.end("FeatureType");
    }

    public void unmarshal(Unmarshaller u)
        throws UnmarshalException
    {
        XMLScanner xs = u.scanner();
        Validator v = u.validator();
        xs.takeStart("FeatureType");
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
        if (xs.atStart("SRS")) {
            xs.takeStart("SRS");
            String s;
            if (xs.atChars(XMLScanner.WS_COLLAPSE)) {
                s = xs.takeChars(XMLScanner.WS_COLLAPSE);
            } else {
                s = "";
            }
            try {
                _SRS = String.valueOf(s);
            } catch (Exception x) {
                throw new ConversionException("SRS", x);
            }
            xs.takeEnd("SRS");
        }
        if (xs.atStart("Operations")) {
            _Operations = ((Operations) u.unmarshal());
        }
        _LatLonBoundingBox = ((LatLonBoundingBox) u.unmarshal());
        {
            List l = PredicatedLists.create(this, pred_MetadataURL, new ArrayList());
            while (xs.atStart("MetadataURL")) {
                l.add(((MetadataURL) u.unmarshal()));
            }
            _MetadataURL = PredicatedLists.createInvalidating(this, pred_MetadataURL, l);
        }
        xs.takeEnd("FeatureType");
    }

    public static FeatureType unmarshal(InputStream in)
        throws UnmarshalException
    {
        return unmarshal(XMLScanner.open(in));
    }

    public static FeatureType unmarshal(XMLScanner xs)
        throws UnmarshalException
    {
        return unmarshal(xs, newDispatcher());
    }

    public static FeatureType unmarshal(XMLScanner xs, Dispatcher d)
        throws UnmarshalException
    {
        return ((FeatureType) d.unmarshal(xs, (FeatureType.class)));
    }

    public boolean equals(Object ob) {
        if (this == ob) {
            return true;
        }
        if (!(ob instanceof FeatureType)) {
            return false;
        }
        FeatureType tob = ((FeatureType) ob);
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
        if (_SRS!= null) {
            if (tob._SRS == null) {
                return false;
            }
            if (!_SRS.equals(tob._SRS)) {
                return false;
            }
        } else {
            if (tob._SRS!= null) {
                return false;
            }
        }
        if (_Operations!= null) {
            if (tob._Operations == null) {
                return false;
            }
            if (!_Operations.equals(tob._Operations)) {
                return false;
            }
        } else {
            if (tob._Operations!= null) {
                return false;
            }
        }
        if (_LatLonBoundingBox!= null) {
            if (tob._LatLonBoundingBox == null) {
                return false;
            }
            if (!_LatLonBoundingBox.equals(tob._LatLonBoundingBox)) {
                return false;
            }
        } else {
            if (tob._LatLonBoundingBox!= null) {
                return false;
            }
        }
        if (_MetadataURL!= null) {
            if (tob._MetadataURL == null) {
                return false;
            }
            if (!_MetadataURL.equals(tob._MetadataURL)) {
                return false;
            }
        } else {
            if (tob._MetadataURL!= null) {
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
        h = ((127 *h)+((_SRS!= null)?_SRS.hashCode(): 0));
        h = ((127 *h)+((_Operations!= null)?_Operations.hashCode(): 0));
        h = ((127 *h)+((_LatLonBoundingBox!= null)?_LatLonBoundingBox.hashCode(): 0));
        h = ((127 *h)+((_MetadataURL!= null)?_MetadataURL.hashCode(): 0));
        return h;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("<<FeatureType");
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
        if (_SRS!= null) {
            sb.append(" SRS=");
            sb.append(_SRS.toString());
        }
        if (_Operations!= null) {
            sb.append(" Operations=");
            sb.append(_Operations.toString());
        }
        if (_LatLonBoundingBox!= null) {
            sb.append(" LatLonBoundingBox=");
            sb.append(_LatLonBoundingBox.toString());
        }
        if (_MetadataURL!= null) {
            sb.append(" MetadataURL=");
            sb.append(_MetadataURL.toString());
        }
        sb.append(">>");
        return sb.toString();
    }

    public static Dispatcher newDispatcher() {
        return Delete.newDispatcher();
    }


    private static class MetadataURLPredicate
        implements PredicatedLists.Predicate
    {


        public void check(Object ob) {
            if (!(ob instanceof MetadataURL)) {
                throw new InvalidContentObjectException(ob, (MetadataURL.class));
            }
        }

    }

}
