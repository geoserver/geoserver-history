<#include "head.ftl">
<h1>DataStores</h1>
All known datastores:
<ul>
<#list datastores as datastore>
  <li><a href="${page.currentURL}/${datastore.id}">${datastore.id} (${datastore.type})</a>
	<ul>
  	<#if datastore.featuretypes??>
		<#list datastore.featuretypes as ft>
		<li><a href="${page.currentURL}/${datastore.id}/featuretypes/${ft}">${ft}</a></li>
		</#list>
	<#else>
		<li>no feature types</li>
	</#if>
	</ul>
  </li>
</#list>
</ul>
<#include "tail.ftl">
