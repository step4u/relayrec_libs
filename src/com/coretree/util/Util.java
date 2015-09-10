package com.coretree.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Util
{
	private static String OS = System.getProperty("os.name");
	
	public static void WriteLog(String message, int delim)
	{
		OS = System.getProperty("os.name");
		String strDir = "./log";
		if (OS.contains("Windows"))
		{
			strDir = ".\\log\\";
		}
		else
		{
			strDir = "./log/";
		}
		
		File _dir = new File(strDir);
		if (!_dir.exists())
		{
			_dir.mkdir();
		}

		LocalDateTime localdatetime = LocalDateTime.now();
		DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
		String logfilename = localdatetime.format(df);
		if (delim == 1)
		{
			logfilename += "_err";
		}
		
        String strFilename = String.format("%s%s.log", strDir, logfilename);
        StringBuffer sb = new StringBuffer();
        
        sb.append(String.format("%s %s\r\n", localdatetime.toLocalDate().toString(), localdatetime.toLocalTime().toString()));
        sb.append(String.format("%s\r\n", message));
        sb.append("--------------------------------------------------------------\r\n");
        
        FileWriter writer = null;
		try
		{
			writer = new FileWriter(strFilename, true);
			writer.write(sb.toString());
			writer.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
