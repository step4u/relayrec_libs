package com.coretree.io;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.coretree.media.WaveFormat;
import com.coretree.media.WaveFormatEncoding;
import com.coretree.util.BitConverter;

public class WaveFileWriter {
	private int headSize = 36;
	private int headSizePos = 4;
	private FileOutputStream outStream;
    private long dataSizePos;
    private long factSampleCountPos;
    public int dataChunkSize = 0;
    private int dataChunkSizePos = 40;
    private WaveFormat format;
    private String filename;
    private long position = 0;
    
    public WaveFileWriter(FileOutputStream outstream, WaveFormat format) throws IOException
    {
        this.outStream = outstream;
        this.format = format;
        
        this.outStream.write("RIFF".getBytes(), 0, 4);	// Chunk ID
        this.outStream.write(BitConverter.GetBytes(0), 0, 4);	// Chunk Size
        this.outStream.write("WAVE".getBytes(), 0, 4);	// Format

        this.outStream.write("fmt ".getBytes(), 0, 4);	// Chunk ID
        this.outStream.write(BitConverter.GetBytes((int)16), 0, 4);	// Chunk Size
        this.outStream.write(BitConverter.GetBytes((short)format.waveFormatTag.GetValue()), 0, 2);	// Audio Format
        this.outStream.write(BitConverter.GetBytes((short)format.channel), 0, 2);	// Number Of Channel
        this.outStream.write(BitConverter.GetBytes((int)format.sampleRate), 0, 4);	// Sample Rate
        this.outStream.write(BitConverter.GetBytes((int)format.averageBytesPerSecond), 0, 4);	// Byte Rate
        this.outStream.write(BitConverter.GetBytes((short)format.blockAlign), 0, 2);	// Block Align
        this.outStream.write(BitConverter.GetBytes((short)format.bitsPerSample), 0, 2);	// Bit Per Sample
        
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
			this.outStream.write("data".getBytes(), 0, 4);
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        try
		{
			this.outStream.write(BitConverter.GetBytes((int)0), 0, 4);
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
            this.outStream.write("fact".getBytes(), 0, 4);
            this.outStream.write((int)4);
            //factSampleCountPos = this.outStream.Position;
            this.outStream.write((int)0);
        }
    }

    private boolean HasFactChunk()
    {
        return format.waveFormatTag != WaveFormatEncoding.Pcm && format.bitsPerSample != 0;
    }
    
    public void flush() throws IOException
    {
    	//this.outStream.flush();
    	UpdateHeader();
    }
    
    protected void UpdateHeader() throws IOException
    {
    	this.outStream.flush();
        UpdateRiffChunk();
        //UpdateFactChunk(writer);
        UpdateDataChunk();
    }
    
    private void UpdateRiffChunk() throws IOException
    {
    	FileChannel ch = this.outStream.getChannel();
    	position = ch.position();
    	ch.position(headSizePos);
    	ch.write(ByteBuffer.wrap(BitConverter.GetBytes((int)(ch.size() - 8)), 0, 4));
    	ch.position(position);
    	
    	// System.out.println(String.format("ch.size() : %d", ch.size()));
    }
    
    private void UpdateDataChunk() throws IOException
    {
    	FileChannel ch = this.outStream.getChannel();
    	position = ch.position();
    	ch.position(dataChunkSizePos);
    	ch.write(ByteBuffer.wrap(BitConverter.GetBytes(dataChunkSize), 0, 4));
    	ch.position(position);
    }

    private void UpdateFactChunk(DataOutputStream writer) throws IOException
    {
        if (HasFactChunk())
        {
            int bitsPerSample = (format.bitsPerSample * format.channel);
            if (bitsPerSample != 0)
            {
            	// 해당 위치에 update
                //writer.Seek((int)factSampleCountPos, SeekOrigin.Begin);
                writer.write((int)((dataChunkSize * 8) / bitsPerSample));
            }
        }
    }
    
    public void Write(byte[] data, int offset, int count) throws IOException
    {
    	this.outStream.write(data, offset, count);
        dataChunkSize += count;
    }

	public void close() throws IOException
	{
		this.flush();
		if (this.outStream != null)
			this.outStream.close();
	}
}
