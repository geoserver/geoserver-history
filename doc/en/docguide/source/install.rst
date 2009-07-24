.. _install_sphinx:

Installing Sphinx
=================

In order to work with Sphinx and generate the HTML/PDF documentation you'll need the following:

* `Python <http://www.python.org/download/>`_
* :command:`easy_install` (included with Python `setuptools <http://pypi.python.org/pypi/setuptools>`_)
* `LaTeX <http://www.latex-project.org/>`_ installation with full extensions (in order to build PDF documentation)

Windows
-------

GeoServer documentation projects all include a :file:`make.bat` which provides much of the same functionality as the :command:`make` command.  If you wish to install :command:`make` anyway, it is available as part of the `MSYS <http://www.mingw.org/wiki/msys>`_ package.

#. Download and install Python. Though there are various distributions and versions, the `official version 2.5.4 <http://www.python.org/download/releases/2.5.4/>`_ has been tested and works as expected.

#. Download and install `setuptools for Python 2.5 <http://pypi.python.org/pypi/setuptools#downloads>`_

#. Put :command:`python` and :command:`setuptools` (and :command:`make` if you installed it) in your Path.  To do so, go to :menuselection:`Control Panel --> System --> Advanced --> Environment Variables`.  Look for ``PATH`` among the system variables, and add the installation locations to the end of the string.  For example, if :command:`python` is installed in :file:`C:\\Python` and :command:`setuptools` is installed in :file:`C:\\Python\\scripts`, add the following to the end of the string::
   
   ...;C:\Python;C:\Python\scripts
   
#. Open a command line window and run::
   
      easy_install sphinx

#. To test for a successful installation, in a command line window, navigate to your GeoServer source checkout, change to the :file:`doc\\user` directory, and run::
  
      make html
  
   This should generate HTML pages in the :file:`doc\\user\\build\\html` directory.

Ubuntu
------

.. note:: These instructions may work on other Linux distributions as well, but have not been tested.

#. Open a terminal and type the following command::
  
      sudo apt-get install make python-setuptools 
  
   Depending on your system this may trigger the installation of other packages.

#. Install Sphinx using :command:`easy_install`::
  
      sudo easy_install sphinx
  
#. To test for a successful installation, navigate to your GeoServer source checkout, go into the :file:`doc/user` directory and run::
  
      make html
  
   This should generate HTML pages in the :file:`doc/user/build/html` directory.

OS X
----

