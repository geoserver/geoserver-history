<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border="0">
<%
try {
org.vfny.geoserver.action.validation.TestValidationResults tvr =
	(org.vfny.geoserver.action.validation.TestValidationResults) session.getAttribute(org.vfny.geoserver.action.validation.TestValidationResults.CURRENTLY_SELECTED_KEY);
if(tvr!=null && tvr.getErrors().size()>0){
%>
<table border="0">
<tr><td><bean:message key="config.validation.displayResults.errors"/></td></tr>
<%if(tvr.isRun()){%>
<tr><td><bean:message key="config.validation.displayResults.runCompleted"/></td></tr>
<%}else{%>
<tr><td><bean:message key="config.validation.displayResults.runNotCompleted"/></td></tr>
<%
}
java.util.Iterator i = tvr.getErrors().entrySet().iterator();
while(i.hasNext()){
  java.util.Map.Entry m = (java.util.Map.Entry)i.next();  
  org.geotools.feature.Feature feature = (org.geotools.feature.Feature) m.getKey();
  String fid = feature != null ? feature.getID() : "(problem)";
  Object msg = m.getValue();
  String message = msg != null ? m.toString() : "an error has occured";
  %>
<tr><td><%=fid%></td><td><code><%=org.vfny.geoserver.action.HTMLEncoder.encode(message)%></code></td></tr>
  <%
} // while
%>
</table>
<%
} // if
} catch( NullPointerException bad){
	bad.printStackTrace();
}
%>