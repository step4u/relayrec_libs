package com.coretree.models;

public class Options
{
	private String OS;
	public String saveDirectory = "./";
	
	public Options()
	{
		OS = System.getProperty("os.name");
		
		if (OS.contains("Windows"))
		{
			this.saveDirectory = ".\\";
		}
		else
		{
			this.saveDirectory = "./";
		}
	}
}
