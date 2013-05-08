package xmu.swordbearer.sinaplugin.bean;

import java.util.ArrayList;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.app.SinaWeiboApp;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SinaFriendsAdapter extends BaseSinaUsersAdapter {
	public SinaFriendsAdapter(Context context, ArrayList<SinaUser> list) {
		super(context, list);
	}

	static class ViewHolder {
		public ImageView mImg;
		public TextView mName;
		public TextView mStatus;
		public Button mWatch;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_follower, null);
			holder = new ViewHolder();
			holder.mImg = (ImageView) convertView
					.findViewById(R.id.follower_listitem_avatar);
			holder.mName = (TextView) convertView
					.findViewById(R.id.follow_listitem_name);
			holder.mStatus = (TextView) convertView
					.findViewById(R.id.follow_listitem_status);
			holder.mWatch = (Button) convertView
					.findViewById(R.id.follower_listitem_watch);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SinaUser user = list.get(pos);
		SinaWeiboApp.loadImage(user.getProfile_image_url(), holder.mImg);
		holder.mName.setText(user.getScreen_name());
		holder.mStatus.setText(user.getDescription());
		return convertView;
	}

}
