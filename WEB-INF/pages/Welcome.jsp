<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<logic:notPresent name="org.apache.struts.action.MESSAGE" scope="application">
  <span class="error">
    ERROR:  Application resources not loaded -- check servlet container
    logs for error messages.
  </span>
</logic:notPresent>

</span>

<p>
<bean:message key="text.welcome"/>
</p>

<h2>
  <bean:write name="WFS" property="title"/>
</h2>
<pre><code><bean:write name="WFS" property="abstract"/>
</code></pre>
<ul>
  <li>
    <a href="<%=org.vfny.geoserver.requests.Requests.getBaseUrl(request)%>wfs/getCapabilities">
      getCapabilities
    </a>
  </li>
  <li>
    <a href="<%=org.vfny.geoserver.requests.Requests.getBaseUrl(request)%>wfs/TestWfsPost">
      TestWfsPost
    </a>
  </li>
</ul>

<h2>
  <bean:write name="WMS" property="title"/>
</h2>
<pre><code><bean:write name="WMS" property="abstract"/>
</code></pre>

<ul>
  <li>
    <a href="<%=org.vfny.geoserver.requests.Requests.getBaseUrl(request)%>wms/getCapabilities">
      getCapabilities
    </a>
  </li>
</ul>