package xmu.swordbearer.sinaplugin.ui;

import java.io.IOException;

import org.json.JSONException;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.bean.SinaStatusAdapter;
import xmu.swordbearer.sinaplugin.bean.SinaStatusList;
import xmu.swordbearer.sinaplugin.uitl.StatusUtil;
import xmu.swordbearer.smallraccoon.widget.LiveListView;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnMoreListener;
import xmu.swordbearer.smallraccoon.widget.LiveListView.OnRefreshListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

public class Home extends Activity implements android.view.View.OnClickListener {
	private LiveListView lvStatuses;
	private SinaStatusAdapter statusAdapter;
	private SinaStatusList statusList;

	//

	private ImageButton btnNew;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_home);

		initView();
	}

	private void initView() {
		btnNew = (ImageButton) findViewById(R.id.home_btn_new);
		lvStatuses = (LiveListView) findViewById(R.id.home_listview);
		lvStatuses.isShowFooter(true);
		lvStatuses.isShowHeader(true);

		btnNew.setOnClickListener(this);

		lvStatuses.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				loadRefresh();
			}
		});
		lvStatuses.setOnMoreListener(new OnMoreListener() {
			@Override
			public void onMore() {
				loadMore();
			}
		});

		statusList = new SinaStatusList();
		statusAdapter = new SinaStatusAdapter(this, statusList.getStatuses());
		lvStatuses.setAdapter(statusAdapter);

		loadRefresh();
	}

	private void loadMore() {
		// +1 防止重复添加最后一条
		StatusUtil.getFriendsTimeline(this, 0, statusList.getLastId() + 1,
				listenerMore);
	}

	private void loadRefresh() {
		StatusUtil.getFriendsTimeline(this, statusList.getFirstId(), 0,
				listenerRefresh);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == SinaCommon.GET_STATUS_COMPLETE) {
				statusAdapter.notifyDataSetChanged();
				lvStatuses.onRefreshComplete();
				lvStatuses.onMoreComplete();
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
			System.out.println(response);
			statusList.preappend(response);
			handler.sendEmptyMessage(SinaCommon.GET_STATUS_COMPLETE);

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
			System.out.println(response);
			statusList.append(response);
			handler.sendEmptyMessage(SinaCommon.GET_STATUS_COMPLETE);
		}
	};

	@Override
	public void onClick(View view) {
		if (view == btnNew) {
			startActivity(new Intent(this, SendWeibo.class));
		}
	}
}
