Model generation steps:
- copy wcs and ows to schemas. Don't copy xlink and gml since
  these would be too big to generate. Some classes will bind
  to Object instead of an actual useful GML3 type, no problem,
  we'll fix the generated interfaces later and re-create the
  model out of them
- in _11.ecore change the name and ns prefix to v1_1_1 (using
  the sample editor)
- in wcs11.genmodel we need to make a few changes
  - the _1 package resource type must be modified to be 
    "none" (otherwise xml marshalling code will be generated)
  - rename the base package to be net.opengis.wcs and the prefix to be Wcs111
  - change the compliance level to 1.4 (otherwise the generated code will use
    templates and we would have to upgrade EMF to a version that do support them)