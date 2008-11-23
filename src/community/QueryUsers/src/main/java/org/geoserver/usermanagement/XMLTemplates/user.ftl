<?xml version="1.0"?>
<user>
  <password>${password}</password>
  <roles>
    <#foreach role in roles><role name="${role}"/></#foreach>
  </roles>
</user>
