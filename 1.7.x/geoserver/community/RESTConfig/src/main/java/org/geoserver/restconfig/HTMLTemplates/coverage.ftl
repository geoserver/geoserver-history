<#include "head.ftl">
<h1>Coverage</h1>
<ul>
<li> Label : <span class="label">${Label}</span> </li>
<li> Description : <span class="description">${Description}</span> </li>
<li> Keywords : <#list Keywords as keyword> <span class="keyword">${keyword}</span>, </#list> </li>
<li> Online Resource : <span class="online-resource">${OnlineResource}</span> </li>
<li> Default Style : <span class="default-style">${DefaultStyle}</span> </li>
<li> Supplementary Styles : 
<#if SupplementaryStyles??>
<ul> <#list SupplementaryStyles as style>
<li> <span class="supplementary-style">${style}</span> </li>
</#list> </ul> 
<#else>
None
</#if>
</li>
<li> CRS: <span class="crs">${CRS}</span> </li> 
<li> Supported Request CRS's : <ul> <#list SupportedRequestCRSs as crs>
<li> <span class="supported-request-crs">${crs}</span> </li>
</#list> </ul> </li>
<li> Supported Response CRS's : <ul> <#list SupportedResponseCRSs as crs>
<li> <span class="supported-response-crs">${crs}</span> </li>
</#list> </ul> </li>
<li> Envelope : <span class="envelope">[<#list Envelope as coord> ${coord}, </#list>]</span></li>
<li> Native Format : <span class="native-format">${NativeFormat}</span> </li>
<li> Supported Formats : <ul> <#list SupportedFormats as format>
<li> <span class="supported-format">${format}</span> </li>
</#list> </ul> </li>
<li> Default Interpolation Method : <span class="default-interpolation">${DefaultInterpolationMethod}</span> </li>
<li> Interpolation Methods : <ul> <#list InterpolationMethods as method>
<li> <span class="supported-interpolation">${method}</span> </li>
</#list> </ul> </li>
</ul>
<#include "tail.ftl">
