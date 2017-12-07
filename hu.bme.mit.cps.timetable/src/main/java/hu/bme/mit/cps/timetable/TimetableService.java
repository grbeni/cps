package hu.bme.mit.cps.timetable;

import java.io.File;
import java.util.Date;

public class TimetableService implements ITimetableService {

	private final String FILE_NAME = "F:\\git\\cps\\hu.bme.mit.cps.timetable\\src\\main\\resources\\timetable.txt";
	
	private TimeTable timeTable;
	
	public TimetableService() {
		timeTable = new TimeTable();
		try {
			// Loading the database
			timeTable.load(new File(FILE_NAME));
			// Deleting old entries
			timeTable.deleteUntil(new Date());
			// Saving the database
			timeTable.serialize(new File(FILE_NAME));
		} catch (Throwable e) {
			// Error handling
			e.printStackTrace();
			File saveFile = new File(FILE_NAME);
			timeTable.addEntry(new TimeTableEntry("CPS", new Date(), new Date()));
			timeTable.serialize(saveFile);
		}
	}
	
	@Override
	public LessonAnswer hasLesson() {
		return new LessonAnswer(timeTable.containsLesson(new Date()));
	}

}
