package hu.bme.mit.cps.timetable;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class ActivityAnswer {

	@XmlElement
	private boolean activity;
	@XmlElement
	private Date date;
	
	public ActivityAnswer(boolean activity) {
		this.activity = activity;
		this.date = new Date();
	}
	
	public ActivityAnswer() {}
	
	public boolean getActivity() {
		return activity;
	}
	
	public void setActivity(boolean activity) {
		this.activity = activity;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
}
