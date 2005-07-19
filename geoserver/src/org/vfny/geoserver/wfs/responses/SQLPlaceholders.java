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

// current package is a converience location - will be shifted to org.geotools.filter

package org.vfny.geoserver.responses.wfs;

import org.geotools.filter.*;	// this won't be required when shifted

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Inspector to pull out placeholders required by a prepared SQL statement.
 * Code adapted from Chris Holme's org.geotools.filter.SQLUnpacker.
 * Builds a new filter with the placeholder removed and a String array of
 * placeholder values.
 * 
 */
public class SQLPlaceholders {

    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses.wfs");

    /** placeholder literals extract from the filter supplied to the constructor*/
	ArrayList placeholderLiteral = new ArrayList();

    /** filter after removing placholders*/
	Filter prunedFilter = Filter.NONE;
	
    // private FilterPair pair;

    /** The types of Filters that should be part of the supported Filter */
    // private FilterCapabilities capabilities;
	
    /**
     * Constructor with FilterCapabilities from the Encoder used in conjunction
     * with this Unpacker.
     *
     * @param capabilities what AbstractFilters should be supported.
     */
    public SQLPlaceholders(Filter filter) {
    	prunedFilter = doUnPack(filter);
        if (prunedFilter == null) {
        	prunedFilter = Filter.NONE;
        }
    }
 
    /**
     * Returns the resulting Filter after pruning prepared statement
     * placheolders.
     *
     * @return The filter remaining after removing prepared statement placeholders
     */
    public Filter getPrunedFilter() {
        return prunedFilter;
    }

    /**
     * Returns an array of placeholder literals to supply to a SQL select
     * statement
     *
     * @return String array of placeholder literals
     */
    public ArrayList getPlaceholderLiterals() {
        return placeholderLiteral;
    }
    
    /**
     * Performs the actual recursive unpacking of the filter.  Can do the
     * unpacking on either AND or OR filters.
     *
     * @param filter the filter to be split
     * @param splitType the short representation of the logic filter to
     *        recursively unpack.
     *
     * @return A filter of the unsupported parts of the unPacked filter.
     */
    private Filter doUnPack(Filter filter) {

    	// optimise this so that it stops when the required no. of 
    	// placedholders have been read
    	
        { Class c = filter.getClass();
        LOGGER.fine("extracting placeholders from filter class " + c.getName()); }
        
        short type;
        Filter subFilter = null; // for logic iteration
        Filter newFilter = null; // for return value
        
        if (filter == null || filter == Filter.NONE ) {
            return Filter.NONE;
        }

        // prune COMPARE_EQUALS if it is used as a placeholder
        
        type = filter.getFilterType();
        LOGGER.fine("filter is type " + type);
        
        if (type == AbstractFilter.COMPARE_EQUALS) {
        	
        	newFilter = filter;
        	Expression leftValue = ((CompareFilter) filter).getLeftValue();
        	if (leftValue.getType() == ExpressionType.ATTRIBUTE) {
            	String attribute = ((AttributeExpressionImpl)leftValue).getAttributePath();
            	if (attribute.toUpperCase().startsWith("_ARG")) {
                	Expression rightValue = ((CompareFilter) filter).getRightValue();
                	short rvalType = rightValue.getType();
            		if (	rvalType == ExpressionType.LITERAL_DOUBLE ||
            				rvalType == ExpressionType.LITERAL_INTEGER ||
            				rvalType == ExpressionType.LITERAL_STRING )
					{ 
            			String literal = ((LiteralExpression)rightValue).getLiteral().toString();
            			LOGGER.fine("extracted prepared statement arg " + literal);
            			placeholderLiteral.add( ((LiteralExpression)rightValue).getLiteral().toString());
                    	newFilter = null;	// ie prune from filter
					}
            	}
        	}
        	
        } else if (	type == AbstractFilter.LOGIC_OR ||
        			type == AbstractFilter.LOGIC_AND) {

        	// unpack
            Iterator filters = ((LogicFilter) filter).getFilterIterator();

            while (filters.hasNext()) {
                subFilter = doUnPack((Filter) filters.next());
                if (subFilter != null){
                	// transcribe to new filter
        			LOGGER.fine("combining " + subFilter.toString() );
                	newFilter = combineFilters(newFilter, subFilter, type);
                	LOGGER.fine("combined filter is " + newFilter.toString() );
                }	
            }
            
        } else if (type == AbstractFilter.LOGIC_NOT ) {
            
        	// unpack
            Iterator filters = ((LogicFilter) filter).getFilterIterator();

            //NOT only has one, so just get filters.next()
            subFilter = doUnPack((Filter) filters.next());
            if (subFilter != null){
            	// transcribe to new filter
            	newFilter = subFilter.not();
            }
            
        } else { 
        	// not a placeholder and can't be unpacked, return for transcription to new filter
            newFilter = filter;
        }
    	if (newFilter == null) {
    		LOGGER.fine("returning null filter" );    		
    	} else {
    		LOGGER.fine("returning " + newFilter.toString() );        
    	}
        return newFilter;        
    }

    /**
     * Combines two filters, which may be null, into one.  If one is null and
     * the other not, it returns the one that's not.  If both are null returns
     * null.
     *
     * @param filter1 one filter to be combined.
     * @param filter2 the other filter to be combined.
     * @param splitType the short representation of the logic filter to
     *        recursively unpack.
     *
     * @return the resulting combined filter.
     */
    private Filter combineFilters(Filter filter1, Filter filter2,
        short splitType) {
        Filter retFilter;

        if (filter1 != null) {
            if (filter2 != null) {
                if (splitType == AbstractFilter.LOGIC_AND) {
                    retFilter = filter1.and(filter2);
                } else { //OR and AND only split types, this must be or.
                    retFilter = filter1.or(filter2);
                }
            } else {
                retFilter = filter1;
            }
        } else {
            if (filter2 != null) {
                retFilter = filter2;
            } else {
                retFilter = null;
            }
        }

        return retFilter;
    }
}
