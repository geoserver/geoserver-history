<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<span class="mainMenu">
<html:link forward="validation"><bean:message key="label.validationConfig"/></html:link><br>

<html:link forward="geoServerConfiguration"><bean:message key="label.geoServerConfiguration"/></html:link><br>

Number of Locks: --<BR>
Number of Connections: --<BR>
Size of LogFile: -- KB<BR>

Feature Lock (Not Yet Implemented)<BR>
LogFile Management (Not Yet Implemented)<BR>

</span>