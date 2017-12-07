package hu.bme.mit.cps.timetable;

import java.util.Date;

public class TimetableEntry {

	private String lessonName;
	private Date beginning;	
	private Date end;
	
	public TimetableEntry(String lessonName, Date beginning, Date end) {
		this.lessonName = lessonName;
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

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
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
		return "TimetableEntry [lessonName=" + lessonName + ", beginning=" + beginning + ", end=" + end + "]";
	}
	
}
