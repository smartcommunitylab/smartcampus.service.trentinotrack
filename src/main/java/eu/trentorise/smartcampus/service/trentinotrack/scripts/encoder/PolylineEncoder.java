package eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class PolylineEncoder {

//	private int numLevels = 18;

//	private int zoomFactor = 2;
//
	private double verySmall = 0.00001;

//	private boolean forceEndpoints = true;

//	private double[] zoomLevelBreaks;

	private HashMap<String, Double> bounds;

	// constructor
	public PolylineEncoder(double verySmall) {

//		this.numLevels = numLevels;
//		this.zoomFactor = zoomFactor;
		this.verySmall = verySmall;
//		this.forceEndpoints = forceEndpoints;

//		this.zoomLevelBreaks = new double[numLevels];

//		for (int i = 0; i < numLevels; i++) {
//			this.zoomLevelBreaks[i] = verySmall
//					* Math.pow(this.zoomFactor, numLevels - i - 1);
//		}
	}

	public PolylineEncoder() {
//		this.zoomLevelBreaks = new double[numLevels];

//		for (int i = 0; i < numLevels; i++) {
//			this.zoomLevelBreaks[i] = verySmall
//					* Math.pow(this.zoomFactor, numLevels - i - 1);
//		}
	}
	/**
	 * Douglas-Peucker algorithm, adapted for encoding
	 * 
	 * @return HashMap [EncodedPoints;EncodedLevels]
	 * 
	 */
	public String dpEncode(List<double[]> track) {
		int i, maxLoc = 0;
		Stack<int[]> stack = new Stack<int[]>();
		double[] dists = new double[track.size()];
		double maxDist, absMaxDist = 0.0, temp = 0.0;
		int[] current;
		String encodedPoints;
//		String encodedLevels;

		if (track.size() > 2) {
			int[] stackVal = new int[] { 0, (track.size() - 1) };
			stack.push(stackVal);

			while (stack.size() > 0) {
				current = stack.pop();
				maxDist = 0;

				for (i = current[0] + 1; i < current[1]; i++) {
					temp = this.distance(track.get(i), track
							.get(current[0]), track
							.get(current[1]));
					if (temp > maxDist) {
						maxDist = temp;
						maxLoc = i;
						if (maxDist > absMaxDist) {
							absMaxDist = maxDist;
						}
					}
				}
				if (maxDist > this.verySmall) {
					dists[maxLoc] = maxDist;
					int[] stackValCurMax = { current[0], maxLoc };
					stack.push(stackValCurMax);
					int[] stackValMaxCur = { maxLoc, current[1] };
					stack.push(stackValMaxCur);
				}
			}
		}

		encodedPoints = createEncodings(track, dists);
		encodedPoints = replace(encodedPoints, "\\", "\\\\");
//		encodedLevels = encodeLevels(track, dists, absMaxDist);

//		HashMap<String, String> hm = new HashMap<String, String>();
//		hm.put("encodedPoints", encodedPoints);
//		hm.put("encodedLevels", encodedLevels);
		return encodedPoints;

	}

	private String replace(String s, String one, String another) {
		// In a string replace one substring with another
		if (s.equals(""))
			return "";
		String res = "";
		int i = s.indexOf(one, 0);
		int lastpos = 0;
		while (i != -1) {
			res += s.substring(lastpos, i) + another;
			lastpos = i + one.length();
			i = s.indexOf(one, lastpos);
		}
		res += s.substring(lastpos); // the rest
		return res;
	}

	/**
	 * distance(p0, p1, p2) computes the distance between the point p0 and the
	 * segment [p1,p2]. This could probably be replaced with something that is a
	 * bit more numerically stable.
	 * 
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	private double distance(double[] p0, double[] p1, double[] p2) {
		double u, out = 0.0;

		if (p1[0] == p2[0]
				&& p1[1] == p2[1]) {
			out = Math.sqrt(Math.pow(p2[0] - p0[0], 2)
					+ Math.pow(p2[1] - p0[1], 2));
		} else {
			u = ((p0[0] - p1[0])
					* (p2[0] - p1[0]) + (p0
					[1] - p1[1])
					* (p2[1] - p1[1]))
					/ (Math.pow(p2[0] - p1[0], 2) + Math
							.pow(p2[1] - p1[1], 2));

			if (u <= 0) {
				out = Math.sqrt(Math.pow(p0[0] - p1[0],
						2)
						+ Math.pow(p0[1] - p1[1], 2));
			}
			if (u >= 1) {
				out = Math.sqrt(Math.pow(p0[0] - p2[0],
						2)
						+ Math.pow(p0[1] - p2[1], 2));
			}
			if (0 < u && u < 1) {
				out = Math.sqrt(Math.pow(p0[0] - p1[0]
						- u * (p2[0] - p1[0]), 2)
						+ Math.pow(p0[1] - p1[1] - u
								* (p2[1] - p1[1]), 2));
			}
		}
		return out;
	}

//	/**
//	 * @param points
//	 *            set the points that should be encoded all points have to be in
//	 *            the following form: Latitude, longitude\n
//	 */
//	public static Track pointsToTrack(String points) {
//		Track trk = new Track();
//
//		StringTokenizer st = new StringTokenizer(points, "\n");
//		while (st.hasMoreTokens()) {
//			String[] pointStrings = st.nextToken().split(", ");
//			trk.addTrackpoint(new Trackpoint(new Double(pointStrings[0]),
//					new Double(pointStrings[1])));
//		}
//		return trk;
//	}

//	/**
//	 * @param LineString
//	 *            set the points that should be encoded all points have to be in
//	 *            the following form: Longitude,Latitude,Altitude"_"...
//	 */
//	public static Track kmlLineStringToTrack(String points) {
//		Track trk = new Track();
//		StringTokenizer st = new StringTokenizer(points, " ");
//		
//		while (st.hasMoreTokens()) {
//			String[] pointStrings = st.nextToken().split(",");
//			trk.addTrackpoint(new Trackpoint(new Double(pointStrings[1]),
//					new Double(pointStrings[0]), new Double(pointStrings[2])));
//		}
//		return trk;
//	}
//
//	/**
//	 * Goolge cant show Altitude, but its in some GPS/GPX Files
//	 * Altitude will be ignored here so far
//	 * @param points
//	 * @return
//	 */
//	public static Track pointsAndAltitudeToTrack(String points) {
//		System.out.println("pointsAndAltitudeToTrack");
//		Track trk = new Track();
//		StringTokenizer st = new StringTokenizer(points, "\n");
//		while (st.hasMoreTokens()) {
//			String[] pointStrings = st.nextToken().split(",");
//			trk.addTrackpoint(new Trackpoint(new Double(pointStrings[1]),
//					new Double(pointStrings[0])));
//			System.out.println(new Double(pointStrings[1]).toString() + ", "
//					+ new Double(pointStrings[0]).toString());
//		}
//		return trk;
//	}

	private static int floor1e5(double coordinate) {
		return (int) Math.floor(coordinate * 1e5);
	}

	private static String encodeSignedNumber(int num) {
		int sgn_num = num << 1;
		if (num < 0) {
			sgn_num = ~(sgn_num);
		}
		return (encodeNumber(sgn_num));
	}

	private static String encodeNumber(int num) {

		StringBuffer encodeString = new StringBuffer();

		while (num >= 0x20) {
			int nextValue = (0x20 | (num & 0x1f)) + 63;
			encodeString.append((char) (nextValue));
			num >>= 5;
		}

		num += 63;
		encodeString.append((char) (num));

		return encodeString.toString();
	}

//	/**
//	 * Now we can use the previous function to march down the list of points and
//	 * encode the levels. Like createEncodings, we ignore points whose distance
//	 * (in dists) is undefined.
//	 */
//	private String encodeLevels(List<double[]> points, double[] dists,
//			double absMaxDist) {
//		int i;
//		StringBuffer encoded_levels = new StringBuffer();
//
//		if (this.forceEndpoints) {
//			encoded_levels.append(encodeNumber(this.numLevels - 1));
//		} else {
//			encoded_levels.append(encodeNumber(this.numLevels
//					- computeLevel(absMaxDist) - 1));
//		}
//		for (i = 1; i < points.size() - 1; i++) {
//			if (dists[i] != 0) {
//				encoded_levels.append(encodeNumber(this.numLevels
//						- computeLevel(dists[i]) - 1));
//			}
//		}
//		if (this.forceEndpoints) {
//			encoded_levels.append(encodeNumber(this.numLevels - 1));
//		} else {
//			encoded_levels.append(encodeNumber(this.numLevels
//					- computeLevel(absMaxDist) - 1));
//		}
////		System.out.println("encodedLevels: " + encoded_levels);
//		return encoded_levels.toString();
//	}

//	/**
//	 * This computes the appropriate zoom level of a point in terms of it's
//	 * distance from the relevant segment in the DP algorithm. Could be done in
//	 * terms of a logarithm, but this approach makes it a bit easier to ensure
//	 * that the level is not too large.
//	 */
//	private int computeLevel(double absMaxDist) {
//		int lev = 0;
//		if (absMaxDist > this.verySmall) {
//			lev = 0;
//			while (absMaxDist < this.zoomLevelBreaks[lev]) {
//				lev++;
//			}
//			return lev;
//		}
//		return lev;
//	}

	private String createEncodings(List<double[]> points, double[] dists) {
		StringBuffer encodedPoints = new StringBuffer();

		double maxlat = 0, minlat = 0, maxlon = 0, minlon = 0;

		int plat = 0;
		int plng = 0;
		
		for (int i = 0; i < points.size(); i++) {

			// determin bounds (max/min lat/lon)
			if (i == 0) {
				maxlat = minlat = points.get(i)[0];
				maxlon = minlon = points.get(i)[1];
			} else {
				if (points.get(i)[0] > maxlat) {
					maxlat = points.get(i)[0];
				} else if (points.get(i)[0] < minlat) {
					minlat = points.get(i)[0];
				} else if (points.get(i)[1] > maxlon) {
					maxlon = points.get(i)[1];
				} else if (points.get(i)[1] < minlon) {
					minlon = points.get(i)[1];
				}
			}

			if (dists[i] != 0 || i == 0 || i == points.size() - 1) {
				double[] point = points.get(i);

				int late5 = floor1e5(point[0]);
				int lnge5 = floor1e5(point[1]);

				int dlat = late5 - plat;
				int dlng = lnge5 - plng;

				plat = late5;
				plng = lnge5;

				encodedPoints.append(encodeSignedNumber(dlat));
				encodedPoints.append(encodeSignedNumber(dlng));

			}
		}

		HashMap<String, Double> bounds = new HashMap<String, Double>();
		bounds.put("maxlat", new Double(maxlat));
		bounds.put("minlat", new Double(minlat));
		bounds.put("maxlon", new Double(maxlon));
		bounds.put("minlon", new Double(minlon));

		this.setBounds(bounds);
		return encodedPoints.toString();
	}

	private void setBounds(HashMap<String, Double> bounds) {
		this.bounds = bounds;
	}

	public static String createEncodings(List<double[]> track, int step) {

//		HashMap<String, String> resultMap = new HashMap<String, String>();
		StringBuffer encodedPoints = new StringBuffer();
//		StringBuffer encodedLevels = new StringBuffer();

		List<double[]> trackpointList = track;

		int plat = 0;
		int plng = 0;
//		int counter = 0;

		int listSize = trackpointList.size();

		double[] trackpoint;

		for (int i = 0; i < listSize; i += step) {
//			counter++;
			trackpoint = trackpointList.get(i);

			int late5 = floor1e5(trackpoint[0]);
			int lnge5 = floor1e5(trackpoint[1]);

			int dlat = late5 - plat;
			int dlng = lnge5 - plng;

			plat = late5;
			plng = lnge5;

			encodedPoints.append(encodeSignedNumber(dlat)).append(
					encodeSignedNumber(dlng));
//			encodedLevels.append(encodeNumber(level));

		}

//		System.out.println("listSize: " + listSize + " step: " + step
//				+ " counter: " + counter);

//		resultMap.put("encodedPoints", encodedPoints.toString());
//		resultMap.put("encodedLevels", encodedLevels.toString());

		return encodedPoints.toString();
	}

	public HashMap<String, Double> getBounds() {
		return bounds;
	}
}