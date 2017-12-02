package hu.bme.mit.cps;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.*;
import com.rti.dds.topic.Topic;
import com.rti.dds.type.builtin.StringDataReader;
import com.rti.dds.type.builtin.StringTypeSupport;

public class MySubscriber extends DataReaderAdapter {

	private static boolean shutdownFlag = false;

	public static void main(String[] args) {
		DomainParticipant participant = DomainParticipantFactory.get_instance().create_participant(0,
				DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, null, StatusKind.STATUS_MASK_NONE);
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

		UvegHazDataReader dataReader = (UvegHazDataReader) participant.create_datareader(topic,
				Subscriber.DATAREADER_QOS_USE_TOPIC_QOS, new MySubscriber(), StatusKind.DATA_AVAILABLE_STATUS);
		if (dataReader == null) {
			System.err.println("Unable to create DDS reader.");
			return;
		}

		System.out.println("Ready to read data.");
		System.out.println("Press Ctrl + C to terminate.");

		while (!shutdownFlag) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("Terminating.");
		participant.delete_contained_entities();
		DomainParticipantFactory.get_instance().delete_participant(participant);

	}

	public void on_data_available(DataReader reader) {
		UvegHazDataReader uvegHazReader = (UvegHazDataReader) reader;
		SampleInfo info = new SampleInfo();

		for (;;) {
			try {
				UvegHaz given = new UvegHaz();
				uvegHazReader.take_next_sample(given, info);
				if (info.valid_data) {
					System.out.println("LOLGEC");
					System.out.println(given.Value);
					if (given.Value == -1.0) {
						shutdownFlag = true;
					}
				}
			} catch (RETCODE_NO_DATA e) {
				break;
			} catch (RETCODE_ERROR e) {
				e.printStackTrace();
			}
		}

	}

}
