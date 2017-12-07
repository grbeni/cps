package hu.bme.mit.cps.timetable;

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
	
	public LessonAnswer(boolean lesson) {
		this.lesson = lesson;
	}
	
	public LessonAnswer() {}
	
	public boolean getLesson() {
		return lesson;
	}
	
	public void setLesson(boolean lesson) {
		this.lesson = lesson;
	}
	
}
