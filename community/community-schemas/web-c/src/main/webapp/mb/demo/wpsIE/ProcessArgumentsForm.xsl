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
  <xsl:param name="toolId"/>
  
  <xsl:param name="webServiceUrl"/>
  <xsl:param name="webServiceMethod"/>
  <xsl:param name="formName"/>
  
  <!-- template rule matching source root element -->
  <xsl:template match="/wps:ProcessDescription/wps:ProcessMember/wps:Process">
    <xsl:variable name="name"><xsl:value-of select="wps:Name"/></xsl:variable>
    <div>
    <form name="{$formName}" id="{$formName}" action="{$webServiceUrl}">
      <input type="hidden" name="request" value="Execute"/>
      <input type="hidden" name="service" value="WPS"/>
      <input type="hidden" name="version" value="0.2.4"/>
      <input type="hidden" name="ProcessName" value="{$name}"/>
    
    <p style="text-align:right;font:70%;margin:0"><a href="{$modelUrl}" target="modelXML">DescribeProcess response</a></p>
    <table cellspacing="0" border="1" width="100%">
      <tr>
        <th align="left">Set parameters for:<xsl:value-of select="$name"/></th>
        <th align="right"><a href="javascript:config.objects.{$widgetId}.executeProcess('{$name}')">Execute</a>
        (<xsl:value-of select="$webServiceMethod"/>)
        </th>
      </tr>
      <tr>
        <td colspan="2">
        <dl>
          <dt><xsl:value-of select="wps:Label"/></dt>
          <dd><xsl:value-of select="wps:Description"/></dd>
        </dl>
        </td>
      </tr>
      <tr>
        <td>Input parameters</td>
        <td>value</td>
      </tr>
      <xsl:apply-templates select="wps:Input"/>
    </table>
    </form>
    </div>
  </xsl:template>

  <xsl:template match="wps:Output/wps:Parameter"/>
  
  <xsl:template match="wps:Input/wps:Parameter">
    <xsl:variable name="paramName"><xsl:value-of select="wps:Name"/></xsl:variable>
    <tr>
      <td>
        <dl class="params">
          <dt><xsl:value-of select="wps:Label"/></dt>
          <dd><xsl:value-of select="wps:Description"/></dd>
          <dd>datatype: <xsl:value-of select="name(wps:Datatype/*)"/></dd>
          <xsl:if test="wps:Datatype/wps:Reference/@xlink:href">
          </xsl:if>
        </dl>
      </td>
      <td>
        <xsl:apply-templates select="wps:Datatype/*">
          <xsl:with-param name="paramName" select="$paramName"/>
        </xsl:apply-templates>
      </td>
    </tr>
  </xsl:template>

  <xsl:template match="wps:LiteralValue">
    <xsl:param name="paramName"/>
    <input type="text" name="{$paramName}" onblur="config.objects.{$modelId}.parentModel.setParam('{$paramName}',this.value)">
      <xsl:if test="string-length(wps:DefaultValue)>0">
        <xsl:attribute name="value"><xsl:value-of select="wps:DefaultValue"/></xsl:attribute>
      </xsl:if>
    </input>
    <br/>
    <span>type: <xsl:value-of select="@ows:type"/></span>
  </xsl:template>
  
  <xsl:template match="wps:Reference">
    <xsl:param name="paramName"/>
    <input type="text" size="30" name="{$paramName}" onblur="config.objects.{$modelId}.parentModel.setParam('{$paramName}',this.value)">
      <xsl:if test="string-length(@xlink:href)>0">
        <xsl:attribute name="value"><xsl:value-of select="@xlink:href"/></xsl:attribute>
      </xsl:if>
    </input>
    <br/>
    <span>format: <xsl:value-of select="ows:Format"/></span>
    <div id="{$widgetId}_{$paramName}_dataSelectorWidget"></div>
  </xsl:template>
  
  <xsl:template match="wps:BoundingBox">
      use the set AOI button in the map to set the bbox
      <table>
        <tr>
          <td>
          </td>
          <td>
            north: <input NAME="northCoord" TYPE="text" SIZE="10" STYLE="font: 8pt Verdana, geneva, arial, sans-serif;"/>
          </td>
          <td>
          </td>
        </tr>
        <tr>
          <td>
            west: <input NAME="westCoord" TYPE="text" SIZE="10" STYLE="font: 8pt Verdana, geneva, arial, sans-serif;"/>
          </td>
          <td>
          </td>
          <td>
            east: <input NAME="eastCoord" TYPE="text" SIZE="10" STYLE="font: 8pt Verdana, geneva, arial, sans-serif;"/>
          </td>
        </tr>
        <tr>
          <td>
          </td>
          <td>
            south: <input NAME="southCoord" TYPE="text" SIZE="10" STYLE="font: 8pt Verdana, geneva, arial, sans-serif;"/>
          </td>
          <td>
          </td>
        </tr>
      </table>
  </xsl:template>

  <xsl:template match="text()|@*"/>

</xsl:stylesheet>
