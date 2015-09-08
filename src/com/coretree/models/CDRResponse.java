package com.coretree.models;

import java.nio.ByteOrder;

import com.coretree.core.SetGetBytes;

public class CDRResponse extends SetGetBytes<Object>
{
	public int cmd;
    public int pCdr;
    public int status;
    
    private int len = 4 + 4 + 4;
    
    public CDRResponse(){}
    
    public CDRResponse(ByteOrder byteorder)
    {
    	this.SetByteOrder(byteorder);
    }
    
    public CDRResponse(ByteOrder byteorder, int pcdr)
    {
    	this.SetByteOrder(byteorder);
    	this.pCdr = pcdr;
    }
    
	public CDRResponse(byte[] buff, ByteOrder byteorder)
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
		
		bytes = object2Bytes(pCdr);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		bytes = object2Bytes(status);
		System.arraycopy(bytes, 0, out, tlength, bytes.length);
		tlength += bytes.length;
		
		return out;
	}

	@Override
	public void toObject(byte[] rcv)
	{
		// Not supported
	}
}
