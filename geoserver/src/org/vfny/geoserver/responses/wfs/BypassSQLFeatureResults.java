/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.responses.wfs;

import com.vividsolutions.jts.geom.Geometry;	// this shouldn't be here, hack to build FeatureType

import org.geotools.data.FeatureResults;
import org.geotools.data.Query;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.DataSourceException;

import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.JDBCFeatureSource;
import org.geotools.data.jdbc.FeatureTypeInfo;
import org.geotools.data.jdbc.JDBCFeatureReader;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.NullFIDMapper;
import org.geotools.data.jdbc.SQLBuilder;

import org.geotools.factory.FactoryConfigurationError;

import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.FeatureTypeFactory;
import org.geotools.feature.FeatureType;

import org.geotools.filter.Filter;
import org.geotools.filter.SQLEncoderException;

// import org.vfny.geoserver.global.FeatureTypeInfo;
import java.util.logging.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Description
 *
 * <p>
 * Details
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 */
public class BypassSQLFeatureResults implements FeatureResults {

    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses.wfs");
	
	protected String bypassSQL;
    protected Query query;
    protected JDBCFeatureSource featureSource;
    protected JDBCDataStore dataStore;
    protected FeatureType schema = null;
    
    ArrayList placeholder;	// is this safe - what are threading issues
    
    /**
     * FeatureResults query against featureSource.
     *
     * <p>
     * Please note that is object will not be valid after the transaction has
     * closed.
     * </p>
     *
     * @param source
     * @param query
     */
    public BypassSQLFeatureResults(
    		JDBCFeatureSource source, Query query, String bypassSQL
			) throws IOException 
	{
    	featureSource = source;
    	schema = source.getSchema();
    	
    	// todo - assignment check - this class will only work for JDBCDataStores
    	dataStore = source.getJDBCDataStore();

    	this.query = query;
        this.bypassSQL = bypassSQL;
        
	    String typeName = query.getTypeName();

	    // need to run the query in the constructor because the schema may have
	    // to determined from the resultset metadata and is required before the
	    // reader is called. Unfortunately each call for a reader will
	    // repeat the query. This class needs to manage the connection,
	    // not QueryData, which would then require resetting the result
	    // cursor.

	    // LOGGER.fine("Constructing query - evaluating schema = " + schema);
	    constructQuery();
	    
	    // if schema is null - exceulte query to build schema from result set
	    // this will happen if no schema.xml is supplied.

	    // LOGGER.fine("Constructed query - evaluating schema = " + schema);
	    
	    if (schema == null) {
		    LOGGER.fine("Building schema (GT FeatureType from result set metadata");
		    QueryData queryData = executeQuery(typeName, bypassSQL, 
		    	featureSource.getTransaction(), false);
	    }
	    
	    // debug schema
	    // LOGGER.fine("Schema.rsmd for " + typeName + " = " + schema );
    }

    /**
     * Schema (attribute description) for the result set; may have been determined
     * automatically from the result set, or been directed by dbJavaType attributes
     * in a provided schema.xml for teh feature type.
     *
     * @return org.geotools.feature.FeatureType instance
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     */
    public FeatureType getSchema() throws IOException {
    	
    	if (schema == null) 
    		throw new DataSourceException("Could not retrieve schema");

    	//return queryData.getFeatureTypeInfo().getSchema();
    	return schema;
    }

    
    /**
     * Returns transaction from featureSource (if it is a FeatureStore), or
     * Transaction.AUTO_COMMIT if it is not.
     *
     * @return Transacstion this FeatureResults opperates against
     */
    protected Transaction getTransaction() {
    	
        return Transaction.AUTO_COMMIT;
/*    	
        if (featureSource instanceof FeatureStore) {
            FeatureStore featureStore = (FeatureStore) featureSource;

            return featureStore.getTransaction();
        } else {
            return Transaction.AUTO_COMMIT;
        }
*/
        
    }

