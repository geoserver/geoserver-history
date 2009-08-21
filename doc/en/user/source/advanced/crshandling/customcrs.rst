.. _crs_custom:

Custom CRS Definitions
======================

Add a custom CRS
----------------

This example shows how to add a custom projection in GeoServer.

#. The projection parameters need to be provided as a WKT (well known text) definition.  The code sample below is just an example::

      PROJCS["NAD83 / Austin",
        GEOGCS["NAD83",
          DATUM["North_American_Datum_1983",
            SPHEROID["GRS 1980", 6378137.0, 298.257222101],
            TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]],
          PRIMEM["Greenwich", 0.0],
          UNIT["degree", 0.017453292519943295],
          AXIS["Lon", EAST],
          AXIS["Lat", NORTH]],
        PROJECTION["Lambert_Conformal_Conic_2SP"],
        PARAMETER["central_meridian", -100.333333333333],
        PARAMETER["latitude_of_origin", 29.6666666666667],
        PARAMETER["standard_parallel_1", 31.883333333333297],
        PARAMETER["false_easting", 2296583.333333],
        PARAMETER["false_northing", 9842500.0],
        PARAMETER["standard_parallel_2", 30.1166666666667],
        UNIT["m", 1.0],
        AXIS["x", EAST],
        AXIS["y", NORTH],
        AUTHORITY["EPSG","100002"]]

   .. note:: This code sample has been formatted for readability.  The information will need to be provided on a single line instead, or with backslash characters at the end of every line (except the last one).

#. Go into the :file:`user_projections` directory inside your data directory, and open the :file:`epsg.properties` file.  If this file doesn't exist, you can create it.

#. Insert the code WKT for the projection at the end of the file (on a single line or with backslash characters)::

      100002=PROJCS["NAD83 / Austin", \
        GEOGCS["NAD83", \
          DATUM["North_American_Datum_1983", \
            SPHEROID["GRS 1980", 6378137.0, 298.257222101], \
            TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]], \
          PRIMEM["Greenwich", 0.0], \
          UNIT["degree", 0.017453292519943295], \
          AXIS["Lon", EAST], \
          AXIS["Lat", NORTH]], \
        PROJECTION["Lambert_Conformal_Conic_2SP"], \
        PARAMETER["central_meridian", -100.333333333333], \
        PARAMETER["latitude_of_origin", 29.6666666666667], \
        PARAMETER["standard_parallel_1", 31.883333333333297], \
        PARAMETER["false_easting", 2296583.333333], \
        PARAMETER["false_northing", 9842500.0], \
        PARAMETER["standard_parallel_2", 30.1166666666667], \
        UNIT["m", 1.0], \
        AXIS["x", EAST], \
        AXIS["y", NORTH], \
        AUTHORITY["EPSG","100002"]]

.. note:: Note the number that precedes the WKT.  This will determine the EPSG code.  So in this example, the EPSG code is 100002.

#. Save the file.

#. Restart GeoServer.

#. Verify that the CRS has been properly parsed by navigating to the :ref:`srs_list` page in the :ref:`web_admin`.

#. If the projection wasn't listed, examine the logs for any errors.

Override an official EPSG code
------------------------------

In some situations it is necessary to override an official EPSG code with a custom definition.  A common case is the need to change the TOWGS84 parameters in order to get better reprojection accuracy in specific areas.

The GeoServer referencing subsystem checks the existence of another property file, :file:`epsg_overrides.properties`, whose format is the same as :file:`epsg.properties`. Any definition contained in :file:`epsg_overrides.properties` will **override** the EPSG code, while definitions stored in :file:`epsg.proeprties` can only **add** to the database.