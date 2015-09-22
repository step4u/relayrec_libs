package com.coretree.media;

import java.io.*;

import com.coretree.exceptions.ArgumentOutOfRangeException;
import com.coretree.util.BitConverter;

public class WaveFormat {
    public WaveFormatEncoding waveFormatTag;
    public short channel;
    public int sampleRate;
    public int averageBytesPerSecond;
    public short blockAlign;
    public short bitsPerSample;
    public short extraSize;

    public WaveFormat()
    {
    	this(44100, 16, 2);
    }
    
    public WaveFormat(int sampleRate, int channels)
    {
    	this(sampleRate, 16, channels);
    }
    
    public WaveFormat(int rate, int bits, int channels)
    {
    	if (channels < 1)
        {
    		Throwable cause = new Throwable("Channels must be 1 or greater");
            try {
				throw new ArgumentOutOfRangeException("channels", cause);
			} catch (ArgumentOutOfRangeException e) {
				e.printStackTrace();
			}
        }
    	
        // minimum 16 bytes, sometimes 18 for PCM
        this.waveFormatTag = WaveFormatEncoding.Pcm;
        this.channel = (short)channels;
        this.sampleRate = rate;
        this.bitsPerSample = (short)bits;
        this.extraSize = 0;
               
        this.blockAlign = (short)(channels * (bits / 8));
        this.averageBytesPerSecond = this.sampleRate * this.blockAlign;
    }
    
    public static WaveFormat FromFormatChunk(DataInputStream br, int formatChunkLength)
    {
    	WaveFormatExtraData waveFormat = new WaveFormatExtraData();
        try {
			waveFormat.ReadWaveFormat(br, formatChunkLength);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
			waveFormat.ReadExtraData(br);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return waveFormat;
    }
    
    public void ReadWaveFormat(DataInputStream br, int formatChunkLength) throws IOException
    {
//        if (formatChunkLength < 16)
//            throw new InvalidDataException("Invalid WaveFormat Structure");
        
        this.waveFormatTag.SetValue(br.readShort());;
        this.channel = br.readShort();
        this.sampleRate = br.readInt();
        this.averageBytesPerSecond = br.readInt();
        this.blockAlign = br.readShort();
        this.bitsPerSample = br.readShort();
        if (formatChunkLength > 16)
        {
            this.extraSize = br.readShort();
            if (this.extraSize != formatChunkLength - 18)
            {
                System.out.println("Format chunk mismatch\r\n");
                this.extraSize = (short)(formatChunkLength - 18);
            }
        }
    }
    
    public static WaveFormat CreateCustomFormat(WaveFormatEncoding tag, int sampleRate, int channels, int averageBytesPerSecond, int blockAlign, int bitsPerSample)
    {
        WaveFormat waveFormat = new WaveFormat();
        waveFormat.waveFormatTag = tag;
        waveFormat.channel = (short)channels;
        waveFormat.sampleRate = sampleRate;
        waveFormat.averageBytesPerSecond = averageBytesPerSecond;
        waveFormat.blockAlign = (short)blockAlign;
        waveFormat.bitsPerSample = (short)bitsPerSample;
        waveFormat.extraSize = 0;
        return waveFormat;
    }

    public static WaveFormat CreateALawFormat(int sampleRate, int channels)
    {
        return CreateCustomFormat(WaveFormatEncoding.ALaw, sampleRate, channels, sampleRate * channels, channels, 8);
    }
    
    public static WaveFormat CreateMuLawFormat(int sampleRate, int channels)
    {
        return CreateCustomFormat(WaveFormatEncoding.MuLaw, sampleRate, channels, sampleRate * channels, channels, 8);
    }
    
    public DataOutputStream Serialize(DataOutputStream writer) throws IOException
    {
        writer.write(BitConverter.GetBytes((int)(18 + extraSize)));
        writer.write(BitConverter.GetBytes((short)waveFormatTag.GetValue()));
        writer.write(BitConverter.GetBytes((short)channel));
        writer.write(BitConverter.GetBytes((int)sampleRate));
        writer.write(BitConverter.GetBytes((int)averageBytesPerSecond));
        writer.write(BitConverter.GetBytes((short)blockAlign));
        writer.write(BitConverter.GetBytes((short)bitsPerSample));
        writer.write(BitConverter.GetBytes((short)extraSize));
        
        return writer;
    }
}
