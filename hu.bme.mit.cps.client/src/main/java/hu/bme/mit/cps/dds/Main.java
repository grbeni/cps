package hu.bme.mit.cps.dds;

import java.io.IOException;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
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
			dataWriter.write(uvegHaz, InstanceHandle_t.HANDLE_NIL);
			try {
				Thread.sleep(2000);
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
		// Creating the data reader
		ActuatorCommandPublisher actuatorCommandPublisher = new ActuatorCommandPublisher(actuatorHandler);
		gasConcentrationSubscriber = new GasConcentrationSubscriber(actuatorCommandPublisher);
		gasConcentrationReader = (UvegHazDataReader) participant.create_datareader(readDataTopic,
				Subscriber.DATAREADER_QOS_USE_TOPIC_QOS, gasConcentrationSubscriber, StatusKind.DATA_AVAILABLE_STATUS);
		if (gasConcentrationReader == null) {
			System.err.println("Unable to create DDS reader.");
			return;
		}
	}
	
	private static void tearDownDdsInfrastructure() {
		gasConcentrationSubscriber.shutDown(); // Releasing the cloud client
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);		
	}
	
}
