/**
 * Copyright(c) 2021 All rights reserved by Jungho Kim in MyungJi University 
 */

package Framework;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Vector;

public class EventQueue implements Serializable
{
    private static final long serialVersionUID = 1L; // Default value for Serializable interface
    private Vector<Event> eventList;
	private long componentId;

	public EventQueue() {
		eventList = new Vector<Event> (15, 1);
		componentId = Calendar.getInstance().getTimeInMillis();
	}
	public long getId()	{
		return componentId;
	}
	public int getSize() {
		return eventList.size();
	}
	public void addEvent(Event newEvent) {
		eventList.add(newEvent);
	}
	public String showEventList() {
		String temp = "";
		for(Event event : eventList)
			temp +="{"+event.getEventId()+"}, {"+event.getMessage()+"}\n";
		return temp;
	}
	public Event getEvent(int i) {
		Event event = null;
			event = eventList.get(i);
		return event;
	}
	public void clearEventQueue() {
		eventList.removeAllElements();
	}
	@SuppressWarnings("unchecked")
	public EventQueue getCopy() {
		EventQueue eventQueue = new EventQueue();
		eventQueue.componentId = componentId;
		eventQueue.eventList = (Vector<Event>) eventList.clone();

		return eventQueue;
	}
}