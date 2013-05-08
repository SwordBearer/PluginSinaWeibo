package xmu.swordbearer.sinaplugin.ui;

import xmu.swordbearer.sinaplugin.bean.SinaFollowersAdapter;
import xmu.swordbearer.sinaplugin.uitl.FriendShipUtil;

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
