package hu.bme.mit.cps.timetable;

import java.io.File;
import java.util.Date;

public class TimetableService implements ITimetableService {

	private final String FILE_NAME = "F:\\git\\cps\\hu.bme.mit.cps.timetable\\src\\main\\resources\\timetable.txt";
	
	private Timetable timetable;
	
	public TimetableService() {
		timetable = new Timetable();
		try {
			// Loading the database
			timetable.load(new File(FILE_NAME));
			// Deleting old entries
			timetable.deleteUntil(new Date());
			// Saving the database
			timetable.serialize(new File(FILE_NAME));
		} catch (Throwable e) {
			// Error handling
			e.printStackTrace();
			File saveFile = new File(FILE_NAME);
			timetable.addEntry(new TimetableEntry("CPS", new Date(), new Date()));
			timetable.serialize(saveFile);
		}
	}
	
	@Override
	public LessonAnswer hasLesson() {
		return new LessonAnswer(timetable.containsLesson(new Date()));
	}

}
