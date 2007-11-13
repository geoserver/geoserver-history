<html>
<head>
<title> Test Thingy for User List </title>
</head>
<body>
<h2> User List </h2> 
<ul>
<#list users as user>
<li> <a href=${currentURL}/${user.name}> ${user.name} </a> </li>
</#list>
</ul>
</body>
</html>
