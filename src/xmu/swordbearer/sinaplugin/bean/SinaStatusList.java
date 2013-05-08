package xmu.swordbearer.sinaplugin.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SinaStatusList implements Serializable {
	private static final String TAG = "SinaUserList";

	private int totalNumber = 0;
	private long firstId;// 第一条微博的ID
	private long lastId;// 最后一条微博的ID
	private ArrayList<SinaStatus> statuses = new ArrayList<SinaStatus>();

	/**
	 * 向尾部追加数据
	 * 
	 * @param statusList
	 * @throws JSONException
	 */
	public void append(String jsonStr) {
		SinaStatusList tempList = new SinaStatusList();
		try {
			tempList.fromJSON(jsonStr);
			if (tempList.getStatuses().size() > 0) {
				long lastID = tempList.getLastId();
				if (lastID < this.lastId)
					this.lastId = lastID;
				statuses.addAll(statuses.size(), tempList.getStatuses());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向头部添加数据
	 * 
	 * @param statusList
	 */
	public void preappend(String jsonStr) {
		SinaStatusList tempList = new SinaStatusList();
		try {
			tempList.fromJSON(jsonStr);
			if (tempList.getStatuses().size() > 0) {
				long firstID = tempList.getFirstId();
				if (firstID > this.firstId)
					this.firstId = firstID;
				statuses.addAll(0, tempList.getStatuses());
			}
		} catch (JSONException e) {
		}
	}

	private void fromJSON(String jsonStr) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonStr);
		JSONArray statusArray = jsonObject.getJSONArray("statuses");
		ArrayList<SinaStatus> newList = new ArrayList<SinaStatus>();
		for (int i = 0; i < statusArray.length(); i++) {
			JSONObject jsonStaus = statusArray.getJSONObject(i);
			newList.add(new SinaStatus(jsonStaus));
		}
		statuses.addAll(newList);
		totalNumber = jsonObject.getInt("total_number");
		if (statuses.size() > 0) {
			firstId = statuses.get(0).getId();
			lastId = statuses.get(statuses.size() - 1).getId();
		}
		Log.e(TAG, "totalNumber " + totalNumber);
		Log.e(TAG, "firstId " + firstId);
		Log.e(TAG, "lastId " + lastId);
	}

	public ArrayList<SinaStatus> getStatuses() {
		return statuses;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public long getFirstId() {
		return firstId;
	}

	public long getLastId() {
		return lastId;
	}

}
