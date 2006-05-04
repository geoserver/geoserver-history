/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation; either version 2.1 of
// the license, or (at your option) any later version.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite
// 330, Boston, MA  02111-1307, USA.
// 
package org.vfny.geoserver.zserver;


//jzkit imports.
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.k_int.IR.AsynchronousEnumeration;
import com.k_int.IR.DefaultSourceEnumeration;
import com.k_int.IR.IFSNotificationTarget;
import com.k_int.IR.IREvent;
import com.k_int.IR.IRQuery;
import com.k_int.IR.IRStatusReport;
import com.k_int.IR.InformationFragment;
import com.k_int.IR.InformationFragmentSource;
import com.k_int.IR.RecordFormatSpecification;
import com.k_int.IR.SearchException;
import com.k_int.IR.SearchTask;
import com.k_int.IR.QueryModels.RPNTree;
import com.k_int.IR.Syntaxes.DOMTree;
import com.k_int.IR.Syntaxes.SUTRS;


/**
 * An extension of SearchTask that implements InformationFragmentSource. Thus
 * this class does handles the query, and gets the results.  To access the
 * results some form of getInformationFragmentSource must be called.
 *
 * @author Chris Holmes, TOPP
 * @author:     Ian Ibbotson (ian.ibbotson@k-int.com) Company:     KI
 * @version $Id: GeoSearchTask.java,v 1.8 2004/01/31 00:27:24 jive Exp $ based on DemoSearchTask:  Copyright:   Copyright (C) 1999-2001 Knowledge Integration Ltd.
 */
