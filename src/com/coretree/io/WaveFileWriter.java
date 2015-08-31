package com.coretree.io;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.coretree.wave.WaveFormat;
import com.coretree.wave.WaveFormatEncoding;

public class WaveFileWriter extends OutputStream {
	private OutputStream outStream;
    private DataOutputStream writer;
    private long dataSizePos;
    private long factSampleCountPos;
    private long dataChunkSize;
    private WaveFormat format;
    private String filename;
    
    public WaveFileWriter(OutputStream outStream, WaveFormat format) throws IOException
    {
        this.outStream = outStream;
        this.format = format;
        this.writer = new DataOutputStream(outStream);
        this.writer.writeBytes("RIFF");
        this.writer.writeInt((int)0); // placeholder
        this.writer.writeBytes("WAVE");
        this.writer.writeBytes("fmt ");
        this.writer = format.Serialize(this.writer);

        CreateFactChunk();
        WriteDataChunkHeader();
    }
    
    public WaveFileWriter(String filename, WaveFormat format) throws FileNotFoundException, IOException
    {
    	this(new FileOutputStream(filename), format);
    	this.filename = filename;
    }
    
	private void WriteDataChunkHeader() throws IOException
    {
        this.writer.write("data");
        //dataSizePos = this.outStream.Position;
        this.writer.write((int)0); // placeholder
    }

	private void CreateFactChunk() throws IOException
    {
        if (HasFactChunk())
        {
            this.writer.write("fact");
            this.writer.write((int)4);
            //factSampleCountPos = this.outStream.Position;
            this.writer.write((int)0); // number of samples
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
    
    protected void UpdateHeader(OutputStreamWriter writer) throws IOException
    {
        writer.flush();
        UpdateRiffChunk(writer);
        UpdateFactChunk(writer);
        UpdateDataChunk(writer);
    }
    
    private void UpdateDataChunk(OutputStreamWriter writer) throws IOException
    {
        //writer.Seek((int)dataSizePos, SeekOrigin.Begin);
        writer.write((int)dataChunkSize);
    }

    private void UpdateRiffChunk(OutputStreamWriter writer)
    {
        //writer.Seek(4, SeekOrigin.Begin);
        //writer.write((int)(outStream.Length - 8));
    }

    private void UpdateFactChunk(OutputStreamWriter writer) throws IOException
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
