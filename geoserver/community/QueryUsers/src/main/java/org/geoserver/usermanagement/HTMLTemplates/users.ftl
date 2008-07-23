<html>
<head>
<title> Test Thingy for User List </title>
</head>
<body>
<h2> User List </h2> 
<ul>
<#list users as user>
<li> <a href="${page.currentURL}/${user}">${user}</a> </li>
</#list>
</ul>
</body>
</html>
