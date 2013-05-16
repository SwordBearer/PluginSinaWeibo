package xmu.swordbearer.sinaplugin.ui;

import xmu.swordbearer.sinaplugin.api.FriendShipUtil;
import xmu.swordbearer.sinaplugin.bean.SinaFollowersAdapter;

public class FriendsActivity extends BaseSinaUserListActivity {
	@Override
	protected void initView() {
		super.initView();
		userAdapter = new SinaFollowersAdapter(this, userList.getFollowers());
		lvUsers.setAdapter(userAdapter);
		loadMore();
	}

	@Override
	public void loadMore() {
		// 获取uid的关注列表
		FriendShipUtil
				.getFriends(this, uid, listener, userList.getNextCursor());
	}
}
