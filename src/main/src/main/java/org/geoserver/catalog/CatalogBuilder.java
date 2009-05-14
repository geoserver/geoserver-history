/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.measure.unit.Unit;

import org.geoserver.catalog.impl.ModificationProxy;
import org.geoserver.catalog.impl.ResourceInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geoserver.data.util.CoverageUtils;
import org.geoserver.ows.util.ClassProperties;
import org.geoserver.ows.util.OwsUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Builder class which provides convenience methods for interacting with the catalog.
 * <p>
 * Warning: this class is stateful, and is not meant to be accessed by multiple threads
 * and should not be an member variable of another class.
 * </p>
 * @author Justin Deoliveira, OpenGEO
 *
 */
public class CatalogBuilder {

    /**
     * the catalog
     */
    Catalog catalog;
    
    /**
     * the current workspace
     */
    WorkspaceInfo workspace;
    /**
     * the current store
     */
    StoreInfo store;
    
    public CatalogBuilder( Catalog catalog ) {
        this.catalog = catalog;
    }
    
    /**
     * Sets the workspace to be used when creating store objects.
     */
    public void setWorkspace(WorkspaceInfo workspace) {
        this.workspace = workspace;
    }

    /**
     * Sets the store to be used when creating resource objects.
     */
    public void setStore( StoreInfo store ) {
        this.store = store;
    }
    
    /**
     * Updates a workspace with the properties of another.
     * 
     * @param original The workspace being updated.
     * @param update The workspace containing the new values.
     */
    public void updateWorkspace( WorkspaceInfo original, WorkspaceInfo update ) {
        update(original,update,WorkspaceInfo.class);
    }
    

    /**
     * Updates a namespace with the properties of another.
     * 
     * @param original The namespace being updated.
     * @param update The namespace containing the new values.
     */
    public void updateNamespace( NamespaceInfo original, NamespaceInfo update ) {
        update(original,update,NamespaceInfo.class);
    }
    
    /**
     * Updates a datastore with the properties of another.
     * 
     * @param original The datastore being updated.
     * @param update The datastore containing the new values.
     */
    public void updateDataStore( DataStoreInfo original, DataStoreInfo update ) {
        update( original, update, DataStoreInfo.class );
    }
    
    /**
     * Updates a coveragestore with the properties of another.
     * 
     * @param original The coveragestore being updated.
     * @param update The coveragestore containing the new values.
     */
    public void updateCoverageStore( CoverageStoreInfo original, CoverageStoreInfo update ) {
        update( original, update, CoverageStoreInfo.class );
    }
    
    /**
     * Updates a feature type with the properties of another.
     * 
     * @param original The feature type being updated.
     * @param update The feature type containing the new values.
     */
    public void updateFeatureType( FeatureTypeInfo original, FeatureTypeInfo update ) {
        update( original, update, FeatureTypeInfo.class );
    }
    
    /**
     * Updates a coverage with the properties of another.
     * 
     * @param original The coverage being updated.
     * @param update The coverage containing the new values.
     */
    public void updateCoverage( CoverageInfo original, CoverageInfo update ) {
        update( original, update, CoverageInfo.class );
    }
    
    /**
     * Updates a layer with the properties of another.
     * 
     * @param original The layer being updated.
     * @param update The layer containing the new values.
     */
    public void updateLayer( LayerInfo original, LayerInfo update ) {
        update( original, update, LayerInfo.class );
    }
    
    /**
     * Updates a layer group with the properties of another.
     * 
     * @param original The layer group being updated.
     * @param update The layer group containing the new values.
     */
    public void updateLayerGroup( LayerGroupInfo original, LayerGroupInfo update ) {
        update( original, update, LayerGroupInfo.class );
    }
    
    /**
     * Updates a style with the properties of another.
     * 
     * @param original The style being updated.
     * @param update The style containing the new values.
     */
    public void updateStyle( StyleInfo original, StyleInfo update ) {
        update( original, update, StyleInfo.class );
    }
    
