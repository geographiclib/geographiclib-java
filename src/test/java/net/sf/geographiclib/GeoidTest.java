package net.sf.geographiclib;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.junit.Assert.*;

public class GeoidTest {

    public static final String GEOID_TEST_RESOURCE = "/GeoidHeights.dat";
    public static final double EPSILON = 0.2; // in meters

    public static final int TESTS_TO_LOAD = 500_000;
    public static final int TESTS_TO_PERFORM = 500_000;

    private static List<GeoidTestPoint> s_testPoints;

    @BeforeClass
    public static void oneTimeSetUp() {
        GeoidTest.init();
        Geoid.init();
    }

    private static boolean init() {
        s_testPoints = new ArrayList<>();

        try {
            InputStream is = GeoidTest.class.getResourceAsStream(GEOID_TEST_RESOURCE);
            if (is == null) throw
                    new RuntimeException("Resource '" + GEOID_TEST_RESOURCE + "' is not available!");

            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            int l = 1; // line counter
            int linesLoaded = 0;
            while (line != null && linesLoaded < TESTS_TO_LOAD) {

                StringTokenizer t = new StringTokenizer(line);
                int c = t.countTokens();

                if (c == 5) {
                    double lat = Double.parseDouble(t.nextToken());
                    double lng = Double.parseDouble(t.nextToken());
                    double offEgm84 = Double.parseDouble(t.nextToken());
                    double offEgm96 = Double.parseDouble(t.nextToken());
                    double offEgm08 = Double.parseDouble(t.nextToken());

                    s_testPoints.add(new GeoidTestPoint(lat, lng, offEgm96));
                    linesLoaded++;
                } else {
                    System.err.println("error on line " + l + ": found " + c + " tokens (expected 5): '" + line + "'");
                }

                line = br.readLine();
                l++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Test
    public void testInit() {
        boolean resultInit = Geoid.init();
        assertTrue(resultInit);
    }

    @Test
    public void initializationTest() {
        boolean success = Geoid.init();
        assertTrue(success);
    }

    @Test
    public void knownEGM96PlacesTest() {

        assertEquals(13.89, Geoid.height(89.74, 5.75), EPSILON);
        assertEquals(11.76, Geoid.height(86.5, 217.25), EPSILON);
        assertEquals(43.56, Geoid.height(71.25, 8.0), EPSILON);
        assertEquals(2.35, Geoid.height(54.0, 182.25), EPSILON);
        assertEquals(-12.91, Geoid.height(35.5, 186.0), EPSILON);
        assertEquals(-8.07, Geoid.height(14.75, 213.5), EPSILON);
        assertEquals(-10.18, Geoid.height(-22.25, 221.5), EPSILON);
        assertEquals(25.61, Geoid.height(-37.5, 13.0), EPSILON);
        assertEquals(-17.88, Geoid.height(-51.75, 157.25), EPSILON);
        assertEquals(5.18, Geoid.height(-70.5, 346.0), EPSILON);
        assertEquals(17.5, Geoid.height(-74.5, 22.0), EPSILON);
        assertEquals(-25.71, Geoid.height(-84.5, 306.25), EPSILON);
        assertEquals(-21.81, Geoid.height(-86.75, 328.75), EPSILON);
        assertEquals(-29.54, Geoid.height(-90.0, 0.0), EPSILON);

    }

    @Test
    public void testAgainstGeographiclibData() {
        assertTrue(Geoid.init());

        Random rand = new Random();
        final List<Double> errors = new ArrayList<>();

        final int testsToPerform = Math.min(s_testPoints.size(), TESTS_TO_PERFORM);

        long startTime = System.currentTimeMillis();
        if (testsToPerform < s_testPoints.size()) {
            // we must choose random points from the available
            rand.ints(0, s_testPoints.size())
                    .mapToObj(s_testPoints::get)
                    .limit(testsToPerform)
                    .forEach(p -> {
                        double h = Geoid.height(p.latitude, p.longitude);
                        if (Double.isNaN(h)) errors.add(Double.NaN);
                        else errors.add(h - p.height);
                    });
        } else {
            // we must test ALL available points
            s_testPoints.forEach(p -> {
                double h = Geoid.height(p.latitude, p.longitude);
                if (Double.isNaN(h)) errors.add(Double.NaN);
                else errors.add(h - p.height);
            });
        }
        long endTime = System.currentTimeMillis();

        assertEquals(testsToPerform, errors.size());
        System.out.printf("Performed %d tests%n", testsToPerform);

        double[] stats = getStats(errors);
        assertNotNull(stats);
        assertEquals(5, stats.length);
        assertEquals(testsToPerform, Math.round(stats[4]));
        double maxAbsError = Math.max(Math.abs(stats[2]), Math.abs(stats[3]));

        double geoidSpeed = (double) testsToPerform / ((double) (endTime - startTime) / 1000.0);
        System.out.printf("Maximum absolute error: %.5f meters%n", maxAbsError);
        System.out.printf("Geoid height performance: %d calls/second%n", Math.round(geoidSpeed));

        assertTrue(maxAbsError < EPSILON);
    }

    // calculate statistics for all values in the list
    // returns array of 5 elements: average, standard deviation, minimum, maximum, count
    private static <T extends Number> double[] getStats(Collection<T> values) {
        if (values == null || values.isEmpty()) return null;

        double sum = 0.0;
        int count = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (T e : values)
            if (e != null) {
                double v = e.doubleValue();
                if (!Double.isNaN(v)) {
                    sum += v;
                    count++;
                    if (v < min) min = v;
                    if (v > max) max = v;
                }
            }

        if (count == 0) return null;

        double mean = sum / ((double) count);
        double sq_diff = 0.0;
        for (T e : values)
            if (e != null) {
                double v = e.doubleValue();
                if (!Double.isNaN(v))
                    sq_diff += (mean - v) * (mean - v);
            }

        double[] result = new double[5]; // avg, stDev, min, max, count
        result[0] = mean;
        result[1] = count == 1 ? 0.0 : Math.sqrt(sq_diff / ((double) (count - 1)));
        result[2] = min;
        result[3] = max;
        result[4] = count;
        return result;
    }

    private static class GeoidTestPoint {
        public final double latitude;
        public final double longitude;
        public final double height;

        GeoidTestPoint(double lat, double lon, double hgt) {
            latitude = lat;
            longitude = lon;
            height = hgt;
        }
    }
}
