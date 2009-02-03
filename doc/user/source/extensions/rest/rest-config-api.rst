.. _rest_config_api_ref:

REST Configuration API Reference
================================

General notes
-------------

A ``format`` specifies how a resource should be represented. In a GET
operation the format specifies what representation of the resource
should be returned to the client. In a POST or PUT request the format
specifies which representation of a resource is being sent to the server.

In a GET operation the format is specified via file extension. For example
consider the resource "foo". To request a representation of foo as XML the 
request uri would end with "foo.xml". To request as JSON the request uri
would end with "foo.json". 

In a POST or PUT operation, the format is specified with the ``Content-type``
header. To send a representation in XML, the content type "text/xml" or
"application/xml" would be used. To send a representation as JSON the 
content type "text/json" would be used.

Workspaces
----------

A ``workspace`` is a grouping of data stores. More commonly known as a 
namespace, it is commonly used to group data that is related in some way.

.. note::

   For GeoServer 1.x a workspace can be considered the equivalent of a
   namespace, and the two are kept in sync. For example, the namespace
   "topp, http://openplans.org/topp" corresponds to the workspace "topp".


Formats
^^^^^^^

- :ref:`HTML <workspace_html>`
- :ref:`XML <workspace_xml>`
- :ref:`JSON <workspace_json>`

Operations
^^^^^^^^^^

