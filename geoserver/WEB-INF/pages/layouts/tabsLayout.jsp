<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%-- 
  Tabs Layout .
  This layout allows to render several tiles in a tabs fashion.
  @param tabList A list of available tabs. We use MenuItem to carry data (name, body, icon, ...)
  @param selectedIndex Index of default selected tab
  @param parameterName Name of parameter carrying selected info in http request.
--%>

<%-- 
Use tiles attributes, and declare them as page java variable.
These attribute must be passed to the tile. 
--%>

<tiles:useAttribute name="parameterName" classname="java.lang.String" />
<tiles:useAttribute id="selectedIndexStr" name="selectedIndex" ignore="true" classname="java.lang.String" />
<tiles:useAttribute name="tabList" classname="java.util.List" />
<%
  String selectedColor="#98ABC7";
  String notSelectedColor="#C0C0C0";
  
  int index = 0; // Loop index
  int selectedIndex = 0;
    // Check if selected come from request parameter
  try {
    selectedIndex = Integer.parseInt(selectedIndexStr);
    selectedIndex = Integer.parseInt(request.getParameter( parameterName ));
	}
   catch( java.lang.NumberFormatException ex )
    { // do nothing
	}
  // Check selectedIndex bounds
  if( selectedIndex < 0 || selectedIndex >= tabList.size() ) selectedIndex = 0;
  String selectedBody = ((org.apache.struts.tiles.beans.MenuItem)tabList.get(selectedIndex)).getLink(); // Selected body
  
%>

<table border="0"  cellspacing="0" cellpadding="0">
  <%-- Draw tabs --%>
<tr>
  <td width="10">&nbsp;</td>
  <td>
    <table border="0"  cellspacing="0" cellpadding="5">
      <tr>
<logic:iterate id="tab" name="tabList" type="org.apache.struts.tiles.beans.MenuItem" >
<% // compute href
 // NO- this will not work with our code. We must have the href point back at an action.
 //   String href = request.getRequestURI() + "?"+parameterName + "=" + index;
    // Don't add request URI prefix , but let the client compute the original URL
	// This allows to use a Struts action as page URL, and perform a forward.
	// Bug reported by Don Peterkofsky 
  //String href = "" + "?"+parameterName + "=" + index;
  
  
	String href = tab.getLink();
  
  String color = notSelectedColor;
  if( index == selectedIndex )
    {
	selectedBody = tab.getLink();
	color = selectedColor;
	} // enf if

%>
  <td bgcolor="<%=color%>">
  
  <% if (index != selectedIndex) { %>
  <a href="<%=href%>" >
  <% } %>
  
  <%=tab.getValue()%>
  
  <% if (index != selectedIndex) { %>
  </a>
  <% } 
    index++;
  %>
  
  </td>
  <td width="1" ></td>
  
</logic:iterate>
      </tr>
    </table>
  </td>
  <td width="10" >&nbsp;</td>
</tr>


<tr>
  <td height="5" bgcolor="<%=selectedColor%>" colspan="3" >&nbsp;</td>
</tr>  

  <%-- Draw body --%>
<tr>
  <td width="10" bgcolor="<%=selectedColor%>">&nbsp;</td>
  <td>
  <tiles:insert name="<%=selectedBody%>" flush="true" />
  </td>
  <td width="10" bgcolor="<%=selectedColor%>">&nbsp;</td>
</tr>  

<tr>
  <td height="5" bgcolor="<%=selectedColor%>" colspan="3" >&nbsp;</td>
</tr>  

</table>

