package org.geoserver.catalog.hibernate.types;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.usertype.UserType;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Hibernate user type for {@link BoundingBox}.
 * 
 * @author Justin Deoliveira, The Open Planing Project
 * 
 */
public class BoundingBoxType implements UserType {

    private static final Logger LOGGER = Logging.getLogger(BoundingBoxType.class);

    public Object assemble(Serializable cached, Object owner) throws HibernateException {

        // String os = owner == null ? "null" : owner.getClass().getSimpleName();
        // String cs = cached == null ? "null" : cached.getClass().getSimpleName();
        // LOGGER.severe("ASSEMBLE " + cs + "(" + cached + ")" + os + "(" + owner + ")");

        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        // if (value == null) {
        // LOGGER.severe("DISASSEMBLE null");
        // } else {
        // LOGGER.severe("DISASSEMBLE " + value.getClass().getSimpleName() + " " + value);
        // }
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return Utilities.equals(x, y);
    }

    public int hashCode(Object x) throws HibernateException {
        return Utilities.deepHashCode(x);
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
            throws HibernateException, SQLException {

        double minx = rs.getDouble(names[0]);
        double miny = rs.getDouble(names[1]);
        double maxx = rs.getDouble(names[2]);
        double maxy = rs.getDouble(names[3]);

        Blob blob = rs.getBlob(names[4]);
        if (blob != null) {
            String wkt = new String(blob.getBytes(1, (int) blob.length()));
            CoordinateReferenceSystem crs;
            try {
                crs = CRS.parseWKT(wkt);
            } catch (Exception e) {
                String msg = "Unable to create crs from wkt: " + wkt;
                throw new HibernateException(msg, e);
            }
            return new ReferencedEnvelope(minx, maxx, miny, maxy, crs);
        } else {
            return new ReferencedEnvelope(minx, maxx, miny, maxy, null);
        }
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index)
            throws HibernateException, SQLException {

        BoundingBox box = (BoundingBox) value;
        if (box == null) {
            st.setDouble(index, Double.NaN);
            st.setDouble(index + 1, Double.NaN);
            st.setDouble(index + 2, Double.NaN);
            st.setDouble(index + 3, Double.NaN);
            st.setBlob(index + 4, (Blob) null);
            return;
        }

        st.setDouble(index, box.getMinX());
        st.setDouble(index + 1, box.getMinY());
        st.setDouble(index + 2, box.getMaxX());
        st.setDouble(index + 3, box.getMaxY());

        if (box.getCoordinateReferenceSystem() != null) {
            CoordinateReferenceSystem crs = box.getCoordinateReferenceSystem();
            st.setBlob(index + 4, Hibernate.createBlob(crs.toWKT().getBytes()));
        } else {
            st.setBlob(index + 4, (Blob) null);
        }
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    public Class<?> returnedClass() {
        return BoundingBox.class;
    }

    private static final int[] SQLTYPES = new int[] { Types.DOUBLE, Types.DOUBLE, Types.DOUBLE, Types.DOUBLE, Types.BLOB };
    public int[] sqlTypes() {
        return SQLTYPES;
    }
}
