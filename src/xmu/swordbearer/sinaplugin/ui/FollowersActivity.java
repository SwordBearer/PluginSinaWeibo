package xmu.swordbearer.sinaplugin.ui;

import xmu.swordbearer.sinaplugin.api.FriendShipUtil;
import xmu.swordbearer.sinaplugin.bean.SinaFollowersAdapter;

/**
 * 粉丝列表
 * 
 * @author SwordBearer
 * 
 */
public class FollowersActivity extends BaseSinaUserListActivity {
	@Override
	protected void initView() {
		super.initView();
		userAdapter = new SinaFollowersAdapter(this, userList.getFollowers());
		lvUsers.setAdapter(userAdapter);
		loadMore();
	}

	@Override
	public void loadMore() {
		// 获取uid的粉丝
		FriendShipUtil.getFollowers(this, uid, listener, userList.getNextCursor());
	}
}
