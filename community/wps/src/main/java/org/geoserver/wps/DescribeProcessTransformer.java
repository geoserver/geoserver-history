/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 *	@author lreed@refractions.net
 */

package org.geoserver.wps;

import java.util.Locale;
import net.opengis.ows11.CodeType;
import net.opengis.wps.DescribeProcessType;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.geoserver.ows.xml.v1_0.OWS;
import org.geotools.data.Parameter;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;

import org.geoserver.wps.transmute.ComplexTransmuter;
import org.geoserver.wps.transmute.LiteralTransmuter;
import org.geoserver.wps.transmute.Transmuter;

public abstract class DescribeProcessTransformer extends TransformerBase
{
    protected WPS wps;

    protected static final String WPS_URI = "http://www.opengis.net/wps";
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    public DescribeProcessTransformer(WPS wps)
    {
        super();

        this.wps  = wps;
    }

    public static class WPS1_0 extends DescribeProcessTransformer
    {
        public WPS1_0(WPS wps)
        {
            super(wps);
        }

        public Translator createTranslator(ContentHandler handler)
        {
            return new DescribeProcessTranslator1_0(handler);
        }

        public class DescribeProcessTranslator1_0 extends TranslatorSupport
        {
            public DescribeProcessType request;

            private Locale locale;

            private DataTransformer dataTransformer;

            public DescribeProcessTranslator1_0(ContentHandler handler)
            {
                super(handler, null, null);

                this.dataTransformer = new DataTransformer();
            }

            public void encode(Object object) throws IllegalArgumentException
            {
                this.request = (DescribeProcessType)object;

                if (null == this.request.getLanguage())
                {
                    this.locale = new Locale("en-CA");
                } else {
                    this.locale = new Locale(this.request.getLanguage());
                }

                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "xmlns:xsi", "xmlns:xsi", "", DescribeProcessTransformer.XSI_URI);
                attributes.addAttribute("", "xmlns",     "xmlns",     "", DescribeProcessTransformer.WPS_URI);
                attributes.addAttribute("", "xmlns:wps", "xmlns:wps", "", DescribeProcessTransformer.WPS_URI);
                attributes.addAttribute("", "xmlns:ows", "xmlns:ows", "", OWS.NAMESPACE);
                attributes.addAttribute("", "version",   "version",   "", "1.0.0");

                start("wps:ProcessDescriptions", attributes);

                for (Object identifier : this.request.getIdentifier())
                {
                    this.processDescription(((CodeType)identifier).getValue());
                }

                end("wps:ProcessDescriptions");
            }

            private void processDescription(String identifier)
            {
                if ("all".equals(identifier))
                {
                    this.processDescriptionAll();

                    return;
                }

                ProcessFactory pf = this.findProcessFactory(identifier);

                if (null == pf)
                {
                    throw new WPSException("Invalid identifier", "InvalidParameterValue");
                }

                this.processDescription(pf);
            }

            private void processDescription(ProcessFactory pf)
            {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "wps:processVersion", "wps:processVersion", "", pf.getVersion());
                attributes.addAttribute("", "statusSupported",    "statusSupported",    "", Boolean.toString(pf.supportsProgress()));

                start("ProcessDescription", attributes);
                    element("ows:Identifier", pf.getName());
                    element("ows:Title",      pf.getTitle().toString(this.locale));
                    element("ows:Abstract",   pf.getDescription().toString(this.locale));
                    this.dataInputs(pf);
                    this.processOutputs(pf);
                end("ProcessDescription");
            }

            private void processDescriptionAll()
            {
                for (ProcessFactory pf : Processors.getProcessFactories())
                {
                    this.processDescription(pf);
                }
            }

            private ProcessFactory findProcessFactory(String name)
            {
                for (ProcessFactory pf : Processors.getProcessFactories())
                {
                    if (pf.getName().equals(name))
                    {
                        return pf;
                    }
                }

                return null;
            }

            private void dataInputs(ProcessFactory pf)
            {
                start("DataInputs");
                for(Parameter<?> inputIdentifier : pf.getParameterInfo().values())
                {
                    AttributesImpl attributes = new AttributesImpl();
                    attributes.addAttribute("", "minOccurs", "minOccurs", "", "" + inputIdentifier.minOccurs);
                    attributes.addAttribute("", "maxOccurs", "maxOccurs", "", "" + inputIdentifier.maxOccurs);

                    start("Input", attributes);
                        element("ows:Identifier", inputIdentifier.key);
                        element("ows:Title",      inputIdentifier.title.toString(      this.locale));
                        element("ows:Abstract",   inputIdentifier.description.toString(this.locale));
                        Transmuter transmuter = this.dataTransformer.getDefaultTransmuter(inputIdentifier.type);
                        if (transmuter instanceof ComplexTransmuter)
                        {
                            start("ComplexData");
                                this.complexParameter((ComplexTransmuter)transmuter);
                            end("ComplexData");
                        } else {
                            this.literalData((LiteralTransmuter)transmuter);
                        }
                    end("Input");
                }
                end("DataInputs");
            }

            private void processOutputs(ProcessFactory pf)
            {
                start("ProcessOutputs");
                for (Parameter<?> outputIdentifier : pf.getResultInfo(null).values())
                {
                    start("Output");
                        element("ows:Identifier", outputIdentifier.key);
                        element("ows:Title",      outputIdentifier.title.toString(      this.locale));
                        element("ows:Abstract",   outputIdentifier.description.toString(this.locale));
                        Transmuter transmuter = this.dataTransformer.getDefaultTransmuter(outputIdentifier.type);
                        if (transmuter instanceof ComplexTransmuter)
                        {
                            start("ComplexOutput");
                                this.complexParameter((ComplexTransmuter)transmuter);
                            end("ComplexOutput");
                        } else {
                            this.literalData((LiteralTransmuter)transmuter);
                        }
                    end("Output");
                }
                end("ProcessOutputs");
            }

            private void literalData(LiteralTransmuter transmuter)
            {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "ows:reference", "ows:reference", "", transmuter.getEncodedType());

                start("LiteralData");
                    start("ows:DataType", attributes);
                    end("ows:DataType");
                end("LiteralData");
            }

            private void complexParameter(ComplexTransmuter transmuter)
            {
                start("Default");
                    this.format(transmuter);    // In future, this should select the default format
                end("Default");
                start("Supported");
                    this.format(transmuter);    // In future, this should iterate over all formats
                end("Supported");
            }

            private void format(ComplexTransmuter transmuter)
            {
                String schemaURL = this.request.getBaseUrl() + "ows?service=WPS&request=getSchema&identifier=";

                start("Format");
                    element("MimeType", transmuter.getMimeType());
                    element("Schema",   schemaURL + transmuter.getSchema());
                end("Format");
            }
        }
    }
}