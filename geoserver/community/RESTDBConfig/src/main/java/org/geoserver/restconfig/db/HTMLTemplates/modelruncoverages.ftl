<#include "head.ftl">
<ul>
   <#list ModelRunGridCoverages as m>
   <li><a href="${page.currentURL}/${m}">${m}</a></li>
   </#list>
</ul>
<#include "tail.ftl">
