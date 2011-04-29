<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions"
    xmlns:wfs="http://www.opengis.net/wfs" xmlns:wfs2="http://www.opengis.net/wfs/2.0" xmlns:gml="http://www.opengis.net/gml">
    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="no" />
    <xsl:template match="/wfs:FeatureCollection/@numberOfFeatures">
        <xsl:attribute name="numberOfFeatures">
            <xsl:value-of
            select="count(/wfs:FeatureCollection/gml:featureMember | /wfs:FeatureCollection/gml:featureMembers/child::*)" /> 
        </xsl:attribute>
    </xsl:template>
    <xsl:template match="/wfs2:FeatureCollection">
        <wfs2:FeatureCollection>
            <xsl:copy-of select="@*" />
            <xsl:attribute name="numberMatched">
                <xsl:text>unknown</xsl:text> 
            </xsl:attribute>
            <xsl:attribute name="numberReturned">
                <xsl:value-of select="count(//wfs2:FeatureCollection/wfs2:member)" />
            </xsl:attribute>
            <xsl:apply-templates select="*" />
        </wfs2:FeatureCollection>
    </xsl:template>
    <xsl:template match="@* | node()" priority="-1000">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>