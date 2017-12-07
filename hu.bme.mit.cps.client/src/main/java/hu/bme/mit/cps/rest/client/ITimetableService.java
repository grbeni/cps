package hu.bme.mit.cps.rest.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("timetable")
public interface ITimetableService {
	@GET
	@Path("lesson")
	@Produces(MediaType.APPLICATION_JSON)
	public LessonAnswer hasLesson();
}