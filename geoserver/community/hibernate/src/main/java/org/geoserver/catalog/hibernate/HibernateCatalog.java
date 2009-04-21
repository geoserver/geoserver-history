package org.geoserver.catalog.hibernate;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MultiHashMap;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.GeophysicParamInfo;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.MapInfo;
import org.geoserver.catalog.ModelInfo;
import org.geoserver.catalog.ModelRunInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.catalog.event.impl.CatalogAddEventImpl;
import org.geoserver.catalog.event.impl.CatalogModifyEventImpl;
import org.geoserver.catalog.event.impl.CatalogRemoveEventImpl;
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.StoreInfoImpl;
import org.geoserver.data.util.CoverageStoreUtils;
import org.geoserver.hibernate.dao.GeoServerDAO;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.geotools.util.SoftValueHashMap;
import org.hibernate.Transaction;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.opengis.temporal.TemporalGeometricPrimitive;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * A {@link Catalog} implementation based on Hibernate
 * 
 * @author Justin Deoliveira (OpenGeo)
 * @author Alessio Fabianni (GeoSolutions)
 * @author Gabriel Roldan (OpenGeo)
 */
public class HibernateCatalog implements Catalog {
    /**
     * 
     */
    private SoftValueHashMap<String, CoverageInfo> coveragesCache = new SoftValueHashMap<String, CoverageInfo>();

    /**
     * 
     */
    private GeoServerDAO catalogDAO;

    /**
     * Flag indicating wether events are thrown on commit or as they happen
     */
    private boolean fireEventsOnCommit = false;

    /**
     * listeners
     */
    private List<CatalogListener> listeners = new ArrayList<CatalogListener>();

    /**
     * events
     */
    private Map<GeoServerDAO, CatalogEvent> events = Collections.synchronizedMap(new MultiHashMap());

    /**
     * resources
     */
    private ResourcePool resourcePool = new ResourcePool();

    private final HibernateCatalogFactory hibernateCatalogFactory;

    private HibernateCatalog() {
        super();
        hibernateCatalogFactory = new HibernateCatalogFactory(this);
    }

    /**
     * @see Catalog#getFactory()
     * @see HibernateCatalogFactory
     */
    public HibernateCatalogFactory getFactory() {
        return hibernateCatalogFactory;
    }

    /**
     * Sets whether to fire events on commits, intended to be used by unit tests
     * only?
     */
    void setFireEventsOnCommit(boolean fireEventsOnCommit) {
        this.fireEventsOnCommit = fireEventsOnCommit;
    }

    boolean isFireEventsOnCommit() {
        return fireEventsOnCommit;
    }

    /**
     * @see Catalog#getStore(String, Class)
     */
    public <T extends StoreInfo> T getStore(String id, Class<T> clazz) {
        T store = this.catalogDAO.getStore(id, clazz);
        if (store != null) {
            store.setCatalog(this);
        }
        return store;
    }

    /**
     * @see Catalog#getStoreByName(String, Class)
     */
    public <T extends StoreInfo> T getStoreByName(String name, Class<T> clazz) {
        T store = this.catalogDAO.getStoreByName(name, clazz);
        if (store != null) {
            store.setCatalog(this);
        }
        return store;
    }

    /**
     * @see Catalog#add(StoreInfo)
     */
    public void add(StoreInfo store) {
        if (store.getWorkspace() == null) {
            store.setWorkspace(getDefaultWorkspace());
        }

        if (store.getWorkspace() == null) {
            throw new IllegalArgumentException(
                    "no workspace set and no default available");
        }

        ((StoreInfoImpl) store).setId(store.getName());
        store.setCatalog(this);
        if (getStoreByName(store.getName(), store.getClass()) == null)
            internalAdd(store);
    }

    /**
     * @see Catalog#remove(StoreInfo)
     */
    public void remove(StoreInfo store) {
        internalRemove(store);
    }

    /**
     * @see Catalog#save(StoreInfo)
     */
    public void save(StoreInfo store) {
        internalSave(store);
    }

    /**
     * @see Catalog#getStores(Class)
     */
    public <T extends StoreInfo> List<T> getStores(Class<T> clazz) {
        List<T> stores = this.catalogDAO.getStores(clazz);
        for (StoreInfo store : stores) {
            store.setCatalog(this);
        }

        return stores;
    }

    /**
     * @see Catalog#getDataStore(String)
     */
    public DataStoreInfo getDataStore(String id) {
        return getStore(id, DataStoreInfo.class);
    }

