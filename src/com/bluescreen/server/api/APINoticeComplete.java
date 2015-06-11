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
		
		public double max_illumination;
		public double min_illumination;
		public double mean_illumination;
		
		public double max_temp;
		public double min_temp;
		public double mean_temp;
		
		public double max_humidity;
		public double min_humidity;
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
			
			sb.append("-조도").append("\n");
			sb.append("  최대 : ").append(String.format("%.1f", max_illumination)).append("\n");
			sb.append("  최소 : ").append(String.format("%.1f", min_illumination)).append("\n");
			sb.append("  평균 : ").append(String.format("%.1f", mean_illumination)).append("\n\n");
			
			sb.append("-온도").append("\n");
			sb.append("  최대 : ").append(String.format("%.1f", max_temp)).append("\n");
			sb.append("  최소 : ").append(String.format("%.1f", min_temp)).append("\n");
			sb.append("  평균 : ").append(String.format("%.1f", mean_temp)).append("\n\n");
			
			sb.append("-습도").append("\n");
			sb.append("  최대 : ").append(String.format("%.1f", max_humidity)).append("\n");
			sb.append("  최소 : ").append(String.format("%.1f", min_humidity)).append("\n");
			sb.append("  평균 : ").append(String.format("%.1f", mean_temp)).append("\n");
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
        
        sampleData.max_illumination = Double.MIN_VALUE;
        sampleData.max_temp = Double.MIN_VALUE;
        sampleData.max_humidity = Double.MIN_VALUE;
        
        sampleData.min_illumination = Double.MAX_VALUE;
        sampleData.min_temp = Double.MAX_VALUE;
        sampleData.min_humidity = Double.MAX_VALUE;
        
        for (SensingData sensingData : sampleData.samples)
        {
			total_illumination += sensingData.illumination;
			if(sampleData.max_illumination < sensingData.illumination)
				sampleData.max_illumination = sensingData.illumination;
			if(sampleData.min_illumination > sensingData.illumination)
				sampleData.min_illumination = sensingData.illumination;
			
			total_temp += sensingData.temp;
			if(sampleData.max_temp < sensingData.temp)
				sampleData.max_temp = sensingData.temp;
			if(sampleData.min_temp > sensingData.temp)
				sampleData.min_temp = sensingData.temp;
			
			total_humidity += sensingData.humidity;
			if(sampleData.max_humidity < sensingData.humidity)
				sampleData.max_humidity = sensingData.humidity;
			if(sampleData.min_humidity > sensingData.humidity)
				sampleData.min_humidity = sensingData.humidity;
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
