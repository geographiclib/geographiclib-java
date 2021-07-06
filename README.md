# Java implementation of the geodesic routines in GeographicLib

The algorithms are documented in

* C. F. F. Karney,
  [Algorithms for geodesics](https://doi.org/10.1007/s00190-012-0578-z),
  J. Geodesy 87, 43-55 (2013);
  [Addenda](https://geographiclib.sourceforge.io/geod-addenda.html).

The Java package is available
[here](https://search.maven.org/artifact/net.sf.geographiclib/GeographicLib-Java).

Here is the documentation on the
[application programming interface](https://geographiclib.sourceforge.io/html/java/)

You can build the example programs using, for example,
```sh
cd inverse/src/main/java
javac -cp .:../../../../src/main/java Inverse.java
echo -30 0 29.5 179.5 | java -cp .:../../../../src/main/java Inverse
```

On Windows, change this to
```sh
cd inverse\src\main\java
javac -cp .;../../../../src/main/java Inverse.java
echo -30 0 29.5 179.5 | java -cp .;../../../../src/main/java Inverse
```

Building with maven:
```sh
mvn install
cd inverse
mvn compile
echo -30 0 29.5 179.5 | mvn -q exec:java
```

Deploy the package with
```sh
mvn -q package -P release
mvn clean deploy -P release
```
