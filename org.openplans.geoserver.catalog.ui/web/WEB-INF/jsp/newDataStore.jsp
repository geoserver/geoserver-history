<%@ include file="include.jsp" %>

<script language="JavaScript">

function dataStoreChanged() {
	var ds = document.forms[0].datastores;
	document.getElementById("description").innerHTML  = ds.options[ds.selectedIndex].value;
}
	
</script>	

<form>

	<select name="datastores" onchange="dataStoreChanged()">
	
	<c:forEach var="datastore" items="${datastores}">
		
		<option value='<c:out value="${datastore.description}"/>'><c:out value="${datastore.displayName}"/>
	</c:forEach>
	
	</select>
	
	<input type="submit">
	
	<div id="description"></div>
</form>


