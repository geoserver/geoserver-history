<#include "head.ftl">
<ul>
<li> Name: ${Name}</li>
<li> Description: ${Description} </li>
<li> Keywords: <#list Keywords as w>${w}, </#list> </li>
<li> BoundingBox: <#list BoundingBox as o>${o}; </#list> </li>
<li> Vertical Domain: <#list Vdomain as vd>${vd}; </#list> </li>
<li> Temporal Domain: <#list Tdomain as td>${td}; </#list> </li>
<li> Variables: <#list Variables as v>${v}, </#list> </li>
</ul>
<#include "tail.ftl">