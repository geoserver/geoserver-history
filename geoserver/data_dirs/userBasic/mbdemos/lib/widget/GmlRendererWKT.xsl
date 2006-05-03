<?xml version="1.0"?>
<xsl:stylesheet xmlns:gml="http://www.opengis.net/gml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"><xsl:output method="xml" encoding="utf-8"/><xsl:template match="/"><js><xsl:apply-templates/></js></xsl:template><xsl:template match="gml:pointMember/gml:Point | gml:pointProperty/gml:Point"><xsl:variable name="x0" select="gml:coord/gml:X"/><xsl:variable name="y0" select="gml:coord/gml:Y"/>
    // Point
    objRef.setValue('POINT(<xsl:value-of select="$x0"/><xsl:value-of select="$y0"/>)');
  </xsl:template><xsl:template match="gml:LineString"><xsl:text>  objRef.setValue('LINESTRING(</xsl:text><xsl:for-each select="gml:coord"><xsl:value-of select="gml:X"/><xsl:text/><xsl:value-of select="gml:Y"/><xsl:if test="following-sibling::gml:coord"><xsl:text>,</xsl:text></xsl:if></xsl:for-each><xsl:text>)');</xsl:text></xsl:template><xsl:template match="text()|@*"/></xsl:stylesheet>
