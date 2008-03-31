package org.geoserver.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataSourceException;
import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;

import au.com.bytecode.opencsv.CSVReader;

/**
 * A quick and dirty replacement for a datastore. It basically only offers pure
 * reading of a CSV file specified in the following format:
 * 
 * <pre>
 * &quot;long name one&quot;,&quot;long name two&quot;,...
 * colname1,colname2,...
 * type1,type2
 * data11,data12,...
 * data21,data22,...
 * </pre>
 * 
 * Where each data item is formatted as "string" if it's a string, or as plain
 * number otherwise (we don't support other data types at the moment). Types can
 * be string, double, int
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class CsvFileReader {
    /**
     * Maps type definitions to proper java classes
     */
    private static final Map<String, Class> TYPE_MAP = new HashMap<String, Class>() {
        {
            put("float", Double.class);
            put("integer", Integer.class);
            put("string", String.class);
        }

    };

    FeatureType ft;

    File csvFile;

    String[] descriptions;

    public CsvFileReader(File csvFile) throws IOException {
        this.csvFile = csvFile;
        parseHeader();
    }

    /**
     * Returns the feature type
     * 
     * @return
     */
    FeatureType getFeatureType() {
        return ft;
    }

    /**
     * Provides a description of the attribute given its position in the feature
     * type
     * 
     * @param attributeName
     * @return
     */
    public String getDescription(int index) {
        return descriptions[index];
    }

    /**
     * Reads the three line header and builds the feature type and associated
     * descriptions
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    void parseHeader() throws IOException {
        FileReader fileReader = null;
        CSVReader csv = null;
        try {
            fileReader = new FileReader(csvFile);
            csv = new CSVReader(fileReader);
            String[] descriptions = csv.readNext();
            String[] names = csv.readNext();
            String[] types = csv.readNext();

            // check that we have at least three header lines
            if (descriptions == null || names == null || types == null)
                throw new IOException(
                        "The file should have at least three header lines, "
                                + "long names, short names, and types");

            // check length is the same
            if (descriptions.length != names.length)
                throw new IOException("The descriptions row has "
                        + descriptions.length
                        + " items whilst the names one has " + names.length
                        + ", they should be equals");
            if (names.length != types.length)
                throw new IOException("The names row has " + names.length
                        + " items whilst the types one has " + types.length
                        + ", they should be equals");

            // second sanity check, names as ok
            for (String name : names) {
                if (!name.matches("\\w*"))
                    throw new IOException("The name " + name
                            + " is invalid, names can contain only chars, "
                            + "digits and the underscore (a-z,A-Z,_,0-9)");
            }

            // store the descriptions for later use
            this.descriptions = descriptions;

            // lets go build the feature type
            AttributeType[] attributes = new AttributeType[names.length];
            for (int i = 0; i < names.length; i++) {
                Class clazz = TYPE_MAP.get(types[i].toLowerCase());
                if (clazz == null)
                    throw new IOException("Unknown type " + types[i]
                            + " valid types are " + TYPE_MAP.keySet());
                attributes[i] = AttributeTypeFactory.newAttributeType(names[i],
                        clazz);
            }
            ft = FeatureTypes.newFeatureType(attributes, csvFile.getName());
        } catch (SchemaException e) {
            throw new DataSourceException("An unexpected error occurred "
                    + "when parsing the csv file headers", e);
        } finally {
            if (csv != null)
                csv.close();
            else if (fileReader != null)
                fileReader.close();
        }
    }

    /**
     * Returns a feature iterator that can be used to read the features out of
     * the csv file
     * 
     * @return
     */
    public FeatureIterator getFeatures() throws IOException {
        FileReader fileReader = null;
        CSVReader csv = null;
        try {
            fileReader = new FileReader(csvFile);
            csv = new CSVReader(fileReader);

            // skip the header lines
            csv.readNext();
            csv.readNext();
            csv.readNext();

            // build the iterator
            FeatureIterator iterator = new CsvFeatureIterator(ft, csv);

            // make sure these aren't close if we got up to this point
            // 
            csv = null;
            fileReader = null;

            return iterator;
        } finally {
            if (csv != null)
                csv.close();
            else if (fileReader != null)
                fileReader.close();
        }
    }
}
