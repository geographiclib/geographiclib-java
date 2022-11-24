package net.sf.geographiclib;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Geoid {

    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static char END_LINE = 0x0a;
    private final static int TD = 360;
    private final static int HD = 180;
    private final static int STENCIL_SIZE = 12;
    private final static int N_TERMS = ((3 + 1) * (3 + 2)) / 2; // for the cubic fit
    private final static int C_0 = 240;
    private final static int C_0_N = 372;
    private final static int C_0_S = 372;

    private static final int[] C_3 = {
            9, -18, -88, 0, 96, 90, 0, 0, -60, -20,
            -9, 18, 8, 0, -96, 30, 0, 0, 60, -20,
            9, -88, -18, 90, 96, 0, -20, -60, 0, 0,
            186, -42, -42, -150, -96, -150, 60, 60, 60, 60,
            54, 162, -78, 30, -24, -90, -60, 60, -60, 60,
            -9, -32, 18, 30, 24, 0, 20, -60, 0, 0,
            -9, 8, 18, 30, -96, 0, -20, 60, 0, 0,
            54, -78, 162, -90, -24, 30, 60, -60, 60, -60,
            -54, 78, 78, 90, 144, 90, -60, -60, -60, -60,
            9, -8, -18, -30, -24, 0, 20, 60, 0, 0,
            -9, 18, -32, 0, 24, 30, 0, 0, -60, 20,
            9, -18, -8, 0, -24, -30, 0, 0, 60, 20,
    };

    private static final int[] C_3_N = {
            0, 0, -131, 0, 138, 144, 0, 0, -102, -31,
            0, 0, 7, 0, -138, 42, 0, 0, 102, -31,
            62, 0, -31, 0, 0, -62, 0, 0, 0, 31,
            124, 0, -62, 0, 0, -124, 0, 0, 0, 62,
            124, 0, -62, 0, 0, -124, 0, 0, 0, 62,
            62, 0, -31, 0, 0, -62, 0, 0, 0, 31,
            0, 0, 45, 0, -183, -9, 0, 93, 18, 0,
            0, 0, 216, 0, 33, 87, 0, -93, 12, -93,
            0, 0, 156, 0, 153, 99, 0, -93, -12, -93,
            0, 0, -45, 0, -3, 9, 0, 93, -18, 0,
            0, 0, -55, 0, 48, 42, 0, 0, -84, 31,
            0, 0, -7, 0, -48, -42, 0, 0, 84, 31,
    };

    private static final int[] C_3_S = {
            18, -36, -122, 0, 120, 135, 0, 0, -84, -31,
            -18, 36, -2, 0, -120, 51, 0, 0, 84, -31,
            36, -165, -27, 93, 147, -9, 0, -93, 18, 0,
            210, 45, -111, -93, -57, -192, 0, 93, 12, 93,
            162, 141, -75, -93, -129, -180, 0, 93, -12, 93,
            -36, -21, 27, 93, 39, 9, 0, -93, -18, 0,
            0, 0, 62, 0, 0, 31, 0, 0, 0, -31,
            0, 0, 124, 0, 0, 62, 0, 0, 0, -62,
            0, 0, 124, 0, 0, 62, 0, 0, 0, -62,
            0, 0, 62, 0, 0, 31, 0, 0, 0, -31,
            -18, 36, -64, 0, 66, 51, 0, 0, -102, 31,
            18, -36, 2, 0, -66, -51, 0, 0, 102, 31,
    };

    private static String _name;
    private static boolean _cubic;
    private static double _rlonres = Double.NaN;
    private static double _rlatres = Double.NaN;
    private static String _description;
    private static LocalDateTime _datetime;
    private static double _offset = Double.NaN;
    private static double _scale = Double.NaN;
    private static double _maxerror = Double.NaN;
    private static double _rmserror = Double.NaN;

    private static int image_width, image_height;
    private static short[] image_data;
    private static boolean isInitialized = false;

    // default Geoid is EGM96 with 15 minutes resolution
    // default interpolation method is cubic
    public static boolean init() {
        if (isInitialized) return true;

        return init("egm96-15", true);
    }

    public static boolean init(String name, Boolean cubic) {

        _cubic = cubic == null ? true : cubic;
        _name = name == null ? "egm96-15" : name;

        String resourceName = "/GeoidData/" + name + ".pgm";
        try (InputStream is = Geoid.class.getResourceAsStream(resourceName)) {
            if (is == null) throw new GeographicErr("Resource '" + resourceName + "' is not available!");

            String s = readString(is);
            if (!s.equals("P5"))
                throw new GeographicErr("The image is not in PGM format!");

            double mce = 0, mbe = 0, rce = 0, rbe = 0;
            boolean done = false;
            boolean successfulHeaderDecode = false;

            while (!done && !"".equals(s = readString(is))) {

                if (s.startsWith("#")) {
                    if (s.startsWith("# Description")) {
                        _description = s.substring(13).trim();
                    } else if (s.startsWith("# DateTime")) {
                        try {
                            _datetime = LocalDateTime.parse(s.substring(10).trim(), dtf);
                        } catch (DateTimeParseException ex) {
                            // nothing to do
                        }
                    } else if (s.startsWith("# Offset")) {
                        try {
                            _offset = Double.parseDouble(s.substring(8).trim());
                        } catch (NumberFormatException ex) {
                            // nothing to do
                        }
                    } else if (s.startsWith("# Scale")) {
                        try {
                            _scale = Double.parseDouble(s.substring(7).trim());
                        } catch (NumberFormatException ex) {
                            // nothing to do
                        }
                    } else if (s.startsWith("# MaxCubicError")) {
                        try {
                            mce = Double.parseDouble(s.substring(15).trim());
                        } catch (NumberFormatException ex) {
                            // nothing to do
                        }
                    } else if (s.startsWith("# MaxBilinearError")) {
                        try {
                            mbe = Double.parseDouble(s.substring(18).trim());
                        } catch (NumberFormatException ex) {
                            // nothing to do
                        }
                    } else if (s.startsWith("# RMSCubicError")) {
                        try {
                            rce = Double.parseDouble(s.substring(15).trim());
                        } catch (NumberFormatException ex) {
                            // nothing to do
                        }
                    } else if (s.startsWith("# RMSBilinearError")) {
                        try {
                            rbe = Double.parseDouble(s.substring(18).trim());
                        } catch (NumberFormatException ex) {
                            // nothing to do
                        }
                    }
                } else {
                    if (s.contains(" ")) {
                        // resolution
                        try {
                            int i = s.indexOf(' ');
                            image_width = Integer.parseInt(s.substring(0, i).trim());
                            image_height = Integer.parseInt(s.substring(i + 1).trim());
                        } catch (NumberFormatException ex) {
                            // nothing to do
                            done = true;
                        }
                    } else {
                        // the image must have 2 bytes per pixel
                        if (s.equals("65535")) successfulHeaderDecode = true;

                        done = true;
                    }
                }
            }
            _maxerror = _cubic ? mce : mbe;
            _rmserror = _cubic ? rce : rbe;

            if (!successfulHeaderDecode)
                throw new GeographicErr("Cannot decode image header in resource " + resourceName);

            if (Double.isNaN(_offset))
                throw new GeographicErr("Cannot estimate offset in resource " + resourceName);

            if (Double.isNaN(_scale))
                throw new GeographicErr("Cannot estimate scale in resource " + resourceName);

            if (_scale < 0)
                throw new GeographicErr("Scale must be positive in resource " + resourceName);

            if (image_height < 2 || image_width < 2)
                throw new GeographicErr("Image resolution is too small in resource " + resourceName);

            _rlonres = (double) image_width / (double) TD;
            _rlatres = ((double) (image_height - 1)) / (double) HD;

            int wordsLeft = image_width * image_height;
            image_data = new short[wordsLeft];
            int b1, b2;
            int idx = 0;
            while ((wordsLeft > 0) && ((b1 = is.read()) != -1) && ((b2 = is.read()) != -1)) {
                wordsLeft--;

                // in PGM images the most significant byte is first
                int v = ((b1 << 8) & 0xff00) + (b2 & 0x00ff);
                image_data[idx] = (short) v;
                idx++;
            }

            if (wordsLeft == 0 && idx == image_width * image_height) {
                isInitialized = true;
            }

            return isInitialized;
        } catch (IOException ex) {
            System.err.println(ex.toString());
            isInitialized = false;
            return isInitialized;
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();

        boolean done = false;
        while (!done) {
            int x = is.read();
            if (x == -1) done = true;
            else {
                char c = (char) x;
                if (c == END_LINE) done = true;
                else sb.append(c);
            }
        }

        return sb.toString();
    }

    public static String getDescription() {
        return _description;
    }

    public static LocalDateTime getDateTime() {
        return _datetime;
    }

    public static String getGeoidName() {
        return _name;
    }

    public static String getInterpolation() {
        return _cubic ? "cubic" : "bilinear";
    }

    public static double getMaxError() {
        return _maxerror;
    }

    public static double getRMSError() {
        return _rmserror;
    }

    public static double getScale() {
        return _scale;
    }

    private static int imageValue(int ix, int iy) {
        while (ix < 0) ix += image_width;
        while (ix >= image_width) ix -= image_width;

        if (iy < 0) iy = 0;
        if (iy >= image_height) iy = image_height - 1;

        return Short.toUnsignedInt(image_data[iy * image_width + ix]);
    }

    public static double height(double lat, double lon) {
        if (Double.isNaN(lat) || Math.abs(lat) > 90.0) return Double.NaN;

        if (Double.isNaN(lon)) return Double.NaN;

        if (!isInitialized) if (!init()) return Double.NaN;

        while (lon > 360.0) lon -= 360.0;
        while (lon < 0.0) lon += 360.0;

        double fx = lon * _rlonres;
        double fy = (90.0 - lat) * _rlatres;

        int ix = (int) Math.floor(fx);
        int iy = (int) Math.floor(fy);

        fx -= ix;
        fy -= iy;

        ix += ix < 0 ? image_width : (ix >= image_width ? -image_width : 0);

        if (!_cubic) {
            double v00, v01, v10, v11;

            v00 = imageValue(ix, iy);
            v01 = imageValue(ix + 1, iy);
            v10 = imageValue(ix, iy + 1);
            v11 = imageValue(ix + 1, iy + 1);

            double a = (1 - fx) * v00 + fx * v01;
            double b = (1 - fx) * v10 + fx * v11;
            double c = (1 - fy) * a + fy * b;

            return _offset + _scale * c;
        } else {
            int k = 0;
            double[] v = new double[STENCIL_SIZE];
            v[k++] = imageValue(ix, iy - 1);
            v[k++] = imageValue(ix + 1, iy - 1);
            v[k++] = imageValue(ix - 1, iy);
            v[k++] = imageValue(ix, iy);
            v[k++] = imageValue(ix + 1, iy);
            v[k++] = imageValue(ix + 2, iy);
            v[k++] = imageValue(ix - 1, iy + 1);
            v[k++] = imageValue(ix, iy + 1);
            v[k++] = imageValue(ix + 1, iy + 1);
            v[k++] = imageValue(ix + 2, iy + 1);
            v[k++] = imageValue(ix, iy + 2);
            v[k] = imageValue(ix + 1, iy + 2);

            int[] c3x = iy == 0 ? C_3_N : (iy == image_height - 2 ? C_3_S : C_3);
            int c0x = iy == 0 ? C_0_N : (iy == image_height - 2 ? C_0_S : C_0);

            double[] t = new double[N_TERMS];
            for (int i = 0; i < N_TERMS; i++) {
                t[i] = 0;
                for (int j = 0; j < STENCIL_SIZE; j++)
                    t[i] += v[j] * (double) c3x[N_TERMS * j + i];
                t[i] /= (double) c0x;
            }

            double c = t[0] + fx * (t[1] + fx * (t[3] + fx * t[6])) +
                    fy * (t[2] + fx * (t[4] + fx * t[7]) +
                            fy * (t[5] + fx * t[8] + fy * t[9]));

            return _offset + _scale * c;
        }
    }
}