    /**
     * Update method which uses reflection to grab property values from one 
     * object and set them on another.
     * <p>
     * Null values from the <tt>update</tt> object are ignored.
     * </p>
     */
    <T> void update( T original, T update, Class<T> clazz ) {
        ClassProperties properties = OwsUtils.getClassProperties( clazz );
        for ( String p : properties.properties() ) {
            Method getter = properties.getter( p, null );
            if ( getter == null ) {
                continue; // should not really happen
            }
            
            Class type = getter.getReturnType();
            Method setter = properties.setter( p, type );
            
            //do a check for read only before calling the getter to avoid an uneccesary call
            if ( setter == null && 
                    !(Collection.class.isAssignableFrom( type ) || Map.class.isAssignableFrom( type ))) {
                //read only
                continue;
            }
            
            try {
                Object newValue = getter.invoke( update, null );
                if( newValue == null ) {
                    continue;
                    //TODO: make this a flag whether to overwrite with null values
                }
                if ( setter == null ){
                    if ( Collection.class.isAssignableFrom( type ) ) {
                        updateCollectionProperty( original, (Collection) newValue, getter );
                    }
                    else if ( Map.class.isAssignableFrom( type ) ) {
                        updateMapProperty( original, (Map) newValue, getter );
                    }
                    continue;
                }

                setter.invoke( original, newValue );
            } 
            catch( Exception e ) {
                throw new RuntimeException( e );
            }
        }
    }

    /**
     * Helper method for updating a collection based property.
     */
    void updateCollectionProperty( Object object, Collection newValue, Method getter ) throws Exception {
        Collection oldValue = (Collection) getter.invoke( object, null );
        oldValue.clear();
        oldValue.addAll( newValue );
    }

    /**
     * Helper method for updating a map based property.
     */

    void updateMapProperty( Object object, Map newValue, Method getter ) throws Exception {
        Map oldValue = (Map) getter.invoke(object, null);
        oldValue.clear();
        oldValue.putAll( newValue );
    }

    /**
     * Builds a new data store.
     */
    public DataStoreInfo buildDataStore( String name ) {
        DataStoreInfo info = catalog.getFactory().createDataStore();
        buildStore(info,name);
            
        return info;
    }
    
    /**
     * Builds a new coverage store.
     */
    public CoverageStoreInfo buildCoverageStore( String name ) {
        CoverageStoreInfo info = catalog.getFactory().createCoverageStore();
        buildStore(info,name);
            
        return info;
    }
    
    /**
     * Builds a store.
     * <p>
     * The workspace of the resulting store is {@link #workspace} if set, else the 
     * default workspace from the catalog.
     * </p>
     */
    void buildStore( StoreInfo info, String name ) {

        info.setName( name );
        info.setEnabled( true );
        
        //set workspace, falling back on default if none specified
        if ( workspace != null ) {
            info.setWorkspace( workspace );
        }
        else {
            info.setWorkspace( catalog.getDefaultWorkspace() );
        }
    }
    
    
    /**
     * Builds a {@link FeatureTypeInfo} from the current datastore and the specified type name
     * <p>
     * The resulting object is not added to the catalog, it must be done by the calling code
     * after the fact.
     * </p>
     */
    public FeatureTypeInfo buildFeatureType( Name typeName ) throws Exception {
        if ( store == null || !( store instanceof DataStoreInfo ) ) {
            throw new IllegalStateException( "Data store not set.");
        }
        
        DataStoreInfo dstore = (DataStoreInfo) store;
        return buildFeatureType(dstore.getDataStore(null).getFeatureSource(typeName));
    }
    
