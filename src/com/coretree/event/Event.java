package com.coretree.event;

import java.util.ArrayList;

public final class Event<TEventArgs extends EventArgs>
{
	private ArrayList<IEventHandler<TEventArgs>> observerList = new ArrayList<IEventHandler<TEventArgs>>();
	
	// Raise Event
	public void raiseEvent(Object sender, TEventArgs e) {
		for(IEventHandler<TEventArgs> handler : this.observerList) {
			handler.eventReceived(sender, e);
		}
	}
	
	// Add Event Handler
	public void addEventHandler(IEventHandler<TEventArgs> handler) {
		this.observerList.add(handler);			
	}
	
	// Remove Event Handler
	public void removeEventHandler(IEventHandler<TEventArgs> handler) {
		this.observerList.remove(handler);
	}
}
