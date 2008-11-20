<#include "head.ftl">
<ul>
   <li> Members: 
   <ul>
       <#list Members as m>
       <li> ${m} </li>
       </#list>
   </ul>
   </li>
   <li> Styles: 
   <ul>
   <#if Styles??>
       <#list Styles as s>
       <li> ${s} </li>
       </#list>
   <#else>
       <li> [None] </li>
   </#if>
   </ul>
   </li>
   <li> SRS: <#if SRS??>${SRS}<#else>[none]</#if> </li>
   <li> Bounding Box: [${Envelope[0]}, ${Envelope[1]}, ${Envelope[2]}, ${Envelope[3]}] </li>
</ul>
<#include "tail.ftl">
