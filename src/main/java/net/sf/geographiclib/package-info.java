/**
 * <h2>Geodesic routines from GeographicLib implemented in Java</h2>
 * @author Charles F. F. Karney (charles@karney.com)
 * @version 2.0
 *
 * <p>
 * The documentation for other versions is available at
 * <code>https://geographiclib.sourceforge.io/Java/</code>.
 * <p>
 * Licensed under the
 * <a href="https://www.opensource.org/licenses/MIT">MIT/X11 License</a>; see
 * <a href="https://geographiclib.sourceforge.io/LICENSE.txt">
 * LICENSE.txt</a>.
 *
 * <h3>Abstract</h3>
 * <p>
 * GeographicLib-Java is a Java implementation of the geodesic algorithms from
 * <a href="https://geographiclib.sourceforge.io">GeographicLib</a>.  This is a
 * self-contained library which makes it easy to do geodesic computations for
 * an ellipsoid of revolution in a Java program.  It requires Java version 1.7
 * or later.
 *
 * <h3>Downloading</h3>
 * <p>
 * Download either the source or the pre-built package as follows:
 *
 * <h4>The pre-built package</h4>
 * GeographicLib-Java is available as a
 * <a href="https://search.maven.org/artifact/net.sf.geographiclib/GeographicLib-Java">
 * pre-built package on Maven Central</a> (thanks to Chris Bennight for help on
 * this deployment).  So, if you use
 * <a href="https://maven.apache.org/">maven</a> to build your code, you just
 * need to include the dependency <pre>{@code
 *   <dependency>
 *     <groupId>net.sf.geographiclib</groupId>
 *     <artifactId>GeographicLib-Java</artifactId>
 *     <version>2.0</version>
 *   </dependency> }</pre>
 * in your {@code pom.xml}.
 *
 * <h4>Obtaining the source</h4>
 * The source is hosted on
 * <a href="https://github.com/geographiclib/geographiclib-java">github</a>.
 * Releases are tagged as v1.52, v2.0, etc.
 *
 * <h3>Sample programs</h3>
 * <p>
 * Included with the source are 3 small test programs
 * <ul>
 * <li>
 *    {@code direct/src/main/java/Direct.java} is a simple command line utility
 *    for solving the direct geodesic problem;
 * <li>
 *    {@code inverse/src/main/java/Inverse.java} is a simple command line
 *    utility for solving the inverse geodesic problem;
 * <li>
 *    {@code planimeter/src/main/java/Planimeter.java} is a simple command line
 *    utility for computing the area of a geodesic polygon given its vertices.
 * </ul>
 * <p>
 * Here, for example, is {@code Inverse.java} <pre>{@code
 * // Solve the inverse geodesic problem.
 * //
 * // This program reads in lines with lat1, lon1, lat2, lon2 and prints
 * // out lines with azi1, azi2, s12 (for the WGS84 ellipsoid).
 *
 * import java.util.*;
 * import net.sf.geographiclib.*;
 * public class Inverse {
 *   public static void main(String[] args) {
 *     try {
 *       Scanner in = new Scanner(System.in);
 *       double lat1, lon1, lat2, lon2;
 *       while (true) {
 *         lat1 = in.nextDouble(); lon1 = in.nextDouble();
 *         lat2 = in.nextDouble(); lon2 = in.nextDouble();
 *         GeodesicData g = Geodesic.WGS84.Inverse(lat1, lon1, lat2, lon2);
 *         System.out.format("%.11f %.11f %.6f%n", g.azi1, g.azi2, g.s12);
 *       }
 *     }
 *     catch (Exception e) {}
 *   }
 * }}</pre>
 *
 * <h3>Compiling and running a sample program</h3>
 * <p>
 * Here's how to compile and run {@code Inverse.java} using
 * <a href="https://maven.apache.org/">maven</a> (the recommended way) and
 * without using maven.  (Thanks to Skip Breidbach for supplying the maven
 * support.)
 *
 * <h4>Using maven</h4>
 * The sample code includes a {@code pom.xml} which specifies
 * GeographicLib-Java as a dependency which maven will download from Maven
 * Central.  You can compile and run Inverse.java with <pre>
 * cd inverse
 * mvn compile
 * echo -30 0 29.5 179.5 | mvn -q exec:java </pre>
 *
 * <h4>Without using maven</h4>
 * Compile and run as follows <pre>
 * cd inverse/src/main/java
 * javac -cp .:../../../../src/main/java Inverse.java
 * echo -30 0 29.5 179.5 | java -cp .:../../../../src/main/java Inverse </pre>
 *
 * <h3>Using the library</h3>
 * <ul>
 * <li>
 *   Put <pre>
 *   import net.sf.geographiclib.*</pre>
 *   in your source code.
 * <li>
 *   Make calls to the geodesic routines from your code.
 * <li>
 *   Compile and run in one of the ways described above.
 * </ul>
 * <p>
 * The important classes are
 * <ul>
 * <li>
 *   {@link net.sf.geographiclib.Geodesic}, for direct and inverse geodesic
 *   calculations;
 * <li>
 *   {@link net.sf.geographiclib.GeodesicLine}, an efficient way of
 *   calculating multiple points on a single geodesic;
 * <li>
 *   {@link net.sf.geographiclib.GeodesicData}, the object containing the
 *   results of the geodesic calculations;
 * <li>
 *   {@link net.sf.geographiclib.GeodesicMask}, the constants that let you
 *   specify the variables to return in
 *   {@link net.sf.geographiclib.GeodesicData} and the capabilities of a
 *   {@link net.sf.geographiclib.GeodesicLine};
 * <li>
 *   {@link net.sf.geographiclib.Constants}, the parameters for the WGS84
 *   ellipsoid;
 * <li>
 *   {@link net.sf.geographiclib.PolygonArea}, a class to compute the
 *   perimeter and area of a geodesic polygon (returned as a
 *   {@link net.sf.geographiclib.PolygonResult}).
 * </ul>
 * <p>
 * The documentation is generated using javadoc when
 * {@code mvn package -P release} is run (the top of the documentation tree is
 * {@code target/apidocs/index.html}).  This is also available on the web at
 * <a href="https://geographiclib.sourceforge.io/Java/doc/index.html">
 * https://geographiclib.sourceforge.io/Java/doc/index.html</a>.
 *
 * <h3>External links</h3>
 * <ul>
 * <li>
 *   These algorithms are derived in C. F. F. Karney,
 *   <a href="https://doi.org/10.1007/s00190-012-0578-z">
 *   Algorithms for geodesics</a>,
 *   J. Geodesy <b>87</b>, 43&ndash;55 (2013)
 *   (<a href="https://geographiclib.sourceforge.io/geod-addenda.html">addenda</a>).
 * <li>
 *   A longer paper on geodesics: C. F. F. Karney,
 *   <a href="https://arxiv.org/abs/1102.1215v1">Geodesics
 *   on an ellipsoid of revolution</a>,
 *   Feb. 2011
 *   (<a href="https://geographiclib.sourceforge.io/geod-addenda.html#geod-errata">
 *   errata</a>).
 * <li>
 *   <a href="https://geographiclib.sourceforge.io">
 *   The GeographicLib web site</a>.
 * <li>
 *   <a href="https://github.com/geographiclib/geographiclib-java">
 *     git repository</a>
 * <li>
 *   The library has been implemented in a few other
 *   <a href="https://geographiclib.sourceforge.io/doc/library.html#languages">
 *   languages</a>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/geodesic-papers/biblio.html">
 *   A geodesic bibliography</a>
 * <li>
 *   The wikipedia page,
 *   <a href="https://en.wikipedia.org/wiki/Geodesics_on_an_ellipsoid">
 *   Geodesics on an ellipsoid</a>
 * </ul>
 *
 * <h3>Change log</h3>
 * <ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/2.0">Version 2.0</a>
 *   (released 2022-04-25)
 * <ul>
 * <li>
 *    This is a major reorganization with the Java library put into its own
 *    <a href="https://github.com/geographiclib/geographiclib-java">github
 *    repository</a>.  Despite this, there are only reasonably minor changes to
 *    the library itself.
 * <li>
 *    Fix bug where the solution of the inverse geodesic problem with
 *    &phi;<sub>1</sub> = 0 and &phi;<sub>2</sub> = nan was treated as
 *    equatorial.
 * <li>
 *    More careful treatment of &plusmn;0&deg; and &plusmn;180&deg;.
 * <ul>
 *   <li>
 *      These behave consistently with taking the limits
 *   <ul>
 *     <li>
 *        &plusmn;0 means &plusmn;&epsilon; as &epsilon; &rarr; 0+
 *     <li>
 *        &plusmn;180 means &plusmn;(180 &minus; &epsilon;) as &epsilon;
 *        &rarr; 0+
 *   </ul>
 *   <li>
 *      As a consequence, azimuths of +0&deg; and +180&deg; are reckoned to
 *      be east-going, as far as tracking the longitude with
 *      {@link net.sf.geographiclib.GeodesicMask#LONG_UNROLL} and the area
 *      goes, while azimuths &minus;0&deg; and &minus;180&deg; are reckoned to
 *      be west-going.
 *   <li>
 *      When computing longitude differences, if &lambda;<sub>2</sub>
 *      &minus; &lambda;<sub>1</sub> = &plusmn;180&deg; (mod 360&deg;),
 *      then the sign is picked depending on the sign of the difference.
 *   <li>
 *      The normal range for returned longitudes and azimuths is
 *      [&minus;180&deg;, 180&deg;].
 *   <li>
 *      A separate test suite signtest.SignTest has been added to check this
 *      treatment.
 * </ul>
 * <li>
 *   The deprecated functions Geodesic.MajorRadius(), etc., have been removed.
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.52">Version 1.52</a>
 *   (released 2021-06-21)
 * <ul>
 * <li>
 *   Be more aggressive in preventing negative s12 and m12 for short
 *   lines.
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.51">Version 1.51</a>
 *   (released 2020-11-22)
 * <ul>
 * <li>
 *   In order to reduce the amount of memory allocation and garbage collection,
 *   introduce versions of GeoMath.norm, GeoMath.sum, GeoMath.AngDiff, and
 *   GeoMath.sincosd, which take a {@link net.sf.geographiclib.Pair} as a
 *   parameter instead of returning a new {@link net.sf.geographiclib.Pair}.
 *   The previous versions are deprecated.
 * <li>
 *   Geodesic.MajorRadius() is now called
 *   {@link net.sf.geographiclib.Geodesic#EquatorialRadius()} and similarly for
 *   {@link net.sf.geographiclib.GeodesicLine},
 *   {@link net.sf.geographiclib.Gnomonic}, and
 *   {@link net.sf.geographiclib.PolygonArea}.
 * <li>
 *   Update to Java 1.7 or later to support testing on Mac OSX.
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.50">Version 1.50</a>
 *   (released 2019-09-24)
 * <ul>
 * <li>
 *   {@link net.sf.geographiclib.PolygonArea} can now handle arbitrarily
 *   complex polygons.  In the case of self-intersecting polygons the area is
 *   accumulated "algebraically", e.g., the areas of the 2 loops in a figure-8
 *   polygon will partially cancel.
 * <li>
 *   Fix two bugs in the computation of areas when some vertices are specified
 *   by an added edge.
 * <li>
 *   Require Java 1.6 or later and so remove epsilon, min, hypot, log1p,
 *   copysign, cbrt from GeoMath.
 * <li>
 *   GeoMath.cbrt, GeoMath.atanh, and GeoMath.asinh preserve the sign of
 *   &minus;0.
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.49">Version 1.49</a>
 *   (released 2017-10-05)
 * <ul>
 * <li>
 *   Fix code formatting and add two tests.
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.48">Version 1.48</a>
 *   (released 2017-04-09)
 * <ul>
 * <li>
 *   Change default range for longitude and azimuth to
 *   (&minus;180&deg;, 180&deg;] (instead of [&minus;180&deg;, 180&deg;)).
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.47">Version 1.47</a>
 *   (released 2017-02-15)
 * <ul>
 * <li>
 *   Improve accuracy of area calculation (fixing a flaw introduced in
 *   version 1.46).
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.46">Version 1.46</a>
 *   (released 2016-02-15)
 * <ul>
 * <li>
 *   Fix bug where the wrong longitude was being returned with direct geodesic
 *   calculation with a negative distance when starting point was at a pole
 *   (this bug was introduced in version 1.44).
 * <li>
 *   Add Geodesic.DirectLine, Geodesic.ArcDirectLine, Geodesic.GenDirectLine,
 *   Geodesic.InverseLine, GeodesicLine.SetDistance, GeodesicLine.SetArc,
 *   GeodesicLine.GenSetDistance, GeodesicLine.Distance, GeodesicLine.Arc,
 *   GeodesicLine.GenDistance.
 * <li>
 *   More accurate inverse solution when longitude difference is close to
 *   180&deg;.
 * <li>
 *   GeoMath.AngDiff now returns a Pair.
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.45">Version 1.45</a>
 *   (released 2015-09-30)
 * <ul>
 * <li>
 *   The solution of the inverse problem now correctly returns NaNs if
 *   one of the latitudes is a NaN.
 * <li>
 *   Add implementation of the ellipsoidal
 *   {@link net.sf.geographiclib.Gnomonic} (courtesy of Sebastian Mattheis).
 * <li>
 *   Math.toRadians and Math.toDegrees are used instead of GeoMath.degree
 *   (which is now removed).  This requires Java 1.2 or later (released
 *   1998-12).
 * </ul>
 * <li>
 *   <a href="https://geographiclib.sourceforge.io/Java/1.44">Version 1.44</a>
 *   (released 2015-08-14)
 * <ul>
 * <li>
 *   Improve accuracy of calculations by evaluating trigonometric
 *   functions more carefully and replacing the series for the reduced
 *   length with one with a smaller truncation error.
 * <li>
 *   The allowed ranges for longitudes and azimuths is now unlimited;
 *   it used to be [&minus;540&deg;, 540&deg;).
 * <li>
 *   Enforce the restriction of latitude to [&minus;90&deg;, 90&deg;] by
 *   returning NaNs if the latitude is outside this range.
 * <li>
 *   Geodesic.Inverse sets <i>s12</i> to zero for coincident points at pole
 *   (instead of returning a tiny quantity).
 * <li>
 *   Geodesic.Inverse pays attentions to the GeodesicMask.LONG_UNROLL bit in
 *   <i>outmask</i>.
 * </ul>
 * </ul>
 **********************************************************************/
package net.sf.geographiclib;
