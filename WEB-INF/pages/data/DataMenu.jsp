<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<span class="dataMenu">

<html:link forward="stores"><bean:message key="label.dataStores"/></html:link><br>
<html:link forward="namespaces"><bean:message key="label.namespaces"/></html:link><br>
<html:link forward="styles"><bean:message key="label.styles"/></html:link><br>
<html:link forward="types"><bean:message key="label.featureTypes"/></html:link><br>

</span>