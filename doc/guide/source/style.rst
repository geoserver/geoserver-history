.. _style_guidelines:

Style Guildelines
=================

This document provides a set of guidelines to use for all GeoServer project documentation. The purpose of which is to ensure that all project documentation remains clear, consistent, and easy to read.

Avoid too much information
--------------------------

Documentation should be concise and not just a brain dump. If the point of the document is to share your thoughts and insights, it belongs in a blog post. There are cases where a document should be verbose, such as the history of the project for instance, but these cases are the exception. For the most part keep it clear, concise, and to the point.

No marketing
------------

Avoid marketing babble. If the point of the document is to showcase a new feature it does not belong in the user or developers guide. Write an article or a blog post about it. If it is necessary to point out a technical benefit of a feature then do so from a technical standpoint.

* *Bad*: Super overlays are a great and wiz-bang way to publish big datasets in Google Earth.
* *Good*: Super overlays allow you to publish data in Google Earth in a way that prevents overloading Google Earth with too much data.

No slang
--------

Do not use slang or other "colorful" language. The point of a technical document is to be informative, not to keep the reader amused. Also keep in mind that the person reading your document may have learned English as a second language so any use of slang just makes the document harder to read and more confusing for them.

* *Bad*: Next fire up your web browser and navigate to ...
* *Good*: Next start your web browser and navigate to ...

Use imperative mood
-------------------

When providing instructions in a step by step guide use the imperative mood, ie, use direct commands or requests. Avoid the use of "we" and "let's".

* *Bad*: Let's add a shapefile by ...
* *Good*: Add a shapefile by ...

* *Bad*: First we will add a shapefile, and then we will create a template for it.
* *Good*: First add a shapefile, and then create a template for it.

Linking
-------

Links to other pages should not be titled as "here". They should be more informative.

* *Bad*: More information about adding shapefiles can be found here.
* *Good*:  For more information about adding shapefiles, see the shapefile tutorial.

Naming conventions
------------------

Many of the guidelines in this section are taken from the Wikipedia naming convensions.

Capitalization
^^^^^^^^^^^^^^

**Page names**

Each word in the page name should be capitalized except for articles (such as "the", "a", "an") and conjunctions (such as "and", "but", "or"). A page name should never start with an article.

* *Bad*: Adding a shapefile or postgis table
* *Good*: Adding a Shapefile or PostGIS Table

* *Bad*: The Shapefile Tutorial
* *Good*: Shapefile Tutorial

**Section names**

Do not capitalize second and subsequent words unless the title is almost always capitalized in English (for example, proper names). Thus, capitalize John Wayne and Art Nouveau, but not Video Games.

* *Bad*: Creating a New Datastore
* *Good*: Creating a new datastore

Verb usage
^^^^^^^^^^

It is recommended that the gerund (the -ing form in English) be used unless there is a more common noun form. For example, an article on swimming is better than one on swim.

* *Bad*: Create a new datastore
* *Good*: Creating a new datastore

Plurals
^^^^^^^

In general only create page titles that are in the singular, unless that noun is always in a plural form in English, such as scissors or trousers, or concerns a small class, such as Arabic numerals, polar coordinates, Bantu languages, or The Beatles, that requires a plural.

* *Bad*: Templates tutorial
* *Good*: Template tutorial

Formatting
----------

Code and command line
^^^^^^^^^^^^^^^^^^^^^

Any code or command line snippets should be formatted as code::

   This is a code block.

When lines are longer than 77 characters, extend multiple lines in a format appropriate for the language in use.  If possible, snippets should be functional if a user copies and pastes them directly into the appropriate target.  

For example, Java and XML make no distinction between a single space and multiple spaces, so the following snippets are fine::

   org.geoserver.package.Object someVeryLongIdentifier =
      org.geoserver.package.Object.factoryMethod();

::

   <namespace:tagname attributename="attributevalue" attribute2="attributevalue"
      nextattribute="this is on another line"/>

For shell scripts, new lines can be escaped with a backslash character (/). It is also recommended to use a simple {{$ }} prompt to save space. For example::

   $ /org/jdk1.5.0*/bin/java \
      -cp /home/user/.m2/repository/org/geoserver/*/*.jar \
      org.geoserver.GeoServer -DGEOSERVER_DATA_DIR=/var/lib/geoserver_data/release

User interface components
^^^^^^^^^^^^^^^^^^^^^^^^^

When describing a button or link or some other user interface component use monospaced text. When describing input surround it with "quotes"

Example: Enter "foo" in the ``Alias`` text field. Enter "4326" in the ``SRS`` text field. Then click the ``Submit`` button. Then the ``Apply`` button.

