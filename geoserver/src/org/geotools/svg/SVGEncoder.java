package org.geotools.svg;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.*;
import org.geotools.feature.*;
import org.geotools.filter.*;
import org.vfny.geoserver.config.FeatureTypeConfig;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.logging.Logger;


/**
 * yes, I know. It is very rustic. Just that the SVG renderer thoes not meet my
 * needs, so I will try, in the near future, to create a kind of sax writer
 * for svg feature sets export, wich will be more elegant than this
 *
 * @version $Id: SVGEncoder.java,v 1.1.2.1 2003/11/16 11:27:37 groldan Exp $
 */
public class SVGEncoder
{
    /** DOCUMENT ME!  */
    private static final Logger LOGGER = Logger.getLogger("org.geotools.svg");

    /** DOCUMENT ME!  */
    private static final ProgressListener voidProgressListener = new ProgressListener()
        {
            public void progress(int value, int max)
            {
            }
        };

    /** DOCUMENT ME!  */
    private static final String SVG_HEADER =
        "<?xml version=\"1.0\" standalone=\"no\"?>\n\t"
        + "<!DOCTYPE svg \n\tPUBLIC \"-//W3C//DTD SVG 20001102//EN\" \n\t\"http://www.w3.org/TR/2000/CR-SVG-20001102/DTD/svg-20001102.dtd\">\n"
        + "<svg \n\tstroke=\"green\" \n\tfill=\"none\" \n\tstroke-width=\"0.1%\" \n\twidth=\"_width_\" \n\theight=\"_height_\" \n\tviewBox=\"_viewBox_\" \n\tpreserveAspectRatio=\"xMidYMid meet\">\n";

    /** DOCUMENT ME!  */
    private static final String SVG_FOOTER = "</svg>\n";

    /**
     * the referene space is an Envelope object wich is used to translate Y
     * coordinates to an SVG viewbox space. It is necessary due to the
     * different origin of Y coordinates in SVG space and in Coordinates space
     */
    private Envelope referenceSpace = null;
    private Feature currentFeature = null;
    private Geometry currentGeometry = null;
    private FeatureType featureType;

    /** DOCUMENT ME!  */
    DecimalFormat formatter;
    private ProgressListener progressListener = voidProgressListener;
    private Writer writer;

    /** DOCUMENT ME!  */
    int currentIndex = 0;

    /** DOCUMENT ME!  */
    int maxIndex = -1;

    private String width = "100%";

    private String height = "100%";

    /**
     * Creates a new SVGEncoder object.
     */
    public SVGEncoder()
    {
        formatter = new DecimalFormat();
        formatter.setGroupingSize(0);
        formatter.setMaximumFractionDigits(8);
        formatter.setMinimumFractionDigits(0);
    }

    public void setWidth(String width)
    {
      this.width = width;
    }

