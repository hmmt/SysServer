package com.bluescreen.server.api;

import org.json.JSONObject;

public class APIHelloTest implements APIBase {

	@Override
	public JSONObject process(JSONObject request) {
		
		String id = request.getString("id");
		
		JSONObject resp = new JSONObject();
		resp.put("msg", "Hello, "+id);
		
		return resp;
	}

}
