<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<span class="geoserverConfiguration">
<BR>
	<html:link forward="config.wfs"><bean:message key="label.wfsConfig"/></html:link><BR>
<BR>
	<html:link forward="config.wms"><bean:message key="label.wmsConfig"/></html:link><BR>
<BR>
	<html:link forward="config.data"><bean:message key="label.dataConfig"/></html:link><BR>
	
<table border=0>
	<html:form action="/config/geoServerSubmit">
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.maxFeatures"/>">
			<bean:message key="label.maxFeatures"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="maxFeatures" size="60"/>
	</td></tr>
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.verbose"/>">
			<bean:message key="label.verbose"/>:
		</span>
	</td><td align="left" valign="center">
		<html:checkbox property="verbose"/>
	</td></tr>
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.numDecimals"/>">
			<bean:message key="label.numDecimals"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="numDecimals" size="60"/>
	</td></tr>
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.charset"/>">
			<bean:message key="label.charset"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="charset" size="60"/>
	</td></tr>
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.baseURL"/>">
			<bean:message key="label.baseURL"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="baseURL" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.schemaBaseURL"/>">
			<bean:message key="label.schemaBaseURL"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="schemaBaseURL" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.loggingLevel"/>">
			<bean:message key="label.loggingLevel"/>:
		</span>
	</td><td align="left" valign="center">
		<html:select property="loggingLevel">
			<html:option value="ALL"/>
			<html:option value="SEVERE"/>
			<html:option value="WARNING"/>
			<html:option value="INFO"/>
			<html:option value="CONFIG"/>
			<html:option value="FINE"/>
			<html:option value="FINER"/>
			<html:option value="FINEST"/>
			<html:option value="OFF"/>			
		</html:select>
	</td></tr>	
	
	<tr><td colspan=2><center><b><bean:message key="label.contactInformation"/></b></center></td></tr>
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.contactPerson"/>">
			<bean:message key="label.contactPerson"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="contactPerson" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.contactOrganization"/>">
			<bean:message key="label.contactOrganization"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="contactOrganization" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.contactPosition"/>">
			<bean:message key="label.contactPosition"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="contactPosition" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.addressType"/>">
			<bean:message key="label.addressType"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="addressType" size="60"/>
	</td></tr>	
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.address"/>">
			<bean:message key="label.address"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="address" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.addressCity"/>">
			<bean:message key="label.addressCity"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="addressCity" size="60"/>
	</td></tr>	
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.addressState"/>">
			<bean:message key="label.addressState"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="addressState" size="60"/>
	</td></tr>		

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.addressPostalCode"/>">
			<bean:message key="label.addressPostalCode"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="addressPostalCode" size="60"/>
	</td></tr>		

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.addressCountry"/>">
			<bean:message key="label.addressCountry"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="addressCountry" size="60"/>
	</td></tr>	
	
	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.contactVoice"/>">
			<bean:message key="label.contactVoice"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="contactVoice" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.contactFacsimile"/>">
			<bean:message key="label.contactFacsimile"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="contactFacsimile" size="60"/>
	</td></tr>	

	<tr><td align="right" valign="center">
		<span class="help" title="<bean:message key="help.global.contactEmail"/>">
			<bean:message key="label.contactEmail"/>:
		</span>
	</td><td align="left" valign="center">
		<html:text property="contactEmail" size="60"/>
	</td></tr>	
		
	<tr><td align="right">&nbsp;</td><td valign="center" align="left">
		<html:submit>
			<bean:message key="label.submit"/>
		</html:submit>
		
		<html:reset>
			<bean:message key="label.reset"/>
		</html:reset>
	</td></tr>	
	
	</html:form>
	<TR><TD colspan=2>
	<h1>Welcome to Error Land</h1>
</TD></TR>
<TR><TD colspan=2>
	DataStore Errors:
</TD></TR>
	<tr><td align="right">	
		<logic:iterate id="key" indexId="ctr" name="GeoServer.ApplicationState" property="dataStoreErrorKeys">
			<nobr><%= key %></nobr><br>
		</logic:iterate>
	</td><td colspan=2 align="left">
		<logic:iterate id="value" indexId="ctr" name="GeoServer.ApplicationState" property="dataStoreErrorValues">
			<nobr><%= value %></nobr><br>
		</logic:iterate>
	</td></tr>	
	
<TR><TD colspan=2>
	NameSpace Errors:
</TD></TR>
	<tr><td align="right">	
		<logic:iterate id="key" indexId="ctr" name="GeoServer.ApplicationState" property="nameSpaceErrorKeys">
			<nobr><%= key %></nobr><br>
		</logic:iterate>
	</td><td colspan=2 align="left">
		<logic:iterate id="value" indexId="ctr" name="GeoServer.ApplicationState" property="nameSpaceErrorValues">
			<nobr><%= value %></nobr><br>
		</logic:iterate>
	</td></tr>	




</table>
</td></tr></table>