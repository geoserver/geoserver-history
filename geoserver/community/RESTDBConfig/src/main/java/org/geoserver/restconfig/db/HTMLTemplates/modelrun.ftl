<#include "head.ftl">
<ul>
<li> Name: ${Name}</li>
<li> UpdateSequence: ${UpdateSequence}</li>
<li> Description: ${Description} </li>
<li> Keywords: <#list Keywords as w>${w}, </#list> </li>

<li> CRS: ${CRS} </li>
<li> Outline: <#list Outline as o>${o}; </#list> </li>

<li> GridCRS: ${GridCRS} </li>
<li> GridCS: ${GridCS} </li>
<li> GridType: ${GridType} </li>
<li> GridLowers: <#list GridLowers as gl>${gl}; </#list> </li>
<li> GridUppers: <#list GridUppers as gu>${gu}; </#list> </li>
<li> GridOrigin: <#list GridOrigin as go>${go}; </#list> </li>
<li> GridOffsets: <#list GridOffsets as goff>${goff}; </#list> </li>

<li> VerticalCoordinateMeaning: ${VerticalCoordinateMeaning} </li>

<li> InitParams: <#if InitParams??>
<ul>
<#list InitParams as ip>
<li>${ip}</li>
</#list>
</ul>
<#else>
[None]
</#if>
</li>

<li> OutParams: <#if OutParams??>
<ul>
<#list OutParams as op>
<li>${op}</li>
</#list>
</ul>
<#else>
[None]
</#if>
</li>

<li> ExecutionTime: ${ExecutionTime} </li>
<li> BaseTime: ${BaseTime} </li>
<li> TAU: ${TAU} </li>
<li> TAU unit of measure: ${TAUunit} </li>
<li> TAU number: ${NumTAU} </li>
</ul>
<#include "tail.ftl">