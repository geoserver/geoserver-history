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
 * @version $Id: SVGEncoder.java,v 1.1.2.2 2003/11/19 18:26:00 groldan Exp $
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
        + "<svg \n\tstroke=\"green\" \n\tfill=\"cyan\" \n\tstroke-width=\"0.001%\" \n\twidth=\"_width_\" \n\theight=\"_height_\" \n\tviewBox=\"_viewBox_\" \n\tpreserveAspectRatio=\"xMidYMid meet\">\n";

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

    private SVGWriter writer;

    private String width = "100%";

    private String height = "100%";

    /** a factor for wich the referenceSpace will be divided to
     * to obtain the minimun distance beteen encoded points
     **/
    private double minCoordDistance = 0;

    /**
     * Creates a new SVGEncoder object.
     */
    public SVGEncoder()
    {
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
     * @param env DOCUMENT ME!
     */
    public void setReferenceSpace(Envelope env)
    {
        setReferenceSpace(env, 5000);
    }

    public void setReferenceSpace(Envelope env, float blurFactor)
    {
        this.referenceSpace = env;
        if(blurFactor > 0)
        {
          double maxDimension = Math.max(env.getWidth(), env.getHeight());
          this.minCoordDistance = maxDimension / blurFactor;
        }else{
          this.minCoordDistance = env.getWidth() + env.getHeight();
        }
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
     * @param coords DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    private void writeClosedPath(Coordinate[] coords) throws IOException
    {
        writer.write("<path ");
        writer.write("d=\"");
        writePathContent(coords);
        writer.write("Z");
        writer.write("\"/>\n");
    }

    //
    public void encode(final FeatureResults features, final OutputStream out)
        throws IOException
    {
        FeatureResults[] results = { features };
        encode(null, results, out);
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
    public void encode(final FeatureTypeConfig[] layers,
                       final FeatureResults[] results,
                       final OutputStream out) throws IOException
    {
        this.writer = new SVGWriter(out);

        long t = System.currentTimeMillis();

        ensureSVGSpace(results);

        writeHeader();

        writeDefs(layers);

        writeLayers(layers, results);

        writer.write(SVG_FOOTER);
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
      writer.write(header);
    }

    private void writeLayers(FeatureTypeConfig[] layers,
                             FeatureResults[] results)
        throws IOException
    {
      int nLayers = results.length;
      int nConfigs = layers != null && layers.length >= nLayers? nLayers : 0;

      FeatureTypeConfig layerConfig;
      int defMaxDecimals = writer.getMaximunFractionDigits();

      for (int i = 0; i < nLayers; i++)
      {
          if(nConfigs == nLayers)
          {
            writer.setMaximunFractionDigits(layers[i].getNumDecimals());
          }else{
            writer.setMaximunFractionDigits(defMaxDecimals);
          }

          FeatureReader featureReader = null;
          try {
            LOGGER.fine("obtaining FeatureReader for " + results[i].getSchema().getTypeName());
            featureReader = results[i].reader();
            LOGGER.fine("got FeatureReader, now writing");
            FeatureType schema = featureReader.getFeatureType();
            String groupId = schema.getTypeName();

            writer.write("<g id=\"" + groupId + "\" class=\"" + groupId + "\">\n");
            writeFeatures(featureReader);
            writer.write("</g>\n");
          }catch (IOException ex) {
            throw ex;
          }finally{
            if(featureReader != null)
              featureReader.close();
          }
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
                ft = reader.next();
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
    private void writePointDefs() throws IOException
    {
        writer.write(
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
        writer.write('M');

        Coordinate prev = coords[0];
        Coordinate curr = null;
        writer.write(getX(prev.x));
        writer.write(' ');
        writer.write(getY(prev.y));

        int nCoords = coords.length;

        writer.write('l');
        int skipCount = 0;

        for (int i = 1; i < nCoords; i++)
        {
            curr = coords[i];
            //let at least 3 points in case it is a polygon
            if(i > 3 && prev.distance(curr) <= minCoordDistance)
            {
              ++skipCount;
              continue;
            }
            writer.write((getX(curr.x) - getX(prev.x)));
            writer.write(' ');
            writer.write(getY(curr.y) - getY(prev.y));
            writer.write(' ');
            prev = curr;
        }
        System.out.println("skipped " + skipCount + " of " + nCoords);
    }

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

    private void writeAttributes() throws IOException
    {
        if (currentGeometry != null)
        {
            Envelope env = currentGeometry.getEnvelopeInternal();
            writer.write("bounds=\"");
            writer.write(getX(env.getMinX()));
            writer.write(" ");
            writer.write(getY(env.getMinY()));
            writer.write(" ");
            writer.write(env.getWidth());
            writer.write(" ");
            writer.write(env.getHeight());
            writer.write("\" ");
        }

        int numAtts = currentFeature.getNumberOfAttributes();
        Object att;

        for (int i = 0; i < numAtts; i++)
        {
            att = currentFeature.getAttribute(i);

            if (!(att instanceof Geometry))
            {
                writer.writeAttribute(featureType.getAttributeType(i).getName(), att);
            }
        }
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
        writer.write("<g ");
        writeId();
        writeAttributes();
        writer.write(">\n");

        int npoints = mp.getNumGeometries();

        for (int i = 0; i < npoints; i++)
        {
            writer.write("\t");
            writePoint((Point) mp.getGeometryN(i));
        }

        writer.write("</g>");
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
        writer.write("<use xlink:href=\"#point\" x=\"");
        writer.write(getX(p.getX()));
        writer.write("\" y=\"");
        writer.write(getY(p.getY()));
        writer.write("\" ");
        writeAttributes();
        writer.write("/>");
        writer.newline();
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
        writer.write("<path fill=\"none\" ");
        writeAttributes();
        writer.write(" d=\"");
        writePathContent(coords);
        writer.write("\"/>");
        writer.newline();
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
        writer.write("<path fill=\"none\" ");
        writeAttributes();
        writer.write(" d=\"");

        int n = mls.getNumGeometries();

        for (int i = 0; i < n; i++)
        {
            writePathContent(mls.getGeometryN(i).getCoordinates());
        }

        writer.write("\"/>");
        writer.newline();
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
        writer.write("<path ");
        writeId();
        writeAttributes();
        writer.write("d=\"");

        for (int i = 0; i < n; i++)
        {
            poly = (com.vividsolutions.jts.geom.Polygon) mpoly.getGeometryN(i);
            writePolyContent(poly);
        }

        writer.write("\"/>");
        writer.newline();

        //writeData(out);
        //write("</path>\n");
    }

    //
    private void writeId() throws IOException
    {
        writer.write("id=\"");
        writer.write(currentFeature.getID());
        writer.write("\" ");
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
        writer.write("Z");

        for (int i = 0; i < nHoles; i++)
        {
            writePathContent(poly.getInteriorRingN(i).getCoordinates());
            writer.write("Z");
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

}