    public void setHeight(String height)
    {
      this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param numDigits DOCUMENT ME!
     */
    public void setMaximunFractionDigits(int numDigits)
    {
        formatter.setMaximumFractionDigits(numDigits);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaximunFractionDigits()
    {
        return formatter.getMaximumFractionDigits();
    }

    /**
     * DOCUMENT ME!
     *
     * @param numDigits DOCUMENT ME!
     */
    public void setMinimunFractionDigits(int numDigits)
    {
        formatter.setMinimumFractionDigits(numDigits);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinimunFractionDigits()
    {
        return formatter.getMinimumFractionDigits();
    }

    /**
     * DOCUMENT ME!
     *
     * @param env DOCUMENT ME!
     */
    public void setReferenceSpace(Envelope env)
    {
        this.referenceSpace = env;
    }

    /**
     * DOCUMENT ME!
     *
     * @param minx DOCUMENT ME!
     * @param miny DOCUMENT ME!
     * @param maxx DOCUMENT ME!
     * @param maxy DOCUMENT ME!
     */
    public void setReferenceSpace(double minx, double miny, double maxx,
        double maxy)
    {
        setReferenceSpace(new Envelope(minx, maxx, miny, maxy));
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void setProgressListener(ProgressListener listener)
    {
        this.progressListener = (listener == null) ? voidProgressListener
                                                   : listener;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coords DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeClosedPath(Coordinate[] coords) throws IOException
    {
        write("<path ");
        write("d=\"");
        writePathContent(coords);
        write("Z");
        write("\"/>\n");
    }

    //
    public void encode(final FeatureResults features, final Writer writer)
        throws IOException
    {
        FeatureResults[] results = { features };
        encode(null, results, writer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param layers the outermost SVG<code>g</code> element id wich will group
     *        all the features in the collection
     * @param results the features to be encoded as SVG vectors
     * @param writer SVG encoder output stream
     *
     * @throws IOException DOCUMENT ME!
     */
    public void encode(FeatureTypeConfig[] layers, FeatureResults[] results,
        Writer writer) throws IOException
    {
        this.writer = writer;

        long t = System.currentTimeMillis();

        ensureSVGSpace(results);

        checkProgressListenerNeed(results);

        writeHeader();

        writeDefs(layers);

        writeLayers(layers, results);

        write(SVG_FOOTER);
        this.writer.flush();
        t = System.currentTimeMillis() - t;
        LOGGER.info("SVG generado en " + t + " ms");
    }

    private void writeHeader()
        throws IOException
    {
      String viewBox = createViewBox();
      String header = SVG_HEADER.replaceAll("_viewBox_", viewBox);
      header = header.replaceAll("_width_", this.width);
      header = header.replaceAll("_height_", this.height);
    }

    private void writeLayers(FeatureTypeConfig[] layers,
                             FeatureResults[] results)
        throws IOException
    {
      int nLayers = results.length;
      int nConfigs = layers != null && layers.length >= nLayers? nLayers : 0;

      FeatureTypeConfig layerConfig;
      int defMaxDecimals = formatter.getMaximumFractionDigits();

      for (int i = 0; i < nLayers; i++)
      {
          if(nConfigs == nLayers)
          {
            formatter.setMaximumFractionDigits(layers[i].getNumDecimals());
          }else{
            formatter.setMaximumFractionDigits(defMaxDecimals);
          }

          FeatureReader featureReader = results[i].reader();
          FeatureType schema = featureReader.getFeatureType();
          String groupId = schema.getTypeName();

          write("<g id=\"" + groupId + "\" class=\"" + groupId + "\">\n");
          writeFeatures(featureReader);
          write("</g>\n");
      }
    }

    private void writeDefs(FeatureTypeConfig[] layers) throws IOException
    {
      if(layers == null)
      {
        LOGGER.warning("Can't write symbol definitions, no FeatureTypes passed");
        return;
      }
      int nLayers = layers.length;
      for(int i = 0; i < nLayers; i++)
      {
        Class geometryClass = layers[i].getSchema().getDefaultGeometry().getType();
        if(geometryClass == MultiPoint.class || geometryClass == Point.class)
        {
          writePointDefs();
          break;
        }
      }
    }

    private String createViewBox() {
      String viewBox = (long) getX(referenceSpace.getMinX()) + " "
          + (long) (getY(referenceSpace.getMinY())
          - referenceSpace.getHeight()) + " "
          + (long) referenceSpace.getWidth() + " "
          + (long) referenceSpace.getHeight();
      return viewBox;
    }

    private void checkProgressListenerNeed(FeatureResults[] results) {
      //if there is a progress listener, count the total number of features
      //so we can send progress events
      if (progressListener != voidProgressListener)
      {
          maxIndex = 0;
          int nLayers = results.length;

          for (int i = 0; i < nLayers; i++)
          {
              try
              {
                  maxIndex += results[i].getCount();
              }
              catch (IOException ex)
              {
                  LOGGER.warning("Can't compute total number of features: "
                      + ex.getMessage());
                  maxIndex = -1;

                  break;
              }
          }
      }
    }

    private void ensureSVGSpace(FeatureResults[] results)
        throws IOException {
      if (this.referenceSpace == null)
      {
          Envelope bounds = new Envelope();

          int nLayers = results.length;

          for (int i = 0; i < nLayers; i++)
          {
              Envelope layerBounds = results[i].getBounds();

              if (layerBounds == null)
                  throw new IOException(
                      "Can't obtain the feature result's bounds");
              bounds.expandToInclude(layerBounds);
          }

          LOGGER.fine("no explicit SVG space defined, using bounds " + bounds);
          setReferenceSpace(bounds);
      }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void openGroup() throws IOException
    {
        openGroup(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void openGroup(String id) throws IOException
    {
        write("<g");

        if (id != null)
        {
            write(" id=\"");
            write(id);
            write('\"');
        }

        write('>');
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws DataSourceException DOCUMENT ME!
     */
    void writeFeatures(FeatureReader reader) throws IOException
    {
        Feature ft;

        try
        {
            this.featureType = reader.getFeatureType();

            while (reader.hasNext())
            {
                ++currentIndex;
                ft = reader.next();
                progressListener.progress(currentIndex, maxIndex);
                writeGeometry(ft);
            }
        }
        catch (NoSuchElementException ex)
        {
            throw new DataSourceException(ex.getMessage(), ex);
        }
        catch (IllegalAttributeException ex)
        {
            throw new DataSourceException(ex.getMessage(), ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void closeGroup() throws IOException
    {
        closeGroup(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    void closeGroup(String id) throws IOException
    {
        write("</g>");

        if (id != null)
        {
            write("<!-- FIN DE ");
            write(id);
            write(" -->");
            newline();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void newline() throws IOException
    {
        writer.write('\n');
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void write(CharSequence s) throws IOException
    {
        writer.write(s.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void write(char c) throws IOException
    {
        writer.write(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void write(double d) throws IOException
    {
        writer.write(formatter.format(d));
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePointDefs() throws IOException
    {
        write(
            "<defs>\n\t<circle id='point' cx='0' cy='0' r='0.2%' fill='blue'/>\n</defs>\n");
    }

    /**
     * DOCUMENT ME!
     *
     * @param coords
     *
     * @throws IOException
     */
    private void writePathContent(Coordinate[] coords)
        throws IOException
    {
        write('M');

        Coordinate prev = coords[0];
        Coordinate curr = null;
        write(getX(prev.x));
        write(' ');
        write(getY(prev.y));

        int nCoords = coords.length;

        write('l');

        for (int i = 1; i < nCoords; i++)
        {
            curr = coords[i];
            write((getX(curr.x) - getX(prev.x)));
            write(' ');
            write(getY(curr.y) - getY(prev.y));
            write(' ');
            prev = curr;
        }
    }

    //

    /**
     * por ahora solo para pligonos...
     *
     * @param ft
     *
     * @throws IOException si algo ocurre escribiendo a <code>out</code>
     */
    private void writeGeometry(Feature ft) throws IOException
    {
        Geometry geom = ft.getDefaultGeometry();

        if ((geom == null) || geom.isEmpty())
        {
            return;
        }

        currentFeature = ft;
        currentGeometry = geom;

        if (geom instanceof MultiPolygon)
        {
            MultiPolygon mp = (MultiPolygon) geom;
            writeMultiPoly(mp);
        }
        else if (geom instanceof LineString)
        {
            LineString l = (LineString) geom;
            writePolyLine(l.getCoordinates());
        }
        else if (geom instanceof MultiLineString)
        {
            writeMultiLineString((MultiLineString) geom);
        }
        else if (geom instanceof MultiPoint)
        {
            writeMultiPoint((MultiPoint) geom);
        }
        else if (geom instanceof com.vividsolutions.jts.geom.Point)
        {
            writePoint((com.vividsolutions.jts.geom.Point) geom);
        }
    }

    //
    private void writeAttributes() throws IOException
    {
        if (currentGeometry != null)
        {
            Envelope env = currentGeometry.getEnvelopeInternal();
            write("bounds=\"");
            write(getX(env.getMinX()));
            write(' ');
            write(getY(env.getMinY()));
            write(' ');
            write(env.getWidth());
            write(' ');
            write(env.getHeight());
            write("\" ");
        }

        int numAtts = currentFeature.getNumberOfAttributes();
        Object att;

        for (int i = 0; i < numAtts; i++)
        {
            att = currentFeature.getAttribute(i);

            if (!(att instanceof Geometry))
            {
                writeAttribute(featureType.getAttributeType(i).getName(), att);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param attName DOCUMENT ME!
     * @param attValue DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeAttribute(String attName, Object attValue)
        throws IOException
    {
        write(attName);
        write("=\"");

        if (attValue != null)
            write(java.net.URLEncoder.encode(String.valueOf(attValue)));

        write("\" ");
    }

    /**
     * DOCUMENT ME!
     *
     * @param mp DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeMultiPoint(MultiPoint mp) throws IOException
    {
        write("<g ");
        writeId();
        writeAttributes();
        write('>');

        int npoints = mp.getNumGeometries();

        for (int i = 0; i < npoints; i++)
        {
            write('\t');
            writePoint((Point) mp.getGeometryN(i));
        }

        write("</g>");
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePoint(com.vividsolutions.jts.geom.Point p)
        throws IOException
    {
        write("<use xlink:href=\"#point\" x=\"");
        write(getX(p.getX()));
        write("\" y=\"");
        write(getY(p.getY()));
        write("\" ");
        writeAttributes();
        write("/>");
        newline();
    }

    /**
     * DOCUMENT ME!
     *
     * @param coords DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePolyLine(Coordinate[] coords) throws IOException
    {
        write("<path fill=\"none\" ");
        writeAttributes();
        write(" d=\"");
        writePathContent(coords);
        write("\"/>");
        newline();
    }

    /**
     * DOCUMENT ME!
     *
     * @param mls DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeMultiLineString(MultiLineString mls)
        throws IOException
    {
        write("<path fill=\"none\" ");
        writeAttributes();
        write(" d=\"");

        int n = mls.getNumGeometries();

        for (int i = 0; i < n; i++)
        {
            writePathContent(mls.getGeometryN(i).getCoordinates());
        }

        write("\"/>");
        newline();
    }

    //

    /**
     * DOCUMENT ME!
     *
     * @param mpoly
     *
     * @throws IOException
     */
    private void writeMultiPoly(com.vividsolutions.jts.geom.MultiPolygon mpoly)
        throws IOException
    {
        int n = mpoly.getNumGeometries();
        com.vividsolutions.jts.geom.Polygon poly;
        write("<path ");
        writeId();
        writeAttributes();
        write("d=\"");

        for (int i = 0; i < n; i++)
        {
            poly = (com.vividsolutions.jts.geom.Polygon) mpoly.getGeometryN(i);
            writePolyContent(poly);
        }

        write("\"/>");
        newline();

        //writeData(out);
        //write("</path>\n");
    }

    //
    private void writeId() throws IOException
    {
        write("id=\"");
        write(currentFeature.getID());
        write("\" ");
    }

    /**
     * DOCUMENT ME!
     *
     * @param poly
     *
     * @throws IOException
     */
    private void writePoly(com.vividsolutions.jts.geom.Polygon poly)
        throws IOException
    {
        LineString shell = poly.getExteriorRing();
        int nHoles = poly.getNumInteriorRing();

        writeClosedPath(shell.getCoordinates());

        for (int i = 0; i < nHoles; i++)
            writeClosedPath(poly.getInteriorRingN(i).getCoordinates());
    }

    /**
     * DOCUMENT ME!
     *
     * @param poly DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writePolyContent(com.vividsolutions.jts.geom.Polygon poly)
        throws IOException
    {
        LineString shell = poly.getExteriorRing();
        int nHoles = poly.getNumInteriorRing();

        writePathContent(shell.getCoordinates());
        write('Z');

        for (int i = 0; i < nHoles; i++)
        {
            writePathContent(poly.getInteriorRingN(i).getCoordinates());
            write('Z');
        }
    }

    /**
     * if a reference space has been set, returns a translated Y coordinate
     * wich is inverted based on the height of such a reference space,
     * otherwise just returns <code>y</code>
     *
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getY(double y)
    {
        return (referenceSpace.getMaxY() - y) + referenceSpace.getMinY();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private double getX(double x)
    {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param argv DOCUMENT ME!
     */
    public static void main(String[] argv)
    {
        String group = "entidades";
        String layer = "MG_ENTIDADES";
        String[] fields = { "SHAPE", "COD_MUNI", "COD_ENTI", "NOM_ENTI" };
        FilterFactory ff = FilterFactory.createFilterFactory();

        SVGEncoder encoder = new SVGEncoder();
        encoder.setMaximunFractionDigits(2);
        encoder.setMinimunFractionDigits(0);

        encoder.setProgressListener(new ProgressListener()
            {
                public void progress(int value, int max)
                {
                    System.out.print('.');
                }
            });

        try
        {
            DataStore store = getDataStore();
            FeatureSource fSource = store.getFeatureSource(layer);
            FeatureType schema = fSource.getSchema();

            CompareFilter anoEfecto = ff.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
            Expression field = ff.createAttributeExpression(schema, "ANO_EFECTO");
            Expression literal = ff.createLiteralExpression(new Integer(1996));
            anoEfecto.addLeftValue(field);
            anoEfecto.addRightValue(literal);

            CompareFilter codMuni = ff.createCompareFilter(AbstractFilter.COMPARE_EQUALS);
            field = ff.createAttributeExpression(schema, "COD_MUNI");
            literal = ff.createLiteralExpression(new Integer(20));
            codMuni.addLeftValue(field);
            codMuni.addRightValue(literal);

            LogicFilter and = ff.createLogicFilter(AbstractFilter.LOGIC_AND);
            and.addFilter(anoEfecto);
            and.addFilter(codMuni);

            Query query = new DefaultQuery(and, fields);

            Envelope referenceBounds = store.getFeatureSource("BIZKAIA")
                                            .getBounds();
            FeatureResults features = fSource.getFeatures(query);

            Writer out = new FileWriter("c:\\" + layer + ".svg");
            out = new BufferedWriter(out, 1024 * 1024);
            encoder.setReferenceSpace(referenceBounds);
            encoder.encode(features, out);
            out.flush();
            out.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private static DataStore getDataStore() throws IOException
    {
        DataStore ds = null;
        Map params = new HashMap();
        params.put("dbtype", "arcsde");
        params.put("server", "localhost");
        params.put("port", "5151");
        params.put("instance", "sde");
        params.put("user", "sde");
        params.put("password", "carto");
        ds = new org.geotools.data.sde.SdeDataStoreFactory().createDataStore(params);

        return ds;
    }
}
