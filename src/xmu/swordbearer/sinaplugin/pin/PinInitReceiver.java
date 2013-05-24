package xmu.swordbearer.sinaplugin.pin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 开机启动定时检测计划
 * 
 * @author SwordBearers
 * 
 */
public class PinInitReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
			// 如果开启了自动监测，则启动检测任务
			PinHandler.startPinAction(context);
		}
	}
}
