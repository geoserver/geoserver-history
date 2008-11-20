<#include "head.ftl">
<ul>
<#list GeophysicParamGridCoverages as model>
  <#assign mkeys = model?keys>
  <#list mkeys as mkey>
      <li>${mkey}
	      <ul>
	      	<#assign rkeys = model[mkey]?keys>
	      	<#list rkeys as rkey>
		      	<li>${rkey}
		      		<ul>
				        <#list model[mkey][rkey] as cv>
					      <li><a href="${page.currentURL}/../../../models/${mkey}/runs/${rkey}/gridcoverages/${cv}">${cv}</a></li>
					    </#list>
					</ul>
				</li>
			</#list>
		  </ul>
	  </li>
  </#list>
</#list>
</ul>
<#include "tail.ftl">
