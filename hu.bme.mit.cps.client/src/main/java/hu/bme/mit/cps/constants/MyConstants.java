package hu.bme.mit.cps.constants;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;

public class MyConstants {

	// Cloud specific constants
	public static final String CONNECTION_STRING = "HostName=cps-stream.azure-devices.net;DeviceId=myComputer;SharedAccessKey=NYVKBzttzHobH1fDJ77xgg==";
	public static final IotHubClientProtocol PROTOCOL = IotHubClientProtocol.HTTPS;
	public static final String DEVICE_ID = "myComputer";
	
	// TODO increase this value
	public static final int LESSON_CHECK_INTERVAL = 5 * 1000;
	
	// TODO specify the ranges
	public static final int WARNING_LEVEL_CLASS = 200;
	public static final int WARNING_LEVEL_NO_CLASS = 300;
	public static final int CRITICAL_LEVEL_CLASS = 500;
	public static final int CRITICAL_LEVEL_NO_CLASS = 700;
	
}
