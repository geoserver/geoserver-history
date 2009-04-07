.. _sphinx_stuff:

Sphinx Tips
===========

This page contains syntax rules, tips, and tricks for using Sphinx.

Page Labels
-----------

**Ensure every page has a label that matches the name of the file.** For example if the page is named ``foo_bar.rst`` then the page should have the label::

   ..  _foo_bar:
  
Other pages can then link to that page by using the following code::

   :ref:`foo_bar`
  

Headings
--------

**Use headings to break up long pages** and to help Sphinx auto-generate tables of contents.

The top of the page (i.e. the title) should have a double underline::

   Camel Spotting
   ==============
   
Subsequent section headers should have a single underline::

   Camel Spotting
   ==============

   Intro to the Camel
   ------------------
   
   Finding a Camel
   ---------------
   
   Recording its Serial Number
   ---------------------------
   
Should these sections require subsections, use the back quote::

   Intro to the Camel
   ------------------
   
   Camel History
   `````````````
   
   Camels Today
   ````````````
   
  
Use List-Tables
---------------

Bulleted lists can sometimes be cumbersome and hard to follow.  When dealing with a long list of items, **use list-tables**.  For example, to talk about a list of options, use the following syntax:

.. list-table::
   :widths: 20 80
   
   * - **Shapes**
     - **Description**
   * - Square
     - Four sides of equal length, 90 degree angles
   * - Rectangle
     - Four sides, 90 degree angles
    
This is done with the following syntax::

   .. list-table::
      :widths: 20 80
      
      * - **Shapes**
        - **Description**
      * - Square
        - Four sides of equal length, 90 degree angles
      * - Rectangle
        - Four sides, 90 degree angles
        
        
.. _style_linking:

Linking
-------

Links to other pages should never be titled as "here". The link should be more informative.  Luckily, Sphinx makes this easy by automatically inserting the title of the linked document.

Bad
   More information about linking can be found :ref:`here <style_linking>`.
Good
   For more information, please see the section on :ref:`style_linking`.

