package hu.bme.mit.cps.timetable;

import java.io.File;
import java.util.Date;

public class TimetableService implements ITimetableService {

	private final String FILE_NAME = "timetable.txt";
	
	private TimeTable timeTable;
	
	public TimetableService() {
//		timeTable = new TimeTable();
//		Date beginning = new Date();
//		File saveFile = new File(FILE_NAME);
//		System.out.println("Full path: " + saveFile.getAbsolutePath());
//		Date end = new Date();
//		timeTable.addEntry(new TimeTableEntry("CPS", beginning, end));
//		timeTable.serialize(saveFile);
	}
	
	@Override
	public LessonAnswer hasLesson() {
		return new LessonAnswer(true);
	}

}
