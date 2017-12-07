package hu.bme.mit.cps.timetable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class TimeTable {

	private List<TimeTableEntry> entries;
	
	public TimeTable() {}
	
	public void addEntry(TimeTableEntry entry) {
		entries.add(entry);
	}
	
	public boolean containsLesson(Date date) {
		for (TimeTableEntry entry : entries) {
			if (entry.inBetween(date)) {
				return true;
			}
		}
		return false;
	}
	
	public void deleteUntil(Date date) {
		List<TimeTableEntry> newEntries = new ArrayList<TimeTableEntry>(entries);
		for (TimeTableEntry entry : entries) {
			if (entry.before(date)) {
				newEntries.remove(entry);
			}
		}
		entries = newEntries;
	}
	
	public void serialize(File file) {
		String json = new Gson().toJson(this);
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(json);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load(File file) {
		try {
			Gson gson = new Gson();
			TimeTable savedTimeTable = gson.fromJson(new FileReader(file), TimeTable.class);
			this.entries = savedTimeTable.entries;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
