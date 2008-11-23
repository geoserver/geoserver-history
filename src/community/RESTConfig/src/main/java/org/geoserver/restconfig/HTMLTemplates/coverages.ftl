<#include "head.ftl">
<h1>Coverages</h1>
All known coverages:
<ul>
<#list coverages as coverage>
  <li><a href="${page.currentURL}/${coverage}">${coverage}</a>
  </li>
</#list>
</ul>
<#include "tail.ftl">
