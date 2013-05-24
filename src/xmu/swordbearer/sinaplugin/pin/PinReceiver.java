package xmu.swordbearer.sinaplugin.pin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 通知PinService进行检测
 * 
 * @author SwordBearers
 * 
 */
public class PinReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (PinHandler.ACTION_STOP_PIN.equals(intent.getAction())) {
			// 停止定时检测
			PinHandler.stopPinAction(context);
			return;
		} else if (PinHandler.ACTION_START_PIN.equals(intent.getAction())) {
			PinHandler.startPinService(context);
		}
	}
}
