<#include "head.ftl">
<h1>Layer Groups</h1>
All known layer groups:
<#list layers as layer>
  <p> ${layer.name} </p>
  <ul>
    <#list layer.members as member>
      <li>${member}</li>
    </#list>
  </ul>
</#list>
<#include "tail.ftl">
