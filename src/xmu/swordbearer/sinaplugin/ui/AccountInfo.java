package xmu.swordbearer.sinaplugin.ui;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.app.SinaWeiboApp;
import xmu.swordbearer.sinaplugin.bean.SinaUser;
import xmu.swordbearer.sinaplugin.uitl.AccountUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我的个人主页
 * 
 * @author swordbearer
 */
public class AccountInfo extends Activity implements
		android.view.View.OnClickListener {
	protected static final String TAG = "AccountInfo";

	public static final int FRIENDSHIP_FOLLOWED_BY = 1;// 我被target关注
	public static final int FRIENDSHIP_FOLLOWING = 2;// 我关注了target
	public static final int FRIENDSHIP_FOLLOWED_EACHOTHER = 3;// 互粉

	private SinaUser user;
	//
	private ImageView ivImg;
	private TextView tvName;// 昵称
	private TextView tvAddr;
	private TextView tvDesc;// 个人描述
	private Button btnFollowers;// 粉丝
	private Button btnFriends;// 关注
	private Button btnStatuses;
	private Button btnFavourites;

	private Button btnEdit;
	private ImageButton btnNew;
	private ImageButton btnHome;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_account_info);
		//
		initViews();
	}

	private void initViews() {
		Intent intent = getIntent();
		user = (SinaUser) intent.getSerializableExtra("cur_user");
		if (user == null) {
			finish();
			return;
		}

		btnNew = (ImageButton) findViewById(R.id.account_btn_new);
		btnHome = (ImageButton) findViewById(R.id.account_btn_home);
		ivImg = (ImageView) findViewById(R.id.account_img);
		tvName = (TextView) findViewById(R.id.account_name);
		tvDesc = (TextView) findViewById(R.id.account_desc);
		tvAddr = (TextView) findViewById(R.id.account_address);
		btnFollowers = (Button) findViewById(R.id.account_followers);
		btnFriends = (Button) findViewById(R.id.account_friends);
		btnStatuses = (Button) findViewById(R.id.account_statuses);
		btnFavourites = (Button) findViewById(R.id.account_favourites);
		btnEdit = (Button) findViewById(R.id.account_btn_edit);

		btnNew.setOnClickListener(this);
		btnHome.setOnClickListener(this);
		ivImg.setOnClickListener(this);
		btnFollowers.setOnClickListener(this);
		btnFriends.setOnClickListener(this);
		btnStatuses.setOnClickListener(this);
		btnFavourites.setOnClickListener(this);
		btnEdit.setOnClickListener(this);

		long uid = user.getId();
		long tmpeId = AccountUtil.readUid(this);
		if (uid == tmpeId) {
			btnEdit.setVisibility(View.VISIBLE);
		} else {
			btnEdit.setVisibility(View.GONE);
		}
		//
		updateAccountView();
	}

	private void updateAccountView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvName.setText(user.getName());
				tvDesc.setText(user.getDescription());
				tvAddr.setText(user.getLocation());

				btnFollowers.setText(user.getFollowers_count() + "\n粉丝");
				btnFriends.setText(user.getFriends_count() + "\n关注");
				btnStatuses.setText(user.getStatuses_count() + "\n微博");
				btnFavourites.setText(user.getFavourites_count() + "\n收藏");

				// 加载图片
				SinaWeiboApp.loadImage(user.getAvatar_large(), ivImg);

				tvName.setVisibility(View.VISIBLE);
				tvDesc.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void onClick(View view) {
		if (view == btnFollowers) {
			Intent intent = new Intent(AccountInfo.this,
					FollowersActivity.class);
			intent.putExtra("uid", user.getId());
			startActivity(intent);
		} else if (view == btnFriends) {
			Toast.makeText(this, "Show Friends", Toast.LENGTH_LONG).show();
			finish();
			Intent intent = new Intent(AccountInfo.this, FriendsActivity.class);
			intent.putExtra("uid", user.getId());
			startActivity(intent);
		} else if (view == btnNew) {
			startActivity(new Intent(this, SendWeibo.class));
		} else if (view == btnHome) {
			Intent intent = new Intent(this, Start.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
