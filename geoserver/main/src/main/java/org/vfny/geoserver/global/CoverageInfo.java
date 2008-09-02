/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.awt.Rectangle;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MetadataLinkInfo;
import org.geoserver.catalog.StyleInfo;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.range.RangeType;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.Style;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.vfny.geoserver.global.dto.CoverageInfoDTO;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 *         
 * @deprecated use {@link CoverageInfo}
 */
public final class CoverageInfo extends GlobalLayerSupertype {
    ///**
    // *
    // */
    //private String formatId;
    //
    ///**
    // *
    // */
    //private String name;
    //
    ///**
    // *
    // */
    //private String wmsPath;
    //
    ///**
    // *
    // */
    //private String label;
    //
    ///**
    // *
    // */
    //private String description;
    //
    ///**
    // *
    // */
    //private MetaDataLink metadataLink;
    //
    ///**
    // *
    // */
    //private String dirName;
    //
    ///**
    // *
    // */
    //private List keywords;
    //
    ///**
    // *
    // */
    //private GeneralEnvelope envelope;
    //
    ///**
    // *
    // */
    //private GeneralEnvelope lonLatWGS84Envelope;
    //
    ///**
    // *
    // */
    //private GridGeometry grid;
    //
    ///**
    // *
    // */
    //private CoverageDimension[] dimensions;
    //
    ///**
    // *
    // */
    //private InternationalString[] dimensionNames;
    //
    ///**
    // *
    // */
    //private List requestCRSs;
    //
    ///**
    // *
    // */
    //private List responseCRSs;
    //
    ///**
    // *
    // */
    //private String nativeFormat;
    //
    ///**
    // *
    // */
    //private List supportedFormats;
    //
    ///**
    // *
    // */
    //private String defaultInterpolationMethod;
    //
    ///**
    // *
    // */
    //private List interpolationMethods;
    //
    ///**
    // *
    // */
    //private Data data;
    //
    ///**
    // *
    // */
    //private Map meta;
    //
    ///**
    // *
    // */
    //private String srsName;
    //
    ///**
    // *
    // */
    //private String srsWKT;
    //
    ///**
    // *
    // */
    //private CoordinateReferenceSystem crs;
    //
    ///**
    // * Default style used to render this Coverage with WMS
    // */
    //private String defaultStyle;
    //
    ///**
    // * Other WMS Styles
    // */
    //private ArrayList styles;
    //
    ///**
    //     * String representation of connection parameter values
    //     */
    //private Map parameters;

    LayerInfo layer;
    org.geoserver.catalog.CoverageInfo coverage;
    Catalog catalog;
        
    //public CoverageInfo(CoverageInfoDTO dto, Data data)
    //    throws ConfigurationException {
    //    this.data = data;
    //    formatId = dto.getFormatId();
    //    name = dto.getName();
    //    wmsPath = dto.getWmsPath();
    //    label = dto.getLabel();
    //    description = dto.getDescription();
    //    metadataLink = dto.getMetadataLink();
    //    dirName = dto.getDirName();
    //    keywords = dto.getKeywords();
    //    crs = dto.getCrs();
    //    srsName = dto.getSrsName();
    //    srsWKT = dto.getSrsWKT();
    //    envelope = dto.getEnvelope();
    //    lonLatWGS84Envelope = dto.getLonLatWGS84Envelope();
    //    grid = dto.getGrid();
    //    dimensions = dto.getDimensions();
    //    dimensionNames = dto.getDimensionNames();
    //    requestCRSs = dto.getRequestCRSs();
    //    responseCRSs = dto.getResponseCRSs();
    //    nativeFormat = dto.getNativeFormat();
    //    supportedFormats = dto.getSupportedFormats();
    //    defaultInterpolationMethod = dto.getDefaultInterpolationMethod();
    //    interpolationMethods = dto.getInterpolationMethods();
    //    defaultStyle = dto.getDefaultStyle();
    //    styles = dto.getStyles();
    //    parameters = dto.getParameters();
    //}
    
    public CoverageInfo( LayerInfo layer, Catalog catalog ) {
        this.layer = layer;
        this.catalog = catalog;
        this.coverage = (org.geoserver.catalog.CoverageInfo) layer.getResource();
    }

