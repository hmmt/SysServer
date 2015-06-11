package com.bluescreen.server.api;

import java.util.HashMap;

import org.json.JSONObject;

import com.bluescreen.server.SysSession;

public class RequestDispatcher {
	
	private HashMap<String, APIBase> apiMap;
	
	
	public RequestDispatcher(SysSession session)
	{
		apiMap = new HashMap<String, APIBase>();
		apiMap.put("HelloTest", new APIHelloTest());
		apiMap.put("SaveSensing", new APISaveSensing());
		apiMap.put("GetSensing", new APIGetSensing());
		apiMap.put("GetWeather", new APIGetWeather());
		apiMap.put("GetWeatherSuwon", new APIGetWeatherSuwon());
		apiMap.put("GetWeatherById", new APIGetWeatherById());
		
		apiMap.put("NoticeWashDown", new APINoticeWashDown());
		apiMap.put("NoticeComplete", new APINoticeComplete());
		apiMap.put("SendPicture", new APISendPicture(session));
	}
	
	public JSONObject dispatchMessage(JSONObject requestObj)
	{
		String requestType = requestObj.getString("request");
		
		APIBase api = apiMap.get(requestType);
		
		if(api == null)
		{
			return new JSONObject("{\"state\":\"error\"}");
		}
		else
		{
			return api.process(requestObj);
		}
	}
}
