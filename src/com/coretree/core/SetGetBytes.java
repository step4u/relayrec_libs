package com.coretree.core;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class SetGetBytes<T> {
	
	private ByteOrder byteorder = ByteOrder.BIG_ENDIAN;
	public byte[] sendBytes = null;

	protected void SetByteOrder(ByteOrder border)
	{
		byteorder = border;
	}
	
	public abstract byte[] toBytes();
	
	public abstract void toObject(byte[] rcv);
	
	public byte[] object2Bytes(Object obj)
	{
		String classtype = obj.getClass().getName();
		String strtmp;
		byte[] outbytes = null;
		ByteBuffer b = null;
		
		switch (classtype)
		{
			case "java.lang.Integer":
				b = ByteBuffer.allocate(4);
				b.order(byteorder);
				b.putInt((int)obj);
				outbytes = b.array();
				break;
			case "java.lang.Short":
				b = ByteBuffer.allocate(2);
				b.order(byteorder);
				b.putShort((short)obj);
				outbytes = b.array();
				break;
			case "java.lang.Long":
				b = ByteBuffer.allocate(8);
				b.order(byteorder);
				b.putLong((long)obj);
				outbytes = b.array();
				break;
			case "java.lang.Float":
				b = ByteBuffer.allocate(4);
				b.order(byteorder);
				b.putFloat((float)obj);
				outbytes = b.array();
				break;
			case "java.lang.Double":
				b = ByteBuffer.allocate(8);
				b.order(byteorder);
				b.putDouble((double)obj);
				outbytes = b.array();
				break;
			case "java.lang.Character":
				b = ByteBuffer.allocate(2);
				b.putChar((char)obj);
				outbytes = b.array();
				break;
			case "java.lang.String":
				outbytes = ((String)obj).getBytes();
				break;
			case "[C":
				strtmp = new String((char[])obj);
				outbytes = strtmp.getBytes();
				break;
			default:
				strtmp = "Wrong command!";
				outbytes = strtmp.getBytes();
				break;
		}

		return outbytes;
	}
	
	public Object bytes2Object (Object obj, byte[] bytes, int offset, int size)
	{
		String classtype = obj.getClass().getName();
		Object o = null;
		byte[] tempbytes = new byte[size];
		
		System.arraycopy(bytes, offset, tempbytes, 0, tempbytes.length);
		
		ByteBuffer b = ByteBuffer.allocate(size);
		b.put(tempbytes);
		b.flip();

		switch (classtype)
		{
			case "java.lang.Integer":
				b.order(byteorder);
				o = b.getInt();
				break;
			case "java.lang.Short":
				b.order(byteorder);
				o = b.getShort();
				break;
			case "java.lang.Long":
				b.order(byteorder);
				o = b.getLong();
				break;
			case "java.lang.Float":
				b.order(byteorder);
				o = b.getFloat();
				break;
			case "java.lang.Double":
				b.order(byteorder);
				o = b.getDouble();
				break;
			case "java.lang.Character":
				o = b.getChar();
				break;
			case "java.lang.String":
				o = new String(bytes2Chars(tempbytes));
				break;
			case "[C":
				o = bytes2Chars(tempbytes);
				break;
			case "[B":
				o = tempbytes;
				break;
			default:
				o = tempbytes;
				break;
		}
		
		return o;
	}
	
	public char[] string2Char(String str, int len)
	{
		char[] out = new char[len];
		char[] src = str.toCharArray();
		
		for (int i = 0; i < src.length; i++)
		{
			out[i] = src[i];
		}
		
		return out;
	}
	
	public char[] bytes2Chars(byte[] bytes)
	{
		char[] out = new char[bytes.length];
		
		for (int i = 0; i < bytes.length; i++)
		{
			out[i] = (char)bytes[i];
		}
		
		return out;
	}
}
