.. _sphinx_stuff:

Sphinx Stuff
============

Rules, tips, and tricks for using Sphinx

Page Labels
-----------

**Ensure every page has a label that matches the name of the file.** For example if the page is named ``foo_bar.rst`` then the page should have the label::

   ..  _foo_bar:
  
Other pages can then link to that page by using the following code::

   :ref:`foo_bar`
  
 
Use List-Tables
---------------

Bulleted lists can be cumbersome.  Instead, **use list-tables**.  For example, to talk about a list of options, use the following syntax:

.. list-table::
   :widths: 20 80
   
   * - **Thing**
     - **Description**
   * - Thing 1
     - Description 1
   * - Thing 2
     - Description 2
     
This is done with the following syntax::

   .. list-table::
      :widths: 20 80
      
      * - **Thing**
        - **Description**
      * - Thing 1
        - Description 1
      * - Thing 2
        - Description 2
        
