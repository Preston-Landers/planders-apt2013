package connexus.backend;

public class HelloBackend {

    public String message;

    public HelloBackend() {};

    public HelloBackend(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}