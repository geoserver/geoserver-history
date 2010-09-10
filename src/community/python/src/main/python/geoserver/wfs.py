from geoscript.feature import Feature

def outputformat(name, mime):
  """
  Dectorator for a wfs getfeature output format.
  """
  def wrap(func):
     def wrapper(features, output): 
        return func(_FeatureIterator(features), output)
     
     wrapper.__wfs_outputformat__ = None
     wrapper.name = name
     wrapper.mime = mime
     return wrapper
     
  return wrap
  

class _FeatureIterator(object):

  def __init__(self, features):
    self.it = features.features()
    
  def __iter__(self):
     return self
     
  def next(self):
    if self.it.hasNext():
       return Feature(f=self.it.next())
       
    self.it.close()
    raise StopIteration
      