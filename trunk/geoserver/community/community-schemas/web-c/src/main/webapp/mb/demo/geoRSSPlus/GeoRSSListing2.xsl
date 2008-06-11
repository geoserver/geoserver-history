<?xml version="1.0" encoding="ISO-8859-1"?>



<!--

Description: presents the list of events in a GeoRSS

Author:      adair

Licence:     LGPL as specified in http://www.gnu.org/copyleft/lesser.html .



$Id$

$Name$

-->



<xsl:stylesheet version="1.0" 

  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 

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

  <xsl:template match="/rdf:RDF">

<table border="0" cellpadding="0" cellspacing="0" width="100%">	

	<tbody><tr><td colspan="2"><img src="Calendar_files/spacer.gif" border="0" height="30" width="1"/></td></tr>



<tr>

	<td colspan="2" class="calHeader">

			All Events

		<br/>

		for GeoRSS feed: <xsl:value-of select="rss:channel/rss:title"/>

		<br/>

	</td>

</tr>



      <xsl:apply-templates select="rss:item"/>

    </tbody></table>

  </xsl:template>



  <xsl:template match="rss:item">

    <xsl:variable name="fid"><xsl:value-of select="@id"/></xsl:variable>

    <xsl:variable name="x"><xsl:value-of select="geo:long"/></xsl:variable>

    <xsl:variable name="y"><xsl:value-of select="geo:lat"/></xsl:variable>

    <xsl:variable name="link"><xsl:value-of select="rss:link"/></xsl:variable>

    <xsl:variable name="time"><xsl:value-of select="dc:date"/></xsl:variable>

<tr>

	<td class="caldate" nowrap="nowrap" valign="top" width="33%"> <img src="Calendar_files/yellow_bullet.gif" alt="" border="0"/>&#160;<a name="June13">June 13</a>&#160;&#160;&#160;&#160;</td>

	<td class="caleventtitle" valign="top" onmouseover="config.objects.{$modelId}.setParam('highlightFeature','{$fid}')" onmouseout="config.objects.{$modelId}.setParam('dehighlightFeature','{$fid}')">

		<xsl:value-of select="rss:title"/>

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

	<td class="caltime"><i><xsl:value-of select="$time"/></i></td>

</tr>

<tr>

	<td>&#160;</td>

	<td class="caldescription">

	<p><xsl:value-of select="rss:description"/></p>

	</td>

</tr>

<tr onmouseover="config.objects.{$modelId}.setParam('highlightFeature','{$fid}')" onmouseout="config.objects.{$modelId}.setParam('dehighlightFeature','{$fid}')">

	<td>&#160;</td>

	<td class="caldescription">

  	<p><a href="{$link}" class="caldescription">Learn more></a></p>

	</td>

</tr>



</xsl:template>



</xsl:stylesheet>



