/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root 
 * application directory.
 */

package org.vfny.geoserver.zserver;

import com.k_int.util.RPNQueryRep.AttrPlusTermNode;
import com.k_int.util.RPNQueryRep.RootNode;
import com.k_int.util.RPNQueryRep.ComplexNode;
import com.k_int.util.RPNQueryRep.QueryNode;
import com.k_int.util.RPNQueryRep.AttrTriple;
import com.k_int.IR.SearchException;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.RangeQuery;
import org.apache.lucene.search.PhraseQuery;

import java.util.Enumeration;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;


/**
 * Helper class that converts a jzkit QueryNode into
 * a lucene search query.
 *
 *@author Chris Holmes, TOPP
 *@version $VERSION$
 */
public class RPNConverter
{
    /** A mapping of the Use Attributes from number to name. */
    private Properties attrMap;

    /** The length of a full date string, CCYYMMDD. */
    private static final int DATE_STR_LENGTH = 8;

    /** Standard logging instance for class */
    private static final Logger LOGGER = 
        Logger.getLogger("org.vfny.geoserver.zserver");

    /** Regular Expression to clean up numbers */
    private static final String NON_DIGITS = "[^0-9\\.\\-]+";

    /** Regular Expression to filter out decimals and negative signs */
    public static final String NEG_OR_DEC = "[\\.\\-]+";

    /** Regular Expression to split values from spaces or commas */
    public static final String WHITE_SPACE_OR_COMMA = "[\\s,]+";

    /** Regular Expression to match for whitespace */
    public static final String WHITE_SPACE = "[\\s]+";

    /**
     * Initializes the database and request handler.
     *
     * @param attrMap the mappings of use attribute values to their names.
     */
    public RPNConverter(Properties attrMap) {
	this.attrMap = attrMap;
	//Possibly put this map in GeoProfile?

    }


    /**
     * Returns a query to search the index corresponding to the z39.50 query
     *
     * @param rpnQuery the internal jzkit representation of a z39.50 query
     */
    public org.apache.lucene.search.Query toLuceneQuery(QueryNode rpnQuery) throws SearchException {
	return doConversion(rpnQuery);
    }		    
	

    /**
     * Performs the actual conversion, using recursion to descend into complex 
     * nodes (ie jzkit's representation of boolean queries).
     *
     * @param rpnQuery the internal jzkit representation of a z39.50 query
     */
    private Query doConversion(QueryNode rpnQuery) throws SearchException {
	try {
	    Class queryClass = rpnQuery.getClass();
	//LOGGER.finer("Our query class is " + queryClass);
	//Possibly do these compares with strings?  Wouldn't have to load class...
	if (queryClass.equals(Class.forName("com.k_int.util.RPNQueryRep.AttrPlusTermNode"))) {
	    return convertTermNode((AttrPlusTermNode)rpnQuery);
	} else if (queryClass.equals(Class.forName("com.k_int.util.RPNQueryRep.RootNode"))) {
	    return doConversion(((RootNode)rpnQuery).getChild());
	} else if (queryClass.equals(Class.forName("com.k_int.util.RPNQueryRep.ComplexNode"))) {
	    return convertComplexNode((ComplexNode)rpnQuery);
	} else {
	    LOGGER.severe("Unknown query node:" + queryClass);
	    return null;
	}
	} catch (ClassNotFoundException e) {
	    LOGGER.finer("class not found " + e.getMessage());
	}
	return null;
    }


    /**
     * Helper function for the conversion.  Splits up a complex node into its
     * two children, calls doConversion on its children and puts them in the
     * proper lucene BooleanQuery structure.
     *
     * @param rpnQuery the internal jzkit representation of a z39.50 query
     */
    private Query convertComplexNode(ComplexNode complexQuery) throws SearchException {
	int queryType = complexQuery.getOp();
	BooleanQuery retQuery = new BooleanQuery();
	switch (queryType) {

	case ComplexNode.COMPLEX_AND:
	    retQuery.add(doConversion(complexQuery.getLHS()), true, false); //require both
	    retQuery.add(doConversion(complexQuery.getRHS()), true, false);
	    break;
	case ComplexNode.COMPLEX_OR:
	    retQuery.add(doConversion(complexQuery.getLHS()), false, false); //no require
	    retQuery.add(doConversion(complexQuery.getRHS()), false, false); //or prohibit
	    break;
	case ComplexNode.COMPLEX_ANDNOT:
	    retQuery.add(doConversion(complexQuery.getLHS()), true, false); //require  (AND)
	    retQuery.add(doConversion(complexQuery.getRHS()), false, true); //prohibit (NOT)
	    break;
	case ComplexNode.COMPLEX_PROX: throw new SearchException("Proximity searches not supported.");
	default:  throw new SearchException("Op type not recognized.");
	    
	}
	return retQuery;

    }


