.. _filter_function_reference:

Filter Function Reference
==========================

This page contains a reference to filter functions that can be used in WFS filtering or in SLD expressions.
If a function reported by the WFS capabilities is not available in this list it might either mean that the
function cannot actually be used for the above purposes, or that it's new and has not been documented still.
Ask for details on the user mailing list.

Function argument type reference
---------------------------------

.. list-table::
   :widths: 20 80
   
   * - **Type**
     - **Description**
   * - Double
     - Floating point number, 8 bytes, IEEE 754. ranging from 4.94065645841246544e-324d to 1.79769313486231570e+308d
   * - Float
     - Floating point number, 4 bytes, IEEE 754. ranging from 1.40129846432481707e-45 to 3.40282346638528860e+38. Smaller range and less accurate than Double.
   * - Integer
     - Integer number, ranging from -2,147,483,648 to 2,147,483,647
   * - Long
     - Integer number, ranging from -9,223,372,036,854,775,808 to +9,223,372,036,854,775,807
   * - Number
     - Can be any type of number
   * - String
     - A sequence of characters
   * - Timestamp
     - Date and time information
     
Comparison and control Functions
--------------------------------

.. list-table::
   :widths: 20 25 55
   
   
   * - **Name**
     - **Arguments**
     - **Description**
   * - between
     - ``num``:Number, ``low``:Number,``high``:Number
     - returns true if ``low`` <= ``num`` <= ``high``
   * - equalTo
     - ``a``:Object, ``b``:Object
     - Can be used to compare for equality two numbers, two strings, two dates, and so on
   * - greaterEqualThan
     - ``x``:Object, ``y``:Object
     - Returns true if ``x`` >= ``y``. Parameters can be either numbers or strings (in the second case lexicographic ordering is used)
   * - greaterThan
     - ``x``:Object, ``y``:Object
     - Returns true if ``x`` > ``y``. Parameters can be either numbers or strings (in the second case lexicographic ordering is used)
   * - if_the_else
     - ``condition``:Boolean, ``x``:Object, ``y``: Object
     - Returns ``x`` if the condition is true, ``y`` otherwise
   * - in10, in9, in8, in7, in6, in5, in4, in3, in2
     - ``candidate``:Object, ``v1``:Object, ..., ``v9``:Object
     - Returns true if ``candidate`` is equal to one of the ``v1``, ..., ``v9`` values. Use the appropriate function name depending on how many arguments you need to pass.
   * - isLike
     - ``string``:String, ``pattern``:String
     - Returns true if the string matches the specified pattern. For the full syntax of the pattern specification see the `Java Pattern class javadocs <http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html>`_
     
Feature functions
------------------

.. list-table::
   :widths: 20 25 55
   
   
   * - **Name**
     - **Arguments**
     - **Description**
   * - id
     - ``feature``:Feature
     - returns the identifier of the feature
     
     
Geometric Functions
--------------------

Most of the geometric function listed below refer to geometry relationship, to get more information about the meaning of each spatial relationship consult the `OGC Simple Feature Specification for SQL <http://www.opengeospatial.org/standards/sfs>`_.

