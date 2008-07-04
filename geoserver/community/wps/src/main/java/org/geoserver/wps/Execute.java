/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.util.Map;
import java.util.Locale;
import java.io.IOException;
import java.io.OutputStream;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.opengis.wps.WpsFactory;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.ows11.LanguageStringType;

import org.geotools.xml.Encoder;
import org.geotools.xml.Configuration;
import org.geotools.wps.WPSConfiguration;

import org.geoserver.wps.transmute.Transmuter;
import org.geoserver.wps.transmute.ComplexTransmuter;
import org.geoserver.wps.transmute.LiteralTransmuter;
import org.geotools.data.Parameter;
import org.geotools.process.ProcessFactory;

import net.opengis.wps.DataType;
import net.opengis.wps.StatusType;
import net.opengis.wps.ExecuteType;
import net.opengis.wps.OutputDataType;
import net.opengis.wps.LiteralDataType;
import net.opengis.wps.ComplexDataType;
import net.opengis.wps.ProcessBriefType;
import net.opengis.wps.ProcessOutputsType1;
import net.opengis.wps.ExecuteResponseType;

/**
 * Main class used to handle Execute requests
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public abstract class Execute
{
    /**
     * Specific implementation for WPS 1.0.0
     *
     * @author Lucas Reed, Refractions Research Inc
     */
    public static class WPS1_0
    {
        private WPS                 wps;
        private Locale              locale;
        private ExecuteType         request;
        private Executor            executor;
        private ExecuteResponseType response;
        private DataTransformer     dataTransformer;

        public WPS1_0(WPS wps)
        {
            this.wps      = wps;
            this.response = WpsFactory.eINSTANCE.createExecuteResponseType();
        }

        /**
         * Main method for performing decoding, execution, and response
         * @param object
         * @param output
         * @throws IllegalArgumentException
         */
        public void run(Object object, OutputStream output) throws IllegalArgumentException
        {
            this.request                = (ExecuteType)object;
            this.executor               = new Executor(this.request, this.wps);
            this.dataTransformer        = new DataTransformer(request.getBaseUrl());
            Map<String, Object> outputs = executor.execute();

            if (null == this.request.getLanguage())
            {
                this.locale = new Locale("en-CA");
            } else {
                this.locale = new Locale(this.request.getLanguage());
            }

            this.response.setService(this.request.getService());
            this.response.setVersion(this.request.getVersion());

            this.processBrief();
            this.status();
            this.outputs(outputs);

            Configuration config  = new WPSConfiguration();
            Encoder       encoder = new Encoder(config);
            encoder.setIndenting(true);

            try
            {
                encoder.encode(this.response, org.geotools.wps.WPS.ExecuteResponse, output);
            } catch(IOException e) {
                throw new WPSException("NoApplicableCode", "Error encoding execute response.");
            }
        }

        private void outputs(Map<String, Object> outputs)
        {
            ProcessFactory      pf             = this.executor.getProcessFactory();
            ProcessOutputsType1 processOutputs = WpsFactory.eINSTANCE.createProcessOutputsType1();

            for(String outputName : outputs.keySet())
            {
                Parameter<?> param = (pf.getResultInfo(null)).get(outputName);

                OutputDataType output = WpsFactory.eINSTANCE.createOutputDataType();

                CodeType identifier = Ows11Factory.eINSTANCE.createCodeType();
                identifier.setValue(param.key);
                output.setIdentifier(identifier);

                LanguageStringType title = Ows11Factory.eINSTANCE.createLanguageStringType();
                title.setValue(param.title.toString(this.locale));
                output.setTitle(title);

                DataType data = WpsFactory.eINSTANCE.createDataType();

                // Determine the output type, Complex or Literal
                Object outputParam = outputs.get(outputName);

                final Transmuter transmuter = this.dataTransformer.getDefaultTransmuter(outputParam.getClass());

                // Create appropriate response document node for given type
                if (transmuter instanceof ComplexTransmuter)
                {
                    data.setComplexData(this.complexData((ComplexTransmuter)transmuter, outputParam));
                } else {
                    if (transmuter instanceof LiteralTransmuter)
                    {
                        data.setLiteralData(this.literalData((LiteralTransmuter)transmuter, outputParam));
                    } else {
                        throw new WPSException("NoApplicableCode", "Could not find transmuter for output " + outputName);
                    }
                }

                output.setData(data);

                processOutputs.getOutput().add(output);
            }

            this.response.setProcessOutputs(processOutputs);
        }

        private LiteralDataType literalData(LiteralTransmuter transmuter, Object value)
        {
            LiteralDataType data = WpsFactory.eINSTANCE.createLiteralDataType();

            data.setValue(   transmuter.encode(value));
            data.setDataType(transmuter.getEncodedType());

            return data;
        }

        private ComplexDataType complexData(ComplexTransmuter transmuter, Object value)
        {
            ComplexDataType data = WpsFactory.eINSTANCE.createComplexDataType();

            data.setSchema(  transmuter.getSchema(this.request.getBaseUrl()));
            data.setMimeType(transmuter.getMimeType());
            data.getData().add(value);

            return data;
        }

        private void processBrief()
        {
            ProcessFactory     pf    = this.executor.getProcessFactory();
            ProcessBriefType   brief = WpsFactory.eINSTANCE.createProcessBriefType();
            LanguageStringType title = Ows11Factory.eINSTANCE.createLanguageStringType();

            brief.setProcessVersion(pf.getVersion());
            brief.setIdentifier(this.request.getIdentifier());
            title.setValue(pf.getTitle().toString(this.locale));
            brief.setTitle(title);

            this.response.setProcess(brief);
        }

        private void status()
        {
            StatusType status = WpsFactory.eINSTANCE.createStatusType();

            status.setProcessSucceeded("Process completed successfully.");

            XMLGregorianCalendar calendar;

            try
            {
                calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar());    // XXX TODO verify UTC
            } catch(Exception e) {
                throw new WPSException("NoApplicableCode", e.getMessage());
            }

            status.setCreationTime(calendar);

            this.response.setStatus(status);
        }
    }
}