    public void load( CoverageInfoDTO dto ) {
        org.geoserver.catalog.CoverageStoreInfo cs = catalog.getCoverageStoreByName(dto.getFormatId());
        coverage.setStore( cs );
        coverage.setName( dto.getName() );
        coverage.setDescription(dto.getDescription());
        
        coverage.getMetadataLinks().clear();
        if ( dto.getMetadataLink() != null ) {
            MetadataLinkInfo ml = catalog.getFactory().createMetadataLink();
            new MetaDataLink(ml).load(dto.getMetadataLink());
            coverage.getMetadataLinks().add( ml );
        }
        
        coverage.getMetadata().put( "dirName", dto.getDirName() );
        coverage.getKeywords().clear();
        coverage.getKeywords().addAll( dto.getKeywords() );
        
        coverage.setNativeCRS(dto.getCrs());
        coverage.setNativeBoundingBox(new ReferencedEnvelope(dto.getEnvelope()));
        coverage.setLatLonBoundingBox(new ReferencedEnvelope(dto.getLonLatWGS84Envelope()));
        
        coverage.setVerticalCRS(dto.getVerticalCRS());
        coverage.setTemporalCRS(dto.getTemporalCRS());
        
        coverage.setVerticalExtent(dto.getVerticalExtent());
        coverage.setTemporalExtent(dto.getTemporalExtent());
        
        coverage.setGrid(dto.getGrid());
        coverage.setFields(dto.getFields());
        
        coverage.getRequestSRS().clear();
        coverage.getRequestSRS().addAll( dto.getRequestCRSs() );

        coverage.getResponseSRS().clear();
        coverage.getResponseSRS().addAll( dto.getResponseCRSs() );
        
        coverage.getSupportedFormats().clear();
        coverage.getSupportedFormats().addAll( dto.getSupportedFormats() );
        
        coverage.getInterpolationMethods().clear();
        coverage.getInterpolationMethods().addAll( dto.getInterpolationMethods() );
        coverage.setDefaultInterpolationMethod(dto.getDefaultInterpolationMethod());
        
        coverage.setNativeFormat(dto.getNativeFormat());
        coverage.setSRS( dto.getSrsName() );
        
        coverage.getParameters().clear();
        coverage.getParameters().putAll( dto.getParameters() );
        coverage.setEnabled( cs.isEnabled() );
        
        layer.setDefaultStyle(catalog.getStyleByName(dto.getDefaultStyle()));
        layer.getStyles().clear();
        for ( Iterator s = dto.getStyles().iterator(); s.hasNext(); ) {
            String styleName = (String) s.next();
            layer.getStyles().add( catalog.getStyleByName( styleName ) );
        }
        layer.setPath(dto.getWmsPath());
        layer.setName( coverage.getName() );
        layer.setType( LayerInfo.Type.RASTER );
        
        //label = dto.getLabel();
        //srsWKT = dto.getSrsWKT();
    }
    
    Object toDTO() {
        CoverageInfoDTO dto = new CoverageInfoDTO();

        dto.setFormatId(getFormatId());
        dto.setName(getCoverageName());
        dto.setWmsPath(getWmsPath());
        dto.setLabel(getLabel());
        dto.setDescription(getDescription());
        dto.setMetadataLink(getMetadataLink());
        dto.setDirName(getDirName());
        dto.setKeywords(getKeywords());
        dto.setCrs(getCrs());
        dto.setSrsName(getSrsName());
        dto.setSrsWKT(getSrsWKT());
        dto.setTemporalCRS(getTemporalCRS());
        dto.setVerticalCRS(getVerticalCRS());
        dto.setEnvelope(getEnvelope());
        dto.setVerticalExtent(getVerticalExtent());
        dto.setTemporalExtent(getTemporalExtent());
        dto.setLonLatWGS84Envelope(getWGS84LonLatEnvelope());
        dto.setGrid(getGrid());
        dto.setFields(getFields());
        dto.setRequestCRSs(getRequestCRSs());
        dto.setResponseCRSs(getResponseCRSs());
        dto.setNativeFormat(getNativeFormat());
        dto.setSupportedFormats(getSupportedFormats());
        dto.setDefaultInterpolationMethod(getDefaultInterpolationMethod());
        dto.setInterpolationMethods(getInterpolationMethods());
        if ( getDefaultStyle() != null ) {
            dto.setDefaultStyle(getDefaultStyle().getName());    
        }
        
        
        ArrayList styles = new ArrayList();
        for ( Iterator s = getStyles().iterator(); s.hasNext(); ) {
            Style style = (Style) s.next();
            styles.add( style.getName() );
        }
        dto.setStyles(styles);
        dto.setParameters(getParameters());
        
        //dto.setFormatId(formatId);
        //dto.setName(name);
        //dto.setWmsPath(wmsPath);
        //dto.setLabel(label);
        //dto.setDescription(description);
        //dto.setMetadataLink(metadataLink);
        //dto.setDirName(dirName);
        //dto.setKeywords(keywords);
        //dto.setCrs(crs);
        //dto.setSrsName(srsName);
        //dto.setSrsWKT(srsWKT);
        //dto.setEnvelope(envelope);
        //dto.setLonLatWGS84Envelope(lonLatWGS84Envelope);
        //dto.setGrid(grid);
        //dto.setDimensions(dimensions);
        //dto.setDimensionNames(dimensionNames);
        //dto.setRequestCRSs(requestCRSs);
        //dto.setResponseCRSs(responseCRSs);
        //dto.setNativeFormat(nativeFormat);
        //dto.setSupportedFormats(supportedFormats);
        //dto.setDefaultInterpolationMethod(defaultInterpolationMethod);
        //dto.setInterpolationMethods(interpolationMethods);
        //dto.setDefaultStyle(defaultStyle);
        //dto.setStyles(styles);
        //dto.setParameters(parameters);

        return dto;
    }

