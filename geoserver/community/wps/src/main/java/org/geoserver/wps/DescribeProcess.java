/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.geoserver.ows.Ows11Util;
import org.geoserver.wfs.xml.XSProfile;
import org.geoserver.wps.ppio.ComplexPPIO;
import org.geoserver.wps.ppio.LiteralPPIO;
import org.geoserver.wps.ppio.ProcessParameterIO;
import org.geotools.data.Parameter;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.opengis.feature.type.Name;
import org.springframework.context.ApplicationContext;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.DomainMetadataType;
import net.opengis.wps.ComplexDataCombinationType;
import net.opengis.wps.ComplexDataCombinationsType;
import net.opengis.wps.ComplexDataDescriptionType;
import net.opengis.wps.DataInputsType;
import net.opengis.wps.DataInputsType1;
import net.opengis.wps.DescribeProcessType;
import net.opengis.wps.InputDescriptionType;
import net.opengis.wps.InputType;
import net.opengis.wps.LiteralDataType;
import net.opengis.wps.LiteralInputType;
import net.opengis.wps.LiteralOutputType;
import net.opengis.wps.OutputDescriptionType;
import net.opengis.wps.ProcessDescriptionType;
import net.opengis.wps.ProcessDescriptionsType;
import net.opengis.wps.ProcessOutputsType;
import net.opengis.wps.SupportedComplexDataInputType;
import net.opengis.wps.SupportedComplexDataType;
import net.opengis.wps.WpsFactory;

/**
 * First-call DescribeProcess class
 *
 * @author Lucas Reed, Refractions Research Inc
 * @author Justin Deoliveira, OpenGEO
 */
public class DescribeProcess {
    WPSInfo wps;
    ApplicationContext context;
    Locale locale;
    XSProfile xsp;
    
    public DescribeProcess(WPSInfo wps, ApplicationContext context) {
        this.wps = wps;
        this.context = context;
        locale = Locale.getDefault();
        
        //TODO: creating this ever time this operation is performed is sort of silly
        // some sort of singleton would be nice
        xsp = new XSProfile();
    }

    public ProcessDescriptionsType run(DescribeProcessType request) {
        
        WpsFactory f = WpsFactory.eINSTANCE;
        ProcessDescriptionsType pds = f.createProcessDescriptionsType();
        
        for ( Iterator i = request.getIdentifier().iterator(); i.hasNext(); ) {
            CodeType id = (CodeType) i.next();
            processDescription( id, pds, f );
        }
        
        return pds;
    }
    
    void processDescription( CodeType id, ProcessDescriptionsType pds, WpsFactory f ) {
        ProcessFactory pf = WPSUtils.findProcessFactory( id );
        if ( pf == null ) {
            throw new WPSException( "No such process: " + id.getValue() );
        }
        
        ProcessDescriptionType pd = f.createProcessDescriptionType();
        pds.getProcessDescription().add( pd );
        
        pd.setIdentifier( Ows11Util.code( id.getValue() ) );
        pd.setTitle( Ows11Util.languageString(pf.getTitle()) );
        pd.setAbstract( Ows11Util.languageString(pf.getDescription()) );
        
        //data inputs
        DataInputsType inputs = f.createDataInputsType();
        pd.setDataInputs(inputs);
        dataInputs( inputs, pf, f );
        
        //process outputs
        ProcessOutputsType outputs = f.createProcessOutputsType();
        pd.setProcessOutputs( outputs );
        processOutputs( outputs, pf, f );
     }
    
