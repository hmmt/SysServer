package com.bluescreen.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SysServer {
	private final int port;
	
	public SysServer(int port) {
		this.port = port;
	}
	
	public void run()
	{
		ServerSocket servSock = null;
		try
		{	
			servSock = new ServerSocket(port);
			
			
			while(true)
			{
				Socket sock;
				sock = servSock.accept();
				
				SysSession session = new SysSession(this, sock);
				session.start();
				
				if(session.isAlive())
				{
					try
					{
						Thread.sleep(1);
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch (IOException ex)
		{
            ex.printStackTrace();
        }
		finally
		{
			if(servSock != null)
			{
				try {
					servSock.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
