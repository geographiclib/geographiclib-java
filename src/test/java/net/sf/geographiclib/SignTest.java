package net.sf.geographiclib.signtest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import net.sf.geographiclib.*;

public class SignTest {

  private static boolean equiv(double x, double y) {
    // Test for equivalence
    return (Double.isNaN(x) && Double.isNaN(y)) ||
      (x == y && Math.copySign(1, x) == Math.copySign(1, y));
  }

  @Test
  public void test_AngRound() {
    // Test special cases for AngRound
    double eps = Math.ulp(1.0);
    assertTrue(equiv(GeoMath.AngRound(-eps/32), -eps/32));
    assertTrue(equiv(GeoMath.AngRound(-eps/64), -0.0   ));
    assertTrue(equiv(GeoMath.AngRound(-  0.0 ), -0.0   ));
    assertTrue(equiv(GeoMath.AngRound(   0.0 ), +0.0   ));
    assertTrue(equiv(GeoMath.AngRound( eps/64), +0.0   ));
    assertTrue(equiv(GeoMath.AngRound( eps/32), +eps/32));
    assertTrue(equiv(GeoMath.AngRound((1-2*eps)/64), (1-2*eps)/64));
    assertTrue(equiv(GeoMath.AngRound((1-eps  )/64),  1.0     /64));
    assertTrue(equiv(GeoMath.AngRound((1-eps/2)/64),  1.0     /64));
    assertTrue(equiv(GeoMath.AngRound((1-eps/4)/64),  1.0     /64));
    assertTrue(equiv(GeoMath.AngRound( 1.0     /64),  1.0     /64));
    assertTrue(equiv(GeoMath.AngRound((1+eps/2)/64),  1.0     /64));
    assertTrue(equiv(GeoMath.AngRound((1+eps  )/64),  1.0     /64));
    assertTrue(equiv(GeoMath.AngRound((1+2*eps)/64), (1+2*eps)/64));
    assertTrue(equiv(GeoMath.AngRound((1-eps  )/32), (1-eps  )/32));
    assertTrue(equiv(GeoMath.AngRound((1-eps/2)/32),  1.0     /32));
    assertTrue(equiv(GeoMath.AngRound((1-eps/4)/32),  1.0     /32));
    assertTrue(equiv(GeoMath.AngRound( 1.0     /32),  1.0     /32));
    assertTrue(equiv(GeoMath.AngRound((1+eps/2)/32),  1.0     /32));
    assertTrue(equiv(GeoMath.AngRound((1+eps  )/32), (1+eps  )/32));
    assertTrue(equiv(GeoMath.AngRound((1-eps  )/16), (1-eps  )/16));
    assertTrue(equiv(GeoMath.AngRound((1-eps/2)/16), (1-eps/2)/16));
    assertTrue(equiv(GeoMath.AngRound((1-eps/4)/16),  1.0     /16));
    assertTrue(equiv(GeoMath.AngRound( 1.0     /16),  1.0     /16));
    assertTrue(equiv(GeoMath.AngRound((1+eps/4)/16),  1.0     /16));
    assertTrue(equiv(GeoMath.AngRound((1+eps/2)/16),  1.0     /16));
    assertTrue(equiv(GeoMath.AngRound((1+eps  )/16), (1+eps  )/16));
    assertTrue(equiv(GeoMath.AngRound((1-eps  )/ 8), (1-eps  )/ 8));
    assertTrue(equiv(GeoMath.AngRound((1-eps/2)/ 8), (1-eps/2)/ 8));
    assertTrue(equiv(GeoMath.AngRound((1-eps/4)/ 8),  1.0     / 8));
    assertTrue(equiv(GeoMath.AngRound((1+eps/2)/ 8),  1.0     / 8));
    assertTrue(equiv(GeoMath.AngRound((1+eps  )/ 8), (1+eps  )/ 8));
    assertTrue(equiv(GeoMath.AngRound( 1-eps      ),  1-eps      ));
    assertTrue(equiv(GeoMath.AngRound( 1-eps/2    ),  1-eps/2    ));
    assertTrue(equiv(GeoMath.AngRound( 1-eps/4    ),  1          ));
    assertTrue(equiv(GeoMath.AngRound( 1.0        ),  1          ));
    assertTrue(equiv(GeoMath.AngRound( 1+eps/4    ),  1          ));
    assertTrue(equiv(GeoMath.AngRound( 1+eps/2    ),  1          ));
    assertTrue(equiv(GeoMath.AngRound( 1+eps      ),  1+  eps    ));
    assertTrue(equiv(GeoMath.AngRound( 90.0-64*eps),  90-64*eps  ));
    assertTrue(equiv(GeoMath.AngRound( 90.0-32*eps),  90         ));
    assertTrue(equiv(GeoMath.AngRound( 90.0       ),  90         ));
  }

