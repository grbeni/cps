package hu.bme.mit.cps.dds;

import java.io.IOException;
import java.util.Date;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;

import hu.bme.mit.cps.datastructs.UvegHaz;
import hu.bme.mit.cps.datastructs.UvegHazDataReader;
import hu.bme.mit.cps.datastructs.UvegHazDataWriter;
import hu.bme.mit.cps.datastructs.UvegHazTypeSupport;

public class Main {
	
	private static UvegHazDataWriter dataWriter;
	private static UvegHazDataReader gasConcentrationReader;
	private static DomainParticipant participant;
	private static GasConcentrationSubscriber gasConcentrationSubscriber;
	
	public static void main(String[] args) throws IOException {		
		// Creating the infrastructure
		createDdsInfrastructure();		
		// Writing data 
		sendDummyData();

		System.out.println("Press ENTER to exit.");
		System.in.read();		
		System.out.println("Terminating.");
		
		tearDownDdsInfrastructure();
	}

	private static void sendDummyData() {
		UvegHaz uvegHaz = new UvegHaz();
		uvegHaz.ID = "TestData";
		uvegHaz.Value = 99.9;
		uvegHaz.TimeStamp = 1;
		while (true) {
			uvegHaz.Value = Math.random() * 1000;
			uvegHaz.TimeStamp = (int) new Date().getTime();
			dataWriter.write(uvegHaz, InstanceHandle_t.HANDLE_NIL);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void createDdsInfrastructure() {
		participant = DomainParticipantFactory.get_instance().create_participant(
				0, DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (participant == null) {
			System.err.println("Unable to create domain participant.");
			return;
		}
		// Registering our own type
		String uvegHazTypeName = UvegHazTypeSupport.get_type_name();
		UvegHazTypeSupport.register_type(participant, uvegHazTypeName);
		
		// Creating the actuator topic
		Topic actuatorTopic = participant.create_topic("window", uvegHazTypeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (actuatorTopic == null) {
			System.err.println("Unable to create topic.");
			return;
		}
		// Creating the data reading topic
		// TODO the name will have to be changed
		Topic readDataTopic = participant.create_topic("humidity", uvegHazTypeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (readDataTopic == null) {
			System.err.println("Unable to create topic.");
			return;
		}
		// TESTING For dummy data
		dataWriter = (UvegHazDataWriter) participant.create_datawriter(
				readDataTopic, Publisher.DATAWRITER_QOS_USE_TOPIC_QOS, null, StatusKind.DATA_AVAILABLE_STATUS);
		if (dataWriter == null) {
			System.err.println("Unable to create DDS writer.");
			return;
		}
		// Creating the actuator handler
		UvegHazDataWriter actuatorHandler = (UvegHazDataWriter) participant.create_datawriter(
				actuatorTopic, Publisher.DATAWRITER_QOS_USE_TOPIC_QOS, null, StatusKind.DATA_AVAILABLE_STATUS);
		if (dataWriter == null) {
			System.err.println("Unable to create DDS writer.");
			return;
		}
		ActuatorCommandPublisher actuatorCommandPublisher = new ActuatorCommandPublisher(actuatorHandler);
		// Creating the data reader
		gasConcentrationSubscriber = new GasConcentrationSubscriber(actuatorCommandPublisher);
		gasConcentrationReader = (UvegHazDataReader) participant.create_datareader(readDataTopic,
				Subscriber.DATAREADER_QOS_USE_TOPIC_QOS, gasConcentrationSubscriber, StatusKind.DATA_AVAILABLE_STATUS);
		if (gasConcentrationReader == null) {
			System.err.println("Unable to create DDS reader.");
			return;
		}
		// Testing the actuating commands
		UvegHazDataReader commandReader = (UvegHazDataReader) participant.create_datareader(actuatorTopic,
				Subscriber.DATAREADER_QOS_USE_TOPIC_QOS, new CommandReader(), StatusKind.DATA_AVAILABLE_STATUS);
	}
	
	private static void tearDownDdsInfrastructure() {
		gasConcentrationSubscriber.shutDown(); // Releasing the cloud client
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);		
	}
	
	// For testing
	public static class CommandReader extends DataReaderAdapter {
		public void on_data_available(DataReader reader) {
			UvegHazDataReader uvegHazReader = (UvegHazDataReader) reader;
			SampleInfo info = new SampleInfo();

			while (true) {
				try {
					UvegHaz receivedData = new UvegHaz();
					uvegHazReader.take_next_sample(receivedData, info);
					if (info.valid_data) {
						System.out.println("Actuating command arrived: " + receivedData);
					}
				} catch (RETCODE_NO_DATA e) {
					break;
				} catch (RETCODE_ERROR e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
