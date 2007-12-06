/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows.xml.v1_0;

import org.geotools.xml.BindingConfiguration;
import org.picocontainer.MutablePicoContainer;


/**
 * Binding configuration for the http://www.opengis.net/ows schema.
 *
 * @generated
 */
public final class OWSBindingConfiguration implements BindingConfiguration {
    /**
     * @generated modifiable
     */
    public void configure(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(OWS.ACCEPTFORMATSTYPE,
            AcceptFormatsTypeBinding.class);
        container.registerComponentImplementation(OWS.ACCEPTVERSIONSTYPE,
            AcceptVersionsTypeBinding.class);
        container.registerComponentImplementation(OWS.ADDRESSTYPE,
            AddressTypeBinding.class);
        container.registerComponentImplementation(OWS.BOUNDINGBOXTYPE,
            BoundingBoxTypeBinding.class);
        container.registerComponentImplementation(OWS.CAPABILITIESBASETYPE,
            CapabilitiesBaseTypeBinding.class);
        container.registerComponentImplementation(OWS.CODETYPE,
            CodeTypeBinding.class);
        container.registerComponentImplementation(OWS.CONTACTTYPE,
            ContactTypeBinding.class);
        container.registerComponentImplementation(OWS.DESCRIPTIONTYPE,
            DescriptionTypeBinding.class);
        container.registerComponentImplementation(OWS.DOMAINTYPE,
            DomainTypeBinding.class);
        container.registerComponentImplementation(OWS.EXCEPTIONTYPE,
            ExceptionTypeBinding.class);
        container.registerComponentImplementation(OWS.GETCAPABILITIESTYPE,
            GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OWS.IDENTIFICATIONTYPE,
            IdentificationTypeBinding.class);
        container.registerComponentImplementation(OWS.KEYWORDSTYPE,
            KeywordsTypeBinding.class);
        container.registerComponentImplementation(OWS.METADATATYPE,
            MetadataTypeBinding.class);
        //container.registerComponentImplementation(OWS.MIMETYPE,MimeTypeBinding.class);
        container.registerComponentImplementation(OWS.ONLINERESOURCETYPE,
            OnlineResourceTypeBinding.class);
        container.registerComponentImplementation(OWS.POSITIONTYPE,
            PositionTypeBinding.class);
        container.registerComponentImplementation(OWS.POSITIONTYPE2D,
            PositionType2DBinding.class);
        container.registerComponentImplementation(OWS.REQUESTMETHODTYPE,
            RequestMethodTypeBinding.class);
        container.registerComponentImplementation(OWS.RESPONSIBLEPARTYSUBSETTYPE,
            ResponsiblePartySubsetTypeBinding.class);
        container.registerComponentImplementation(OWS.RESPONSIBLEPARTYTYPE,
            ResponsiblePartyTypeBinding.class);
        container.registerComponentImplementation(OWS.SECTIONSTYPE,
            SectionsTypeBinding.class);
        //container.registerComponentImplementation(OWS.SERVICETYPE,ServiceTypeBinding.class);
        container.registerComponentImplementation(OWS.TELEPHONETYPE,
            TelephoneTypeBinding.class);
        //container.registerComponentImplementation(OWS.UPDATESEQUENCETYPE,UpdateSequenceTypeBinding.class);
        //container.registerComponentImplementation(OWS.VERSIONTYPE,VersionTypeBinding.class);
        container.registerComponentImplementation(OWS.WGS84BOUNDINGBOXTYPE,
            WGS84BoundingBoxTypeBinding.class);

        //elements
        container.registerComponentImplementation(OWS.EXCEPTIONREPORT,
            ExceptionReportBinding.class);
    }
}
