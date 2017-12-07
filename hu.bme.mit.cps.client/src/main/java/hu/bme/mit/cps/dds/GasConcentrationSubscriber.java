package hu.bme.mit.cps.dds;

import java.util.LinkedList;
import java.util.Queue;

import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;

import hu.bme.mit.cps.client.CloudData;

public class GasConcentrationSubscriber extends DataReaderAdapter {
	
	private ActuatorCommandPublisher commandPublisher;
	private CloudDataSender sender;
	// Stores whether the last check resulted in an alert
	private boolean hadAlert;
	// TO communicate with the timetable service
	private TimetableClient timetableClient;
	
	public GasConcentrationSubscriber(ActuatorCommandPublisher commandPublisher) {
		this.commandPublisher = commandPublisher;
		this.sender = new CloudDataSender();
		timetableClient = new TimetableClient();
		System.out.println("Test: " + new TimetableClient().hasLesson().getLesson());
	}
	
	private Queue<UvegHaz> dataQueue = new LinkedList<UvegHaz>();

	public void on_data_available(DataReader reader) {
		UvegHazDataReader uvegHazReader = (UvegHazDataReader) reader;
		SampleInfo info = new SampleInfo();

		while(true) {
			try {
				UvegHaz receivedData = new UvegHaz();
				uvegHazReader.take_next_sample(receivedData, info);
				if (info.valid_data) {
					System.out.println("Data arrived: " + receivedData);
					this.storeDataLocally(receivedData);
					System.out.println("Sending data to the cloud");
					this.handleData(receivedData);
				}
			} catch (RETCODE_NO_DATA e) {
				break;
			} catch (RETCODE_ERROR e) {
				e.printStackTrace();
			}
		}

	}
	
	private void handleData(UvegHaz uvegHaz) {
		// Checking whether alert is needed
		boolean isAlert = checkConcentration();
		// Using actuators
		if (isAlert && !hadAlert) {
			// Opening window and starting fan
			commandPublisher.openWindow();
			commandPublisher.turnOnFan();
		}
		else if (!isAlert && hadAlert) {
			commandPublisher.closeWindow();
			commandPublisher.turnOffFan();
		}
		hadAlert = isAlert;
		// Sending data to cloud
		sender.send(new CloudData(uvegHaz, ""), isAlert);
	}
	
	private boolean checkConcentration() {
		// TODO Auto-generated method stub
		System.out.println("Has lesson: " + timetableClient.hasLesson());

		return true;
	}

	private void storeDataLocally(UvegHaz uvegHaz) {
		if (dataQueue.size() >= 10) {
			dataQueue.remove();
		}
		dataQueue.add(uvegHaz);
	}
	
	public void shutDown() {
		sender.close();
	}

}
