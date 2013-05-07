package xmu.swordbearer.sinaplugin.ui;

import java.io.IOException;

import org.json.JSONException;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.bean.SinaUser;
import xmu.swordbearer.sinaplugin.bean.SinaUserList;
import xmu.swordbearer.sinaplugin.bean.SinaUsersAdapter;
import xmu.swordbearer.smallraccoon.widget.LiveListView;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnMoreListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public abstract class SinaUserListActivity extends Activity implements
		android.view.View.OnClickListener {
	protected static final String TAG = "SinaUserListActivity";
	protected LiveListView lvUsers;
	protected SinaUserList userList;
	protected SinaUsersAdapter userAdapter;

	private ImageButton btnBack;
	private ImageButton btnHome;
	private Button btnCat;

	protected long uid = -1;

	public abstract void loadMore();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_followers);
		initView();
	}

	protected void initView() {
		btnBack = (ImageButton) findViewById(R.id.followers_btn_back);
		btnHome = (ImageButton) findViewById(R.id.followers_btn_home);
		btnCat = (Button) findViewById(R.id.followers_cat);
		lvUsers = (LiveListView) findViewById(R.id.followers_listview);
		lvUsers.isShowFooter(true);

		Intent intent = getIntent();
		uid = intent.getLongExtra("uid", -1);
		Toast.makeText(SinaUserListActivity.this, "当前用户的Id 是 " + uid,
				Toast.LENGTH_LONG).show();
		if (uid == -1) {
			finish();
			return;
		}

		userList = new SinaUserList();

		btnBack.setOnClickListener(this);
		btnHome.setOnClickListener(this);
		lvUsers.setOnMoreListener(new OnMoreListener() {
			@Override
			public void onMore() {
				loadMore();
			}
		});
		lvUsers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> lisview, View item, int pos,
					long id) {
				SinaUser user = (SinaUser) userAdapter.getItem((int) id);
				Intent intent = new Intent(SinaUserListActivity.this,
						AccountInfo.class);
				intent.putExtra("cur_user", user);
				startActivity(intent);
			}
		});
	}

	protected Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == SinaCommon.GET_FOLLOWER_COMPLETE) {
				userAdapter.notifyDataSetChanged();
				lvUsers.onMoreComplete();

				btnCat.setText("(" + userList.getFollowers().size() + "/"
						+ userList.getTotalNumber() + ")");
				int nextCursor = userList.getNextCursor();
				/*
				 * 新浪微博会自动过滤掉垃圾用户，所以一次返回的数据可能少于 PAGE_SIZE[40]
				 * 查询第一页时，nextCursor=0;查询至最后一页时,nextCursor也是0
				 */
				if (nextCursor == 0 && userList.getFollowers().size() > 0) {
					lvUsers.noMore();
					return;
				}
			} else if (msg.what == SinaCommon.GET_FOLLOWER_ERROR) {
				Toast.makeText(SinaUserListActivity.this, "获取数据失败...",
						Toast.LENGTH_LONG).show();
			} else {
				super.handleMessage(msg);
			}
		}
	};

	protected RequestListener listener = new RequestListener() {
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
				userList.fromJSON(response);
				handler.sendEmptyMessage(SinaCommon.GET_FOLLOWER_COMPLETE);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

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
