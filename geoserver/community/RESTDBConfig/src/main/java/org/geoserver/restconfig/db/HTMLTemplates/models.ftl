<#include "head.ftl">
<ul>
   <#list Models as m>
   <li><a href="${page.currentURL}/${m}">${m}</a></li>
   </#list>
</ul>
<#include "tail.ftl">