    /**
     * Retrieve a FeatureReader for this Query
     *
     * @return FeatureReader for this Query
     *
     * @throws IOException If results could not be obtained
     */
    public FeatureReader reader() throws IOException {
   	
	    // String typeName = query.getTypeName();
    
	    QueryData queryData = executeQuery(query.getTypeName(), bypassSQL, 
	    	featureSource.getTransaction(), false);


	    // reader = createFeatureReader(schema, postFilter, queryData);
	    
	    // schema arg is not used !!!!, also use null rather than Filter.NONE to avoid filtering reader
	    
	    // protected method in JDBCDataStore - cut to the chase
	    // return dataStore.createFeatureReader(null, null, queryData);
	    
	    // this is only right if postfilter is null
	    return new JDBCFeatureReader(queryData);

    }

    private void constructQuery()
    throws IOException, DataSourceException {

    	Filter filter = query.getFilter();
    	if (filter == null || filter == Filter.NONE) {
    		return;
    	}
  	
	    /* prune placeholders from query */
    	
    	SQLPlaceholders placeholders = new SQLPlaceholders(filter);
    	placeholder = placeholders.getPlaceholderLiterals();
    	Filter prunedFilter = placeholders.getPrunedFilter();

    	/* todo : check that we have the right no. of placeholders */
    	
    	/* bolt the rest of it onto the back of the where clause */
    	
	    String typeName = query.getTypeName();
	    SQLBuilder sqlBuilder = dataStore.getSqlBuilder(typeName);
	    LOGGER.fine("sqlBuilder = " + sqlBuilder);
	    Filter preFilter = sqlBuilder.getPreQueryFilter(prunedFilter);
	    // LOGGER.fine("prefilter is " + preFilter.toString());
	    Filter postFilter = sqlBuilder.getPostQueryFilter(prunedFilter);
	    if (postFilter == null) {
	    	postFilter = Filter.NONE;
	    }
	    // LOGGER.fine("postfilter is " + postFilter.toString());
	    
	    /* bolt on any user supplied where clause - is always ANDED - before any "order by" clause or at end of complete statement..
	    */
	    StringBuffer where = new StringBuffer();
	    try {
	    	sqlBuilder.sqlWhere(where, preFilter);
	    } catch (SQLEncoderException e) {
	        throw new DataSourceException("Error building SQL where predicate for user supplied filter", e);
	    }
	    LOGGER.fine("preFilter where clause is " + where);
	    if ( where != null && ! where.toString().trim().equals("") ) {
	    	where.replace(1, 6, "AND (");
	    	int insertPoint = bypassSQL.toLowerCase().indexOf("order by");
	    	if (insertPoint > 0 )
	    	{
	    		bypassSQL = bypassSQL.substring(0,insertPoint) + where + ") " + bypassSQL.substring(insertPoint );
		}
		else
		{
	    		LOGGER.fine("appending additional filter constraint " + where);
	    		bypassSQL = bypassSQL + where+ ")";
	    	}
	    }

	    /* bolt on any stuff that is to explicitly appear after a user supplied 
	     * filter / where clause
	     */ 
	    
	}

    
    // needs to go back into jdbcdatastore ?
    
