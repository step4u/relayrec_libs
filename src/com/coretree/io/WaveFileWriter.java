package com.coretree.io;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.coretree.util.BitConverter;
import com.coretree.wave.WaveFormat;
import com.coretree.wave.WaveFormatEncoding;

public class WaveFileWriter extends OutputStream {
	private int headSize = 32;
	private int headSizePos = 4;
	private OutputStream outStream;
    private DataOutputStream writer;
    private long dataSizePos;
    private long factSampleCountPos;
    public int dataChunkSize = 0;
    private int dataChunkSizePos = 28;
    private WaveFormat format;
    private String filename;
    
    public WaveFileWriter(OutputStream outStream, WaveFormat format) throws IOException
    {
        this.outStream = outStream;
        this.format = format;
        this.writer = new DataOutputStream(outStream);
        this.writer.writeBytes("RIFF");
        this.writer.write(BitConverter.GetBytes(0), 0, 4);
        this.writer.writeBytes("WAVE");
        this.writer.writeBytes("fmt ");
        
        this.writer.write(BitConverter.GetBytes((int)16));
        this.writer.write(BitConverter.GetBytes((short)format.waveFormatTag.GetValue()));
        this.writer.write(BitConverter.GetBytes((short)format.channels));
        this.writer.write(BitConverter.GetBytes((int)format.sampleRate));
        this.writer.write(BitConverter.GetBytes((int)format.averageBytesPerSecond));
        this.writer.write(BitConverter.GetBytes((short)format.blockAlign));
        this.writer.write(BitConverter.GetBytes((short)format.bitsPerSample));
        //this.writer.write(BitConverter.GetBytes((short)format.extraSize));
        
        //this.writer = format.Serialize(this.writer);

        //CreateFactChunk();
        WriteDataChunkHeader();
        
        //this.close();
    }
    
    public WaveFileWriter(String filename, WaveFormat format) throws FileNotFoundException, IOException
    {
    	this(new FileOutputStream(filename), format);
    	this.filename = filename;
    }
    
	private void WriteDataChunkHeader()
    {
        try
		{
			this.writer.writeBytes("data");
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try
		{
			this.writer.write(BitConverter.GetBytes((int)0));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	private void CreateFactChunk() throws IOException
    {
        if (HasFactChunk())
        {
            this.writer.writeBytes("fact");
            this.writer.write((int)4);
            //factSampleCountPos = this.outStream.Position;
            this.writer.write((int)0);
        }
    }

    private boolean HasFactChunk()
    {
        return format.waveFormatTag != WaveFormatEncoding.Pcm && format.bitsPerSample != 0;
    }
    
    @Override
    public void flush() throws IOException
    {
    	this.writer.flush();
    	UpdateHeader(writer);
    }
    
    protected void UpdateHeader(DataOutputStream writer) throws IOException
    {
        writer.flush();
        UpdateRiffChunk();
        //UpdateFactChunk(writer);
        UpdateDataChunk();
    }
    
    private void UpdateDataChunk()
    {
        //writer.Seek((int)dataSizePos, SeekOrigin.Begin);
        try
		{
			this.writer.write(BitConverter.GetBytes(dataChunkSize), dataChunkSizePos, 4);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    private void UpdateRiffChunk()
    {
    	try
		{
			this.writer.write(BitConverter.GetBytes((int)(headSize + dataChunkSize - 8)), headSizePos, 4);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
    }

    private void UpdateFactChunk(DataOutputStream writer) throws IOException
    {
        if (HasFactChunk())
        {
            int bitsPerSample = (format.bitsPerSample * format.channels);
            if (bitsPerSample != 0)
            {
            	// 해당 위치에 update
                //writer.Seek((int)factSampleCountPos, SeekOrigin.Begin);
                writer.write((int)((dataChunkSize * 8) / bitsPerSample));
            }
        }
    }
    
    @Override
    public void write(byte[] data, int offset, int count) throws IOException
    {
    	outStream.write(data, offset, count);
        dataChunkSize += count;
    }

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void close() throws IOException
	{
		this.flush();
		if (this.writer != null)
			this.writer.close();
	}
}
