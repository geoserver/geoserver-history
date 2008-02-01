<#include "head.ftl">
<h1>Layer Groups</h1>
All known layer groups:
  <ul>
  <#if layers??>
<#list layers as layer>
  <li><a href="${page.currentURL}/${layer}"</li>
</#list>
  <#else>
    <li> [No layers defined] </li>
  </#if>
  </ul>
<#include "tail.ftl">
