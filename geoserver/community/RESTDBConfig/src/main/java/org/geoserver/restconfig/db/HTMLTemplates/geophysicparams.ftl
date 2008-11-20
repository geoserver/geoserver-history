<#include "head.ftl">
<ul>
   <#list GeophysicParams as gp>
   <li><a href="${page.currentURL}/${gp}">${gp}</a></li>
   </#list>
</ul>
<#include "tail.ftl">
