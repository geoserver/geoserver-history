/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.List;

import javax.media.jai.JAI;
import javax.servlet.ServletContext;

import org.geoserver.catalog.ResourcePool;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.ConfigurationListenerAdapter;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.JAIInfo;
import org.geoserver.config.ServiceInfo;
import org.springframework.beans.factory.DisposableBean;
import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.util.Requests;

import com.sun.media.jai.util.SunTileCache;

/**
 * Complete configuration set for the whole server
 *
 * @author Gabriel Roldan
 * @author dzwiers
 * @version $Id$
 * 
 * @deprecated use {@link GeoServerInfo}.
 */
public class GeoServer extends GlobalLayerSupertype implements DisposableBean {
    /**
     * Simple listener interface.
     * 
     * JD: this is a temporary substitute until we have a decent config system.
     */
    public interface Listener {
        /**
         * Callback fired when application state has changed.
         */
        void changed();
        
    }
    
    /**
     * Wrapper around {@link Listener} which implements the new {@link ConfigurationListener} 
     * api and forwards events to the old style listener.
     *
     */
    static class ListenerWrapper extends ConfigurationListenerAdapter {

        Listener listener;
        
        ListenerWrapper( Listener listener ) {
            this.listener = listener;
        }
        
        public void handleGlobalChange(GeoServerInfo global,
                List<String> propertyNames, List<Object> oldValues,
                List<Object> newValues) {
        }

        public void handleServiceChange(ServiceInfo service,
                List<String> propertyNames, List<Object> oldValues,
                List<Object> newValues) {
        }

        public void reloaded() {
            listener.changed();
        }
        
    }
    
    /**
     * For finding the instance of this class to use from the web container
     *
     * <p>
     * ServletContext sc = ... GeoServer gs =
     * (GeoServer)sc.getAttribute(GeoServer.WEB_CONTAINER_KEY);
     * </p>
     */
    public static final String WEB_CONTAINER_KEY = "GeoServer";
    /*
    private String title;
    private int maxFeatures = Integer.MAX_VALUE;
    private boolean verbose = true;
    private int numDecimals = 4;
    private Charset charSet = Charset.forName("UTF-8");
    private final JAI jaiDef = JAI.getDefaultInstance();
    private SunTileCache jaiCache;
    private String adminUserName = "admin";
    private String adminPassword;
    private String schemaBaseUrl;
    private String proxyBaseUrl;
    private String contactPerson;
    private String contactOrganization;
    private String contactPosition;
    private String addressType;
    private String address;
    private String addressCity;
    private String addressState;
    private String addressPostalCode;
    private String addressCountry;
    private String contactVoice;
    private String contactFacsimile;
    private String contactEmail;
    private String onlineResource;
    private double memoryCapacity;
    private double memoryThreshold;
    private int tileThreads;
    private int tilePriority;
    private String tileCache;
    private Boolean recycling;
    private Boolean imageIOCache;
    private Boolean JPEGnativeAcc;
    private Boolean PNGnativeAcc;
    
    
    private boolean verboseExceptions = false;

    private String log4jConfigFile;
    private boolean suppressStdOutLogging = false;
    private String logLocation = null;

    private List listeners;
    private Config config;
    
    
    private static boolean suppressLoggingConfiguration;
    
    private int updateSequence;
    */
    
    /**
     * Default constructor only to facilitate unit testing mock ups; real
     * uses shall create an instance through {@link #GeoServer(Config)}.
     */
//    public GeoServer() {
//        //do nothing
//    }

    org.geoserver.config.GeoServer gs;
    GeoServerInfo info;
    
    /**
     * Creates a GeoServer instance and loads its configuration.
     *
     * @throws ConfigurationException
     */
//    public GeoServer(Config config) throws ConfigurationException {
//        LOGGER.fine("Creating GeoServer");
//        load(config.getGeoServer());
//        this.config = config;
//        
//        listeners = new ArrayList();    
//    }

    /**
     * do not use this
     */
    protected GeoServer() {
        
    }
    
    public GeoServer( org.geoserver.config.GeoServer gs ) {
        this.gs = gs;
        init();
    }
    