    /**
     * Helper function for the base case of the recursive conversion.  Turns
     * a jzkit AttrPlusTermNode into a lucene TermQuery.
     *
     * @param rpnTermNode the internal jzkit representation of a z39.50 term and Attribute.
     */
    private Query convertTermNode(AttrPlusTermNode rpnTermNode) 
    	throws SearchException {	
	if (rpnTermNode == null) {
	    throw new SearchException("AttrPlusTermNode is null");
	}
	//LOGGER.finer("query attrset = " + query.toRPN().getAttrset());
	LOGGER.finer("attrplus term node = " + rpnTermNode);
	String term = rpnTermNode.getTerm();
	if (term == null) {
	    throw new SearchException("term passed in is null");
	}
	term = term.toLowerCase();
	Enumeration enum = rpnTermNode.getAttrEnum();
	String useVal = GeoProfile.Attribute.ANY; //default is any.
	int  relation = GeoProfile.EQUALS; //default is equals.
	boolean truncation = false; //default is no truncation.
	while (enum.hasMoreElements()) 
	{ 
	    AttrTriple triple = (AttrTriple)enum.nextElement();
	    String attrVal = triple.getAttrVal().toString();
	    switch (triple.getAttrType().intValue()) {
	    case GeoProfile.USE: useVal = attrVal;
		break;
	    case GeoProfile.RELATION: relation = Integer.parseInt(attrVal);
		break;
	    case GeoProfile.STRUCTURE:  break;//REVISIT: currently we treat everything as
		//a string, decide what to do based on the use value.
	    case GeoProfile.TRUNCATION: truncation = (attrVal.equals(GeoProfile.TRUNCATE));
		break;
	    default: break;
	    }
	    
	   
	}
	 LOGGER.finer("use val = " + useVal);
	String searchField = attrMap.getProperty(useVal.toString());
	if (searchField == null) {
	    throw new SearchException("Unsupported Use Attribute - " + 
				      useVal, GeoProfile.Diag.UNSUPPORTED_ATTR);
	} 
	LOGGER.finer("search field is " + searchField);
	//	Term searchTerm = new Term(searchField, rpnTermNode.getTerm().toLowerCase());
	//to lower case for case insensitivity, to mimic the SimpleAnalyzer.  
	//Query indexQuery = new TermQuery(searchTerm);
	Query indexQuery = getQuery(searchField, term, relation, truncation);
	//LOGGER.finer("Searching for: " + indexQuery);
	return indexQuery;
    }

    /**
     * Creates the query for a date field.  Can take one or two dates in the value
     * string, seperated by a space.  If there are more than two dates it just uses
     * the first two.
     *
     * @param relation the int representation of the relation attribute for this search.
     * @param searchField the name of the field to be queried.
     * @param searchValue a date string
     */
     private Query getDateQuery(int relation, String searchField, String searchValue) {
	/*Implementation notes:  the searchValue is a Datestring, which should have one
	  date or two.  If it has more we just use the first two.  If there are two
	  we set which is the higher date.  Calling getHighTerm appends 9's if needed,
	  so that a date like 1996 will turn into 19969999, so that dates such as 19960405
	  are not matched when we want all dates after 1996.  For the low term we want
	  to use the 1996, as it is lower than 19960101, so there is no getLowTerm function.
	  If there is just one date we create a highTerm with the same date as the low
	  term.  Then a switch is used, using range queries to get the right behavior.
	  REVISIT: One border case still fails, when a date is stored in our index as
	  1995 and we do a more specific during search, such as 19950204 to 19980203.  Such
	  a search will not match 1995, which it should, because 1995 is below the low term.
	  The only way I can now think of to work around this is to store both a highTerm and lowTerm
	  in the index for each date, which seems to be too much overhead for that one case/ */
	Query returnQuery;
	Term highTerm;
	Term lowTerm;
	String[] dates = searchValue.split("\\s+");
	boolean twoDates = false;
	
	if (dates.length > 1) { // = 2?  If more we just use first 2 dates
	    twoDates = true;
	    if (dates[0].compareTo(dates[1]) > 0) {
		highTerm = getHighTerm(searchField, dates[0]);  //getHighTerm appends 9's if needed
		lowTerm = new Term(searchField, dates[1]); //no need to with low term.
	    } else {
		highTerm = getHighTerm(searchField, dates[1]);
		lowTerm = new Term(searchField, dates[0]);
	    }
	} else { 
	    highTerm = getHighTerm(searchField, dates[0]);
	    lowTerm = new Term(searchField, dates[0]);
	}
	String searchDate = dates[0];
	Term searchTerm = new Term(searchField, searchDate);
	switch (relation) {
	case GeoProfile.LESS_THAN: 
	case GeoProfile.BEFORE: 
	    returnQuery = new RangeQuery(null, lowTerm, false);
	    break;
	case GeoProfile.BEFORE_OR_DURING:
	case GeoProfile.LESS_THAN_EQUAL : returnQuery = new RangeQuery(null, highTerm, true);
	    break;
	case GeoProfile.DURING: 
	case GeoProfile.EQUALS: 
	default: 
	    if (twoDates) {
		returnQuery = new RangeQuery(lowTerm, highTerm, true);  //must be
		//between low and high, inclusive.
	    } else {
		returnQuery = new PrefixQuery(lowTerm);
		//even if a full ccyymmdd date term a prefix query doesn't hurt.
	    }
	    break;
	case GeoProfile.DURING_OR_AFTER:
	case GeoProfile.GREATER_THAN_EQUAL : returnQuery = new RangeQuery(lowTerm, null, true);
	    break;
	case GeoProfile.AFTER:
	case GeoProfile.GREATER_THAN : returnQuery = new RangeQuery(highTerm, null, false);
	    break; 
	case GeoProfile.NOT_EQUAL : returnQuery = notEqualQuery(new PrefixQuery(lowTerm)); 
	    //REVISIT: not equal with two date terms.  Is that even possible?
	    //maybe turn it into a boolean with two ranges, before and after the
	    //two dates.
	    break;

	}
	return returnQuery;
     }


