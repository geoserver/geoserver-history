<#include "head.ftl">
<h1>Coverages</h1>
Details for coverage ${coverageName}
<ul>
<li> Label : ${Label} </li>
<li> Description : ${Description} </li>
<li> Keywords : <#list Keywords as keyword>
${keyword},
</#list> </li>
<li> Default Style: ${DefaultStyle} </li>
<li> CRS: ${CRSDescription} <p> ${CRSFull}</p></li> 
</ul>
<#include "tail.ftl">