    public void init() {
        this.info = gs.getGlobal();
    }
    
    /**
     * Adds a listener to be notified of state change.
     */
    public void addListener( Listener listener ) {
        gs.addListener( new ListenerWrapper( listener ) );
    }

    /**
     * Removes a listener.
     */
    public void removeListener( Listener listener ) {
        ListenerWrapper toRemove = null;
        for( ConfigurationListener l : gs.getListeners() ) {
            if ( l instanceof ListenerWrapper ) {
                ListenerWrapper lw = (ListenerWrapper) l;
                if ( lw.listener.equals( listener ) ) {
                    toRemove = lw;
                    break;
                }
            }
        }
        
        if ( toRemove != null ) {
            gs.removeListener( toRemove );    
        }
        
    }
    
    /**
     * Notifies all listeners of a change.
     */
    public void fireChange() {
        //TODO: forward event
        //for ( Iterator l = listeners.iterator(); l.hasNext(); ) {
        //    Listener listener = (Listener) l.next();
        //    try {
        //        listener.changed();
        //    }
        //    catch( Throwable t ) {
        //        LOGGER.warning( "listener threw exception, turn logging to FINE to view stack trace" );
        //        LOGGER.log( Level.FINE, t.getLocalizedMessage(), t );
        //    }
        //}
    }
    /**
     * getAddress purpose.
     *
     * <p>
     * Returns the contact Address.
     * </p>
     *
     * @return String the contact Address.
     */
    public String getAddress() {
        return info.getContact().getAddress();
        //return notNull(address);
    }

    /**
     * getAddressCity purpose.
     *
     * <p>
     * Returns the contact City.
     * </p>
     *
     * @return String the contact City.
     */
    public String getAddressCity() {
        return info.getContact().getAddressCity();
        //return notNull(addressCity);
    }

    /**
     * getAddressCountry purpose.
     *
     * <p>
     * Returns the contact Country.
     * </p>
     *
     * @return String the contact Country.
     */
    public String getAddressCountry() {
        return info.getContact().getAddressCountry();
        //return notNull(addressCountry);
    }

    /**
     * getAddressPostalCode purpose.
     *
     * <p>
     * Returns the contact PostalCode.
     * </p>
     *
     * @return String the contact PostalCode.
     */
    public String getAddressPostalCode() {
        return info.getContact().getAddressPostalCode();
        //return notNull(addressPostalCode);
    }

    /**
     * getAddressState purpose.
     *
     * <p>
     * Returns the contact State.
     * </p>
     *
     * @return String the contact State.
     */
    public String getAddressState() {
        return info.getContact().getAddressState();
        //return notNull(addressState);
    }

    /**
     * getAddressType purpose.
     *
     * <p>
     * Returns the contact Address Type.
     * </p>
     *
     * @return String the contact Address Type.
     */
    public String getAddressType() {
        return info.getContact().getAddressType();
        //return notNull(addressType);
    }

    /**
     * Returns the default charset for this server instance.
     * <p>
     * That is, the server wide setting that indicates which character encoding scheme
     * {@link org.vfny.geoserver.global.Service} implementations shall provide to Operations in
     * order to encode XML responses in.
     * </p>
     * 
     * @return Charset the default charset for this server instance.
     */
    public Charset getCharSet() {
        return Charset.forName(info.getCharset());
        
        //if (charSet != null) {
        //    return charSet;
        //}
        //
        //return Charset.forName("UTF-8");
    }

    /**
     * getContactEmail purpose.
     *
     * <p>
     * Returns the contact Email.
     * </p>
     *
     * @return String the contact Email.
     */
    public String getContactEmail() {
        return info.getContact().getContactEmail();
        //return notNull(contactEmail);
    }

    /**
     * getContactFacsimile purpose.
     *
     * <p>
     * Returns the contact Facsimile.
     * </p>
     *
     * @return String the contact Facsimile.
     */
    public String getContactFacsimile() {
        return info.getContact().getContactFacsimile();
        //return notNull(contactFacsimile);
    }

