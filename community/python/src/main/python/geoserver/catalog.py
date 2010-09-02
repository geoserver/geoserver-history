from geoserver.store import Store
from geoscript.layer import Layer
from geoscript.workspace import Workspace

class Catalog(object):
  """
  Constructs a new Catalog instance. 

  The *workspace* parameter is the name of a GeoServer workspace. If 
  unspecified it will default to the default configured workspace.
  """

  def __init__(self, workspace=None):
     try:
       from org.geoserver.platform import GeoServerExtensions
     except ImportError:
       pass
     else:
       self._catalog = GeoServerExtensions.bean('catalog') 
       if not workspace:
          workspace = self._catalog.getDefaultWorkspace().getName()

       self.workspace = workspace

  def stores(self):
     """  
     Lists all the stores in the catalog. 

     Returns a ``list`` of ``str``.
     """  

     stores = self._catalog.getDataStoresByWorkspace(self.workspace);
     return [s.name for s in stores]

  def get(self, name):
     """
     Looks up a store by name.

     *name* is the name of the store.
     
     Returns a :class:`geoserver.store.Store` object, or ``None`` if no such 
     store exists.
     """

     store = self._catalog.getDataStoreByName(self.workspace, name)
     if store:
       return Store(store, self)

  def add(self, data, name, **attrs):
     """
     Adds a new store to the catalog.

     *data* is the :class:`geoscript.workspace.Workspace` representing the underlying data for the store.

     *name* is the name for the new store.

     *attrs* is a variable argument that enumerates additional attributes such as name, title, etc... for the store.

     Returns a :class:`geoserver.store.Store` object. 
     """

     store = self._catalog.getFactory().createDataStore()
     store.setName(name)

     ws = self._catalog.getWorkspaceByName(self.workspace)
     store.setWorkspace(ws)
     store.getConnectionParameters().putAll(data.params)
     for name, valu in attrs.iteritems():
       if hasattr(store, name):
         setattr(store, name, valu)

     self._catalog.add(store)
     return store

  def __getitem__(self, key):
    st = self.get(key)
    if not st:
      raise KeyError('No such store %s' % key)

    return st

  def __setitem__(self, key, val):
    self.add(key, val)

  def __iter__(self):
    return self.stores().__iter__()    

  def iterkeys(self):
    return self.stores().__iter__()

  def iteritems(self):
    for st in self.stores():
      yield (st, self.get(st))

