package hu.bme.mit.cps.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;

public class App {	
	
	private static String connString = "HostName=cps-stream.azure-devices.net;DeviceId=myComputer;SharedAccessKey=NYVKBzttzHobH1fDJ77xgg==";
	private static IotHubClientProtocol protocol = IotHubClientProtocol.HTTPS;
	private static String deviceId = "myComputer";
	private static DeviceClient client;
	
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
	
	private static class MessageSender implements Runnable {
	  private static final int SLEEP_TIME = 3000;

	public void run()  {
		try {
		  double minTemperature = 20;
		  double minHumidity = 60;
		  Random rand = new Random();

		  while (true) {
			double currentTemperature = minTemperature + rand.nextDouble() * 15;
			double currentHumidity = minHumidity + rand.nextDouble() * 20;
			CloudData telemetryDataPoint = new CloudData();
			telemetryDataPoint.deviceId = deviceId;
			telemetryDataPoint.temperature = currentTemperature;
			telemetryDataPoint.humidity = currentHumidity;

			String msgStr = telemetryDataPoint.serialize();
			Message msg = new Message(msgStr);
			msg.setProperty("temperatureAlert", (currentTemperature > 30) ? "true" : "false");
			msg.setMessageId(java.util.UUID.randomUUID().toString()); 
			System.out.println("Sending: " + msgStr);

			Object lockobj = new Object();
			EventCallback callback = new EventCallback();
			client.sendEventAsync(msg, callback, lockobj);

			synchronized (lockobj) {
			  lockobj.wait();
			}
			Thread.sleep(SLEEP_TIME);
		  }
		} catch (InterruptedException e) {
		  System.out.println("Finished.");
		}
	  }
	}
	
	public static void main(String[] args) throws IOException, URISyntaxException {
	  client = new DeviceClient(connString, protocol);
	  client.open();

	  MessageSender sender = new MessageSender();

	  ExecutorService executor = Executors.newFixedThreadPool(1);
	  executor.execute(sender);

	  System.out.println("Press ENTER to exit.");
	  System.in.read();
	  executor.shutdownNow();
	  client.closeNow();
	}
	
}
