<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>





<table border=0>

	<html:form action="/config/validation/test">
	
	<tr><td valign="top" align="right">
		<bean:message key="label.currentTestSuite"/>:
	</td><td>
		<bean:write name="selectedTestSuite" property="name"/>
	</td></tr>
	
	<tr><td valign="top" align="right">
		<bean:message key="label.testName"/>:
	</td><td>
		<html:select property="selectedTest">
			<html:options property="tests"/>
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
	<tr><td colspan=2><hr></td></tr>
	</html:form>
	
	<html:form action="/config/validation/testNew">
	
	
	<tr><td valign="top" align="right">
		<bean:message key="label.newName"/>:
	</td><td>
		<html:text property="newName" size="60"/>
	</td></tr>

<logic:iterate id="param" 
               indexId="index"
               name="validationTestNewForm"
               property="plugInConfigs">

	<tr><td valign="top" align="right">
		<bean:write name="param" property="name"/>:
		<html:radio idName="param" property="selectedPlugIn" value="name"/>
	</td><td>
		<bean:write name="param" property="description"/>
	</td></tr>

</logic:iterate>

	<tr><td>&nbsp;</td><td valign="top" align="left">
		<html:submit>
			<bean:message key="label.new"/>
		</html:submit>
	</td></tr>
	</html:form>
	
	

</table>
