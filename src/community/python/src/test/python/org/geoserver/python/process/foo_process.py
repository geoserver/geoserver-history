from geoserver.process import process;
from geoscript.geom import Point;

@process('Foo', 'The foo process', '1.2.3', [('bar', 'The bar parameter', str), 
    ('baz', 'The baz parameter'), ('bam', 'The bam parameter', Point)], 
    [('result', 'The result', float)])
def foo(bar, baz, bam):
  return 1.2;