.. list-table::
   :widths: 20 25 55
   
   
   * - **Name**
     - **Arguments**
     - **Description**
   * - Area
     - ``geometry``:Geometry
     - The area of the specified geometry. Works in a cartesian plane, the result will be in the same unit of measure as the geometry coordinates (which also means the results won't make any sense for geographic data)
   * - boundary
     - ``geometry``:Geometry
     - Returns the boundary of a geometry
   * - boundaryDimension
     - ``geometry``:Geometry
     - Returns the number of dimensions of the geometry boundary
   * - buffer
     - ``geometry``:Geometry, ``distance``:Double
     - Returns the buffere area around the geometry using the specified distance
   * - bufferWithSegments
     - ``geometry``:Geometry, ``distance``:Double, ``segments``:Integer
     - Returns the buffere area around the geometry using the specified distance and using the specified number of segments to represent a quadrant of a circle.
   * - bufferWithSegments
     - ``geometry``:Geometry, ``distance``:Double, ``segments``:Integer
     - Returns the buffered area around the geometry using the specified distance and using the specified number of segments to represent a quadrant of a circle.
   * - centroid
     - ``geometry``:Geometry
     - Returns the centroid of the geometry. Can be often used as a label point for polygons, though there is no guarantee it will actually lie inside the geometry 
   * - contains
     - ``a``:Geometry, ``b``:Geometry
     - Returns true if the geometry ``a`` contains ``b``
   * - convexHull
     - ``geometry``:Geometry
     - Returns the convex hull of the specified geometry
   * - crosses
     - ``a``:Geometry, ``b``:Geometry
     - Returns true if ``a`` crosses ``b``
   * - difference
     - ``a``:Geometry, ``b``:Geometry
     - Returns all the points that sit in ``a`` but not in ``b``
   * - dimension
     - ``a``:Geometry
     - Returns the dimension of the specified geometry
   * - disjoint
     - ``a``:Geometry, ``b``:Geometry
     - Returns true if the two geometries are disjoint, false otherwise   
   * - distance
     - ``a``:Geometry, ``b``:Geometry
     - Returns the euclidean distance between the two geometries
   * - endPoint
     - ``line``:LineString
     - Returns the end point of the line
   * - envelope
     - ``geometry``:geometry
     - Returns the polygon representing the envelope of the geometry, that is, the minimum rectangle with sides parellels to the axis containing it
   * - equalsExact
     - ``a``:Geometry, ``b``:Geometry
     - Returns true if the two geometries are exactly equal, same coordinates in the same order
   * - equalsExactTolerance
     - ``a``:Geometry, ``b``:Geometry, ``tol``:Double
     - Returns true if the two geometries are exactly equal, same coordinates in the same order, allowing for a ``tol`` distance in the corresponding points
   * - exteriorRing
     - ``poly``:Polygon
     - Returns the exterior ring of the specified polygon
   * - geometryType
     - ``geometry``:Geometry
     - Returns the type of the geometry as a string. May be ``Point``, ``MultiPoint``, ``LineString``, ``LinearRing``, ``MultiLineString``, ``Polygon``, ``MultiPoligon``, ``GeometryCollection``
   * - geomFromWKT
     - ``wkt``:String
     - Returns the ``Geometry`` represented in the Well Known Text format contained in the ``wkt`` parameter
   * - geomLength
     - ``geometry``:Geometry
     - Returns the length/perimeter of this geometry (computed in cartesian space)
   * - getGeometryN
     - ``collection``:GeometryCollection, ``n``:Integer
     - Returns the n-th geometry inside the collection
   * - getX
     - ``p``:Point
     - Returns the ``x`` ordinate of ``p``
   * - getY
     - ``p``:Point
     - Returns the ``y`` ordinate of ``p``
   * - getZ
     - ``p``:Point
     - Returns the ``z`` ordinate of ``p``
   * - interiorPoint
     - ``geometry``:Geometry
     - Returns a point that is either interior to the geometry, when possible, or sitting on its boundary, otherwise
   * - interiorRingN
     - ``polyg``:Polygon, ``n``:Integer
     - Returns the n-th interior ring of the polygon
   * - intersection
	 - ``a``:Geometry, ``b``:Geometry
	 - Returns the intersection between ``a`` and ``b``. The intersection result can be anything including a geometry collection of etherogeneous, if the result is empty, it will be represented by an empty collection.
   * - intersects
	 - ``a``:Geometry, ``b``:Geometry
	 - Returns true if ``a`` intersects ``b``
   * - isClosed
     - ``line``: LineString
     - Returns true if ``line`` forms a closed ring, that is, if the first and last coordinates are equal
   * - isEmpty
     - ``geometry``: Geometry
     - Returns true if the geometry does not contain any point (typical case, an empty geometry collection)
   * - isValid
     - ``geometry``: Geometry
     - Returns true if the geometry is topologically valid (rings are closed, holes are inside the hull, and so on)
   * - isSimple
     - ``geometry``: Geometry
     - Returns true if the geometry is simple, that is, it's not a collection of simpler geometries (not a GeometryCollection
       or a Multi*)
   * - isWithinDistance
     - ``a``: Geometry, ``b``:Geometry, ``distance``: Double
     - Returns true if the distance between ``a`` and ``b`` is less than ``distance`` (measured as an euclidean distance)
   * - numGeometries
     - ``collection``: GeometryCollection
     - Returns the numer of geometries contained in the geometry collection
   * - numInteriorRing
     - ``poly``: Polygon
     - Returns the number of interior rings (holes) inside the specified polygon
   * - numPoint
     - ``geometry``: Geometry
     - Returns the number of points (vertices) contained in ``geometry``
   * - overlaps
     - ``a``: Geometry, ``b``:Geometry
     - Returns true ``a`` overlaps with ``b``
   * - pointN
     - ``geometry``: Geometry, ``n``:Integer
     - Returns the n-th point inside the specified geometry
   * - relate
     - ``a``: Geometry, ``b``:Geometry
     - Returns the DE-9IM intesection matrix for ``a`` and ``b``
   * - relatePattern
     - ``a``: Geometry, ``b``:Geometry, ``pattern``:String
     - Returns true if the DE-9IM intesection matrix for ``a`` and ``b`` matches the specified pattern
   * - startPoint
     - ``line``: LineString
     - Returns the starting point of the specified geometry
   * - symDifference
     - ``a``: Geometry, ``b``:Geometry
     - Returns the symmetrical difference between ``a`` and ``b`` (all points that are inside ``a`` or ``b``, but not both)
   * - touches
     - ``a``: Geometry, ``b``: Geometry
     - Returns true if ``a`` touches ``b`` according to the SQL simple feature specification rules
   * - toWKT
     - ``geometry``: Geometry
     - Returns the WKT representation of ``geometry``
   * - union
     - ``a``: Geometry, ``b``:Geometry
     - Returns the union of ``a`` and ``b`` (the result may be a geometry collection)
   * - within
     - ``a``: Geometry, ``b``:Geometry
     - Returns true is fully contained inside ``b``
   
   
	 
