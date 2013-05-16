package xmu.swordbearer.sinaplugin.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class SinaStatus extends BaseBean {
	private String created_at;
	private long id;
	private long mid;
	private String text;
	private String source;
	private boolean favorited;
	private String thumbnail_pic;
	private String bmiddle_pic;
	private String original_pic;
	private int reposts_count;
	private int comments_count;
	private int attitudes_count;
	private SinaUser user;
	private SinaStatus retweeted_status;

	private boolean isLiked = false;// 自定义属性：是否已经赞过了

	public SinaStatus(JSONObject json) throws JSONException {
		created_at = json.getString("created_at");
		id = json.getLong("id");
		mid = json.getLong("mid");
		text = json.getString("text");
		source = json.getString("source");
		favorited = json.getBoolean("favorited");
		if (json.has("thumbnail_pic")) {
			thumbnail_pic = json.getString("thumbnail_pic");
		} else {
			thumbnail_pic = null;
		}
		if (json.has("bmiddle_pic")) {
			bmiddle_pic = json.getString("bmiddle_pic");
		} else {
			bmiddle_pic = null;
		}
		if (json.has("original_pic")) {
			original_pic = json.getString("original_pic");
		} else {
			original_pic = null;
		}
		if (json.has("retweeted_status")) {
			retweeted_status = new SinaStatus(
					json.getJSONObject("retweeted_status"));
		} else {
			retweeted_status = null;
		}
		reposts_count = json.getInt("reposts_count");
		comments_count = json.getInt("comments_count");
		attitudes_count = json.getInt("attitudes_count");
		user = new SinaUser(json.getJSONObject("user"));
	}

	/**************** Getter ******************/
	public SinaUser getUser() {
		return user;
	}

	public String getCreated_at() {
		return created_at;
	}

	public long getId() {
		return id;
	}

	public long getMid() {
		return mid;
	}

	public String getText() {
		return text;
	}

	public String getSource() {
		return source;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public String getThumbnail_pic() {
		return thumbnail_pic;
	}

	public String getBmiddle_pic() {
		return bmiddle_pic;
	}

	public String getOriginal_pic() {
		return original_pic;
	}

	public int getReposts_count() {
		return reposts_count;
	}

	public int getComments_count() {
		return comments_count;
	}

	public int getAttitudes_count() {
		return attitudes_count;
	}

	public SinaStatus getRetweeted_status() {
		return retweeted_status;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
		if (isLiked) {
			this.attitudes_count += 1;
		} else {
			this.attitudes_count -= 1;
		}
	}
}
