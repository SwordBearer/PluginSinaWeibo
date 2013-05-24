package xmu.swordbearer.sinaplugin.pin;

import java.io.Serializable;

import android.database.Cursor;

public class PinedUser implements Serializable {
	private static final long serialVersionUID = 3246791237086612503L;
	//
	long _id;
	long uid;
	String name;
	String profile_image_url;// 头像地址
	long lastest_id;

	public PinedUser(Cursor cursor) {
		this._id = cursor.getLong(0);
		this.uid = cursor.getLong(1);
		this.name = cursor.getString(2);
		this.profile_image_url = cursor.getString(3);
		this.lastest_id = cursor.getLong(4);
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public long getLastest_id() {
		return lastest_id;
	}

	public void setLastest_id(long lastest_id) {
		this.lastest_id = lastest_id;
	}
}
