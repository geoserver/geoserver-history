Notes on how this model was built
----------------------------------------------

If the world was a nice an peaceful place the operations
needed to build this module would have been:
* copy the wcs 1.1 schema in the module
* create an EMF model that references the ows package
* make sure the resource type is none, so that no XML
  binding code is generated
* generate the model code
* done.

Unfortunately we're not so lucky for a variety of reasons.
Here are the actual steps:
* copy the WCS 1.1 schema in the module
* generate the EMF model... which is almost ok, but 
  the packages are net.opengis.wcs._1 and 
  net.opengis.wcs._1._1.ows
* (sigh) fix by hand the ecore files and the .genmodel
  file so that the packages become net.opengis.wcs and
  net.opengis.wcs
* generate the code
* looking at the code generated and the xsds it's evident
  that the fancy OGC folks duplicated part of the OWS
  schema almost verbatim and for no apparent good reason.
  ...