package org.geoserver.wfsv.xml.v1_1_0;

import java.io.Reader;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.geoserver.ows.XmlRequestReader;
import org.geoserver.wfs.WFS;
import org.geoserver.wfs.WFSException;
import org.geotools.util.Version;
import org.geotools.xml.Parser;
import org.xml.sax.InputSource;

public class WfsXmlReader extends XmlRequestReader {

    private WFS wfs;

    private WFSVConfiguration configuration;

    public WfsXmlReader(String element, WFS wfs,
            WFSVConfiguration configuration) {
        super(new QName(org.geoserver.wfs.xml.v1_1_0.WFS.NAMESPACE, element),
                new Version("1.1.0"), "wfsv");
        this.wfs = wfs;
        this.configuration = configuration;
    }

    public Object read(Reader reader) throws Exception {
        // TODO: make this configurable?
        configuration.getProperties().add(
                Parser.Properties.PARSE_UNKNOWN_ELEMENTS);

        Parser parser = new Parser(configuration);

        // set the input source with the correct encoding
        InputSource source = new InputSource(reader);
        source.setEncoding(wfs.getCharSet().name());

        Object parsed = parser.parse(source);

        // valid request? this should definitley be a configuration option
        // TODO: HACK, disabling validation for transaction
        if (!"Transaction".equalsIgnoreCase(getElement().getLocalPart())) {
            if (!parser.getValidationErrors().isEmpty()) {
                WFSException exception = new WFSException("Invalid request",
                        "InvalidParameterValue");

                for (Iterator e = parser.getValidationErrors().iterator(); e
                        .hasNext();) {
                    Exception error = (Exception) e.next();
                    exception.getExceptionText().add(
                            error.getLocalizedMessage());
                }

                throw exception;
            }
        }

        return parsed;
    }
}
