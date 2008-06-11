WICKET BASED MODULE INFRASTRUCTURE
--------------------------------------------------------

Pluggability:    dynamic pages are completely contained in the classpath,
                 as well as the resources they do reference (css, images).
                 In order to have the ability to serve fully static pages
                 a simple servlet should be developed that serves files out
                 of the classpath (configuring which packages can be inspected,
                 and how they do map onto url).
Template system: based on wicket inheritance, see:
                 http://wicketframework.org/ExampleMarkupInheritance.html
                 An alternative may be to use components instead of pages, the
                 advantage is a composition structure which is more similar
                 to desktop ui, disadvantage is that we end up with really just
                 one page that morphs during requests swapping panels (not ajax
                 anyway, althought this can be implemented too).
Plugin system:   Spring based, simple bean instances are created in the context
                 describing sub-pages and the links that point to them. 
                 BookmarkablePageInfo is the root of these info objects, subclasses
                 are created just in order to facilitate the search for a particular
                 kind of page in the Spring context (that is, main pages, data configuration
                 pages, and the like).