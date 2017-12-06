package hu.bme.mit.cps.constants;

import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;

public class MyConstants {

	public static final String CONNECTION_STRING = "HostName=cps-stream.azure-devices.net;DeviceId=myComputer;SharedAccessKey=NYVKBzttzHobH1fDJ77xgg==";
	public static final IotHubClientProtocol PROTOCOL = IotHubClientProtocol.HTTPS;
	public static final String DEVICE_ID = "myComputer";
	
}
