<#include "head.ftl">
Workspace "${properties.name}"
<ul>
<#list properties.stores as s>
  <li><a href="${page.pageURI}/datastores/${s.properties.name}.html">${s.properties.name}</a></li>
</#list>
</ul>
<#include "tail.ftl">
