package connexus;

import java.util.Random;

/**
 * Contain Latitude and Longitude coords
 */
public class LatLong {
    double latitude;
    double longitude;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Generates a RANDOM coordinates anywhere on earth.
     * @return a randomized coordinate.
     */
    public static LatLong generateFakeCoordinates() {
        LatLong latLong = new LatLong();
        latLong.setLatitude(generateFakeLatitude());
        latLong.setLongitude(generateFakeLongitude());
        return latLong;
    }

    private static double generateFakeLatitude() {
        // double between reasonable latitudes
        return randomInRange(-70, 70.0);
    }

    private static double generateFakeLongitude() {
        // double between longitude range
        return randomInRange(-180.0, 180.0);
    }

    private static double randomInRange(double rangeMin, double rangeMax) {
        Random r = new Random();
        double randomValue = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        return randomValue;
    }
}
