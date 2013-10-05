package xmu.swordbearer.sinaplugin.ui;

import java.util.ArrayList;
import java.util.List;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.app.SinaWeiboApp;
import xmu.swordbearer.sinaplugin.bean.SinaUser;
import xmu.swordbearer.sinaplugin.pin.PinHandler;
import xmu.swordbearer.sinaplugin.pin.PinedUser;
import xmu.swordbearer.smallraccoon.cache.CacheUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 管理我的PinedUser
 * 
 * @author SwordBearer
 * 
 */
public class PinedUserManage extends Activity implements OnClickListener {
	private ImageButton btnBack;
	private ImageButton btnAdd;
	private ListView pinedUseListView;
	private List<PinedUser> pinedUserList = new ArrayList<PinedUser>();
	private PinedUserAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pineduser);

		btnBack = (ImageButton) findViewById(R.id.pineduser_btn_back);
		btnAdd = (ImageButton) findViewById(R.id.pineduser_btn_add);
		pinedUseListView = (ListView) findViewById(R.id.pineduser_listview);
		btnBack.setOnClickListener(this);
		btnAdd.setOnClickListener(this);

		pinedUseListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {}
		});

		pinedUserList = PinHandler.getPinList(this);
		adapter = new PinedUserAdapter(this);
		pinedUseListView.setAdapter(adapter);
	}

	/**
	 * 更新PinedUser列表
	 */
	private void updatePinedList() {
		pinedUserList = PinHandler.getPinList(this);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			finish();
		} else if (v == btnAdd) {
			showChooseDialog();
			// Intent goToChooseUser = new Intent(PinedUserManage.this, ChoosePinedUser.class);
			// startActivityForResult(goToChooseUser, Activity.RESULT_OK);
		}
	}

	private void showChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择添加方式");
		CharSequence[] types = { "关注列表", "粉丝列表", "搜索" };
		builder.setSingleChoiceItems(types, -1, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					Intent intent = new Intent(PinedUserManage.this, FriendsActivity.class);
					SinaUser user = (SinaUser) CacheUtil.readCache(PinedUserManage.this, SinaCommon.CACEH_KEY_CUR_ACCOUNT);
					intent.putExtra("uid", user.getId());
					startActivityForResult(intent, 1);
				} else if (which == 1) {
					Intent intent = new Intent(PinedUserManage.this, FollowersActivity.class);
					SinaUser user = (SinaUser) CacheUtil.readCache(PinedUserManage.this, SinaCommon.CACEH_KEY_CUR_ACCOUNT);
					intent.putExtra("uid", user.getId());
					startActivityForResult(intent, 1);
				} else if (which == 2) {
				}
				dialog.cancel();
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				updatePinedList();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	private class PinedUserAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		class PinedUserItem {
			ImageView avater;
			TextView name;
			TextView desc;
			ImageButton btnRemove;
		}

		public PinedUserAdapter(Context context) {
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return pinedUserList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return pinedUserList.get(arg0);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View container, ViewGroup group) {
			PinedUser pinedUser = pinedUserList.get(pos);
			PinedUserItem item;
			if (container == null) {
				container = inflater.inflate(R.layout.list_item_pineduser, null);
				item = new PinedUserItem();
				item.avater = (ImageView) container.findViewById(R.id.pineduser_listitem_avatar);
				item.name = (TextView) container.findViewById(R.id.pineduser_listitem_name);
				item.btnRemove = (ImageButton) container.findViewById(R.id.pineduser_listitem_remove);
			} else {
				item = (PinedUserItem) container.getTag();
			}
			SinaWeiboApp.loadImage(pinedUser.getProfile_image_url(), item.avater, false);
			item.name.setText(pinedUser.getName());
			container.setTag(item);
			return container;
		}
	}
}
