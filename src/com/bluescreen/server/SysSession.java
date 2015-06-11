package com.bluescreen.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import com.bluescreen.server.api.RequestDispatcher;

public class SysSession extends Thread {

	private RequestDispatcher dispatcher;
	private SysServer server;
	private Socket sock;
	
	private InputStream is;
	private BufferedReader reader;
    private PrintWriter writer;
	
	private boolean running;
	
	public SysSession(SysServer server, Socket sock)
	{
		this.server = server;
		this.sock = sock;
		running = false;
		
		dispatcher = new RequestDispatcher(this);
	}
	
	public void close()
	{
		
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	public BufferedReader getBufferedReader()
	{
		return reader;
	}
	
	public InputStream getInputStream() throws IOException
	{
		return sock.getInputStream();
	}
/*	
	public byte[] read(int size) throws IOException, InterruptedException
	{
		byte[] data = null;
		while(buf.getSize() < size) {
			if(data == null) {
				data = new byte[1000];
			}
			is.read(data);
		}
		
		byte[] output = new byte[size];
		buf.read(output);
		
		return output;
	}
	
	public byte[] readPacket() throws IOException, InterruptedException
	{
		byte[] packetSize = read(4);
		byte[] data = read(ByteBuffer.wrap(packetSize).order(ByteOrder.BIG_ENDIAN).getInt());
		
		return data;
	}
	
	public String readPacketString()
	{
		byte[] data;
		
		try {
			data = readPacket();
			
			return new String(data, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	*/
	private JSONObject readMsg(BufferedReader reader) throws IOException
	{
		String str = reader.readLine();
		
		if(str == null)
		{
			return null;
		}
    	
		try
		{
			return new JSONObject(str);
		}
		catch(JSONException ex)
		{
			ex.printStackTrace();
		}
		return null;
	}
	@Override
	public void run()
	{
		running = true;
		System.out.println("session start!");
		try
        {
			InputStreamReader isr = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(isr);
            
            writer = new PrintWriter(sock.getOutputStream(), true);
            
            while(true)
            {
	            JSONObject jsonObj = readMsg(reader);
	            if(jsonObj == null)
	            {
	            	break;
	            }
	            System.out.println(jsonObj.toString());
	            
	            JSONObject resp = dispatcher.dispatchMessage(jsonObj);
	            
	            
	            StringBuilder sb = new StringBuilder();
	            int count = 0;
	            for(String key : resp.keySet())
	            {
	            	if(count++ > 0)
	            		sb.append("$");
	            	sb.append(key);
	            	sb.append("=");
	            	sb.append(resp.get(key).toString());
	            }
	            
	            System.out.println(sb.toString());
	            
	            writer.println(sb.toString());
            }
        }
		catch(IOException ex)
        {
			ex.printStackTrace();
        }
        finally
        {
            try
            {
                sock.close();
            }
            catch (IOException ex)
            {
            	ex.printStackTrace();
            }
            running = false;
        }
	}

}
