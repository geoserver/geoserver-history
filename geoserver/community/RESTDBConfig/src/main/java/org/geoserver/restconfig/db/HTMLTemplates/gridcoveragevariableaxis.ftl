<#include "head.ftl">
<ul>
<li> Axis Name: ${Name}</li>
<li> Description: ${Description} </li>
<li> Unit Of Measure: ${UnitOfMeasure} </li>
<li> Coordinate Reference System: ${CoordinateReferenceSystem} </li>

<li> Keys: <#if Keys??>
<ul>
<#list Keys as k>
<li>${k}</li>
</#list>
</ul>
<#else>
[None]
</#if>
</li>

</ul>
<#include "tail.ftl">