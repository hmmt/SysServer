package com.bluescreen.server.api;

import org.json.JSONObject;

import com.bluescreen.server.HttpSender;

public class APINoticeWashDown implements APIBase {

	@Override
	public JSONObject process(JSONObject request) {
		String id = request.getString("id");
		
		try {
			HttpSender.sendEmailById(id, "빨래 떨어짐", "빨래가 떨어졌습니다. \n 확인해주세요.");
			
			JSONObject output = new JSONObject();
			output.put("state", "ok");
			return output;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			JSONObject output = new JSONObject();
			output.put("state", "error");
			output.put("error", e.getMessage());
			return output;
		}
	}

}
