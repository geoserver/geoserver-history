/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.zserver;

import java.util.Properties;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.lucene.search.Query;

import com.k_int.util.RPNQueryRep.AttrPlusTermNode;
import com.k_int.util.RPNQueryRep.ComplexNode;
import com.k_int.util.RPNQueryRep.QueryNode;
import com.k_int.util.RPNQueryRep.RootNode;


/**
 * Tests the RPNConverter helper methods.
 *
 * @author Chris Holmes, TOPP
 * @version $Id: RPNConverterSuite.java,v 1.7 2004/01/31 00:17:51 jive Exp $
 */
public class RPNConverterSuite extends TestCase {
    // Initializes the logger. Uncomment to see log messages.
    //static {
    //    org.vfny.geoserver.config.Log4JFormatter.init("org.vfny.geoserver", 
    //java.util.logging.Level.FINEST);
    //}

    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.zserver");
    private static final String TITLE_NUM = "4";
    private static final String TITLE_NAME = "title";
    private static final String ORIGIN_NUM = "1005";
    private static final String ORIGIN_NAME = "origin";
    private static final String DATE_NUM = "31";
    private static final String DATE_NAME = "pubdate";

    //to get all fields to print one must call toString
    //with a field that does not exist.
    private static final String ALL_FIELDS = "ALL";
    private Properties attrMap;
    private RPNConverter converter;
    private RootNode root1;
    private RootNode root2;
    private AttrPlusTermNode rpn1;
    private AttrPlusTermNode rpn2;

    /**
     * Constructor that calls super.
     *
     * @param testName The name of the test to run.
     */
    public RPNConverterSuite(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(RPNConverterSuite.class);
        LOGGER.fine("Creating RPNConverter suite.");

        return suite;
    }

    public void setUp() {
        attrMap = new Properties();
        attrMap.setProperty(TITLE_NUM, TITLE_NAME);
        attrMap.setProperty(ORIGIN_NUM, ORIGIN_NAME);

        converter = new RPNConverter(attrMap);
        root1 = new RootNode();
        root2 = new RootNode();
        rpn1 = new AttrPlusTermNode(root1);
        rpn1.setAttr(null, new Integer(GeoProfile.USE), new Integer(TITLE_NUM));

        rpn2 = new AttrPlusTermNode(root2);
        rpn2.setAttr(null, new Integer(GeoProfile.USE), new Integer(ORIGIN_NUM));
    }

    public void testSimple() throws Exception {
        rpn1.setTerm("water");

        Query retQuery = converter.toLuceneQuery((QueryNode) rpn1);
        String term = retQuery.toString(TITLE_NAME);
        LOGGER.fine("1st query is " + term);
        assertTrue(term.equals("water"));

        rpn2.setTerm("jibbay");

        Query retQuery2 = converter.toLuceneQuery((QueryNode) rpn2);
        String retTerm = retQuery2.toString(ORIGIN_NAME);
        LOGGER.fine("2nd query is " + retTerm);
        assertTrue(retTerm.equals("jibbay"));
    }

    public void testRoot() throws Exception {
        rpn1.setTerm("lime");
        root1.setChild(rpn1);

        Query retQuery = converter.toLuceneQuery((QueryNode) root1);
        String term = retQuery.toString(TITLE_NAME);
        LOGGER.fine("1st query is " + term);
        assertTrue(term.equals("lime"));
    }

    public void testLowerCase() throws Exception {
        //indexer puts to lower case, so terms must be lower
        //case to search.
        rpn2.setTerm("UpPeR");

        Query retQuery = converter.toLuceneQuery((QueryNode) rpn2);
        String term = retQuery.toString(ORIGIN_NAME);
        LOGGER.fine("lower term is " + term);
        assertTrue(term.equals("upper"));
    }

