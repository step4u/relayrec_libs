package com.coretree.models;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import com.coretree.io.WaveFileWriter;
import com.coretree.wave.WaveFormat;;

public class RTPRecordInfo implements Closeable
{
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
	
	private Timer timer;
	private Timer endtimer;
	private short endcount = 0;
	public double idx = 0.0d;
	public String ext;
	public String peer;
	public WaveFormat codec;
	public String savepath;
	public String filename;

	private List<ReceivedRTP> listIn;
	private List<ReceivedRTP> listOut;
	private WaveFileWriter writer = null;
	private WaveFormat pcmFormat = new WaveFormat(8000, 16, 1);

	private final int timerInterval = 3000;
	private final int endtimerInterval = 15000;

	public RTPRecordInfo(WaveFormat _codec, String savepath, String filename)
	{
		// LocalDateTime now = LocalDateTime.now();
		// long ts = now - (new LocalDateTime(1970, 1, 1, 0, 0, 0, 0));
		// idx = ts.TotalMilliseconds;
		codec = _codec;
		listIn = new ArrayList<ReceivedRTP>();
		listOut = new ArrayList<ReceivedRTP>();

		try
		{
			writer = new WaveFileWriter(String.format("%s\\%s", savepath, filename), pcmFormat);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.InitTimer();
	}

	private void InitTimer()
	{
		Timer_Elapsed timer_elapsed = new Timer_Elapsed();
		timer = new Timer();
		timer.schedule(timer_elapsed, timerInterval, timerInterval);

		Endtimer_Elapsed endtimer_Elapsed = new Endtimer_Elapsed();
		endtimer = new Timer();
		endtimer.schedule(endtimer_Elapsed, endtimerInterval, endtimerInterval);
	}

	public void Add(ReceivedRTP obj)
    {
        if (obj.size == 0)
        {
            endcount++;
        }

        if (endcount > 1)
        {
            if (timer != null)
            {
                timer.cancel();
                timer.purge();
            }

            if (endtimer != null)
            {
                endtimer.cancel();
                endtimer.purge();
            }

            this.MixRtp("final");

//            if (EndOfRtpStreamEven != null)
//                EndOfRtpStreamEven(this, new EventArgs());

            return;
        }

        if (endtimer != null)
        {
            endtimer.cancel();
            endtimer.purge();
            
            Endtimer_Elapsed endtimer_Elapsed = new Endtimer_Elapsed();
            endtimer = new Timer();
            endtimer.schedule(endtimer_Elapsed, endtimerInterval, endtimerInterval);
        }

        if (obj.size == 0) return;

        if (obj.isExtension == 1)
        {
        	ReceivedRTP tmpRtp = new ReceivedRTP();
        	tmpRtp.seq = obj.seq;
        	tmpRtp.size = obj.size;
        	tmpRtp.isExtension = obj.isExtension;
        	tmpRtp.ext = obj.ext;
        	tmpRtp.peer = obj.peer;
        	tmpRtp.buff = obj.buff;
        	
        	w.lock();
        	try
        	{
            	listIn.add(tmpRtp);
        	}
        	finally
        	{
        		w.unlock();
        	}
        }
        else
        {
        	ReceivedRTP tmpRtp = new ReceivedRTP();
        	tmpRtp.seq = obj.seq;
        	tmpRtp.size = obj.size;
        	tmpRtp.isExtension = obj.isExtension;
        	tmpRtp.ext = obj.ext;
        	tmpRtp.peer = obj.peer;
        	tmpRtp.buff = obj.buff;
        	
        	w.lock();
        	try
        	{
            	listOut.add(tmpRtp);        		
        	}
        	finally
        	{
        		w.unlock();
        	}
        }
    }

	private void MixRtp(String check)
	{
		if (listIn == null || listOut == null)
			return;

		List<ReceivedRTP> linin = new ArrayList<ReceivedRTP>();
		List<ReceivedRTP> linout = new ArrayList<ReceivedRTP>();

		r.lock();
		try
		{
			linin = listIn.stream().collect(Collectors.toList());			
		}
		finally
		{
			r.unlock();
		}

		r.lock();
		try
		{
			linout = listOut.stream().collect(Collectors.toList());			
		}
		finally
		{
			r.unlock();
		}

		Collections.sort(linin, new Comparator<ReceivedRTP>()
		{
			@Override
			public int compare(ReceivedRTP o1, ReceivedRTP o2)
			{
				if (o1.seq > o2.seq)
					return 1;
				else if (o1.seq < o2.seq)
					return -1;
				else
					return 0;
			}
		});

		Collections.sort(linout, new Comparator<ReceivedRTP>()
		{
			@Override
			public int compare(ReceivedRTP o1, ReceivedRTP o2)
			{
				if (o1.seq > o2.seq)
					return 1;
				else if (o1.seq < o2.seq)
					return -1;
				else
					return 0;
			}
		});

		ReceivedRTP itemIn = null;
		ReceivedRTP itemOut = null;
		
		try
		{
			itemIn = linin.stream().findFirst().get();
		}
		catch (NoSuchElementException e)
		{
			//itemIn = null;
		}
		
		try
		{
			itemOut = linout.stream().findFirst().get();
		}
		catch (NoSuchElementException e)
		{
			//itemOut = null;
		}
		
		DelayedMsec delayedMsec = DelayedMsec.same;
		if (itemIn == null || itemOut == null)
		{
			return;
		}
		else
		{
			byte[] mixedbytes = null;
			if ((itemIn.size - headersize) == 80 && (itemOut.size - headersize) == 160)
			{
				delayedMsec = DelayedMsec.i80o160;

				if (check.equals("final"))
				{
					for (ReceivedRTP item : linout)
					{
						mixedbytes = this.Mixing(linin, linout, item, delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
				else
				{
					for (int i = 0; i < linout.size() * 0.8; i++)
					{
						mixedbytes = this.Mixing(linin, linout, linout.get(i), delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
			}
			else if ((itemIn.size - headersize) == 160 && (itemOut.size - headersize) == 80)
			{
				delayedMsec = DelayedMsec.i160o80;

				if (check.equals("final"))
				{
					for (ReceivedRTP item : linin)
					{
						mixedbytes = this.Mixing(linin, linout, item, delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
				else
				{
					for (int i = 0; i < linin.size() * 0.8; i++)
					{
						mixedbytes = this.Mixing(linin, linout, linin.get(i), delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
			}
			else
			{
				delayedMsec = DelayedMsec.same;

				if (check.equals("final"))
				{
					for (ReceivedRTP item : linin)
					{
						mixedbytes = this.Mixing(linin, linout, item, delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
				else
				{
					for (int i = 0; i < linin.size() * 0.8; i++)
					{
						mixedbytes = this.Mixing(linin, linout, linin.get(i), delayedMsec);
						this.WaveFileWriting(mixedbytes);
					}
				}
			}
		}
	}

	private int headersize = 12;

	private byte[] Mixing(List<ReceivedRTP> linin, List<ReceivedRTP> linout, ReceivedRTP item, DelayedMsec delayedMsec)
	{
		byte[] mixedbytes = null;
		
		ReceivedRTP _item0 = null;
		ReceivedRTP _item1 = null;

		if (delayedMsec == DelayedMsec.i80o160)
		{
			int seq = item.seq * 2;
			
			try
			{
				r.lock();
				try
				{
					_item0 = linin.stream().filter(x -> x.seq == seq).findFirst().get();					
				}
				finally
				{
					r.unlock();
				}
			}
			catch (NoSuchElementException e)
			{
				_item0 = new ReceivedRTP();
				_item0.buff = new byte[332];
				_item0.seq = seq;
				_item0.size = 92;
				_item0.ext = item.ext;
				_item0.peer = item.peer;
			}
			
			try
			{
				r.lock();
				try
				{
					_item1 = linin.stream().filter(x -> x.seq == (seq + 1)).findFirst().get();					
				}
				finally
				{
					r.unlock();
				}
			}
			catch (NoSuchElementException e)
			{
				_item1 = new ReceivedRTP();
				_item1.buff = new byte[332];
				_item1.seq = seq + 1;
				_item1.size = 92;
				_item1.ext = item.ext;
				_item1.peer = item.peer;
			}
			
			final ReceivedRTP __item0 = _item0;
			final ReceivedRTP __item1 = _item1;

			// item2 + tmpitem mix with item1 and write
			byte[] tmpbuff = new byte[332];
			System.arraycopy(__item0.buff, 0, tmpbuff, 0, __item0.size);
			System.arraycopy(__item1.buff, headersize, tmpbuff, __item0.size, (__item1.size - headersize));

			ReceivedRTP _itm = new ReceivedRTP();
			_itm.buff = tmpbuff;
			_itm.size = (_item0.size + _item1.size - headersize);

			mixedbytes = this.RealMix(_itm, item);

			w.lock();
			try
			{
				listIn.removeIf(x -> (x.seq == __item0.seq));
			}
			finally
			{
				w.unlock();
			}
			
			w.lock();
			try
			{
				listIn.removeIf(x -> x.seq == __item1.seq);				
			}
			finally
			{
				w.unlock();
			}

			w.lock();
			try
			{
				listOut.removeIf(x -> x.seq == item.seq);				
			}
			finally
			{
				w.unlock();
			}
		}
		else if (delayedMsec == DelayedMsec.i160o80)
		{
			int seq = item.seq * 2;
			
			try
			{
				r.lock();
				try
				{
					_item0 = linout.stream().filter(x -> x.seq == seq).findFirst().get();					
				}
				finally
				{
					r.unlock();
				}
			
			}
			catch (NoSuchElementException e)
			{
				_item0 = new ReceivedRTP();
				_item0.buff = new byte[332];
				_item0.seq = seq;
				_item0.size = 92;
				_item0.ext = item.ext;
				_item0.peer = item.peer;
			}
			
			try
			{
				r.lock();
				try
				{
					_item1 = linout.stream().filter(x -> x.seq == seq + 1).findFirst().get();					
				}
				finally
				{
					r.unlock();
				}
				
			}
			catch (NoSuchElementException e)
			{
				_item1 = new ReceivedRTP();
				_item1.buff = new byte[332];
				_item1.seq = seq;
				_item1.size = 92;
				_item1.ext = item.ext;
				_item1.peer = item.peer;
			}
			
			final ReceivedRTP __item0 = _item0;
			final ReceivedRTP __item1 = _item1;

			// item2 + tmpitem mix with item1 and write
			byte[] tmpbuff = new byte[332];
			System.arraycopy(__item0.buff, 0, tmpbuff, 0, __item0.size);
			System.arraycopy(__item1.buff, headersize, tmpbuff, __item0.size, (__item1.size - headersize));
			ReceivedRTP _itm = new ReceivedRTP();
			_itm.buff = tmpbuff;
			_itm.size = (__item0.size + __item1.size - headersize);

			mixedbytes = this.RealMix(_itm, item);

			w.lock();
			try
			{
				listIn.removeIf(x -> x.seq == item.seq);				
			}
			finally
			{
				w.unlock();
			}

			w.lock();
			try
			{
				listOut.removeIf(x -> x.seq == __item0.seq);				
			}
			finally
			{
				w.unlock();
			}

			w.lock();
			try
			{
				listOut.removeIf(x -> x.seq == __item1.seq);				
			}
			finally
			{
				w.unlock();
			}
		}
		else
		{
			// item > in
			// same
			// item1 mix with item2 and write
			ReceivedRTP _item = null;
			
			try
			{
				r.lock();
				try
				{
					_item = linout.stream().filter(x -> x.seq == item.seq).findFirst().get();					
				}
				finally
				{
					r.unlock();
				}
			}
			catch (NoSuchElementException e)
			{
				_item = new ReceivedRTP();
				_item.buff = new byte[332];
				_item.seq = item.seq;
				_item.size = item.size;
				_item.ext = item.ext;
				_item.peer = item.peer;
			}
			
			final ReceivedRTP __item = _item;

			mixedbytes = this.RealMix(item, __item);

			w.lock();
			try
			{
				listIn.removeIf(x -> x.seq == item.seq);
			}
			finally
			{
				w.unlock();
			}
			
			w.lock();
			try
			{
				listOut.removeIf(x -> x.seq == __item.seq);				
			}
			finally
			{
				w.unlock();
			}
		}

		return mixedbytes;
	}

	private byte[] RealMix(ReceivedRTP item1, ReceivedRTP item2)
	{
		if (item1 == null || item2 == null)
			return null;

		if (item1.size == 0 || item2.size == 0)
			return null;

		byte[] wavSrc1 = new byte[item1.size - headersize];
		byte[] wavSrc2 = new byte[item2.size - headersize];

		System.arraycopy(item1.buff, headersize, wavSrc1, 0, (item1.size - headersize));
		System.arraycopy(item2.buff, headersize, wavSrc2, 0, (item2.size - headersize));

		byte[] mixedbytes = new byte[wavSrc1.length];
		
		for (int i=0; i < wavSrc1.length; i++)
			mixedbytes[i] = (byte) ((wavSrc1[i] + wavSrc2[i]) >> 1);
		
		return mixedbytes;
	}
	
    private void WaveFileWriting(byte[] buff)
    {
        if (buff == null) return;

        if (buff.length == 0) return;

        try
		{
			this.writer.write(buff, 0, buff.length);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        try
		{
			this.writer.flush();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	@Override
	public void close() throws IOException
	{
		// TODO Auto-generated method stub
		this.writer.close();
	}

	enum DelayedMsec
	{
		i80o160, i160o80, same
	}

	class Timer_Elapsed extends TimerTask
	{
		@Override
		public void run()
		{
			MixRtp("");
		}
	}

	class Endtimer_Elapsed extends TimerTask
	{

		@Override
		public void run()
		{
			MixRtp("final");
			
			try
			{
				close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// if (EndOfRtpStreamEven != null)
			// EndOfRtpStreamEven(this, new EventArgs());
		}

	}
}