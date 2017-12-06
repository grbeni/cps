package hu.bme.mit.cps.dds;

import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.SampleInfo;

public class MySubscriber extends DataReaderAdapter {

	public void on_data_available(DataReader reader) {
		UvegHazDataReader uvegHazReader = (UvegHazDataReader) reader;
		SampleInfo info = new SampleInfo();

		while(true) {
			try {
				UvegHaz given = new UvegHaz();
				uvegHazReader.take_next_sample(given, info);
				if (info.valid_data) {
					System.out.println("Data arrived:");
					System.out.println(given.Value);
				}
			} catch (RETCODE_NO_DATA e) {
				break;
			} catch (RETCODE_ERROR e) {
				e.printStackTrace();
			}
		}

	}

}
