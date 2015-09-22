package com.coretree.models;

import java.nio.ByteOrder;

import com.coretree.core.SetGetBytes;

public class GroupWareData extends SetGetBytes<Object>
{
	public byte cmd;
    public byte direct;
    public byte type;
    public byte status;
    public String caller = ""; 
    public String callee = "";
    public String extension = "";
    public int responseCode;
    public int ip;
    public int port;
    public String unconditional = "";
    public String noanswer = "";
    public String busy = "";
    public byte DnD;
    public String UserAgent = "";
    public String dummy = "";
    
	private int len = 162;
    
    public GroupWareData(){}
    
    public GroupWareData(ByteOrder byteorder)
    {
    	this.SetByteOrder(byteorder);
    }
    
	public GroupWareData(byte[] buff, ByteOrder byteorder)
	{
		this.SetByteOrder(byteorder);
		this.toObject(buff);
	}

	@Override
	public byte[] toBytes()
	{
		byte[] out = new byte[len];

		int tlength = 0;
		
		byte[] bytes = object2Bytes(cmd);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(direct);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(type);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(status);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(caller);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(callee);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(extension);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(responseCode);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(ip);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(port);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(unconditional);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(noanswer);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(busy);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(DnD);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(UserAgent);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(dummy);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		return out;
	}

	@Override
	public void toObject(byte[] rcv)
	{
		int tlength = 0;
		this.cmd = (byte)bytes2Object(this.cmd, rcv, tlength, 1);
		tlength += 1;
		this.direct = (byte)bytes2Object(this.direct, rcv, tlength, 1);
		tlength += 1;
		this.type = (byte)bytes2Object(this.type, rcv, tlength, 1);
		tlength += 1;
		this.status = (byte)bytes2Object(this.status, rcv, tlength, 1);
		tlength += 1;
		this.caller = new String((byte[])bytes2Object(new byte[16], rcv, tlength, 16)).trim();
		// this.caller = (char[])bytes2Object(this.caller, rcv, tlength, 16);
		tlength += 16;
		this.callee = new String((byte[])bytes2Object(new byte[16], rcv, tlength, 16)).trim();
		// this.callee = (char[])bytes2Object(this.callee, rcv, tlength, 16);
		tlength += 16;
		this.extension = new String((byte[])bytes2Object(new byte[5], rcv, tlength, 5)).trim();
		// this.extension = (char[])bytes2Object(this.extension, rcv, tlength, 5);
		tlength += 5;
		this.responseCode = (int)bytes2Object(this.responseCode, rcv, tlength, 4);
		tlength += 4;
		this.ip = (int)bytes2Object(this.ip, rcv, tlength, 4);
		tlength += 4;
		this.port = (int)bytes2Object(this.port, rcv, tlength, 4);
		tlength += 4;
		this.unconditional = new String((byte[])bytes2Object(new byte[16], rcv, tlength, 16)).trim();
		// this.unconditional = (char[])bytes2Object(this.unconditional, rcv, tlength, 16);
		tlength += 16;
		this.noanswer = new String((byte[])bytes2Object(new byte[16], rcv, tlength, 16)).trim();
		// this.noanswer = (char[])bytes2Object(this.noanswer, rcv, tlength, 16);
		tlength += 16;
		this.busy = new String((byte[])bytes2Object(new byte[16], rcv, tlength, 16)).trim();
		// this.busy = (char[])bytes2Object(this.busy, rcv, tlength, 16);
		tlength += 16;
		this.DnD = (byte)bytes2Object(this.DnD, rcv, tlength, 1);
		tlength += 1;
		this.UserAgent = new String((byte[])bytes2Object(new byte[10], rcv, tlength, 10)).trim();
		// this.UserAgent = (char[])bytes2Object(this.UserAgent, rcv, tlength, 10);
		tlength += 10;
		this.dummy = new String((byte[])bytes2Object(new byte[50], rcv, tlength, 50)).trim();
		// this.dummy = (char[])bytes2Object(this.dummy, rcv, tlength, 50);
		tlength += 50;
	}
}
