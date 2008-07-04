/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.util.List;
import java.util.Locale;

import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import org.geotools.xlink.XLINK;
import org.geotools.filter.v1_1.OGC;
import org.geoserver.ows.xml.v1_0.OWS;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;

import net.opengis.wps.GetCapabilitiesType;

import org.geotools.process.Processors;
import org.geotools.process.ProcessFactory;
import org.geotools.xml.transform.Translator;
import org.geotools.xml.transform.TransformerBase;

/**
 * GetCapabilities transformer
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public abstract class CapabilitiesTransformer extends TransformerBase
{
    protected              WPS    wps;

    protected static final String WPS_URI = "http://www.opengis.net/wps";
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    public CapabilitiesTransformer(WPS wps)
    {
        super();

        this.wps  = wps;
    }

    /**
     * WPS 1.0.0 specific implementation
     *
     * @author Lucas Reed, Refractions Research Inc
     */
    public static class WPS1_0 extends CapabilitiesTransformer
    {
        public WPS1_0(WPS wps)
        {
            super(wps);
        }

        public Translator createTranslator(ContentHandler handler)
        {
            return new CapabilitiesTranslator1_0(handler);
        }

        public class CapabilitiesTranslator1_0 extends TranslatorSupport
        {
            public GetCapabilitiesType request;

            private Locale locale;

            public CapabilitiesTranslator1_0(ContentHandler handler)
            {
                super(handler, null, null);
            }

            public void encode(Object object) throws IllegalArgumentException
            {
                this.request = (GetCapabilitiesType)object;

                if (null == this.request.getLanguage())
                {
                    this.locale = new Locale("en-CA");
                } else {
                    this.locale = new Locale(this.request.getLanguage());
                }

                String proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(),
                		wps.getGeoServer().getProxyBaseUrl());

                AttributesImpl attrs = new AttributesImpl();
                attrs.addAttribute("", "xmlns:xsi",          "xmlns:xsi",   "",
                		CapabilitiesTransformer.XSI_URI);
                attrs.addAttribute("", "xmlns",              "xmlns",       "",
                		CapabilitiesTransformer.WPS_URI);
                attrs.addAttribute("", "xmlns:wps",          "xmlns:wps",   "",
                		CapabilitiesTransformer.WPS_URI);
                attrs.addAttribute("", "xmlns:ows",          "xmlns:ows",   "", OWS.NAMESPACE);
                attrs.addAttribute("", "version",            "version",     "", "1.0.0");
                attrs.addAttribute("", "xmlns:ogc",          "xmlns:ogc",   "", OGC.NAMESPACE);
                attrs.addAttribute("", "xmlns:xlink",        "xmlns:xlink", "", XLINK.NAMESPACE);
                attrs.addAttribute("", "xsi:schemaLocation", "xsi:schemaLocation", "",
                		"http://www.opengis.net/wps/1.0.0 ../wpsGetCapabilities_request.xsd");

                start("wps:Capabilities", attrs);
                    this.serviceIdentification();
                    this.serviceProvider();
                    this.operationsMetadata();
                    this.processOfferings();
                    this.languages();
                end("wps:Capabilities");
            }

            private void serviceIdentification()
            {
                start("ows:ServiceIdentification");

                element("ows:Title", wps.getTitle());
                element("ows:Abstract", wps.getAbstract());

                keywords((List<String>)wps.getKeywords());

                element("ows:ServiceType", "WPS");
                element("ows:ServiceTypeVersion", "1.0.0");

                element("ows:Fees", wps.getFees());
                element("ows:AccessConstraints", wps.getAccessConstraints());

                end("ows:ServiceIdentification");
            }

            private void serviceProvider()
            {
                start("ows:ServiceProvider");

                element("ows:ProviderName",   null);
                element("ows:ProviderSite",   null);
                element("ows:ServiceContact", null);

                end("ows:ServiceProvider");
            }

            private void operationsMetadata()
            {
                start("ows:OperationsMetadata", null);
                    this.operationGetCapabilities();
                end("ows:OperationsMetadata");
            }

            private void processOfferings()
            {
                start("wps:ProcessOfferings", null);

                for(ProcessFactory pf : Processors.getProcessFactories())
                {
                    start("wps:Process", null);
                        element("ows:Identifier", pf.getName());
                        element("ows:Title",      pf.getTitle().toString(this.locale));
                        element("ows:Abstract",   pf.getDescription().toString(this.locale));
                    end("wps:Process");
                }

                end("wps:ProcessOfferings");
            }

            private void languages()
            {
                start("wps:Languages", null);
                    start("wps:Default", null);
                        element("ows:Language", "en-CA");
                    end("wps:Default");
                    start("wps:Supported");
                        element("ows:Language", "en-CA");
                        element("ows:Language", "ko-KR");
                    end("wps:Supported");
                end("wps:Languages");
            }

            // Secondary methods

            private void operationGetCapabilities()
            {
                AttributesImpl attributes = new AttributesImpl();

                attributes.addAttribute("", "name", "name", "", "GetCapabilities");

                start("ows:Operation", attributes);

                end("ows:Operation");
            }

            // Utility methods

            private void keywords(String[] keywords)
            {
                if ((null == keywords) || (0 == keywords.length))
                {
                    return;
                }

                start("ows:Keywords");

                for (int i = 0; i < keywords.length; i++)
                {
                    element("ows:Keyword", keywords[i]);
                }

                end("ows:Keywords");
            }

            private void keywords(List<String> keywords)
            {
                if(null != keywords)
                {
                    keywords((String[])keywords.toArray(new String[keywords.size()]));
                }
            }
        }
    }
}
