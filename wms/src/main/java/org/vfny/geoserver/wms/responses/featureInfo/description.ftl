<table border='1'>

<tr>
  <th colspan='2' scope='col'>${typeName}</th>
</tr>

<#list attributes as a>
<tr>
  <td>${a.name}</td> <td><#if a.isGeometry > [GEOMETRY] <#else> ${a.value} </#if> </td>
</tr>
</#list>
</table>
