<?xml version="1.0" encoding="ISO-8859-1"?>

<!--
Description: presents the list of events in a GeoRSS
Author:      adair
Licence:     LGPL as specified in http://www.gnu.org/copyleft/lesser.html .

$Id$
$Name:  $
-->

<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:atom="http://www.w3.org/2005/Atom" 
  xmlns:georss="http://georss.org/rss" 
  xmlns:gml="http://www.opengis.net/gml/3.1.1" 
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" 
  xmlns:rss="http://purl.org/rss/1.0/" 
  xmlns:taxo="http://purl.org/rss/1.0/modules/taxonomy/" 
  xmlns:dc="http://purl.org/dc/elements/1.1/" 
  xmlns:syn="http://purl.org/rss/1.0/modules/syndication/" 
  xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#">

  <xsl:output method="xml" omit-xml-declaration="no" encoding="ISO-8859-1" indent="yes"/>

  <!-- The coordinates of the DHTML Layer on the HTML page -->
  <xsl:param name="modelId"/>
  <xsl:param name="targetModelId"/>
  <xsl:param name="widgetId"/>
  
  <!-- template rule matching source root element -->
  <xsl:template match="/atom:feed">
<table border="0" cellpadding="0" cellspacing="0" width="100%">	
	<tbody><tr><td colspan="2"><img src="spacer.gif" border="0" height="30" width="1"/></td></tr>

<tr>
	<td colspan="2" class="calHeader">
		<xsl:value-of select="/atom:feed/atom:title"/>:
		<br/>
    <br/>
	</td>
</tr>

<xsl:apply-templates select="atom:entry"/>
    </tbody></table>
  </xsl:template>

  <xsl:template match="atom:entry">
    <xsl:variable name="fid"><xsl:value-of select="@id"/></xsl:variable>
    <xsl:variable name="coords"><xsl:value-of select="georss:where/gml:Point/gml:pos"/></xsl:variable>
    <xsl:variable name="link"><xsl:value-of select="atom:link/@href"/></xsl:variable>
    <xsl:variable name="time"><xsl:value-of select="atom:updated"/></xsl:variable>
    <xsl:variable name="source"><xsl:value-of select="atom:source"/></xsl:variable>
<tr>
	<td class="caldate" nowrap="nowrap" valign="top" width="33%"> <img src="../../lib/skin/blue/images/yellow_bullet.gif" alt="" border="0"/>&#160;<a name="June13"><xsl:value-of select="$time"/></a>&#160;&#160;&#160;&#160;</td>
	<td class="caleventtitle" valign="top" onmouseover="config.objects.{$modelId}.setParam('highlightFeature','{$fid}')" onmouseout="config.objects.{$modelId}.setParam('dehighlightFeature','{$fid}')">
		<xsl:value-of select="atom:title"/>
	</td>
</tr>
<tr>
	<td>&#160;</td>
	<td class="caltime" onmouseover="config.objects.{$modelId}.setParam('highlightFeature','{$fid}')" onmouseout="config.objects.{$modelId}.setParam('dehighlightFeature','{$fid}')">
     <i>
 	     Address - see marker on map
		</i>
		</td>
	</tr>
<tr>
	<td>&#160;</td>
</tr>
<tr>
	<td>&#160;</td>
	<td class="caldescription">
	<p><xsl:value-of select="atom:content"/></p>
	</td>
</tr>
<tr onmouseover="config.objects.{$modelId}.setParam('highlightFeature','{$fid}')" onmouseout="config.objects.{$modelId}.setParam('dehighlightFeature','{$fid}')">
	<td>&#160;</td>
	<td class="caldescription">
  	<p><a href="{$link}" class="caldescription">Learn more></a></p><br/>
	</td>
</tr>

</xsl:template>

</xsl:stylesheet>

