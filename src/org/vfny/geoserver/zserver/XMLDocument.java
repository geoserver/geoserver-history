package org.vfny.geoserver.zserver;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateField;

//for main
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;

//import org.apache.samples.ApplyXpath;
import java.util.Properties;
import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.io.FileInputStream;
import java.util.Date;
import java.io.FilenameFilter;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

//import org.vfny.zServer.search.GeoProfile;

/**
* A utility for making lucene document from an XML source and a set of
xpaths
* based on Document example from Lucene
*
*/
public class XMLDocument
{
    private static String westbc;
    private static String eastbc;
    private static String northbc;
    private static String southbc;

        private XMLDocument() { }

         /**
          * @param file Document that to be converted to a lucene document
          * @param propertyList properties where the key is the field
name and the value is the
          * XML xpath.
          * @throws FileNotFoundException
          * @throws Exception
          * @return lucene document
          */
        public static Document Document (File file, Properties propertyList)
	    throws java.io.FileNotFoundException, Exception
        {
                Document doc = new Document();
		System.out.println("processing file: " + file);
                // add path
		String xmlPath = file.getPath();
                doc.add(Field.Keyword("path", xmlPath));
		System.out.println("adding path " + xmlPath);
		//TODO: Checking to make sure these files exist!
		doc.add(Field.UnIndexed("sutrs", changeExtension(xmlPath, "txt")));
		doc.add(Field.UnIndexed("html", changeExtension(xmlPath, "html")));
		doc.add(Field.UnIndexed("sgml", changeExtension(xmlPath, "sgml")));	
		

                //add date modified
                doc.add(Field.Keyword("modified", 
				      DateField.timeToString(file.lastModified())));
		//If Lucene has to/can return the Breif, Summary and Full records it
		//could probably be done really easy by just storing them as unindexed
		//fields, then Hits can get the B, S or F field as needed, and return
		//all the right data.  Damn lucene is good.  
		doc.add(Field.UnIndexed("url", file.getPath()));

		doc.add(Field.Text("true", "true"));  //used for searching not equals, since
		//lucene does not support it, we have to do a boolean query with
		//something always true, which will be this field.

		Date today = new Date();
		doc.add(Field.Text("date added", DateField.dateToString(today)));
                //add field list in property list
		Collection values = propertyList.values();
		Set valueSet = new HashSet(values);
		Iterator e = valueSet.iterator();
		 while (e.hasNext())
                 {
		     String key = (String) e.next();
		     //Object xpath = e.next();
		     if (key.charAt(0) == '/') {
			String xpath = key + "/text()";
			//System.out.println("xpath is " + xpath);
			String[] valueArray = ApplyXPath.apply(file.getPath(),xpath);
			StringBuffer value = new StringBuffer("");
			for (int i=0; i < valueArray.length; i++)
                        {
			    value.append(valueArray[i]);
                        }
                        String textValue = formatValue(value.toString(), key);
			doc.add(Field.Text(key,textValue));
		     }
		 }
		 
		 doc.add(Field.Text("extent", computeExtent()));
                 return doc;
        }



         /**
          * @return lucene document
          * @param fieldNames field names for the lucene document
          * @param file Document that to be converted to a lucene document
          * @param xpaths XML xpaths for the information you want to get
          * @throws Exception
          */
         public static Document Document(File file, java.lang.String[]
fieldNames, java.lang.String[] xpaths)  throws java.io.FileNotFoundException , Exception
         {
             if (fieldNames.length != xpaths.length)
             {
                 throw new IllegalArgumentException ("String arrays are not equal size");
             }

             Properties propertyList = new Properties();

             // generate properties from the arrays
             for (int i=0;i<fieldNames.length;i++) {
                 propertyList.setProperty(fieldNames[i],xpaths[i]);
             }

             Document doc = Document (file, propertyList);
             return doc;
         }

         /**
          * @param path path of the Document that to be converted to a
lucene document
          * @param keys
          * @param xpaths
          * @throws Exception
          * @return
          */
         public static Document Document(String path, String[]
fieldNames, String[] xpaths)
         throws Exception
         {
             File file = new File(path);
             Document doc = Document (file, fieldNames, xpaths);
             return doc;
         }

         /**
          * @param path path of document you want to convert to a lucene
document
          * @param propertyList properties where the key is the field
name and the value is the
          * XML xpath.
          * @throws Exception
          * @return lucene document
          */
         public static Document Document(String path, Properties
propertyList)
         throws Exception
         {
             File file = new File(path);
             Document doc = Document (file, propertyList);
             return doc;
         }

         /**
          * @param documentPath path of the Document that to be converted
to a lucene document
          * @param propertyPath path of file containing properties where
the key is the field name and the value is the
          * XML xpath.
          * @throws Exception
          * @return
          */
         public static Document Document(String documentPath, String
propertyPath)
         throws Exception
         {
	     
             File file = new File(documentPath);
             FileInputStream fis = new FileInputStream(propertyPath);
             Properties propertyList = new Properties();
             propertyList.load(fis);
             Document doc = Document (file, propertyList);
             return doc;
         }

         /**
          * @param documentFile Document that to be converted to a lucene
document
          * @param propertyFile file containing properties where the key
is the field name and the value is the
          * XML xpath.
          * @throws Exception
          * @return
          */
         public static Document Document(File documentFile, File
propertyFile)
         throws Exception
         {
             FileInputStream fis = new FileInputStream(propertyFile);
             Properties propertyList = new Properties();
             propertyList.load(fis);
             Document doc = Document (documentFile, propertyList);
             return doc;
         }

         private static String filter(String key, StringBuffer value) {
             String newValue;
             newValue = value.toString();
             return newValue;
         }

