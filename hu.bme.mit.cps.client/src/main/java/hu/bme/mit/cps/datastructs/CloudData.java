package hu.bme.mit.cps.datastructs;

import com.google.gson.Gson;

public class CloudData {
	
	public String id=  "" ; /* maximum length = (255) */
    public double value= 0;
    public int timeStamp= 0;
    public String level;

	public CloudData(UvegHaz uvegHaz, String level) {
		this.id = uvegHaz.ID;
		this.value = uvegHaz.Value;
		this.timeStamp = uvegHaz.TimeStamp;
		this.level = level;
	}
	
	public CloudData() {}
	
	public String serialize() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

}
