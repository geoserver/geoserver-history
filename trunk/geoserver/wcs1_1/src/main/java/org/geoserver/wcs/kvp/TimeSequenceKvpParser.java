package org.geoserver.wcs.kvp;

import java.util.logging.Logger;

import net.opengis.wcs11.TimeSequenceType;

import org.geoserver.ows.KvpParser;
import org.geotools.util.logging.Logging;

public class TimeSequenceKvpParser extends KvpParser {
    Logger LOGGER = Logging.getLogger(TimeSequenceKvpParser.class);

    public TimeSequenceKvpParser() {
        super("TimeSequence", TimeSequenceType.class);
        
    }

    @Override
    public Object parse(String value) throws Exception {
        LOGGER.warning("TimeSequence is not supported in GeoServer WCS 1.1 implementation so far");
        return null;
    }

}
