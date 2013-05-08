package xmu.swordbearer.sinaplugin.bean;

import java.util.ArrayList;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.app.SinaWeiboApp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SinaStatusAdapter extends BaseAdapter {
	protected LayoutInflater inflater;
	protected ArrayList<SinaStatus> list;

	public SinaStatusAdapter(Context context, ArrayList<SinaStatus> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	private static class ViewHolder {
		ImageView avater;
		TextView tvName;
		TextView tvRedirect;
		TextView tvComment;
		TextView tvSrc;
		TextView tvLike;
		TextView tvContent;
		ImageView contentPic;
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
	public long getItemId(int pos) {
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup group) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_status, null);
			holder = new ViewHolder();
			holder.avater = (ImageView) convertView
					.findViewById(R.id.status_listitem_avater);
			holder.tvName = (TextView) convertView
					.findViewById(R.id.status_listitem_name);
			holder.tvRedirect = (TextView) convertView
					.findViewById(R.id.status_listitem_redirect);
			holder.tvComment = (TextView) convertView
					.findViewById(R.id.status_listitem_comment);
			holder.tvContent = (TextView) convertView
					.findViewById(R.id.status_listitem_content);
			holder.tvSrc = (TextView) convertView
					.findViewById(R.id.status_listitem_src);
			holder.tvLike = (TextView) convertView
					.findViewById(R.id.status_listitem_like);
			holder.contentPic = (ImageView) convertView
					.findViewById(R.id.status_listitem_cotent_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SinaStatus status = list.get(pos);
		SinaUser user = status.getUser();
		SinaWeiboApp.loadImage(user.getProfile_image_url(), holder.avater);
		String thumbnail_url = status.getThumbnail_pic();
		if (thumbnail_url != null) {
			SinaWeiboApp.loadImage(thumbnail_url, holder.contentPic);
		} else {
			holder.contentPic.setImageBitmap(null);
		}

		holder.tvName.setText(user.getName());
		holder.tvRedirect.setText(status.getReposts_count() + "");
		holder.tvComment.setText(status.getComments_count() + "");
		holder.tvContent.setText(status.getText());
		holder.tvSrc.setText(status.getSource());
		holder.tvLike.setText(status.getAttitudes_count() + "");

		return convertView;
	}
}
