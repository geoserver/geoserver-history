<?xml version="1.0"?>
<xsl:stylesheet xmlns:gml="http://www.opengis.net/gml" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"><xsl:output method="xml" encoding="utf-8"/><xsl:param name="width" select="400"/><xsl:param name="height" select="200"/><xsl:param name="bBoxMinX" select="-180"/><xsl:param name="bBoxMinY" select="-90"/><xsl:param name="bBoxMaxX" select="180"/><xsl:param name="bBoxMaxY" select="90"/><xsl:variable name="xRatio" select="$width div ( $bBoxMaxX - $bBoxMinX )"/><xsl:variable name="yRatio" select="$height div ( $bBoxMaxY - $bBoxMinY )"/><xsl:template match="gml:coord/gml:X"><xsl:element name="gml:X"><xsl:value-of select="round((number(.)-$bBoxMinX)*$xRatio)"/></xsl:element></xsl:template><xsl:template match="gml:coord/gml:Y"><xsl:element name="gml:Y"><xsl:value-of select="round($height - (number(.)-$bBoxMinY)*$yRatio)"/></xsl:element></xsl:template><xsl:template match="*|@*|comment()|processing-instruction()|text()"><xsl:copy><xsl:apply-templates select="*|@*|comment()|processing-instruction()|text()"/></xsl:copy></xsl:template></xsl:stylesheet>
