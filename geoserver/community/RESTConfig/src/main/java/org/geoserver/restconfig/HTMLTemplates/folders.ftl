<#include "head.ftl">
<ul>
   <#list Layers as l>
   <li><a href="./${l}">${l}</a></li>
   </#list>
</ul>
<#include "tail.ftl">