    public void testComplex() throws Exception {
        rpn1.setTerm("left");
        rpn2.setTerm("right");

        RootNode comRoot = new RootNode();
        ComplexNode complex = new ComplexNode(comRoot, rpn1, rpn2);
        complex.setOp(ComplexNode.COMPLEX_OR);

        Query retQuery = converter.toLuceneQuery((QueryNode) complex);
        String term = retQuery.toString(ALL_FIELDS);
        LOGGER.fine("Complex OR term is " + term);
        assertTrue(term.equals("title:left origin:right"));

        //RootNode comRoot2 = new RootNode();
        rpn1.setTerm("or");

        ComplexNode complex2 = new ComplexNode(comRoot, rpn1, rpn2);
        complex2.setOp(ComplexNode.COMPLEX_AND);
        retQuery = converter.toLuceneQuery((QueryNode) complex2);
        term = retQuery.toString(ALL_FIELDS);
        LOGGER.fine("Complex AND term is " + term);
        assertTrue(term.equals("+title:or +origin:right"));

        AttrPlusTermNode rpn3 = new AttrPlusTermNode(root1);
        rpn3.setAttr(null, new Integer(GeoProfile.USE), new Integer(TITLE_NUM));
        rpn3.setTerm("nor");

        AttrPlusTermNode rpn4 = new AttrPlusTermNode(root2);
        rpn4.setAttr(null, new Integer(GeoProfile.USE), new Integer(ORIGIN_NUM));
        rpn4.setTerm("not");

        ComplexNode complex3 = new ComplexNode(comRoot, rpn3, rpn4);
        complex3.setOp(ComplexNode.COMPLEX_ANDNOT);
        retQuery = converter.toLuceneQuery((QueryNode) complex3);
        term = retQuery.toString(ALL_FIELDS);
        LOGGER.fine("Complex ANDNOT term is " + term);
        assertTrue(term.equals("+title:nor -origin:not"));

        ComplexNode uber = new ComplexNode(comRoot, complex, complex3);
        uber.setOp(ComplexNode.COMPLEX_AND);
        retQuery = converter.toLuceneQuery((QueryNode) uber);
        term = retQuery.toString(ALL_FIELDS);
        LOGGER.fine("Uber complex term is " + term);
        assertTrue(term.equals("+(title:or origin:right) "
                + "+(+title:nor -origin:not)"));
    }

    public void testNumbers() throws Exception {
        attrMap.setProperty("22", "westbc");

        String searchVal = "15";
        rpn2.setTerm(searchVal);

        String numConv = NumericField.numberToString(searchVal);
        rpn2.clearAttrs();
        setAttribute(rpn2, GeoProfile.USE, 22);
        setAttribute(rpn2, GeoProfile.RELATION, GeoProfile.LESS_THAN);

        Query retQuery = converter.toLuceneQuery((QueryNode) rpn2);
        String term = retQuery.toString("westbc");
        LOGGER.fine("Less than query is " + term);
        assertTrue(term.equals("{null-" + numConv + "}"));

        setAttribute(rpn2, GeoProfile.RELATION, GeoProfile.LESS_THAN_EQUAL);
        retQuery = converter.toLuceneQuery((QueryNode) rpn2);
        term = retQuery.toString("westbc");
        LOGGER.fine("Less than or eq query is " + term);
        assertTrue(term.equals("[null-" + numConv + "]"));

        setAttribute(rpn2, GeoProfile.RELATION, GeoProfile.GREATER_THAN_EQUAL);
        retQuery = converter.toLuceneQuery((QueryNode) rpn2);
        term = retQuery.toString("westbc");
        LOGGER.fine("Greater than or eq query is " + term);
        assertTrue(term.equals("[" + numConv + "-null]"));

        setAttribute(rpn2, GeoProfile.RELATION, GeoProfile.GREATER_THAN);
        retQuery = converter.toLuceneQuery((QueryNode) rpn2);
        term = retQuery.toString("westbc");
        LOGGER.fine("Greater than query is " + term);
        assertTrue(term.equals("{" + numConv + "-null}"));
    }

