package com.coretree.models;

import java.nio.ByteOrder;

import com.coretree.core.SetGetBytes;

public class CDRData extends SetGetBytes<Object>
{
	public int seq;
    public String office_name = null;
    public int start_yyyy;
    public int start_month;
    public int start_day;
    public int start_hour;
    public int start_min;
    public int start_sec;
    public int end_yyyy;
    public int end_month;
    public int end_day;
    public int end_hour;
    public int end_min;
    public int end_sec;
    public int caller_type;
    public int callee_type;
    public String caller = null;
    public String callee = null;
    public String caller_ipn_number = null;
    public String caller_group_code = null;
    public String caller_group_name = null;
    public String caller_human_name = null;
    public String callee_ipn_number = null;
    public String callee_group_code = null;
    public String callee_group_name = null;
    public String callee_human_name = null;
    public int result;
    public int next;
    
    // private int len = 4 + 40 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 20 + 20 + 20 + 5 + 31 + 20 + 20 + 5 + 31 + 20 + 4 + 4;
    
    public CDRData(){}
    
    public CDRData(ByteOrder byteorder)
    {
    	this.SetByteOrder(byteorder);
    }
    
	public CDRData(byte[] buff, ByteOrder byteorder)
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
		this.seq = (int)bytes2Object(this.seq, rcv, tlength, 4);
		tlength += 4;
		this.office_name = new String((byte[])bytes2Object(new byte[40], rcv, tlength, 40)).trim();
		tlength += 40;
		this.start_yyyy = (int)bytes2Object(this.start_yyyy, rcv, tlength, 4);
		tlength += 4;
		this.start_month = (int)bytes2Object(this.start_month, rcv, tlength, 4);
		tlength += 4;
		this.start_day = (int)bytes2Object(this.start_day, rcv, tlength, 4);
		tlength += 4;
		this.start_hour = (int)bytes2Object(this.start_hour, rcv, tlength, 4);
		tlength += 4;
		this.start_min = (int)bytes2Object(this.start_min, rcv, tlength, 4);
		tlength += 4;
		this.start_sec = (int)bytes2Object(this.start_sec, rcv, tlength, 4);
		tlength += 4;
		this.end_yyyy = (int)bytes2Object(this.end_yyyy, rcv, tlength, 4);
		tlength += 4;
		this.end_month = (int)bytes2Object(this.end_month, rcv, tlength, 4);
		tlength += 4;
		this.end_day = (int)bytes2Object(this.end_day, rcv, tlength, 4);
		tlength += 4;
		this.end_hour = (int)bytes2Object(this.end_hour, rcv, tlength, 4);
		tlength += 4;
		this.end_min = (int)bytes2Object(this.end_min, rcv, tlength, 4);
		tlength += 4;
		this.end_sec = (int)bytes2Object(this.end_sec, rcv, tlength, 4);
		tlength += 4;
		this.caller_type = (int)bytes2Object(this.caller_type, rcv, tlength, 4);
		tlength += 4;
		this.callee_type = (int)bytes2Object(this.callee_type, rcv, tlength, 4);
		tlength += 4;
		this.caller = new String((byte[])bytes2Object(new byte[20], rcv, tlength, 20)).trim();
		tlength += 20;
		this.callee = new String((byte[])bytes2Object(new byte[20], rcv, tlength, 20)).trim();
		tlength += 20;
		this.caller_ipn_number = new String((byte[])bytes2Object(new byte[20], rcv, tlength, 20)).trim();
		tlength += 20;
		this.caller_group_code = new String((byte[])bytes2Object(new byte[5], rcv, tlength, 5)).trim();
		tlength += 5;
		this.caller_group_name = new String((byte[])bytes2Object(new byte[31], rcv, tlength, 31)).trim();
		tlength += 31;
		this.caller_human_name = new String((byte[])bytes2Object(new byte[20], rcv, tlength, 20)).trim();
		tlength += 20;
		this.callee_ipn_number = new String((byte[])bytes2Object(new byte[20], rcv, tlength, 20)).trim();
		tlength += 20;
		this.callee_group_code = new String((byte[])bytes2Object(new byte[5], rcv, tlength, 5)).trim();
		tlength += 5;
		this.callee_group_name = new String((byte[])bytes2Object(new byte[31], rcv, tlength, 31)).trim();
		tlength += 31;
		this.callee_human_name = new String((byte[])bytes2Object(new byte[20], rcv, tlength, 20)).trim();
		tlength += 20;
	}
}
