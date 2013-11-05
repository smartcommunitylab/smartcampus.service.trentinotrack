package eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder;

import java.util.Arrays;


public class CoordinateConversion {

	public static void main(String[] args) {
		System.err.println(Arrays.toString(new CoordinateConversion().utm2LatLon(32, "N", 698829.798201531, 5129351.04453526)));
	}
	
	public double[] utm2LatLon(int zone, String hemisphere, double easting, double northing) {
		UTM2LatLon c = new UTM2LatLon();
		return c.convertUTMToLatLong(zone, hemisphere, easting, northing);
	}

	public double degreeToRadian(double degree) {
		return degree * Math.PI / 180;
	}

	public double radianToDegree(double radian) {
		return radian * 180 / Math.PI;
	}

	private double POW(double a, double b) {
		return Math.pow(a, b);
	}

	private double SIN(double value) {
		return Math.sin(value);
	}

	private double COS(double value) {
		return Math.cos(value);
	}

	private double TAN(double value) {
		return Math.tan(value);
	}


	private class UTM2LatLon {
		double easting;
		double northing;

		public double[] convertUTMToLatLong(int zone, String hemisphere, double east, double north) {
			double[] latlon = { 0.0, 0.0 };
			double latitude = 0.0;
			double longitude = 0.0;
			easting = east;
			northing = north;

			if (hemisphere.equals("S")) {
				northing = 10000000 - northing;
			}
			setVariables();
			latitude = 180 * (phi1 - fact1 * (fact2 + fact3 + fact4)) / Math.PI;

			if (zone > 0) {
				zoneCM = 6 * zone - 183.0;
			} else {
				zoneCM = 3.0;

			}

			longitude = zoneCM - _a3;
			if (hemisphere.equals("S")) {
				latitude = -latitude;
			}

			latlon[0] = latitude;
			latlon[1] = longitude;
			return latlon;

		}

		protected void setVariables() {
			arc = northing / k0;
			mu = arc
					/ (a * (1 - POW(e, 2) / 4.0 - 3 * POW(e, 4) / 64.0 - 5 * POW(
							e, 6) / 256.0));

			ei = (1 - POW((1 - e * e), (1 / 2.0)))
					/ (1 + POW((1 - e * e), (1 / 2.0)));

			ca = 3 * ei / 2 - 27 * POW(ei, 3) / 32.0;

			cb = 21 * POW(ei, 2) / 16 - 55 * POW(ei, 4) / 32;
			cc = 151 * POW(ei, 3) / 96;
			cd = 1097 * POW(ei, 4) / 512;
			phi1 = mu + ca * SIN(2 * mu) + cb * SIN(4 * mu) + cc * SIN(6 * mu)
					+ cd * SIN(8 * mu);

			n0 = a / POW((1 - POW((e * SIN(phi1)), 2)), (1 / 2.0));

			r0 = a * (1 - e * e)
					/ POW((1 - POW((e * SIN(phi1)), 2)), (3 / 2.0));
			fact1 = n0 * TAN(phi1) / r0;

			_a1 = 500000 - easting;
			dd0 = _a1 / (n0 * k0);
			fact2 = dd0 * dd0 / 2;

			t0 = POW(TAN(phi1), 2);
			Q0 = e1sq * POW(COS(phi1), 2);
			fact3 = (5 + 3 * t0 + 10 * Q0 - 4 * Q0 * Q0 - 9 * e1sq)
					* POW(dd0, 4) / 24;

			fact4 = (61 + 90 * t0 + 298 * Q0 + 45 * t0 * t0 - 252 * e1sq - 3
					* Q0 * Q0)
					* POW(dd0, 6) / 720;

			//
			lof1 = _a1 / (n0 * k0);
			lof2 = (1 + 2 * t0 + Q0) * POW(dd0, 3) / 6.0;
			lof3 = (5 - 2 * Q0 + 28 * t0 - 3 * POW(Q0, 2) + 8 * e1sq + 24 * POW(
					t0, 2)) * POW(dd0, 5) / 120;
			_a2 = (lof1 - lof2 + lof3) / COS(phi1);
			_a3 = _a2 * 180 / Math.PI;

		}

		double arc;

		double mu;

		double ei;

		double ca;

		double cb;

		double cc;

		double cd;

		double n0;

		double r0;

		double _a1;

		double dd0;

		double t0;

		double Q0;

		double lof1;

		double lof2;

		double lof3;

		double _a2;

		double phi1;

		double fact1;

		double fact2;

		double fact3;

		double fact4;

		double zoneCM;

		double _a3;

		double a = 6378137;

		double e = 0.081819191;

		double e1sq = 0.006739497;

		double k0 = 0.9996;

	}

}
