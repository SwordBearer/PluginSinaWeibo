package xmu.swordbearer.sinaplugin.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import xmu.swordbearer.sinaplugin.R;
import xmu.swordbearer.sinaplugin.api.AccountUtil;
import xmu.swordbearer.sinaplugin.api.SinaCommon;
import xmu.swordbearer.sinaplugin.api.StatusUtil;
import xmu.swordbearer.smallraccoon.util.NetUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.net.RequestListener;

/**
 * 发送微博
 * 
 * @author SwordBearer
 * 
 */
public class SendWeibo extends Activity implements OnClickListener {
	private static final String TAG = "SendWeibo";

	public static final String BUNDLE_WEIBO = "bundle_weibo";
	private ImageButton btnBack, btnSend;
	private ImageView imgPreview;
	private EditText editText;
	private ProgressDialog dialog;
	private ImageButton btnCamera, btnPics, btnMention, btnEmotion, btnTrend;

	private String uploadImgPath = "";

	private static final int RESULT_LOAD_IMAGE = 0x01;
	private static final int RESULT_TAKE_PIC = 0x02;
	/**
	 * 待增加： 图片尺寸限制
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			Toast.makeText(SendWeibo.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
		}
	};

	private RequestListener listener = new RequestListener() {
		public void onComplete(String response) {
			SinaCommon.handleMessage(handler, "发送成功", 0);
			finish();
		}

		public void onError(WeiboException exc) {
			handleError(exc.getMessage());
		}

		public void onIOException(IOException ioex) {
			SinaCommon.handleMessage(handler, "发送微博失败：未知错误 ", 0);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sendstatus);
		if (!AccountUtil.checkIsLogined(this)) {
			finish();
		} else {
			initView();
		}
		dialog = new ProgressDialog(this);
		dialog.setCancelable(false);
		dialog.setProgressStyle(0);
		dialog.setMessage("正在发送微博,请稍候...");
	}

	private void initView() {
		btnBack = (ImageButton) findViewById(R.id.sendstatus_btn_back);
		btnSend = (ImageButton) findViewById(R.id.sendstatus_btn_send);
		imgPreview = (ImageView) findViewById(R.id.sendstatus_img_preview);
		editText = (EditText) findViewById(R.id.sendstatus_content);
		btnCamera = (ImageButton) findViewById(R.id.sendstatus_camera);
		btnPics = (ImageButton) findViewById(R.id.sendstatus_pic);
		btnMention = (ImageButton) findViewById(R.id.sendstatus_mention);
		btnEmotion = (ImageButton) findViewById(R.id.sendstatus_emotion);
		btnTrend = (ImageButton) findViewById(R.id.sendstatus_trend);

		btnBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		imgPreview.setOnClickListener(this);
		btnPics.setOnClickListener(this);
		btnCamera.setOnClickListener(this);
		btnMention.setOnClickListener(this);
		btnEmotion.setOnClickListener(this);
		btnTrend.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			finish();
		} else if (v == btnSend) {
			sendWeibo();
		} else if (v == imgPreview) {
			showBigImg();
		} else if (v == btnCamera) {
			takePicture();
		} else if (v == btnEmotion) {
			chooseEmotion();
		} else if (v == btnMention) {
			gotoMention();
		} else if (v == btnTrend) {
			writeTrend();
		} else if (v == btnPics) {
			choosePic();
		}
	}

	/**
	 * 显示大图
	 */
	private void showBigImg() {}

	/**
	 * 选择图片
	 */
	private void choosePic() {
		Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	/**
	 * 照相
	 */
	private void takePicture() {
		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(i, RESULT_TAKE_PIC);
	}

	/**
	 * 选择表情
	 */
	private void chooseEmotion() {}

	/**
	 * 书写话题
	 */
	private void writeTrend() {}

	/**
	 * [@]某一个人
	 */
	private void gotoMention() {}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			uploadImgPath = cursor.getString(columnIndex);
			cursor.close();
			//
			showImageThumbnail();
		} else if (requestCode == RESULT_TAKE_PIC && resultCode == RESULT_OK) {
			Bundle b = data.getExtras();
			Bitmap bmp = (Bitmap) b.get("data");
			FileOutputStream fos = null;
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File file = new File(Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".jpg");
				try {
					if (!file.exists()) {
						file.createNewFile();
						fos = new FileOutputStream(file);
						bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						fos.flush();
					}
				} catch (IOException e) {
				} finally {
					try {
						if (fos != null)
							fos.close();
					} catch (IOException e) {
					}
				}
				uploadImgPath = file.getAbsolutePath();
				showImageThumbnail();
			} else {
				Toast.makeText(this, "SD卡不可用，无法拍照", Toast.LENGTH_LONG).show();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showImageThumbnail() {
		Bitmap originalImage = BitmapFactory.decodeFile(uploadImgPath);
		imgPreview.setImageBitmap(originalImage);
	}

	private void sendWeibo() {
		if (!NetUtil.isNetworkConnected(this)) {
			Toast.makeText(SendWeibo.this, "网络连接异常，无法发送微博", Toast.LENGTH_LONG).show();
			return;
		}

		String content = "";
		content = editText.getText().toString().trim();
		if (content.length() == 0) {
			Toast.makeText(SendWeibo.this, "请输入微博正文内容", Toast.LENGTH_LONG).show();
			return;
		}
		if (content.length() > 280) {
			Toast.makeText(SendWeibo.this, "微博文字内容不能超过140字", Toast.LENGTH_LONG).show();
			return;
		}
		dialog.show();
		StatusUtil.sendWeibo(this, content, uploadImgPath, listener);
	}

	private void handleError(String errorResponse) {
		try {
			JSONObject jsonObject = new JSONObject(errorResponse);
			int errorCode = jsonObject.getInt("error_code");
			switch (errorCode) {
			case 10001:
				SinaCommon.handleMessage(handler, "微博发送失败,系统错误！", 0);
				break;
			case 10010:
				SinaCommon.handleMessage(handler, "发布微博超时，请重新发送", 0);
				break;
			case 10024:
				SinaCommon.handleMessage(handler, "微博发布太频繁，请稍后再发布 :)", 0);
				break;
			case 20006:
				SinaCommon.handleMessage(handler, "微博图片太大，请选择更小的图片", 0);
				break;
			case 20019:
				SinaCommon.handleMessage(handler, "已经发布过相同内容的微博，请勿重复发送 ", 0);
				break;

			default:
				SinaCommon.handleMessage(handler, errorResponse, 0);
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private class SelectedImageAdapter extends BaseAdapter {
		private List<String> imagePathes;
		private LayoutInflater inflater;

		public SelectedImageAdapter(Context context, List<String> list) {
			inflater = LayoutInflater.from(context);
			this.imagePathes = list;
		}

		@Override
		public int getCount() {
			return imagePathes.size();
		}

		@Override
		public Object getItem(int arg0) {
			return imagePathes.get(arg0);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View convertView, ViewGroup group) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item_selected_image, null);
			}
			ImageView imageView = (ImageView) convertView.findViewById(R.id.send_status_selected_image);
			if (pos < (imagePathes.size() - 1)) {
				String path = imagePathes.get(pos);
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				imageView.setImageBitmap(bitmap);
			} else {
				imageView.setImageResource(R.drawable.more_icon);
			}
			return convertView;
		}

	}
}
