from geoscript.layer import Layer as GeoScriptLayer

class Layer(GeoScriptLayer):
  """
  A GeoServer layer.
  """

  def __init__(self, info, store):
    fs = info.getFeatureSource(None, None)
    GeoScriptLayer.__init__(self, workspace=store, fs=fs)

    self.info = info
    self.store = store

  def save(self):
    """
    Saves any changes made to the store back to the catalog.
    """
    self.store.cat._catalog.save(self.info)

  def getmeta(self):
    return self.info

  meta = property(getmeta, None, None, 'The layer metadata. An instance of :class:`org.geoserver.catalog.LayerInfo`')
