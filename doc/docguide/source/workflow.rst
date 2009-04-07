.. _workflow:

Workflow
========

GeoServer documentation aims to mirror the development process of the software itself.  Source code is kept in an online repository, copied locally using version control software, edited and tested locally, and then committed back to the project. Documentation works the same way.


Check out source
----------------

This documentation source code exists in the same repository as the GeoServer source code::

   https://svn.codehaus.org/geoserver/

Within this path are the various ``branches`` and ``tags`` associated with releases.  So, for example, the documentation for the 1.7.x branch is found inside this path::

   https://svn.codehaus.org/geoserver/branches/1.7.x/doc/

Subversion
``````````

You must use a version control software to retrieve files.

.. warning:: Add info about installing/configuring SVN/TortoiseSVN  


Make edits
----------

Documentation in Sphinx is written using reStructuredText.  This is the "source code" for the documentation, and contains only content, leaving the theming to the build process.

.. warning:: Add details about RST

For suggestions about writing style, please see the :ref:`style_guidelines`. For tips and tricks about Sphinx, please see the :ref:`sphinx`.

   
Build locally
-------------

Sphinx builds the RST files into themed HTML pages and PDF sites.  You can install Sphinx on your local system to build the documentation locally and immediately view changes.  Also, a nightly build of the existing documentation is available on `docs.geoserver.org <http://docs.geoserver.org>`_, but it is *strongly* recommended to view any changes made locally before committing them.

.. warning:: Add info about installing Sphinx and testing the build

Commit changes
--------------

The final step is to commit the changes to the official repository.

.. note:: You must have commit rights to do this.  Please see the section on :ref:`contributing` for details on how to obtain this.

.. warning:: Add directions on how to commit.

