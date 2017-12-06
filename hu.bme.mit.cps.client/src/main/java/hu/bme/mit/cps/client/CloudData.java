package hu.bme.mit.cps.client;

import com.google.gson.Gson;

public class CloudData {

	public String deviceId;
	public double temperature;
	public double humidity;

	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
