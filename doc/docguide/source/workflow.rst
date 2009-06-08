.. _workflow:

Workflow
========

GeoServer documentation aims to mirror the development process of the software itself.  The process for writing/editing documentation is as follows:

* **Step 1**: Copy source locally using version control software
* **Step 2**: Make any changes
* **Step 3**: Test to make sure that changes are correct
* **Step 4**: Commit changes back to the repository
   


Checking out source
-------------------

Repository
``````````

This documentation source code exists in the same repository as the GeoServer source code::

   https://svn.codehaus.org/geoserver/

Within this path are the various ``branches`` and ``tags`` associated with releases.  So, for example, the documentation for the 1.7.x branch is found inside this path::

   https://svn.codehaus.org/geoserver/branches/1.7.x/doc/
   
When making changes to the 1.7.x branch, this is the path to checkout.

Software
````````

You must use a version control software to retrieve files.  Most people use `Subversion <http://subversion.tigris.org/>`_ (aka "svn"), a command line utility for managing version control systems.  There also exists a shell-integrated version of Subversion for Windows called `TortoiseSVN <http://tortoisesvn.tigris.org/>`_

.. warning:: Add info about installing/configuring SVN


Making edits
------------

Documentation in Sphinx is written in `reStructuredText <http://docutils.sourceforge.net/rst.htm>`_, a lightweight markup syntax.  For suggestions on writing reStructuredText for use with Sphinx, please see the section on :ref:`sphinx`.  For suggestions about writing style, please see the :ref:`style_guidelines`. 


Testing locally
---------------

You should install Sphinx on your local system to build the documentation locally and view any changes made.  Sphinx builds the reStructuredText files into HTML pages and PDF files.

.. warning:: Add info about installing Sphinx and testing the build

Committing changes
------------------

The final step is to commit the changes to the official repository.  If you are using Subversion, the command to use is::

   svn commit [path/files]
   
where ``[path/files]`` is the path and files you wish to commit back to the repository.

.. note:: You must have commit rights to do this.  Please see the section on :ref:`contributing_commit_rights` for details.

