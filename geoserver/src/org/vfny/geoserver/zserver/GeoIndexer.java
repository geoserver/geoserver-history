/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;


/**
 * Performs the indexing of the documents.  The Properties files passed in
 * should have the values 'database' (where the index to be written to is
 * stored) 'datafolder' (where the metadata folders are stored) and
 * 'fieldmap' (where the mapping of use attribute numbers to xpaths is).
 * This class has one public function, update, which checks if there are new
 * or modified files in the datafolder, and if so runs the indexer on them.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: GeoIndexer.java,v 1.7 2004/01/12 21:01:26 dmzwiers Exp $
 */
public class GeoIndexer {
    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");

    /** The name of the lucene path field (see XMLDocument) */
    private static final String PATH_FIELD = "path";

    /** The name of the lucene modified field (see XMLDocument) */
    private static final String MODIFIED_FIELD = "modified";

    /** The name of the xml file where metadata will be found. */
    private static final String META_NAME = "metadata.xml";

    /** The server props to query about locations of files */
    private Properties serverProps;

    /** The mappings of use attribute numbers to names */
    private Properties attrMap;

    /** The path to the lucene index. */
    private String pathToIndex;

    /** The folder where the metadata layers are stored. */
    private File dataFolder;

    /**
     * Constructor to initializes indexer.
     *
     * @param serverProps should contain datafolder,  fieldmap and database
     *        keys.
     */
    public GeoIndexer(Properties serverProps) {
        this.serverProps = serverProps;
        pathToIndex = serverProps.getProperty("database");
        dataFolder = new File(serverProps.getProperty("datafolder"));
        attrMap = GeoProfile.getUseAttrMap();
    }

    /**
     * Checks the last modified times of the files in the datafolder, and if
     * they've changed or there are new folders this adds them to the index.
     *
     * @return the number of documents run through the indexer.
     */
    public int update() {
        try {
            Set currentFiles = new HashSet();
            List docsToAdd = new ArrayList();

            if (dataFolder.isDirectory()) {
                //DirectoryFilter dirFilter = new DirectoryFilter();
                File[] dirFiles = dataFolder.listFiles();
                IndexSearcher searcher = null;
                searcher = createSearcher(pathToIndex);

                for (int i = 0; i < dirFiles.length; i++) {
                    File curDir = dirFiles[i];
                    File curFile = new File(curDir.getPath(), META_NAME);

                    if (curFile.exists()) {
                        String path = curFile.getPath();
                        long fileModified = curFile.lastModified();
                        Hits hits = null;

                        //try {
                        if (searcher != null) {
                            //search to see if this file is already in the index
                            Term searchTerm = new Term(PATH_FIELD, path);
                            Query searchQuery = new TermQuery(searchTerm);
                            hits = searcher.search(searchQuery);
                        }

                        try {
                            if ((hits != null) && (hits.length() > 0)) {
                                long docModified = DateField.stringToTime(hits.doc(
                                            0).get(MODIFIED_FIELD));

                                //if it is check the modification time.
                                if (fileModified > docModified) {
                                    LOGGER.finer("file at " + path
                                        + "has been modified recently");

                                    //if modified then put in the docs to add.
                                    docsToAdd.add(XMLDocument.Document(
                                            curFile, attrMap));

                                    //don't add it to current files so that 
                                    //the old one will be deleted.
                                } else {
                                    //if it hasn't been modified, then it goes in the
                                    //the list of files currently stored by the index.
                                    LOGGER.finer("adding path " + path
                                        + " to currentFiles");
                                    currentFiles.add(path);
                                }
                            } else {
                                //no hits, the file isn't in the index, process and add.
                                docsToAdd.add(XMLDocument.Document(curFile,
                                        attrMap));
                            }
                        } catch (Exception e) {
                            LOGGER.severe("Problem with storing xml document "
                                + path + " : " + e.getMessage() + "\nstack: ");

                            StackTraceElement[] trace = e.getStackTrace();

                            for (int j = 0; j < trace.length; j++) {
                                LOGGER.finer(trace[j].toString());
                            }
                        }
                    }
                }

                boolean createNew;

                if (searcher != null) { //if we were able to open the searcher
                    searcher.close(); //then close it
                    createNew = false; //and don't create a new index.
                } else { //if not then there is not an index at the path given.
                    createNew = true; //so create a new index.
                }

                addDocs(docsToAdd, createNew, currentFiles);

                return docsToAdd.size();
            } else {
                LOGGER.warning("The datafolder server property : " + dataFolder
                    + " is not valid");

                return 0;
            }
        } catch (IOException e) {
            LOGGER.warning(e.toString());

            return 0;
        }
    }

    private IndexSearcher createSearcher(String pathToIndex) {
        IndexSearcher searcher = null;

        try {
            LOGGER.finer("using index at " + pathToIndex);
            searcher = new IndexSearcher(pathToIndex);
        } catch (IOException e) {
            LOGGER.finer("No zserver index found at: " + pathToIndex
                + ".  Creating new index if necessary.");
        }

        return searcher;
    }

    /**
     * Helper method to update.  Handles iterating through currently  stored
     * documents to see if any should be added or deleted, and then  adds the
     * appropriate documents.  Also creates a new index if needed.
     *
     * @param docsToAdd a list of the document paths to add to the index.
     * @param createNew The index searcher used.
     * @param currentFiles The set of files that should not be deleted.
     *
     * @throws IOException If there are problems writing.
     */
    private void addDocs(List docsToAdd, boolean createNew, Set currentFiles)
        throws IOException {
        IndexWriter writer;

        if (!createNew) {
            //if there is an index that exists (createNew is false)
            //if (!(docsToAdd.isEmpty() || currentFiles.isEmpty())) { 
            IndexReader reader = IndexReader.open(pathToIndex);
            int max = reader.maxDoc();

            //then iterate through all the documents.
            for (int i = 0; i < max; i++) {
                if (!reader.isDeleted(i)) {
                    Document curDoc = reader.document(i);
                    String docPath = curDoc.get(PATH_FIELD);

                    if (!currentFiles.contains(docPath)) {
                        //if the path that this was indexed from is
                        //not in our list of current files then delete it.
                        reader.delete(i);
                        LOGGER.finer("updating doc at " + docPath);
                    }
                }
            }

            reader.close();

            //}
        }

        if (!docsToAdd.isEmpty()) {
            writer = new IndexWriter(pathToIndex, new ZServAnalyzer(), createNew);
            LOGGER.finer("about to add docs....");

            for (int i = 0; i < docsToAdd.size(); i++) {
                Document doc = (Document) docsToAdd.get(i);
                writer.addDocument(doc);
            }

            writer.optimize();
            writer.close();
            LOGGER.finer("closed writer");
        }
    }

    public int numDocs() {
        try {
            IndexReader reader = IndexReader.open(pathToIndex);

            return reader.numDocs();
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * A filename filter that accepts all files that end with 'xml'
     */
    private class XMLFilenameFilter implements FilenameFilter {
        /**
         * no argument constructor
         */
        public XMLFilenameFilter() {
        }

        /**
         * Tells whether or not to accept the given name.
         *
         * @param dir The directory where the file to test resides.
         * @param name The name of the file.
         *
         * @return true if the name ends in xml, false otherwise.
         */
        public boolean accept(File dir, String name) {
            int strln = name.length();

            //TODO: use reg-exps, faster, also filter out files starting
            //with . or #
            return (name.substring(strln - 3, strln).equals("xml"));
        }
    }
}
