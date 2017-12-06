package hu.bme.mit.cps.client;

import com.google.gson.Gson;

import hu.bme.mit.cps.dds.UvegHaz;

public class CloudData {
	
	public String id=  "" ; /* maximum length = (255) */
    public double value= 0;
    public int timeStamp= 0;
    public String comment;

	public CloudData(UvegHaz uvegHaz, String comment) {
		this.id = uvegHaz.ID;
		this.value = uvegHaz.Value;
		this.timeStamp = uvegHaz.TimeStamp;
		this.comment = comment;
	}
	
	public CloudData() {}
	
	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
