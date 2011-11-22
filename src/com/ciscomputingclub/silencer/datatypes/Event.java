/** Event.java */
package com.ciscomputingclub.silencer.datatypes;

/****************************************************************
 * com.ciscomputingclub.silencer.datatypes.Event
 * @author Caleb Gomer
 * @version 1.0
 ***************************************************************/
public class Event {

	private String title;
	private String subtitle;
	private int start;
	private int end;
	private boolean vibrate;

	/****************************************************************
	 * @param title
	 * @param subtitle
	 * @param start
	 * @param end
	 * @param vibrate
	 ***************************************************************/
	public Event(String title,
	    String subtitle,
	    int start,
	    int end,
	    boolean vibrate) {

		this.title = title;
		this.subtitle = subtitle;
		this.start = start;
		this.end = end;
		this.vibrate = vibrate;
	}
	
	public String stringatize() {
		String s = "";
		s+=title+"";
		return s;
	}

	/****************************************************************
	 * @return the title
	 ***************************************************************/
	public String getTitle() {
		return title;
	}

	/****************************************************************
	 * @return the subtitle
	 ***************************************************************/
	public String getSubtitle() {
		return subtitle;
	}

	/****************************************************************
	 * @return the start
	 ***************************************************************/
	public int getStart() {
		return start;
	}

	/****************************************************************
	 * @return the end
	 ***************************************************************/
	public int getEnd() {
		return end;
	}

	/****************************************************************
	 * @return the vibrate
	 ***************************************************************/
	public boolean isVibrate() {
		return vibrate;
	}

	/****************************************************************
	 * @param title the title to set
	 ***************************************************************/
	public void setTitle(String title) {
		this.title = title;
	}

	/****************************************************************
	 * @param subtitle the subtitle to set
	 ***************************************************************/
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/****************************************************************
	 * @param start the start to set
	 ***************************************************************/
	public void setStart(int start) {
		this.start = start;
	}

	/****************************************************************
	 * @param end the end to set
	 ***************************************************************/
	public void setEnd(int end) {
		this.end = end;
	}

	/****************************************************************
	 * @param vibrate the vibrate to set
	 ***************************************************************/
	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}

}
