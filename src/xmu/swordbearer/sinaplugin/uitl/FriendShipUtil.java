package xmu.swordbearer.sinaplugin.uitl;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import xmu.swordbearer.sinaplugin.api.AccessTokenKeeper;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import android.content.Context;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.AccountAPI;
import com.weibo.sdk.android.api.FriendshipsAPI;
import com.weibo.sdk.android.net.RequestListener;

public class FriendShipUtil {

	/**
	 * 根据uid来获取用户的粉丝
	 * 
	 * @param context
	 * @param listener
	 * @param cursor
	 */
	public static void getFollowers(Context context, long uid,
			final RequestListener listener, final int cursor) {
		Oauth2AccessToken toke = AccessTokenKeeper.readAccessToken(context);
		final FriendshipsAPI friendshipsAPI = new FriendshipsAPI(toke);
		friendshipsAPI.followers(uid, SinaCommon.PAGE_SIZE, cursor, false,
				listener);
	}

	/**
	 * 获取当前登录用户的粉丝 需要先获取用户的uid
	 * 
	 * @param context
	 * @param listener
	 * @param cursor
	 */
	public static void getMyFollowers(Context ctx,
			final RequestListener listener, final int cursor) {
		/*
		 * 如果已经存在了我的uid，则直接获取账号信息 ,否则，需要根据token先获得userId,然后根据UserId去得到帐号信息
		 */
		long my_uid = AccountUtil.readUid(ctx);
		if (my_uid != -1) {
			getFollowers(ctx, my_uid, listener, cursor);
		} else {
			Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(ctx);
			AccountAPI accountAPI = new AccountAPI(token);
			final FriendshipsAPI friendshipsAPI = new FriendshipsAPI(token);
			RequestListener uid_listener = new RequestListener() {
				public void onIOException(IOException arg0) {
				}

				public void onError(WeiboException arg0) {
				}

				public void onComplete(String response) {
					try {
						JSONObject json = new JSONObject(response);
						long uid = json.getLong("uid");
						friendshipsAPI.followers(uid, SinaCommon.PAGE_SIZE,
								cursor, false, listener);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			accountAPI.getUid(uid_listener);
		}
	}

	/**
	 * 获取我和对方的关系
	 * 
	 * @param context
	 * @param target_id对方的id
	 * @param listener
	 */
	public static void getRelationship(Context context, final long target_id,
			final RequestListener listener) {
		Oauth2AccessToken token = AccessTokenKeeper.readAccessToken(context);
		final FriendshipsAPI friendshipsAPI = new FriendshipsAPI(token);
		long my_uid = AccountUtil.readUid(context);
		if (my_uid != -1) {
			friendshipsAPI.show(my_uid, target_id, listener);
		} else {
			AccountAPI accountAPI = new AccountAPI(token);
			RequestListener uid_listener = new RequestListener() {
				public void onIOException(IOException arg0) {
				}

				public void onError(WeiboException arg0) {
				}

				public void onComplete(String response) {
					try {
						JSONObject json = new JSONObject(response);
						long uid = json.getLong("uid");
						friendshipsAPI.show(uid, target_id, listener);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			accountAPI.getUid(uid_listener);
		}
	}
}
