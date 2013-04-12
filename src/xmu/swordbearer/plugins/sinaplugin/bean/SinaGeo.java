package xmu.swordbearer.plugins.sinaplugin.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class SinaGeo {
	private String longitude;
	private String latitude;
	private String city;
	private String province;
	private String city_name;
	private String province_name;
	private String address;

	public SinaGeo(String jsonStr) throws JSONException {
		JSONObject json = new JSONObject(jsonStr);
	}
}
