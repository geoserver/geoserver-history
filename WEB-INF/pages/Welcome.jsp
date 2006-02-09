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
<bean:message key="text.welcome1"/>
</p>

<!--p>
<bean:message key="text.welcome2"/>
</p-->

<!--p>
<bean:message key="text.welcome3"/>
</p-->

<p>
<bean:message key="text.welcome5"/>
</p>

<ul>
  <li>
    <a href="http://geoserver.sourceforge.net/documentation/1.3.0">
      Documentation
    </a>
  </li>
  <li>
    <a href="http://docs.codehaus.org/display/GEOS/Home">
      Wiki
    </a>
  </li>
  <li>
    <a href="http://jira.codehaus.org/secure/BrowseProject.jspa?id=10311">
      Task Tracker
    </a>
   </li>
   <li>
    <a href="http://www.moximedia.com:8080/imf-ows/imf.jsp?site=gs_users">
      User Map
    </a>
  </li>
</ul>

<p>
	<bean:message key="text.visitDemoPage"/>
</p>

<p>
	<a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wfs?request=GetCapabilities&service=WFS&version=1.0.0">WFS GetCapabilities</a><br>
	<a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wms?request=GetCapabilities&service=WMS&version=1.0.0">WMS GetCapabilities</a>
</p>

<br>
