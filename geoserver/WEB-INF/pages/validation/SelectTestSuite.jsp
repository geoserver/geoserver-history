<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table border=0>

	<html:form action="/config/validation/testSuiteSelect">
	<tr><td valign="top" align="right">
		<bean:message key="label.testSuite"/>:
	</td><td>
		<html:select property="selectedTestSuite">
			<html:options property="testSuites"/>
		</html:select>
	</td></tr>
	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:submit property="buttonAction">
			<bean:message key="label.edit"/>
		</html:submit>
	</td></tr>
	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:submit property="buttonAction">
			<bean:message key="label.delete"/>
		</html:submit>
	</td></tr>

	</html:form>

	<tr><td colspan=2><HR></td></tr>

	<html:form action="/config/validation/testSuiteNew">	
	<tr><td valign="top" align="right">
			<bean:message key="label.newName"/>:
		</td><td>
			<html:text property="newName" size="60"/>
	</td></tr>
	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:submit>
			<bean:message key="label.new"/>
		</html:submit>
	</td></tr>
	</html:form>
	

</table>
<table border="0">
<%
org.vfny.geoserver.action.validation.TestValidationResults tvr = (org.vfny.geoserver.action.validation.TestValidationResults)session.getAttribute(org.vfny.geoserver.action.validation.TestValidationResults.CURRENTLY_SELECTED_KEY);
if(tvr!=null && tvr.getErrors().size()>0){
%>
<table border="0">
<tr><td>ERRORS</td></tr>
<%if(tvr.isRun()){%>
<tr><td>RUN COMPLETED</td></tr>
<%}else{%>
<tr><td>RUN NOT COMPLETED</td></tr>
<%
}
java.util.Iterator i = tvr.getErrors().entrySet().iterator();
while(i.hasNext()){
  java.util.Map.Entry m = (java.util.Map.Entry)i.next();  
  String fid = ((org.geotools.feature.Feature)m.getKey()).getID();
  String message = m.getValue().toString(); // should be a string anyways
  %>
<tr><td><%=fid%></td><td><pre><code><%=message%></code></pre></td></tr>
  <%
} // while
%>
</table>
<%
} // if
%>
