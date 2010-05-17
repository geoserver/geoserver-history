package org.geoserver.rest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;

import org.geoserver.rest.xstream.CRSConverter;
import org.vfny.geoserver.config.WCSConfig;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.config.WMSConfig;

import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.OutputRepresentation;
import org.restlet.resource.Representation;

import com.thoughtworks.xstream.XStream;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class XStreamFormat implements DataFormat {

    public Representation makeRepresentation(final Object data) {
        return new OutputRepresentation(MediaType.APPLICATION_XML){
            public void write(OutputStream outputStream){
                XStream xstream = getXStream();
                xstream.toXML(data, outputStream);
            }
        };
    }

    public Object readRepresentation(Representation representation) {
        try{
            XStream xstream = getXStream();
            ObjectInputStream stream = xstream.createObjectInputStream(representation.getStream());
            return stream.readObject();
        } catch (Exception e){
            throw new RestletException("Couldn't parse input with XStream", Status.SERVER_ERROR_INTERNAL, e);
        }
    }

    protected XStream getXStream(){
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);
        xstream.alias("envelope", GeneralEnvelope.class);
        xstream.alias("WCS", WCSConfig.class);
        xstream.alias("WFS", WFSConfig.class);
        xstream.alias("WMS", WMSConfig.class);
        xstream.alias("crs", CoordinateReferenceSystem.class);
        xstream.aliasField("abstract", WMSConfig.class, "__abstract");
        xstream.registerConverter(new CRSConverter());
        return xstream;
    }

}
