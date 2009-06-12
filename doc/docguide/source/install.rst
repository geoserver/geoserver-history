.. _install_sphinx:

Installing and working with Sphinx
==================================

In order to work with Sphix and generate the html documentation you'll need the following:

* a Python install
* easy-install (included in Python setup-tools)
* a make utility

If you need to generate the PDF version you'll also need a Latex install with full extensions.
The following provides some information on how to install Sphinx on some operating systems. 

Installing on Windows
---------------------

The following steps should give you a working Sphinx install:

* Download and install `MSYS <http://www.mingw.org/wiki/msys>`_. The `MSYS-1.0.10 <http://downloads.sourceforge.net/mingw/MSYS-1.0.10.exe>`_ package will be enoug, no need to install MinGW as well. This will provide the :command:`make` utility as well as other common Unix commands.
* Download and install Python. There are various distributions and various versions, the `official 2.5.4 installer <http://www.python.org/download/releases/2.5.4/>`_ has been tested and works as expected.
* Download and install `setup tools for Python 2.5 <http://pypi.python.org/pypi/setuptools#downloads>`_
* Put all of the above tools in the PATH. To do so, click on ``My computer``, ``Advanced``, ``Enviroment variables``, look for ``PATH`` among the system variables and add the following at the end of it (adapt the path to your effective install)::
   
   ...;C:\msys\1.0\bin;C:\Python25;C:\Python25\scripts
   
* open a terminal window and run::
   
   easy_install sphinx
   

Now you should have a proper enviroment to install and run Sphinx. To build the docs enter your GeoServer checkout, go into the :file:`doc/user` directory and run::
  
  make html
  
This should generate the HTML docs in the :file:`doc/userbuild/html` directory.

Installing on Ubuntu
--------------------

To install on Ubuntu run, in a terminal::
  
  sudo apt-get install make python-setuptools 
  
Depending on your system this may trigger the installation of other packages or just inform you ``make`` or ``python-setuptools`` are already installed. Then install Sphinx using::
  
  sudo easy_install sphinx
  
Now you should have a proper enviroment to install and run Sphinx. To build the docs enter your GeoServer checkout, go into the :file:`doc/user` directory and run::
  
  make html
  
This should generate the HTML docs in the :file:`doc/userbuild/html` directory.
