package com.coretree.event;

import java.util.EventObject;

public interface IEndOfRtpStreamEventListener {
	public void HandleEndOfRtpStreamEvent(EventObject e);
}
