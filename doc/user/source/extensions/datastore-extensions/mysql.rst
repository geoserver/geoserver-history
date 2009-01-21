.. _mysql_extension:

MySQL
=====

Introduction
------------

The MySQL datastore extension adds functionality to GeoServer to allow connection to `MySQL databases <http://www.mysql.com/>`_.


Installation
------------

#. Download the MySQL extension from the `GeoServer download page <http://geoserver.org/display/GEOS/Download>`_.  Make sure to match the extension version with your GeoServer version.
#. Extract the contents of the archive to the ``WEB-INF/lib`` directory of your GeoServer instance.

The extension will become active when GeoServer is restarted.


Configuration
-------------

When properly installed, a new type of datastore (**MySQL**) will be 
available.  Navigate to the **Create New Feature Data Set** page 
(**Config** -> **Data** -> **Datastore** -> **New**) and an option for 
**MySQL** will be in the dropdown menu for **Feature Data Set 
Description.** Select this option, enter a name in the box for **Feature 
Data Set ID**, and click **Next**.


.. figure:: mysqlcreate.png
   :align: center

   *Figure 1: Creating a new MySQL datastore*


The next page is the **Feature Data Set Editor** page. Fill out 
the form with information on the MySQL instance to connect to. When 
finished, click **Submit**, then **Apply** and **Save**. 

.. figure:: mysqlconfigure.png
   :align: center

   *Figure 2: Configuring a new MySQL datastore*
   

Table Structure
---------------

Before you can create a featuretype from your MySQL datastore, you will need to create a database table to contain your geospatial data (assuming you don't have one already).  The following SQL will create an appropriately-typed table::

   CREATE TABLE `name_of_featuretype` (
      `the_geom` geometry NOT NULL,
      `fid` varchar(255) NOT NULL,
      `description` varchar(2000) default NULL,
      PRIMARY KEY  (`fid`)
    ) ENGINE=MyISAM DEFAULT CHARSET=latin1
   
You may now add featuretypes as you would normally do, by navigating to 
the **Create New Feature Type** page (**Config** -> **Data** -> 
**Featuretype** -> **New**). 

  
Known Bugs
----------

* When creating a new featuretype be sure that the table has a primary key.  If the table lacks a primary key, the featureIDs returned will not work properly.

* Passwords for users are mandatory.

