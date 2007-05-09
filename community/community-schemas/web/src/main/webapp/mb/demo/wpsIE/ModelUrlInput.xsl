<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" 
    xmlns:mb="http://mapbuilder.sourceforge.net/mapbuilder" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!--
Description: Output a form for setting any model's url and method values
Author:      Mike Adair
Licence:     LGPL as per: http://www.gnu.org/copyleft/lesser.html

$Id$
-->

  <xsl:output method="xml" encoding="utf-8"/>

  <!-- The common params set for all widgets -->
  <xsl:param name="lang">en</xsl:param>
  <xsl:param name="modelId"/>
  <xsl:param name="modelTitle"/>
  <xsl:param name="targetModel"/>
  <xsl:param name="widgetId"/>

  <!-- Text params for this widget -->
  <xsl:param name="title"/>
  <xsl:param name="load"/>

    <!-- The name of the form for coordinate output -->
  <xsl:param name="modelUrl"/>
  <xsl:param name="formName">ModelUrlInputForm</xsl:param>

  <!-- Main html -->
  <xsl:template match="/">
    <div class="tabbedContent">
      <form name="{$formName}" id="{$formName}" onsubmit="return config.objects.{$widgetId}.submitForm()">
        Select a Web Processing service from the menu on the left, 
        or enter the WPS GetCapabilities URL here:    
        <input name="modelUrl" type="text" size="30" value="{$modelUrl}"/>
        <a href="javascript:config.objects.{$widgetId}.submitForm();"><xsl:value-of select="$load"/></a>
      </form>
    </div>
  </xsl:template>
  
</xsl:stylesheet>
