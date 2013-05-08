package xmu.swordbearer.sinaplugin.ui;

import xmu.swordbearer.sinaplugin.api.AccessTokenKeeper;
import xmu.swordbearer.sinaplugin.uitl.AccountUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

public class Start extends Activity {
	protected static final String TAG = "Start";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);//
		if (!AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
			Log.e(TAG, "auth()");
			AccountUtil.auth(this, authListener);
		} else {
			gotoHome();
		}
	}

	private void authErrorCheck() {
		Toast.makeText(this, "账号认证错误 ", Toast.LENGTH_LONG).show();
		finish();
	}

	private WeiboAuthListener authListener = new WeiboAuthListener() {
		@Override
		public void onWeiboException(WeiboException arg0) {
			authErrorCheck();
		}

		@Override
		public void onError(WeiboDialogError arg0) {
			authErrorCheck();
		}

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			Oauth2AccessToken oauth2AccessToken = new Oauth2AccessToken(token,
					expires_in);
			AccessTokenKeeper.keepAccessToken(Start.this, oauth2AccessToken);
			if (oauth2AccessToken.isSessionValid()) {
			} else {
				Toast.makeText(Start.this, "账号登录错误!", Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			gotoHome();
		}

		@Override
		public void onCancel() {
			finish();
		}
	};

	private void gotoHome() {
		Intent intent = new Intent(this, Home.class);
		startActivity(intent);
		finish();
	}
}
