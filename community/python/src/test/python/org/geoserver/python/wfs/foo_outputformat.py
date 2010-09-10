from geoserver.wfs import outputformat

@outputformat('Foo', 'text/plain')
def write(features, output):
  for f in features:
     output.write(f.id)