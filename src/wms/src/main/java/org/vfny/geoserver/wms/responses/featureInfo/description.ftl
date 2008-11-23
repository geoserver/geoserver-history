<h4>${typeName}</h4>

<ul class="textattributes">
<#list attributes as a>
  <#if ! a.isGeometry ><li><strong><span class="atr-name">${a.name}</span>:</strong> <span class="atr-value">${a.value}</span></li></#if>
</#list>
</ul>
