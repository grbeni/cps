package hu.bme.mit.cps.constants;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;

public class MyConstants {

	public static final String CONNECTION_STRING = "HostName=cps-stream.azure-devices.net;DeviceId=myComputer;SharedAccessKey=NYVKBzttzHobH1fDJ77xgg==";
	public static final IotHubClientProtocol PROTOCOL = IotHubClientProtocol.HTTPS;
	public static final String DEVICE_ID = "myComputer";
	
	// TODO increase this value
	public static final int LESSON_CHECK_INTERVAL = 5 * 1000;
	
	// TODO specify the ranges
	
}
