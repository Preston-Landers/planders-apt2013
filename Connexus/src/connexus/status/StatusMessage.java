package connexus.status;

public class StatusMessage {
	StatusMessageType type;
	String message;
	public StatusMessage(StatusMessageType type, String message) {
		this.type = type;
		this.message = message;
	}
	public StatusMessageType getType() {
		return type;
	}
	public void setType(StatusMessageType type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
