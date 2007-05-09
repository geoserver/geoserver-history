<?xml version="1.0"?>
<xsl:stylesheet xmlns:wfs="http://www.opengis.net/wfs" xmlns:wmc="http://www.opengis.net/context" xmlns:mb="http://mapbuilder.sourceforge.net/mapbuilder" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"><xsl:output method="xml" omit-xml-declaration="no" encoding="utf-8" indent="no"/><xsl:preserve-space elements="gml:coordinates"/><xsl:param name="cs" select="','"/><xsl:param name="ts" select="' '"/><xsl:param name="bBoxMinX"/><xsl:param name="bBoxMinY"/><xsl:param name="bBoxMaxX"/><xsl:param name="bBoxMaxY"/><xsl:param name="srs"/><xsl:param name="version"/><xsl:param name="httpMethod">get</xsl:param><xsl:param name="filter"/><xsl:param name="maxFeatures">500</xsl:param><xsl:param name="geometry"/><xsl:template match="wfs:FeatureType"><xsl:param name="resourceName" select="wfs:Name"/><xsl:param name="featureSrs" select="wfs:SRS"/><GetFeature version="1.0.0" service="WFS" maxFeatures="{$maxFeatures}"><xsl:choose><xsl:when test="$httpMethod='post'"><Query typeName="{$resourceName}"><ogc:Filter><ogc:And><xsl:if test="$bBoxMinX"><ogc:BBOX><ogc:PropertyName><xsl:value-of select="$geometry"/></ogc:PropertyName><gml:Box srsName="{$srs}"><xsl:value-of select="$bBoxMinX"/><xsl:value-of select="$cs"/><xsl:value-of select="$bBoxMinY"/><xsl:value-of select="$ts"/><xsl:value-of select="$bBoxMaxX"/><xsl:value-of select="$cs"/><xsl:value-of select="$bBoxMaxY"/></gml:Box></ogc:BBOX></xsl:if><xsl:if test="$filter"><xsl:value-of select="$filter"/></xsl:if></ogc:And></ogc:Filter></Query></xsl:when><xsl:otherwise><mb:QueryString><xsl:variable name="bbox"><xsl:value-of select="$bBoxMinX"/>,<xsl:value-of select="$bBoxMinY"/>,

            <xsl:value-of select="$bBoxMaxX"/>,<xsl:value-of select="$bBoxMaxY"/></xsl:variable><xsl:variable name="query">

         request=GetFeature

    &amp;service=WFS

    &amp;version=<xsl:value-of select="$version"/>

&amp;maxfeatures=<xsl:value-of select="$maxFeatures"/>

   &amp;typename=<xsl:value-of select="$resourceName"/><xsl:if test="$bBoxMinX">

   &amp;bbox=<xsl:value-of select="$bbox"/></xsl:if></xsl:variable><xsl:value-of select="translate(normalize-space($query),' ','')" disable-output-escaping="no"/><xsl:if test="$filter">&amp;filter=<xsl:value-of select="$filter"/></xsl:if></mb:QueryString></xsl:otherwise></xsl:choose></GetFeature></xsl:template><xsl:template match="wmc:FeatureType[wmc:Server/@service='OGC:WFS']"><xsl:param name="resourceName" select="wmc:Name"/><xsl:param name="featureSrs" select="wmc:SRS"/><GetFeature xmlns="http://www.opengis.net/wfs" version="1.0.0" service="WFS" maxFeatures="{$maxFeatures}"><xsl:choose><xsl:when test="$httpMethod='post'"><Query typeName="{$resourceName}"><ogc:Filter><ogc:And><xsl:if test="wmc:Geometry"><xsl:variable name="bbox" select="concat($bBoxMinX,concat(',',concat($bBoxMinY,concat(' ',concat($bBoxMaxX,concat(',',$bBoxMaxY))))))"/><ogc:BBOX><ogc:PropertyName><xsl:value-of select="wmc:Geometry"/></ogc:PropertyName><gml:Box srsName="{$srs}"><gml:coordinates><xsl:value-of select="$bbox"/></gml:coordinates></gml:Box></ogc:BBOX></xsl:if><xsl:if test="ogc:Filter"><xsl:copy-of select="ogc:Filter/*"/></xsl:if></ogc:And></ogc:Filter></Query></xsl:when><xsl:otherwise><mb:QueryString><xsl:variable name="cFilterStr"><xsl:value-of select="ogc:Filter"/></xsl:variable><xsl:variable name="bbox"><xsl:value-of select="$bBoxMinX"/>,<xsl:value-of select="$bBoxMinY"/>,

            <xsl:value-of select="$bBoxMaxX"/>,<xsl:value-of select="$bBoxMaxY"/></xsl:variable><xsl:variable name="query">

         request=GetFeature

    &amp;service=WFS

    &amp;version=<xsl:value-of select="$version"/>

&amp;maxfeatures=<xsl:value-of select="$maxFeatures"/>

   &amp;typename=<xsl:value-of select="$resourceName"/><xsl:if test="$bBoxMinX">

   &amp;bbox=<xsl:value-of select="$bbox"/></xsl:if></xsl:variable><xsl:value-of select="translate(normalize-space($query),' ','')" disable-output-escaping="no"/><xsl:if test="$cFilterStr">&amp;filter=<xsl:value-of select="normalize-space($cFilterStr)"/></xsl:if></mb:QueryString></xsl:otherwise></xsl:choose></GetFeature></xsl:template><xsl:template match="text()|@*"/></xsl:stylesheet>
