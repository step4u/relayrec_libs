package com.coretree.event;

public interface IEventHandler<TEventArgs extends EventArgs>
{
	public void eventReceived(Object sender, TEventArgs e);
}
