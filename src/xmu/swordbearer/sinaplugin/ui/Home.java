package xmu.swordbearer.sinaplugin.ui;

import java.io.IOException;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.api.StatusUtil;
import xmu.swordbearer.sinaplugin.bean.SinaStatusAdapter;
import xmu.swordbearer.sinaplugin.bean.SinaStatusList;
import xmu.swordbearer.smallraccoon.widget.LiveListView;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnMoreListener;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnRefreshListener;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class Home extends Activity implements android.view.View.OnClickListener {
	private static final String TAG = "Home";
	private LiveListView lvStatuses;
	private SinaStatusAdapter statusAdapter;
	private SinaStatusList statusList;

	private ImageButton btnNew;
	private ImageButton btnReload;
	private ImageView progressView;
	private AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_home);

		initView();
	}

	private void initView() {
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

		statusList = new SinaStatusList();
		statusAdapter = new SinaStatusAdapter(this, statusList.getStatuses());
		lvStatuses.setAdapter(statusAdapter);

		reload();
	}

	/**
	 * 加载更多
	 */
	private void getMoreStatus() {
		showProgress(true);
		// -1 防止重复添加最后一条
		StatusUtil.getFriendsTimeline(this, 0, statusList.getMax_id() - 1,
				listenerMore);
	}

	/**
	 * 加载最新微博
	 */
	private void reload() {
		showProgress(true);
		StatusUtil.getFriendsTimeline(this, statusList.getSince_id(), 0,
				listenerRefresh);
	}

	private void showProgress(final boolean isLoading) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				animationDrawable = (AnimationDrawable) progressView
						.getDrawable();
				if (isLoading) {
					btnReload.setVisibility(View.GONE);
					progressView.setVisibility(View.VISIBLE);
					animationDrawable.start();
				} else {
					btnReload.setVisibility(View.VISIBLE);
					progressView.setVisibility(View.GONE);
					animationDrawable.stop();
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
				updateList();
				// 显示最新条数
				showNewStatusCount(msg.arg1);
			} else if (msg.what == SinaCommon.GET_MORE_STATUS_COMPLETE) {
				updateList();
			} else {
				super.handleMessage(msg);
			}
		}
	};

	private RequestListener listenerRefresh = new RequestListener() {
		@Override
		public void onIOException(IOException arg0) {
		}

		@Override
		public void onError(WeiboException arg0) {
		}

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
		public void onIOException(IOException arg0) {
		}

		@Override
		public void onError(WeiboException arg0) {
		}

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
			lvStatuses.setSelection(0);
			reload();
		}
	}
}
