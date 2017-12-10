package hu.bme.mit.cps.rest.client;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class LessonAnswer {

	@XmlElement
	private boolean lesson;
	@XmlElement
	private Date date;
	
	public LessonAnswer(boolean lesson) {
		this.lesson = lesson;
		this.date = new Date();
	}
	
	public LessonAnswer() {}
	
	public boolean getLesson() {
		return lesson;
	}
	
	public void setLesson(boolean lesson) {
		this.lesson = lesson;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public Date getDate() {
		return date;
	}
	
}
