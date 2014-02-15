This is a fork of http://code.google.com/p/annovention/ that fixes bugs and
removes Apache log dependency to keep the library lightweight.

Differences compared to the original Annovention library:

* Discoverer can be specified to scan only classes, fields, or methods;
  and to scan only runtime visible or invisible annotations
* ClasspathDiscoverer has been improved to properly scan all class loaders
  (based on http://code.google.com/p/reflections)
* Apache Common Log has been removed; the only dependency is Javassist
* 2014-02-14 - dbennett455 - New listeners added that accept the Javassist
  objects for access to annotation parameters and other info.

See `Javadoc <http://ngocdaothanh.github.io/annovention/>`_.

If you use Scala, see also `Sclasner <https://github.com/ngocdaothanh/sclasner>`_.

Using with Maven
----------------

::

  <dependency>
    <groupId>tv.cntt</groupId>
    <artifactId>annovention</artifactId>
    <version>1.5</version>
  </dependency>

Using with SBT
--------------

::

  libraryDependencies += "tv.cntt" % "annovention" % "1.5"
