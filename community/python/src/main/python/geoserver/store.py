from geoserver.layer import Layer
from geoscript.workspace import Workspace

class Store(Workspace):
   """
   A GeoServer store. 
   """

   def __init__(self, info, cat):
     ds = info.getDataStore(None)
     Workspace.__init__(self, ds=ds)
     self.info = info
     self.cat = cat
     
   def layers(self):
     """
     Lists all the layers which are part of the store.

     Returns a ``list`` of ``str``.
     """
     featureTypes = self.cat._catalog.getFeatureTypesByStore(self.info)
     return [ft.getName() for ft in featureTypes]

   def get(self, name):
     """
     Looks up a layer by name.

     *name* is the name of the layer to lookup.

     Returns a :class:`geoserver.layer.Layer` object, or ``None`` if no such 
     layer exists.
     """
     l = Workspace.get(self, name)
     if l:
       layerinfo = self.cat._catalog.getFeatureTypeByStore(self.info, name)
       return Layer(layerinfo, self)

   def save(self):
     """
     Saves any changes made to the store back to the catalog.
     """
     self.cat.save(self.info)

   def getmeta(self):
     return self.info
     
   meta = property(getmeta, None, None, 'The store metadata. An instance of :class:`org.geoserver.catalog.StoreInfo`')

   def __getitem__(self, key):
     l = self.get(key)
     if not l:
       raise KeyError('No such layer %s' % (key))
     return l

   def __setitem__(self, key, val):
     pass 

   def __iter__(self):
     return self.layers().__iter__()

   def iterkeys(self):
     return self.layers().__iter__()

   def iteritems(self):
     for l in self.layers():
       return (l, self.get(l))
