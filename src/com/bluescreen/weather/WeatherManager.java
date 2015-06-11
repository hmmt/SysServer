package com.bluescreen.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

class DataPair
{
	public String code;
	public String value;
}

class Top
{
	public String code;
	public DataPair[] mdlCodes;
}

class Mdl
{
	public String code;
	public Leaf[] leafs;
}

class Leaf
{
	public String code;
	public String value;
	public String x;
	public String y;
}

public class WeatherManager {

    private final String USER_AGENT = "Mozilla/5.0";
    private static final String CITY_LIST_URL = "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
    
    private HashMap<String, Top> topTable;
    private HashMap<String, Mdl> mdlTable;
    private HashMap<String, Leaf> leafTable;
    
    public WeatherManager()
    {
    	topTable = new HashMap<String, Top>();
    	mdlTable = new HashMap<String, Mdl>();
    	leafTable = new HashMap<String, Leaf>();
    }
    
    public Top getTopData(String topCode)
    {
    	Top top = topTable.get(topCode);
    	if(top == null)
    	{
    		String resp;
			try {
				resp = sendGet(makeMdlUrl(topCode));
				
				JSONArray arr = new JSONArray(resp);
	    		top = new Top();
	    		
	    		top.code = topCode;
	    		
	    		ArrayList<DataPair> list = new ArrayList<DataPair>();
	    		
	    		for(int i=0; i<arr.length(); i++)
	    		{
	    			JSONObject mdlJson = arr.getJSONObject(i);
	    			DataPair data = new DataPair();
	    			data.code = mdlJson.getString("code");
	    			data.value = mdlJson.getString("value");
	    			list.add(data);
	    		}
	    		top.mdlCodes = list.toArray(new DataPair[list.size()]);
	    		
	    		topTable.put(topCode, top);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	return top;
    }
    
    public Mdl getMdlData(String mdlCode)
    {
    	Mdl mdl = mdlTable.get(mdlCode);
    	if(mdl == null)
    	{
    		String resp;
			try {
				resp = sendGet(makeLeafUrl(mdlCode));
				
				JSONArray arr = new JSONArray(resp);
				mdl = new Mdl();
	    		
				mdl.code = mdlCode;
	    		
	    		ArrayList<Leaf> list = new ArrayList<Leaf>();
	    		
	    		for(int i=0; i<arr.length(); i++)
	    		{
	    			JSONObject leafJson = arr.getJSONObject(i);
	    			Leaf data = new Leaf();
	    			data.code = leafJson.getString("code");
	    			data.value = leafJson.getString("value");
	    			data.x = leafJson.getString("x");
	    			data.y = leafJson.getString("y");
	    			list.add(data);
	    		}
	    		mdl.leafs = list.toArray(new Leaf[list.size()]);
	    		
	    		mdlTable.put(mdlCode, mdl);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	return mdl;
    }
    
    public ArrayList<Weather> getWeather(String x, String y)
    {
    	ArrayList<Weather> output = new ArrayList<Weather>();
    	try {
			String result = sendGet(makeQueryUrl(x, y));
			
			InputSource is = new InputSource(new StringReader(result));
        	Document document = DocumentBuilderFactory.newInstance()
        			.newDocumentBuilder()
        			.parse(is);
        	
        	XPath xpath = XPathFactory.newInstance().newXPath();
        	
        	NodeList nodeList = (NodeList)xpath.evaluate("//body/data", document, XPathConstants.NODESET);
        	for (int i=0; i<nodeList.getLength(); i++) {
				Node dataNode = nodeList.item(i);
				Weather weather = new Weather();
				
				NodeList children = dataNode.getChildNodes();
				for(int j=0; j<children.getLength(); j++)
				{
					Node node = children.item(j);
					if(node.getNodeName().equals("temp"))
						weather.temp = Double.parseDouble(node.getTextContent());
					else if(node.getNodeName().equals("pop"))
						weather.pop = Double.parseDouble(node.getTextContent());
					else if(node.getNodeName().equals("wfEn"))
						weather.wf = node.getTextContent();
					else if(node.getNodeName().equals("reh"))
						weather.reh = Double.parseDouble(node.getTextContent());
					else if(node.getNodeName().equals("hour"))
						weather.hour = Integer.parseInt(node.getTextContent());
					else if(node.getNodeName().equals("day"))
						weather.day = Integer.parseInt(node.getTextContent());
				}
				output.add(weather);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return output;
    }
     
    public static void main(String[] args) throws Exception {
 
        WeatherManager http = new WeatherManager();
        
        Top top = http.getTopData("11");
        
        for(DataPair data : top.mdlCodes)
        {
        	Mdl mdl = http.getMdlData(data.code);
        	
        	String output = http.sendGet(http.makeQueryUrl(mdl.leafs[0].x, mdl.leafs[0].y));
        	
        	
        	InputSource is = new InputSource(new StringReader(output));
        	Document document = DocumentBuilderFactory.newInstance()
        			.newDocumentBuilder()
        			.parse(is);
        	
        	XPath xpath = XPathFactory.newInstance().newXPath();
        	
        	NodeList nodeList = (NodeList)xpath.evaluate("//body/data", document, XPathConstants.NODESET);
        	for (int i=0; i<nodeList.getLength(); i++) {
				Node dataNode = nodeList.item(i);
				Weather weather = new Weather();
				
				weather.seq = Integer.parseInt(dataNode.getAttributes().getNamedItem("seq").getTextContent());
				
				NodeList children = dataNode.getChildNodes();
				for(int j=0; j<children.getLength(); j++)
				{
					Node node = children.item(j);
					if(node.getNodeName().equals("temp"))
						weather.temp = Double.parseDouble(node.getTextContent());
					else if(node.getNodeName().equals("pop"))
						weather.pop = Double.parseDouble(node.getTextContent());
					else if(node.getNodeName().equals("wfEn"))
						weather.wf = node.getTextContent();
					else if(node.getNodeName().equals("reh"))
						weather.reh = Double.parseDouble(node.getTextContent());
					else if(node.getNodeName().equals("hour"))
						weather.hour = Integer.parseInt(node.getTextContent());
					
					//hour
				}
				
				System.out.println(mdl.leafs[0].value + " = "+weather.toString());
			}
        }
    }
    
    public String makeTopUrl()
    {
    	return "http://www.kma.go.kr/DFSROOT/POINT/DATA/top.json.txt";
    }
    
    public String makeMdlUrl(String topCode)
    {
    	return "http://www.kma.go.kr/DFSROOT/POINT/DATA/mdl."+topCode+".json.txt";
    }
    
    public String makeLeafUrl(String mdlCode)
    {
    	return "http://www.kma.go.kr/DFSROOT/POINT/DATA/leaf."+mdlCode+".json.txt";
    }
    
    public String makeQueryUrl(String x, String y)
    {
    	return "http://www.kma.go.kr/wid/queryDFS.jsp?gridx="+x+"&gridy="+y;
    }
 
    // HTTP GET request
    private String sendGet(String url) throws Exception {
 
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
        // optional default is GET
        con.setRequestMethod("GET");
 
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
 
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
 
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"));
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
 
        //print result
        //System.out.println(new String(response.toString().getBytes("utf-8"),"euc-kr"));
        
        String output = response.toString();
        System.out.println(output);

        return output;
    }
    
}