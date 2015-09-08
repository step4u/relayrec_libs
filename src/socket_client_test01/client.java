package socket_client_test01;

import java.io.*;
import java.net.*;

import com.coretree.io.WaveFileWriter;
import com.coretree.media.WaveFormat;
import com.coretree.models.ReqUserInfo2;

public class client {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("line 1");
		
//		ReqUserInfo obj = new ReqUserInfo();
//		obj.cmd = 100;
//		obj.status = 0;
//		obj.userip = "127.0.0.1";
//		
//		byte[] bytes = object2Bytes((Object)obj);
//		
//		System.out.println("line 2");
//		System.out.println(bytes.length);
//		
//		ReqUserInfo obj2 = (ReqUserInfo)bytes2Object(bytes);
//		
//		System.out.println("line 3");
//		System.out.println(obj2.cmd + "//" + obj2.status + "//" + obj2.userip);
		

/*		
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("14.63.172.64");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		ReqUserInfo obj = new ReqUserInfo();
		obj.cmd = 100;
		obj.status = 0;
		//obj.userip = "127.0.0.1";

		sendData = object2Bytes((Object)obj);
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2015);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		
		ReqUserInfo obj2 = (ReqUserInfo)bytes2Object(receivePacket.getData());
		
		System.out.println("FROM SERVER: " + obj2.cmd + "//" + obj2.status + "//" + obj2.userip);
		
		clientSocket.close();
*/
		
/*
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress IPAddress = InetAddress.getByName("14.63.172.64");
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];

		ReqUserInfo2 obj = new ReqUserInfo2();
		obj.cmd = 100;
		obj.status = 0;
		obj.seqnum = 101;
		obj.userip = obj.string2Char("127.0.0.1", obj.userip.length);
		obj.userid = obj.string2Char("tester01", obj.userid.length);

		sendData = obj.toBytes();
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2015);
		clientSocket.send(sendPacket);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		
		ReqUserInfo2 obj2 = new ReqUserInfo2();
		obj2.toObject(receivePacket.getData());
		
		System.out.println("FROM SERVER: " + obj2.cmd + "//" + obj2.status + "//" + new String(obj2.userip) + "//" + new String(obj2.userid));
		
		clientSocket.close();
*/
		
//		//BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
//		DatagramSocket clientSocket = new DatagramSocket();
//		//InetAddress IPAddress = InetAddress.getByName("localhost");
//		InetAddress IPAddress = InetAddress.getByName("14.63.172.64");
//		byte[] sendData = new byte[1024];
//		byte[] receiveData = new byte[1024];
//		//String sentence = inFromUser.readLine();
//		String sentence = "22222";
//		sendData = sentence.getBytes();
//		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 2015);
//		clientSocket.send(sendPacket);
//		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//		clientSocket.receive(receivePacket);
//		String modifiedSentence = new String(receivePacket.getData());
//		System.out.println("FROM SERVER:" + modifiedSentence);
//		clientSocket.close();
		
		
//		StringBuilder sb = new StringBuilder(16);
//		System.out.println(sb.capacity());
//		System.out.println(sb.length());
//		String str = sb.toString();
//		System.out.println(str.length());
//		System.out.println(str.length());
		
//		// bytebuffer ???
//		ByteBuffer b = ByteBuffer.allocate(4);
//		//b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
//		b.putInt(123);
//
//		byte[] result = b.array();
//		
//		System.out.println(result.toString());
//		
//		char[] chararray = { '1', '2', '3', '\0', '4', '\0' };
//		
//		System.out.println(chararray);
//		System.out.println(chararray.toString());
//		
//		byte[] res = chararray.toString().getBytes();
//		String tt = new String(chararray);
//		byte[] res0 = tt.getBytes();
//		byte[] res1 = "123".getBytes();
//		
//		System.out.println(res.toString());
		
		
		
		
//		// Å×½ºÆ®  waveformat
//		WaveFormat waveformat = WaveFormat.CreateALawFormat(8000,  1);
//		WaveFileWriter writer = new WaveFileWriter("123456789.wav", waveformat);
//		
//		
//		System.out.println(waveformat.toString());
	}
	
	public static byte[] object2Bytes(Object obj)
	{
		ByteArrayOutputStream b = null;
		ObjectOutputStream o = null;
		byte[] outbytes = null;
		
		try {
			b = new ByteArrayOutputStream();
			o = new ObjectOutputStream(b);
	        o.writeObject(obj);
			o.flush();
			outbytes = b.toByteArray();
		} catch (IOException ex) {
			System.out.println("object2Bytes IOException1 : " + ex);
		} finally {
			try {
				if (o != null) {
					o.close();
				}
			} catch (IOException ex) {
				System.out.println("object2Bytes IOException2 : " + ex);
			}
			try {
				b.close();
			} catch (IOException ex) {
				System.out.println("object2Bytes IOException3 : " + ex);
			}
		}

//		byte[] outbytes = null;
//		try
//		{
//			ByteArrayOutputStream b = new ByteArrayOutputStream();
//			ObjectOutputStream o = new ObjectOutputStream(b);
//			o.writeObject(obj);
//			outbytes = b.toByteArray();
//		}
//		catch (IOException ex)
//		{
//			ex.printStackTrace();
//		}
		
		return outbytes;
	}
	
	public static Object bytes2Object(byte[] bytes)
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		Object out = null;
		
		try {
		  in = new ObjectInputStream(bis);
		  out = in.readObject();
		} catch (ClassNotFoundException e) {
			
		} catch (IOException e) {
			
		} finally {
		  try {
		    bis.close();
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		  try {
		    if (in != null) {
		      in.close();
		    }
		  } catch (IOException ex) {
		    // ignore close exception
		  }
		}
		
		return out;
	}
}
