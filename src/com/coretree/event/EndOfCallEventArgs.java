package com.coretree.event;

import com.coretree.models.RTPRecordInfo;

public class EndOfCallEventArgs extends EventArgs
{
	private RTPRecordInfo item = null;
	
	public EndOfCallEventArgs(RTPRecordInfo _item) {
		this.item = _item;
	}
	
	public EndOfCallEventArgs(String msg)
	{
		// TODO Auto-generated constructor stub
	}

	public RTPRecordInfo getEnfOfCallInstance()
	{
		return this.item;
	}
}
