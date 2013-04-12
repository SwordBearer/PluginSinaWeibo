package xmu.swordbearer.plugins.sinaplugin;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import xmu.swordbearer.plugins.sinaplugin.bean.SinaUser;
import xmu.swordbearer.plugins.sinaplugin.uitl.AccountUtil;
import xmu.swordbearer.plugins.sinaplugin.uitl.FriendShipUtil;
import xmu.swordbearer.smallraccoon.widget.AsyncImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 其他用户的个人主页
 * 
 * @author SwordBearers
 * 
 */
public class UserInfoActivity extends Activity implements
		android.view.View.OnClickListener {
	protected static final String TAG = "AccountInfo";

	public static final int FRIENDSHIP_FOLLOWED_BY = 1;// 我被target关注
	public static final int FRIENDSHIP_FOLLOWING = 2;// 我关注了target
	public static final int FRIENDSHIP_FOLLOWED_EACHOTHER = 3;// 互粉

	private SinaUser user;

	//
	private AsyncImageView ivImg;
	private TextView tvName;// 昵称
	private TextView tvDesc;// 个人描述
	private Button btnFollowers;// 粉丝
	private Button btnFriends;
	private Button btnStatuses;
	private Button btnFavourites;

	private Button btnAttention;

	private ImageButton btnBack;
	private ImageButton btnHome;

	private int relation = 0;// 两个用户间的关注关系

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_user_info);
		btnBack = (ImageButton) findViewById(R.id.userinfo_btn_back);
		btnHome = (ImageButton) findViewById(R.id.userinfo_btn_home);
		ivImg = (AsyncImageView) findViewById(R.id.userinfo_img);
		tvName = (TextView) findViewById(R.id.userinfo_name);
		tvDesc = (TextView) findViewById(R.id.userinfo_desc);
		btnFollowers = (Button) findViewById(R.id.userinfo_followers);
		btnFriends = (Button) findViewById(R.id.userinfo_friends);
		btnStatuses = (Button) findViewById(R.id.userinfo_statuses);
		btnFavourites = (Button) findViewById(R.id.userinfo_favourites);

		btnAttention = (Button) findViewById(R.id.userinfo_btn_attention);

		btnBack.setOnClickListener(this);
		btnHome.setOnClickListener(this);
		ivImg.setOnClickListener(this);
		btnFollowers.setOnClickListener(this);
		btnFriends.setOnClickListener(this);
		btnStatuses.setOnClickListener(this);
		btnFavourites.setOnClickListener(this);
		//
		initViews();
	}

	private void initViews() {
		Intent intent = getIntent();
		long uid = intent.getLongExtra("uid", -1);
		if (uid == -1) {
			finish();
		} else {// 显示其他人的账号信息
			// btnEdit.setVisibility(View.VISIBLE);
			AccountUtil.getAccount(this, uid, new RequestListener() {
				@Override
				public void onIOException(IOException arg0) {
					Toast.makeText(UserInfoActivity.this, "获取账号错误！",
							Toast.LENGTH_SHORT).show();
					finish();
				}

				@Override
				public void onError(WeiboException arg0) {
					Toast.makeText(UserInfoActivity.this, "获取账号错误！",
							Toast.LENGTH_SHORT).show();
					finish();
				}

				@Override
				public void onComplete(String response) {
					try {
						user = SinaUser.fromJSON(response);
						updateAccountView();
						getRelationShip();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	private void updateAccountView() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvName.setText(user.getName());
				tvDesc.setText(user.getDescription());
				Drawable drawable = null;
				if (user.getGender().equals("m")) {
					drawable = getResources().getDrawable(R.drawable.icon_male);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
				} else {
					drawable = getResources().getDrawable(
							R.drawable.icon_female);
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
				}
				tvName.setCompoundDrawables(null, null, drawable, null);
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

	private void updateButton() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (relation == 0) {// 是我自己
					btnAttention.setVisibility(View.GONE);
				} else {
					btnAttention.setVisibility(View.VISIBLE);
					if (relation == FRIENDSHIP_FOLLOWED_EACHOTHER) {
						btnAttention.setText("<>互相关注");
					} else if (relation == FRIENDSHIP_FOLLOWED_BY) {
						btnAttention.setText("关注");
					} else if (relation == FRIENDSHIP_FOLLOWING) {
						btnAttention.setText("取消关注");
					}
				}
			}
		});
	}

	public void getRelationShip() {
		long target_id = user.getId();
		RequestListener listener = new RequestListener() {

			@Override
			public void onIOException(IOException arg0) {
			}

			@Override
			public void onError(WeiboException arg0) {
			}

			@Override
			public void onComplete(String resppnse) {
				try {
					JSONObject jsonObject = new JSONObject(resppnse);
					JSONObject me = jsonObject.getJSONObject("source");
					boolean followed_by = me.getBoolean("followed_by");
					boolean following = me.getBoolean("following");
					//
					if (followed_by && following) {
						relation = FRIENDSHIP_FOLLOWED_EACHOTHER;
					} else if (followed_by && !following) {// 只是粉丝
						relation = FRIENDSHIP_FOLLOWED_BY;
					} else {
						relation = FRIENDSHIP_FOLLOWING;
					}
					updateButton();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
		FriendShipUtil.getRelationship(this, target_id, listener);
	}

	@Override
	public void onClick(View view) {
		if (view == btnFollowers) {
			Intent intent = new Intent(UserInfoActivity.this,
					FollowersActivity.class);
			intent.putExtra("uid", user.getId());
			startActivity(intent);
		} else if (view == btnBack) {
			finish();
		} else if (view == btnHome) {
			Intent intent = new Intent(this, Start.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
}
