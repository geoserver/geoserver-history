<?xml version="1.0"?>
<xsl:stylesheet xmlns:wmc="http://www.opengis.net/context" xmlns:mb="http://mapbuilder.sourceforge.net/mapbuilder" xmlns:param="http;//www.opengis.net/param" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"><xsl:output method="xml"/><xsl:strip-space elements="*"/><xsl:param name="bBoxMinX"/><xsl:param name="bBoxMinY"/><xsl:param name="bBoxMaxX"/><xsl:param name="bBoxMaxY"/><xsl:param name="width"/><xsl:param name="height"/><xsl:param name="srs"/><xsl:param name="version"/><xsl:template match="wmc:Coverage"><xsl:variable name="bbox"><xsl:value-of select="$bBoxMinX"/>,<xsl:value-of select="$bBoxMinY"/>,
      <xsl:value-of select="$bBoxMaxX"/>,<xsl:value-of select="$bBoxMaxY"/></xsl:variable><GetCoverage><mb:QueryString><xsl:variable name="src">    
      VERSION=<xsl:value-of select="$version"/>
 &amp;REQUEST=GetCoverage
 &amp;SERVICE=WCS
     &amp;CRS=<xsl:value-of select="$srs"/>
    &amp;BBOX=<xsl:value-of select="$bbox"/>
   &amp;WIDTH=<xsl:value-of select="$width"/>
  &amp;HEIGHT=<xsl:value-of select="$height"/>
&amp;COVERAGE=<xsl:value-of select="wmc:Name"/>
  &amp;FORMAT=<xsl:value-of select="wmc:FormatList/wmc:Format[@current='1']"/><xsl:for-each select="wmc:DimensionList/wmc:Dimension">
&amp;<xsl:value-of select="@name"/>=<xsl:value-of select="."/></xsl:for-each><xsl:for-each select="wmc:ParameterList/wmc:Parameter">
&amp;<xsl:value-of select="param:kvp/@name"/>=<xsl:value-of select="param:kvp/@value"/></xsl:for-each></xsl:variable><xsl:value-of select="translate(normalize-space($src),' ', '' )" disable-output-escaping="no"/></mb:QueryString></GetCoverage></xsl:template><xsl:template match="text()|@*"/></xsl:stylesheet>
