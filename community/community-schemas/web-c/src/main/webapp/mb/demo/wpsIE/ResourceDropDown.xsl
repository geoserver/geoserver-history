<?xml version="1.0" encoding="ISO-8859-1"?>

<!--
Description: parses an OGC context document to generate an array of DHTML layers.
Author:      adair
Licence:     LGPL as specified in http://www.gnu.org/copyleft/lesser.html .

$Id$
$Name:  $
-->

<xsl:stylesheet version="1.0" 
    xmlns:wmc="http://www.opengis.net/context" 
    xmlns:ows="http://www.opengis.net/ows"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:xlink="http://www.w3.org/1999/xlink" exclude-result-prefixes="wmc xlink">

  <xsl:output method="xml"/>
  <xsl:strip-space elements="*"/>

  <!-- The coordinates of the DHTML Layer on the HTML page -->
  <xsl:param name="modelId"/>
  <xsl:param name="widgetId"/>
  <xsl:param name="selectName">resourceSelect</xsl:param>

  <!-- template rule matching source root element -->
  <xsl:template match="/wmc:OWSContext">
      <select name="{$selectName}" onchange="javascript:config.objects.{$widgetId}.selectResource(this.options[this.selectedIndex]);" >
        <option value="">Select a map resource</option>
        <xsl:apply-templates select="wmc:ResourceList/*"/>
      </select>
  </xsl:template>
  
  <!-- these handled outside of the stylesheet -->
  <xsl:template match="wmc:Coverage | wmc:FeatureType | wmc:Layer">
    <option>
      <xsl:attribute name="resourceType"><xsl:value-of select="wmc:Server/@service"/></xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="wmc:Name"/></xsl:attribute>
      <b><xsl:value-of select="wmc:Server/@service"/></b> - <xsl:value-of select="wmc:Title"/>
    </option>
  </xsl:template>
  
</xsl:stylesheet>