    void dataInputs( DataInputsType inputs, ProcessFactory pf, WpsFactory f ) {
        for(Parameter<?> p : pf.getParameterInfo().values()) {
            InputDescriptionType input = f.createInputDescriptionType();
            inputs.getInput().add( input );
            
            input.setIdentifier( Ows11Util.code( p.key ) );
            input.setTitle( Ows11Util.languageString( p.title ) );
            input.setAbstract( Ows11Util.languageString( p.description ) );
            
            // WPS spec specifies non-negative for unlimited inputs, so -1 -> 0
            input.setMaxOccurs( p.maxOccurs == -1 
                ? BigInteger.valueOf( Long.MAX_VALUE ) : BigInteger.valueOf( p.maxOccurs ) );
             
            input.setMinOccurs( BigInteger.valueOf( p.minOccurs ) );
            
            List<ProcessParameterIO> ppios = ProcessParameterIO.findAll( p, context);
            if ( ppios.isEmpty() ) {
                throw new WPSException( "Could not find process parameter for type " + p.key + "," + p.type );
            }
           
            //handle the literal case
            if ( ppios.size() == 1 && ppios.get( 0 ) instanceof LiteralPPIO ) {
                LiteralPPIO lppio = (LiteralPPIO) ppios.get( 0 );
                
                LiteralInputType literal = f.createLiteralInputType();
                input.setLiteralData( literal );
                
                //map the java class to an xml type name
                if ( !String.class.equals( lppio.getType() ) ) {
                    Name typeName = xsp.name( lppio.getType() ); 
                    if ( typeName != null ) {
                        literal.setDataType( Ows11Util.type( typeName.getLocalPart() ) );        
                    }    
                }

                //TODO: output the default value
            }
            else {
                //handle the complex data case
                SupportedComplexDataInputType complex = f.createSupportedComplexDataInputType();
                input.setComplexData( complex );
                
                complex.setSupported( f.createComplexDataCombinationsType() );
                for ( ProcessParameterIO ppio : ppios ) {
                    ComplexPPIO cppio = (ComplexPPIO) ppio;

                    ComplexDataDescriptionType format = f.createComplexDataDescriptionType();
                    format.setMimeType( cppio.getMimeType() );
                    
                    //add to supported
                    complex.getSupported().getFormat().add( format );
                    
                    //handle the default    
                    if ( complex.getDefault() == null ) {
                        ComplexDataDescriptionType def = f.createComplexDataDescriptionType();
                        def.setMimeType( format.getMimeType() );
                        
                        complex.setDefault( f.createComplexDataCombinationType() );
                        complex.getDefault().setFormat( def );
                    }
                }
            }
        }
    }
    
    void processOutputs( ProcessOutputsType outputs, ProcessFactory pf, WpsFactory f ) {
        Map<String,Parameter<?>> outs = pf.getResultInfo(null);
        for ( Parameter p : outs.values() ) {
            OutputDescriptionType output = f.createOutputDescriptionType();
            outputs.getOutput().add( output );
            
            output.setIdentifier( Ows11Util.code( p.key ) );
            output.setTitle( Ows11Util.languageString( p.title ) );
            
            List<ProcessParameterIO> ppios = ProcessParameterIO.findAll( p, context);
            if ( ppios.isEmpty() ) {
                throw new WPSException( "Could not find process parameter for type " + p.key + "," + p.type );
            }
            
            //handle the literal case
            if ( ppios.size() == 1 && ppios.get( 0 ) instanceof LiteralPPIO ) {
                LiteralPPIO lppio = (LiteralPPIO) ppios.get( 0 );
                
                LiteralOutputType literal = f.createLiteralOutputType();
                output.setLiteralOutput(literal);
                
                //map the java class to an xml type name
                if ( !String.class.equals( lppio.getType() ) ) {
                    Name typeName = xsp.name( lppio.getType() ); 
                    if ( typeName != null ) {
                        literal.setDataType( Ows11Util.type( typeName.getLocalPart() ) );        
                    }    
                }
            }
            else {
                //handle the complex data case
                SupportedComplexDataType complex = f.createSupportedComplexDataType();
                output.setComplexOutput( complex );
                
                complex.setSupported( f.createComplexDataCombinationsType() );
                for ( ProcessParameterIO ppio : ppios ) {
                    ComplexPPIO cppio = (ComplexPPIO) ppio;

                    ComplexDataDescriptionType format = f.createComplexDataDescriptionType();
                    format.setMimeType( cppio.getMimeType() );
                    
                    //add to supported
                    complex.getSupported().getFormat().add( format );
                    
                    //handle the default    
                    if ( complex.getDefault() == null ) {
                        ComplexDataDescriptionType def = f.createComplexDataDescriptionType();
                        def.setMimeType( format.getMimeType() );
                        
                        complex.setDefault( f.createComplexDataCombinationType() );
                        complex.getDefault().setFormat( def );
                    }
                }
            }
        }
    }
}