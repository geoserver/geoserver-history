package org.vfny.geoserver.config;

import org.geotools.data.DataSource;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataSourceFinder;
import org.geotools.data.DataSourceMetaData;
import org.vfny.geoserver.responses.WfsException;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Reads all necessary feature type information to abstract away from servlets.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: TypeInfo.java,v 1.21 2003/09/12 17:01:36 cholmesny Exp $
 */
public class TypeInfo {
    /** Class logger */
    private static Logger LOG = Logger.getLogger("org.vfny.geoserver.config");

    /** the deliminter between namespace prefixes and types.*/
    //public static final String PREFIX_DELIMITER = ":";

     /** Internal representation of user defined type information. */
    private FeatureType internalType;
    private ConfigInfo config = ConfigInfo.getInstance();

    /** The namespace prefix used internally for this featureType. */
    private String prefix;
    private String prefixFileDelimiter = config.getFilePrefixDelimiter();

    /** the string of where the schema file should be located. */
    private String pathToSchemaFile;

    private DataSource transactionDS;
    private DataSource featureDSource;

    //hold the default schema for this datasource?  Would save processing, and
    //when schemas can go to xsd and back it would be good to have here.

    /**
     * Reads the typeInfo
     *
     * @param typePath The path to the info.xml file to read.
     */
    public TypeInfo(String typePath) {
        LOG.finest("reading typeinfo for " + typePath);
        readTypeInfo(typePath);
    }

    /**
     * gets the Namespace prefix used internally for this featureType.
     *
     * @return The namespace prefix used internally.
     */
    public String getPrefix() {
        return this.prefix;
    }

    /**
     * Sets the prefix.  The prefix should generally be set during feature type
     * reading, based on the name of the folder in which it is stored, such as
     * ns01:rail, where rail is the name and ns01 is the prefix.
     *
     * @param prefix The namespace prefix used internally.
     */
    final void setPrefix(String prefix) {
        if (config.getNSUri(prefix) == null) {
            config.addPrefixNamespace(prefix);
        }

        this.prefix = prefix;
    }

    /**
     * gets the string of the path to the schema file.  This is set during
     * feature reading, the schema file should be in the same folder as the
     * feature type info, with the name schema.xml.  This function does not
     * guarantee that the schema file actually exists, it just gives the
     * location where it _should_ be located.
     *
     * @return The path to the schema file.
     */
    public String getSchemaFile() {
        return pathToSchemaFile;
    }

    /**
     * gets the namespace uri of this type.  The mappings must be set right in
     * ConfigInfo for this to work right.
     *
     * @return The full namespace uri of this type.
     */
    public String getXmlns() {
        return config.getNSUri(this.prefix);
    }

    /**
     * Fetches the feature type name (also the table name)
     *
     * @return The name of this featureType.
     */
    public String getName() {
        LOG.finest("returning name " + internalType.getName());

        return internalType.getName();
    }

    /**
     * Fetches the featureType name with its proper namespace prefix.
     * This should be used for all internal references to the type.
     * @return The name with its prefix.
     */
    public String getFullName() {
        return getPrefix() + ":" + internalType.getName();
    }

    /**
     * Fetches the feature type abstract
     *
     * @return The abstract read from info.xml.
     */
    public String getAbstract() {
        return internalType.getAbstract();
    }

    /**
     * Fetches the feature type spatial reference system.
     *
     * @return The srs read from info.xml.
     *
     * @task REVISIT: check backend.
     */
    public String getSrs() {
        return internalType.getSRS();
    }

    /**
     * Fetches the user-defined feature type keywords.
     *
     * @return The keywords from info.xml
     */
    public String getKeywords() {
        StringBuffer keywords = new StringBuffer();
        Iterator keywordIter = internalType.getKeywords().iterator();

        while (keywordIter.hasNext()) {
            keywords.append(keywordIter.next().toString());

            if (keywordIter.hasNext()) {
                keywords.append(", ");
            }
        }

        return keywords.toString();
    }

    /**
     * Fetches the user-defined bounding box
     *
     * @return The bounding box from info.xml
     */
    public String getBoundingBox() {
        return internalType.getLatLonBoundingBox().toString();
    }

    /**
     * Fetches the user-defined metadata URL
     *
     * @return The metadata url from info.xml.
     *
     * @task TODO: implement this.
     */
    public String getMetadataUrl() {
        return null; //not implemented in new FeatureType internal type yet.
    }

    /**
     * Gets the params needed to initialize this featureType's data source.
     *
     * @return the DatasourceParams from info.xml
     */
    public Map getDataParams() {
        return internalType.getDataParams();
    }

