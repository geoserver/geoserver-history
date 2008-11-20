<#include "head.ftl">
<ul>
<li> Variable Name: ${Name}</li>
<li> Description: ${Description} </li>
<li> Unit Of Measure: ${UnitOfMeasure} </li>

<li> Axes: <#if Axes??>
<ul>
<#list Axes as axis>
<li><a href="${page.currentURL}/axis/${axis}">${axis}</a></li>
</#list>
</ul>
<#else>
[None]
</#if>
</li>

<li> SampleDimensions: <#if SampleDimensions??>
<ul>
<#list SampleDimensions as sd>
<li><a href="${page.currentURL}/sampledim/${sd}">${sd}</a></li>
</#list>
</ul>
<#else>
[None]
</#if>
</li>

</ul>
<#include "tail.ftl">