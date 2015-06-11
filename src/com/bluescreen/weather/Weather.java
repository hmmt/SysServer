package com.bluescreen.weather;

public class Weather
{
	public int seq;
	public int hour;
	public double temp;
	public String wf;
	public double pop;
	public double reh;
	public int day;
	
	public String toString()
	{
		return "day : "+day+", hour : "+hour+",temp :"+temp+", wf :"+wf+", pop :"+pop+", reh:"+reh;
	}
}