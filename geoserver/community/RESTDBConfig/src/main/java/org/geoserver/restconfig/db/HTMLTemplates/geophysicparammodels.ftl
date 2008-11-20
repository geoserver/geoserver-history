<#include "head.ftl">
<ul>
   <#list GeophysicParamModels as m>
   <li><a href="${page.currentURL}/../../../models/${m}">${m}</a></li>
   </#list>
</ul>
<#include "tail.ftl">
