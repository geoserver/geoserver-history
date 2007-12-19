/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
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
     * Service identifier
     */
    final String serviceId;

    /**
     * Creates the xml reader for the specified element.
     *
     * @param element The qualified name of the element the reader reads.
     */
    public XmlRequestReader(QName element) {
        this(element, null, null);
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
    public XmlRequestReader(QName element, Version version, String serviceId) {
        this.element = element;
        this.version = version;
        this.serviceId = serviceId;

        if (element == null) {
            throw new NullPointerException("element");
        }
    }

    //    /**
    //     *
    //     * Creates the xml reader for the specified element of a particular version.
    //     *
    //     * @param namespace The namespace of the element
    //     * @param local The local name of the element
    //     * @param version The version of the element in which the reader supports,
    //     * may be <code>null</code>.
    //     */
    //    public XmlRequestReader(String namespace, String local, Version version) {
    //        this(new QName(namespace, local), version, null);
    //    }

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
     * Reads the xml and initializes the request object.
     * <p>
     * The <tt>request</tt> parameter may be <code>null</code>, so in this case
     * the request reader would be responsible for creating the request object,
     * or throwing an exception if this is not supported.
     * </p>
     * <p>
     * In the case of the <tt>request</tt> being non <code>null</code>, the
     * request reader may chose to modify and return <tt>request</tt>, or create
     * a new request object and return it.
     * </p>
     */
    public abstract Object read(Object request, Reader reader)
        throws Exception;

    /**
     * Two XmlReaders considered equal if namespace,element, and version properties
     * are the same.
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof XmlRequestReader)) {
            return false;
        }

        XmlRequestReader other = (XmlRequestReader) obj;

        return new EqualsBuilder().append(element, other.element).append(version, other.version)
                                  .append(serviceId, other.serviceId).isEquals();
    }

    /**
     * Implementation of hashcode.
     */
    public int hashCode() {
        return new HashCodeBuilder().append(element).append(version).append(serviceId).toHashCode();
    }

    public String getServiceId() {
        return serviceId;
    }
}
