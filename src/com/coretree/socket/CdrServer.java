package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteOrder;
import com.coretree.models.CDRData;
import com.coretree.models.CDRRequest;
import com.coretree.models.CDRResponse;
// import com.coretree.models.Options;

public class CdrServer
{
	private DatagramSocket serverSocket;
	// private Options _option;
	
	public CdrServer()
	{
		InitiateSocket();
	}
	
	public void InitiateSocket()
	{
		try
		{
			// InetSocketAddress address = new InetSocketAddress("localhost", 21003);
			serverSocket = new DatagramSocket(21003);
			
			byte[] receiveData = new byte[1024];
			byte[] sendData = null;

			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				System.err.println("has received.");
				
				byte[] rcvbytes = receivePacket.getData();
				CDRRequest rcvObj = new CDRRequest(rcvbytes, ByteOrder.BIG_ENDIAN);
				CDRData rcvData = new CDRData(rcvObj.data, ByteOrder.BIG_ENDIAN);
				
				// DB
				System.err.println(rcvData.office_name);
				//
				
				// Response
				CDRResponse res = new CDRResponse(ByteOrder.BIG_ENDIAN);
				res.cmd = 2;
				res.pCdr = rcvObj.pCdr;
				res.status = 0;
				sendData = res.toBytes();
				
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), receivePacket.getPort());
				serverSocket.send(sendPacket);
				System.err.println("has sent.");
			}
		}
		catch (SocketException e)
		{
			e.printStackTrace();
		}
		catch (IOException ie)
		{
			ie.printStackTrace();
		}
	}
	
	
}
