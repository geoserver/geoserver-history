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
    <a href="http://geoserver.sourceforge.net/documentation/1.3.0-beta">
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

<h2>
  <bean:write name="WCS" property="title"/>
</h2>
<pre><code><bean:write name="WCS" property="abstract"/>
</code></pre>
<ul>
  <li>
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wcs/GetCapabilities">
      getCapabilities
    </a>
  </li>
  <li>
	<a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wcs/DescribeCoverage?coverage=Arc_Sample">
	  describeCoverage
	</a>
  </li>
  <li>
  	<a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wcs/GetCoverage?sourcecoverage=Arc_Sample&outputformat=png">
  	  getCoverage
  	</a>
  </li>
  <li>
  	<a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wms?bbox=-130,24,-66,50&styles=normal,population&Format=image/png&request=GetMap&layers=Img_Sample,topp:states&width=550&height=250&srs=EPSG:4326">
  	  getMap
  	</a>
  </li>
</ul>

<h2>
  <bean:write name="WFS" property="title"/>
</h2>
<pre><code><bean:write name="WFS" property="abstract"/>
</code></pre>
<ul>
  <li>
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wfs/GetCapabilities">
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
    <a href="<%=org.vfny.geoserver.util.Requests.getBaseUrl(request)%>wms/GetCapabilities">
      getCapabilities
    </a>
  </li>
</ul>

<p>
<bean:message key="text.welcome4"/>
</p>