    /**
     * Builds a feature type from a geotools feature source.
     * <p>
     * The resulting object is not added to the catalog, it must be done by the calling code
     * after the fact.
     * </p>
     */
    public FeatureTypeInfo buildFeatureType( FeatureSource featureSource ) throws Exception {
        if ( store == null || !( store instanceof DataStoreInfo ) ) {
            throw new IllegalStateException( "Data store not set.");
        }
        
        FeatureType featureType = featureSource.getSchema();
        
        FeatureTypeInfo ftinfo = catalog.getFactory().createFeatureType();
        ftinfo.setStore( store );
        ftinfo.setEnabled(true);
        
        //naming
        ftinfo.setNativeName( featureType.getName().getLocalPart() );
        ftinfo.setName( featureType.getName().getLocalPart() );
        
        WorkspaceInfo workspace = store.getWorkspace();
        NamespaceInfo namespace = catalog.getNamespaceByPrefix( workspace.getName() );
        if ( namespace == null ) {
            namespace = catalog.getDefaultNamespace();
        }
        
        ftinfo.setNamespace( namespace );
        
        // bounds
        ReferencedEnvelope bounds = featureSource.getBounds();
        ftinfo.setNativeBoundingBox( bounds );
        
        CoordinateReferenceSystem crs = featureType.getCoordinateReferenceSystem();
        if ( crs == null ) {
            crs = bounds.getCoordinateReferenceSystem();
        }
        ftinfo.setNativeCRS(crs);
        
        // fix the native bounds if necessary, some datastores do
        // not build a proper referenced envelope
        if(bounds.getCoordinateReferenceSystem() == null && crs != null) {
            bounds = new ReferencedEnvelope(bounds, crs);
        }
        
        ReferencedEnvelope boundsLatLon = null;
        if ( crs != null ) {
            if ( !CRS.equalsIgnoreMetadata( DefaultGeographicCRS.WGS84, crs ) ) {
                //transform
                try {
                    boundsLatLon = bounds.transform( DefaultGeographicCRS.WGS84, true );
                } 
                catch( Exception e ) {
                    throw (IOException) new IOException("transform error").initCause( e );
                }
            }
            else {
                boundsLatLon = new ReferencedEnvelope( bounds );
            }
        }
        ftinfo.setLatLonBoundingBox( boundsLatLon );
        
        // srs
        if ( crs != null ) {
            try {
                ftinfo.setSRS( CRS.lookupIdentifier( crs, true ) );
            } catch (FactoryException e) {
                throw (IOException) new IOException().initCause( e );
            }
        } 
        ftinfo.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);
        
