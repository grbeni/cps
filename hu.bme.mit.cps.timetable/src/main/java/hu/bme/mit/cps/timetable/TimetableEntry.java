package hu.bme.mit.cps.timetable;

import java.util.Date;

public class TimetableEntry {

	private String activityName;
	private Date beginning;	
	private Date end;
	
	public TimetableEntry(String activityName, Date beginning, Date end) {
		this.activityName = activityName;
		this.beginning = beginning;
		this.end = end;
	}

	public boolean inBetween(Date date) {
		return beginning.before(date) && end.after(date) ||
				beginning.compareTo(date) == 0 || end.compareTo(date) == 0;
	}
	
	public boolean before(Date date) {
		return end.before(date);
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Date getBeginning() {
		return beginning;
	}

	public void setBeginning(Date beginning) {
		this.beginning = beginning;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Override
	public String toString() {
		return "TimetableEntry [activityName=" + activityName + ", beginning=" + beginning + ", end=" + end + "]";
	}
	
}
