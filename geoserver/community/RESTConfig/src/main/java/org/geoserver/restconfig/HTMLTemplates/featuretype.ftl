<#include "head.ftl">
<h1>Featuretype ${name}</h1>
<ul>
<li>Source: <a href="../../${datastoreid}">${datastoreid}</a></li>
<li>Namespace: ${namespace}</li>
<#if schemaattributes??>
<li>Schema Attributes:
<ul>
	<#list schemaattributes as sa>
  		<li>${sa}</li>
	</#list>
</li>
</ul>
<#else>
	<li>no feature types</li>
</#if>
</ul>
<#include "tail.ftl">