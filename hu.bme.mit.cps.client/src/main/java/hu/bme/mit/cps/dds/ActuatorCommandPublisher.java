package hu.bme.mit.cps.dds;

import java.util.Date;

import com.rti.dds.infrastructure.InstanceHandle_t;

import hu.bme.mit.cps.datastructs.UvegHaz;
import hu.bme.mit.cps.datastructs.UvegHazDataWriter;

public class ActuatorCommandPublisher {
	
	private UvegHazDataWriter actuatorWriter;

	public ActuatorCommandPublisher(UvegHazDataWriter actuatorWriter) {
		this.actuatorWriter = actuatorWriter;
	}
	
	public void turnOnFan() {
		actuatorWriter.write(createUvegHaz("setvent", 1, (int) new Date().getTime()), InstanceHandle_t.HANDLE_NIL);
	}
	
	public void turnOffFan() {
		actuatorWriter.write(createUvegHaz("setvent", 0, (int) new Date().getTime()), InstanceHandle_t.HANDLE_NIL);
	}
	
	public void openWindow() {	
		actuatorWriter.write(createUvegHaz("setwindow1", 1, (int) new Date().getTime()), InstanceHandle_t.HANDLE_NIL);
		actuatorWriter.write(createUvegHaz("setwindow2", 1, (int) new Date().getTime()), InstanceHandle_t.HANDLE_NIL);
	}

	public void closeWindow() {
		actuatorWriter.write(createUvegHaz("setwindow1", 0, (int) new Date().getTime()), InstanceHandle_t.HANDLE_NIL);
		actuatorWriter.write(createUvegHaz("setwindow2", 0, (int) new Date().getTime()), InstanceHandle_t.HANDLE_NIL);
	}	
	
	private UvegHaz createUvegHaz(String id, int value, int timeStamp) {
		UvegHaz uvegHazData = new UvegHaz();
		uvegHazData.ID = id;
		uvegHazData.Value = value;
		uvegHazData.TimeStamp = timeStamp;
		return uvegHazData;
	}
	
}
