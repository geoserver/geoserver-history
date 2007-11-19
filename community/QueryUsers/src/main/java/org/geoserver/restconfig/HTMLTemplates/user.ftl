<html>
<head>
<title> Test Thingy for User List </title>
</head>
<body>
<h2> User Details </h2> 
<ul>
<li> Password : <span>${password}</span> </li>
<li> Roles <ul>
<#list roles as role>
  <li>${role}</li>
</#list>
</ul>
</li>
</ul>
</body>
</html>
