// Title:       DemoSearchTask
// @version:    $Id: GeoSearchTask.java,v 1.1 2002/11/15 22:25:33 cholmesny Exp $
// Copyright:   Copyright (C) 2001 Knowledge Integration Ltd.
// @author:     Ian Ibbotson (ibbo@k-int.com)
// Company:     KI
// Description: 
//

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

import com.k_int.IR.*;
import com.k_int.IR.Syntaxes.*;
import com.k_int.IR.QueryModels.*;
import com.k_int.util.RPNQueryRep.*;
import java.util.*;


// For constructing a dummy result record
import com.k_int.IR.*;
import org.w3c.dom.*;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xerces.dom.DOMImplementationImpl;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
//import org.apache.lucene.document.Document;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;

// Imported JAVA API for XML Parsing 1.0 classes
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; 

import org.xml.sax.InputSource;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class GeoSearchTask extends SearchTask implements InformationFragmentSource
{
  private static String[] private_status_types = { "Idle", "Searching", "Search complete", "Requesting records", "All records returned" };
  public int geo_search_status = 0;
  private int fragment_count = 0;
  private GeoSearchable source = null;
  private java.util.Random random = new java.util.Random();
    private IRQuery q;
    private Hits hits;
    private Properties attrMap;
    private Properties serverProps;

  public GeoSearchTask(GeoSearchable source, Observer[] observers)
  {
    super(observers);
    this.source = source;
    setTaskStatusCode(TASK_IDLE);
  }

    public GeoSearchTask(GeoSearchable source, Observer[] observers, 
			 IRQuery q, Properties serverProps){
	this(source, observers);
	this.q = q;
	this.serverProps = serverProps;
	String attrMapFile = serverProps.getProperty("fieldmap");
	attrMap =  getAttrMap(attrMapFile);
    }


  public int getPrivateTaskStatusCode()
  {
    return geo_search_status;
  }

  public void setPrivateTaskStatusCode(int i)
  {
    this.geo_search_status = i;
  }

  public String lookupPrivateStatusCode(int code)
  {
    return private_status_types[code];
  }

    public int evaluate(int timeout) throws SearchException
  {

      
      try {

	  Vector databases = resolveDBs(q.collections);
	  //can only deal with one database now, so just get the first
	  //path string from the collections vector.
	  Searcher searcher = new IndexSearcher(databases.get(0).toString());
	  Analyzer analyzer = new StopAnalyzer();


	  
	  
	  // Random number of records.. Set up the result set
	  System.out.println("Evaluation: query is: db - " + q.collections.firstElement() + "query" + q.query); 
	  RPNTree rpnQuery = (RPNTree) q.query;
	  //attrMap = 
	  //  getProperties(new File("/home/chris/zServer/props/geo.props"));
	  //bad hard code...change!	
	  
	  //      Query indexQuery = RPN2LuceneQuery.convertRPN(rpnQuery.toRPN(), attrMap);
	  RPNConverter converter = new RPNConverter(attrMap);
	  Query indexQuery = converter.toLuceneQuery(rpnQuery.toRPN());
	  // System.out.println("query is " + indexQuery + "bool" + ((org.apache.lucene.search.BooleanQuery)indexQuery).toString("title"));
	  
	  
	  hits = searcher.search(indexQuery);
	  System.out.println(hits.length() + " total matching documents");
	  
	  
	  final int HITS_PER_PAGE = 10;
	  for (int start = 0; start < hits.length(); start += HITS_PER_PAGE) {
	      int end = Math.min(hits.length(), start + HITS_PER_PAGE);
	      for (int i = start; i < end; i++) {
		  org.apache.lucene.document.Document doc = hits.doc(i);
		  String path = doc.get("path");
		  if (path != null) {
		      System.out.println(i + ". " + path);
		      System.out.println("Title: " + doc.get("title"));
		  } else {
		      String url = doc.get("url");
		      if (url != null) {
			  System.out.println(i + ". " + url);
			  System.out.println("   - " + doc.get("title"));
		      } else {
			  System.out.println(i + ". " + "No path nor URL for this document");
		      }
		  }
	      }
	      break;
	  }
	  
      } catch (java.io.IOException e) {
	  System.out.println("Io except " + e.getMessage());
      }
      
      setFragmentCount(hits.length());
      setTaskStatusCode(TASK_COMPLETE);
      
      
      return(getTaskStatusCode());   
      
  }
 
  // Allow access to the set of objects based on a handle system (DOI?)
  public InformationFragment getFragment(String handle)
  {
    return getFragment(handle, InformationFragmentSource.default_spec);
  }
 
  // Allow access to the set of objects based on a handle system (DOI?)
  public InformationFragment getFragment(String handle, RecordFormatSpecification spec)
  {
    return null;
  }
 
  // Get a result record from this query
  public InformationFragment getFragment(int index)
  {
    return getFragment(index, InformationFragmentSource.default_spec);
  }

  // Get a result record from this query
  public InformationFragment getFragment(int index, RecordFormatSpecification spec)
  {
      System.out.println("spec is " + spec); 
      //If multiple dbs are implemented then change the first two arguments of
      //the return records.
      if (spec.getFormat().getName().equals("xml")) {
	  return new DOMTree("DefaultDB", "DefaultCollection", null, getXmlRecordForHit(index, spec), spec);
      } else { //we can use SUTRS information fragment for html and sgml.
	  return new SUTRS("DefaultDB", "DefaultCollection", null, getRecordForHit(index, spec), spec);
      }      

  }

  public InformationFragment[] getFragment(int starting_fragment, int count)
  {
    return getFragment(starting_fragment, count, InformationFragmentSource.default_spec);
  }

  public InformationFragment[] getFragment(int starting_fragment, 
		                           int count,
					   RecordFormatSpecification spec)
  {
    InformationFragment result[] = new InformationFragment[count];
    for ( int i=0; i<count; i++ )
    {
// System.out.println("spec is " + spec + " format name: " + /*spec.getFormatName() +*/
	//			 " schema: " + spec.getSchema() + "setname: " + spec.getSetname());
	result[i] = getFragment(i+starting_fragment, spec);

	/*      result[i] = new DOMTree("DummyDB",
		              "DummyCollection",
			      null,
			      getRecordForHit(i+starting_fragment, spec),
			      spec); */
			      //new RecordFormatSpecification("xml","meta","f"));
    }
    return result;
  }

  public void store(int id, InformationFragment fragment)
  {
  }

  public void setFragmentCount(int i)
  {
    fragment_count = i;
    FragmentSourceEvent e = new FragmentSourceEvent(FragmentSourceEvent.FRAGMENT_COUNT_CHANGE, new Integer(i));
    setChanged();
    notifyObservers(e);
  }

  public int getFragmentCount()
  {
    return fragment_count;
  } 



    private String getRecordForHit(int n, RecordFormatSpecification spec){
	String setname = spec.getSetname().toString();
	String reqFormat = spec.getFormat().getName();
	System.out.println("reqFomrat is " + reqFormat);
	String retStr = null;
	try {
	    org.apache.lucene.document.Document hit = hits.doc((n - 1));
	    if (setname.equals("B")){
		//HACK: change this hardcode.
		retStr = (hit.get("//metadata/idinfo/citation/citeinfo/title"));
		System.out.println("return = " + retStr);
	    } else if (setname.equals("S")) {
		GeoSummary summary = new GeoSummary(hit, attrMap);
		if (reqFormat.equals("html")) {
		    retStr = summary.getHtmlSummary();
		} else {
		retStr = summary.getTextSummary();
		}
	    } else {
		String filename = hit.get(reqFormat); //TODO: check to make sure it's supported
		RandomAccessFile file = new RandomAccessFile(filename, "r");
		int len = (int)file.length();
		byte[] retArr = new byte[len];
		file.read(retArr);
		retStr =  (new String(retArr));
	    }
	} catch (Exception e) {
	    System.out.println("problem getting " + reqFormat + " record " + e.getMessage());
	}
	return retStr;
    }

    
 private Document getXmlRecordForHit(int n, RecordFormatSpecification spec)
  {
      String setname = spec.getSetname().toString();
      String reqFormat = spec.getFormat().getName().toString();
      Document retval = null;
      try {
      org.apache.lucene.document.Document hit = hits.doc((n - 1));
      String filename = hit.get("path");

      //TODO: handle default of F and B...figure out summary!

      if (setname.equals("F")){
	  InputSource in = new InputSource(new FileInputStream(filename));
	  DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
	  dfactory.setNamespaceAware(true);
	  retval = dfactory.newDocumentBuilder().parse(in);
      } else if (setname.equals("S")) {
	  GeoSummary summary = new GeoSummary(hit, attrMap);
	  retval = summary.getXmlSummary();
      } else {
	  DOMImplementation dom_impl = org.apache.xerces.dom.DOMImplementationImpl.getDOMImplementation();
	  retval = new org.apache.xerces.dom.DocumentImpl();
	  Element root = retval.createElement("metadata");
	  retval.appendChild( root );
	  Element id = retval.createElement("idinfo");
	  root.appendChild(id);
	  Element citation = retval.createElement("citdation");
	  id.appendChild(citation);
	  Element citeinfo = retval.createElement("citeinfo");
	  citation.appendChild(citeinfo);
	  //HACK: change this hardcode
	  addElement(retval, citation, "title", hit.get("//metadata/idinfo/citation/citeinfo/title"));
	  //-----------
	  Element root2 = retval.createElement("metadata");
	  retval.appendChild( root2 );
	  Element id2 = retval.createElement("idinfo");
	  root2.appendChild(id2);
	  Element citation2 = retval.createElement("citation");
	  id2.appendChild(citation2);
	  Element citeinfo2 = retval.createElement("citeinfo");
	  citation2.appendChild(citeinfo2);
	  //HACK: change this hardcode
	  addElement(retval, citation2, "pubdate", hit.get("//metadata/idinfo/citation/citeinfo/pubdate"));
      }   
      /*if (setname.equals("F")) {
      /addElement(retval, root, "publish", hit.get("publish"));
	addElement(retval, root, "originator", hit.get("origin"));
	addElement(retval, root, "subject", hit.get("summary"));
	addElement(retval, root, "coverage", "Dummy Coverage "+n);
	} */

      } catch (Exception e) {
	  //TODO: catch each exception...should be 4.  Deal with gracefully
	  System.out.println("exception: " + e.getMessage());
      }
      return retval;
  }

  private Document getDummyRecordForHit(int n)
  {
      Document retval = null;
      try {
	  if (n > 5) n = 2;
      org.apache.lucene.document.Document hit = hits.doc((n - 1));
      String filename = hit.get("path");

      //InputSource in = new InputSource(new FileInputStream(filename));
      //DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
      //dfactory.setNamespaceAware(true);
      //retval = dfactory.newDocumentBuilder().parse(in);
		

      DOMImplementation dom_impl = org.apache.xerces.dom.DOMImplementationImpl.getDOMImplementation();
      retval = new org.apache.xerces.dom.DocumentImpl();
      Element root = retval.createElement("meta");
    retval.appendChild( root );
    addElement(retval, root, "title", hit.get("title"));
    addElement(retval, root, "creator", hit.get("publish"));
    addElement(retval, root, "originator", hit.get("origin"));
    addElement(retval, root, "subject", hit.get("summary"));
    addElement(retval, root, "coverage", "Dummy Coverage "+n);
      

      } catch (Exception e) {
	  //TODO: catch each exception...should be 4.  Deal with gracefully
	  System.out.println("exception: " + e.getMessage());
      }
      return retval;
  }

  private void addElement(Document d, Element parent, String elem_name, Object value)
  {
    if ( value != null )
    {
      Element new_element = d.createElement(elem_name);
      parent.appendChild(new_element);
      new_element.appendChild(d.createTextNode(value.toString()));
    }
  }

  public InformationFragmentSource getTaskResultSet()
  {
    return this;
  }

  public void destroy()
  {
  }

  /** From SearchTask abstract base class */
  public void destroyTask()
  {
  }

  // public String getStatusReport()
  // {
  //   return "Dummy Result Set : "+getFragmentCount();
  // }

  public AsynchronousEnumeration elements()
  {
    return new DefaultSourceEnumeration(this);
  }

  public IRStatusReport getStatusReport()
  {
    return new IRStatusReport("Demo",
                              "Demo",
                              "Demo",
                              private_status_types[geo_search_status],
                              getFragmentCount(),
                              getFragmentCount(),
                              null,
			      getLastStatusMessages());
  }

    private Properties getProperties(File propFile) {
	//add defaults for any field?
	Properties propertyList = null;
	try {
	    FileInputStream fis = new FileInputStream(propFile);
	    propertyList = new Properties();
	    propertyList.load(fis);
	    return propertyList;
	} catch (FileNotFoundException e) {
	    System.out.println("File not found: " + e.getMessage());
	} catch (IOException e) {
	    System.out.println("IO exception: " + e.getMessage());
	}
    
    return propertyList;
    }

    /**
     * Turns a vector of database names into the paths to their
     * lucene dbs.  Just queries the props file for the database, thus
     * always searching the default db, it doesn't matter what
     * the user passes in.
     *
     * @param dbs The databases to be searched, from the search request.
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



    /**
     * Creates the Attribute Map hashtable given the path
     * to the props file.  
     *
     * @param pathToPropFile the location of the mapping file.
     * @return the mapping of attribute numbers to the attribute xpaths.
     */
    private Properties getAttrMap(String pathToPropFile) {
	//TODO: put default behavior in, good way might be to
	//have call the Properties constructor with defaults.
	//Question is where to store the defaults...in the same
	//directory as this file?  As a class that just creates
	//the default props?
	File propFile = new File(pathToPropFile);
	//add defaults for any field?
	Properties propertyList = null;
	try {
	    FileInputStream fis = new FileInputStream(propFile);
	    propertyList = new Properties();
	    propertyList.load(fis);
	    return propertyList;
	} catch (FileNotFoundException e) {
	    System.out.println("File not found: " + e.getMessage());
	} catch (IOException e) {
	    System.out.println("IO exception: " + e.getMessage());
	}
    
    return propertyList;
    }      


    /*    private byte[] getOldRecordForHit(int n, RecordFormatSpecification spec){
	String setname = spec.getSetname().toString();
	String reqFormat = spec.getFormat().getName();
	System.out.println("reqFomrat is " + reqFormat);
	byte[] retArr = null;
	try {
	    org.apache.lucene.document.Document hit = hits.doc((n - 1));
	    if (setname.equals("B")){
		retArr = (hit.get("title")).getBytes();
		System.out.println("return = " + hit.get("title"));
	    } else {
		String filename = hit.get(reqFormat); //TODO: check to make sure it's supported
		RandomAccessFile file = new RandomAccessFile(filename, "r");
		int len = (int)file.length();
		retArr = new byte[len];
		file.read(retArr);
	    }
	} catch (Exception e) {
	    System.out.println("problem getting html record " + e.getMessage());
	}
	return retArr;
    }*/




}