    /**
     * getContactOrganization purpose.
     *
     * <p>
     * Returns the contact Organization.
     * </p>
     *
     * @return String the contact Organization.
     */
    public String getContactOrganization() {
        return info.getContact().getContactOrganization();
        //return notNull(contactOrganization);
    }

    /**
     * getContactPerson purpose.
     *
     * <p>
     * Returns the contact Person.
     * </p>
     *
     * @return String the contact Person.
     */
    public String getContactPerson() {
        return info.getContact().getContactPerson();
        //return notNull(contactPerson);
    }

    /**
     * getContactPosition purpose.
     *
     * <p>
     * Returns the contact Position.
     * </p>
     *
     * @return String the contact Position.
     */
    public String getContactPosition() {
        return info.getContact().getContactPosition();
        //return notNull(contactPosition);
    }

    /**
     * getContactVoice purpose.
     *
     * <p>
     * Returns the contact Phone.
     * </p>
     *
     * @return String the contact Phone.
     */
    public String getContactVoice() {
        return info.getContact().getContactVoice();
        //return notNull(contactVoice);
    }

    /**
     * getOnlineResource purpose.
     *
     * <p>
     * Returns the online Resource.
     * </p>
     *
     * @return String the online Resource.
     */
    public String getOnlineResource() {
        return info.getOnlineResource();
        //return notNull(onlineResource);
    }

    /**
    * getLoggingLevel purpose.
    *
    * <p>
    * Returns the Logging Level.
    * </p>
    *
    * @return String the Logging Level.
    */
    public String getLog4jConfigFile() {
        return gs.getLogging().getLevel();
        //return log4jConfigFile;
    }

    /**
     * getMaxFeatures purpose.
     *
     * <p>
     * Returns the max number of features supported.
     * </p>
     *
     * @return String the max number of features supported.
     */
    public int getMaxFeatures() {
        Object wfs = wfs();
        if ( wfs != null ) {
            try {
                Method g = wfs.getClass().getMethod( "getMaxFeatures", null );
                return (Integer) g.invoke( wfs, null );
            }
            catch (Exception e) {}
        }
        
        return -1;
        //return info.getMaxFeatures();
        //return maxFeatures;
    }

    void setMaxFeatures( int maxFeatures ) {
        Object wfs = wfs();
        if ( wfs != null ) {
            try {
                Method s = wfs.getClass().getMethod( "setMaxFeatures", int.class );
                s.invoke( wfs, maxFeatures );
                
                gs.save( (ServiceInfo) wfs );
            }
            catch (Exception e) {}
            
        }
    }
    
