<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<table border=0><tr><td>

<html:link forward="validation"><bean:message key="label.validationConfig"/></html:link>
<BR>
<html:link forward="geoServerConfiguration"><bean:message key="label.geoServerConfiguration"/></html:link>
<BR>
Number of Locks: --<BR>
Number of Connections: --<BR>
Size of LogFile: -- KB<BR>

Feature Lock (Not Yet Implemented)<BR>
LogFile Management (Not Yet Implemented)<BR>


</td></tr></table>