    private CoordinateReferenceSystem getVerticalCRS() {
        return coverage.getVerticalCRS();
    }

    private CoordinateReferenceSystem getTemporalCRS() {
        return coverage.getTemporalCRS();
    }

    private Set<TemporalGeometricPrimitive> getTemporalExtent() {
        return coverage.getTemporalExtent();
    }

    private Set<Envelope> getVerticalExtent() {
        return coverage.getVerticalExtent();
    }

    private RangeType getFields() {
        return coverage.getFields();
    }

    public CoverageStoreInfo getFormatInfo() {
        return new CoverageStoreInfo( coverage.getStore(), catalog );
        //return data.getFormatInfo(formatId);
    }

    public boolean isEnabled() {
        return coverage.isEnabled();
        //return (getFormatInfo() != null) && (getFormatInfo().isEnabled());
    }

    public CoverageStoreInfo getFormatMetaData() {
        return new CoverageStoreInfo( coverage.getStore(), catalog );
        //return data.getFormatInfo(formatId);
    }

    public boolean containsMetaData(String key) {
        return coverage.getMetadata().get( key ) != null;
        //return meta.containsKey(key);
    }

    public void putMetaData(String key, Object value) {
        coverage.getMetadata().put( key, (Serializable) value );
        //meta.put(key, value);
    }

    public Object getMetaData(String key) {
        return coverage.getMetadata().get( key );
        //return meta.get(key);
    }

    /**
     * @return Returns the data.
     */
    public Data getData() {
        throw new UnsupportedOperationException();
        //return data;
    }

    /**
     * @return Returns the defaultInterpolationMethod.
     */
    public String getDefaultInterpolationMethod() {
        return coverage.getDefaultInterpolationMethod();
        //return defaultInterpolationMethod;
    }

    /**
     * @return Returns the description.
     */
    public String getDescription() {
        return coverage.getDescription();
        //return description;
    }

    /**
     * @return Returns the dirName.
     */
    public String getDirName() {
        return (String) coverage.getMetadata().get( "dirName" );
        //return dirName;
    }

    /**
     * @return Returns the envelope.
     */
    public GeneralEnvelope getEnvelope() {
        try {
            return new GeneralEnvelope( coverage.getBoundingBox() );
        } 
        catch (Exception e) {
            throw new RuntimeException( e );
        }
        //return envelope;
    }

    /**
     * @return Returns the formatId.
     */
    public String getFormatId() {
        return coverage.getStore().getName();
        //return coverage.getName();
        //return formatId;
    }

    /**
     * @return Returns the interpolationMethods.
     */
    public List getInterpolationMethods() {
        return coverage.getInterpolationMethods();
        //return interpolationMethods;
    }

