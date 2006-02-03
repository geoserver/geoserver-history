<%@ include file="include.jsp" %>

<script language="JavaScript">

function dataStoreChanged() {
	var ds = document.forms[0].dataStore;
	var id = ds.options[ds.selectedIndex].value;
	
	for (var i = 0; i < document.forms[0].elements.length; i++) {
		var element = document.forms[0].elements[i];
		alert(element.type);
	}
	
	//document.getElementById("description").innerHTML  = desc;
}
	
</script>	

<form method="post">

	<select name="dataStore" onchange="dataStoreChanged()">
	
	<c:forEach var="ds" items="${datastores}">
		<option value='<c:out value="${ds.displayName}"/>'><c:out value="${ds.displayName}"/>
		<input type="hidden" id="${ds.displayName}" value="${ds.description}"/>
	</c:forEach>
	
	</select>
	
	<input type="submit">
	
	<div id="description"></div>
</form>


