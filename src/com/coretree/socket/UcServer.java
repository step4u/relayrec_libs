package com.coretree.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteOrder;
import com.coretree.models.GroupWareData;
import com.coretree.util.Const4pbx;
// import com.coretree.models.Options;

public class UcServer implements Runnable
{
	private ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
	
	private DatagramSocket serverSocket;
	private InetSocketAddress remoteep;
	private Thread[] threads;
	// private Options _option;
	
	public UcServer()
	{
		this("127.0.0.1", 31001, 1, ByteOrder.BIG_ENDIAN);
	}
	
	public UcServer(String addr, int port, int threadcount, ByteOrder order)
	{
		try
		{
			byteorder = order;
			remoteep = new InetSocketAddress(addr, port);
			
			// InetSocketAddress address = new InetSocketAddress("localhost", 21003);
			serverSocket = new DatagramSocket(31001);
			serverSocket.connect(remoteep);
			
			threads = new Thread[threadcount];			
		}
		catch (SocketException e)
		{
			
		}
	}
	
	public void start() {
        for (int i = 0; i < threads.length; i++) {
        	threads[i] = new Thread(this);
        	threads[i].start();
        }
    }
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		this.InitiateSocket();
	}
	
	private void InitiateSocket()
	{
		try
		{
			byte[] receiveData = new byte[1024];
			// byte[] sendData = null;

			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				serverSocket.receive(receivePacket);
				
				System.err.println("UC Data has been received.");
				
				// byte[] rcvbytes = receivePacket.getData();
				this.TreatDataHasRecieved(serverSocket, receivePacket.getData());
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
	
	public void Send(GroupWareData data) throws IOException
	{
		byte[] sendData = data.toBytes();
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
		serverSocket.send(sendPacket);
		System.err.println("has sent.");
	}
	
	public void Send(DatagramSocket sock, DatagramPacket packet, GroupWareData data) throws IOException
	{
		byte[] sendData = data.toBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);
		sock.send(sendPacket);
		System.err.println("has sent.");
	}
	
	private void TreatDataHasRecieved(DatagramSocket sock, byte[] bytes)
	{
		//GroupWareData rcvData = new GroupWareData(bytes, byteorder);
		GroupWareData rcvData = new GroupWareData(bytes, byteorder);
		

		
		// DB
		System.err.println(String.format("cmd : %d, status=%d", rcvData.cmd, rcvData.status));
		//
	}
	
	public void Regist()
	{
		GroupWareData data = this.GetData(Const4pbx.UC_REGISTER_REQ, "", "");
		
		try
		{
			this.Send(data);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void MakeCall(String ext, String peer)
	{
		GroupWareData data = this.GetData(Const4pbx.UC_MAKE_CALL_REQ, ext, peer);
		
		try
		{
			this.Send(data);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GroupWareData GetData(byte cmd, String ext, String peer)
	{
		GroupWareData data = new GroupWareData(byteorder);
		data.cmd = cmd;
		
		switch (cmd)
		{
			case Const4pbx.UC_REGISTER_REQ:
			case Const4pbx.UC_UNREGISTER_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				break;
			case Const4pbx.UC_BUSY_EXT_REQ:
				
				break;
			case Const4pbx.UC_INFO_SRV_REQ:
				data.extension = "2001";
				break;
			case Const4pbx.UC_MAKE_CALL_REQ:
				data.type = Const4pbx.UC_TYPE_GROUPWARE;
				data.extension = ext;
				data.callee = peer;
				break;
		}
		
		return data;
	}
}
