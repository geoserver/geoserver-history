<#include "head.ftl">
<h1>Featuretypes in ${datastoreid}</h1>
<ul>
<#if featuretypes??>
<#list featuretypes as ft>
  <li><a href="${page.currentURL}/featuretypes/${ft.name}">${ft.name} (SRS: <#if ft.srs??>${ft.srs}<#else>unknown</#if>)</a></li>
</#list>
<#else>
	<li>no feature types</li>
</#if>
</ul>
<#include "tail.ftl">
