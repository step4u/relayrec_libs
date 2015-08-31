package com.coretree.wave;

import java.io.*;

public class WaveFormatExtraData extends WaveFormat {
	private byte[] extraData = new byte[100];
	
	public WaveFormatExtraData()
	{
	}
	
	public byte[] ExtraData()
	{
		return extraData;
	}
	
    public WaveFormatExtraData(InputStream reader) throws IOException
    {
    	ReadExtraData(reader);
    }
	
	public void ReadExtraData(InputStream reader) throws IOException
    {
        if (this.extraSize > 0)
        {
            reader.read(extraData, 0, extraSize);
        }
    }

	public void Serialize(ByteArrayOutputStream writer)
    {
        this.Serialize(writer);
        if (extraSize > 0)
        {
            writer.write(extraData, 0, extraSize);
        }
    }
}
