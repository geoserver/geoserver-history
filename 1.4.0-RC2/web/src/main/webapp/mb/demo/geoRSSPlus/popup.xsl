<?xml version="1.0" encoding="UTF-8"?>



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



   <xsl:output method="xml" indent="yes" />



  <xsl:template match="rss:item">
    <results> 
  	<b>title:</b><xsl:value-of select="rss:title"/><br/>
  	<b>description:</b><xsl:value-of select="rss:description"/><br/>
  	<b>date:</b><xsl:value-of select="rss:date"/><br/>

    <b>long:</b><xsl:value-of select="geo:long"/><br/>

    <b>lat:</b><xsl:value-of select="geo:lat"/><br/>

    <b>link:</b><xsl:element name="a">
     <xsl:attribute name="href"><xsl:value-of select="rss:link"/></xsl:attribute>click here for more information!
    </xsl:element><br/>
    </results>

  </xsl:template>



</xsl:stylesheet>