    /**
     * @see Catalog#getDataStoreByName(String)
     */
    public DataStoreInfo getDataStoreByName(String name) {
        return getStoreByName(name, DataStoreInfo.class);
    }

    /**
     * @see Catalog#getDataStores()
     */
    public List<DataStoreInfo> getDataStores() {
        return getStores(DataStoreInfo.class);
    }

    /**
     * @see Catalog#getCoverageStore(String)
     */
    public CoverageStoreInfo getCoverageStore(String id) {
        return getStore(id, CoverageStoreInfo.class);
    }

    /**
     * @see Catalog#getCoverageStoreByName(String)
     */
    public CoverageStoreInfo getCoverageStoreByName(String name) {
        return getStoreByName(name, CoverageStoreInfo.class);
    }

    /**
     * @see Catalog#getCoverageStores()
     */
    public List<CoverageStoreInfo> getCoverageStores() {
        return getStores(CoverageStoreInfo.class);
    }

    /**
     * @see Catalog#getResourcePool()
     */
    public ResourcePool getResourcePool() {
        return resourcePool;
    }

    /**
     * @see Catalog#setResourcePool(ResourcePool)
     */
    public void setResourcePool(ResourcePool resourcePool) {
        this.resourcePool = resourcePool;
    }

    /**
     * @see Catalog#getResource(String, Class)
     */
    public <T extends ResourceInfo> T getResource(String id, Class<T> clazz) {
        T resource = this.catalogDAO.getResource(id, clazz);
        if (resource != null) {
            resource.setCatalog(this);
            resource.getStore().setCatalog(this);
            return resource;
        }
        return null;
    }

    /**
     * @see Catalog#getResourceByName(String, Class)
     */
    public <T extends ResourceInfo> T getResourceByName(String name,
            Class<T> clazz) {
        if (getDefaultNamespace() != null) {
            T resource = getResourceByName(getDefaultNamespace().getPrefix(),
                    name, clazz);
            if (resource != null) {
                resource.setCatalog(this);
                resource.getStore().setCatalog(this);
                return resource;
            }
        }

        // TODO: make a query to retrieve the matching list directly
        List<T> matches = new ArrayList<T>();
        for (Iterator<T> i = getResources(clazz).iterator(); i.hasNext();) {
            T resource = i.next();
            if (name.equals(resource.getName())) {
                matches.add(resource);
            }
        }

        if (matches.size() == 1) {
            return matches.get(0);
        }// he, this method contract is odd... imho the method shouldn't even
         // exist. Rather, let
        // client code care about asking for the resource in the default
        // namespace explicitly

        return null;
    }

    /**
     * @see Catalog#getResourceByName(String, String, Class)
     */
    public <T extends ResourceInfo> T getResourceByName(String ns, String name,
            Class<T> clazz) {
        NamespaceInfo namespace = null;
        if (ns == null) {
            namespace = getDefaultNamespace();
        } else {
            // first try prefix
            namespace = getNamespaceByPrefix(ns);
            if (namespace == null) {
                // then try uri
                namespace = getNamespaceByURI(ns);
            }
        }

        if (namespace != null) {
            T resource = this.catalogDAO.getResourceByName(namespace
                    .getId(), name, clazz);
            if (resource != null) {
                resource.setCatalog(this);
                resource.getStore().setCatalog(this);
                return resource;
            }
        } else {
            // TODO: throw exception
        }

        return null;
    }

    /**
     * @see Catalog#add(ResourceInfo)
     */
    public void add(ResourceInfo resource) {
        validate(resource);

        if (resource.getNamespace() == null) {
            // default to default namespace
            resource.setNamespace(getDefaultNamespace());
        }

        resource.setCatalog(this);
        internalAdd(resource);
    }

    /**
     * Checks whether a resource to be added is in a valid state (ie, it's
     * attached to a {@link StoreInfo})
     */
    void validate(ResourceInfo resource) {
        if (resource.getStore() == null) {
            throw new IllegalArgumentException("source must be part of a store");
        }
    }

    /**
     * @see Catalog#remove(ResourceInfo)
     */
    public void remove(ResourceInfo resource) {
        internalRemove(resource);
    }

    /**
     * @see Catalog#save(ResourceInfo)
     */
    public void save(ResourceInfo resource) {
        internalSave(resource);
    }

    /**
     * @see Catalog#getResources(Class)
     */
    public <T extends ResourceInfo> List<T> getResources(Class<T> clazz) {
        List<T> resources = this.catalogDAO.getResources(clazz);
        for (T resource : resources) {
            resource.setCatalog(this);
            resource.getStore().setCatalog(this);
        }
        return resources;
    }

