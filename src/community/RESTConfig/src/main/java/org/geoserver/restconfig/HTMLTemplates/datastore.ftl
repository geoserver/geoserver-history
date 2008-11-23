<#include "head.ftl">
<ul>
<li> Enabled : <#if Enabled == "true"> yes <#else> false </#if> 
<li> Namespace : ${Namespace} </li>
<li> Description : ${Description} </li>
<li> Parameters: 
<ul>
<#if Params??>
    <#list Params as param>
       <li>${param.name} = <#if (param.value)??>${param.value}<#else>null</#if></li>
    </#list>
<#else>
   <li>No parameters</li>
</#if>
</ul>
</li>
<li> <a href="${page.currentURL}/layers">FeatureTypes</a> </li>
</ul>
<#include "tail.ftl">
