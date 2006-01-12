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
    <a href="http://geoserver.sourceforge.net/documentation/1.3.0-PR1">
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
	<bean:message key="text.newFeatureLogo"/>&nbsp;
	<a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>mapPreview.do"/><bean:message key="text.newFeature1"/></a>
</p>

<h2>
  <bean:write name="WFS" property="title"/>
</h2>
<pre><code><bean:write name="WFS" property="abstract"/>
</code></pre>
<ul>
  <li>
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wfs?request=GetCapabilities&service=WFS">
      getCapabilities
    </a>
  </li>
  <li>
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wfs/TestWfsPost">
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
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wms?request=GetCapabilities&service=WMS">
      getCapabilities
    </a>
  </li>
</ul>

<p>
<bean:message key="text.welcome4"/>
</p>

<h2>
  <bean:message key="text.welcome.mapbuilder"/>
</h2>

<p>
<bean:message key="text.welcome.mapbuilder.detail"/>
</p>
<ul>
  <li>
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>data/mbdemos/demo/wfs-t/index.html">
      Mapbuilder/Geoserver (Tasmania)
    </a>
  </li>
  <li>
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>data/mbdemos/demo/cite/index.html">
      Mapbuilder/Geoserver (CITE)
    </a>
  </li>
</ul>

<br>
