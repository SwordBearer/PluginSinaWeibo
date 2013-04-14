package xmu.swordbearer.sinaplugin.bean;

import java.util.ArrayList;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.smallraccoon.widget.AsyncImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class SinaFollowerAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<SinaUser> list;

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
		SinaUser user = list.get(pos);
		if (container == null) {
			container = inflater.inflate(R.layout.list_item_follower, null);
		}
		AsyncImageView imageView = (AsyncImageView) container
				.findViewById(R.id.follower_listitem__img);
		TextView tvName = (TextView) container
				.findViewById(R.id.follow_listitem_name);
		TextView tvStatus = (TextView) container
				.findViewById(R.id.follow_listitem_status);
		Button btnWatch = (Button) container
				.findViewById(R.id.follower_listitem_watch);
		imageView.loadImage(user.getProfile_image_url());
		tvName.setText(user.getScreen_name());
		tvStatus.setText(user.getDescription());
		container.setTag(user);
		return container;
	}
}
