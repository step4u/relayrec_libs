package com.coretree.event;

import java.util.EventObject;

public class EndOfRtpStreamEvent extends EventObject {
	public EndOfRtpStreamEvent(Object source) {
		super(source);
	}
}