    protected QueryData executeQuery(
        String typeName,
        String sqlQuery,
        Transaction transaction,
        boolean forWrite)
        throws IOException 
	{
        LOGGER.fine("About to prepare and execute: " + sqlQuery);

        Connection conn = null;
        PreparedStatement pstatement = null;
        ResultSet rs = null;
    	org.geotools.data.jdbc.FeatureTypeInfo featureTypeInfo = null;        

        try {
        	// requires JDBCDataStore.getConnection to be made public - not a probem if locate
        	// in same package, else investigate other methods of getting a connection
        	
            conn = featureSource.getJDBCDataStore().getConnection(transaction);
            
            if (!forWrite) {
               //for postgis streaming, but I don't believe it hurts anyone.
               conn.setAutoCommit(false);
            }
            
            pstatement = conn.prepareStatement(sqlQuery);
            
            // prepare the parameters
            setPlaceholders (placeholder,conn, transaction, pstatement); 
            
            rs = pstatement.executeQuery();
            
            // build FeatureType from result set metadata
            if (schema == null) {
            	schema = buildSchema(typeName, rs, new NullFIDMapper());
            }
 
            featureTypeInfo = new org.geotools.data.jdbc.FeatureTypeInfo(
            		typeName, schema, new NullFIDMapper() ); 
            
            return new QueryData(featureTypeInfo, featureSource.getJDBCDataStore(), 
            		conn, (java.sql.Statement) pstatement, rs, transaction);
            
        } catch (SQLException e) {
            // if an error occurred we close the resources
            String msg = "Error Performing SQL query";
            LOGGER.log(Level.SEVERE, msg, e);
            JDBCUtils.close(rs);
            JDBCUtils.close(pstatement);
            JDBCUtils.close(conn, transaction, e);
            throw new DataSourceException(msg, e);
        }
    }

    
    protected void setPlaceholders (ArrayList placeholder, Connection conn,
    		Transaction transaction, PreparedStatement ps) 
    		throws DataSourceException {
		
		// PeterB - this may need to become more sophisticated - currently assumes jdbc
    	// will cast correctly using setObject, although I know this doesn't work for
    	// Oracle CLOBS - although they are very unlikely to be a filter attribute ? 

		String value;
		int i = 0;
		
		if (placeholder == null) return;	// no placeholders
		
		Iterator literals = placeholder.listIterator();
	    try {
	        while (literals.hasNext()) {
	        	value = (String) literals.next();
	        	i++;
	        	LOGGER.fine("placeholder " + i + " = " + value);
				ps.setObject(i, value);		 
	        }
	    } catch (SQLException e) {
	        // if an error occurred we close the resources
            String msg = "Error setting prepared select statement parameters";
            LOGGER.log(Level.SEVERE, msg, e);
            JDBCUtils.close(ps);
            JDBCUtils.close(conn, transaction, e);
            throw new DataSourceException(msg, e);
	    }		
	}
    

    /**
     * Builds the schema for a resultset. This has to replace the schema
     * associated with the feature type created by Geoserver/Geotools and be cached.
     * In fact, need to stop Geoserver from creating any sort of schema - 
     * the featuretype should not have to exist in the db, would also need
     * to defer the fid mapper. Also find out
     * if info.xml can nominate a FID attribute or expression? 
     * 
     * FIDmappers don't make much sense here ? so just going to plug in the
     * null FID mapper for now
     * 
     * <p>
     * adaptation of org.geotools.data.oracle.JDBCDataStore.buildSchema
     *  
     * </p>
     * @param typeName The name of the table to construct a feature type for.
     * @param mapper The name of the column holding the fid.
     *
     * @return The FeatureType for the result set.
     *
     * @throws IOException 
     * @throws DataSourceException This can occur if there is an SQL error or
     *         an error constructing the FeatureType.
     *
     * @see JDBCDataStore#buildAttributeType(ResultSet)
     */
    protected FeatureType buildSchema(String typeName, ResultSet resultSet, 
		FIDMapper mapper) throws IOException {

        List attributeTypes = new ArrayList();
        AttributeType attributeType;

        try {
            // conn = getConnection(Transaction.AUTO_COMMIT);
        	
        	ResultSetMetaData rsmd = resultSet.getMetaData();

        	for (int i=1; i <= rsmd.getColumnCount(); i++  ) {
 
                String columnName = rsmd.getColumnName(i);

                // review for relevance - depends if we want fid atts listed or not 
                if (!mapper.returnFIDColumnsAsAttributes()) {
                    boolean isPresent = false;

                    for (int j = 0; j < mapper.getColumnCount(); j++) {
                        if (columnName.equalsIgnoreCase(mapper.getColumnName(j))) {
                            isPresent = true;

                            break;
                        }
                    }

                    if (isPresent) {
                        continue;
                    }
                }

                // AttributeType attributeType = buildAttributeType(tableInfo);

                int dataType = rsmd.getColumnType(i);
                Class type = (Class) JDBCDataStore.TYPE_MAPPINGS.get(new Integer(dataType));
                
                if (type == null) {
                	// wild & exciting guess !!! I'm tired - really have to fix this up in the datastores
                	// see buildAttributeType
                	attributeType = AttributeTypeFactory.newAttributeType(columnName, Geometry.class);
                } else {
                	attributeType =  AttributeTypeFactory.newAttributeType(columnName, type);
                }

                if (attributeType != null) {
                    attributeTypes.add(attributeType);
                } else {
                    LOGGER.finest("Unknown SQL Type: " + rsmd.getColumnType(i) +
                    		" column " + columnName);
                }
         	}
        	
            AttributeType[] types = (AttributeType[]) attributeTypes.toArray(new AttributeType[0]);

            return FeatureTypeFactory.newFeatureType(types, typeName, dataStore.getNameSpaceURI());
            
        } catch (SQLException sqlException) {
            throw new DataSourceException(
                "SQL Error building FeatureType from ResultSetMetaData for " + typeName + " " + sqlException.getMessage(),
                sqlException);
        } catch (FactoryConfigurationError e) {
            throw new DataSourceException("Error creating FeatureType from ResultSetMetaData for " + typeName, e);
        } catch (SchemaException e) {
            throw new DataSourceException("Error creating FeatureType from ResultSetMetaData for " + typeName, e);
        } 
    }
    
