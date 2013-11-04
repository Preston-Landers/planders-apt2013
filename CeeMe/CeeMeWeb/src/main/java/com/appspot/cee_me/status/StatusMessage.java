package com.appspot.cee_me.status;

import java.io.Serializable;

/**
 * Encapsulates an user visible status message to be displayed on the top of the UI
 * at the next page view. There are currently four types defined: 
 *  SUCCESS, ERROR, INFO, WARNING
 * 
 */
public class StatusMessage implements Serializable {
	private static final long serialVersionUID = -73465466713153918L;
	StatusMessageType type;
	String message;
	String iconClass;

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
	
	public String getIconClass() {
		String rv = "glyphicon glyphicon-ok-sign";
		switch (type) {
		case SUCCESS:
			rv = "glyphicon glyphicon-ok-sign";
			break;
		case ERROR:
			rv = "glyphicon glyphicon-remove";
			break;
		case INFO:
			rv = "glyphicon glyphicon-info-sign";
			break;
		case WARNING:
			rv = "glyphicon glyphicon-warning-sign";
			break;
		}
		return rv;
	}
	
	public String getCSSClass() {
		String rv = "alert-info";
		switch (type) {
		case SUCCESS:
			rv = "alert-success";
			break;
		case ERROR:
			rv = "alert-danger";
			break;
		case INFO:
			rv = "alert-info";
			break;
		case WARNING:
			rv = "alert-warning";
			break;
		}
		return rv;
	}
}
