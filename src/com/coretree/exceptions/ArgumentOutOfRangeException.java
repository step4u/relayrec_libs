package com.coretree.exceptions;

@SuppressWarnings("serial")
public class ArgumentOutOfRangeException extends Exception {
	public ArgumentOutOfRangeException() {}
	
	public ArgumentOutOfRangeException(String message)
    {
       super(message);
    }
	
	public ArgumentOutOfRangeException (Throwable cause) {
        super (cause);
    }
	
	public ArgumentOutOfRangeException (String message, Throwable cause) {
        super (message, cause);
    }
}