    public void testDates() throws Exception {
        attrMap.setProperty(DATE_NUM, DATE_NAME);
        converter = new RPNConverter(attrMap);
        rpn1.setTerm("1979");
        rpn1.clearAttrs();
        rpn1.setAttr(null, new Integer(GeoProfile.USE), new Integer(DATE_NUM));

        Query retQuery = converter.toLuceneQuery((QueryNode) rpn1);
        String term = retQuery.toString(DATE_NAME);
        LOGGER.fine("1st query is " + term);
        assertTrue(term.equals("1979*")); //prefix search is proper return.

        setAttribute(rpn1, GeoProfile.RELATION, GeoProfile.DURING_OR_AFTER);
        retQuery = converter.toLuceneQuery((QueryNode) rpn1);
        term = retQuery.toString(DATE_NAME);
        LOGGER.fine("During or After query is " + term);
        assertTrue(term.equals("[1979-null]"));

        setAttribute(rpn1, GeoProfile.RELATION, GeoProfile.AFTER);
        retQuery = converter.toLuceneQuery((QueryNode) rpn1);
        term = retQuery.toString(DATE_NAME);
        LOGGER.fine("After query is " + term);
        assertTrue(term.equals("{19799999-null}"));

        //trailing 9's so it doesn't match dates like 197902,
        //since that's not after 1979.  {} means exclusive.
        rpn1.setTerm("19550203 1998");
        setAttribute(rpn1, GeoProfile.RELATION, GeoProfile.DURING);
        retQuery = converter.toLuceneQuery((QueryNode) rpn1);
        term = retQuery.toString(DATE_NAME);
        LOGGER.fine("During query is " + term);
        assertTrue(term.equals("[19550203-19989999]"));

        //trailing 9's are so all dates within a year match.
        setAttribute(rpn1, GeoProfile.RELATION, GeoProfile.BEFORE);
        retQuery = converter.toLuceneQuery((QueryNode) rpn1);
        term = retQuery.toString(DATE_NAME);
        LOGGER.fine("Before query is " + term);
        assertTrue(term.equals("{null-19550203}"));

        setAttribute(rpn1, GeoProfile.RELATION, GeoProfile.BEFORE_OR_DURING);
        retQuery = converter.toLuceneQuery((QueryNode) rpn1);
        term = retQuery.toString(DATE_NAME);
        LOGGER.fine("Before or during query is " + term);
        assertTrue(term.equals("[null-19989999]"));
    }

    public void testBoundingQuery() throws Exception {
        attrMap.setProperty("58", "bounding");
        attrMap.setProperty(GeoProfile.Attribute.WESTBC, "westbc");
        attrMap.setProperty(GeoProfile.Attribute.NORTHBC, "northbc");
        attrMap.setProperty(GeoProfile.Attribute.SOUTHBC, "southbc");
        attrMap.setProperty(GeoProfile.Attribute.EASTBC, "eastbc");

        String searchVal = "25, -25, -25, 25";
        rpn2.setTerm(searchVal);

        String numConvPos = NumericField.numberToString("25");
        String numConvNeg = NumericField.numberToString("-25");
        rpn2.clearAttrs();
        setAttribute(rpn2, GeoProfile.USE, 58);

        Query retQuery = converter.toLuceneQuery((QueryNode) rpn2);
        String term = retQuery.toString(ALL_FIELDS);
        LOGGER.fine("Bounding query " + term);
        assertTrue(term.equals("+southbc:[null-" + numConvPos + "] +northbc:["
                + numConvNeg + "-null] +westbc:[null-" + numConvPos
                + "] +eastbc:[" + numConvNeg + "-null]"));
    }

    //TODO: test truncation
    private void setAttribute(AttrPlusTermNode node, int attr, int value) {
        node.setAttr(null, new Integer(attr), new Integer(value));
    }
}
