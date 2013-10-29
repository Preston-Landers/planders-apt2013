package connexus.servlet;

import connexus.LatLong;
import connexus.model.Media;
import connexus.model.Stream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class UpdateFakeGeodata extends HttpServlet {

    private static final long serialVersionUID = 4152585588958400844L;

	/**
	 * Generates fake geo-data for ALL images in system.
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		resp.setContentType("text/plain");
		PrintWriter pw = resp.getWriter();
        doGenerateFakeGeoData();

		pw.println("Fake geo-data generated.");
		pw.flush();
	}

    public static void doGenerateFakeGeoData() {
        System.out.println("Generating fake geo-data.");
        generateFakeGeoData();
    }

    /**
     * Generates fake (random) coordinates for ALL media in system
     * which does not already have a coordinate!
     * Use with caution...
     */
    public static void generateFakeGeoData() {
        List<Stream> streamList = Stream.getAllStreams(null);
        Double zero = new Double(0.0);
        for (Stream stream : streamList) {
            List<Media> mediaList = stream.getMedia(0, 0);
            for (Media media : mediaList) {
                if ((media.getLongitude() == null) ||
                        (media.getLatitude() == null) ||
                        (media.getLongitude().equals(zero)) ||
                        (media.getLatitude().equals(zero))) {
                    LatLong fakeCoords = LatLong.generateFakeCoordinates();
                    media.setLatitude(fakeCoords.getLatitude());
                    media.setLongitude(fakeCoords.getLongitude());
                    media.save(false);
                }
            }
        }
    }

}