        return ftinfo;
    }
    
    /**
     * Initializes a feature type object setting any info that has not been set.
     * 
     * TODO: sync this method up with {@link #buildFeatureType(FeatureSource)}.
     */
    public void initFeatureType(FeatureTypeInfo featureType) throws Exception {
        if ( featureType.getNativeName() == null && featureType.getName() != null ) {
            featureType.setNativeName( featureType.getName() );
        }
        if ( featureType.getNativeName() != null && featureType.getName() == null ) {
            featureType.setName( featureType.getNativeName() );
        }
         
        if ( featureType.getLatLonBoundingBox() == null ) {
            if ( featureType.getNativeBoundingBox() != null ) {
                //back project into lat long
                featureType.setLatLonBoundingBox(
                    featureType.getNativeBoundingBox().transform( CRS.decode( "EPSG:4326" ), true ));
            }
        }
        else {
            if ( featureType.getNativeBoundingBox() == null ) {
                //TODO: try to forward project
            }
            
        }
        
        if ( featureType.getNativeBoundingBox() == null ) {
            //calculate it
            FeatureSource source = catalog.getResourcePool().getFeatureSource( featureType, null );
            ReferencedEnvelope bounds = source.getBounds();
            if ( bounds == null ) {
                //manually calculate it
                //TODO: optimize to only load geometric attributes
                FeatureCollection features = source.getFeatures();
                FeatureIterator fi = features.features();
                try {
                    while( fi.hasNext() ) {
                        Feature f = (Feature) fi.next();
                        if ( bounds == null ) {
                            bounds = new ReferencedEnvelope(f.getBounds());
                        }
                        else {
                            bounds.include( f.getBounds() );
                        }
                    }
                }
                finally {
                    features.close( fi );
                }
            }
            
            featureType.setNativeBoundingBox( bounds );
            featureType.setLatLonBoundingBox( bounds.transform( CRS.decode( "EPSG:4326"), true ) );
        }

        if ( featureType.getSRS() == null ) {
            FeatureType ft = catalog.getResourcePool().getFeatureType( featureType );
            CoordinateReferenceSystem crs = ft.getCoordinateReferenceSystem();
            if ( crs != null ) {
                Integer epsgCode = CRS.lookupEpsgCode( crs, true );
                if ( epsgCode != null ) {
                    featureType.setSRS( "EPSG:" + epsgCode );
                }
            }
        }
    }
    
    /**
     * Builds the default coverage contained in the current store 
     * @return
     * @throws Exception
     */
    public CoverageInfo buildCoverage() throws Exception {
        if ( store == null || !( store instanceof CoverageStoreInfo ) ) {
            throw new IllegalStateException( "Coverage store not set.");
        }
        
        CoverageStoreInfo csinfo = (CoverageStoreInfo) store;
        AbstractGridCoverage2DReader reader = (AbstractGridCoverage2DReader) catalog.getResourcePool().getGridCoverageReader(csinfo, null);
        return buildCoverage(reader);
    }
    
    /**
     * Builds a coverage from a geotools grid coverage reader.
     */
    public CoverageInfo buildCoverage( AbstractGridCoverage2DReader reader ) throws Exception {
        if ( store == null || !( store instanceof CoverageStoreInfo ) ) {
            throw new IllegalStateException( "Coverage store not set.");
        }
        
        CoverageStoreInfo csinfo = (CoverageStoreInfo) store;
        CoverageInfo cinfo = catalog.getFactory().createCoverage();
        
        cinfo.setStore( csinfo );
        cinfo.setEnabled(true);
        
        WorkspaceInfo workspace = store.getWorkspace();
        NamespaceInfo namespace = catalog.getNamespaceByPrefix( workspace.getName() );
        if ( namespace == null ) {
            namespace = catalog.getDefaultNamespace();
        }
        cinfo.setNamespace(namespace);
        
        CoordinateReferenceSystem nativeCRS = reader.getCrs();
        cinfo.setNativeCRS(nativeCRS);
        
        // mind the default projection policy, Coverages do not have a flexible
        // handling as feature types, they do reproject if the native srs is set,
        // force if missing
        if ( nativeCRS != null && !nativeCRS.getIdentifiers().isEmpty()) {
            cinfo.setSRS( nativeCRS.getIdentifiers().toArray()[0].toString() );
            cinfo.setProjectionPolicy(ProjectionPolicy.REPROJECT_TO_DECLARED);
        } 
        if(nativeCRS == null) {
            cinfo.setProjectionPolicy(ProjectionPolicy.FORCE_DECLARED);
        }
        
        
        GeneralEnvelope envelope = reader.getOriginalEnvelope();
        cinfo.setNativeBoundingBox( new ReferencedEnvelope( envelope ) );
        cinfo.setLatLonBoundingBox( new ReferencedEnvelope(CoverageStoreUtils.getWGS84LonLatEnvelope(envelope)) );
        
        GridEnvelope originalRange=reader.getOriginalGridRange();
        cinfo.setGrid(new GridGeometry2D(originalRange,reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),nativeCRS));

        ///////////////////////////////////////////////////////////////////////
        //
        // Now reading a fake small GridCoverage just to retrieve meta
                // information about bands:
        //
        // - calculating a new envelope which is 1/20 of the original one
        // - reading the GridCoverage subset
        //
        ///////////////////////////////////////////////////////////////////////
        Format format = csinfo.getFormat();
        final GridCoverage2D gc;

        
        final ParameterValueGroup readParams = format.getReadParameters();
        final Map parameters = CoverageUtils.getParametersKVP(readParams);
        final int minX=originalRange.getLow(0);
        final int minY=originalRange.getLow(1);
        final int width=originalRange.getSpan(0);
        final int height=originalRange.getSpan(1);
        final int maxX=minX+(width<=5?width:5);
        final int maxY=minY+(height<=5?height:5);
        
        //we have to be sure that we are working against a valid grid range.
        final GridEnvelope2D testRange= new GridEnvelope2D(minX,minY,maxX,maxY);
        
        //build the corresponding envelope
        final MathTransform gridToWorldCorner =  reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        final GeneralEnvelope testEnvelope =CRS.transform(gridToWorldCorner,new GeneralEnvelope(testRange.getBounds()));
        testEnvelope.setCoordinateReferenceSystem(nativeCRS);
        
        parameters.put(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString(),
            new GridGeometry2D(testRange, testEnvelope));

        //try to read this coverage
        gc = (GridCoverage2D) reader.read(CoverageUtils.getParameters(readParams, parameters,
                    true));
        if(gc==null){
            throw new Exception ("Unable to acquire test coverage for format:"+ format.getName());
        }
        
        cinfo.getDimensions().addAll( getCoverageDimensions(gc.getSampleDimensions()));
            
        //TODO: 
        //dimentionNames = getDimensionNames(gc);
        /*
        StringBuilder cvName =null;
        int count = 0;
        while (true) {
            final StringBuilder key = new StringBuilder(gc.getName().toString());
            if (count > 0) {
                key.append("_").append(count);
            }

            Map coverages = dataConfig.getCoverages();
            Set cvKeySet = coverages.keySet();
            boolean key_exists = false;

            for (Iterator it = cvKeySet.iterator(); it.hasNext();) {
                String cvKey = ((String) it.next()).toLowerCase();
                if (cvKey.endsWith(key.toString().toLowerCase())) {
                    key_exists = true;
                }
            }

            if (!key_exists) {
                cvName = key;
                break;
            } else {
                count++;
            }
        }

        String name = cvName.toString();
        */
        String name = gc.getName().toString();
        cinfo.setName( name );
        cinfo.setTitle(new StringBuffer(name).append(" is a ").append(format.getDescription()).toString());
        cinfo.setDescription(new StringBuffer("Generated from ").append(format.getName()).toString() );
        
        // metadata links
        if(format.getDocURL() != null && !"".equals(format.getDocURL().trim())) {
            MetadataLinkInfo ml = catalog.getFactory().createMetadataLink();
            ml.setContent(format.getDocURL());
            ml.setMetadataType("other");
            cinfo.getMetadataLinks().add( ml );
        }
        
        //keywords
        cinfo.getKeywords().add("WCS");
        cinfo.getKeywords().add(format.getName());
        cinfo.getKeywords().add(name);
        
        //native format name
        cinfo.setNativeFormat(format.getName());
        cinfo.getMetadata().put( "dirName", new StringBuffer(store.getName()).append("_").append(name).toString());
        
        //request SRS's
        if ((gc.getCoordinateReferenceSystem2D().getIdentifiers() != null)
                && !gc.getCoordinateReferenceSystem2D().getIdentifiers().isEmpty()) {
            cinfo.getRequestSRS().add(((Identifier) gc.getCoordinateReferenceSystem2D().getIdentifiers()
                                            .toArray()[0]).toString());
        }
        
        //response SRS's
        if ((gc.getCoordinateReferenceSystem2D().getIdentifiers() != null)
                && !gc.getCoordinateReferenceSystem2D().getIdentifiers().isEmpty()) {
            cinfo.getResponseSRS().add(((Identifier) gc.getCoordinateReferenceSystem2D().getIdentifiers()
                                             .toArray()[0]).toString());
        }
        
        //supported formats
        final List formats = CoverageStoreUtils.listDataFormats();
        for (Iterator i = formats.iterator(); i.hasNext();) {
            final Format fTmp = (Format) i.next();
            final  String fName = fTmp.getName();

            if (fName.equalsIgnoreCase("WorldImage")) {
                // TODO check if coverage can encode Format
                cinfo.getSupportedFormats().add("GIF");
                cinfo.getSupportedFormats().add("PNG");
                cinfo.getSupportedFormats().add("JPEG");
                cinfo.getSupportedFormats().add("TIFF");
            } else if (fName.toLowerCase().startsWith("geotiff")) {
                // TODO check if coverage can encode Format
                cinfo.getSupportedFormats().add("GEOTIFF");
            } else {
                // TODO check if coverage can encode Format
                cinfo.getSupportedFormats().add(fName);
            }
        }

        //interpolation methods
        cinfo.setDefaultInterpolationMethod("nearest neighbor");
        cinfo.getInterpolationMethods().add("nearest neighbor");
        cinfo.getInterpolationMethods().add("bilinear");
        cinfo.getInterpolationMethods().add("bicubic");
        
        //read parameters
        cinfo.getParameters().putAll( CoverageUtils.getParametersKVP(format.getReadParameters()) );
        
        return cinfo;
    }

    List<CoverageDimensionInfo> getCoverageDimensions(GridSampleDimension[] sampleDimensions) {
    
        final int length = sampleDimensions.length;
        List<CoverageDimensionInfo> dims = new ArrayList<CoverageDimensionInfo>();
        
        for (int i = 0; i < length; i++) {
            CoverageDimensionInfo dim = catalog.getFactory().createCoverageDimension();
            dim.setName(sampleDimensions[i].getDescription().toString(Locale.getDefault()));

            StringBuffer label = new StringBuffer("GridSampleDimension".intern());
            final Unit uom = sampleDimensions[i].getUnits();

            if (uom != null) {
                label.append("(".intern());
                parseUOM(label, uom);
                label.append(")".intern());
            }

            label.append("[".intern());
            label.append(sampleDimensions[i].getMinimumValue());
            label.append(",".intern());
            label.append(sampleDimensions[i].getMaximumValue());
            label.append("]".intern());
            
            dim.setDescription(label.toString());
            dim.setRange(sampleDimensions[i].getRange());

            final List<Category> categories = sampleDimensions[i].getCategories();
            if(categories!=null) {
                for (Category cat:categories) {
    
                    if ((cat != null) && cat.getName().toString().equalsIgnoreCase("no data")) {
                        double min = cat.getRange().getMinimum();
                        double max = cat.getRange().getMaximum();
    
                        dim.getNullValues().add( min );
                        if ( min != max ) {
                            dim.getNullValues().add( max );
                        }
                    }
                }
            }
        }

        return dims;
    }
    
    void parseUOM(StringBuffer label, Unit uom) {
        String uomString = uom.toString();
        uomString = uomString.replaceAll("�", "^2");
        uomString = uomString.replaceAll("�", "^3");
        uomString = uomString.replaceAll("�", "A");
        uomString = uomString.replaceAll("�", "");
        label.append(uomString);
    }
    
    /**
     * Builds a layer for a feature type.
     * <p>
     * The resulting object is not added to the catalog, it must be done by the calling code
     * after the fact.
     * </p>
     */
    public LayerInfo buildLayer( FeatureTypeInfo featureType ) throws IOException {
        //also create a layer for the feautre type
        LayerInfo layer = buildLayer( (ResourceInfo) featureType );
        
        StyleInfo style = getDefaultStyle(featureType);
        layer.setDefaultStyle(style);
        
        return layer;
    }
    
    /**
     * Builds a layer for a coverage.
     * <p>
     * The resulting object is not added to the catalog, it must be done by the calling code
     * after the fact.
     * </p>
     */
    public LayerInfo buildLayer( CoverageInfo coverage ) throws IOException {
        LayerInfo layer = buildLayer((ResourceInfo)coverage);
        
        layer.setDefaultStyle(getDefaultStyle(coverage));
        
        return layer;
    }
    
    /**
     * Returns the default style for the specified resource
     * @param resource
     * @return
     * @throws IOException
     */
    public StyleInfo getDefaultStyle(ResourceInfo resource) throws IOException {
        // raster wise, only one style
        if(resource instanceof CoverageInfo)
            return catalog.getStyleByName(StyleInfo.DEFAULT_RASTER);
     
        // for vectors we depend on the the nature of the default geometry
        String styleName;
        FeatureTypeInfo featureType = (FeatureTypeInfo) resource;
        Class gtype = featureType.getFeatureType().getGeometryDescriptor().getType().getBinding();
        if ( Point.class.isAssignableFrom(gtype) || MultiPoint.class.isAssignableFrom(gtype)) {
            styleName = StyleInfo.DEFAULT_POINT;
        }
        else if ( LineString.class.isAssignableFrom(gtype) || MultiLineString.class.isAssignableFrom(gtype)) {
            styleName = StyleInfo.DEFAULT_LINE;
        }
        else if ( Polygon.class.isAssignableFrom(gtype) || MultiPolygon.class.isAssignableFrom(gtype)) {
            styleName = StyleInfo.DEFAULT_POLYGON;
        } else {
            //fall back to point
            styleName = StyleInfo.DEFAULT_POINT;
        }
        
        return catalog.getStyleByName( styleName );
    }
    
    LayerInfo buildLayer( ResourceInfo resource ) {
        LayerInfo layer = catalog.getFactory().createLayer();
        layer.setName( resource.getName() );
        layer.setEnabled(true);
        layer.setResource( resource );
        return layer;
    }
    
    /**
     * Calculates the bounds of a layer group specifying a particular crs.
     */
    public void calculateLayerGroupBounds( LayerGroupInfo lg, CoordinateReferenceSystem crs )
        throws Exception {
        
        if ( lg.getLayers().isEmpty() ) {
            return; 
        }
        
        LayerInfo l = lg.getLayers().get( 0 );
        ReferencedEnvelope bounds = transform( l.getResource().getLatLonBoundingBox(), crs );
        
        for ( int i = 1; i < lg.getLayers().size(); i++ ) {
            l = lg.getLayers().get( i );
            bounds.expandToInclude( transform( l.getResource().getLatLonBoundingBox(), crs ) );
        }
        lg.setBounds( bounds );
    }
    
    /**
     * Calculates the bounds of a layer group by aggregating the bounds of each layer.
     * TODO: move this method to a utility class, it should not be on a builder.
     */
    public void calculateLayerGroupBounds( LayerGroupInfo lg ) throws Exception {
        if ( lg.getLayers().isEmpty() ) {
            return; 
        }
        
        LayerInfo l = lg.getLayers().get( 0 );
        ReferencedEnvelope bounds = l.getResource().boundingBox();
        boolean latlon = false;
        if ( bounds == null ) {
            bounds = l.getResource().getLatLonBoundingBox();
            latlon = true;
        }
        
        if ( bounds == null ) {
            throw new IllegalArgumentException( "Could not calculate bounds from layer with no bounds, " + l.getName());
        }
        
        for ( int i = 1; i < lg.getLayers().size(); i++ ) {
            l = lg.getLayers().get( i );
            
            ReferencedEnvelope re;
            if ( latlon ) {
                re = l.getResource().getLatLonBoundingBox();
            }
            else {
                re = l.getResource().boundingBox();
            }
            
            re = transform( re, bounds.getCoordinateReferenceSystem() );
            if ( re == null ) {
                throw new IllegalArgumentException( "Could not calculate bounds from layer with no bounds, " + l.getName());
            }
            bounds.expandToInclude( re );
        }
       
        lg.setBounds( bounds );
    }
    
    /**
     * Helper method for transforming an envelope.
     */
    ReferencedEnvelope transform( ReferencedEnvelope e, CoordinateReferenceSystem crs ) throws TransformException, FactoryException {
        if ( !CRS.equalsIgnoreMetadata( crs, e.getCoordinateReferenceSystem() ) ) {
            return e.transform( crs, true );
        }
        return e;
    }
    
    //
    //remove methods
    //
    
    /**
     * Removes a workspace from the catalog.
     * <p>
     * The <tt>recursive</tt> flag controls whether objects linked to the workspace such as stores
     * should also be deleted.
     * </p>
     */
    public void removeWorkspace( WorkspaceInfo workspace, boolean recursive ) {
        if ( recursive ) {
            workspace.accept(new CascadeDeleteVisitor(catalog));
        } else {
            catalog.remove( workspace );
        }
    }
    
    /**
     * Removes a store from the catalog.
     * <p>
     * The <tt>recursive</tt> flag controls whether objects linked to the store such as resources
     * should also be deleted.
     * </p>
     */
    public void removeStore( StoreInfo store, boolean recursive ) {
        if ( recursive ) {
            store.accept(new CascadeDeleteVisitor(catalog));
        } else {
            catalog.remove( store );
        }
    }
    
    /**
     * Removes a resource from the catalog.
     * <p>
     * The <tt>recursive</tt> flag controls whether objects linked to the resource such as layers
     * should also be deleted.
     * </p>
     */
    public void removeResource( ResourceInfo resource, boolean recursive ) {
        if ( recursive ) {
            resource.accept(new CascadeDeleteVisitor(catalog));
        } else {
            catalog.remove( resource );
        }
    }
    
    /**
     * Reattaches a serialized {@link StoreInfo} to the catalog
     */
    public void attach(StoreInfo storeInfo) {
        storeInfo = ModificationProxy.unwrap(storeInfo);
        ((StoreInfoImpl) storeInfo).setCatalog(catalog);
    }
    
    /**
     * Reattaches a serialized {@link ResourceInfo} to the catalog 
     */
    public void attach(ResourceInfo resourceInfo) {
        resourceInfo = ModificationProxy.unwrap(resourceInfo);
        ((ResourceInfoImpl) resourceInfo).setCatalog(catalog);
    }
    
    /**
     * Reattaches a serialized {@link LayerInfo} to the catalog
     */
    public void attach(LayerInfo layerInfo) {
        attach(layerInfo.getResource());
    }
    
    /**
     * Reattaches a serialized {@link MapInfo} to the catalog 
     */
    public void attach(MapInfo mapInfo) {
        // hmmm... mapInfo has a list of layers inside? Not names?
        for (LayerInfo layer : mapInfo.getLayers()) {
            attach(layer);
        }
    }
    
    /**
     * Reattaches a serialized {@link LayerGroupInfo} to the catalog
     */
    public void attach(LayerGroupInfo groupInfo) {
        for (LayerInfo layer : groupInfo.getLayers()) {
            attach(layer);
        }
        for (StyleInfo style : groupInfo.getStyles()) {
            attach(style);
        }
    }
    
    /**
     * Reattaches a serialized {@link StyleInfo} to the catalog 
     */
    public void attach(StyleInfo styleInfo) {
        styleInfo = ModificationProxy.unwrap(styleInfo);
        ((StyleInfoImpl) styleInfo).setCatalog(catalog);
    }
    
    /**
     * Reattaches a serialized {@link NamespaceInfo} to the catalog
     */
    public void attach(NamespaceInfo nsInfo) {
        // nothing to do
    }
    
    /**
     * Reattaches a serialized {@link WorkspaceInfo} to the catalog 
     */
    public void attach(WorkspaceInfo wsInfo) {
        // nothing to do
    }
}
