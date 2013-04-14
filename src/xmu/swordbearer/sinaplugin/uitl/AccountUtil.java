package xmu.swordbearer.sinaplugin.uitl;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import xmu.swordbearer.sinaplugin.api.AccessTokenKeeper;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.smallraccoon.util.NetUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.UsersAPI;
import com.weibo.sdk.android.net.RequestListener;

/**
 * @author swordbearer
 */
public class AccountUtil {
	public static final String TAG = "sinaUtil";

	/* 认证完成，取消，错误 */
	public static final int AUTH_COMPLETE = 0x01;
	public static final int AUTH_CANCEL = 0x02;
	public static final int AUTH_ERROR = 0x03;
	public static final int AUTH_EXCEPTION = 0x04;

	// 获取帐号的结果：完成，错误，异常
	public static final int ACCOUNT_COMPLETE = 0x08;
	public static final int ACCOUNT_ERRO = 0x09;
	public static final int ACCOUNT_EXCEPTION = 0x10;

	//

	/**/

	/**
	 * 检测是否已经登陆
	 * 
	 * @param context
	 * @param handler
	 */
	public static boolean checkIsLogined(Context context) {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
		if (!token.isSessionValid()) {// 如果认证失败，就清除账户信息
			AccessTokenKeeper.clear(context);
			return false;
		}
		return true;
	}

	/**
	 * 账户验证
	 * 
	 * @param context
	 * @param handler
	 */
	public static void auth(Context context, WeiboAuthListener listener) {
		if (NetUtil.isNetworkConnected(context)) {
			Weibo mWeibo = Weibo.getInstance(SinaCommon.APP_KEY,
					SinaCommon.REDIRECT_URL);
			mWeibo.authorize(context, listener);
		} else {
			// SinaCommon.handleMessage(handler, "网络未连接，无法登录", AUTH_ERROR);
		}
	}

	/**
	 * 保存当前登录用户的uid
	 * 
	 * @param context
	 * @param uid
	 * @return
	 */
	public static void saveUid(Context context, long uid) {
		SharedPreferences pref = context.getSharedPreferences(
				SinaCommon.PREFERENCES_NAME, Context.MODE_APPEND);
		Editor editor = pref.edit();
		editor.putLong("my_uid", uid);
		editor.commit();
	}

	public static long readUid(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				SinaCommon.PREFERENCES_NAME, Context.MODE_APPEND);
		return pref.getLong("my_uid", -1);
	}

	/**
	 * 获取我的账号信息
	 * 
	 * @param context
	 * @param handler消息处理句柄
	 */
	public static void getAccount(final Context context,
			final RequestListener listener) {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
		AccountAPI accountAPI = new AccountAPI(token);
		final UsersAPI usersAPI = new UsersAPI(token);

		/*
		 * 如果已经存在了我的uid，则直接获取账号信息 ,否则，需要根据token先获得userId,然后根据UserId去得到帐号信息
		 */
		long my_uid = readUid(context);
		if (my_uid != -1) {
			usersAPI.show(my_uid, listener);
		} else {
			Log.e(TAG, "没有保存uid");
			RequestListener uid_listener = new RequestListener() {
				public void onIOException(IOException arg0) {
				}

				public void onError(WeiboException arg0) {
				}

				public void onComplete(String response) {
					try {
						JSONObject json = new JSONObject(response);
						long uid = json.getLong("uid");
						Log.e("TEST", "uid " + uid);
						// 保存用户的uid
						saveUid(context, uid);
						// show 获得用户帐号
						usersAPI.show(uid, listener);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			accountAPI.getUid(uid_listener);
		}
	}

	/**
	 * 根据uid来获取用户的账户信息
	 * 
	 * @param context
	 * @param uid
	 * @param listener
	 */
	public static void getAccount(Context context, long uid,
			final RequestListener listener) {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
		final UsersAPI usersAPI = new UsersAPI(token);
		usersAPI.show(uid, listener);
	}
}