package hu.bme.mit.cps.dds;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;

public class Main {
	
	private static UvegHazDataWriter dataWriter;
	private static UvegHazDataReader dataReader;
	private static DomainParticipant participant;
	
	public static void main(String[] args) {		
		// Creating the infrastructure
		createDdsInfrastructure();		
		
		// Writing data 
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		UvegHaz uvegHaz = new UvegHaz();
		uvegHaz.Value = 88.1;
		dataWriter.write(uvegHaz, InstanceHandle_t.HANDLE_NIL);

		System.out.println("Terminating.");
		tearDownDdsInfrastructure();
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
		// Creating the topic
		Topic topic = participant.create_topic("UvegHaz", uvegHazTypeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (topic == null) {
			System.err.println("Unable to create topic.");
			return;
		}
		dataWriter = (UvegHazDataWriter) participant.create_datawriter(
				topic, Publisher.DATAWRITER_QOS_USE_TOPIC_QOS, null, StatusKind.DATA_AVAILABLE_STATUS);
		if (dataWriter == null) {
			System.err.println("Unable to create DDS writer.");
			return;
		}
		dataReader = (UvegHazDataReader) participant.create_datareader(topic,
				Subscriber.DATAREADER_QOS_USE_TOPIC_QOS, new MySubscriber(), StatusKind.DATA_AVAILABLE_STATUS);
		if (dataReader == null) {
			System.err.println("Unable to create DDS reader.");
			return;
		}
	}
	
	private static void tearDownDdsInfrastructure() {
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);		
	}

	
}
