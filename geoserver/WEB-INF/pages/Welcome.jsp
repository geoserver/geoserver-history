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

<h2>
  <bean:write name="WFS" property="title"/>
</h2>
<p>
  <bean:write name="WFS" property="abstract"/>
</p>

<h2>
  <bean:write name="WMS" property="title"/>
</h2>
<p>
  <bean:write name="WMS" property="abstract"/>
</p>