.. _filtering:

Filtering data in GeoServer
===========================

Data filtering in GeoServer is based on the concepts found in the `OGC Filter Encoding Spefication <http://www.opengeospatial.org/standards/filter>`_, which we suggest the reader to get familiar with.

In particular GeoServer accepts filters encoded in three different languages:
  
- *OGC Filter encoding specification v 1.0*, used in WFS 1.0 and SLD 1.0
- *OGC Filter encoding specification v 1.1*, used in WFS 1.1
- *CQL*, a plain text language created for the OGC Catalog specification and adapted to be a general and easy to use filtering mechanism. A quicka quick _cql_tutorial_ is also available in this guide.

We suggest to look into the respective specifications for details.

All specifications contain a concept, the *filter function*, that needs to be specifically documented for each server. A *filter function* 
is a function, with arguments, that can be called inside of a filter to perform specific calculations. It can be a trigonometric function, a string formatting one, a geometry buffer. The filter specification does not mandate specific functions and any server is free to provide whatever function it wants.

It turns out GeoServer has tens on them. This document contains a :doc:`function`.
