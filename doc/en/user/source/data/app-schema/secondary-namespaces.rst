.. _app-schema.secondary-namespaces:

Secondary Namespaces
====================

In order to allow GeoServer App-Schema to support secondary namespaces, please follow the steps outlined below:

Using the sampling namespace as an example.

Step 1:Create the Secondary Namespace folder
````````````````````````````````````````````
Create a folder to represent the secondary namespace in the data/ workspaces directory, 
in our example that will be the "sa" folder

Step 2:Create files
````````````````````
Create  two files below in the "sa" folder: 

#. namespace.xml
#. workspace.xml

Step3:Edit content of files
```````````````````````````

Contents of these files are as follows:

namespace.xml (uri is a valid uri for the secondary namespace, in our case the sampling namespace uri)::

   <namespace>
		<id>sa_namespace</id>
		<prefix>sa</prefix>
		<uri>http://www.opengis.net/sampling/1.0</uri>
  </namespace> 
	
workspace.xml::

	<workspace>
		<id>sa_workspace</id>	
		<name>sa</name>
	</workspace> 

That's it. 

You have now set up your workspace to use a Secondary Namespace.
