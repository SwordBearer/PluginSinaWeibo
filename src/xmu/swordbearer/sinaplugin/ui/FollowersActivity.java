package xmu.swordbearer.sinaplugin.ui;

import java.io.IOException;

import org.json.JSONException;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.bean.SinaFollowerAdapter;
import xmu.swordbearer.sinaplugin.bean.SinaFollowersList;
import xmu.swordbearer.sinaplugin.bean.SinaUser;
import xmu.swordbearer.sinaplugin.uitl.FriendShipUtil;
import xmu.swordbearer.smallraccoon.widget.LiveListView;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnMoreListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class FollowersActivity extends Activity implements
		android.view.View.OnClickListener {
	protected static final String TAG = "FollowersActivity";
	private ImageButton btnBack;
	private ImageButton btnHome;
	private Button btnCat;
	private LiveListView lvFollowers;
	private SinaFollowerAdapter adapter;
	private SinaFollowersList followersList;

	private long uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_followers);
		initView();
	}

	private void initView() {
		btnBack = (ImageButton) findViewById(R.id.followers_btn_back);
		btnHome = (ImageButton) findViewById(R.id.followers_btn_home);
		btnCat = (Button) findViewById(R.id.followers_cat);
		lvFollowers = (LiveListView) findViewById(R.id.followers_listview);

		Intent intent = getIntent();
		uid = intent.getLongExtra("uid", -1);
		if (uid == -1) {
			finish();
			return;
		}

		followersList = new SinaFollowersList();
		adapter = new SinaFollowerAdapter(this, followersList.getFollowers());
		lvFollowers.setAdapter(adapter);

		btnBack.setOnClickListener(this);
		btnHome.setOnClickListener(this);

		lvFollowers.setOnMoreListener(new OnMoreListener() {
			@Override
			public void onMore() {
				loadFollowersList();
			}
		});
		lvFollowers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> lisview, View item, int pos,
					long id) {
				SinaUser user = (SinaUser) adapter.getItem((int) id);
				Intent intent = new Intent(FollowersActivity.this,
						UserInfoActivity.class);
				intent.putExtra("uid", user.getId());
				startActivity(intent);
			}
		});

		loadFollowersList();
		lvFollowers.isShowHeader(false);
		lvFollowers.isShowFooter(true);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == SinaCommon.GET_FOLLOWER_COMPLETE) {
				adapter.notifyDataSetChanged();
				btnCat.setText("(" + followersList.getFollowers().size() + "/"
						+ followersList.getTotalNumber() + ")");
				lvFollowers.onMoreComplete();
				int nextCursor = followersList.getNextCursor();
				/*
				 * 新浪微博会自动过滤掉垃圾用户，所以一次返回的数据可能少于 PAGE_SIZE[40]
				 * 查询第一页时，nextCursor=0;查询至最后一页时,nextCursor也是0
				 */
				if (nextCursor == 0 && followersList.getFollowers().size() > 0) {
					lvFollowers.noMore();
					return;
				}
			} else if (msg.what == SinaCommon.GET_FOLLOWER_ERROR) {
				Toast.makeText(FollowersActivity.this, "获取粉丝失败...",
						Toast.LENGTH_LONG).show();
			} else {
				super.handleMessage(msg);
			}
		}
	};
	private RequestListener listener = new RequestListener() {
		@Override
		public void onIOException(IOException arg0) {
		}

		@Override
		public void onError(WeiboException arg0) {
		}

		@Override
		public void onComplete(String response) {
			System.out.println(response);
			try {
				followersList.fromJSON(response);
				Log.e(TAG, "粉丝数 " + followersList.getFollowers().size());
				handler.sendEmptyMessage(SinaCommon.GET_FOLLOWER_COMPLETE);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private void loadFollowersList() {
		Log.e(TAG, "loadFollowersList " + followersList.getNextCursor());
		// 获取uid的粉丝
		FriendShipUtil.getFollowers(this, uid, listener,
				followersList.getNextCursor());
	}

	@Override
	public void onClick(View view) {
		if (view == btnBack) {
			finish();
		} else if (view == btnHome) {
			Intent intent = new Intent(this, Start.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
