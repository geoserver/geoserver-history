/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;


/**
 * A utility for making lucene document from an XML source and a  mapping of
 * attributes consisting of a use number and the xpath of the name. Based on
 * Document example from Lucene.  Taken from the lucene mailing list:
 * http://www.mail-archive.com/lucene-user@jakarta.apache.org/msg00346.html and
 * modified to work with the Geo Profile. email details: From: carlson
 * Subject: Re: Indexing other documents type than html and txt (XML) Date:
 * Thu, 29 Nov 2001 09:03:53 -0800
 *
 * @author carlson
 * @author Chris Holmes, TOPP
 */
public class XMLDocument {
    public static final String WHITE_SPACE = "[\\s]+";

    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");

    /** To compute the extent with */
    private static String westbc;

    /** To compute the extent with */
    private static String eastbc;

    /** To compute the extent with */
    private static String northbc;

    /** To compute the extent with */
    private static String southbc;

    private XMLDocument() {
    }

    /**
     * Converts an xml file to a lucene document, using the values of the
     * attribute map as the xpaths.  These xpaths also serve as the name of
     * the fields for conversions later.
     *
     * @param file Document that to be converted to a lucene document
     * @param attrMap mappings of attribute numbers to xpaths.
     *
     * @return lucene document
     *
     * @throws java.io.FileNotFoundException
     * @throws Exception
     */
    public static Document Document(File file, Properties attrMap)
        throws java.io.FileNotFoundException, Exception {
        Document doc = new Document();
        LOGGER.finer("processing file: " + file);

        // add path
        String xmlPath = file.getPath();
        doc.add(Field.Keyword("path", xmlPath));
        doc.add(Field.UnIndexed("sutrs", changeExtension(xmlPath, "txt")));
        doc.add(Field.UnIndexed("html", changeExtension(xmlPath, "html")));
        doc.add(Field.UnIndexed("sgml", changeExtension(xmlPath, "sgml")));

        //add date modified for reindexing.
        doc.add(Field.Keyword("modified",
                DateField.timeToString(file.lastModified())));
        doc.add(Field.Text("true", "true")); //used for searching not equals,

        Collection values = attrMap.values();
        Set valueSet = new HashSet(values);

        //only one copy of each of the values.
        Iterator e = valueSet.iterator();

        while (e.hasNext()) {
            String key = (String) e.next();

            //LOGGER.info("reading " + key);
            //Object xpath = e.next();
            if (key.charAt(0) == '/') { //if it is in fact an xpath

                //grab just the text inside the element.
                String xpath = key + "/text()";
                String[] valueArray = ApplyXPath.apply(file.getPath(), xpath);
                StringBuffer value = new StringBuffer("");

                for (int i = 0; i < valueArray.length; i++) {
                    //TODO: Probably don't want to just append all
                    //of the values together.  This works fine
                    //for most fields, as there is usually only
                    //one of each, but when there are multiples
                    //we should be able to process them properly.
                    value.append(valueArray[i]);
                }

                String textValue = formatValue(value.toString(), key);
                doc.add(Field.Text(key, textValue));
            }
        }

        //LOGGER.info("doc is " + doc);
        doc.add(Field.Text("extent", computeExtent()));

        return doc;
    }

    /**
     * Convenience method, turns the string into a file.
     *
     * @param path path of document you want to convert to a lucene document
     * @param attrMap properties where the key is the field name and the value
     *        is the XML xpath.
     *
     * @return lucene document
     *
     * @throws Exception
     */
    public static Document Document(String path, Properties attrMap)
        throws Exception {
        File file = new File(path);
        Document doc = Document(file, attrMap);

        return doc;
    }

    /**
     * Convenience method, turns the documentPath into a file, and the
     * propertyPath into a Property object.
     *
     * @param documentPath path of the Document that to be converted to a
     *        lucene document
     * @param propertyPath path of file containing mapping of attribute use
     *        numbers to xpaths. XML xpath.
     *
     * @return
     *
     * @throws Exception
     */
    public static Document Document(String documentPath, String propertyPath)
        throws Exception {
        File file = new File(documentPath);
        FileInputStream fis = new FileInputStream(propertyPath);
        Properties attrMap = new Properties();
        attrMap.load(fis);

        Document doc = Document(file, attrMap);

        return doc;
    }

