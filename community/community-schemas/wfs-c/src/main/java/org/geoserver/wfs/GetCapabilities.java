/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import net.opengis.wfs.GetCapabilitiesType;
import org.geotools.util.Version;
import org.vfny.geoserver.global.Data;
import java.util.Iterator;
import java.util.TreeSet;


/**
 * Web Feature Service GetCapabilities operation.
 * <p>
 * This operation returns a {@link org.geotools.xml.transform.TransformerBase} instance
 * which will serialize the wfs capabilities document. This class uses ows version negotiation
 * to determine which version of the wfs capabilities document to return.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetCapabilities {
    /**
     * WFS service configuration
     */
    WFS wfs;

    /**
     * The catalog
     */
    Data catalog;

    /**
     * Creates a new wfs GetCapabilitis operation.
     *
     * @param wfs The wfs configuration
     * @param catalog The geoserver catalog.
     */
    public GetCapabilities(WFS wfs, Data catalog) {
        this.wfs = wfs;
        this.catalog = catalog;
    }

    public CapabilitiesTransformer run(GetCapabilitiesType request)
        throws WFSException {
        //cite requires that we fail when we see an "invalid" update sequence,
        // since we dont support update sequences, all are invalid, but we take
        // our more lax approach and just ignore it when not doint the cite thing
        if (wfs.getCiteConformanceHacks()) {
            if (request.getUpdateSequence() != null) {
                throw new WFSException("Invalid update sequence", "InvalidUpdateSequence");
            }
        }

        //TODO: the rest of this routine should be done by the dispatcher
        //make sure service is set, cite conformance thing
        //JD - We wrap this in a cite conformance check because cite stricly 
        // tests that every request includes the 'service=WFS' key value pair.
        // However often the the context of the request is good enough to 
        // determine what the service is, like in 'geoserver/wfs?request=GetCapabilities'
        if (wfs.getCiteConformanceHacks()) {
            if (!request.isSetService()) {
                //give up 
                throw new WFSException("Service not set", "MissingParameterValue", "service");
            }
        }

        //do the version negotiation dance

        //any accepted versions
        if ((request.getAcceptVersions() == null)
                || request.getAcceptVersions().getVersion().isEmpty()) {
            //no, respond with highest
            return new CapabilitiesTransformer.WFS1_1(wfs, catalog);
        }

        //first check the format of each of the version numbers
        for (Iterator i = request.getAcceptVersions().getVersion().iterator(); i.hasNext();) {
            String version = (String) i.next();

            if (!version.matches("[0-99]\\.[0-99]\\.[0-99]")) {
                String msg = version + " is an invalid version numver";
                throw new WFSException(msg, "VersionNegotiationFailed");
            }
        }

        //first figure out which versions are provided
        //TODO: use an extension point?
        TreeSet provided = new TreeSet();
        provided.add(new Version("1.0.0"));
        provided.add(new Version("1.1.0"));

        //next figure out what the client accepts
        TreeSet accepted = new TreeSet();

        for (Iterator v = request.getAcceptVersions().getVersion().iterator(); v.hasNext();) {
            accepted.add(new Version((String) v.next()));
        }

        //prune out those not provided
        for (Iterator v = accepted.iterator(); v.hasNext();) {
            Version version = (Version) v.next();

            if (!provided.contains(version)) {
                v.remove();
            }
        }

        String version = null;

        if (!accepted.isEmpty()) {
            //return the highest version provided
            version = ((Version) accepted.last()).toString();
        } else {
            accepted = new TreeSet();

            for (Iterator v = request.getAcceptVersions().getVersion().iterator(); v.hasNext();) {
                accepted.add(new Version((String) v.next()));
            }

            //if highest accepted less then lowest provided, send lowest
            if (((Version) accepted.last()).compareTo(provided.first()) < 0) {
                version = ((Version) provided.first()).toString();
            }

            //if lowest accepted is greater then highest provided, send highest
            if (((Version) accepted.first()).compareTo(provided.last()) > 0) {
                version = ((Version) provided.last()).toString();
            }

            if (version == null) {
                //go through from lowest to highest, and return highest provided 
                // that is less than the highest accepted
                Iterator v = provided.iterator();
                Version last = (Version) v.next();

                for (; v.hasNext();) {
                    Version current = (Version) v.next();

                    if (current.compareTo(accepted.last()) > 0) {
                        break;
                    }

                    last = current;
                }

                version = last.toString();
            }
        }

        if ("1.0.0".equals(version)) {
            return new CapabilitiesTransformer.WFS1_0(wfs, catalog);
        }

        if ("1.1.0".equals(version)) {
            return new CapabilitiesTransformer.WFS1_1(wfs, catalog);
        }

        throw new WFSException("Could not understand version:" + version);
    }
}
