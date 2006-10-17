<?xml version="1.0" encoding="ISO-8859-1"?>

<!--
Description: parses an OGC context document to generate an array of DHTML layers.
Author:      adair
Licence:     LGPL as specified in http://www.gnu.org/copyleft/lesser.html .

$Id$
$Name:  $
-->

<xsl:stylesheet version="1.0" 
    xmlns:wps="http://www.opengis.net/wps"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
		xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:ows="http://www.opengis.net/ows"
		xmlns:gml="http://www.opengis.net/gml"
    xmlns:xlink="http://www.w3.org/1999/xlink">

  <xsl:output method="xml" omit-xml-declaration="no" encoding="utf-8" indent="yes"/>

  <!-- The coordinates of the DHTML Layer on the HTML page -->
  <xsl:param name="modelId"/>
  <xsl:param name="modelUrl"/>
  <xsl:param name="widgetId"/>
  
  <!-- template rule matching source root element -->
  <xsl:template match="/wps:Capabilities">
    <div>
      <p class="section">
        <xsl:value-of select="ows:ServiceIdentification/ows:Title"/>
      </p>
      <p style="text-align:right;font:70%;margin:0"><a href="{$modelUrl}" target="modelXML">capabilities document</a></p>
      <p>
        Abstract: <xsl:value-of select="ows:ServiceIdentification/ows:Abstract"/>
        <br/>
        provided by: 
        <a>
          <xsl:attribute name="href">
            <xsl:value-of select="ows:ServiceProvider/ows:ServiceContact/ows:ContactInfo/ows:OnlineResource/@xlink:href"/>
          </xsl:attribute>
          <xsl:value-of select="ows:ServiceProvider/ows:ProviderName"/>
        </a>
      </p>
      <dl>
        <xsl:apply-templates select="wps:ProcessOfferings/wps:Process"/>
      </dl>
    </div>
  </xsl:template>

  <!-- template rule matching source root element -->
  <xsl:template match="wps:Process">
    <xsl:variable name="name"><xsl:value-of select="wps:Name"/></xsl:variable>
    <dt>
      <a href="javascript:config.objects.{$modelId}.setParam('wps_DescribeProcess','{$name}')">
        <xsl:value-of select="$name"/>
      </a>
    </dt>
    <dd>
      <xsl:value-of select="wps:Description"/>
    </dd>
  </xsl:template>

  <xsl:template match="text()|@*"/>

</xsl:stylesheet>
