package com.coretree.socket;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteOrder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.coretree.event.EndOfCallEventArgs;
import com.coretree.event.IEventHandler;
import com.coretree.media.WaveFormat;
import com.coretree.media.WaveFormatEncoding;
import com.coretree.models.Options;
import com.coretree.models.RTPInfo;
import com.coretree.models.RTPRecordInfo;
import com.coretree.models.ReceivedRTP;
import com.coretree.util.Finalvars;
import com.coretree.util.Util;

public class RTPRecordServer extends Thread implements IEventHandler<EndOfCallEventArgs>
{
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
	
	private DatagramSocket serverSocket;
	private List<RTPRecordInfo> recordIngList;
	
	private Options _option;
	private String OS = System.getProperty("os.name");
	// private Thread sockThread = null;

	public RTPRecordServer()
	{
		recordIngList = new ArrayList<RTPRecordInfo>();
		_option = new Options();
		_option.saveDirectory = "./";

		InitiateSocket();
	}

	public void InitiateSocket()
	{
		try
		{
			// InetSocketAddress address = new InetSocketAddress("localhost", 21010);
			serverSocket = new DatagramSocket(21010);
			
			byte[] receiveData = new byte[416];
			// byte[] sendData = new byte[1024];

			while (true)
			{
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
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
				rcvRtp.ext = rtpObj.extension;
				rcvRtp.peer = rtpObj.peer_number;
				rcvRtp.size = rtpObj.size;
				rcvRtp.buff = rtpObj.voice;
				
				// String logMsg = String.format("seq:%d, ext:%s, peer:%s, isExtension:%d, size:%d", rcvRtp.seq, rcvRtp.ext, rcvRtp.peer, rcvRtp.isExtension, rcvRtp.size);
				// Util.WriteLog(logMsg, 0);

				StackRtp2Instance(rcvRtp);
			}
		}
		catch (IOException e)
		{
			// e.printStackTrace();
			System.err.println("UDP Port 21010 is occupied.");
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1001, e.toString()), 1);
		}
		finally
		{
			serverSocket.close();
		}
		
	}

	private void StackRtp2Instance(ReceivedRTP rtp)
	{
		RTPRecordInfo ingInstance = null;

		r.lock();
		w.lock();
		try
		{
			ingInstance = recordIngList.stream().filter(x -> x.ext.equals(rtp.ext)).findFirst().get();
			ingInstance.Add(rtp);
		}
		catch (NoSuchElementException | NullPointerException e)
		{
			// Util.WriteLog(e.toString(), 1);
			
//			if (ingInstance == null)
//			{
				WaveFormat wavformat;

				switch (rtp.codec)
				{
				case 0:
					wavformat = WaveFormat.CreateMuLawFormat(8000, 1);
					// wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.MuLaw, 8000, 1, 16000, 1, 16);
					break;
				case 8:
					wavformat = WaveFormat.CreateALawFormat(8000, 1);
					// wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.ALaw, 8000, 1, 16000, 1, 16);
					// wavformat = WaveFormat.CreateCustomFormat(WaveFormatEncoding.Pcm, 8000, 1, 8000, 1, 8);
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

				String _strformat = "%s/%s";
				if (OS.contains("Windows"))
				{
					_strformat = "%s\\%s";
				}
				else
				{
					_strformat = "%s/%s";
				}
				
				String _fileName = String.format("%s_%s_%s.wav", _header.trim(), rtp.ext.trim(), rtp.peer.trim());

				 String _path = String.format(_strformat, _option.saveDirectory, _datepath);
				 
				 File _dir = new File(_path);
				 if (!_dir.exists())
				 {
					 // boolean result = _dir.mkdir();
					 _dir.mkdir();
				 }

				ingInstance = new RTPRecordInfo(wavformat, String.format(_strformat, _option.saveDirectory, _datepath), _fileName);
				ingInstance.ext = rtp.ext;
				ingInstance.peer = rtp.peer;
				// recInstance.codec = wavformat;
				// recInstance.idx = ts.TotalMilliseconds;
				ingInstance.savepath = String.format(_strformat, _option.saveDirectory, _datepath);
				ingInstance.filename = _fileName;

				ingInstance.Add(rtp);
				ingInstance.EndOfCallEventHandler.addEventHandler(this);
				
					recordIngList.add(ingInstance);
//			}
		}
		finally
		{
			r.unlock();
			w.unlock();
		}
	}

	@Override
	public void eventReceived(Object sender, EndOfCallEventArgs e)
	{
		RTPRecordInfo item = (RTPRecordInfo)sender;
		String ext = item.ext;
		String peer = item.peer;
		
		try
		{
			recordIngList.removeIf(x -> x.ext.equals(item.ext));
		}
		catch (NullPointerException | UnsupportedOperationException e1)
		{
			Util.WriteLog(String.format(Finalvars.ErrHeader, 1002, e1.toString()), 1);
		}
		finally
		{
			System.out.println(String.format("stream end event : ext:%s, peer:%s", ext, peer));
		}
	}
}
