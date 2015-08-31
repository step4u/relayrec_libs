package com.coretree.models;

import com.coretree.core.*;

public class ReqUserInfo2 extends SetGetBytes<Object> {
	public int cmd;
	public int status;
	public int seqnum;
	public char[] userip = new char[16];
	public char[] userid = new char[30];
	
	private int len = 4 + 4 + 4 + 16 + 30;
	
	public ReqUserInfo2()
	{
	}
	
	@Override
	public byte[] toBytes() {
		byte[] out = new byte[len];
		
		int tlength = 0;
		
		byte[] bytescmd = object2Bytes(cmd);
		System.arraycopy(bytescmd, 0, out, tlength, bytescmd.length);
		tlength = bytescmd.length;
		
		byte[] bytesstatus = object2Bytes(status);
		System.arraycopy(bytesstatus, 0, out, tlength, bytesstatus.length);
		tlength += bytesstatus.length;
		
		byte[] bytesseqnum = object2Bytes(seqnum);
		System.arraycopy(bytesseqnum, 0, out, tlength, bytesseqnum.length);
		tlength += bytesseqnum.length;
		
		byte[] bytesuserip = object2Bytes(userip);
		System.arraycopy(bytesuserip, 0, out, tlength, bytesuserip.length);
		tlength += bytesuserip.length;
		
		byte[] bytesuserid = object2Bytes(userid);
		System.arraycopy(bytesuserid, 0, out, tlength, bytesuserid.length);
		tlength += bytesuserid.length;
		
		return out;
	}

	@Override
	public void toObject(byte[] rcv) {
		int tlength = 0;
		this.cmd = (int)bytes2Object(this.cmd, rcv, tlength, 4);
		tlength += 4;
		this.status = (int)bytes2Object(this.status, rcv, tlength, 4);
		tlength += 4;
		this.seqnum = (int)bytes2Object(this.seqnum, rcv, tlength, 4);
		tlength += 4;
		this.userip = (char[])bytes2Object(this.userip, rcv, tlength, 16);
		tlength += 16;
		this.userid = (char[])bytes2Object(this.userid, rcv, tlength, 30);
	}
}
