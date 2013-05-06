package xmu.swordbearer.sinaplugin.bean;

import java.util.ArrayList;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.app.SinaWeiboApp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SinaFollowerAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<SinaUser> list;

	static class ViewHolder {
		public ImageView mImg;
		public TextView mName;
		public TextView mStatus;
		public Button mWatch;
	}

	public SinaFollowerAdapter(Context context, ArrayList<SinaUser> list) {
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int pos) {
		return list.get(pos);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int pos, View container, ViewGroup group) {
		ViewHolder holder;
		if (container == null) {
			container = inflater.inflate(R.layout.list_item_follower, null);
			holder = new ViewHolder();
			holder.mImg = (ImageView) container
					.findViewById(R.id.follower_listitem__img);
			holder.mName = (TextView) container
					.findViewById(R.id.follow_listitem_name);
			holder.mStatus = (TextView) container
					.findViewById(R.id.follow_listitem_status);
			holder.mWatch = (Button) container
					.findViewById(R.id.follower_listitem_watch);
			container.setTag(holder);
		} else {
			holder = (ViewHolder) container.getTag();
		}
		SinaUser user = list.get(pos);
		SinaWeiboApp.loadImage(user.getProfile_image_url(), holder.mImg);
		holder.mName.setText(user.getScreen_name());
		holder.mStatus.setText(user.getDescription());
		return container;
	}
}
