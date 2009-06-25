WMS Attribution
===============

The WMS Attribution section of the layer configuration page allows you to publish information about data providers.  The information is broken up into several fields:

Attribution Text
    Human-readable text describing the data provider.  This might be used as the text for a hyperlink to the data provider's web site.

Attribution Link
    A URL to the data provider's website.

Logo URL
    A URL to an image that serves as a logo for the data provider.

Logo Content Type, Width, and Height
    These fields provide information about the logo image that clients may use to assist with layout.  GeoServer will auto-detect these values if you click the :guilabel:``Auto-detect image size and type`` link at the bottom of the section.

The text, link, and logo are each advertised in the WMS Capabilities document if they are provided; some WMS clients will display this information to allow users to know which providers provide a particular dataset.  If you omit some of the fields, those that are provided will be published and those that are not will be omitted from the Capabilities document.