Math Functions
--------------

.. list-table::
   :widths: 20 25 55
   
   
   * - **Name**
     - **Arguments**
     - **Description**
   * - abs
     - ``value``:Integer
     - The absolute value of the specified Integer ``value``
   * - abs_2
     - ``value``:Long
     - The absolute value of the specified Long ``value``
   * - abs_3
     - ``value``:Float
     - The absolute value of the specified Float ``value``
   * - abs_4
     - ``value``:Double
     - The absolute value of the specified Double ``value``
   * - acos
     - ``angle``:Double
     - Returns the arc cosine of an ``angle`` expressed in radians, in the range of 0.0 through ``PI``
   * - asin
     - ``angle``:Double
     - Returns the arc sine of an ``angle`` expressed in radians, in the range of ``-PI / 2`` through ``PI / 2``
   * - atan
     - ``angle``:Double
     - Returns the arc tangent of an angle, in the range of ``-PI/2`` through ``PI/2``
   * - atan2
     - ``x``:Double, ``y``:Double
     - Converts rectangular coordinates ``(x, y)`` to polar ``(r, theta)``.
   * - ceil
     - ``x``: Double
     - Returns the smallest (closest to negative infinity) double value that is greater than or equal to the argument and is equal to a mathematical integer.
   * - cos
     - ``angle``: Double
     - Returns the cosine of an ``angle`` expressed in radians
   * - double2bool
     - ``x``: Double
     - Returns true if the number is zero, false otherwise
   * - exp
     - ``x``: Double
     - Returns Euler's number raised to the power of ``x``
   * - floor
     - ``x``: Double
     - Returns the largest (closest to positive infinity) value that is less than or equal to the argument and is equal to a mathematical integer
   * - IEEERemainder
     - ``x``: Double, ``y``:Double
     - Computes the remainder operation on two arguments as prescribed by the IEEE 754 standard
   * - int2bbool
     - ``x``: Integer
     - Returns true if the number is zero, false otherwise
   * - int2ddouble
     - ``x``: Integer
     - Converts the number to Double
   
   
String functions
-----------------   

.. list-table::
   :widths: 20 25 55
   
   * - **Name**
     - **Arguments**
     - **Description**
   * - strConcat
     - ``a``:String, ``b``:String
     - Concatenates the two strings into one
   * - strEndsWith
     - ``a``:String, ``b``:String
     - Concatenates the two strings into one
   
     
Parsing and formatting functions
--------------------------------

.. list-table::
   :widths: 20 25 55
   
   * - **Name**
     - **Arguments**
     - **Description**
   * - dateFormat
     - ``date``:Timestamp, ``format``:String
     - Formats the specified date according to the provided format. The format syntax can be found in the `Java SimpleDateFormat javadocs <http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html>`_
   * - dateParse
     - ``dateString``:String, ``format``:String
     - Parses a date from a ``dateString`` formatted according to the ``format`` specification. The format syntax can be found in the `Java SimpleDateFormat javadocs <http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html>`_
   
   

   




