.. _sld_cook_book_lines:

Lines
=====

Lines, by having length but no width, can seem to be a simple shape.  However, there are many tricks for making nicer lines, which is especially important when styling roads.  

The line layer used in the examples below contain road information about a section of Supox, a major city in the fictional country of Frungy.  Download this layer.

.. warning:: Add link to line data

Simple line
-----------

This example shows a basic line style, colored black with a thickness of three (3) pixels.

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Better line style
-----------------

An often used trick for improving the style of lines is to draw the line twice, once with a small thickness, and once with a slightly larger thickness.  This has the effect of creating a "fill and stroke" look for lines.  This example fakes a blue fill and a gray stroke by drawing a blue line at 3 pixels, and a gray line at 5 pixels.

Since these lines are drawn twice, the order of the drawing is very important.  In this style, all of the gray lines are drawn first, followed by all of the blue lines.  This not only ensures that the blue lines won't be obscured, but also allows for better styling at intersections (so the inner lines "connect").

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Dashed line
-----------

This example takes the simple line and creates a dashed line.  The style as drawn consists of five (5) pixels of drawn line, followed by two (2) pixels of blank space.

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Railroad (hatching)
-------------------

This example leverages the use of hatching to create a railroad style.  Both the line and the hatches are black, with a two (2) pixel thickness.

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Default label
-------------

This example shows a text label on the simple line.  In the absence of any other customization, this is how a label will be displayed.

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Label following line
--------------------

.. warning:: Add description

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Repeated label
--------------

If the lines are very long, it may be advantageous to repeat the label in multiple places.  However, two many repeats can clutter up your map.  This example repeats the label every one hundred (100) pixels.

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Displaced label
---------------

This example shows the labels displaced from their lines by a distance of ten (10) pixels.

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Attribute-based style
---------------------

There are various road classes in Supox, ranging from back roads to high-speed freeways.  This example styles the lines differently based on road class.

.. warning:: Add more description

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD

Zoom-based style
----------------

When making a nice looking map, it is beneficial to make shapes look larger as the map is zoomed in.  This example the simple line style and adjusts it for multiple zoom levels.  (Each zoom level effectively adds 100% more code, so this example will be fairly long, although much of it is duplicated code.)

.. warning:: Add more description

.. warning:: Add code

.. warning:: Add screenshot

.. warning:: Add link to SLD