  @Test
  public void test_sincosd() {
    // Test special cases for sincosd
    double inf = Double.POSITIVE_INFINITY, nan = Double.NaN;
    Pair p = new Pair();
    GeoMath.sincosd(p, -  inf);
    assertTrue(equiv(p.first,  nan) && equiv(p.second,  nan));
    GeoMath.sincosd(p, -810.0);
    assertTrue(equiv(p.first, -1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, -720.0);
    assertTrue(equiv(p.first, -0.0) && equiv(p.second, +1.0));
    GeoMath.sincosd(p, -630.0);
    assertTrue(equiv(p.first, +1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, -540.0);
    assertTrue(equiv(p.first, -0.0) && equiv(p.second, -1.0));
    GeoMath.sincosd(p, -450.0);
    assertTrue(equiv(p.first, -1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, -360.0);
    assertTrue(equiv(p.first, -0.0) && equiv(p.second, +1.0));
    GeoMath.sincosd(p, -270.0);
    assertTrue(equiv(p.first, +1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, -180.0);
    assertTrue(equiv(p.first, -0.0) && equiv(p.second, -1.0));
    GeoMath.sincosd(p, - 90.0);
    assertTrue(equiv(p.first, -1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, -  0.0);
    assertTrue(equiv(p.first, -0.0) && equiv(p.second, +1.0));
    GeoMath.sincosd(p, +  0.0);
    assertTrue(equiv(p.first, +0.0) && equiv(p.second, +1.0));
    GeoMath.sincosd(p, + 90.0);
    assertTrue(equiv(p.first, +1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, +180.0);
    assertTrue(equiv(p.first, +0.0) && equiv(p.second, -1.0));
    GeoMath.sincosd(p, +270.0);
    assertTrue(equiv(p.first, -1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, +360.0);
    assertTrue(equiv(p.first, +0.0) && equiv(p.second, +1.0));
    GeoMath.sincosd(p, +450.0);
    assertTrue(equiv(p.first, +1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, +540.0);
    assertTrue(equiv(p.first, +0.0) && equiv(p.second, -1.0));
    GeoMath.sincosd(p, +630.0);
    assertTrue(equiv(p.first, -1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, +720.0);
    assertTrue(equiv(p.first, +0.0) && equiv(p.second, +1.0));
    GeoMath.sincosd(p, +810.0);
    assertTrue(equiv(p.first, +1.0) && equiv(p.second, +0.0));
    GeoMath.sincosd(p, +  inf);
    assertTrue(equiv(p.first,  nan) && equiv(p.second,  nan));
    GeoMath.sincosd(p,   nan);
    assertTrue(equiv(p.first,  nan) && equiv(p.second,  nan));

    /// Test accuracy of sincosd
    double s1, c1, s2, c2, s3, c3;
    GeoMath.sincosd(p,          9.0); s1 = p.first; c1 = p.second;
    GeoMath.sincosd(p,         81.0); s2 = p.first; c2 = p.second;
    GeoMath.sincosd(p, -123456789.0); s3 = p.first; c3 = p.second;
    assertTrue(equiv(s1, c2));
    assertTrue(equiv(s1, s3));
    assertTrue(equiv(c1, s2));
    assertTrue(equiv(c1,-c3));
  }

  @Test
  public void test_atan2d() {
    // Test special cases for atan2d
    double inf = Double.POSITIVE_INFINITY, nan = Double.NaN;
    assertTrue(equiv(GeoMath.atan2d(+0.0 , -0.0 ), +180));
    assertTrue(equiv(GeoMath.atan2d(-0.0 , -0.0 ), -180));
    assertTrue(equiv(GeoMath.atan2d(+0.0 , +0.0 ), +0.0));
    assertTrue(equiv(GeoMath.atan2d(-0.0 , +0.0 ), -0.0));
    assertTrue(equiv(GeoMath.atan2d(+0.0 , -1.0 ), +180));
    assertTrue(equiv(GeoMath.atan2d(-0.0 , -1.0 ), -180));
    assertTrue(equiv(GeoMath.atan2d(+0.0 , +1.0 ), +0.0));
    assertTrue(equiv(GeoMath.atan2d(-0.0 , +1.0 ), -0.0));
    assertTrue(equiv(GeoMath.atan2d(-1.0 , +0.0 ),  -90));
    assertTrue(equiv(GeoMath.atan2d(-1.0 , -0.0 ),  -90));
    assertTrue(equiv(GeoMath.atan2d(+1.0 , +0.0 ),  +90));
    assertTrue(equiv(GeoMath.atan2d(+1.0 , -0.0 ),  +90));
    assertTrue(equiv(GeoMath.atan2d(+1.0 ,  -inf), +180));
    assertTrue(equiv(GeoMath.atan2d(-1.0 ,  -inf), -180));
    assertTrue(equiv(GeoMath.atan2d(+1.0 ,  +inf), +0.0));
    assertTrue(equiv(GeoMath.atan2d(-1.0 ,  +inf), -0.0));
    assertTrue(equiv(GeoMath.atan2d( +inf, +1.0 ),  +90));
    assertTrue(equiv(GeoMath.atan2d( +inf, -1.0 ),  +90));
    assertTrue(equiv(GeoMath.atan2d( -inf, +1.0 ),  -90));
    assertTrue(equiv(GeoMath.atan2d( -inf, -1.0 ),  -90));
    assertTrue(equiv(GeoMath.atan2d( +inf,  -inf), +135));
    assertTrue(equiv(GeoMath.atan2d( -inf,  -inf), -135));
    assertTrue(equiv(GeoMath.atan2d( +inf,  +inf),  +45));
    assertTrue(equiv(GeoMath.atan2d( -inf,  +inf),  -45));
    assertTrue(equiv(GeoMath.atan2d(  nan, +1.0 ),  nan));
    assertTrue(equiv(GeoMath.atan2d(+1.0 ,   nan),  nan));

    // Test accuracy of atan2d
    double s = 7e-16;
    assertEquals(GeoMath.atan2d(s, -1.0), 180 - GeoMath.atan2d(s, 1.0), 0);
  }

  @Test
  public void test_sum() {
    // Test special cases of sum
    Pair p = new Pair();
    GeoMath.sum(p, +9.0, -9.0); assertTrue(equiv(p.first, +0.0));
    GeoMath.sum(p, -9.0, +9.0); assertTrue(equiv(p.first, +0.0));
    GeoMath.sum(p, -0.0, +0.0); assertTrue(equiv(p.first, +0.0));
    GeoMath.sum(p, +0.0, -0.0); assertTrue(equiv(p.first, +0.0));
    GeoMath.sum(p, -0.0, -0.0); assertTrue(equiv(p.first, -0.0));
    GeoMath.sum(p, +0.0, +0.0); assertTrue(equiv(p.first, +0.0));
  }

  @Test
  public void test_AngNormalize() {
    // Test special cases of AngNormalize
    assertTrue(equiv(GeoMath.AngNormalize(-900.0), -180));
    assertTrue(equiv(GeoMath.AngNormalize(-720.0), -0.0));
    assertTrue(equiv(GeoMath.AngNormalize(-540.0), -180));
    assertTrue(equiv(GeoMath.AngNormalize(-360.0), -0.0));
    assertTrue(equiv(GeoMath.AngNormalize(-180.0), -180));
    assertTrue(equiv(GeoMath.AngNormalize(  -0.0), -0.0));
    assertTrue(equiv(GeoMath.AngNormalize(  +0.0), +0.0));
    assertTrue(equiv(GeoMath.AngNormalize( 180.0), +180));
    assertTrue(equiv(GeoMath.AngNormalize( 360.0), +0.0));
    assertTrue(equiv(GeoMath.AngNormalize( 540.0), +180));
    assertTrue(equiv(GeoMath.AngNormalize( 720.0), +0.0));
    assertTrue(equiv(GeoMath.AngNormalize( 900.0), +180));
  }

  @Test
  public void test_AngDiff() {
    // Test special cases of AngDiff
    double eps = Math.ulp(1.0);
    Pair p = new Pair();
    GeoMath.AngDiff(p, +  0.0,+  0.0); assertTrue(equiv(p.first,+0.0 ));
    GeoMath.AngDiff(p, +  0.0,-  0.0); assertTrue(equiv(p.first,-0.0 ));
    GeoMath.AngDiff(p, -  0.0,+  0.0); assertTrue(equiv(p.first,+0.0 ));
    GeoMath.AngDiff(p, -  0.0,-  0.0); assertTrue(equiv(p.first,+0.0 ));
    GeoMath.AngDiff(p, +  5.0,+365.0); assertTrue(equiv(p.first,+0.0 ));
    GeoMath.AngDiff(p, +365.0,+  5.0); assertTrue(equiv(p.first,-0.0 ));
    GeoMath.AngDiff(p, +  5.0,+185.0); assertTrue(equiv(p.first,+180.0));
    GeoMath.AngDiff(p, +185.0,+  5.0); assertTrue(equiv(p.first,-180.0));
    GeoMath.AngDiff(p,  +eps ,+180.0); assertTrue(equiv(p.first,+180.0));
    GeoMath.AngDiff(p,  -eps ,+180.0); assertTrue(equiv(p.first,-180.0));
    GeoMath.AngDiff(p,  +eps ,-180.0); assertTrue(equiv(p.first,+180.0));
    GeoMath.AngDiff(p,  -eps ,-180.0); assertTrue(equiv(p.first,-180.0));

    // Test accuracy of AngDiff
    double x = 138 + 128 * eps, y = -164; GeoMath.AngDiff(p, x, y);
    assertEquals(p.first, 58 - 128 * eps, 0);
  }

  @Test
  public void test_equatorial_coincident() {
    // azimuth with coincident point on equator
    //  lat1 lat2 azi1/2
    double C[][] = {
      { +0.0, -0.0, 180 },
      { -0.0, +0.0,   0 }
    };
    for (int i = 0; i < C.length; ++i) {
      GeodesicData inv = Geodesic.WGS84.Inverse(C[i][0], 0.0, C[i][1], 0.0);
      assertTrue(equiv(inv.azi1, C[i][2]));
      assertTrue(equiv(inv.azi2, C[i][2]));
    }
  }

  @Test
  public void test_equatorial_NS() {
    // Does the nearly antipodal equatorial solution go north or south?
    //  lat1 lat2 azi1 azi2
    double C[][] = {
      { +0.0, +0.0,  56, 124},
      { -0.0, -0.0, 124,  56}
    };
    for (int i = 0; i < C.length; ++i) {
      GeodesicData inv = Geodesic.WGS84.Inverse(C[i][0], 0.0, C[i][1], 179.5);
      assertEquals(inv.azi1, C[i][2], 1);
      assertEquals(inv.azi2, C[i][3], 1);
    }
  }

  @Test
  public void test_antipodal() {
    // How does the exact antipodal equatorial path go N/S + E/W"""
    //  lat1 lat2 lon2 azi1 azi2
    double C[][] = {
      { +0.0, +0.0, +180,   +0.0, +180},
      { -0.0, -0.0, +180, +180,   +0.0},
      { +0.0, +0.0, -180,   -0.0, -180},
      { -0.0, -0.0, -180, -180,   -0.0}
    };
    for (int i = 0; i < C.length; ++i) {
      GeodesicData inv = Geodesic.WGS84.Inverse(C[i][0], 0.0, C[i][1], C[i][2]);
      assertTrue(equiv(inv.azi1, C[i][3]));
      assertTrue(equiv(inv.azi2, C[i][4]));
    }
  }

  @Test
  public void test_antipodal_prolate() {
  // Antipodal points on the equator with prolate ellipsoid
  //  lon2 azi1/2
    double C[][] = {
      { +180, +90 },
      { -180, -90 }
    };
    Geodesic geod = new Geodesic(6.4e6, -1/300.0);
    for (int i = 0; i < C.length; ++i) {
      GeodesicData inv = geod.Inverse(0.0, 0.0, 0.0, C[i][0]);
      assertTrue(equiv(inv.azi1, C[i][1]));
      assertTrue(equiv(inv.azi2, C[i][1]));
    }
  }

  @Test
  public void test_azimuth_0_180() {
    // azimuths = +/-0 and +/-180 for the direct problem
    //  azi1, lon2, azi2
    double C[][] = {
      { +0.0, +180, +180 },
      { -0.0, -180, -180 },
      { +180, +180, +0.0 },
      { -180, -180, -0.0 }
    };
    for (int i = 0; i < C.length; ++i) {
      GeodesicData dir =
        Geodesic.WGS84.Direct(0.0, 0.0, C[i][0], 15e6,
                              GeodesicMask.STANDARD | GeodesicMask.LONG_UNROLL);
      assertTrue(equiv(dir.lon2, C[i][1]));
      assertTrue(equiv(dir.azi2, C[i][2]));
    }
  }
}
