package hu.bme.mit.cps.dds;

import java.io.IOException;
import java.net.URISyntaxException;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;

import hu.bme.mit.cps.constants.MyConstants;
import hu.bme.mit.cps.datastructs.CloudData;

public class CloudDataSender {

	private DeviceClient client;

	public CloudDataSender() {
		try {
			client = new DeviceClient(MyConstants.CONNECTION_STRING, MyConstants.PROTOCOL);
			client.open();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private static class EventCallback implements IotHubEventCallback {
		public void execute(IotHubStatusCode status, Object context) {
			System.out.println("IoT Hub responded to message with status: " + status.name());
			if (context != null) {
				synchronized (context) {
					context.notify();
				}
			}
		}
	}

	public void send(CloudData cloudData, boolean isAlert) {
		try {
			// Setting the message
			String msgStr = cloudData.serialize();
			Message msg = new Message(msgStr);
			msg.setProperty("alert", (isAlert) ? "true" : "false");
			msg.setMessageId(java.util.UUID.randomUUID().toString());
			System.out.println("Sending: " + msgStr);
			// Sending the message
			Object lockobj = new Object();
			EventCallback callback = new EventCallback();
			client.sendEventAsync(msg, callback, lockobj);
			// Waiting for response
			synchronized (lockobj) {
				lockobj.wait();
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted.");
		}
	}

	public void close() {
		if (client != null) {
			try {
				client.closeNow();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