    /**
     * @return Returns the keywords.
     */
    public List getKeywords() {
        return coverage.getKeywords();
        //return keywords;
    }

    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return coverage.getTitle();
        //return label;
    }

    /**
     * @return Returns the meta.
     */
    public Map getMeta() {
        return coverage.getMetadata();
        //return meta;
    }

    /**
     * @return Returns the metadataLink.
     */
    public MetaDataLink getMetadataLink() {
        return coverage.getMetadataLinks().isEmpty() ? 
            null : new MetaDataLink( coverage.getMetadataLinks().get( 0 ) );
        //return metadataLink;
    }

    /**
     * @return String the namespace prefix.
     */
    public String getPrefix() {
        return coverage.getNamespace().getPrefix();
        //CoverageStoreInfo info = getFormatInfo();
        //
        //if (info != null) {
        //    return info.getNameSpace().getPrefix();
        //}
        //
        //return null;
    }

    /**
     * @return NameSpaceInfo the namespace specified for the specified
     *         CoverageStoreInfo (by ID)
     *
     * @throws IllegalStateException
     *             Thrown when disabled.
     */
    public NameSpaceInfo getNameSpace() {
        if (!isEnabled()) {
            throw new IllegalStateException("This coverage is not " + "enabled");
        }

        return new NameSpaceInfo( coverage.getNamespace(), catalog );
        //return getFormatInfo().getNameSpace();
    }

    public String getCoverageName() {
        return coverage.getName();
    }
    
    /**
     * @return Returns the name.
     */
    public String getName() {
        return coverage.getPrefixedName();
        //return getPrefix() + ":" + name;
    }

    /**
     * @return Returns the nativeFormat.
     */
    public String getNativeFormat() {
        return coverage.getNativeFormat();
        //return nativeFormat;
    }

    /**
     * @return Returns the requestCRSs.
     */
    public List getRequestCRSs() {
        return coverage.getRequestSRS();
        //return requestCRSs;
    }

    /**
     * @return Returns the responseCRSs.
     */
    public List getResponseCRSs() {
        return coverage.getResponseSRS();
        //return responseCRSs;
    }

    /**
     * @return Returns the srsName.
     */
    public String getSrsName() {
        return coverage.getSRS();
        //return srsName;
    }

    /**
     * @return Returns the supportedFormats.
     */
    public List getSupportedFormats() {
        return coverage.getSupportedFormats();
        //return supportedFormats;
    }

    /**
     * By now just return the default style to be able to declare it in WMS
     * capabilities, but all this stuff needs to be revisited since it seems
     * currently there is no way of retrieving all the styles declared for a
     * given Coverage.
     *
     * @return the default Style for the Coverage
     */
    public Style getDefaultStyle() {
        StyleInfo style = layer.getDefaultStyle();
        try {
            return style != null ? style.getStyle() : null;
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
        //return data.getStyle(defaultStyle);
    }

    public ArrayList getStyles() {
        final ArrayList realStyles = new ArrayList();
        for ( StyleInfo si : layer.getStyles() ) {
            try {
                realStyles.add( si.getStyle() );
            } catch (IOException e) {
                throw new RuntimeException( e );
            }
        }

        return realStyles;
        //final ArrayList realStyles = new ArrayList();
        //Iterator s_IT = styles.iterator();
        //
        //while (s_IT.hasNext())
        //    realStyles.add(data.getStyle((String) s_IT.next()));
        //
        //return realStyles;
    }

    /**
     *
     */
    public CoordinateReferenceSystem getCrs() {
        try {
            return coverage.getCRS();
        } 
        catch (Exception e) {
            throw new RuntimeException( e );
        }
        //return crs;
    }

    /**
     *
     */
    public GridGeometry getGrid() {
        return coverage.getGrid();
        //return grid;
    }

    public String getSrsWKT() {
        try {
            return coverage.getCRS().toWKT();
        } 
        catch (Exception e) {
            throw new RuntimeException( e );
        }
        //return srsWKT;
    }

    public GeneralEnvelope getWGS84LonLatEnvelope() {
        return new GeneralEnvelope( coverage.getLatLonBoundingBox() );
        
        //
        //if (this.lonLatWGS84Envelope == null) {
        //    try {
        //        this.lonLatWGS84Envelope = CoverageStoreUtils.getWGS84LonLatEnvelope(this.envelope);
        //    } catch (IndexOutOfBoundsException e) {
        //        return null;
        //    } catch (FactoryException e) {
        //        return null;
        //    } catch (TransformException e) {
        //        return null;
        //    }
        //}
        //
        //return this.lonLatWGS84Envelope;
    }

    public String getWmsPath() {
        return layer.getPath();
        //return wmsPath;
    }

    public void setWmsPath(String wmsPath) {
        layer.setPath( wmsPath );
        //this.wmsPath = wmsPath;
    }

    public CoverageAccess getCoverageAccess() {
        try {
            return coverage.getCoverageAccess(null, null);
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }

    public CoverageAccess createCoverageAccess(Hints hints) {
        try {
            return coverage.getCoverageAccess(null,hints);
        } 
        catch (IOException e) {
            throw new RuntimeException( e );
        }
    }

    public Map getParameters() {
        return coverage.getParameters();
    }
}
