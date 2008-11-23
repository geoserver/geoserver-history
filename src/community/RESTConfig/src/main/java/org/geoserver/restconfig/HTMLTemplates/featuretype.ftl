<#include "head.ftl">
<ul>
<li>Default Style: ${Style}</li>
<li>Additional Styles: <#if AdditionalStyles??>
<ul>
<#list AdditionalStyles as st>
<li>${st}</li>
</#list>
</ul>
<#else>
[None]
</#if>
</li>
<li> SRS: ${SRS} </li>
<li> SRS Handling: ${SRSHandling} </li>
<li> Title: ${Title} </li>
<li> Bounding Box: [${BBox[0]}, ${BBox[1]}, ${BBox[2]}, ${BBox[3]}] </li>
<li> Keywords: <#list Keywords as w>${w}, </#list> </li>
<li> Description: ${Abstract} </li>
<li> WMS Path: ${WMSPath} </li>
<li> Metadata Links: <br/> <#list MetadataLinks as ml> ${ml} <br/> </#list> </li>
<li> Caching: <#if CachingEnabled == "true"> Enabled <#else> Disabled </#if> </li>
<li> Caching Interval: <#if CacheTime??> ${CacheTime} <#else> [None] </#if> </li>
<li> Schema Base: <#if SchemaBase??> ${SchemaBase} <#else> [None] </#if> </li>
</ul>
<#include "tail.ftl">
