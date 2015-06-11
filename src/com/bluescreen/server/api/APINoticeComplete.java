package com.bluescreen.server.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONObject;

import com.bluescreen.server.HttpSender;
import com.bluescreen.server.SensingData;
import com.bluescreen.server.SensorDataManager;

public class APINoticeComplete implements APIBase  {
	
	class SampleData
	{
		public ArrayList<SensingData> samples;
		public double mean_illumination;
		public double mean_temp;
		public double mean_humidity;
		
		public String convertToString()
		{
			if(samples == null || samples.size() == 0)
			{
				return "no sample data...";
			}
			StringBuilder sb = new StringBuilder();
			
			sb.append("=====================================").append("\n");
			sb.append("       건조 환경 정보").append("\n");
			sb.append("=====================================").append("\n");
			sb.append("조도 : "+Math.floor(mean_illumination*10)/10).append("\n");
			sb.append("온도 : "+Math.floor(mean_temp*10)/10).append("\n");
			sb.append("습도 : "+Math.floor(mean_humidity*10)/10).append("\n");
			sb.append("=====================================").append("\n");
			
			return sb.toString();
		}
	}
	
	private SampleData getStatistics(String id, int duration)
	{
		SampleData sampleData = new SampleData();
		
		try {
			sampleData.samples = SensorDataManager.getRecentSensingDataList(id, duration);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			return sampleData;
		}

        double total_illumination = 0, total_temp = 0, total_humidity = 0;
        int count = sampleData.samples.size();
        
        if(count == 0)
        {
        	return sampleData;
        }
        
        for (SensingData sensingData : sampleData.samples)
        {
			total_illumination += sensingData.illumination;
			total_temp += sensingData.temp;
			total_humidity += sensingData.humidity;
		}
        
        sampleData.mean_illumination = total_illumination / count;
        sampleData.mean_temp = total_temp / count;
        sampleData.mean_humidity = total_humidity / count;
        
        return sampleData;
	}

	@Override
	public JSONObject process(JSONObject request) {
		String id = request.getString("id");
		
		try {
			SampleData sampleData = getStatistics(id, 180);
			HttpSender.sendEmailById(id, "건조 알림","건조 알림입니다.\n"
					+ sampleData.convertToString());
			
			JSONObject output = new JSONObject();
			output.put("state", "ok");
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			
			JSONObject output = new JSONObject();
			output.put("state", "error");
			output.put("error", e.getMessage());
			return output;
		}
	}

}
