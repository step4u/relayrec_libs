package com.coretree.models;

import java.nio.ByteOrder;

import com.coretree.core.SetGetBytes;

public class RTPInfo extends SetGetBytes<Object> {
	public String extension;
	public String peer_number;
	public int isExtension;
	public int codec;
	public int seq;
	public int rtp_seq;
	public int yyyy;
	public int month;
	public int day;
	public int hh;
	public int mm;
	public int sec;
	public int msec;
	public int offset;
	public int size;
	public byte[] voice = new byte[320 + 12];
	public int next;	
	
	private int len = 5 + 20 + (4 * 13) + (320 + 12) + 4;
	
	public RTPInfo()
	{
	}
	
	public RTPInfo(ByteOrder byteorder)
	{
		this.SetByteOrder(byteorder);
	}
	
	public RTPInfo(byte[] buff, ByteOrder byteorder)
	{
		this.SetByteOrder(byteorder);
		this.toObject(buff);
	}

	@Override
	public byte[] toBytes() {
		byte[] out = new byte[len];
		
		int tlength = 0;
		
		byte[] bytes = object2Bytes(extension);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(peer_number);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(isExtension);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(codec);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(seq);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(rtp_seq);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(yyyy);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(month);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(day);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(hh);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(mm);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(sec);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(msec);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(offset);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(size);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(voice);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(next);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		return out;
	}

	@Override
	public void toObject(byte[] rcv) {
		int tlength = 0;
		this.extension = new String((byte[])bytes2Object(new byte[5], rcv, tlength, 5)).trim();
		tlength += 5;
		this.peer_number = new String((byte[])bytes2Object(new byte[20], rcv, tlength, 20)).trim();
		tlength += 20;
		tlength += 3;
		this.isExtension = (int)bytes2Object(this.isExtension, rcv, tlength, 4);
		tlength += 4;
		this.codec = (int)bytes2Object(this.codec, rcv, tlength, 4);
		tlength += 4;
		this.seq = (int)bytes2Object(this.seq, rcv, tlength, 4);
		tlength += 4;
		this.rtp_seq = (int)bytes2Object(this.rtp_seq, rcv, tlength, 4);
		tlength += 4;
		this.yyyy = (int)bytes2Object(this.yyyy, rcv, tlength, 4);
		tlength += 4;
		this.month = (int)bytes2Object(this.month, rcv, tlength, 4);
		tlength += 4;
		this.day = (int)bytes2Object(this.day, rcv, tlength, 4);
		tlength += 4;
		this.hh = (int)bytes2Object(this.hh, rcv, tlength, 4);
		tlength += 4;
		this.mm = (int)bytes2Object(this.mm, rcv, tlength, 4);
		tlength += 4;
		this.sec = (int)bytes2Object(this.sec, rcv, tlength, 4);
		tlength += 4;
		this.msec = (int)bytes2Object(this.msec, rcv, tlength, 4);
		tlength += 4;
		this.offset = (int)bytes2Object(this.offset, rcv, tlength, 4);
		tlength += 4;
		this.size = (int)bytes2Object(this.size, rcv, tlength, 4);
		tlength += 4;
		System.arraycopy(rcv, tlength, this.voice, 0, 320 + 12);
		tlength += (320 + 12);
		this.next = (int)bytes2Object(this.next, rcv, tlength, 4);
		tlength += 4;
	}
}
