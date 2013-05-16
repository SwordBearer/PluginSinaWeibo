package xmu.swordbearer.sinaplugin.app;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.uitl.BitmapUtil;
import xmu.swordbearer.smallraccoon.imgCache.LazyImageLoader;
import xmu.swordbearer.smallraccoon.imgCache.LazyImageLoader.ImageLoadListener;
import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class SinaWeiboApp extends Application {
	protected static final String TAG = "SinaWeiboApp";
	private static LazyImageLoader imageLoader;
	private static final int MSG_IMG_LOADED = 1;

	@Override
	public void onCreate() {
		super.onCreate();
		imageLoader = LazyImageLoader.newInstance(this);
	}

	public static void loadImage(String url, final ImageView view,
			final boolean isRound) {
		view.setTag(url);
		ImageLoadListener listener = new ImageLoadListener() {
			@Override
			public void onLoaded(String url, Bitmap bitmap) {
				if (isRound) {
					bitmap = BitmapUtil.getRoundedCornerBitmap(bitmap);
				}
				// 设置图片
				view.setImageBitmap(bitmap);
			}
		};
		imageLoader.loadBitmap(url, listener);
	}
}
