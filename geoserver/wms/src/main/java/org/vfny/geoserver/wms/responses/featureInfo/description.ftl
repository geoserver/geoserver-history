<table border='1'>

<tr>
  <th colspan='${attributes?size}' scope='col'>${fid}</th>
</tr>

<tr>
<#list attributes as a>
  <td>${a.name}</td>
</#list>
</tr>

<tr>
<#list attributes as a>
  <td>
  <#if a.isGeometry >
[GEOMETRY]
  <#else>
${a.value}
  </#if>
  </td>
</#list>
</tr>

</table>
