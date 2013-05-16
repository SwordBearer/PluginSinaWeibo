package xmu.swordbearer.sinaplugin.api;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * 新浪微博工具类，封装了一些方法
 * 
 * @author swordbearer
 * 
 */
public class SinaCommon {
	public static final String TAG = "sinaUtil";

	public static final String APP_KEY = "87052459";
	public static final String APP_SECRET = "2ba8e76bbd4dc3bfb2c17cde72c5c80d";
	public static final String REDIRECT_URL = "http://www.sina.com";

	public static final String AUTH_PREFERENCES_NAME = "us_sina_weibo_prefs";

	public static final String CACHE_PATH = "/sdcard/sinaplugin/cache";
	public static final String CACHE_KEY_PROFILE_IMG = "sina_profile_img.cache";
	public static final String CACHE_KEY_FOLLOWERS = "sina_followers.cache";

	/**
	 * 事件结果处理消息
	 */
	// 获取用户列表
	public static final int GET_USER_COMPLETE = 0x01;
	public static final int GET_USER_ERROR = 0x02;
	// 获取微博
	public static final int GET_MORE_STATUS_COMPLETE = 0x03;
	public static final int GET_REFRESH_STATUS_COMPLETE = 0x04;
	public static final int GET_STATUS_ERROR = 0x05;

	// 发送微博的结果：完成，错误，异常
	public static final int SEND_STATUS_COMPLETE = 0x06;
	public static final int SEND_STATUS_ERROR = 0x07;
	public static final int SEND_STATUS_EXCEPTION = 0x08;

	/**
	 * 消息处理
	 * 
	 * @param handler
	 * @param obj
	 * @param msgWhat
	 */
	public static void handleMessage(Handler handler, Object obj, int msgWhat) {
		Message msg = handler.obtainMessage();
		msg.what = msgWhat;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	/****************************** Account Manage ********************************/

	public static void getFans(Context context, final Handler handler) {
	}

	public static void getGuanzhus(Context context, final Handler handler) {
	}

	public static void getFavorites(Context context, final Handler handler) {
	}

	public static void getNewsOfUser(Context context, final Handler handler,
			long uid) {
	}

}
