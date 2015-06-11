package com.bluescreen.server.api;

import org.json.JSONObject;

public interface APIBase {
	JSONObject process(JSONObject request);
}
