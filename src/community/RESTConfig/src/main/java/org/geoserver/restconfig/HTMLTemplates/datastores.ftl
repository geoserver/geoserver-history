<#include "head.ftl">
<h1>DataStores</h1>
All known datastores:
<ul>
<#list datastores as datastore>
  <li><a href="${page.currentURL}/${datastore}">${datastore}</a>
  </li>
</#list>
</ul>
<#include "tail.ftl">
