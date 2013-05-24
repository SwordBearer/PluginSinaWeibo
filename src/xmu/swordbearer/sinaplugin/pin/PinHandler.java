package xmu.swordbearer.sinaplugin.pin;

import java.util.ArrayList;
import java.util.List;

import xmu.swordbearer.sinaplugin.bean.SinaUser;
import xmu.swordbearer.sinaplugin.db.DBHelper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.util.Log;

/**
 * 负责处理关注列表的更新状况
 * 
 * @author SwordBearer
 * 
 */
public class PinHandler {
	private static final String TAG = "WatchHandler";

	public static final String ACTION_START_PIN = "xmu.swordbearer.sinaplugin.action_start_pin";
	public static final String ACTION_STOP_PIN = "xmu.swordbearer.sinaplugin.action_stop_pin";
	public static final String ACTION_CHECK_FINISHED = "xmu.swordbearer.sinaplugin.action_check_finished";
	//
	private static final String PREF_PIN_INTERVAL = "pref_check_interval";
	private static final String KEY_PIN_INTERVAL = "key_check_interval";
	private static final String KEY_IS_STARTED = "key_is_started";
	private static final int DEF_PIN_INTERVAL = 5 * 60 * 1000;// 5 minutes

	/**
	 * 执行检测
	 * 
	 * @param context
	 */
	public static void startPinService(Context context) {
		Intent watchService = new Intent(ACTION_START_PIN);
		context.startService(watchService);
	}

	public static boolean isStarted(Context context) {
		SharedPreferences pref = context.getSharedPreferences(PREF_PIN_INTERVAL, Context.MODE_PRIVATE);
		// 检测是否已经开启定时检测功能
		boolean isStarted = pref.getBoolean(KEY_IS_STARTED, false);
		if (isStarted) {
			return true;
		}
		return false;
	}

	/**
	 * 启动定时检测
	 * 
	 * @param context
	 */
	public static void startPinAction(Context context) {
		// 如果已经关闭定时检测，则不执行下一次检测任务
		if (!isStarted(context))
			return;
		Log.e(TAG, "启动定时检测计划");
		SharedPreferences pref = context.getSharedPreferences(PREF_PIN_INTERVAL, Context.MODE_PRIVATE);
		long atTimeMillis = pref.getLong(KEY_PIN_INTERVAL, -1);
		if (atTimeMillis == -1) {
			atTimeMillis = DEF_PIN_INTERVAL;
		}
		Intent intent = new Intent(ACTION_START_PIN);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, atTimeMillis, sender);
	}

	/**
	 * 取消定时检测
	 * 
	 * @param context
	 */
	public static void stopPinAction(Context context) {
		Log.e(TAG, "取消定时检测计划");
		// Intent intent = new Intent(ACTION_START_WATCH);
		// PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent,
		// PendingIntent.FLAG_CANCEL_CURRENT);
		// AlarmManager am = (AlarmManager) context
		// .getSystemService(Context.ALARM_SERVICE);
		// am.cancel(sender);
		//
		SharedPreferences pref = context.getSharedPreferences(PREF_PIN_INTERVAL, Context.MODE_PRIVATE);
		Editor editor = pref.edit();
		editor.putBoolean(KEY_IS_STARTED, false);
		editor.commit();
	}

	/**
	 * 
	 */
	public static void addPinedUser(Context context, SinaUser user) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.KEY_PINLIST_UID, user.getId());
		String name = user.getRemark();
		if (name == null) {
			name = user.getScreen_name();
		}
		values.put(DBHelper.KEY_PINLIST_NAME, name);
		values.put(DBHelper.KEY_PINLIST_LATESTID, -1);// 刚加入时，默认为-1

		DBHelper dbHelper = new DBHelper(context);
		dbHelper.open();
		if (dbHelper.add(DBHelper.TBL_PINLIST, values)) {
		} else {
			// 添加失败
		}
		dbHelper.close();
	}

	public static void removePinedUser(Context context, long id) {
		DBHelper dbHelper = new DBHelper(context);
		dbHelper.open();
		if (dbHelper.delete(DBHelper.TBL_PINLIST, id)) {
		} else {
			// 移除失败
		}
		dbHelper.close();
	}

	/**
	 * 从数据库获取Pin的用户列表
	 */
	public static List<PinedUser> getPinList(Context context) {
		List<PinedUser> pinList = new ArrayList<PinedUser>();
		DBHelper dbHelper = new DBHelper(context);
		dbHelper.open();
		Cursor cursor = dbHelper.query(DBHelper.TBL_PINLIST);
		if (cursor.moveToFirst()) {
			pinList.add(new PinedUser(cursor));
			while (cursor.moveToNext()) {
				pinList.add(new PinedUser(cursor));
			}
		}
		cursor.close();
		dbHelper.close();

		return pinList;
	}

}
