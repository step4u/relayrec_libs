package com.coretree.models;

import java.nio.ByteOrder;

import com.coretree.core.SetGetBytes;

public class CDRRequest extends SetGetBytes<Object>
{
	public int cmd;
    public int pCdr;
    public byte[] data = new byte[512];
    public int next;
    
    // private int len = 4 + 4 + 512 + 4;
    
    public CDRRequest(){}
    
    public CDRRequest(ByteOrder byteorder)
    {
    	this.SetByteOrder(byteorder);
    }
    
	public CDRRequest(byte[] buff, ByteOrder byteorder)
	{
		this.SetByteOrder(byteorder);
		this.toObject(buff);
	}

	@Override
	public byte[] toBytes()
	{
		return null;
	}

	@Override
	public void toObject(byte[] rcv)
	{
		int tlength = 0;
		this.cmd = (int)bytes2Object(this.cmd, rcv, tlength, 4);
		tlength += 4;
		this.pCdr = (int)bytes2Object(this.pCdr, rcv, tlength, 4);
		tlength += 4;
		this.data = (byte[])bytes2Object(this.data, rcv, tlength, 512);
		tlength += 512;
		this.next = (int)bytes2Object(this.next, rcv, tlength, 4);
		tlength += 4;
	}
}
