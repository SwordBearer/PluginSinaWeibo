package xmu.swordbearer.sinaplugin.pin;

import java.io.IOException;
import java.util.List;

import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.api.StatusUtil;
import xmu.swordbearer.sinaplugin.bean.SinaStatusList;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 检测Pin的用户的最新状态
 * 
 * @author SwordBearer
 */
public class PinService extends Service {
	private static final String TAG = "WatchService";

	private SinaStatusList statusList;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			stopSelf();
			return START_NOT_STICKY;
		}
		// 防止定时检测功能关闭后仍然执行检测
		if (!PinHandler.isStarted(this)) {
			stopSelf();
			return START_NOT_STICKY;
		}
		// 获取最新的微博
		getPinListNews();

		return START_STICKY;
	}

	/**
	 * 获取观察列表的最新动态,耗时操作
	 */
	private void getPinListNews() {
		Log.e(TAG, "getPinListNews执行检测");
		/*
		 * 获得关注的列表 分别根据关注的用户的uid去获取他们的最新微博的ID
		 * @假设有10人，获得他们的uid后，依次去检测最新的微博
		 * @将最新的状态ID合并，依次去获取微博
		 * @如果有新的状态，则通知栏提醒用户查看，用户点开通知后，就能看到关注列表的用户的最新微博
		 */

		List<PinedUser> pinList = PinHandler.getPinList(this);
		statusList = new SinaStatusList();

		RequestListener requestListener = new RequestListener() {
			@Override
			public void onComplete(String response) {
				// 将其添加到statusList后面
				statusList.append(response);
			}

			@Override
			public void onError(WeiboException arg0) {}

			@Override
			public void onIOException(IOException arg0) {}
		};
		/* 遍历查询最新微博 */
		for (int i = 0; i < pinList.size(); i++) {
			PinedUser pinedUser = pinList.get(i);
			int count = 0;
			// 如果是刚加入的user，其lasted_id是-1，则只查询两条记录
			if (pinedUser.lastest_id == -1) {
				count = 2;
			} else {
				count = SinaCommon.PAGE_STATUS_SIZE;
			}
			StatusUtil.getUserTimeline(this, pinedUser.uid, pinedUser.lastest_id, 0, count, requestListener);
		}

		/**
		 * 执行检测完毕后,所有的最新数据都存放于 statusList中
		 */
		// 在通知栏提醒
		notifyNews();

		// 启动下一次检测任务
		PinHandler.startPinAction(PinService.this);
	}

	private void notifyNews() {
		int newCount = statusList.getStatuses().size();
		if (newCount == 0) {
			return;
		}

		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int notifyIcon = xmu.swordbearer.sinaplugin.R.drawable.sina_logo;
		String tickerText = "有最新微博";
		Notification notification = new Notification(notifyIcon, tickerText, System.currentTimeMillis());
		notification.defaults = Notification.DEFAULT_SOUND;// 提示音
		notification.defaults = Notification.DEFAULT_VIBRATE;// 震动
		notification.defaults = Notification.DEFAULT_LIGHTS;// 灯光闪烁
		notification.flags = Notification.FLAG_AUTO_CANCEL;// 点击后自动清除

		/* 点击后跳转 */
		Intent intent = new Intent(this, PinedUser.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

		String contentTitle = "有" + newCount + "条最新微博";
		String contentText = statusList.getStatuses().get(0).getText();
		notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
		nm.notify(0, notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
