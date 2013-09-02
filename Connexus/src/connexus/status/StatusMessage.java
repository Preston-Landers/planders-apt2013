package connexus.status;

import java.io.Serializable;

/**
 * Encapsulates an "action status" message to be displayed on the top of the UI
 * at the next page view. There are currently two types defined: STATUS and
 * ERROR messages.
 * 
 * 
 */
public class StatusMessage implements Serializable {
	private static final long serialVersionUID = -73465466713153918L;
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
