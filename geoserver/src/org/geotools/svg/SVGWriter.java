package org.geotools.svg;

import com.vividsolutions.jts.geom.*;
import org.geotools.feature.*;
import java.io.*;
import java.text.*;
import java.util.Locale;

/**
 * @author Gabriel Roldán
 * @version $Id: SVGWriter.java,v 1.1.2.1 2003/11/19 18:26:00 groldan Exp $
 */

public class SVGWriter extends OutputStreamWriter
{
  private static DecimalFormatSymbols decimalSymbols =
      new DecimalFormatSymbols(new Locale("en", "US"));
  static{
    decimalSymbols.setDecimalSeparator('.');
  }

  /** DOCUMENT ME!  */
  private DecimalFormat formatter;

  public SVGWriter(OutputStream out)
  {
    super(out);
    formatter = new DecimalFormat();
    formatter.setDecimalFormatSymbols(decimalSymbols);
    //do not group
    formatter.setGroupingSize(0);
    //do not show decimal separator if it is not needed
    formatter.setDecimalSeparatorAlwaysShown(false);
    formatter.setDecimalFormatSymbols(null);
    //set default number of fraction digits
    formatter.setMaximumFractionDigits(5);
    //minimun fraction digits to 0 so they get not rendered if not needed
    formatter.setMinimumFractionDigits(0);
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
   * @param d DOCUMENT ME!
   *
   * @throws IOException DOCUMENT ME!
   */
  public void write(double d) throws IOException
  {
      write(formatter.format(d));
  }

  public void write(char c) throws IOException
  {
    super.write(c);
  }

  /**
   * DOCUMENT ME!
   *
   * @throws IOException DOCUMENT ME!
   */
  public void newline() throws IOException
  {
      super.write('\n');
  }

  /**
   * DOCUMENT ME!
   *
   * @param attName DOCUMENT ME!
   * @param attValue DOCUMENT ME!
   *
   * @throws IOException DOCUMENT ME!
   */
  public void writeAttribute(String attName, Object attValue)
      throws IOException
  {
      write(attName);
      write("=\"");

      if (attValue != null)
          write(java.net.URLEncoder.encode(String.valueOf(attValue)));
      write("\" ");
  }


}