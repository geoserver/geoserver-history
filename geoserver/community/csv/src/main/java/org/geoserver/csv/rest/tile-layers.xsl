<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xlink="http://www.w3.org/1999/xlink">
    <xsl:output method="xml" encoding="utf-8" omit-xml-declaration="yes" indent="yes" standalone="yes"/>
    
    <xsl:param name="geoserver" select="/WMT_MS_Capabilities/Service/OnlineResource/@xlink:href"/>
    
    <!-- reusable replace-string function -->
    <xsl:template name="replace-string">
        <xsl:param name="text"/>
        <xsl:param name="from"/>
        <xsl:param name="to"/>
        <xsl:choose>
            <xsl:when test="contains($text, $from)">
                <xsl:variable name="before" select="substring-before($text, $from)"/>
                <xsl:variable name="after" select="substring-after($text, $from)"/>
                <xsl:variable name="prefix" select="concat($before, $to)"/>
                <xsl:value-of select="$before"/>
                <xsl:value-of select="$to"/>
                <xsl:call-template name="replace-string">
                    <xsl:with-param name="text" select="$after"/>
                    <xsl:with-param name="from" select="$from"/>
                    <xsl:with-param name="to" select="$to"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$text"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="Layer">
        <Layer>
            <Name>
                <xsl:value-of select="Namer"/>
            </Name>
            <Title>
                <xsl:value-of select="Title"/>
            </Title>
            <GoolgeUrl>
                <xsl:call-template name="replace-string">
                    <xsl:with-param name="text" select="$geoserver"/>
                    <xsl:with-param name="from">/wms</xsl:with-param>
                    <xsl:with-param name="to">/gwc/service/gmaps?layers=<xsl:value-of select="Name" />&amp;zoom={Z}&amp;x={X}&amp;y={Y}</xsl:with-param>
                </xsl:call-template>
            </GoolgeUrl>
            <StyleUrl>
                <xsl:call-template name="replace-string">
                    <xsl:with-param name="text" select="$geoserver"/>
                    <xsl:with-param name="from">/wms</xsl:with-param>
                    <xsl:with-param name="to">/rest/styles/<xsl:value-of select="Style/Name" />.xml</xsl:with-param>
                </xsl:call-template>
            </StyleUrl>
        </Layer>
    </xsl:template>
    
    <xsl:template match="/">
        <Layers>
            <xsl:apply-templates select="/WMT_MS_Capabilities/Capability/Layer/Layer"/>
        </Layers>
    </xsl:template>
</xsl:stylesheet>
