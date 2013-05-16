package xmu.swordbearer.sinaplugin.bean;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SinaStatusList implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String TAG = "SinaUserList";

	private int totalNumber = 0;
	private long since_id;// 第一条微博的ID
	private long max_id;// 最后一条微博的ID
	private ArrayList<SinaStatus> statuses = new ArrayList<SinaStatus>();

	/**
	 * 向头部添加数据
	 * 
	 * @param jsonStr
	 */
	public int preappend(String jsonStr) {
		int newCount = 0;// 新微博的条数
		SinaStatusList tempList = new SinaStatusList();
		try {
			tempList.fromJSON(jsonStr);
			newCount = tempList.getStatuses().size();
			if (newCount > 0) {
				long sinceID = tempList.getSince_id();
				long maxID = tempList.getMax_id();
				if (sinceID > this.since_id)
					this.since_id = sinceID;
				// 如果之前的数据为空，则设置max_id为此数据的最后一条
				if (this.statuses.size() == 0)
					this.max_id = maxID;
				statuses.addAll(0, tempList.getStatuses());
			}
		} catch (JSONException e) {
		}
		return newCount;
	}

	/**
	 * 向尾部追加数据
	 * 
	 * @param jsonStr
	 * @throws JSONException
	 */
	public void append(String jsonStr) {
		SinaStatusList tempList = new SinaStatusList();
		try {
			tempList.fromJSON(jsonStr);
			if (tempList.getStatuses().size() > 0) {
				this.max_id = tempList.getMax_id();
				statuses.addAll(statuses.size(), tempList.getStatuses());
			}
		} catch (JSONException e) {
			e.printStackTrace();
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
		int statusesSize = statuses.size();
		if (statusesSize > 0) {
			this.since_id = statuses.get(0).getMid();
			this.max_id = statuses.get(statusesSize - 1).getMid();
		}
		Log.e(TAG, "totalNumber " + totalNumber);
		Log.e(TAG, "since_id " + since_id);
		Log.e(TAG, "max_id " + max_id);
	}

	public ArrayList<SinaStatus> getStatuses() {
		return statuses;
	}

	public int getTotalNumber() {
		return totalNumber;
	}

	public long getSince_id() {
		return since_id;
	}

	public long getMax_id() {
		return max_id;
	}

}
