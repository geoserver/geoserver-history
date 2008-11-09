/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.ows11.ResponsiblePartySubsetType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.ServiceProviderType;
import net.opengis.wps.DefaultType2;
import net.opengis.wps.GetCapabilitiesType;
import net.opengis.wps.LanguagesType;
import net.opengis.wps.LanguagesType1;
import net.opengis.wps.ProcessBriefType;
import net.opengis.wps.ProcessOfferingsType;
import net.opengis.wps.WPSCapabilitiesType;
import net.opengis.wps.WpsFactory;

import org.geoserver.config.GeoServerInfo;
import org.geoserver.ows.Ows11Util;
import org.geoserver.ows.util.RequestUtils;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;

/**
 * @author Lucas Reed, Refractions Research Inc
 */
public class GetCapabilities {
    public WPSInfo wps;

    public GetCapabilities(WPSInfo wps) {
        this.wps = wps;
    }

    public WPSCapabilitiesType run(GetCapabilitiesType request) throws WPSException {
        // do the version negotiation dance
        List<String> provided = Collections.singletonList("1.0.0");
        List<String> accepted = null;
        if (request.getAcceptVersions() != null)
            accepted = request.getAcceptVersions().getVersion();
        String version = RequestUtils.getVersionOws11(provided, accepted);

        if (!"1.0.0".equals(version)) {
            throw new WPSException("Could not understand version:" + version);
        }

        // TODO: add update sequence negotiation
        
        // encode the response
        WpsFactory wpsf = WpsFactory.eINSTANCE;
        Ows11Factory owsf = Ows11Factory.eINSTANCE;
        
        WPSCapabilitiesType caps = wpsf.createWPSCapabilitiesType();
        caps.setVersion("1.0.0");
        
        //ServiceIdentification
        ServiceIdentificationType si = owsf.createServiceIdentificationType();
        caps.setServiceIdentification( si );
        
        si.getTitle().add( Ows11Util.languageString( wps.getTitle() ) );
        si.getAbstract().add( Ows11Util.languageString( wps.getAbstract() ) );
        si.getKeywords().add( Ows11Util.keywords( wps.getKeywords() ) );
        si.setServiceType( Ows11Util.code( "WPS" ) );
        si.getServiceTypeVersion().add( "1.0.0" );
        si.setFees( wps.getFees() );
        
        if ( wps.getAccessConstraints() != null ) {
            si.getAccessConstraints().add( wps.getAccessConstraints() );    
        }
        
        //ServiceProvider
        ServiceProviderType sp = owsf.createServiceProviderType();
        caps.setServiceProvider( sp );
        
        sp.setProviderSite(owsf.createOnlineResourceType());
        sp.getProviderSite().setHref( wps.getGeoServer().getOnlineResource() );
        sp.setServiceContact( responsibleParty( wps.getGeoServer(), owsf ) );
        
        //OperationsMetadata
        OperationsMetadataType om = owsf.createOperationsMetadataType();
        caps.setOperationsMetadata( om );
        
        OperationType gco = owsf.createOperationType();
        gco.setName("GetCapabilities");
        om.getOperation().add( gco );
        
        OperationType dpo = owsf.createOperationType();
        dpo.setName( "DescribeProcess");
        om.getOperation().add( dpo );
        
        OperationType eo = owsf.createOperationType();
        eo.setName( "Execute" );
        om.getOperation().add( eo );
        
        ProcessOfferingsType po = wpsf.createProcessOfferingsType();
        caps.setProcessOfferings( po );
        
        for(ProcessFactory pf : Processors.getProcessFactories()) {
            ProcessBriefType p = wpsf.createProcessBriefType();
            p.setProcessVersion(pf.getVersion());
            po.getProcess().add( p );
            
            p.setIdentifier( Ows11Util.code( pf.getName()) );
            p.setTitle( Ows11Util.languageString( pf.getTitle().toString() ) );
            p.setAbstract( Ows11Util.languageString( pf.getDescription().toString() ) );
        }

        LanguagesType1 l = wpsf.createLanguagesType1();
        final DefaultType2 defaultLang = wpsf.createDefaultType2();
        defaultLang.setLanguage("en");
        l.setDefault(defaultLang);
        LanguagesType language = wpsf.createLanguagesType();
        language.setLanguage("en");
        l.setSupported(language);
        caps.setLanguages( l );
        
        l.setDefault( defaultLang );
        l.getDefault().setLanguage( Locale.getDefault().getLanguage() );
        
        return caps;
        // Version detection and alternative invocation if being implemented.
    }
    
    ResponsiblePartySubsetType responsibleParty( GeoServerInfo global, Ows11Factory f ) {
        ResponsiblePartySubsetType rp = f.createResponsiblePartySubsetType();
        return rp;
    }
}