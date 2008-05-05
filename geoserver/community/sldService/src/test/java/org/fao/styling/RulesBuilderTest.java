package org.fao.styling;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.data.DataStore;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.styling.Rule;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

public class RulesBuilderTest extends TestCase {
    DataStore store;
    private RulesBuilder builder;

    @Override
    protected void setUp() throws Exception {
        store = new PropertyDataStore(new File("./src/test/resources/data"));
        builder = new RulesBuilder();
    }
    
    public void testUniqueId() throws Exception  {
        FeatureCollection fc = store.getFeatureSource("TestPoly").getFeatures();
        List<Rule> rules = builder.uniqueIntervalClassification(fc, "id");
        assertEquals(8, rules.size());
        for (Rule rule : rules) {
            final Filter filter = rule.getFilter();
            assertTrue(filter instanceof PropertyIsEqualTo);
            final Expression propName = ((PropertyIsEqualTo) filter).getExpression1();
            assertTrue(propName instanceof PropertyName);
            assertEquals("id", ((PropertyName) propName).getPropertyName());
        }
    }
    
    public void testUniqueClass() throws Exception  {
        FeatureCollection fc = store.getFeatureSource("TestPoly").getFeatures();
        List<Rule> rules = builder.uniqueIntervalClassification(fc, "class");
        assertEquals(3, rules.size());
        Set<String> values = new HashSet<String>();
        values.add("one");
        values.add("two");
        values.add("three");
        for (Rule rule : rules) {
            final Filter filter = rule.getFilter();
            assertTrue(filter instanceof PropertyIsEqualTo);
            final Expression propName = ((PropertyIsEqualTo) filter).getExpression1();
            final Expression value = ((PropertyIsEqualTo) filter).getExpression2();
            assertTrue(propName instanceof PropertyName);
            assertEquals("class", ((PropertyName) propName).getPropertyName());
            assertTrue(values.contains((value.evaluate(null, String.class))));
        }
    }
    
    public void testEqualInterval() throws Exception  {
        FeatureCollection fc = store.getFeatureSource("TestPoly").getFeatures();
        List<Rule> rules = builder.equalIntervalClassification(fc, "population", 2);
        assertEquals(2, rules.size());
        
        // check first rule
        Rule r1 = rules.get(0);
        Filter f1 = r1.getFilter();
        assertTrue(f1 instanceof PropertyIsLessThanOrEqualTo);
        assertEquals(100.0, ((PropertyIsLessThanOrEqualTo) f1).getExpression2().evaluate(null));
        
        // check second rule
        Rule r2 = rules.get(1);
        Filter f2 = r2.getFilter();
        assertTrue(f2 instanceof PropertyIsGreaterThan);
        assertEquals(100.0, ((PropertyIsGreaterThan) f2).getExpression2().evaluate(null));
    }
    
    public void testQuantileOverboard() throws Exception  {
        FeatureCollection fc = store.getFeatureSource("TestPoly").getFeatures();
        List<Rule> rules = builder.quantileClassification(fc, "population", 10);
        // there are just 8 values, so it does not make sense to have 10 rules
        assertEquals(8, rules.size());
    }
    
    
}
