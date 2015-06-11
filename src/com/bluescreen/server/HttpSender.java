package com.bluescreen.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpSender {

	// HTTP GET request
    public static String sendEmail(String email, String title, String content) throws Exception {
 
        URL obj = new URL("http://52.10.103.172/send_email.php");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
        // optional default is GET
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        
        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        
        writer.write("email="+URLEncoder.encode(email, "UTF-8")
        		+"&title="+URLEncoder.encode(title, "UTF-8")
        		+"&content="+URLEncoder.encode(content, "UTF-8"));
        
        writer.flush();
        writer.close();
        os.close();
 
        //add request header
 
        int responseCode = con.getResponseCode();
 
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
    
    public static String sendEmailById(String id, String title, String content) throws Exception {
    	 
        URL obj = new URL("http://52.10.103.172/send_email_by_id.php");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
        // optional default is GET
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        
        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        
        writer.write("id="+URLEncoder.encode(id, "UTF-8")
        		+"&title="+URLEncoder.encode(title, "UTF-8")
        		+"&content="+URLEncoder.encode(content, "UTF-8"));
        
        writer.flush();
        writer.close();
        os.close();
 
        //add request header
 
        int responseCode = con.getResponseCode();
 
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
