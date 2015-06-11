package com.bluescreen.server.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.sql.Date;

import org.json.JSONObject;

import com.bluescreen.server.SysSession;

public class APISendPicture implements APIBase {
	
	private SysSession session;
	
	public APISendPicture(SysSession session) {
		this.session = session;
	}

	@Override
	public JSONObject process(JSONObject request) {
		

		try {
			InputStream input = session.getInputStream();
			
			byte[] len = new byte[4];
			byte[] data;
			
			System.out.println("APISendPicture");
			
			input.read(len);
			
			ByteBuffer wrapped = ByteBuffer.wrap(len);
			
			int num = wrapped.getInt();
			
			System.out.println("read : "+ num);
			
			data = new byte[num];
			 
			
			System.out.println("read start");
			input.read(data);
			
			System.out.println("read end");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
