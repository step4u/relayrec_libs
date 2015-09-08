package com.coretree.models;

public class Options
{
	private String OS = System.getProperty("os.name");
	public String saveDirectory = "./";
	
	public Options()
	{
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
