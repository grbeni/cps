package hu.bme.mit.cps.timetable;

public class Timetable implements ITimetable {

	@Override
	public LessonAnswer hasLesson() {
		return new LessonAnswer(true);
	}

}
