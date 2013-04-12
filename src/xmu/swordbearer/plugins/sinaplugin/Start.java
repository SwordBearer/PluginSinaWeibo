package xmu.swordbearer.plugins.sinaplugin;

import java.io.IOException;

import org.json.JSONException;

import xmu.swordbearer.plugins.sinaplugin.api.AccessTokenKeeper;
import xmu.swordbearer.plugins.sinaplugin.bean.SinaUser;
import xmu.swordbearer.plugins.sinaplugin.uitl.AccountUtil;
import xmu.swordbearer.smallraccoon.widget.AsyncImageView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 预览当前登录用户的账号
 * 
 * @author SwordBearers
 * 
 */
public class Start extends Activity implements View.OnClickListener {
	protected static final String TAG = "PreviewActivity";
	private SinaUser user;
	//
	private View previewContainer;
	private ProgressBar progressBar;
	private AsyncImageView ivImg;
	private TextView tvName;
	private TextView tvDesc;

	private ImageButton btnBack;
	private ImageButton btnHome;

	private void initViews() {
		previewContainer = (View) findViewById(R.id.preview_container);
		ivImg = (AsyncImageView) findViewById(R.id.preview_img);
		progressBar = (ProgressBar) findViewById(R.id.preview_progressbar);
		tvName = (TextView) findViewById(R.id.preview_name);
		tvDesc = (TextView) findViewById(R.id.preview_desc);
		btnBack = (ImageButton) findViewById(R.id.preview_btn_back);
		btnHome = (ImageButton) findViewById(R.id.preview_btn_home);

		btnBack.setOnClickListener(this);
		btnHome.setOnClickListener(this);
		previewContainer.setOnClickListener(this);

	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_account_preview);
		initViews();
		//
		if (!AccessTokenKeeper.readAccessToken(this).isSessionValid()) {
			AccountUtil.auth(this, new Handler());
			return;
		}
		AccountUtil.getAccount(this, requestListener);
		loadWatchList();
	}

	private RequestListener requestListener = new RequestListener() {
		@Override
		public void onIOException(IOException arg0) {
			// Toast.makeText(Start.this, "获取账号错误！", Toast.LENGTH_SHORT)
			// .show();
			finish();
		}

		@Override
		public void onError(WeiboException arg0) {
			// Toast.makeText(Start.this, "获取账号错误！", Toast.LENGTH_SHORT)
			// .show();
			finish();
		}

		@Override
		public void onComplete(String response) {
			Log.e(TAG, "response " + response);
			if (response != null && !response.equals("")) {
				try {
					user = SinaUser.fromJSON(response);
					// 获得帐号后，通知AccountInfo去更新数据
					updateAccountView();
				} catch (JSONException e) {
					Toast.makeText(Start.this, "获取账号错误！", Toast.LENGTH_SHORT)
							.show();
					finish();
				}
			}
		}
	};

	private void updateAccountView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ivImg.loadImage(user.getProfile_image_url());
				tvName.setText(user.getName());
				tvDesc.setText(user.getDescription());

				tvName.setVisibility(View.VISIBLE);
				tvDesc.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);

			}
		});
	}

	private void loadWatchList() {
	}

	private void gotoDetails() {
		Intent intent = new Intent(this, AccountInfo.class);
		intent.putExtra("cur_user", user);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v == previewContainer) {
			gotoDetails();
		} else if (v == btnBack) {
			finish();
		} else if (v == btnHome) {
			Intent intent = new Intent(this, Start.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
