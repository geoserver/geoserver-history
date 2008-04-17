<h2>${typeName}</h2>

<ul class="textattributes">
<#list attributes as a>
  <#if ! a.isGeometry ><li><strong><div class="atr-name">${a.name}</div>:</strong> <div class="atr-value">${a.value}</div></li></#if>
</#list>
</ul>
