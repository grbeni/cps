package hu.bme.mit.cps.dds;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;

import hu.bme.mit.cps.constants.MyConstants;
import hu.bme.mit.cps.datastructs.CloudData;
import hu.bme.mit.cps.datastructs.UvegHaz;
import hu.bme.mit.cps.datastructs.UvegHazDataReader;
import hu.bme.mit.cps.rest.client.TimetableClient;

public class GasConcentrationSubscriber extends DataReaderAdapter {

	private ActuatorCommandPublisher commandPublisher;
	private CloudDataSender sender;
	// Stores whether the last check resulted in an alert
	private boolean hadAlert;
	// To store whether there is a lesson
	private volatile boolean hasLesson;
	// To communicate with the timetable service
	private TimetableClient timetableClient;
	// Timer to check the lessons periodically
	private final Timer timer;

	public GasConcentrationSubscriber(ActuatorCommandPublisher commandPublisher) {
		this.commandPublisher = commandPublisher;
		this.sender = new CloudDataSender();
		timetableClient = new TimetableClient();
		timer = new Timer();
		this.startLessonCheck();
	}

	private Queue<UvegHaz> dataQueue = new LinkedList<UvegHaz>();

	public void on_data_available(DataReader reader) {
		UvegHazDataReader uvegHazReader = (UvegHazDataReader) reader;
		SampleInfo info = new SampleInfo();

		while (true) {
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
		boolean isAlert = checkConcentration(uvegHaz.Value);
		// Using actuators
		if (isAlert && !hadAlert) {
			// Opening window and starting fan
			commandPublisher.openWindow();
			commandPublisher.turnOnFan();
		} else if (!isAlert && hadAlert) {
			commandPublisher.closeWindow();
			commandPublisher.turnOffFan();
		}
		hadAlert = isAlert;
		// Sending data to cloud
		sender.send(new CloudData(uvegHaz, ""), isAlert);
	}

	private boolean checkConcentration(double concentration) {
		// TODO
		if (hasLesson) {

		} else {

		}

		return true;
	}

	private void storeDataLocally(UvegHaz uvegHaz) {
		if (dataQueue.size() >= 10) {
			dataQueue.remove();
		}
		dataQueue.add(uvegHaz);
	}

	/**
	 * Starts a timer which schedules the task responsible for sampling whether
	 * there is a lesson in the observed room.
	 */
	private void startLessonCheck() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("Checking lesson");
				hasLesson = timetableClient.hasLesson();
				System.out.println("has lesson: " + hasLesson);
			}
		}, 0, MyConstants.LESSON_CHECK_INTERVAL);
	}

	public void shutDown() {
		timer.cancel();
		sender.close();
	}

}
