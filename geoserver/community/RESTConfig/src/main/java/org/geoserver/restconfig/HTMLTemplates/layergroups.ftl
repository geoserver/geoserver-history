<#include "head.ftl">
<h1>Layer Groups</h1>
All known layer groups:
<ul>
<#list layers as layer>
  <li> ${layer} </li>
</#list>
</ul>
<#include "tail.ftl">
