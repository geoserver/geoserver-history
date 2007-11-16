/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs.xml;

import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDDerivationMethod;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDForm;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.wfs.WFS;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Schemas;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Builds a {@link org.eclipse.xsd.XSDSchema} from {@link FeatureTypeInfo}
 * metadata objects.
 * <p>
 *
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class FeatureTypeSchemaBuilder {
    /** logging instance */
    static Logger logger = org.geotools.util.logging.Logging.getLogger("org.geoserver.wfs");

    /** wfs configuration */
    WFS wfs;

    /** the catalog */
    Data catalog;

    /** resource loader */
    GeoServerResourceLoader resourceLoader;

    /**
     * profiles used for type mapping.
     */
    protected List profiles;

    /**
     * gml schema stuff
     */
    protected String gmlNamespace;
    protected String gmlSchemaLocation;
    protected Configuration xmlConfiguration;

    protected FeatureTypeSchemaBuilder(WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader) {
        this.wfs = wfs;
        this.catalog = catalog;
        this.resourceLoader = resourceLoader;

        profiles = new ArrayList();
        profiles.add(new XSProfile());
    }

    public XSDSchema build(FeatureTypeInfo featureTypeInfo, String baseUrl)
        throws IOException {
        return build(new FeatureTypeInfo[] { featureTypeInfo }, baseUrl);
    }

    public XSDSchema build(FeatureTypeInfo[] featureTypeInfos, String baseUrl)
        throws IOException {
        XSDFactory factory = XSDFactory.eINSTANCE;
        XSDSchema schema = factory.createXSDSchema();
        schema.setSchemaForSchemaQNamePrefix("xsd");
        schema.getQNamePrefixToNamespaceMap().put("xsd", XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001);
        schema.setElementFormDefault(XSDForm.get(XSDForm.QUALIFIED));

        //group the feature types by namespace
        HashMap ns2featureTypeInfos = new HashMap();

        for (int i = 0; i < featureTypeInfos.length; i++) {
            String prefix = featureTypeInfos[i].getNameSpace().getPrefix();
            List l = (List) ns2featureTypeInfos.get(prefix);

            if (l == null) {
                l = new ArrayList();
            }

            l.add(featureTypeInfos[i]);

            ns2featureTypeInfos.put(prefix, l);
        }
        
        if (baseUrl == null)
            baseUrl = wfs.getSchemaBaseURL(); 

        if (ns2featureTypeInfos.entrySet().size() == 1) {
            //import gml schema
            XSDImport imprt = factory.createXSDImport();
            imprt.setNamespace(gmlNamespace);

            imprt.setSchemaLocation( ResponseUtils.appendPath(baseUrl,
                    "schemas/" + gmlSchemaLocation));

            XSDSchema gmlSchema = gmlSchema();
            imprt.setResolvedSchema(gmlSchema);

            schema.getContents().add(imprt);

            String targetPrefix = (String) ns2featureTypeInfos.keySet().iterator().next();
            String targetNamespace = catalog.getNameSpace(targetPrefix).getURI();

            schema.setTargetNamespace(targetNamespace);
            //schema.getQNamePrefixToNamespaceMap().put( null, targetNamespace );
            schema.getQNamePrefixToNamespaceMap().put(targetPrefix, targetNamespace);
            schema.getQNamePrefixToNamespaceMap().put("gml", gmlNamespace);

            //all types in same namespace, write out the schema
            for (int i = 0; i < featureTypeInfos.length; i++) {
                buildSchemaContent(featureTypeInfos[i], schema, factory);
            }
        } else {
            //different namespaces, write out import statements
            for (Iterator i = ns2featureTypeInfos.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                String prefix = (String) entry.getKey();
                List types = (List) entry.getValue();

                StringBuffer queryString = new StringBuffer(
                        "request=DescribeFeatureType&version=1.1.0");
                queryString.append("&typeName=");

                for (Iterator t = types.iterator(); t.hasNext();) {
                    FeatureTypeInfo type = (FeatureTypeInfo) t.next();
                    queryString.append(type.getName());

                    if (t.hasNext()) {
                        queryString.append(",");
                    }
                }

                String schemaLocation = ResponseUtils.appendQueryString(baseUrl,
                        queryString.toString());
                String namespace = catalog.getNameSpace(prefix).getURI();

                XSDImport imprt = factory.createXSDImport();
                imprt.setNamespace(namespace);
                imprt.setSchemaLocation(schemaLocation);

                schema.getContents().add(imprt);
            }
        }

        return schema;
    }

    /**
     * Adds types defined in the catalog to the provided schema.
     */
    public XSDSchema addApplicationTypes( XSDSchema wfsSchema ) throws IOException {
        //incorporate application schemas into the wfs schema
        Collection featureTypeInfos = catalog.getFeatureTypeInfos().values();

        for (Iterator i = featureTypeInfos.iterator(); i.hasNext();) {
            FeatureTypeInfo meta = (FeatureTypeInfo) i.next();

            //build the schema for the types in the single namespace
            XSDSchema schema = build(new FeatureTypeInfo[] { meta }, null);

            //declare the namespace
            String prefix = meta.getNameSpace().getPrefix();
            String namespaceURI = meta.getNameSpace().getURI();
            wfsSchema.getQNamePrefixToNamespaceMap().put(prefix, namespaceURI);

            //add the types + elements to the wfs schema
            for (Iterator t = schema.getTypeDefinitions().iterator(); t.hasNext();) {
                wfsSchema.getTypeDefinitions().add(t.next());
            }

            for (Iterator e = schema.getElementDeclarations().iterator(); e.hasNext();) {
                wfsSchema.getElementDeclarations().add(e.next());
            }
        }

        return wfsSchema;
    }
    
    void buildSchemaContent(FeatureTypeInfo featureTypeMeta, XSDSchema schema, XSDFactory factory)
        throws IOException {
        //look if the schema for the type is already defined
        String prefix = featureTypeMeta.getNameSpace().getPrefix();
        String name = featureTypeMeta.getTypeName();

        File schemaFile = null;

        try {
            schemaFile = resourceLoader.find("featureTypes/" + prefix + "_" + name + "/schema.xsd");
        } catch (IOException e1) {
        }

        if (schemaFile != null) {
            //schema file found, parse it and lookup the complex type
            List resolvers = Schemas.findSchemaLocationResolvers(xmlConfiguration);
            XSDSchema ftSchema = null;

            try {
                ftSchema = Schemas.parse(schemaFile.getAbsolutePath(), null, resolvers);
            } catch (IOException e) {
                logger.log(Level.WARNING,
                    "Unable to parse schema: " + schemaFile.getAbsolutePath(), e);
            }

            if (ftSchema != null) {
                //add the contents of this schema to the schema being built
                //look up the complex type
                List contents = ftSchema.getContents();

                for (Iterator i = contents.iterator(); i.hasNext();) {
                    XSDSchemaContent content = (XSDSchemaContent) i.next();
                    content.setElement(null);
                }

                schema.getContents().addAll(contents);
                schema.updateElement();

                return;
            }
        }

        //build the type manually
        SimpleFeatureType featureType = featureTypeMeta.getFeatureType();
        XSDComplexTypeDefinition complexType = factory.createXSDComplexTypeDefinition();
        complexType.setName(featureType.getTypeName() + "Type");

        complexType.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);
        complexType.setBaseTypeDefinition(schema.resolveComplexTypeDefinition(gmlNamespace,
                "AbstractFeatureType"));

        XSDModelGroup group = factory.createXSDModelGroup();
        group.setCompositor(XSDCompositor.SEQUENCE_LITERAL);

        List attributes = featureType.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            AttributeDescriptor attribute = (AttributeDescriptor) attributes.get(i);

            if ( filterAttributeType( attribute ) ) {
                continue;
            }
           
            XSDElementDeclaration element = factory.createXSDElementDeclaration();
            element.setName(attribute.getLocalName());
            element.setNillable(attribute.isNillable());

            Class binding = attribute.getType().getBinding();
            Name typeName = findTypeName(binding);

            if (typeName == null) {
                throw new NullPointerException("Could not find a type for property: "
                    + attribute.getName() + " of type: " + binding.getName());
            }

            XSDTypeDefinition type = schema.resolveTypeDefinition(typeName.getNamespaceURI(),
                    typeName.getLocalPart());
            element.setTypeDefinition(type);

            XSDParticle particle = factory.createXSDParticle();
            particle.setMinOccurs(attribute.getMinOccurs());
            particle.setMaxOccurs(attribute.getMaxOccurs());
            particle.setContent(element);
            group.getContents().add(particle);
        }

        XSDParticle particle = factory.createXSDParticle();
        particle.setContent(group);

        complexType.setContent(particle);

        schema.getContents().add(complexType);

        XSDElementDeclaration element = factory.createXSDElementDeclaration();
        element.setName(name);

        element.setSubstitutionGroupAffiliation(schema.resolveElementDeclaration(gmlNamespace,
                "_Feature"));
        element.setTypeDefinition(complexType);

        schema.getContents().add(element);
        schema.updateElement();

        schema.updateElement();
    }

   
    
    Name findTypeName(Class binding) {
        for (Iterator p = profiles.iterator(); p.hasNext();) {
            TypeMappingProfile profile = (TypeMappingProfile) p.next();
            Name name = profile.name(binding);

            if (name != null) {
                return name;
            }
        }

        return null;
    }

    protected abstract XSDSchema gmlSchema();

    protected boolean filterAttributeType( AttributeDescriptor attribute ) {
        return "name".equals( attribute.getName() ) 
            || "description".equals( attribute.getName()) 
            || "boundedBy".equals( attribute.getName());
    }
    
    public static final class GML2 extends FeatureTypeSchemaBuilder {
        /**
         * Cached gml2 schema
         */
        private static XSDSchema gml2Schema;

        public GML2(WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader) {
            super(wfs, catalog, resourceLoader);

            profiles.add(new GML2Profile());
            gmlNamespace = org.geotools.gml2.GML.NAMESPACE;
            gmlSchemaLocation = "gml/2.1.2/feature.xsd";
            xmlConfiguration = new GMLConfiguration();
        }

        protected XSDSchema gmlSchema() {
            if (gml2Schema == null) {
                gml2Schema = xmlConfiguration.schema();
            }

            return gml2Schema;
        }
    }

    public static final class GML3 extends FeatureTypeSchemaBuilder {
        /**
         * Cached gml3 schema
         */
        private static XSDSchema gml3Schema;

        public GML3(WFS wfs, Data catalog, GeoServerResourceLoader resourceLoader) {
            super(wfs, catalog, resourceLoader);

            profiles.add(new GML3Profile());

            gmlNamespace = org.geotools.gml3.GML.NAMESPACE;
            gmlSchemaLocation = "gml/3.1.1/base/gml.xsd";
            xmlConfiguration = new org.geotools.gml3.GMLConfiguration();
        }

        protected XSDSchema gmlSchema() {
            if (gml3Schema == null) {
                gml3Schema = xmlConfiguration.schema();
            }

            return gml3Schema;
        }
        
        protected boolean filterAttributeType( AttributeDescriptor attribute ) {
            return super.filterAttributeType( attribute ) || 
                "metaDataProperty".equals( attribute.getName() ) || 
                "location".equals( attribute.getName() );
        }
    }
}
