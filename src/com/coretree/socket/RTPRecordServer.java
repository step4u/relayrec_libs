package com.coretree.socket;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.coretree.models.Options;
import com.coretree.models.RTPInfo;
import com.coretree.models.RTPRecordInfo;
import com.coretree.models.ReceivedRTP;
import com.coretree.wave.WaveFormat;
import com.coretree.wave.WaveFormatEncoding;

public class RTPRecordServer extends Thread
{
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
	
	private DatagramSocket serverSocket;
	private List<RTPRecordInfo> recordIngList;
	private Options _option;

	public RTPRecordServer()
	{
		recordIngList = new ArrayList<RTPRecordInfo>();
		_option = new Options();

		// InitiateSocket();
	}

	public void InitiateSocket()
	{
		try
		{
			serverSocket = new DatagramSocket(21010);
			// serverSocket = new DatagramSocket(21010,
			// Inet4Address.getLocalHost());
			byte[] receiveData = new byte[416];
			// byte[] sendData = new byte[1024];

			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				// DatagramPacket receivePacket = new
				// DatagramPacket(receiveData, 0, receiveData.length);
				serverSocket.receive(receivePacket);
				byte[] rcvbytes = receivePacket.getData();
				RTPInfo rtpObj = new RTPInfo(rcvbytes, ByteOrder.BIG_ENDIAN);

				int nDataSize = rtpObj.size - 12;

				if (nDataSize != 80 && nDataSize != 160 && nDataSize != 240 && nDataSize != -12)
					return;

				ReceivedRTP rcvRtp = new ReceivedRTP();
				rcvRtp.seq = rtpObj.seq;
				rcvRtp.codec = rtpObj.codec;
				rcvRtp.isExtension = rtpObj.isExtension;
				rcvRtp.ext = new String(rtpObj.extension);
				rcvRtp.peer = new String(rtpObj.peer_number);
				rcvRtp.size = rtpObj.size;
				rcvRtp.buff = rtpObj.voice;

				StackRtp2Instance(rcvRtp);

				/*
				 * RTPRecordInfo rcvObj = new RTPRecordInfo(rcvbytes,
				 * ByteOrder.LITTLE_ENDIAN); rcvObj.toObject(rcvbytes);
				 * 
				 * System.out.format(
				 * "seq:%d, extension:%s, peer_number:%s, isExtension:%d\r\n",
				 * rcvObj.seq, rcvObj.extension, rcvObj.peer_number,
				 * rcvObj.isExtension); System.out.println("seq:" + rcvObj.seq +
				 * ", extension:" + new String(rcvObj.extension) +
				 * ", peer_number:" + new String(rcvObj.peer_number) +
				 * ", isExtension:" + rcvObj.isExtension + "\r\n");
				 */

				/*
				 * InetAddress IPAddress = receivePacket.getAddress(); int port
				 * = receivePacket.getPort(); String capitalizedSentence =
				 * sentence.toUpperCase(); sendData =
				 * capitalizedSentence.getBytes(); DatagramPacket sendPacket =
				 * new DatagramPacket(sendData, sendData.length, IPAddress,
				 * port); serverSocket.send(sendPacket);
				 */
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

	private void StackRtp2Instance(ReceivedRTP rtp)
	{
		RTPRecordInfo ingInstance = null;

		try
		{
			ingInstance = recordIngList.stream().filter(x -> x.ext.equals(rtp.ext)).findFirst().get();
			ingInstance.Add(rtp);
		}
		catch (NoSuchElementException e)
		{
			if (ingInstance == null)
			{
				WaveFormat wavformat;

				switch (rtp.codec)
				{
				case 0:
					wavformat = WaveFormat.CreateMuLawFormat(8000, 1);
					break;
				case 8:
					wavformat = WaveFormat.CreateALawFormat(8000, 1);
					break;
				case 4:
					wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.G723, 8000, 1, 8000, 1, 8);
					break;
				case 18:
					wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.G729, 8000, 1, 8000, 1, 8);
					break;
				default:
					wavformat = WaveFormat.CreateMuLawFormat(8000, 1);
					break;
				}

				LocalDateTime localdatetime = LocalDateTime.now();
				// TimeSpan ts = now - new DateTime(1970, 1, 1, 0, 0, 0, 0,
				// DateTimeKind.Local);

				DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
				String _header = localdatetime.format(df);
				df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String _datepath = localdatetime.format(df);

				String _fileName = String.format("%s_%s_%s.wav", _header.trim(), rtp.ext.trim(), rtp.peer.trim());
				// String _wavFileName = String.format("%s\\%s\\%s", _option.saveDirectory, _datepath, _fileName);

				 String _path = String.format("%s\\%s", _option.saveDirectory, _datepath);
				 
				 File _dir = new File(_path);
				 if (!_dir.exists())
				 {
					 boolean result = _dir.mkdir();
				 }

				RTPRecordInfo recInstance = new RTPRecordInfo(wavformat, String.format("%s\\%s", _option.saveDirectory, _datepath), _fileName);
				recInstance.ext = rtp.ext;
				recInstance.peer = rtp.peer;
				//recInstance.codec = wavformat;
				// recInstance.idx = ts.TotalMilliseconds;
				recInstance.savepath = String.format("%s\\%s", _option.saveDirectory, _datepath);
				recInstance.filename = _fileName;

				recInstance.Add(rtp);
				
				w.lock();
				try
				{
					recordIngList.add(recInstance);					
				}
				finally
				{
					w.unlock();
				}
			}
//			else
//			{
//				ingInstance.Add(rtp);
//			}
		}
	}
}