``/workspaces[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - List all workspaces
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - Create a new workspace
     - 201 with ``Location`` header 
     - XML, JSON
     - 
   * - PUT
     -
     - 405
     -
     -
   * - DELETE
     -
     - 405
     -
     -

``/workspaces/<ws>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Returns workspace ``ws``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     -
     - 405
     -
     -
   * - PUT
     - 200
     - Modify workspace ``ws``
     - XML, JSON
     -
   * - DELETE
     - 200
     - Delete workspace ``ws``
     - XML, JSON
     -

*Exceptions*:

- GET for a workspace that does not exist -> 404
- PUT that changes name of workspace -> 403
- DELETE against a workspace that is non-empty -> 403

``/workspaces/default[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Returns default workspace
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     -
     - 405
     -
     -
   * - PUT
     - 200
     - Set default workspace
     - XML, JSON
     -
   * - DELETE
     -
     - 405
     -
     -

Data stores
-----------

A ``data store`` is a source of spatial data that is vector based. It can be a 
file in the case of a Shapefile, a database in the case of PostGIS, or a 
server in the case of a remote Web Feature Service.

Formats
^^^^^^^

- :ref:`HTML <datastore_html>`
- :ref:`XML <datastore_xml>`
- :ref:`JSON <datastore_json>`

Operations
^^^^^^^^^^

``/workspaces/<ws>/datastores[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - List all data stores in workspace ``ws``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - Create a new data store
     - 201 with ``Location`` header 
     - XML, JSON
     - 
   * - PUT
     -
     - 405
     -
     -
   * - DELETE
     -
     - 405
     -
     -

``/workspaces/<ws>/datastores/<ds>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return data store ``ds``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - 
     - 405
     - 
     - 
   * - PUT
     - Modify data store ``ds``
     -
     -
     -
   * - DELETE
     - Delete data store ``ds``
     -
     -
     -

*Exceptions*:

- GET for a data store that does not exist -> 404
- PUT that changes name of data store -> 403
- PUT that changes workspace of data store -> 403
- DELETE against a data store that contains configured feature types -> 403


Feature types
-------------

A ``feature type`` is a vector based spatial resource or data set that
originates from a data store. In some cases, like Shapefile, a feature type
has a one-to-one relationship with its data store. In other cases, like
PostGIS, the relationship of feature type to data store is many-to-one, with
each feature type corresponding to a table in the database.

Formats
^^^^^^^

- :ref:`HTML <featuretype_html>`
- :ref:`XML <featuretype_xml>`
- :ref:`JSON <featuretype_json>`

Operations
^^^^^^^^^^

``/workspaces/<ws>/datastores/<ds>/featuretypes[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
     - Parameters
   * - GET
     - List all feature types in datastore ``ds``
     - 200
     - HTML, XML, JSON
     - HTML
     - :ref:`list <list_parameter>`
   * - POST
     - Create a new feature type
     - 201 with ``Location`` header
     - XML, JSON
     - 
     - 
   * - PUT
     -
     - 405
     -
     -
     -
   * - DELETE
     -
     - 405
     -
     -
     -
   
.. _list_parameter:

The ``list`` parameter is used to control the category of feature types that 
are returned. It can take one of the three values "configured", "available", or "all".

- ``configured`` - Only setup or configured feature types are returned. This
  is the default value.
- ``available`` - Only unconfigured feature types (not yet setup) but are 
  available from the specified datastore  will be returned.
- ``all`` - The union of ``configured`` and ``available``.

``/workspaces/<ws>/datastores/<ds>/featuretypes/<ft>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return feature type ``ft``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     -
     - 405
     -
     -
   * - PUT
     - Modify feature type ``ft``
     - 200
     - XML,JSON
     - 
   * - DELETE
     - Delete feature type ``ft``
     - 200
     -
     -

*Exceptions*:

- GET for a feature type that does not exist -> 404
- PUT that changes name of feature type -> 403
- PUT that changes data store of feature type -> 403


Coverage stores
---------------

A ``coverage store`` is a source of spatial data that is raster based.

Formats
^^^^^^^

- :ref:`HTML <coveragestore_html>`
- :ref:`XML <coveragestore_xml>`
- :ref:`JSON <coveragestore_json>`

Operations
^^^^^^^^^^

``/workspaces/<ws>/coveragestores[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - List all coverage stores in workspace ``ws``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - Create a new coverage store
     - 201 with ``Location`` header 
     - XML, JSON
     - 
   * - PUT
     -
     - 405
     -
     -
   * - DELETE
     -
     - 405
     -
     -

``/workspaces/<ws>/coveragestores/<cs>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return coverage store ``cs``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - 
     - 405
     - 
     - 
   * - PUT
     - Modify coverage store ``cs``
     -
     -
     -
   * - DELETE
     - Delete coverage store ``ds``
     -
     -
     -

*Exceptions*:

- GET for a coverage store that does not exist -> 404
- PUT that changes name of coverage store -> 403
- PUT that changes workspace of coverage store -> 403
- DELETE against a coverage store that contains configured coverage -> 403


Coverages
---------

A ``coverage`` is a raster based data set which originates from a coverage 
store.

Formats
^^^^^^^

- :ref:`HTML <coverage_html>`
- :ref:`XML <coverage_xml>`
- :ref:`JSON <coverage_json>`

Operations
^^^^^^^^^^

``/workspaces/<ws>/coveragestores/<cs>/coverages[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - List all coverages in coverage store ``cs``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - Create a new coverage
     - 201 with ``Location`` header
     - XML, JSON
     - 
   * - PUT
     -
     - 405
     -
     -
   * - DELETE
     -
     - 405
     -
     -
   
``/workspaces/<ws>/coveragestores/<cs>/coverages/<c>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return coverage ``c``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     -
     - 405
     -
     -
   * - PUT
     - Modify coverage ``c``
     - 200
     - XML,JSON
     - 
   * - DELETE
     - Delete coverage ``c``
     - 200
     -
     -

*Exceptions*:

- GET for a coverage that does not exist -> 404
- PUT that changes name of coverage -> 403
- PUT that changes coverage store of coverage -> 403

Styles
------

A ``style`` describes how a resource (feature type or coverage) should be 
symbolized or rendered by a Web Map Service. In GeoServer styles are 
specified with :ref:`SLD <styling>`.

Formats
^^^^^^^

- :ref:`SLD <style_sld>`
- :ref:`HTML <style_html>`
- :ref:`XML <style_xml>`
- :ref:`JSON <style_json>`

Operations
^^^^^^^^^^

``/styles[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
     - Parameters
   * - GET
     - Return all styles
     - 200
     - HTML, XML, JSON
     - HTML
     -
   * - POST
     - Create a new style
     - 201 with ``Location`` header
     - SLD, XML, JSON
     -
     - :ref:`name <name_parameter>`
   * - PUT
     - 
     - 405
     - 
     - 
     -
   * - DELETE
     - 
     - 405
     -
     -
     -

.. _name_parameter:

The ``name`` parameter specifies the name to be given to the style. This 
option is most useful when POSTing a style in SLD format, and an appropriate
name can be not be inferred from the SLD itself.

``/styles/<s>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return style ``s``
     - 200
     - SLD, HTML, XML, JSON
     - HTML
   * - POST
     - 
     - 405
     -
     -
   * - PUT
     - Modify style ``s`` 
     - 200
     - SLD,XML,JSON
     - 
   * - DELETE
     - Delete style ``s``
     - 200
     -
     -

*Exceptions*:

- GET for a style that does not exist -> 404
- PUT that changes name of style -> 403
- DELETE against style which is referenced by existing layers -> 403


Layers
------

A ``layer`` is a *published* resource (feature type or coverage). 

.. note::

   In GeoServer 1.x a layer can considered the equivalent of a feature type or
   a coverage. In GeoServer 2.x, the two will be separate entities, with the 
   relationship from a feature type to a layer being one-to-many.

Formats
^^^^^^^

- :ref:`HTML <layer_html>`
- :ref:`XML <layer_xml>`
- :ref:`JSON <layer_json>`

Operations
^^^^^^^^^^

``/layers[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return all layers
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     -
     - 405
     - 
     -
   * - PUT
     - 
     - 405
     - 
     - 
   * - DELETE
     - 
     - 405
     -
     -

``/layers/<l>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return layer ``l``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - 
     - 405
     -
     -
   * - PUT
     - Modify layer ``l`` 
     - 200
     - XML,JSON
     - 
   * - DELETE
     -
     - 405
     -
     -

*Exceptions*:

- GET for a layer that does not exist -> 404
- PUT that changes name of layer -> 403
- PUT that changes resource of layer -> 403

``/layers/<l>/styles[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return all styles for layer ``l``
     - 200
     - SLD, HTML, XML, JSON
     - HTML
   * - POST
     - Add a new style to layer ``l``
     - 201, with ``Location`` header
     - XML, JSON
     -
   * - PUT
     - 
     - 405
     - 
     - 
   * - DELETE
     -
     - 405
     -
     -

Layer groups
------------

A ``layer group`` is a grouping of layers and styles that can be accessed as a 
single layer in a WMS GetMap request. A Layer group is often referred to as a 
"base map".

Formats
^^^^^^^

- :ref:`HTML <layergroup_html>`
- :ref:`XML <layergroup_xml>`
- :ref:`JSON <layergroup_json>`

Operations
^^^^^^^^^^

``/layergroups[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return all layer groups
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - Add a new layer group
     - 201, with ``Location`` header
     - XML,JSON
     -
   * - PUT
     - 
     - 405
     - 
     - 
   * - DELETE
     -
     - 405
     -
     -

``/layergroups/<lg>[.<format>]``

.. list-table::
   :header-rows: 1

   * - Method
     - Action
     - Return Code
     - Formats
     - Default Format
   * - GET
     - Return layer group ``lg``
     - 200
     - HTML, XML, JSON
     - HTML
   * - POST
     - 
     - 405
     -
     -
   * - PUT
     - Modify layer group ``lg``
     - 200
     - XML,JSON
     - 
   * - DELETE
     - Delete layer group ``lg``
     - 200
     -
     -

*Exceptions*:

- GET for a layer group that does not exist -> 404
- POST that specifies layer group with no layers -> 400
- PUT that changes name of layer group -> 403 

