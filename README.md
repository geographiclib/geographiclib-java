# Java implementation of the geodesic routines in GeographicLib

This is a library to solve geodesic problems on an ellipsoid model of
the earth.

Licensed under the MIT/X11 License; see
[LICENSE.txt](https://geographiclib.sourceforge.io/LICENSE.txt).

The algorithms are documented in

* C. F. F. Karney,
  [Algorithms for geodesics](https://doi.org/10.1007/s00190-012-0578-z),
  J. Geodesy **87**(1), 43–55 (2013);
  [Addenda](https://geographiclib.sourceforge.io/miscgeod-addenda.html).

The Java package is available
[here](https://search.maven.org/artifact/net.sf.geographiclib/GeographicLib-Java).

Here is the documentation on the
[application programming interface](https://geographiclib.sourceforge.io/Java/doc/)

You can build the example programs `direct`, `inverse`, or
`planimeter`, with, for example:
```sh
cd inverse
mvn compile
echo -30 0 29.5 179.5 | mvn -q exec:java
```
The ouput should be
```
154.37818274278 25.48587026077 19937782.280350
```
