package connexus.android;

import android.location.Location;

public class LatLong {
    Double latitude;
    Double longitude;

    public LatLong(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }
    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public LatLong(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
