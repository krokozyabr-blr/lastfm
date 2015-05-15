package com.krokozyabr.lastfm.networking;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorInfo {
	private static final String ERROR = "error";
	private static final String MESSAGE = "message";
	private static final String EMPTY = "";
	private String errorMessage;

	public ErrorInfo() {
		errorMessage = EMPTY;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void parse (String response) {
		JSONObject responseJO = null;
		try {
			responseJO =  new JSONObject(response);
		} catch (JSONException e) {
			errorMessage = response;
		}
		if(responseJO != null)
			if(responseJO.has(ERROR))
				errorMessage = responseJO.optString(ERROR);
			else
				errorMessage = responseJO.optString(MESSAGE);
	}
}
