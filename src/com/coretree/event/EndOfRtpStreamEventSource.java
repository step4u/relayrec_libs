package com.coretree.event;

import java.util.ArrayList;
import java.util.Iterator;

public class EndOfRtpStreamEventSource {
	private ArrayList<EndOfRtpStreamEventListener> _listeners = new ArrayList<EndOfRtpStreamEventListener>();
	
	public synchronized void addEventListener(EndOfRtpStreamEventListener listener)  {
		_listeners.add(listener);
	}
	
	public synchronized void removeEventListener(EndOfRtpStreamEventListener listener)   {
		_listeners.remove(listener);
	}
	 
	private synchronized void fireEvent() {
		EndOfRtpStreamEvent event = new EndOfRtpStreamEvent(this);
		Iterator<EndOfRtpStreamEventListener> i = _listeners.iterator();
		while(i.hasNext())  {
			((EndOfRtpStreamEventListener) i.next()).HandleEndOfRtpStreamEvent(event);
		}
	}
}
