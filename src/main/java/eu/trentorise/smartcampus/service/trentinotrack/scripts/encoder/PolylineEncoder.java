package eu.trentorise.smartcampus.service.trentinotrack.scripts.encoder;

import java.util.List;

public class PolylineEncoder {


	private static StringBuffer encodeSignedNumber(int num) {
        int sgn_num = num << 1;
        if (num < 0) {
            sgn_num = ~(sgn_num);
        }
        return(encodeNumber(sgn_num));
    }

    private static StringBuffer encodeNumber(int num) {
        StringBuffer encodeString = new StringBuffer();
        while (num >= 0x20) {
                int nextValue = (0x20 | (num & 0x1f)) + 63;
                encodeString.append((char)(nextValue));
            num >>= 5;
        }
        num += 63;
        encodeString.append((char)(num));
        return encodeString;
    }
    
    /**
     * Encode a polyline with Google polyline encoding method
     * @param polyline the polyline
     * @param precision 1 for a 6 digits encoding, 10 for a 5 digits encoding. 
     * @return the encoded polyline, as a String
     */
    public static String encode(List<double[]> polyline) {
                StringBuffer encodedPoints = new StringBuffer();
                int prev_lat = 0, prev_lng = 0;
                for (double[] trackpoint:polyline) {
                        int lat = (int)(trackpoint[0]*1E5);
                        int lng = (int)(trackpoint[1]*1E5);
                        encodedPoints.append(encodeSignedNumber(lat - prev_lat));
                        encodedPoints.append(encodeSignedNumber(lng - prev_lng));                       
                        prev_lat = lat;
                        prev_lng = lng;
                }
                return encodedPoints.toString();
        }

}