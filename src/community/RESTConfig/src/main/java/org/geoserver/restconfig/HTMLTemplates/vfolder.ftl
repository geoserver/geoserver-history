<#include "head.ftl">
<ul>
<#list Layers as l>
<li> <a href="${page.currentURL}/layers/${l}">${l}</a> </li>
</#list>
</ul>
<#include "tail.ftl">