    /**
     * If the searchDate is not a full CCYYMMDD string this appends
     * 9's to the end of it.  This is for border cases such as before
     * or during 1996, when just using 1996 as a search term will not
     * pick up all the dates in 1996.  Not needed for the opposite case,
     * such as before 1996, when we actually want to use just 1996, as it
     * is below any specific date in 1996, and also will not match with
     * just 1996.
     * 
     * @param searchField the field for this search term.
     * @param searchDate the date string used in the search.
     * @return the term of the properly formated date.
     */
    private Term getHighTerm(String searchField, String searchDate) {
	StringBuffer sb = new StringBuffer(searchDate);
	for (int i = searchDate.length(); i < DATE_STR_LENGTH; i++){
	    sb.append("9");
	}
	return new Term(searchField, sb.toString());
    }

    /**
     * Given a string strips out the characters that aren't parts
     * of a number so that the string can be properly converted.
     *
     * @param number A string to be converted to a number.
     * @return the number string that won't raise a conversion error.
     */
    public String cleanNumber(String number) {
	String[] cleanArr = number.split(NON_DIGITS); 
	String retString = "";
	for (int i=0; i < cleanArr.length; i++) {
	    retString += cleanArr[i];
	}
	if ( retString.matches(NEG_OR_DEC)) { //if just dots or dashes it won't be parsed right.
	    retString = "";
	}
	LOGGER.finer("The number we're using is " + retString);
	return retString;
    }


    /**
     * If the given value is a date or number formats it to the proper searchable
     * lucene searchable string.  If not just returns the value.
     *
     * @param value to be formatted to a date or number.
     * @param pathName should contain the fgdc metadata xml name of this value.
     * @return the properly formated string
     */

