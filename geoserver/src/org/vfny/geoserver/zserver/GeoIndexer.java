package org.vfny.geoserver.zserver;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.DateField;

//for main
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.io.FileNotFoundException;

public class GeoIndexer {
    private static final String PATH_FIELD = "path";
    private static final String MODIFIED_FIELD = "modified";
    private static final String META_NAME = "metadata.xml";

    Properties serverProps;
    //IndexWriter indexer;
    //IndexSearcher searcher;
    Properties attrMap;
    String pathToIndex;
    File dataFolder;

    public GeoIndexer(Properties serverProps) {
	this.serverProps = serverProps;
	pathToIndex = serverProps.getProperty("database");
	//indexer = new IndexWriter(pathToIndex, new ZServAnalyzer(), true);
	//searcher = new IndexSearcher(pathToIndex);
	try {
	    dataFolder = new File(serverProps.getProperty("datafolder"));
	    FileInputStream fis = new FileInputStream(serverProps.getProperty("fieldmap"));
	    attrMap = new Properties();
	    attrMap.load(fis);
	} catch (FileNotFoundException e) {
	    System.out.println("couldn't find file " + e.getMessage());
	} catch (IOException e) {
	    System.out.println(e.getMessage());
	}
    }

    public void update() {
	try {
        Set currentFiles = new HashSet();
	List docsToAdd = new ArrayList();
	
	
	if (dataFolder.isDirectory()) {
	    //DirectoryFilter dirFilter = new DirectoryFilter();
	    File[] dirFiles = dataFolder.listFiles();
	    //TODO: use a file filter so we only pick up actual xml files
	    //possibly later add checks to make sure xml are fgdc compliant
	    IndexSearcher searcher = null;
	       try {
		   System.out.println("the index we're opening is at " + pathToIndex);
		   searcher = new IndexSearcher(pathToIndex);
	       } catch (IOException e) {
		   System.out.println("No index found at: " + pathToIndex + ".  Creating new index" );
	       }		
	        for (int i = 0; i < dirFiles.length; i++) {
		    File curDir = dirFiles[i];
		    if (curDir.isDirectory()) {
			File curFile = new File( curDir.getPath(), META_NAME);
		    String path = curFile.getPath();
		    //currentFiles.add(path);
		    long fileModified = curFile.lastModified();
		    Hits hits = null;
		    //try {
		    if (searcher != null) {
			
			//Term searchTerm = new Term("//metadata/idinfo/citation/citeinfo/title", "w");
			Term searchTerm = new Term("path", path);
			Query searchQuery = new TermQuery(searchTerm);
			System.out.println("search term is " + searchTerm + " query is " + searchQuery);
			hits = searcher.search(searchQuery);
			//hits = searcher.search(new TermQuery(new Term(PATH_FIELD, path)));
			System.out.println("There are " + hits.length() + " hits");
		    }
			//} catch (IOException e) {
			//System.out.println(e.getMessage());
			//}		    
		    try {
			if (hits != null && hits.length() > 0) {
			    Document doc = hits.doc(0);
			    System.out.println("got a doc that matches path + " + path);
			    long docModified = DateField.stringToTime(hits.doc(0).get(MODIFIED_FIELD));
			    if (fileModified > docModified) {
				System.out.println("file has been modified recently");
				docsToAdd.add(XMLDocument.Document(curFile, attrMap));
				//don't add it to current files so that the old one will be
				//deleted.
			    } else {
				System.out.println("adding path " + path + "to currentFiles");
				currentFiles.add(path);
			    }
			} else {
			    System.out.println("no matches to path " + path);
			    //no hits, adding doc.
			    docsToAdd.add(XMLDocument.Document(curFile, attrMap));
			}
		    } catch (Exception e) {
			System.out.println("Problem with storing xml document " + path + " : " + e.getMessage() + "\nstack: ");
			//if logging level debug print stack trace.
		    }
		}
		}
		//TODO: put a check on some modify time so this runs
		//when the user deletes files from the data directory, not just
		//when there are new files.
		IndexWriter writer;
		if (searcher != null) {
		    searcher.close();
		    System.out.println("closed searcher");
		    if (!docsToAdd.isEmpty()) {
			System.out.println("there are docs to add...iterating");
			IndexReader reader = IndexReader.open(pathToIndex);
			int max = reader.maxDoc();
			
			for (int i = 0; i < max; i++) {
			    if (!reader.isDeleted(i)) {
				Document curDoc = reader.document(i);
				String docPath = curDoc.get("path");
				System.out.println("Iterating through docs, path is " + docPath);
				if (!currentFiles.contains(docPath)) {
				//if the path that this was indexed from is
				//not in our list of current files then delete it.
				    System.out.println("deleting file " + docPath);
				    reader.delete(i);
				//REVISIT: Prompt user to be sure they want to remove it?
				//Something like: 'There is no longer a file at: path, would
				//you like to remove it from the results returned?'  Probably
				//too much of a hassle, as it'd ask every time, and they'd 
				//notice if the results were gone and just add them.
				}
			    }
			}
		    reader.close();
		    }
		    System.out.println("opening already created writer");
		    writer = new IndexWriter(pathToIndex, new ZServAnalyzer(), false);
		} else { //searcher is null, which means we must create a new index
		    System.out.println("opening newly created writer");
		    writer = new IndexWriter(pathToIndex, new ZServAnalyzer(), true);
		}

		System.out.println("about to add docs....");
		for (int i = 0; i < docsToAdd.size(); i++) {
		    Document doc = (Document)docsToAdd.get(i);
		    System.out.println("adding docnum " + i);
		    writer.addDocument(doc);
		}
		writer.close();
		System.out.println("closed writer");
	} else {
	    System.out.println("The datafolder server property : " + dataFolder + " is not valid");
	    //System.exit(1);
	}

 	} catch (IOException e) {
	    System.out.println(e.getMessage());
	}

    }

    private class XMLFilenameFilter implements FilenameFilter {

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
