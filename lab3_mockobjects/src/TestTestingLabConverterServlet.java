import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import junit.framework.TestCase;

public class TestTestingLabConverterServlet extends TestCase {

    public void test_bad_parameter() throws Exception {
        TestingLabConverterServlet s = new TestingLabConverterServlet();
        MockHttpServletRequest request =
                new MockHttpServletRequest();
        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setupAddParameter("farenheitTemperature", "boo!");
        response.setExpectedContentType("text/html");
        s.doGet(request, response);
        response.verify();
        assertEquals("<html><head><title>Bad Temperature</title></head><body><h2>Need to enter a valid temperature!Got a NumberFormatException on boo!</h2></body></html>",
                response.getOutputStreamContents().trim());
    }

    public void test_no_parameter() throws Exception {
        TestingLabConverterServlet s = new TestingLabConverterServlet();
        MockHttpServletRequest request =
                new MockHttpServletRequest();
        MockHttpServletResponse response =
                new MockHttpServletResponse();

        response.setExpectedContentType("text/html");
        s.doGet(request, response);
        response.verify();
        assertEquals("<html><head><title>No Temperature</title></head><body><h2>Need to enter a temperature!</h2></body></html>",
                response.getOutputStreamContents().trim());
    }

    public void test_boil() throws Exception {
        TestingLabConverterServlet s = new TestingLabConverterServlet();
        MockHttpServletRequest request =
                new MockHttpServletRequest();
        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setupAddParameter("farenheitTemperature", "212");
        response.setExpectedContentType("text/html");
        s.doGet(request, response);
        response.verify();

        String resp = response.getOutputStreamContents().trim();
        String expected = "212 Farenheit = 100 Celsius";
        assertTrue(
                "Response did not contain: <" + expected + "> instead had: " + resp,
                resp.contains(expected));
    }

    public void test_freeze() throws Exception {
        TestingLabConverterServlet s = new TestingLabConverterServlet();
        MockHttpServletRequest request =
                new MockHttpServletRequest();
        MockHttpServletResponse response =
                new MockHttpServletResponse();

        request.setupAddParameter("farenheitTemperature", "32");
        response.setExpectedContentType("text/html");
        s.doGet(request, response);
        response.verify();

        String resp = response.getOutputStreamContents().trim();
        String expected = "32 Farenheit = 0 Celsius";
        assertTrue(
                "Response did not contain: <" + expected + "> instead had: " + resp,
                resp.contains(expected));
    }

}