    private Query getQuery(String searchField, String searchValue, int relation, boolean truncation){
	Query returnQuery;
	//TODO: type checking...maybe put all in NumericField?  yes.
	//if (GeoProfile.isFGDCnum(searchField)) {
	//    searchValue = NumericField.numberToString(searchValue);
	//}
	Term searchTerm = new Term(searchField, searchValue);
	if (GeoProfile.isBoundingField(searchField)) { //(searchField.matches(".*bounding")) {
	    returnQuery= getBoundingQuery(searchValue);
	} else if (GeoProfile.isFGDCdate(searchField)) {
	    //if only one term.
	    returnQuery = getDateQuery(relation, searchField, searchValue);
	    //else do complex date.
	} else if (GeoProfile.isFGDCnum(searchField)) {
	    String searchNumber;
	    try { 
		searchNumber = NumericField.numberToString(searchValue);
	    } catch (NumberFormatException e) {
		String clean = cleanNumber(searchValue);

		if (clean != null && !(clean.equals(""))){
		    searchNumber = NumericField.numberToString(clean);
		} else {
		    return new TermQuery(new Term(searchField, "0")); 
		    //if we reach this case it means the string passed in
		    //contains no digits...this should match nothing.
		}
	    }		    
	    Term numTerm = new Term(searchField, searchNumber);
	    switch (relation) {
	    case GeoProfile.LESS_THAN: returnQuery = new RangeQuery(null, numTerm, false);
		break;
	    case GeoProfile.LESS_THAN_EQUAL : returnQuery = new RangeQuery(null, numTerm, true);
		break;
	    case GeoProfile.EQUALS: returnQuery = new TermQuery(numTerm);
		break;
	    case GeoProfile.GREATER_THAN_EQUAL : returnQuery = new RangeQuery(numTerm, null, true);
		break;
	    case GeoProfile.GREATER_THAN : returnQuery = new RangeQuery(numTerm, null, false);
		break; 
	    case GeoProfile.NOT_EQUAL : returnQuery = notEqualQuery(new TermQuery(numTerm)); 

		break;
	    default: returnQuery = new TermQuery(numTerm); //default equals.
		break;
	    }
	} else if (truncation) {
	    returnQuery = new PrefixQuery(searchTerm);
	    //TODO: truncation with a phrase.  Not sure how to do this in lucene, as you
	    //can't put prefix queries in phrase queries.
	} else {
	    String[] terms = searchValue.split(WHITE_SPACE); 
	    if (terms.length == 1) {
		returnQuery = new TermQuery(new Term(searchField, terms[0]));
	    } else {
		PhraseQuery phraseQ = new PhraseQuery();
		for (int i = 0; i < terms.length; i++) {
		    phraseQ.add(new Term(searchField, terms[i]));
		}
		returnQuery = phraseQ;
	    }
	}

	LOGGER.finer("Search value = " + searchValue);
	return returnQuery;
	
	//} else if (isFGDCnum(pathName)) {
	    //format value       
	//} else { //nothing to be done if not date or time.
	//    return value;
	//}
    }

    private Query notEqualQuery(Query notQuery) {
	BooleanQuery retQuery = new BooleanQuery();

	
	Term trueTerm = new Term("true", "true"); //all documents have the
	//field true with the value true.
	TermQuery trueQuery = new TermQuery(trueTerm);
	retQuery.add(trueQuery, true, false); //term is required
	retQuery.add(notQuery, false, true); //term is prohibited
	return retQuery;
    }

 



    /**
     * Helper function to turn a string into the lucene number
     * format.  Calls cleanNumber to parse out mistypings such as 9d2.34,
     * returns null if there are no numbers
     */
    private String getSearchNum(String searchValue) {
	String number = cleanNumber(searchValue);
	//For better performance only call cleanNumber if NumericField
	//throws an exception.
	if ( number.equals("") || number == null ) {
	    return null;
	} else {
	    return NumericField.numberToString(number);
	}
    }

    private Query getBoundingQuery(String searchValue){
	//REVISIT: This currently acts the same as Isite, where it just assumes that
	//a coordinate string is the short-cut version with only 4 values, the northwest
	//and southeast corners of a rectangle, corresponding to the north, west, south, and
	//east bounding coordinates.  Would be nice to actual use the coordinate string as it
	//was intended.  First step would be to parse it into a JTS polygon.  From there one 
	//could just use the bounding envelope of that, which would still be an approximation,
	//such as if the user did not pass in a rectangle, but would be a good start.
	String[] bounds = searchValue.split(WHITE_SPACE_OR_COMMA); //split string along whitespace or a comma

	String north = null;
	String west = null;
	String south = null;
	String east = null;
	
	try {

		north = getSearchNum(bounds[0]);
		west  = getSearchNum(bounds[1]);
		south = getSearchNum(bounds[2]);
		east = getSearchNum(bounds[3]);
	} catch (ArrayIndexOutOfBoundsException e) {
	    //if out of bounds we want that field to just be null
	    //which is the default so do nothing here.
	}

	BooleanQuery retQuery = new BooleanQuery();

	//if our strings
	if (north != null) {
	Term southTerm = new Term("southbc", north);
	//the southbc field must be less than the north passed in.
	retQuery.add(new RangeQuery(null, southTerm, true), true, false);
	}
	if (south != null) {
	Term northTerm = new Term("northbc", south);
	//the northbc field must be greater than the southbc passed in.
	retQuery.add(new RangeQuery(northTerm, null, true), true, false);
	}
	if (east != null) {
	Term westTerm = new Term("westbc", east);
	//the westbc field must be less than the east passed in.
	retQuery.add(new RangeQuery(null, westTerm, true), true, false);
	}
	if (west != null) {
	Term eastTerm = new Term("eastbc", west);
	//the eastbc field must be greater than the westbc passed in.
	retQuery.add(new RangeQuery(eastTerm, null, true), true, false);
	}
	return retQuery;
    }

  
}
