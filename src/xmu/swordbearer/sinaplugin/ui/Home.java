package xmu.swordbearer.sinaplugin.ui;

import java.io.IOException;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.api.StatusUtil;
import xmu.swordbearer.sinaplugin.bean.SinaStatusAdapter;
import xmu.swordbearer.sinaplugin.bean.SinaStatusList;
import xmu.swordbearer.smallraccoon.util.NetUtil;
import xmu.swordbearer.smallraccoon.widget.LiveListView;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnMoreListener;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnRefreshListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 主页：显示关注的用户的微博
 * 
 * @author SwordBearer
 * 
 */
public class Home extends Activity implements android.view.View.OnClickListener {
	private static final String TAG = "Home";

	private LiveListView lvStatuses;
	private SinaStatusAdapter statusAdapter;
	private SinaStatusList statusList;

	private ImageButton btnNew;
	private ImageButton btnReload;
	private ImageView progressView;
	Animation progressAnim;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_home);
		Log.e(TAG, "FUCKFDFDSF");
		initViews();
	}

	private void initViews() {
		btnNew = (ImageButton) findViewById(R.id.home_btn_new);
		btnReload = (ImageButton) findViewById(R.id.home_btn_reload);
		progressView = (ImageView) findViewById(R.id.home_progressview);
		lvStatuses = (LiveListView) findViewById(R.id.home_listview);
		lvStatuses.isShowFooter(true);
		lvStatuses.isShowHeader(true);

		btnNew.setOnClickListener(this);
		btnReload.setOnClickListener(this);

		lvStatuses.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				reload();
			}
		});
		lvStatuses.setOnMoreListener(new OnMoreListener() {
			@Override
			public void onMore() {
				getMoreStatus();
			}
		});

		progressAnim = AnimationUtils.loadAnimation(this, R.anim.loading);
		statusList = new SinaStatusList();
		statusAdapter = new SinaStatusAdapter(this, statusList.getStatuses());
		lvStatuses.setAdapter(statusAdapter);
		//
		reload();
	}

	/**
	 * 加载更多
	 */
	private void getMoreStatus() {
		if (NetUtil.isNetworkConnected(this)) {
			showProgress(true);
			// -1 防止重复添加最后一条
			StatusUtil.getFriendsTimeline(this, 0, statusList.getMax_id() - 1, SinaCommon.PAGE_STATUS_SIZE, listenerMore);
		} else {
			Toast.makeText(this, "未连接到网络，无法获取最更多微博...", Toast.LENGTH_LONG).show();
			lvStatuses.onMoreComplete();
			showProgress(false);
		}
	}

	/**
	 * 加载最新微博
	 */
	private void reload() {
		lvStatuses.setSelection(0);
		showProgress(true);
		if (NetUtil.isNetworkConnected(this)) {
			StatusUtil.getFriendsTimeline(this, statusList.getSince_id(), 0, SinaCommon.PAGE_STATUS_SIZE, listenerRefresh);
		} else {
			Toast.makeText(this, "未连接到网络，无法获取最新微博...", Toast.LENGTH_LONG).show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					statusList.readCache(Home.this);
					handler.sendEmptyMessage(SinaCommon.GET_CACHED_STATUS_COMPLETE);
				}
			}).start();
		}
	}

	private void showProgress(final boolean isLoading) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (isLoading) {
					btnReload.setVisibility(View.GONE);
					progressView.setAnimation(progressAnim);
					progressView.setVisibility(View.VISIBLE);
				} else {
					btnReload.setVisibility(View.VISIBLE);
					progressView.setVisibility(View.GONE);
					progressView.clearAnimation();
				}
			}
		});
	}

	/**
	 * 提示有多少条最新微博
	 */
	private void showNewStatusCount(int newCount) {
		String str = "";
		if (newCount > 0) {
			str = newCount + " 条最新微博";
		} else {
			str = "暂时没有最新微博";
		}
		Toast toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
		toast.show();
	}

	private void updateList() {
		statusAdapter.notifyDataSetChanged();
		lvStatuses.onRefreshComplete();
		lvStatuses.onMoreComplete();
		showProgress(false);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == SinaCommon.GET_REFRESH_STATUS_COMPLETE) {
				Log.i(TAG, "获取最新数据完毕");
				updateList();
				// 显示最新条数
				showNewStatusCount(msg.arg1);
			} else if (msg.what == SinaCommon.GET_MORE_STATUS_COMPLETE) {
				updateList();
			} else if (msg.what == SinaCommon.GET_CACHED_STATUS_COMPLETE) {
				updateList();
			} else {
				super.handleMessage(msg);
			}
		}
	};

	private RequestListener listenerRefresh = new RequestListener() {
		@Override
		public void onIOException(IOException arg0) {}

		@Override
		public void onError(WeiboException arg0) {}

		@Override
		public void onComplete(String response) {
			int newCount = statusList.preappend(response);
			Message message = handler.obtainMessage();
			message.what = SinaCommon.GET_REFRESH_STATUS_COMPLETE;
			message.arg1 = newCount;
			handler.sendMessage(message);
		}
	};
	private RequestListener listenerMore = new RequestListener() {

		@Override
		public void onIOException(IOException arg0) {}

		@Override
		public void onError(WeiboException arg0) {}

		@Override
		public void onComplete(String response) {
			statusList.append(response);
			handler.sendEmptyMessage(SinaCommon.GET_MORE_STATUS_COMPLETE);
		}
	};

	@Override
	public void onClick(View view) {
		if (view == btnNew) {
			startActivity(new Intent(this, SendWeibo.class));
		} else if (view == btnReload) {
			reload();
		}
	}

	@Override
	public void onDestroy() {
		statusList.saveCache(this);
		super.onDestroy();
	}
}
