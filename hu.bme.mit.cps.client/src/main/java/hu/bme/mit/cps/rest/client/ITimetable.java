package hu.bme.mit.cps.rest.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("timetable")
public interface ITimetable {
	@GET
	@Path("lesson")
	public boolean hasLesson();
}