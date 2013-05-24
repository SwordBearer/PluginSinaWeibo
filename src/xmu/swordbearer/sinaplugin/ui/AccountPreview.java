package xmu.swordbearer.sinaplugin.ui;

import java.io.IOException;

import org.json.JSONException;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.AccountUtil;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.app.SinaWeiboApp;
import xmu.swordbearer.sinaplugin.bean.SinaUser;
import xmu.swordbearer.smallraccoon.cache.CacheUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class AccountPreview extends Activity implements View.OnClickListener {
	protected static final String TAG = "PreviewActivity";
	private SinaUser user;
	//
	private View previewContainer;
	private ProgressBar progressBar;
	private ImageView ivImg;
	private TextView tvName;
	private TextView tvDesc;

	private ImageButton btnRefresh;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_account_preview);
		initViews();
		//
		// if (!AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
		// Log.e(TAG, "auth()");
		// AccountUtil.auth(this, authListener);
		// } else {
		// Log.e(TAG, "getAccount()");
		// loadWatchList();
		// }
	}

	private void initViews() {
		previewContainer = (View) findViewById(R.id.preview_container);
		ivImg = (ImageView) findViewById(R.id.preview_img);
		progressBar = (ProgressBar) findViewById(R.id.preview_progressbar);
		tvName = (TextView) findViewById(R.id.preview_name);
		tvDesc = (TextView) findViewById(R.id.preview_desc);
		btnRefresh = (ImageButton) findViewById(R.id.preview_btn_refresh);

		user = (SinaUser) CacheUtil.readCache(this, SinaCommon.CACEH_KEY_CUR_ACCOUNT);
		if (user != null) {
			updateAccountView();
		} else {
			AccountUtil.getAccount(this, requestListener);
		}
		//
		btnRefresh.setOnClickListener(this);
		previewContainer.setOnClickListener(this);
	}

	private RequestListener requestListener = new RequestListener() {
		@Override
		public void onIOException(IOException arg0) {
			finish();
		}

		@Override
		public void onError(WeiboException arg0) {
			finish();
		}

		@Override
		public void onComplete(String response) {
			if (response != null && !response.equals("")) {
				try {
					user = SinaUser.fromJSON(response);
					// 获得帐号后，通知AccountInfo去更新数据
					updateAccountView();
					saveAccount2Cache();
				} catch (JSONException e) {
					Toast.makeText(AccountPreview.this, "获取账号错误！", Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}
	};

	/**
	 * 更新账户界面
	 */
	private void updateAccountView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				SinaWeiboApp.loadImage(user.getProfile_image_url(), ivImg, true);
				tvName.setText(user.getName());
				tvDesc.setText(user.getDescription());
				tvName.setVisibility(View.VISIBLE);
				tvDesc.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);

				previewContainer.setFocusable(true);
				previewContainer.setClickable(true);
			}
		});
	}

	/**
	 * 将账户数据缓存
	 */
	private void saveAccount2Cache() {
		if (user != null)
			CacheUtil.saveCache(this, SinaCommon.CACEH_KEY_CUR_ACCOUNT, user);
	}

	private void gotoDetails() {
		Intent intent = new Intent(this, AccountInfo.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		intent.putExtra("cur_user", user);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == previewContainer) {
			gotoDetails();
		} else if (v == btnRefresh) {
			AccountUtil.getAccount(this, requestListener);
		}
	}
}
