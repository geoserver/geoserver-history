<?xml version="1.0" encoding="utf-8"?>
<service xmlns="http://purl.org/atom/app#" xmlns:atom="http://www.w3.org/2005/Atom">
  <workspace>
    <atom:title>Main Site</atom:title>
    <#list LAYERS as layer>
    <collection href="${SERVER_URL}history?layer=${layer}">
      <atom:title>Change feed for layer ${layer}</atom:title>
    </collection>
    </#list>
  </workspace>
</service>
