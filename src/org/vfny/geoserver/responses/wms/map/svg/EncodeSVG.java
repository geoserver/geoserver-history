package org.vfny.geoserver.responses.wms.map.svg;

import com.vividsolutions.jts.geom.*;
import org.geotools.data.*;
import org.geotools.feature.*;
import org.geotools.filter.*;
import org.geotools.styling.*;
import org.vfny.geoserver.global.*;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 * @author Gabriel Roldán
 * @version $Id: EncodeSVG.java,v 1.1 2004/03/14 16:15:22 groldan Exp $
 */

public class EncodeSVG
{
  /** DOCUMENT ME! */
  private static final Logger LOGGER = Logger.getLogger(
          "org.vfny.geoserver.responses.wms.map");

  private EncoderConfig config;

  private SVGWriter writer;

  private boolean abortProcess;

  public EncodeSVG(EncoderConfig encoderConfig)
  {
    config = encoderConfig;
  }

  /**
   * DOCUMENT ME!
   */
  public void abort() {
      abortProcess = true;
  }

  public void encode(final OutputStream out) throws IOException {
      Envelope env = config.getReferenceSpace();
      this.writer = new SVGWriter(out, config);
      writer.setMinCoordDistance(config.getMinCoordDistance());

      abortProcess = false;

      long t = System.currentTimeMillis();

      try {
        writeHeader();

        writeLayers();

        if (config.isWriteHeader()) {
          writer.write(EncoderConfig.SVG_FOOTER);
        }

        this.writer.flush();
        t = System.currentTimeMillis() - t;
        LOGGER.info("SVG generated in " + t + " ms");
      }catch (IOException ioe) {
        if(abortProcess)
        {
          LOGGER.fine("SVG encoding aborted");
          return;
        }else
          throw ioe;
      }catch (AbortedException ex) {
        return;
      }

  }

  public String createViewBox() {
      Envelope referenceSpace = config.getReferenceSpace();
      String viewBox = (long) writer.getX(referenceSpace.getMinX()) + " "
          + (long) (writer.getY(referenceSpace.getMinY())
          - referenceSpace.getHeight()) + " "
          + (long) referenceSpace.getWidth() + " "
          + (long) referenceSpace.getHeight();

      return viewBox;
  }



  private void writeHeader() throws IOException {
      if (config.isWriteHeader()) {
          String viewBox = createViewBox();
          String header = EncoderConfig.SVG_HEADER.replaceAll("_viewBox_", viewBox);
          header = header.replaceAll("_width_", String.valueOf(config.getMapWidth()));
          header = header.replaceAll("_height_",String.valueOf(config.getMapHeight()));
          writer.write(header);
      }
  }

  private void writeDefs(FeatureTypeInfo layer)
      throws IOException
  {
          GeometryAttributeType gtype = layer.getFeatureType().getDefaultGeometry();
          Class geometryClass = gtype.getType();

          if ((geometryClass == MultiPoint.class)
                  || (geometryClass == Point.class)) {
              writePointDefs();
          }
  }

  /**
   * DOCUMENT ME!
   *
   * @throws IOException DOCUMENT ME!
   */
  private void writePointDefs() throws IOException {
      writer.write(
          "<defs>\n\t<circle id='point' cx='0' cy='0' r='0.02%' fill='blue'/>\n</defs>\n");
  }

  private void writeLayers()
      throws IOException, AbortedException
  {
      FeatureTypeInfo[] layers = config.getLayers();
      FeatureResults[] results = config.getResults();
      Style []styles = config.getStyles();

      int nLayers = results.length;
      FeatureTypeInfo layerInfo = null;
      int defMaxDecimals = writer.getMaximunFractionDigits();

      for (int i = 0; i < nLayers; i++) {
          layerInfo = layers[i];
          writer.setMaximunFractionDigits(layerInfo.getNumDecimals());

          FeatureReader featureReader = null;

          try {
              LOGGER.fine("obtaining FeatureReader for "
                  + layerInfo.getName(true));
              featureReader = results[i].reader();
              LOGGER.fine("got FeatureReader, now writing");

              String groupId = null;
              String styleName = null;

              groupId = layerInfo.getName(true);
              if (styles != null) {
                  styleName = styles[i].getName();
              }

              writer.write("<g id=\"" + groupId + "\"");

              if(!styleName.startsWith("#"))
                writer.write(" class=\"" + styleName + "\"");

              writer.write(">\n");

              if(config.isWriteHeader())
                writeDefs(layerInfo);

              writeFeatures(featureReader, styleName);
              writer.write("</g>\n");
          } catch (IOException ex) {
              throw ex;
          } catch (AbortedException ae) {
              LOGGER.info("process aborted: " + ae.getMessage());
              throw ae;
          } catch (Throwable t) {
              LOGGER.warning("UNCAUGHT exception: " + t.getMessage());

              IOException ioe = new IOException("UNCAUGHT exception: "
                      + t.getMessage());
              ioe.setStackTrace(t.getStackTrace());
              throw ioe;
          } finally {
              if (featureReader != null) {
                  featureReader.close();
              }
          }
      }
  }

  private void writeFeatures(FeatureReader reader, String style)
      throws IOException, AbortedException {
      Feature ft;

      try {
          FeatureType featureType = reader.getFeatureType();
          Class gtype = featureType.getDefaultGeometry().getType();

          boolean doCollect = config.isCollectGeometries() &&
                gtype != Point.class && gtype != MultiPoint.class;

          writer.setGeometryType(gtype);

          writer.setPointsAsCircles("#circle".equals(style));

          if(style != null && !"#circle".equals(style) && style.startsWith("#"))
            style = style.substring(1);
          else
            style = null;

          writer.setAttributeStyle(style);

          if(doCollect)
          {
            writer.write("<path ");
            writer.write("d=\"");
          }

          while (reader.hasNext()) {
              if (abortProcess) {
                  throw new AbortedException("writing features");
              }

              ft = reader.next();
              writer.writeFeature(ft);
              ft = null;
          }

          if(doCollect)
          {
            writer.write("\"/>\n");
          }

          LOGGER.fine("encoded " + featureType.getTypeName());
      } catch (NoSuchElementException ex) {
          throw new DataSourceException(ex.getMessage(), ex);
      } catch (IllegalAttributeException ex) {
          throw new DataSourceException(ex.getMessage(), ex);
      }
  }
}