public class GeoSearchTask extends SearchTask
    implements InformationFragmentSource {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");

    /** Task status codes. */
    private static String[] private_status_types = {
        "Idle", "Searching", "Search complete", "Requesting records",
        "All records returned"
    };

    /** Default record format, a full xml. */

    //private static RecordFormatSpecification defaultSpec = 
    //new RecordFormatSpecification("xml", null, GeoProfile.FULL_SET);

    /** The status of the search. */
    public int geo_search_status = 0;

    /** The number of records */
    private int fragment_count = 0;

    /** The jzkit representation of the query to evaluate */
    private IRQuery q;

    /** The lucene structure of records returned from a search. */
    private Hits hits;

    /** The mapping of use attribute names to numbers. */
    private Properties attrMap;

    /** The properties passed into to start the server */
    private Properties serverProps;

    /** The name of the database passed in with the IRQuery */
    private String dbName;

    /**
     * Constructer to create a search task.
     *
     * @param source The class that created this task.
     * @param q the query to evaluate.
     */
    public GeoSearchTask(GeoSearchable source, IRQuery q) {
        this.q = q;

        //REVISIT: We should get rid of these Server Props, and make
        //a singleton that holds the attribute map, the databases, and
        //other configuration information.  Could be good to have it 
        //similar to geoserver's ConfigInfo, and geoserver's config could
        //initialize zserver's configuration information, instead of 
        //using these props files as we do now.  Would be good to do this
        //the same time that we get rid of GeoSearchable.
        this.serverProps = source.getServerProps();

        //String attrMapFile = serverProps.getProperty("fieldmap");
        attrMap = GeoProfile.getUseAttrMap(); //getAttrMap(attrMapFile);
    }

    /**
     * Gets the task status code.
     *
     * @return the int representation of the status code.
     */
    public int getPrivateTaskStatusCode() {
        return geo_search_status;
    }

    /**
     * Sets the task status.
     *
     * @param i the status.
     */
    public void setPrivateTaskStatusCode(int i) {
        this.geo_search_status = i;
    }

    /**
     * Converts an int of a status code to the string representation
     *
     * @param code the number of the code.
     *
     * @return the string representation of code.
     */
    public String lookupPrivateStatusCode(int code) {
        return private_status_types[code];
    }

    /**
     * Performs the evaluation.  To access the results getFragment must be
     * called.
     *
     * @param timeout not implemented, needed for interface.
     *
     * @return the status task code.
     */
    public int evaluate(int timeout) throws SearchException {
        //setTaskStatusCode(TASK_EXECUTING_SYNC);
        //REVISIT: Implement the use of timeout, possibly a timer, if
        //time is exceeded then throw a new TimeoutExceededException.
        //Not that necessary now, as searches should be pretty quick.
        Searcher searcher = null;

        try {
            Vector databases = resolveDBs(q.collections);
            dbName = q.collections.get(0).toString();

            //can only deal with one database now, so just get the first
            //path string from the collections vector.
            LOGGER.finer("using database at " + databases.get(0).toString());
            searcher = new IndexSearcher(databases.get(0).toString());

            // Analyzer analyzer = new StopAnalyzer();
            LOGGER.finer("Evaluating Query: " + q.query);

            RPNTree rpnQuery = (RPNTree) q.query;
            RPNConverter converter = new RPNConverter(attrMap);
            Query indexQuery = converter.toLuceneQuery(rpnQuery.toRPN());
            LOGGER.finer("internal query is " + indexQuery.toString("field"));
            hits = searcher.search(indexQuery);
            LOGGER.finer(hits.length() + " total matching documents");

            //Log the first ten hits for diagnostic purposes.
            final int HITS_PER_PAGE = 10;

            for (int start = 0; start < hits.length();
                    start += HITS_PER_PAGE) {
                int end = Math.min(hits.length(), start + HITS_PER_PAGE);

                for (int i = start; i < end; i++) {
                    org.apache.lucene.document.Document doc = hits.doc(i);
                    String path = doc.get("path");

                    if (path != null) {
                        LOGGER.finer(i + ". " + path);
                    }
                }

                break;
            }

            setFragmentCount(hits.length());
            setTaskStatusCode(TASK_COMPLETE);
        } catch (java.io.IOException e) {
            LOGGER.warning("Io except " + e.getMessage());
            throw new SearchException("could not complete search"
                + "...server error: " + e.getMessage());
        } finally {
            try {
                searcher.close();
            } catch (java.io.IOException e) {
                LOGGER.warning("Io except " + e.getMessage());
                throw new SearchException("could not close backend searcher"
                    + "...server error: " + e.getMessage());
            }
        }

        return (getTaskStatusCode());
    }

    /**
     * Retrieves the record at the specified index in the specified format.
     * Position based access to the result set. Implementation must be 1
     * based: IE, First record in result set is 1 not 0.
     *
     * @param index the index of the record to be returned.
     * @param spec the preferred return format.
     *
     * @return the record in the specified format.
     */
    public InformationFragment getFragment(int index,
        RecordFormatSpecification spec) {
        //If multiple dbs are implemented then change the first two arguments of
        //the return records.
        if (spec.getFormat().getName().equals("xml")) {
            return new DOMTree(dbName, "DefaultCollection", null,
                getXmlRecordForHit(index, spec), spec);
        } else { //we can use SUTRS information fragment for html and sgml

            //they all store info the same.
            return new SUTRS(dbName, "DefaultCollection", null,
                getRecordForHit(index, spec), spec);
        }
    }

    /**
     * Retrieves an array of records, from the starting fragment to  the count.
     * Position based range access to the result set. Implementation must be 1
     * based: IE, First record in result set is 1 not 0. Local mappings (e.g
     * to vector) must account for this!
     *
     * @param starting_fragment the position of the first record returned.
     * @param count the number of records to be returned.
     * @param spec the preferred return format.
     *
     * @return an array of records of size count.
     */
    public InformationFragment[] getFragment(int starting_fragment, int count,
        RecordFormatSpecification spec) {
        InformationFragment[] result = new InformationFragment[count];

        for (int i = 0; i < count; i++) {
            result[i] = getFragment(i + starting_fragment, spec);
        }

        return result;
    }

    /**
     * Sets the number of fragments (records) of this task.
     *
     * @param i the number to set.
     */
    public void setFragmentCount(int i) {
        fragment_count = i;

        IREvent e = new IREvent(IREvent.FRAGMENT_COUNT_CHANGE, new Integer(i));
        setChanged();
        notifyObservers(e);
    }

    /**
     * gets the fragment count.
     *
     * @return the number of records found by evaluate.
     */
    public int getFragmentCount() {
        return fragment_count;
    }

    /**
     * Retrieves a html, sgml or sutrs record for a hit.  All are accessed and
     * stored the same way, this method just opens and reads in the file where
     * they should be stored.  If the metadata directory does not hold the
     * requested file this simply returns null, the server should then return
     * an appropriate diagnostic.
     *
     * @param n The number of the record requested.
     * @param spec should hold the format type (html, sgml or sutrs)  and the
     *        setname (B, S, F)
     *
     * @return the string representation of the record.
     */
    private String getRecordForHit(int n, RecordFormatSpecification spec) {
        String setname = spec.getSetname().toString();
        String reqFormat = spec.getFormat().getName();
        LOGGER.finer("reqFomrat is " + reqFormat);

        String retStr = null;

        try {
            org.apache.lucene.document.Document hit = hits.doc((n - 1));

            if (setname.equals(GeoProfile.FULL_SET)) {
                String filename = hit.get(reqFormat);

                //our index stores the paths where the files should
                //be according to their name in the RecordFormatSpecification.
                RandomAccessFile file = null;

                try {
                    file = new RandomAccessFile(filename, "r");
                } catch (FileNotFoundException e) {
                    LOGGER.warning("Please make sure " + reqFormat
                        + " is stored at " + filename);

                    return null; //REVISIT: give better diagnostic, such 
                }

                int len = (int) file.length();
                byte[] retArr = new byte[len];
                file.read(retArr);
                file.close();
                retStr = (new String(retArr));
            } else if (setname.equals(GeoProfile.SUMMARY_SET)) {
                GeoSummary summary = new GeoSummary(hit, attrMap);

                if (reqFormat.equals("html")) {
                    retStr = summary.getHtmlSummary();
                } else {
                    //Isite just uses text summary for sgml.
                    retStr = summary.getTextSummary();
                }
                 //REVISIT: implement "A" setname?  

                //Would be easy with GeoSummary class.
            } else { //breif default for now. 

                //REVISIT: return diagnostic for unrecognized setname.
                retStr = (hit.get(attrMap.getProperty(
                            GeoProfile.Attribute.TITLE)));

                //Isite ref. implementation of z39.50 server 
                //just returns the title string.
                LOGGER.finer("return breif: " + retStr);
            }
        } catch (IOException e) {
            LOGGER.warning("File problem getting " + reqFormat + " record "
                + e.getMessage());
        }

        return retStr;
    }

    /**
     * Retrieves the xml record.  If a full record is requested then it just
     * parses the stored file.  If a breif or summary then GeoSummary is  used
     * to construct it.
     *
     * @param n The number of the record requested.
     * @param spec should hold the setname (B, S, F).  Xml is assumed for the
     *        format type.
     *
     * @return the xml document
     */
    private Document getXmlRecordForHit(int n, RecordFormatSpecification spec) {
        String setname = spec.getSetname().toString();
        Document retval = null;
        String filename = null;

        try {
            org.apache.lucene.document.Document hit = hits.doc((n - 1));
            filename = hit.get("path");

            FileInputStream fis = new FileInputStream(filename);

            if (setname.equals(GeoProfile.FULL_SET)) {
                InputSource in = new InputSource(fis);
                DocumentBuilderFactory dfactory = DocumentBuilderFactory
                    .newInstance();
                dfactory.setNamespaceAware(true);
                retval = dfactory.newDocumentBuilder().parse(in);
                fis.close();
            } else if (setname.equals(GeoProfile.SUMMARY_SET)) {
                GeoSummary summary = new GeoSummary(hit, attrMap);
                retval = summary.getXmlSummary();
            } else { //breif default for now.  

                //REVISIT: return diagnostic for unrecognized setname.
                GeoSummary sum = new GeoSummary();
                sum.setTitle(hit.get(attrMap.getProperty(
                            GeoProfile.Attribute.TITLE)));
                retval = sum.getXmlSummary();
            }
        } catch (java.io.IOException e) {
            LOGGER.warning("IOException when reading hits in GeoSearchTask: "
                + e);
        } catch (javax.xml.parsers.ParserConfigurationException pce) {
            LOGGER.warning("XML parser not properly configured, "
                + "could not get xml from file" + pce);
        } catch (org.xml.sax.SAXException se) {
            LOGGER.warning("problems parsing xml file at " + filename + " :"
                + se);
        }

        return retval;
    }

    /**
     * Turns a vector of database names into the paths to their lucene dbs.
     * Just queries the props file for the database, thus always searching the
     * default db, it doesn't matter what the user passes in.
     *
     * @param dbs The databases to be searched, from the search request.
     *
     * @return The paths to the databases passed in.
     */
    private Vector resolveDBs(Vector dbs) {
        /*Implementation notes:
         * Can only handle one database for now.  To add support for
         * multiple databases, set up the props file to store multiple
         * databases and change this method to read them and store the
         * paths in the return vector.  Then modify the evaluate method
         * of GeoSearchTask so that it searches multiple dbs instead
         * of just the first one. */
        Vector paths = new Vector();
        paths.add(serverProps.getProperty("database"));

        return paths;
    }

    public InformationFragmentSource getTaskResultSet() {
        return this;
    }

    public IRQuery getQuery() {
        return q;
    }

    /**
     * Release all resources and shut down the object
     */
    public void destroy() {
    }

    /**
     * From SearchTask abstract base class
     */
    public void destroyTask() {
    }

    /**
     * Enumerate all the items availabe from this fragment source
     */
    public AsynchronousEnumeration elements() {
        return new DefaultSourceEnumeration(this);
    }

    //needed to fully implement InformationFragmentSource interface.
    public IRStatusReport getStatusReport() {
        return null;
    }


    /**
     * Needed to conform to SearchTask interface.
     */
    public void asyncGetFragment(int starting_fragment, int count,
        RecordFormatSpecification spec, IFSNotificationTarget target) {
    }
}