    /**
     * Fetches the mandatory property names.  This should eventually move to
     * the schema - but we need to be able to read in XML schemas for that to
     * happen.  For now the info.xml file has a field that will get the
     * property names that should be  mandatory.  If they are entered wrong
     * things _will_ mess up.
     *
     * @return an array of the property names to be tested.  Unvalidated, so
     *         they will mess up all requests if the datasource does not
     *         contain the names.
     *
     * @task TODO: only return valid property names.  Also, this could take a
     *       schema as a param, and would return the required names. Though
     *       that should probably happen in the datasource...
     */
    public String[] getMandatoryProps() {
        return internalType.getMandatoryAtts();
    }

    //This is sort of bad.  It's fine if users always use locking, 
    //but if they don't then concurrent transactions are on the same 
    //connection.  The problem is that we _do_ want subtransactions
    //to be on the same connection, or else we risk deadlock (wanting
    //to operate on same feature, but waiting for the commit of the
    //previous, which does not happen till everything is done.)  Perhaps
    //a connection pool with a key, so that one transaction operation
    //can have each datasource get the same connection.  Of course, this
    //will not be a common problem, and perhaps it should just be the
    //bad behaviour that happens when locking is not used.  In any case,
    //low priority.
    public DataSource getTransactionDataSource() throws WfsException {
        if (transactionDS == null) {
            try {
                Map params = internalType.getDataParams();
                transactionDS = DataSourceFinder.getDataSource(params);

                if (transactionDS == null) {
                    throw new WfsException(
                        "could not construct a datasource for " + getFullName()
                        + " with params: " + params);
                }
            } catch (DataSourceException e) {
                throw new WfsException(e,
                    "While getting connection to datasource", getName());
            }

            LOG.finer("data source is " + transactionDS);
        }

        return transactionDS;
    }

    /**
     * Gets the datasource associated with this typeInfo.  This uses the
     * current datasource connection, so it should not be used for
     * transactions, those should generate their own datasources from their
     * connections.
     *
     * @return a newly created datasource using the connection held by this
     *         typeInfo.
     *
     * @throws WfsException if there were problems creating the datasource or
     *         the schema.
     *
     * @task REVISIT: With connection pooling it wouldn't be so bad to  just
     *       construct a new datasource each time.  Previously tons of
     *       connections would have just been left open.  Now the only
     *       limitation is the constructing of schemas, so we should wait
     *       until we cache schemas in geotools.  This needs to be thought
     *       through a bit, but once done it could be better to make the
     *       datasource each time.
     */
    public DataSource getDataSource() throws WfsException {
        if (featureDSource == null) {
            try {
                Map params = internalType.getDataParams();
                LOG.finer("params is " + params);
                featureDSource = DataSourceFinder.getDataSource(params);

                if (featureDSource == null) {
                    String mesg =
                        "Please contact the admin, the configuration for "
                        + getFullName()
                        + " has an error.  No datasource could be "
                        + "constructed with the params: " + params
                        + " from the info.xml file.";
                    throw new WfsException(mesg);
                }
            } catch (DataSourceException e) {
                throw new WfsException(e,
                    "While getting connection to datasource", getName());
            }

            LOG.finer("data source is " + transactionDS);
        }

        return featureDSource;
    }

    /**
     * Returns a capabilities XML fragment for a specific feature type.
     *
     * @param version The version of the request (0.0.14 or 0.0.15)
     *
     * @return The featureType element of the capabilities document.
     */
    public String getCapabilitiesXml(String version) {
        LOG.finest("getting capabilities " + version);

        if (version.equals("0.0.14") || version.equals("1.0.0")) {
            //1.0.0 is almost exactly like 0.0.14
            return getCapabilitiesXmlv14(version);
        } else {
            return getCapabilitiesXmlv15();
        }
    }

    /**
     * Reads feature type information
     *
     * @param typeName The query from the request object.
     */
    private void readTypeInfo(String typeName) {
        try {
            File featureTypeFile = new File(typeName);
            File parentDir = featureTypeFile.getParentFile();
            String parentDirName = parentDir.getName();
            String typePrefix = config.getDefaultNSPrefix();
            int prefixDelimPos = parentDirName.indexOf(prefixFileDelimiter);

            if (prefixDelimPos > 0) {
                typePrefix = parentDirName.substring(0, prefixDelimPos);
            }

            setPrefix(typePrefix);
            pathToSchemaFile = new File(parentDir, config.SCHEMA_FILE).toString();
            LOG.finer("pathToSchema is " + pathToSchemaFile);

            //LOG.finer("prefix is " + prefix);
            internalType = FeatureType.getInstance(typeName);
        } catch (ConfigurationException e) {
            LOG.info("Trouble reading featureType info at " + typeName + ": "
                + e.getMessage());
        }
    }

