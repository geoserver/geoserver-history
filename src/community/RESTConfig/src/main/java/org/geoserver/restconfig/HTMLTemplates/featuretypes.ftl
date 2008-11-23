<#include "head.ftl">
<ul>
<li> Configured FeatureTypes 
<ul>
<#list Configured as ft>
<li> <a href="${page.currentURL}/${ft}">${ft}</a> </li>
</#list>
</ul>
</li>
<li> Unconfigured FeatureTypes 
<ul>
<#list Available as ft>
<li> <a href="${page.currentURL}/${ft}">${ft}</a> </li>
</#list>
</ul>
</li>
</ul>
<#include "tail.ftl">
