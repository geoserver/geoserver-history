<#include "head.ftl">
<ul>
<li> Enabled: <#if Enabled> Yes <#else> No </#if> </li>
<li> Namespace: ${Namespace} </li>
<li> URL: ${URL} </li>
<li> Description: ${Description} </li>
<li> Type: ${Type} </li>
<li> <a href="${page.currentURL}/coverages">Coverages</a> </li>
</ul>
<#include "tail.ftl">
