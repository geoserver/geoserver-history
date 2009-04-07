.. _workflow:

Workflow
========


Content
```````

Documentation in Sphinx is written using reStructuredText.  ReStructuredText (or RST) is a _________.  This is the "source code" for the documentation, and contains mostly content.  This documentation source code exists in the same repository as the GeoServer source code::

   https://svn.codehaus.org/geoserver/

Within this path are the various ``branches`` and ``tags`` associated with releases.  So, for example, the source code for the ``1.7.x`` is found at this link::

   https://svn.codehaus.org/geoserver/branches/1.7.x/
   
And the documentation is found inside the ``doc`` subdirectory::

   https://svn.codehaus.org/geoserver/branches/1.7.x/doc/
   
The 1.7.3 documentation is at this link::

   https://svn.codehaus.org/geoserver/tags/1.7.3/doc/

Building
````````

Sphinx builds the RST files into themed HTML pages and PDF sites.  You can install Sphinx on your local system to build the documentation locally and immediately view changes.  Also, a nightly build of the documentation is available _________________, but it is best to view changes before committing them.



