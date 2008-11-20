<#include "head.ftl">
<ul>
<li> Name: ${Name}</li>
<li> Version: ${Version}</li>
<li> Metadata Links: <br/> <#list MetadataLink as ml> ${ml} <br/> </#list> </li>
<li> Title: ${Title} </li>
<li> Description: ${Description} </li>
<li> Abstract: ${Abstract} </li>
<li> OriginatingCenter: ${OriginatingCenter} </li>
<li> SubCenter: ${SubCenter} </li>
<li> Keywords: <#list Keywords as w>${w}, </#list> </li>
<li> Discipline: ${Discipline} </li>
<li> TypeOfData: ${TypeOfData} </li>
<li> Products: <#list Products as p>${p}, </#list> </li>

<li> Metadata: <#if Metadata??>
<ul>
<#list Metadata as meta>
<li>${meta}</li>
</#list>
</ul>
<#else>
[None]
</#if>
</li>

</ul>
<#include "tail.ftl">