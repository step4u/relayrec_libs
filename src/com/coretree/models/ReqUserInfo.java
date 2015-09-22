package com.coretree.models;

public class ReqUserInfo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int cmd;
	public int status;
	public int seqnum;
	public char[] userip = new char[16];
	public char[] userid = new char[30];
}
