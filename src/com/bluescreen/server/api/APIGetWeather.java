package com.bluescreen.server.api;

import java.util.ArrayList;

import org.json.JSONObject;

import com.bluescreen.weather.Weather;
import com.bluescreen.weather.WeatherManager;

public class APIGetWeather  implements APIBase  {

	@Override
	public JSONObject process(JSONObject request) {
		
		String x = request.getString("x");
		String y = request.getString("y");
		
		WeatherManager wt = new WeatherManager();
		
		ArrayList<Weather> weatherList = wt.getWeather(x, y);


		JSONObject output = new JSONObject();
		
		for(Weather weather : weatherList)
		{
			if(weather.day == 0)
			{
				JSONObject weatherJson = new JSONObject();
				weatherJson.put("pop", weather.pop);
				weatherJson.put("reh", weather.reh);
				weatherJson.put("temp", weather.temp);
				weatherJson.put("wf", weather.wf);
				output.put(String.valueOf(weather.hour), weatherJson);
			}
		}
		return output;
	}

}