    /**
     * Generates v0.0.14 capabilities document fragment for a feature type.
     * Also used for v1.0.0, as they are practically identical, except
     * LatLonBoundingBox has a Long instead of a Lon, and it uses the prefix.
     *
     * @param version 0.0.14 or 1.0.0
     *
     * @return the FeatureType element for the capabilites document.
     */
    private String getCapabilitiesXmlv14(String version) {
        // MAKE TERSE VERSION CAPABILITY
        StringBuffer tempResponse = new StringBuffer("    <FeatureType>\n");
        String name = internalType.getName();
        String latLonName = "LatLonBoundingBox";
        boolean supportsAdd = false;
        boolean supportsModify = false;
        boolean supportsRemove = false;

        try {
            DataSourceMetaData opInfo = getTransactionDataSource().getMetaData();

            supportsAdd = opInfo.supportsAdd();
            supportsModify = opInfo.supportsModify();
            supportsRemove = opInfo.supportsRemove();
        } catch (WfsException e) {
            LOG.warning("Could not get DataSource: " + getFullName()
                + " during GetCapabilities: " + e.getMessage()
                + " returning basic Capabilities, but GetFeature requests"
                + " will likely fail.");

            //don't make error on capabilities document just because we could
            //not connect to get the operations supported, just support less.
        }

        if (!version.startsWith("0.0.1")) {
            //REVISIT: get this elsewhere?  Make sure that myns is
            //declared in the capabilities document returned.
            name = prefix + ":" + name;
            latLonName = "LatLongBoundingBox";
        }

        tempResponse.append("      <Name>" + name + "</Name>\n");
        tempResponse.append("      <Title>" + internalType.getTitle()
            + "</Title>\n");
        tempResponse.append("      <Abstract>" + getAbstract()
            + "</Abstract>\n");
        tempResponse.append("      <Keywords>" + getKeywords()
            + "</Keywords>\n");
        tempResponse.append("      <SRS>http://www.opengis.net/gml/srs/epsg#"
            + internalType.getSRS() + "</SRS>\n");

        if (supportsAdd || supportsModify || supportsRemove) {
            tempResponse.append("      <Operations>\n");

            if (supportsAdd) {
                tempResponse.append("        <Insert/>\n");
            }

            if (supportsModify) {
                tempResponse.append("        <Update/>\n");
            }

            if (supportsRemove) {
                tempResponse.append("        <Delete/>\n");
            }

            tempResponse.append("      </Operations>\n");
        }

        tempResponse.append("      <" + latLonName + " minx=\""
            + internalType.getLatLonBoundingBox().getMinx() + "\" ");
        tempResponse.append("miny=\""
            + internalType.getLatLonBoundingBox().getMiny() + "\" ");
        tempResponse.append("maxx=\""
            + internalType.getLatLonBoundingBox().getMaxx() + "\" ");
        tempResponse.append("maxy=\""
            + internalType.getLatLonBoundingBox().getMaxy() + "\"/>\n");

        //tempResponse.append("      <MetaDataURL";
        //tempResponse.append(" type=\"" + internalType.getMetadataURL().getType();
        //tempResponse.append("\" format=\"" + internalType.getMetadataURL().getFormat();
        //tempResponse.append("\">" + internalType.getMetadataURL().getUrl() + "</MetaDataURL>\n";
        tempResponse.append("    </FeatureType>\n");

        return tempResponse.toString();
    }

    /**
     * Generates v0.0.15 capabilities document fragment for a feature type.
     *
     * @return the 0.0.15 capabilities FeatureType element for this feature.
     */
    private String getCapabilitiesXmlv15() {
        // SHOULD CHANGE TO STRING BUFFER
        // ALSO MAKE TERSE VERSION CAPABILITY
        StringBuffer tempResponse = new StringBuffer(
                "        <wfsfl:FeatureType>\n");
        tempResponse.append("            <wfsfl:Name>" + internalType.getName()
            + "</wfsfl:Name>\n");
        tempResponse.append("            <wfsfl:SRS srsName=\""
            + "http://www.opengis.net/gml/srs/epsg#" + internalType.getSRS()
            + "\"/>\n");
        tempResponse.append("            <wfsfl:LatLonBoundingBox minx=\""
            + internalType.getLatLonBoundingBox().getMinx());
        tempResponse.append("\" miny=\""
            + internalType.getLatLonBoundingBox().getMiny());
        tempResponse.append("\" maxx=\""
            + internalType.getLatLonBoundingBox().getMaxx());
        tempResponse.append("\" maxy=\""
            + internalType.getLatLonBoundingBox().getMaxy() + "\"/>\n");
        tempResponse.append(
            "            <wfsfl:Operations><wfsfl:Query/></wfsfl:"
            + "Operations>\n");
        tempResponse.append("        </wfsfl:FeatureType>\n");

        return tempResponse.toString();
    }

    /**
     * closes the resources associated with this type.  For now this means
     * closing the connection that postgis uses.  This should eventually be
     * handled completely in the datasource, but for now this is they way  to
     * let the connections go. REVISIT: this now does nothing.  A close method
     * in geotools could be a good idea.  Or some sort of connection manager
     * which can shut itself down.
     */
    public void close() {
	featureDSource = null;
        transactionDS = null;
    }
}
