package xmu.swordbearer.sinaplugin.bean;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class BaseSinaUsersAdapter extends BaseAdapter {
	protected LayoutInflater inflater;
	protected ArrayList<SinaUser> list;

	public BaseSinaUsersAdapter(Context context, ArrayList<SinaUser> list) {
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

}
