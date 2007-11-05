<#include "head.ftl">
<h1>DataStores</h1>
All known datastores:
<ul>
<#list datastores as datastore>
  <li><a href="datastores/${datastore.id}">${datastore.id} (${datastore.type})</a>
	<ul>
  	<#if datastore.featuretypes??>
		<#list datastore.featuretypes as ft>
		<li><a href="datastores/${datastore.id}/featuretypes/${ft.name}">${ft.name}</a></li>
		</#list>
	<#else>
		<li>no feature types</li>
	</#if>
	</ul>
  </li>
</#list>
</ul>
<#include "tail.ftl">