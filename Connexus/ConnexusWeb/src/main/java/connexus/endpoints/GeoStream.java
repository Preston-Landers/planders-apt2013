package connexus.endpoints;

import java.util.Date;

/**
 * The JSON return value of a geo-view query.
 */
public class GeoStream {
    Date beginDate;
    Date endDate;
    Stream stream;

    public GeoStream() {
    }

    public GeoStream(Stream stream) {
        this.stream = stream;
    }

    public static GeoStream convertStreamToGeoAPI(connexus.model.Stream modelStream) {
        return new GeoStream(Stream.convertStreamToAPI(modelStream));
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(Stream stream) {
        this.stream = stream;
    }
}
