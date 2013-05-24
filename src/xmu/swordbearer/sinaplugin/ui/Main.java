package xmu.swordbearer.sinaplugin.ui;

import xmu.swordbearer.sinaplugin.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 程序框架
 * 
 * @author SwordBearer
 */
public class Main extends TabActivity {
	private static final String TAB_ID_HOME = "tab_home";
	private static final String TAB_ID_ACCOUNTPREVIEW = "tab_accountpreview";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setContentView(R.layout.activity_main);
		initTabs();
	}

	private void initTabs() {
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_ID_ACCOUNTPREVIEW).setIndicator("首页", null).setContent(new Intent(this, Home.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_ID_HOME).setIndicator("账号", null).setContent(new Intent(this, AccountPreview.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_ID_HOME).setIndicator("设置", null).setContent(new Intent(this, Settings.class)));
		tabHost.addTab(tabHost.newTabSpec(TAB_ID_HOME).setIndicator("PinedUser", null).setContent(new Intent(this, PinedUserManage.class)));
	}
}