    /**
     * Turns the File into a lucene. XMLDocument. 
     *
     * @param documentFile Document that to be converted to a lucene document.
     * @param attrMapFile file containing mappings with xpath values. XML
     *        xpath.
     *
     * @return
     *
     * @throws Exception
     */
    public static Document Document(File documentFile, File attrMapFile)
        throws Exception {
        FileInputStream fis = new FileInputStream(attrMapFile);
        Properties attrMap = new Properties();
        attrMap.load(fis);

        Document doc = Document(documentFile, attrMap);

        return doc;
    }

    /**
     * main used to create an index from a directory full of xml files.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("args: indexPath xmlFile");
            System.exit(0);
        }

        System.out.println("Index Path =--- " + args[0]);
        System.out.println("XML Dir = " + args[1]);
        System.out.println("props File = " + args[2]);

        File xmlDir = new File(args[1]);
        File propsFile = new File(args[2]);

        if (xmlDir.isDirectory()) {
            IndexWriter indexWriter = new IndexWriter(args[0],
                    new ZServAnalyzer(), true);
            XMLFilenameFilter xmlFilter = new XMLFilenameFilter();
            File[] xmlFiles = xmlDir.listFiles(xmlFilter);

            for (int i = 0; i < xmlFiles.length; i++) {
                Document doc = XMLDocument.Document(xmlFiles[i], propsFile);
                indexWriter.addDocument(doc);
            }

            indexWriter.close();
            System.exit(0);
        } else {
            System.out.println(args[1] + " is not a directory");
            System.exit(1);
        }
    }

    /**
     * Saves the bounding coordinates to compute the extent.
     *
     * @param value the value of the field.
     * @param key the name of the field.
     */
    private static void grabBoundingCoords(String value, String key) {
        int strlen = key.length();

        if (strlen > 7) {
            String tail = key.substring(strlen - 7, strlen);

            if (tail.equals("/eastbc")) {
                eastbc = value;
            } else if (tail.equals("/westbc")) {
                westbc = value;
            } else if (tail.equals("northbc")) {
                northbc = value;
            } else if (tail.equals("southbc")) {
                southbc = value;
            }
        }
    }

    /**
     * Computes the extent with the bounding coords set with
     * grabBoundingCoords. Basically a wrapper for the GeoProfile
     * computeExtent method, converting the returned value to a lucene
     * NumericField string.
     *
     * @return a string number representation of the extent.
     */
    private static String computeExtent() {
        try {
            Double extent = GeoProfile.computeExtent(eastbc, westbc, northbc,
                    southbc);

            return (extent != null) ? NumericField.numberToString(extent) : "";
        } catch (NumberFormatException e) {
            return null; //REVISIT: is this what we want?
        }
    }

    /**
     * Formats the value to a NumberField number if needed, strips the newline
     * characters.
     *
     * @param value to be formatted to a date or number.
     * @param key should contain the fgdc metadata xml name of this value.
     *
     * @return the properly formated string
     */
    private static String formatValue(String value, String key) {
        //int strlen = key.length();
        String retString;

        if (GeoProfile.isFGDCnum(key)) { //need to add extent...
            grabBoundingCoords(value, key);

            try {
                retString = NumericField.numberToString(value);
            } catch (NumberFormatException e) {
                retString = NumericField.numberToString("0");

                //maybe clean up number like we do in search?
            }
        } else {
            if (!key.equals("/")) { //if key is not / (for any text field)
                retString = stripAndTrim(value);

                //to get rid of new lines and trailing whitespace 
                //for presentation.
            } else {
                retString = value;
            }
        }

        return retString;
    }

    /**
     * Cleans up a string, cleaning up the whitespace chars. Basically replaces
     * every weird whitespace character, such as newline (\n) or tab (\t) with
     * a simple space.
     *
     * @param value the string to be cleaned.
     *
     * @return the clean string.
     */
    private static String stripAndTrim(String value) {
        String[] cleanArr = value.split(WHITE_SPACE);
        String retString = "";

        for (int i = 0; i < cleanArr.length; i++) {
            retString += (cleanArr[i] + " ");
        }

        return retString.trim();
    }

    /**
     * removes the xml from a path and puts a new extension on.
     *
     * @param file the full path to an xml file.
     * @param newExt the new 3 or 4 char ending.
     *
     * @return the file string with the last 3 chars changed to newExt.
     */
    private static String changeExtension(String file, String newExt) {
        return ((file.substring(0, file.length() - 3)) + newExt);
    }

    /**
     * A filename filter that accepts all files that end with 'xml' REVISIT: Do
     * we need this class now?
     */
    private static class XMLFilenameFilter implements FilenameFilter {
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
