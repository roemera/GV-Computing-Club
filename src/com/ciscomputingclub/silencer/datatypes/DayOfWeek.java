/** DayOfWeek.java */
package com.ciscomputingclub.silencer.datatypes;

import java.util.ArrayList;

/****************************************************************
 * com.ciscomputingclub.silencer.datatypes.DayOfWeek
 * 
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public class DayOfWeek {

	private ArrayList<Event> events;

	public DayOfWeek(ArrayList<Event> events) {
		this.events = events;
	}

	public void addEvent(Event event) {
		if (events != null)
			events.add(event);
	}

	public Event getEvent(int pos) {
		return events.get(pos);
	}

	public String[] getAllNames() {
		int size = events.size();
		String s[] = new String[size];
		for (int i = 0; i < size; i++)
			s[i] = events.get(i).getTitle();
		return s;
	}

	public String stringatize() {
		String s = "";
		for (Event e : events)
			s += e.stringatize();
		return s;
	}

}