    /**
     * @see Catalog#getResourcesByNamespace(NamespaceInfo, Class)
     */
    public <T extends ResourceInfo> List<T> getResourcesByNamespace(
            NamespaceInfo namespace, Class<T> clazz) {
        return this.catalogDAO.getResourcesByNamespace(namespace, clazz);
    }

    /**
     * @see Catalog#getFeatureType(String)
     */
    public FeatureTypeInfo getFeatureType(String id) {
        return getResource(id, FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypeByName(String)
     */
    public FeatureTypeInfo getFeatureTypeByName(String name) {
        return getResourceByName(name, FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypeByName(String, String)
     */
    public FeatureTypeInfo getFeatureTypeByName(String ns, String name) {
        return getResourceByName(ns, name,
                FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypes()
     */
    public List<FeatureTypeInfo> getFeatureTypes() {
        return getResources(FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getFeatureTypesByNamespace(NamespaceInfo)
     */
    public List<FeatureTypeInfo> getFeatureTypesByNamespace(
            NamespaceInfo namespace) {
        return getResourcesByNamespace(namespace, FeatureTypeInfo.class);
    }

    /**
     * @see Catalog#getCoverage(String)
     */
    public CoverageInfo getCoverage(String id) {
        CoverageInfo coverage = getResource(id,
                CoverageInfo.class);
        if (coverage != null && coverage.getFields() == null) {
            // initializing fields, vertical and temporal extent
            initCoverage(coverage);
        }
        return coverage;
    }

    /**
     * @see Catalog#getCoverageByName(String)
     */
    public CoverageInfo getCoverageByName(String name) {
        CoverageInfo coverage = getResourceByName(name, CoverageInfo.class);
        if (coverage != null && coverage.getFields() == null) {
            // initializing fields, vertical and temporal extent
            initCoverage(coverage);
        }
        return coverage;
    }

    /**
     * @see Catalog#getCoverageByName(String, String)
     */
    public CoverageInfo getCoverageByName(String ns, String name) {
        CoverageInfo coverage = getResourceByName(ns, name,
                CoverageInfo.class);
        if (coverage != null && coverage.getFields() == null) {
            // initializing fields, vertical and temporal extent
            initCoverage(coverage);
        }
        return coverage;
    }

    private void initCoverage(final CoverageInfo coverage) {
        if (coverage != null) {
            if (coveragesCache.containsKey(coverage.getId())) {
                CoverageInfo cachedCoverage = coveragesCache.get(coverage.getId());
                
                coverage.setNativeName(cachedCoverage.getNativeName());
                
                coverage.setNativeBoundingBox(cachedCoverage.getNativeBoundingBox());
                coverage.setNativeCRS(cachedCoverage.getNativeCRS());
                coverage.setLatLonBoundingBox(cachedCoverage.getLatLonBoundingBox());

                coverage.setFields(cachedCoverage.getFields());
                
                coverage.setTemporalCRS(cachedCoverage.getTemporalCRS());
                coverage.setTemporalExtent(cachedCoverage.getTemporalExtent());

                coverage.setVerticalCRS(cachedCoverage.getVerticalCRS());
                coverage.setVerticalExtent(cachedCoverage.getVerticalExtent());
            } else {
                try {
                    org.geoserver.catalog.CoverageStoreInfo coverageStore = coverage.getStore();
                    Driver driver = coverage.getStore().getDriver();
                    Map<String, Serializable> params = new HashMap<String, Serializable>();
                    params.put("url", GeoserverDataDirectory.findDataFile(coverageStore.getURL()).toURI().toURL());
                    CoverageAccess cvAccess = driver.connect(params, null, null);
                    if (cvAccess != null) {
                        CoverageSource cvSource = cvAccess.access(new NameImpl(coverage.getNativeName()), null, AccessType.READ_ONLY, null, null);
                        if (cvSource != null) {
                            coverage.setNativeBoundingBox((ReferencedEnvelope) cvSource.getHorizontalDomain(false, new NullProgressListener()).get(0));
                            coverage.setNativeCRS(coverage.getNativeBoundingBox().getCoordinateReferenceSystem());
                            coverage.setLatLonBoundingBox(new ReferencedEnvelope(CoverageStoreUtils.getWGS84LonLatEnvelope(new GeneralEnvelope(coverage.getNativeBoundingBox()))));

                            coverage.setFields(cvSource.getRangeType(null));

                            CoordinateReferenceSystem compundCRS = cvSource.getCoordinateReferenceSystem(null);
                            Set<TemporalGeometricPrimitive> temporalExtent = cvSource.getTemporalDomain(null);
                            CoordinateReferenceSystem temporalCRS = null;
                            CoordinateReferenceSystem verticalCRS = null;
                            if (temporalExtent != null && !temporalExtent.isEmpty()) {
                                if (compundCRS instanceof CompoundCRS) {
                                    temporalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
                                }
                            }
                            Set<org.opengis.geometry.Envelope> verticalExtent = cvSource.getVerticalDomain(false, null);
                            if (verticalExtent != null && !verticalExtent.isEmpty()) {
                                if (compundCRS instanceof CompoundCRS) {
                                    if (temporalCRS != null)
                                        verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(1);
                                    else
                                        verticalCRS = ((CompoundCRS) compundCRS).getCoordinateReferenceSystems().get(0);
                                }
                            }

                            coverage.setTemporalCRS(temporalCRS);
                            coverage.setTemporalExtent(temporalExtent);

                            coverage.setVerticalCRS(verticalCRS);
                            coverage.setVerticalExtent(verticalExtent);
                        }
                    }
                    
                    coveragesCache.put(coverage.getId(), coverage);
                } catch (MalformedURLException e) {
                    // e.printStackTrace();
                } catch (IOException e) {
                    // e.printStackTrace();
                } catch (MismatchedDimensionException e) {
                    // e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    // e.printStackTrace();
                } catch (FactoryException e) {
                    // e.printStackTrace();
                } catch (TransformException e) {
                    // e.printStackTrace();
                }
            }
        }
    }

    /**
     * @see Catalog#getCoverages()
     */
    public List<CoverageInfo> getCoverages() {
        List<CoverageInfo> coverages = getResources(CoverageInfo.class);
        if (coverages != null && coverages.size() > 0) {
            for (CoverageInfo coverage : coverages) {
                if (coverage.getFields() == null) {
                    // initializing fields, vertical and temporal extent
                    initCoverage(coverage);
                }
            }
        }
        return coverages;
    }

    /**
     * @see Catalog#getCoveragesByNamespace(NamespaceInfo)
     */
    public List<CoverageInfo> getCoveragesByNamespace(NamespaceInfo namespace) {
        List<CoverageInfo> coverages = getResourcesByNamespace(namespace,
                CoverageInfo.class);
        if (coverages != null && coverages.size() > 0) {
            for (CoverageInfo coverage : coverages) {
                if (coverage.getFields() == null) {
                    // initializing fields, vertical and temporal extent
                    initCoverage(coverage);
                }
            }
        }
        return coverages;
    }

    /**
     * @see Catalog#getLayer(String)
     */
    public LayerInfo getLayer(String id) {
        return this.catalogDAO.getLayer(id);
    }

    /**
     * @see Catalog#getLayerByName(String)
     */
    public LayerInfo getLayerByName(String name) {
        return this.catalogDAO.getLayerByName(name);
    }

    /**
     * @see Catalog#add(LayerInfo)
     */
    public void add(LayerInfo layer) {
        validate(layer);

        ((LayerInfoImpl) layer).setId(layer.getName());
        if (layer.getType() == null) {
            if (layer.getResource() instanceof FeatureTypeInfo) {
                layer.setType(LayerInfo.Type.VECTOR);
            } else if (layer.getResource() instanceof CoverageInfo) {
                layer.setType(LayerInfo.Type.RASTER);
            } else {
                String msg = "Layer type not set and can't be derived from resource";
                throw new IllegalArgumentException(msg);
            }
        }
        internalAdd(layer);
    }

    void validate(LayerInfo layer) {
        if (layer.getName() == null) {
            throw new NullPointerException("Layer name must not be null");
        }
        if (layer.getResource() == null) {
            throw new NullPointerException("Layer resource must not be null");
        }
        // (JD): not sure if default style should be mandatory
        // if ( layer.getDefaultStyle() == null ){
        // throw new NullPointerException(
        // "Layer default style must not be null" );
        // }
    }

    /**
     * @see Catalog#remove(LayerInfo)
     */
    public void remove(LayerInfo layer) {
        internalRemove(layer);
    }

    /**
     * @see Catalog#getMaps()
     */
    public List<MapInfo> getMaps() {
        return this.catalogDAO.getMaps();
    }

    /**
     * @see Catalog#getMap(String)
     */
    public MapInfo getMap(String id) {
        return this.catalogDAO.getMap(id);
    }

    /**
     * @see Catalog#getMapByName(String)
     */
    public MapInfo getMapByName(String name) {
        return this.catalogDAO.getMapByName(name);
    }

    /**
     * @see Catalog#add(MapInfo)
     */
    public void add(MapInfo map) {
        internalAdd(map);
    }

    /**
     * @see Catalog#remove(MapInfo)
     */
    public void remove(MapInfo map) {
        internalRemove(map);
    }

    /**
     * @see Catalog#save(MapInfo)
     */
    public void save(MapInfo map) {
        internalSave(map);
    }

    /**
     * @see Catalog#save(LayerInfo)
     */
    public void save(LayerInfo layer) {
        validate(layer);
        internalSave(layer);
    }

    /**
     * @see Catalog#getLayers()
     */
    public List<LayerInfo> getLayers() {
        return this.catalogDAO.getLayers();
    }

    /**
     * @see Catalog#getLayers(ResourceInfo)
     */
    public List<LayerInfo> getLayers(ResourceInfo resource) {
        List<LayerInfo> matches = new ArrayList<LayerInfo>();
        for (LayerInfo layer : getLayers()) {
            ResourceInfo targetResource = getResource(layer.getResource().getId(), layer.getResource().getClass());
            if (resource.equals(targetResource)) {
                layer.setResource(resource);
                layer.getDefaultStyle().setCatalog(this);
                for (StyleInfo style : layer.getStyles())
                    style.setCatalog(this);
                matches.add(layer);
            }
        }

        return matches;
    }

    /**
     * @see Catalog#setDefaultNamespace(NamespaceInfo)
     * @todo implement setDefaultNamespace
     */
    public void setDefaultNamespace(NamespaceInfo defaultNamespace) {
        HbNamespaceInfo ns = getNamespaceByPrefix(defaultNamespace.getPrefix());
        if (ns == null) {
            throw new IllegalArgumentException("No such namespace: '"
                    + defaultNamespace.getPrefix() + "'");
        }

        HbNamespaceInfo previousDefault = this.catalogDAO.getDefaultNamespace();
        if (previousDefault != null) {
            previousDefault.setDefault(false);
            this.catalogDAO.update(previousDefault);
        }
        ns.setDefault(true);
        this.catalogDAO.merge((Object) ns);
    }

    /**
     * @see Catalog#getStyle(String)
     */
    public StyleInfo getStyle(String id) {
        StyleInfo style = this.catalogDAO.getStyle(id);
        style.setCatalog(this);
        return style;
    }

    /**
     * @see Catalog#getStyleByName(String)
     */
    public StyleInfo getStyleByName(String name) {
        StyleInfo style = this.catalogDAO.getStyleByName(name);
        if (style != null)
            style.setCatalog(this);
        return style;
    }

    /**
     * @see Catalog#add(StyleInfo)
     */
    public void add(StyleInfo style) {
        internalAdd(style);
    }

    /**
     * @see Catalog#remove(StyleInfo)
     */
    public void remove(StyleInfo style) {
        internalRemove(style);
    }

    /**
     * @see Catalog#save(StyleInfo)
     */
    public void save(StyleInfo style) {
        internalSave(style);
    }

    /**
     * @see Catalog#getStyles()
     */
    public List<StyleInfo> getStyles() {
        List<StyleInfo> styles = this.catalogDAO.getStyles();
        for (StyleInfo style : styles) {
            style.setCatalog(this);
        }
        return styles;
    }

    /**
     * @see Catalog#getNamespace(String)
     */
    public NamespaceInfo getNamespace(String id) {
        return this.catalogDAO.getNamespace(id);
    }

    /**
     * @see Catalog#getNamespaceByPrefix(String)
     */
    public HbNamespaceInfo getNamespaceByPrefix(String prefix) {
        return this.catalogDAO.getNamespaceByPrefix(prefix);
    }

    /**
     * @see Catalog#getNamespaceByURI(String)
     * @todo: revisit: what prevents us from having the same URI in more than
     *        one namespace?
     */
    public NamespaceInfo getNamespaceByURI(String uri) {
        return this.catalogDAO.getNamespaceByURI(uri);
    }

    /**
     * @see Catalog#add(NamespaceInfo)
     */
    public void add(NamespaceInfo namespace) {
        if (namespace.getPrefix() == null) {
            throw new NullPointerException("namespace prefix can't be null");
        }
        HbNamespaceInfo existing = getNamespaceByPrefix(namespace.getPrefix());
        if (existing == null) {
            internalAdd(namespace);
        } else {
            ((HbNamespaceInfo) namespace).setId(existing.getId());
            ((HbNamespaceInfo) namespace).setDefault(existing.isDefault());
            this.catalogDAO.merge(namespace);
        }
    }

    /**
     * @see Catalog#remove(NamespaceInfo)
     */
    public void remove(NamespaceInfo namespace) {
        internalRemove(namespace);
    }

    /**
     * @see Catalog#save(NamespaceInfo)
     */
    public void save(NamespaceInfo namespace) {
        // takes care of updating the ns taking into account the isDefault
        // custom field
        add(namespace);
        fireModified(namespace, null, null, null);
        // internalSave(namespace);
    }

    /**
     * @see Catalog#getNamespaces()
     */
    public List<NamespaceInfo> getNamespaces() {
        return this.catalogDAO.getNamespaces();
    }

    /**
     * @see Catalog#addListener(CatalogListener)
     */
    public void addListener(CatalogListener listener) {
        listeners.add(listener);
    }

    /**
     * @see Catalog#removeListener(CatalogListener)
     */
    public void removeListener(CatalogListener listener) {
        listeners.remove(listener);
    }

    /**
     * @see Catalog#getListeners()
     * @todo revisit: we have add and remove listener, it seems like we should
     *       return a safe copy here!
     */
    public Collection<CatalogListener> getListeners() {
        return listeners;
    }

    private void internalAdd(Object object) {
        this.catalogDAO.save(object);
        fireAdded(object);
    }

    private void internalRemove(Object object) {
        this.catalogDAO.delete(object);
        fireRemoved(object);
    }

    private void internalSave(Object object) {
        this.catalogDAO.update(object);
        fireModified(object, null, null, null);
    }

    /**
     * Fires an added event with the {@code addedObject} as source
     * 
     * @param addedObject
     */
    void fireAdded(Object addedObject) {
        CatalogAddEventImpl event = new CatalogAddEventImpl();
        event.setSource(addedObject);

        fireEvent(event);
    }

    /**
     * Fires a modified event over the {@code modifiedObject}
     * 
     * @param modifiedObject
     *            the modified object to be set as the event's source
     * @param propertyNames
     *            the list of changed property names
     * @param oldValues
     *            the modified properties old values
     * @param newValues
     *            the modified properties new values
     */
    void fireModified(Object modifiedObject, List propertyNames,
            List oldValues, List newValues) {
        CatalogModifyEventImpl event = new CatalogModifyEventImpl();

        event.setSource(modifiedObject);
        event.setPropertyNames(propertyNames);
        event.setOldValues(oldValues);
        event.setNewValues(newValues);

        fireEvent(event);
    }

    /**
     * Fires a removed event
     * 
     * @param removedObject
     *            the object removed from the catalog, to be set as the event's
     *            source
     */
    void fireRemoved(Object removedObject) {
        CatalogRemoveEventImpl event = new CatalogRemoveEventImpl();
        event.setSource(removedObject);

        fireEvent(event);
    }

    private void fireEvent(CatalogEvent event) {
        if (fireEventsOnCommit) {
            // store for later
            events.put(this.catalogDAO, event);
        } else {
            // fire now
            doFireEvent(event);
        }

    }

    void fireEvents(Transaction tx) {
        if (!fireEventsOnCommit)
            return;

        Collection toFire = (Collection) events.remove(tx);
        if (toFire == null) {
            return;
        }
        for (Iterator e = toFire.iterator(); e.hasNext();) {
            CatalogEvent event = (CatalogEvent) e.next();
            doFireEvent(event);
        }
    }

    /**
     * Fires a catalog event for good
     * 
     * @see #fireEvent(CatalogEvent)
     */
    private void doFireEvent(CatalogEvent event) {
        for (Iterator<CatalogListener> l = listeners.iterator(); l.hasNext();) {
            CatalogListener listener = l.next();
            if (event instanceof CatalogAddEvent) {
                listener.handleAddEvent((CatalogAddEvent) event);
            } else if (event instanceof CatalogRemoveEvent) {
                listener.handleRemoveEvent((CatalogRemoveEvent) event);
            } else if (event instanceof CatalogModifyEvent) {
                listener.handleModifyEvent((CatalogModifyEvent) event);
            }
        }
    }

    void rollbackEvents(Transaction tx) {
        events.remove(tx);
    }

    /**
     * Diffs two objects determing which properties have changed.
     * <p>
     * <tt>o1</tt> and <tt>o2</tt> must be of the same type.
     * </p>
     * 
     * @param o1
     *            The first object.
     * @param o2
     *            The second object.
     * 
     * @return List of properties of the object that differ.
     */
    private List<String> diff(Object o1, Object o2) {

        List<String> changed = new ArrayList<String>();
        PropertyDescriptor[] properties = PropertyUtils
                .getPropertyDescriptors(o1);

        BeanComparator comparator = new BeanComparator();
        for (int i = 0; i < properties.length; i++) {
            comparator.setProperty(properties[i].getName());
            if (comparator.compare(o1, o2) != 0) {
                changed.add(properties[i].getName());
            }
        }

        return changed;

    }

    /**
     * @see Catalog#add(LayerGroupInfo)
     */
    public void add(LayerGroupInfo layerGroup) {
        ((LayerGroupInfoImpl) layerGroup).setId(layerGroup.getName());
        internalAdd(layerGroup);
    }

    /**
     * @see Catalog#add(WorkspaceInfo)
     */
    public void add(WorkspaceInfo workspace) {
        if (getWorkspaceByName(workspace.getName()) == null)
            internalAdd(workspace);
    }

    /**
     * @see Catalog#dispose()
     */
    public void dispose() {
        listeners.clear();
        events.clear();

        resourcePool.dispose();
    }

    /**
     * @see Catalog#getLayerGroup(String)
     */
    public LayerGroupInfo getLayerGroup(String id) {
        return null;
    }

    /**
     * @see Catalog#getLayerGroupByName(String)
     */
    public LayerGroupInfo getLayerGroupByName(String name) {
        return null;
    }

    /**
     * @see Catalog#getLayerGroups()
     * @todo missing mapping for LayerGroupInfo
     */
    public List<LayerGroupInfo> getLayerGroups() {
        return this.catalogDAO.getLayerGroups();
    }

    /**
     * @see Catalog#getStoresByWorkspace(WorkspaceInfo, Class)
     */
    public <T extends StoreInfo> List<T> getStoresByWorkspace(
            WorkspaceInfo workspace, Class<T> clazz) {
        return null;
    }

    /**
     * @see Catalog#getWorkspace(String)
     */
    public WorkspaceInfo getWorkspace(String id) {
        return this.catalogDAO.getWorkspace(id);
    }

    /**
     * @see Catalog#getWorkspaceByName(String)
     */
    public WorkspaceInfo getWorkspaceByName(String name) {
        return this.catalogDAO.getWorkspaceByName(name);
    }

    /**
     * @see Catalog#getWorkspaces()
     */
    public List<WorkspaceInfo> getWorkspaces() {
        return this.catalogDAO.getWorkspaces();
    }

    /**
     * @see Catalog#remove(LayerGroupInfo)
     */
    public void remove(LayerGroupInfo layerGroup) {
        internalRemove(layerGroup);
    }

    /**
     * @see Catalog#remove(WorkspaceInfo)
     */
    public void remove(WorkspaceInfo workspace) {
        internalRemove(workspace);
    }

    /**
     * @see Catalog#save(LayerGroupInfo)
     */
    public void save(LayerGroupInfo layerGroup) {
        internalSave(layerGroup);
    }

    /**
     * @see Catalog#save(WorkspaceInfo)
     */
    public void save(WorkspaceInfo workspace) {
        internalSave(workspace);
    }

    /**
     * @see Catalog#getDefaultWorkspace()
     * @todo implement getDefaultWorkspace
     */
    public HbWorkspaceInfo getDefaultWorkspace() {
        return this.catalogDAO.getDefaultWorkspace();
    }

    /**
     * @see Catalog#setDefaultWorkspace(WorkspaceInfo)
     */
    public void setDefaultWorkspace(WorkspaceInfo workspace) {
        HbWorkspaceInfo currentDefault = getDefaultWorkspace();
        if (currentDefault != null && currentDefault != workspace) {
            currentDefault.setDefault(false);
            this.catalogDAO.update(currentDefault);
        }
        ((HbWorkspaceInfo) workspace).setDefault(true);

        WorkspaceInfo current = getWorkspaceByName(workspace.getName());
        if (current == null) {
            this.catalogDAO.save(workspace);
        } else {
            ((HbWorkspaceInfo) workspace).setId(current.getId());
            this.catalogDAO.merge(workspace);
        }
    }

    /**
     * Creates the mimimum set of configuration objects, intended to be used to
     * set up a new database contents
     */
    public void bootStrap() {
        HbWorkspaceInfo defaultWs = getFactory().createWorkspace();
        defaultWs.setDefault(Boolean.TRUE);
        defaultWs.setName("topp");
        setDefaultWorkspace(defaultWs);

        NamespaceInfo nsinfo = getFactory().createNamespace();
        nsinfo.setPrefix("topp");
        nsinfo.setURI("http://www.opengeo.org");
        add(nsinfo);
        setDefaultNamespace(nsinfo);
    }

    // Model / ModelRun methods
    /**
     * 
     */
    public void add(ModelInfo model) {
        internalAdd(model);
    }

    /**
     * 
     */
    public void add(ModelRunInfo modelRun) {
        internalAdd(modelRun);
    }

    /**
     * 
     */
    public ModelInfo getModel(String id) {
        return this.catalogDAO.getModel(id);
    }

    /**
     * 
     */
    public ModelInfo getModelByName(String name) {
        return this.catalogDAO.getModelByName(name);
    }

    /**
     * 
     */
    public ModelRunInfo getModelRun(String id) {
        return this.catalogDAO.getModelRun(id);
    }

    /**
     * 
     */
    public ModelRunInfo getModelRunByName(String name) {
        return this.catalogDAO.getModelRunByName(name);
    }

    /**
     * 
     */
    public List<ModelRunInfo> getModelRuns() {
        return this.catalogDAO.getModelRuns();
    }

    /**
     * 
     */
    public List<ModelRunInfo> getModelRuns(ModelInfo model) {
        return this.catalogDAO.getModelRuns(model);
    }

    /**
     * 
     */
    public List<ModelInfo> getModels(GeophysicParamInfo param) {
        return this.catalogDAO.getModels(param);
    }

    /**
     * 
     * @param param
     * @return
     */
    public List<ModelRunInfo> getModelRuns(GeophysicParamInfo param) {
        return this.catalogDAO.getModelRuns(param);
    }

    /**
     * 
     */
    public List<ModelInfo> getModels() {
        return this.catalogDAO.getModels();
    }

    /**
     * 
     */
    public List<GeophysicParamInfo> getGeophysicParams() {
        return this.catalogDAO.getGeophysicalParameters();
    }

    /**
     * 
     */
    public List<CoverageInfo> getGridCoverages(ModelRunInfo modelRun) {
        return this.catalogDAO.getGridCoverages(modelRun);
    }

    /**
     * 
     * @param param
     * @return
     */
    public List<CoverageInfo> getGridCoverages(GeophysicParamInfo param) {
        return this.catalogDAO.getGridCoverages(param);
    }

    /**
     * 
     * @param model
     * @return
     */
    public List<GeophysicParamInfo> getGeophysicalParameters(ModelInfo model) {
        return this.catalogDAO.getGeophysicalParameters(model);
    }

    /**
     * 
     * @param modelRun
     * @return
     */
    public List<GeophysicParamInfo> getGeophysicalParameters(
            ModelRunInfo modelRun) {
        return this.catalogDAO.getGeophysicalParameters(modelRun);
    }

    /**
     * 
     */
    public List<GeophysicParamInfo> getGeophysicalParams(CoverageInfo coverage) {
        return this.catalogDAO.getGeophysicalParameters(coverage);
    }

    /**
     * 
     */
    public GeophysicParamInfo getGeophysicParamByName(String variableName) {
        return this.catalogDAO.getGeophysicParamByName(variableName);
    }

    /**
     * 
     */
    public void remove(ModelInfo model) {
        internalRemove(model);
    }

    /**
     * 
     */
    public void remove(ModelRunInfo modelRun) {
        internalRemove(modelRun);
    }

    /**
     * 
     */
    public void save(ModelInfo model) {
        internalSave(model);
    }

    /**
     * 
     */
    public void save(ModelRunInfo modelRun) {
        internalSave(modelRun);
    }

    /**
     * @return the catalogDAO
     */
    public GeoServerDAO getCatalogDAO() {
        return catalogDAO;
    }

    /**
     * @param catalogDAO
     *            the catalogDAO to set
     */
    public void setCatalogDAO(GeoServerDAO catalogDAO) {
        this.catalogDAO = catalogDAO;
    }

    /**
     * 
     */
    public NamespaceInfo getDefaultNamespace() {
        return this.catalogDAO.getDefaultNamespace();
    }

}
