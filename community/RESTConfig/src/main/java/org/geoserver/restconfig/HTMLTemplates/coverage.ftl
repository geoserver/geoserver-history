<#include "head.ftl">
<h1>Coverage</h1>
<ul>
<li> Label : ${Label} </li>
<li> Description : ${Description} </li>
<li> Keywords : <#list Keywords as keyword> ${keyword}, </#list> </li>
<li> Default Style : ${DefaultStyle} </li>
<li> Online Resource : ${OnlineResource} </li>
<li> Supplementary Styles : 
<#if SupplementaryStyles??>
<ul> <#list SupplementaryStyles as style>
<li> ${style} </li>
</#list> </ul> 
<#else>
None
</#if>
</li>
<li> CRS: ${CRS} </li> 
<li> Supported Request CRS's : <ul> <#list SupportedRequestCRSs as crs>
<li> ${crs} </li>
</#list> </ul> </li>
<li> Supported Response CRS's : <ul> <#list SupportedResponseCRSs as crs>
<li> ${crs} </li>
</#list> </ul> </li>
<li> Envelope : [<#list Envelope as coord> ${coord}, </#list>]</li>
<li> Native Format : ${NativeFormat} </li>
<li> Supported Formats : <ul> <#list SupportedFormats as format>
<li> ${format} </li>
</#list> </ul> </li>
<li> Default Interpolation Method : ${DefaultInterpolationMethod} </li>
<li> Interpolation Methods : <ul> <#list InterpolationMethods as method>
<li> ${method} </li>
</#list> </ul> </li>
</ul>
<#include "tail.ftl">
