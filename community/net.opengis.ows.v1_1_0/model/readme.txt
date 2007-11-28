Model generation steps:
- copy the ows schema to schemas. Don't copy any other dependend schema
- in _1.ecore change the name and ns prefix to v1_1_0 (using
  the sample editor)
- in _1.ecore, add the "baseUrl: String" attribute to GetCapabilities type.
  GeoServer will need it to handle proxies
- in ows11.genmodel we need to make a few changes
  - the _1 package resource type must be modified to be 
    "none" (otherwise xml marshalling code will be generated)
  - rename the base package to be net.opengis.ows and the prefix to be Ows11
  - change the compliance level to 1.4 (otherwise the generated code will use
    templates and we would have to upgrade EMF to a version that do support them)
