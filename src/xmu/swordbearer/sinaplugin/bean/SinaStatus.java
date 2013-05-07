package xmu.swordbearer.sinaplugin.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class SinaStatus extends BaseBean {
	public String created_at;
	public long id;
	public String text;
	public boolean favorited;
	private String thumbnail_pic;
	private String bmiddle_pic;
	private String original_pic;
	private int reposts_count;
	private int comments_count;
	private int attitudes_count;

	public static SinaStatus friendship_follower(JSONObject json)
			throws JSONException {
		SinaStatus status = new SinaStatus();
		status.created_at = json.getString("created_at");
		status.id = json.getLong("id");
		status.text = json.getString("text");
		status.favorited = json.getBoolean("favorited");
		status.thumbnail_pic = json.getString("thumbnail_pic");
		status.bmiddle_pic = json.getString("bmiddle_pic");
		status.original_pic = json.getString("original_pic");
		return status;
	}
}
