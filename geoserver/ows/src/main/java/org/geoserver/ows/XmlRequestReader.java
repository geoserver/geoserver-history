/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import org.geotools.util.Version;
import java.io.Reader;
import javax.xml.namespace.QName;


/**
 * Creates a request bean from xml.
 * <p>
 * A request bean is an object which captures the parameters of an operation
 * being requested to a service.
 * </p>
 * <p>
 * An xml request reader must declare the root element of xml documents that it
 * is capable of reading. This is accomplished with {@link #getNamespace()} and
 * {@link #getElement()}.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class XmlRequestReader {
    /**
     * the qualified name of the element this reader can read.
     */
    final QName element;

    /**
     * Appliction specific version number.
     */
    final Version version;

    /**
     * Creates the xml reader for the specified element.
     *
     * @param element The qualified name of the element the reader reads.
     */
    public XmlRequestReader(QName element) {
        this(element, null);
    }

    /**
     * Creates the xml reader for the specified element.
     *
     * @param namespace The namespace of the element
     * @param local The local name of the element
     */
    public XmlRequestReader(String namespace, String local) {
        this(new QName(namespace, local));
    }

    /**
     *
     * Creates the xml reader for the specified element of a particular version.
     *
     * @param element The qualified name of the element the reader reads.
     * @param version The version of the element in which the reader supports,
     * may be <code>null</code>.
     */
    public XmlRequestReader(QName element, Version version) {
        this.element = element;
        this.version = version;

        if (element == null) {
            throw new NullPointerException("element");
        }
    }

    /**
     *
     * Creates the xml reader for the specified element of a particular version.
     *
     * @param namespace The namespace of the element
     * @param local The local name of the element
     * @param version The version of the element in which the reader supports,
     * may be <code>null</code>.
     */
    public XmlRequestReader(String namespace, String local, Version version) {
        this(new QName(namespace, local), version);
    }

    /**
     * @return The qualified name of the element that this reader reads.
     */
    public QName getElement() {
        return element;
    }

    /**
     * @return The version of hte element that this reader reads.
     */
    public Version getVersion() {
        return version;
    }

    /**
     * Reads the xml and produces the request object.
     *
     * @param reader The xml input stream.
     *
     * @return The request object.
     */
    public abstract Object read(Reader reader) throws Exception;

    /**
     * Two XmlReaders considered equal if namespace,element, and version properties
     * are the same.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof XmlRequestReader)) {
            return false;
        }

        XmlRequestReader other = (XmlRequestReader) obj;

        if (!element.equals(other.element)) {
            return false;
        }

        if (version != null) {
            return version.equals(other.version);
        }

        return other.version == null;
    }

    /**
     * Implementation of hashcode.
     */
    public int hashCode() {
        int result = element.hashCode();

        if (version != null) {
            result = (result * 17) + version.hashCode();
        }

        return result;
    }
}
