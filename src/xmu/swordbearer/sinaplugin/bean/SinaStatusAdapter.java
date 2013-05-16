package xmu.swordbearer.sinaplugin.bean;

import java.util.ArrayList;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.app.SinaWeiboApp;
import xmu.swordbearer.sinaplugin.uitl.CalendarUtil;
import android.content.Context;
import android.text.Html;
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
		TextView tvContent;
		ImageView contentPic;
		View retweetContainer;
		TextView tvRetweetContent;
		ImageView reTweetPic;
		TextView tvTime;
		TextView tvSrc;
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
		final ViewHolder holder;
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

			holder.contentPic = (ImageView) convertView
					.findViewById(R.id.status_listitem_pic);
			holder.retweetContainer = (View) convertView
					.findViewById(R.id.status_listitem_retweet_container);
			holder.tvRetweetContent = (TextView) convertView
					.findViewById(R.id.status_listitem_retweet_content);
			holder.reTweetPic = (ImageView) convertView
					.findViewById(R.id.status_listitem_retweet_pic);
			holder.tvSrc = (TextView) convertView
					.findViewById(R.id.status_listitem_src);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.status_listitem_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final SinaStatus status = list.get(pos);
		SinaUser user = status.getUser();
		SinaWeiboApp
				.loadImage(user.getProfile_image_url(), holder.avater, true);
		String thumbnail_url = status.getThumbnail_pic();
		if (thumbnail_url != null) {
			SinaWeiboApp.loadImage(thumbnail_url, holder.contentPic, false);
		} else {
			holder.contentPic.setImageBitmap(null);
		}
		SinaStatus retweetStatus = status.getRetweeted_status();
		if (retweetStatus != null) {

			holder.retweetContainer.setVisibility(View.VISIBLE);
			holder.tvRetweetContent.setText(retweetStatus.getText());
			String retweetPicUrl = retweetStatus.getThumbnail_pic();
			if (retweetPicUrl != null) {
				SinaWeiboApp.loadImage(retweetPicUrl, holder.reTweetPic, false);
			}
		} else {
			holder.retweetContainer.setVisibility(View.GONE);
		}

		holder.tvName.setText(user.getName());
		holder.tvRedirect.setText(status.getReposts_count() + "");
		holder.tvComment.setText(status.getComments_count() + "");
		holder.tvContent.setText(status.getText());
		holder.tvTime.setText(CalendarUtil.GMT2String(status.getCreated_at()));
		holder.tvSrc.setText("来自:" + Html.fromHtml(status.getSource()));

		return convertView;
	}
}
