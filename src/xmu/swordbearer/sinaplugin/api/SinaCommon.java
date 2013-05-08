package xmu.swordbearer.sinaplugin.api;

import java.io.IOException;

import xmu.swordbearer.smallraccoon.util.NetUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.StatusesAPI;
import com.weibo.sdk.android.net.RequestListener;

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
	public static final int GET_STATUS_COMPLETE = 0x03;
	public static final int GET_STATUS_ERROR = 0x04;

	// 发送微博的结果：完成，错误，异常
	public static final int SEND_COMPLETE = 0x05;
	public static final int SEND_ERROR = 0x06;
	public static final int SEND_EXCEPTION = 0x07;

	/**
	 * 消息处理
	 * 
	 * @param handler
	 * @param obj
	 * @param msgWhat
	 */
	public static void handleMessage(Handler handler, Object obj, int msgWhat) {
		Message msg = new Message();
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

	/**
	 * 发送微博
	 * 
	 * @param context
	 * @param content文字内容
	 * @param file图片路径
	 *            (不发送图片则为null)
	 * @param handler
	 */
	public static void sendWeibo(final Context context, String content,
			String file, final Handler handler) {
		if (!NetUtil.isNetworkConnected(context)) {
			handleMessage(handler, "网络连接异常，无法发送微博", SinaCommon.SEND_EXCEPTION);
			return;
		}
		final ProgressDialog dialog = new ProgressDialog(context);
		dialog.setCancelable(false);
		dialog.setProgressStyle(0);
		dialog.setMessage("正在发送微博,请稍候...");
		RequestListener reslListener = new RequestListener() {
			public void onComplete(String response) {
				handleMessage(handler, null, SinaCommon.SEND_COMPLETE);
				dialog.dismiss();
			}

			public void onError(WeiboException exc) {
				handleMessage(handler, "微博发送失败！", SinaCommon.SEND_ERROR);
				dialog.dismiss();
			}

			public void onIOException(IOException ioex) {
				handleMessage(handler, "微博发送失败！", SinaCommon.SEND_EXCEPTION);
				dialog.dismiss();
			}
		};

		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
		if (!token.isSessionValid()) {
			handleMessage(handler, "账户登录异常，微博发送失败！", SinaCommon.SEND_ERROR);
			AccessTokenKeeper.clear(context);
			return;
		}

		dialog.show();
		StatusesAPI statusesAPI = new StatusesAPI(token);
		if (file == null) {
			statusesAPI.update(content, "00.00", "00.00", reslListener);
		} else {
			statusesAPI.upload(content, file, "00.00", "00.00", reslListener);
		}
	}
}
