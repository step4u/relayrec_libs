package com.coretree.media;

public enum WaveFormatEncoding {
	MuLaw (0x0007),
	ALaw (0x0006),
	G723 (0x00A3),
	G729 (0x00A2),
	Pcm (0x0001);
	
	private int val = 0x0006;
	
	WaveFormatEncoding(int input)
	{
		val = input;
	}
	
	public int GetValue() {
		return val;
	}
	
	public void SetValue(int v) {
		val = v;
	}
}
