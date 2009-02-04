<#include "head.ftl">
<ul>
<#list Layers as l>
<li> <a href="${page.pageURI}/layers/${l}">${l}</a> </li>
</#list>
</ul>
<#include "tail.ftl">
