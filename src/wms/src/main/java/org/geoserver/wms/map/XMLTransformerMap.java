package org.geoserver.wms.map;

import org.geoserver.wms.Map;
import org.geotools.xml.transform.TransformerBase;

public class XMLTransformerMap extends Map {

    private TransformerBase transformer;

    private Object transformerSubject;

    /**
     * 
     * @param transformer
     *            the transformer that writes to the response stream
     * @param subject
     *            the object to be passed down to the transformer, might be {@code null} at the
     *            user's choice
     * @param mimeType
     *            the MIME-Type to be declared in the response
     */
    public XMLTransformerMap(final TransformerBase transformer, final Object subject,
            final String mimeType) {
        this.transformer = transformer;
        this.transformerSubject = subject;
        setMimeType(mimeType);
    }

    /**
     * @return the xml transformer that writes to the destination output stream
     */
    public TransformerBase getTransformer() {
        return transformer;
    }

    /**
     * @return the object to be passed down to the transformer
     */
    public Object getTransformerSubject() {
        return transformerSubject;
    }
}
