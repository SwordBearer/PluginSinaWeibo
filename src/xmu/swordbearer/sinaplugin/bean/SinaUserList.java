package xmu.swordbearer.sinaplugin.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SinaUserList implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TAG = "SinaUserList";

	private int nextCursor = 0;// 下一页数据
	private int previousCursor = 0;
	private int totalNumber = 0;
	private ArrayList<SinaUser> users = new ArrayList<SinaUser>();

	public void fromJSON(String jsonStr) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonStr);
		JSONArray usersArray = jsonObject.getJSONArray("users");
		ArrayList<SinaUser> newList = new ArrayList<SinaUser>();
		for (int i = 0; i < usersArray.length(); i++) {
			JSONObject jsonUser = usersArray.getJSONObject(i);
			newList.add(new SinaUser(jsonUser));
		}
		users.addAll(newList);
		nextCursor = jsonObject.getInt("next_cursor");
		previousCursor = jsonObject.getInt("previous_cursor");
		totalNumber = jsonObject.getInt("total_number");
		Log.e(TAG, "nextCursor " + nextCursor);
		Log.e(TAG, "previousCursor " + previousCursor);
	}

	public ArrayList<SinaUser> getFollowers() {
		return users;
	}

	public int getNextCursor() {
		return nextCursor;
	}

	public int getPreviousCursor() {
		return previousCursor;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	@Deprecated
	public void getMore() {
	}

	// /**
	// * 缓存
	// */
	// public void saveToCache() {
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// CacheUtil.saveCacheSerializable2SD(SinaCommon.CACHE_PATH,
	// SinaCommon.CACHE_KEY_FOLLOWERS, this);
	// }
	// }).start();
	//
	// }
	//
	// public static SinaUserList fromCache() {
	// Object object = CacheUtil.readCacheSerializableInSD(
	// SinaCommon.CACHE_PATH, SinaCommon.CACHE_KEY_FOLLOWERS);
	// if (object != null) {
	// return (SinaUserList) object;
	// }
	// return new SinaUserList();
	// }
}
