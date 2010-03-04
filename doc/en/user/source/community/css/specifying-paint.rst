
Specifying Paint
================

.. warning:: 
    The features discussed in this page are **not implemented** at this time.
    Code snippets are purely for design discussion.

This page is for enumerating the ideas that have been tabled for specifying
complex paint values (ie, having support for inline expressions of "a circle
with a cross on it" and other combinations of "Well Known Marks").

.. highlight:: xml

For the example snippets in this section, I've tried to find the CSS equivalent
of the following SLD snippet, which creates a simple hospital icon by
overlaying a red cross above a white circle::

    <Rule>
       <PointSymbolizer>
           <Graphic>
               <Mark>
                   <WellKnownName>circle</WellKnownName>
                   <Fill>
                       <CssParameter name="fill">
                           <Literal>#FFFFFF</Literal>
                       </CssParameter>
                   </Fill>
                   <Size>16</Size>
               </Mark>
           </Graphic>
       </PointSymbolizer>
       <PointSymbolizer>
           <Graphic>
               <Mark>
                   <WellKnownName>cross</WellKnownName>
                   <Fill>
                       <CssParameter name="fill">
                           <Literal>#FF0000</Literal>
                       </CssParameter>
                   </Fill>
                   <Size>12</Size>
               </Mark>
           </Graphic>
       </PointSymbolizer>
    </Rule>

.. highlight:: css

Special URLs
------------

One way to do this without introducing too much extra syntax would be to
provide a special URL scheme for the well-known marks, relying on
:doc:`multivalues` to do the overlaying.  This would look like::

    hospitals {
      mark: url("mark://circle&size=16&stroke=white&fill=white"),
            url("mark://cross&size=12&stroke=red&fill=red");
    }

An obvious problem with this approach is the conflict with existing
"well-known" mark extensions in GeoTools such as the charting extension, which
use URL syntax to specify their own, internal parameters.

Inline Paint Servers
--------------------

Another option would be to adopt and expand on SVG's idea of
`paint servers <http://www.w3.org/TR/SVG/pservers.html>`_, which are defined
using some inline syntax (in the SVG document) and then referenced by named
anchor.  For use in the GeoServer ``css`` module, this would require some way
of inlining a paintserver in the stylesheet.  It might look like::

    @paint('hospital') {
      shape: symbol(circle), symbol(cross);
      shape-size: 16px, 12px;
      fill: white, red;
      stroke: white, red;
    }

    hospital {
      mark: url("#hospital");
    }

Pseudo-elements for Paintable Geometries
----------------------------------------

After talking to some HTML+CSS designers about this, it was suggested to
simplify present the marks and strokes as stylable elements in the CSS
document.  This should look something like::

    hospital {
      mark: symbol(circle), symbol(cross);
      mark-size: 16px, 12px;
    }

    hospital :mark#1 {
      fill: white;
      stroke: white;
    }

    hospital :mark#2 {
      fill: red;
      stroke: red;
    }
