package hu.bme.mit.cps;

import java.io.BufferedReader;
import java.util.Scanner;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.*;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringDataWriter;
import com.rti.dds.type.builtin.StringTypeSupport;

public class MyPublisher {
	
	private static boolean shutdownFlag = false;
	
	public static void main(String[] args) {		
		DomainParticipant participant = DomainParticipantFactory.get_instance().create_participant(
				0, DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (participant == null) {
			System.err.println("Unable to create domain participant.");
			return;
		}
		
		String uvegHazTypeName = UvegHazTypeSupport.get_type_name();
		UvegHazTypeSupport.register_type(participant, uvegHazTypeName);
		
		Topic topic = participant.create_topic("UvegHaz", uvegHazTypeName,
				DomainParticipant.TOPIC_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
		if (topic == null) {
			System.err.println("Unable to create topic.");
			return;
		}
		// Creating a writer
		UvegHazDataWriter dataWriter = (UvegHazDataWriter) participant.create_datawriter(
				topic, Publisher.DATAWRITER_QOS_USE_TOPIC_QOS, null, StatusKind.DATA_AVAILABLE_STATUS);
		if (dataWriter == null) {
			System.err.println("Unable to create DDS writer.");
			return;
		}
		// Creating a reader
		UvegHazDataReader dataReader = (UvegHazDataReader) participant.create_datareader(topic,
				Subscriber.DATAREADER_QOS_USE_TOPIC_QOS, new MySubscriber(), StatusKind.DATA_AVAILABLE_STATUS);
		if (dataReader == null) {
			System.err.println("Unable to create DDS reader.");
			return;
		}
		
		System.out.println("Ready to write data.");
		System.out.println("Press Ctrl + C to terminate.");
		// Writing data 
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		UvegHaz uvegHaz = new UvegHaz();
		uvegHaz.Value = 88.1;
		dataWriter.write(uvegHaz, InstanceHandle_t.HANDLE_NIL);

		System.out.println("Terminating.");
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);	
		
	}

	
}