    Object wfs() {
        try {
            Class c = Class.forName( "org.geoserver.wfs.WFSInfo" );
            return gs.getService( c );
        } 
        catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    /**
     * getMimeType purpose.
     *
     * <p>
     * Returns the server default mimetype.
     * </p>
     *
     * @return String the server default mimetype.
     */
    public String getMimeType() {
        return "text/xml; charset=" + getCharSet().name();
    }

    /**
     * getNumDecimals purpose.
     *
     * <p>
     * The default number of decimals allowed in the data.
     * </p>
     *
     * @return int the default number of decimals allowed in the data.
     */
    public int getNumDecimals() {
        return info.getNumDecimals();
        //return numDecimals;
    }

    /**
     * getSchemaBaseUrl purpose.
     *
     * <p>
     * The Schema Base URL for this instance.  This should generally be a local
     * reference, as GeoServer by default puts up the schemas that it needs
     * and references them.  It could be used to specify an alternate site for
     * the schemas, however, for example if a user didn't want their servlet
     * container hit every time someone did a validation, they could instead
     * store it on another machine.  I don't really know if this is useful to
     * anyone...
     * </p>
     *
     * @return String the Schema Base URL for this instance.
     *
     * @task TODO: Right now this is broken, and I'm not quite sure if there's
     *       an elegant way to have this return the local schemas.  Perhaps we
     *       should just have it return 'local', and then the users of this
     *       method can do the local referencing themselves.  For now no one
     *       is using this  method, perhaps we should just leave it out for
     *       1.2.0, as it's very  obscure.  I think I only added it originally
     *       because I didn't want to  go through the busy work of cleaning up
     *       and figuring out how to copy over the ogc schemas.
     */
    public String getSchemaBaseUrl() {
        return info.getSchemaBaseUrl();
        //return schemaBaseUrl;
    }

    /**
     * Used when Geoserver is running behind a reverse-proxy so that url
     * in getCapabilities documents are fine
     * @return
     */
    public String getProxyBaseUrl() {
        return info.getProxyBaseUrl();
        //return proxyBaseUrl;
    }

    /**
	 * @return the updateSequence
	 */
	public int getUpdateSequence() {
	    return info.getUpdateSequence();
	    //return updateSequence;
	}

	/**
	 * @param updateSequence the updateSequence to set
	 */
	public void setUpdateSequence(int updateSequence) {
	    info.setUpdateSequence(updateSequence);
	    //this.updateSequence = updateSequence;
	}

	/**
     * whether xml documents should be pretty formatted
     *
     * @return true when verbose
     */
    public boolean isVerbose() {
        return info.isVerbose();
        //return verbose;
    }

    /**
     * <p>
     * Loads the GeoServerDTO into the current instance as a GeoServer object
     * </p>
     *
     * @param dto
     * @throws ConfigurationException
     */
    public void load(GeoServerDTO dto) throws ConfigurationException {
        if (dto != null) {
            ContactInfo contact = gs.getFactory().createContact();
            contact.setAddress( dto.getContact().getAddress() );
            contact.setAddressCity( dto.getContact().getAddressCity() );
            contact.setAddressCountry( dto.getContact().getAddressCountry() );
            contact.setAddressPostalCode( dto.getContact().getAddressPostalCode() );
            contact.setAddressState( dto.getContact().getAddressState() );
            contact.setAddressType( dto.getContact().getAddressType() );
            contact.setContactEmail( dto.getContact().getContactEmail() );
            contact.setContactFacsimile( dto.getContact().getContactFacsimile() );
            contact.setContactOrganization( dto.getContact().getContactOrganization() );
            contact.setContactPerson( dto.getContact().getContactPerson() );
            contact.setContactPosition( dto.getContact().getContactPosition() );
            contact.setContactVoice( dto.getContact().getContactVoice() );;
            contact.setOnlineResource( dto.getContact().getOnlineResource() );
            
            info.setContact( contact );
            
            //address = dto.getContact().getAddress();
            //addressCity = dto.getContact().getAddressCity();
            //addressCountry = dto.getContact().getAddressCountry();
            //addressPostalCode = dto.getContact().getAddressPostalCode();
            //addressState = dto.getContact().getAddressState();
            //addressType = dto.getContact().getAddressType();
            //charSet = dto.getCharSet();
            //contactEmail = dto.getContact().getContactEmail();
            //contactFacsimile = dto.getContact().getContactFacsimile();
            //contactOrganization = dto.getContact().getContactOrganization();
            //contactPerson = dto.getContact().getContactPerson();
            //contactPosition = dto.getContact().getContactPosition();
            //contactVoice = dto.getContact().getContactVoice();
            
            gs.getLogging().setLevel( dto.getLog4jConfigFile() );
            gs.getLogging().setStdOutLogging(!dto.getSuppressStdOutLogging());
            gs.getLogging().setLocation( dto.getLogLocation() );
            
            //log4jConfigFile = dto.getLog4jConfigFile();
            //suppressStdOutLogging = dto.getSuppressStdOutLogging();
            //logLocation = dto.getLogLocation();
            //try {
            //    if(!suppressLoggingConfiguration)
            //        configureGeoServerLogging(info);
            //        //configureGeoServerLogging(log4jConfigFile, suppressStdOutLogging, logLocation);
            //} catch (IOException ioe) {
            //    if (LOGGER.isLoggable(Level.FINE)) {
            //        LOGGER.log(Level.FINE,"",ioe);
            //    }
            //    throw new ConfigurationException("", ioe);
            //}
            
            info.getJAI().setMemoryCapacity(dto.getJaiMemoryCapacity());
            info.getJAI().setMemoryThreshold(dto.getJaiMemoryThreshold());
            info.getJAI().setTileThreads( dto.getJaiTileThreads() );
            info.getJAI().setTilePriority( dto.getJaiTilePriority() );
            info.getJAI().setRecycling( dto.getJaiRecycling() );
            info.getJAI().setImageIOCache( dto.getImageIOCache() );
            info.getJAI().setPngAcceleration( dto.getJaiPNGNative() );
            info.getJAI().setJpegAcceleration( dto.getJaiJPEGNative() );
            info.getJAI().setAllowNativeMosaic(dto.getJaiMosaicNative());
            //initJAI(jai);
            //info.getMetadata().put( JAIInfo.KEY, jai );
            
            //memoryCapacity = dto.getJaiMemoryCapacity();
            //memoryThreshold = dto.getJaiMemoryThreshold();
            //tileThreads = dto.getJaiTileThreads();
            //tilePriority = dto.getJaiTilePriority();
            //tileCache = dto.getTileCache();
            //recycling = dto.getJaiRecycling();
            //imageIOCache = dto.getImageIOCache();
            //JPEGnativeAcc = dto.getJaiJPEGNative();
            //PNGnativeAcc = dto.getJaiPNGNative();
            
            
            //initJAI(memoryCapacity, memoryThreshold, recycling, imageIOCache);

            //info.setMaxFeatures( dto.getMaxFeatures() );
            setMaxFeatures( dto.getMaxFeatures() );
            info.setNumDecimals( dto.getNumDecimals() );
            info.setSchemaBaseUrl( dto.getSchemaBaseUrl() );
            info.setProxyBaseUrl( dto.getProxyBaseUrl() );
            info.setVerbose( dto.isVerbose() );
            info.setVerboseExceptions( dto.isVerboseExceptions() );
            info.setUpdateSequence( dto.getUpdateSequence() );
            
            if ( dto.getCharSet() != null ) {
                info.setCharset( dto.getCharSet().toString() );
            }
            else {
                info.setCharset( "UTF-8");
            }
            
            gs.save( info );
            
            for ( ConfigurationListener l : gs.getListeners() ) {
                l.reloaded();
            }
            //maxFeatures = dto.getMaxFeatures();
            //numDecimals = dto.getNumDecimals();
            //onlineResource = dto.getContact().getOnlineResource();
            //schemaBaseUrl = dto.getSchemaBaseUrl();
            //proxyBaseUrl = dto.getProxyBaseUrl();
            //verbose = dto.isVerbose();
            //adminUserName = dto.getAdminUserName();
            //adminPassword = dto.getAdminPassword();
            //verboseExceptions = dto.isVerboseExceptions();
            //updateSequence = dto.getUpdateSequence();
        } else {
            throw new ConfigurationException("load(GeoServerDTO) expected a non-null value");
        }
    }

    /**
     *
     * load purpose.
     *
     * <p>
     * Loads the GeoServerDTO into the current instance as a GeoServer object.
     * As GeoServer moves to Spring, we want to move away from storing state
     * in the servlet context, so this method is deprecated.
     * </p>
     *
     * @param dto GeoServerDTO
     *
     * @throws ConfigurationException If an error occurs
     *
     * @deprecated use {@link #load(GeoServerDTO)}
     */
    public final void load(GeoServerDTO dto, ServletContext context)
        throws ConfigurationException {
        load(dto);
    }

    /**
     * Convenience method for determining the actual location on the local file
     * system of the log file based an arbirtrary path. Relative paths are
     * appended to the geoserver data directory.
     *
     * @param location The log file path, this can be an absolute or relative
     * path.
     * @param context The servlet context
     *
     * @return The file containing the absolute path to the log file.
     * @throws IOException
     */
    public static File getLogLocation(String logLocation)
        throws IOException {
        File f = new File(logLocation);

        if (f.exists()) {
            if (f.isDirectory()) {
                //attach a file to the end of the directory
                if (!logLocation.endsWith(File.separator)) {
                    logLocation += File.separator;
                }

                logLocation += "geoserver.log";
            }
        } else {
            //could be a relative path
            if (!f.isAbsolute()) {
                //append to data dir
                File data = GeoserverDataDirectory.getGeoserverDataDirectory();
                f = new File(data, f.getPath());
            }

            //make sure parent directory exists
            if ((f.getParentFile() != null) && !f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            f.createNewFile();
        }

        return f;
    }

    //public void initJAI( JAIInfo jai ) {
    ////public void initJAI(final double memCapacity, final double memoryThreshold,
    ////    final Boolean recycling, final Boolean ImageIOCache) {
    //    
    //    JAI jaiDef = JAI.getDefaultInstance();
    //    jai.setJAI( jaiDef );
    //    
    //    // setting JAI wide hints
    //    jaiDef.setRenderingHint(JAI.KEY_CACHED_TILE_RECYCLING_ENABLED, jai.getRecycling());
    //
    //    // tile factory and recycler
    //    final RecyclingTileFactory recyclingFactory = new RecyclingTileFactory();
    //    jaiDef.setRenderingHint(JAI.KEY_TILE_FACTORY, recyclingFactory);
    //    jaiDef.setRenderingHint(JAI.KEY_TILE_RECYCLER, recyclingFactory);
    //
    //    // Setting up Cache Capacity
    //    SunTileCache jaiCache = (SunTileCache) jaiDef.getTileCache();
    //    jai.setTileCache( jaiCache );
    //    
    //    long jaiMemory = (long) (jai.getMemoryCapacity() * Runtime.getRuntime().maxMemory());
    //    jaiCache.setMemoryCapacity(jaiMemory);
    //
    //    // Setting up Cahce Threshold
    //    jaiCache.setMemoryThreshold((float) jai.getMemoryThreshold());
    //
    //    jaiDef.getTileScheduler().setParallelism(jai.getTileThreads());
    //    jaiDef.getTileScheduler().setPrefetchParallelism(jai.getTileThreads());
    //    jaiDef.getTileScheduler().setPriority(jai.getTilePriority());
    //    jaiDef.getTileScheduler().setPrefetchPriority(jai.getTilePriority());
    //
    //    // ImageIO Caching
    //    ImageIO.setUseCache(jai.getImageIOCache());
    //}

    /**
     * toDTO purpose.
     *
     * <p>
     * This method is package visible only, and returns a reference to the
     * GeoServerDTO. This method is unsafe, and should only be used with
     * extreme caution.
     * </p>
     *
     * @return DTO the generated object
     */
    public GeoServerDTO toDTO() {
        GeoServerDTO dto = new GeoServerDTO();
        dto.setCharSet( getCharSet() );
        dto.setMaxFeatures( getMaxFeatures() );
        dto.setNumDecimals( getNumDecimals() );
        dto.setSchemaBaseUrl( getSchemaBaseUrl() );
        dto.setProxyBaseUrl( getProxyBaseUrl() );
        dto.setVerbose( isVerbose() );
        dto.setVerboseExceptions( isVerboseExceptions() );
        dto.setLogLocation( getLogLocation() );
        dto.setSuppressStdOutLogging( getSuppressStdOutLogging() );
        dto.setLog4jConfigFile( getLog4jConfigFile() );
        
        dto.setJaiMemoryCapacity(getMemoryCapacity());
        dto.setJaiMemoryThreshold(getMemoryThreshold());
        dto.setJaiTileThreads(getTileThreads());
        dto.setJaiTilePriority(getTilePriority());
        dto.setTileCache(getTileCache());
        dto.setJaiRecycling(getRecycling());
        dto.setImageIOCache(getImageIOCache());
        dto.setJaiJPEGNative(getJPEGNativeAcceleration());
        dto.setJaiPNGNative(getPNGNativeAcceleration());
        dto.setJaiMosaicNative(getMosaicNativeAcceleration());
        dto.setUpdateSequence(getUpdateSequence());
        
        //dto.setLog4jConfigFile(log4jConfigFile);
        //dto.setCharSet(charSet);
        //dto.setMaxFeatures(maxFeatures);
        //dto.setNumDecimals(numDecimals);
        //dto.setSchemaBaseUrl(schemaBaseUrl);
        //dto.setProxyBaseUrl(proxyBaseUrl);
        //dto.setVerbose(verbose);
        //dto.setAdminUserName(adminUserName);
        //dto.setAdminPassword(adminPassword);
        //dto.setVerboseExceptions(verboseExceptions);
        //dto.setSuppressStdOutLogging(suppressStdOutLogging);
        //dto.setLogLocation(logLocation);
        //dto.setJaiMemoryCapacity(memoryCapacity);
        //dto.setJaiMemoryThreshold(memoryThreshold);
        //dto.setJaiTileThreads(tileThreads);
        //dto.setJaiTilePriority(tilePriority);
        //dto.setTileCache(tileCache);
        //dto.setJaiRecycling(recycling);
        //dto.setImageIOCache(imageIOCache);
        //dto.setJaiJPEGNative(JPEGnativeAcc);
        //dto.setJaiPNGNative(PNGnativeAcc);
        //dto.setUpdateSequence(updateSequence);

        ContactDTO cdto = new ContactDTO();
        dto.setContact(cdto);


        cdto.setAddress(getAddress());
        cdto.setAddressCity(getAddressCity());
        cdto.setAddressCountry(getAddressCountry());
        cdto.setAddressPostalCode(getAddressPostalCode());
        cdto.setAddressState(getAddressState());
        cdto.setAddressType(getAddressType());
        cdto.setContactEmail(getContactEmail());
        cdto.setContactFacsimile(getContactFacsimile());
        cdto.setContactOrganization(getContactOrganization());
        cdto.setContactPerson(getContactPerson());
        cdto.setContactPosition(getContactPosition());
        cdto.setContactVoice(getContactVoice());
        cdto.setOnlineResource(getOnlineResource());
        
        //cdto.setAddress(address);
        //cdto.setAddressCity(addressCity);
        //cdto.setAddressCountry(addressCountry);
        //cdto.setAddressPostalCode(addressPostalCode);
        //cdto.setAddressState(addressState);
        //cdto.setAddressType(addressType);
        //cdto.setContactEmail(contactEmail);
        //cdto.setContactFacsimile(contactFacsimile);
        //cdto.setContactOrganization(contactOrganization);
        //cdto.setContactPerson(contactPerson);
        //cdto.setContactPosition(contactPosition);
        //cdto.setContactVoice(contactVoice);
        //cdto.setOnlineResource(onlineResource);
        

        return dto;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the title.
     */
    public String getTitle() {
        return info.getTitle();
        //return title;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        info.setTitle( title );
        //this.title = title;
    }

    /**
     * Property representing the contact party (person, position or
     * organization).
     *
     * <p>
     * This is a derived property.
     * </p>
     *
     * @return Contact party (person, position or organization), null if
     *         unknown
     */
    public String getContactParty() {
        if ((getContactPerson() != null) && (getContactPerson().length() != 0)) {
            return getContactPerson(); // ie Chris Holmes 
        }

        if ((getContactPosition() != null) && (getContactPosition().length() != 0)) {
            return getContactPosition(); // ie Lead Developer 
        }

        if ((getContactOrganization() != null) && (getContactOrganization().length() != 0)) {
            return getContactOrganization(); // ie TOPP 
        }

        return null;
    }

    public String getAdminUserName() {
        return info.getAdminUsername();
    }
    
    public String getAdminPassword() {
        return info.getAdminPassword();
    }

    public String toString() {
        StringBuffer geoserver = new StringBuffer("[GeoServer: \n");
        geoserver.append("   maxFeatures - " + getMaxFeatures());
        geoserver.append("\n   verbose - " + isVerbose());
        geoserver.append("\n   numDecimals - " + getNumDecimals());
        geoserver.append("\n   charSet - " + getCharSet());
        geoserver.append("\n   log4jConfigFile - " + getLog4jConfigFile());
        geoserver.append("\n   adminUserName - " + getAdminUserName());
        geoserver.append("\n   adminPassword - " + getAdminPassword());

        return geoserver.toString();
    }

    /**
     * Should we display stackTraces or not? (And give them a nice
    * little message instead?)
     *
     * @return Returns the showStackTraces.
     */
    public boolean isVerboseExceptions() {
        return info.isVerboseExceptions();
        //return verboseExceptions;
    }

    /**
     * If set to true, response exceptions will throw their stack trace
    * back to the end user.
     *
     * @param showStackTraces The showStackTraces to set.
     */
    public void setVerboseExceptions(boolean showStackTraces) {
        info.setVerboseExceptions( showStackTraces );
        //this.verboseExceptions = showStackTraces;
    }

    /**
     * Returns the location of where the server ouputs logs. Note that this may
     * not reference an actual physical location on disk.
     * Call {@link GeoServer#getLogLocation(String, ServletContext)} to map this
     * string to a file on disk.
     *
     */
    public String getLogLocation() {
        return gs.getLogging().getLocation();
        //return logLocation;
    }

    /**
     * @param logLocation The string representation of the path on disk in which
     * the server logs to.
     */
    public void setLogLocation(String logLocation) {
        gs.getLogging().setLocation( logLocation );
        //this.logLocation = logLocation;
    }

    /**
     * @return True if the server is logging to file, otherwise false.
     */
    public boolean getSuppressStdOutLogging() {
        return !gs.getLogging().isStdOutLogging();
        //return suppressStdOutLogging;
    }

    /**
     * Toggles server logging to file.
     */
    public void setSuppressStdOutLogging(boolean loggingToFile) {
        gs.getLogging().setStdOutLogging(!loggingToFile);
        //this.suppressStdOutLogging = loggingToFile;
    }

    JAIInfo jai() {
        return info.getJAI();
        //return (JAIInfo) info.getMetadata().get( JAIInfo.KEY );
    }
    
    public JAI getJAIDefault() {
        return jai().getJAI();
    }

    public SunTileCache getJaiCache() {
        return jai().getTileCache();
    }

    public double getMemoryCapacity() {
        return jai().getMemoryCapacity();
        //return memoryCapacity;
    }

    public Boolean getRecycling() {
        return jai().isRecycling();
        //return recycling;
    }

    public Boolean getJPEGNativeAcceleration() {
        return jai().isJpegAcceleration();
        //return JPEGnativeAcc;
    }

    public Boolean getPNGNativeAcceleration() {
        return jai().isPngAcceleration();
        //return PNGnativeAcc;
    }
    
    public Boolean getMosaicNativeAcceleration() {
        return jai().isAllowNativeMosaic();
    }

    public double getMemoryThreshold() {
        return jai().getMemoryThreshold();
        //return memoryThreshold;
    }

    /**
     * @return Returns the imageIOCache.
     */
    public Boolean getImageIOCache() {
        return jai().isImageIOCache();
        //return imageIOCache;
    }

    public int getTilePriority() {
        return jai().getTilePriority();
        //return tilePriority;
    }

    public int getTileThreads() {
        return jai().getTileThreads();
        //return tileThreads;
    }

    /**
     * Used when GeoServer is running beheind tile caching server.
     * <p>
     * This value should be used when writing out a url which is a getmap
     * request to reference the tile caching server and not GeoServer itself.
     * </p>
     *<p>
     * This value can be:
     * <ol>
     *  <li>A fully qualified host name + path (URL)
     *  <li>A partial path which is interpreted as relative to the host running
     *  GeoServer
     *  <li><code>null</code>
     * </ol>
     * </p>
     * <p>
     *
     * </p>
     * @see Requests#getTileCacheBaseUrl(javax.servlet.http.HttpServletRequest, GeoServer)
     */
    public String getTileCache() {
        return null;
        //return tileCache;
    }

    //public void setTileCache(String tileCache) {
    //    this.tileCache = tileCache;
    //}

    /**
     * Implements {@link DisposableBean#destroy()} to release resources being held
     * by the server at server shutdown, such as JDBC connection pools and ArcSDE
     * connection pools.
     * <p>
     * Note since 1.7.x this seems not to be needed anymore, as {@link ResourcePool}
     * takes care of disposing datastores.
     * </p>
     * @deprecated {@link ResourcePool#dispose()} takes care of releasing resources now
     */
    public void destroy() throws Exception {
        //nothing to do
    }
}