    /**
     * Returns the bounding box of this FeatureResults
     *
     * <p>
     * This implementation will generate the correct results from reader() if
     * the provided FeatureSource does not provide an optimized result via
     * FeatureSource.getBounds( Query ).
     * </p>
     * If the feature has no geometry, then an empty envelope is returned.
     *
     * @return
     *
     * @throws IOException If bounds could not be obtained
     * @throws DataSourceException See IOException
     *
     * @see org.geotools.data.FeatureResults#getBounds()
     */
    public Envelope getBounds() throws IOException {
        
    	LOGGER.fine("computing bounds for query (executing query)");
    	
    	Envelope bounds;
/*       
        bounds = featureSource.getBounds(query);

        if (bounds != null) {
            return bounds;
        }
*/
        try {
            Feature feature;
            bounds = new Envelope();
            FeatureReader reader = reader();

            while (reader.hasNext()) {
                feature = reader.next();
                bounds.expandToInclude(feature.getBounds());
            }

            reader.close();

            return bounds;
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Could not read feature : info.xml and schema.xml probably mismatch ", e);
        }
        
    }

    /**
     * Number of Features in this query.
     *
     * <p>
     * This implementation will generate the correct results from reader() if
     * the provided FeatureSource does not provide an optimized result via
     * FeatureSource.getCount( Query ).
     * </p>
     *
     * @return
     *
     * @throws IOException If feature could not be read
     * @throws DataSourceException See IOException
     *
     * @see org.geotools.data.FeatureResults#getCount()
     */
    public int getCount() throws IOException {

    	LOGGER.fine("computing count on query (executing query)");

    	int count;
        
    	// This won't work for pass through SQL - it doesn't have the 
    	// placeholder args - pb
/*    	
        count = featureSource.getCount(query);

    	
        if (count != -1) {
            // we have an optimization!
            return count;
        }
*/
        // Okay lets count the FeatureReader
        try {
            count = 0;

            FeatureReader reader = reader();

            for (; reader.hasNext(); count++) {
                reader.next();
            }

            reader.close();

            return count;
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Could not read feature : info.xml and schema.xml probably mismatch ", e);
        }
    }

    public FeatureCollection collection() throws IOException {
        try {
            FeatureCollection collection = FeatureCollections.newCollection();
            Feature feature;
            FeatureReader reader = reader();

            while (reader.hasNext()) {
                collection.add(reader.next());
            }

            reader.close();

            return collection;
        } catch (IllegalAttributeException e) {
            throw new DataSourceException("Could not read feature : info.xml and schema.xml probably mismatch ", e);
        }
    }
}
