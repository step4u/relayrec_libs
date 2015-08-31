package com.coretree.models;

public class ReceivedRTP {
    public int seq = -1;
    public int codec = -1;
	public int isExtension = -1;
    public String ext;
    public String peer;
    public int size = -1;
    public byte[] buff = null;
    
    public ReceivedRTP() { }
}
