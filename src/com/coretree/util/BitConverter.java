package com.coretree.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BitConverter
{
	public static byte[] GetBytes(int v)
	{
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putInt(v);
		b.flip();
		return b.array();
	}
	
	public static byte[] GetBytes(short v)
	{
		ByteBuffer b = ByteBuffer.allocate(2);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putShort(v);
		b.flip();
		return b.array();
	}
	
	public static byte[] GetBytes(long v)
	{
		ByteBuffer b = ByteBuffer.allocate(8);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putLong(v);
		b.flip();
		return b.array();
	}
	
	public static byte[] GetBytes(float v)
	{
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putFloat(v);
		b.flip();
		return b.array();
	}
	
	public static byte[] GetBytes(double v)
	{
		ByteBuffer b = ByteBuffer.allocate(8);
		b.order(ByteOrder.LITTLE_ENDIAN);
		b.putDouble(v);
		b.flip();
		return b.array();
	}
	
	public static int ToShort(byte[] value, int startindex)
	{
		ByteBuffer b = ByteBuffer.allocate(4);
		b.order(ByteOrder.LITTLE_ENDIAN);
		return b.getInt();
	}
}
