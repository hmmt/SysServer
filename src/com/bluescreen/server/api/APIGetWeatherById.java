package com.bluescreen.server.api;

import java.util.ArrayList;

import org.json.JSONObject;

import com.bluescreen.weather.Weather;
import com.bluescreen.weather.WeatherManager;

public class APIGetWeatherById implements APIBase {
	@Override
	public JSONObject process(JSONObject request) {
		
		String areaId = request.getString("areaId");
		
		WeatherManager wt = new WeatherManager();
		
		ArrayList<Weather> weatherList;
		if("1".equals(areaId))
		{
			weatherList = wt.getWeather("61", "120");
		}
		else if("2".equals(areaId))
		{
			weatherList = wt.getWeather("58", "125");
		}
		/*else if("3".equals(areaId))
		{
			weatherList = wt.getWeather("53", "124");
		}*/
		else if("4".equals(areaId))
		{
			weatherList = wt.getWeather("63", "124");
		}
		else if("5".equals(areaId))
		{
			weatherList = wt.getWeather("66", "118");
		}
		else if("6".equals(areaId))
		{
			weatherList = wt.getWeather("58", "114");
		}
		else if("3".equals(areaId)) // 제주
		{
			weatherList = wt.getWeather("48", "36");
		}
		else
		{
			System.out.println(areaId);
			return null;
		}


		JSONObject output = new JSONObject();
		
		for(Weather weather : weatherList)
		{
			if(weather.seq == 0)
			{
				JSONObject weatherJson = new JSONObject();
				weatherJson.put("pop", weather.pop);
				weatherJson.put("temp", weather.temp);
				weatherJson.put("reh", weather.reh);
				//weatherJson.put("wf", weather.wf);
				
				if(weather.wf.indexOf("Cloudy") >= 0) {
					weatherJson.put("weather", "4");
				} else if(weather.wf.indexOf("Clear") >= 0) {
					weatherJson.put("weather", "5");
				} else if(weather.wf.indexOf("Rain") >= 0) {
					weatherJson.put("weather", "6");
				} else if(weather.wf.indexOf("Snow") >= 0) {
					weatherJson.put("weather", "7");
				} else {
					weatherJson.put("weather", "5"); // default : clear
				}
				return weatherJson;
			}
		}
		return output;
	}

}
