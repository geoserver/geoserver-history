<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>

<table border=0><tr><td>

<html:link forward="dataConfigDataStores"><bean:message key="label.dataStores"/></html:link>
<BR>
<html:link forward="dataConfigNamespaces"><bean:message key="label.namespaces"/></html:link>
<BR>
<html:link forward="dataConfigStyles"><bean:message key="label.styles"/></html:link>
<BR>
<html:link forward="dataConfigFeatureTypes"><bean:message key="label.featureTypes"/></html:link>
<BR>

</td></tr></table>