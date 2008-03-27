<#include "head.ftl">
<h1>Featuretypes in ${datastoreid}</h1>
<ul>
<li> Enabled : <#if Enabled> yes <#else> no </#if>
<li> Namespace : ${Namespace} </li>
<li> Description : ${Description} </li>
<li> Data URL: ${DataURL} </li>
<li> Spatial Indexing : <#if SpatialIndexingEnabled> yes <#else> no </#if> </li>
<li> Character Set : ${CharSet} </li>
<li> Memory Mapping : <#if MemoryMappingEnabled> Enabled <#else> Disabled </#if> </li>
<#if featuretypes??>
<#list featuretypes as ft>
  <li><a href="${page.currentURL}/featuretypes/${ft.name}">${ft.name} (SRS: <#if ft.srs??>${ft.srs}<#else>unknown</#if>)</a></li>
</#list>
<#else>
	<li>no feature types</li>
</#if>
</ul>
<#include "tail.ftl">
