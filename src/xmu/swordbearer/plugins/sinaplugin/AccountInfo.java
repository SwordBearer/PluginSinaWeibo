package xmu.swordbearer.plugins.sinaplugin;

import xmu.swordbearer.plugins.sinaplugin.bean.SinaUser;
import xmu.swordbearer.smallraccoon.widget.AsyncImageView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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
	private AsyncImageView ivImg;
	private TextView tvName;// 昵称
	private TextView tvAddr;
	private TextView tvDesc;// 个人描述
	private Button btnFollowers;// 粉丝
	private Button btnFriends;
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
		ivImg = (AsyncImageView) findViewById(R.id.account_img);
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
				ivImg.loadImage(user.getAvatar_large());
				
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