    public static void main(String[] args) throws Exception
    {
	
	if(args.length == 0)
	    {
		System.out.println("args: indexPath xmlFile");
		System.exit(0);
	    }
	
	System.out.println("Index Path =--- "+args[0]);
	System.out.println("XML Dir = "+args[1]);
	System.out.println("props File = "+args[2]);
	

	File xmlDir = new File(args[1]);
	File propsFile = new File(args[2]);
	if (xmlDir.isDirectory()) {
	    IndexWriter indexWriter = 
		new IndexWriter(args[0], new ZServAnalyzer(), true);
	    XMLFilenameFilter xmlFilter = new XMLFilenameFilter();
	    File[] xmlFiles = xmlDir.listFiles(xmlFilter);
	    //TODO: use a file filter so we only pick up actual xml files
	    //possibly later add checks to make sure xml are fgdc compliant
	    
	        for (int i = 0; i < xmlFiles.length; i++) {
		Document doc = XMLDocument.Document(xmlFiles[i], propsFile);
		//System.out.println(doc);
		indexWriter.addDocument(doc);
	    }
	    indexWriter.close();
	    System.exit(0);


	} else {
	    System.out.println(args[1] + " is not a directory");
	    System.exit(1);
	}


	/*	Document doc;		
	try
	    {
		
		doc = XMLDocument.Document(xmlFile, propsFile);
		System.out.println(doc);
		indexWriter.addDocument(doc);
		indexWriter.close();
	    }
	catch (Exception ex)
	    {
		ex.printStackTrace(System.out);
		System.err.println(ex);
		System.exit(1);
		}*/
    }

    private static void grabBoundingCoords(String value, String key) {
	int strlen = key.length();		
	//System.out.println(key + " is " + value);
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

    private static String computeExtent() {
	try {
	    double east = Double.parseDouble(eastbc);
	    double west = Double.parseDouble(westbc);
	    double north = Double.parseDouble(northbc);
	    double south = Double.parseDouble(southbc);
	    Double extent = new Double((north - south) * (east - west));
	    //System.out.println(north + " - " + south + " * " + east + " - " + west +
	    //" = " + extent);
	    return NumericField.numberToString(extent);
	} catch (NumberFormatException e) {
	    return null; //REVISIT: is this what we want?
	}
    }
    /**
     * If the given value is a date or number formats it to the proper searchable
     * lucene searchable string.  If not just returns the value.
     *
     * @param value to be formatted to a date or number.
     * @param key should contain the fgdc metadata xml name of this value.
     * @return the properly formated string
     */

    private static String formatValue(String value, String key){
	//int strlen = key.length();
	String retString;
	if (GeoProfile.isFGDCnum(key)) { //need to add extent...
	    grabBoundingCoords(value, key);

	    
	    try {
		retString = NumericField.numberToString(value);
	    } catch (NumberFormatException e) {
		retString = NumericField.numberToString("0"); //maybe clean up number like we do in search?
	    }  
	    //System.out.println(key + " is " + retString);
	} else {
	    if (!key.equals("/")) { //if key is not / (for any text field)
		retString = stripAndTrim(value); //to get rid of new lines and trailing whitespace.
	    } else {
		retString = value;
	    }
	}
	return retString;
    }

    /**
     * Cleans up a string, cleaning up the whitespace chars.
     * Basically replaces every weird whitespace character, such
     * as newline (\n) or tab (\t) with a simple space.
     * 
     * @param value the string to be cleaned.
     * @return the clean string.
     */
    private static String stripAndTrim(String value) {
	String[] cleanArr = value.split("[\\s]+");
	String retString = "";
	for (int i=0; i < cleanArr.length; i++) {
	    retString += cleanArr[i] + " ";
	}
	return retString.trim();
    }

    /**
     * Turns a z39.50 Date String to a lucene searchable date
     * string.
     *
     * @param zDateString in the z39.50 format.
     * @return the date in the lucene date format.
     */
    private static String formatDate(String zDateString) {
        int strLen = zDateString.length();
	if (strLen < 8) {
	    //zDateString += "*";
	}
	return zDateString;
	/*From the GEO profile annex A
	 * (http://www.blueangeltech.com/standards/GeoProfile/annex_a.htm):
	 * A Date String is a character string that represents a single date 
	 * using CCYYMMDD format, or a beginning and ending date range using 
	 * CCYYMMDD/CCYYMMDD format, where CC, YY, MM, DD are the two-digit 
	 * representation of the century, year, month, and day, respectively. 
	 * The month (MM) and day (DD) are optional. */
	//I think geo profile only uses single dates: CHECK THIS!
	/*strLen = zDateString.length();
	String year;
	String month;
	String day;
	year = zDateString.substring(0,4);
	if (strLen < 6) { //just the year (CCYY)
	    month = "01";
	} else {
	    month = zDateString.substring(4,6);
	} 
	if (strLen < 8){
	    day = "01";
	} else {
	    day = zDateString.substring(6,8);
	}
	return year + month + day;*/

	/*Calendar cal = new Calender();
	cal.set(Integer.parseInt(year), 
		Integer.parseInt(month), 
		Integer.parseInt(date));
		return DateField.dateToString(cal.getTime());*/
    }



    private static String changeExtension(String file, String newExt){
	return((file.substring(0, file.length() - 3)) + newExt);
    }

    private static class XMLFilenameFilter implements FilenameFilter {

	public XMLFilenameFilter(){
	}
	
	public boolean accept(File dir, String name){
	    int strln = name.length();
	    //TODO: use reg-exps, faster, also filter out files starting
	    //with . or #
	    return (name.substring(strln - 3, strln).equals("xml"));
	}
    }
}

