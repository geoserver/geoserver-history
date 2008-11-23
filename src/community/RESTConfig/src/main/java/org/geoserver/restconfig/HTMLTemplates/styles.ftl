<#include "head.ftl">
<h1>Styles</h1>
All known styles:
<ul>
<#list styles as style>
  <li><a href="${page.currentURL}/${style}">${style}</a>
  </li>
</#list>
</ul>
<#include "tail.ftl">
