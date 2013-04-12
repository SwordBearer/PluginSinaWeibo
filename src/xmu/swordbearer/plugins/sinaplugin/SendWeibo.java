package xmu.swordbearer.plugins.sinaplugin;

import xmu.swordbearer.plugins.sinaplugin.api.SinaCommon;
import xmu.swordbearer.plugins.sinaplugin.uitl.AccountUtil;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SendWeibo extends Activity implements OnClickListener {
	public static final String BUNDLE_WEIBO = "bundle_weibo";
	private ImageButton btnBack;
	private ImageButton btnSend;
	private ImageButton btnDeleteImg;
	private ImageButton btnImg;
	private EditText editText;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SinaCommon.SEND_COMPLETE:
				Toast.makeText(SendWeibo.this, "发送成功", Toast.LENGTH_SHORT)
						.show();
				finish();
				break;
			case SinaCommon.SEND_ERROR:
				Log.e("TEST", "收到的消息" + msg.obj);
				// Toast.makeText(SendWeibo.this, msg.obj.toString(),
				// Toast.LENGTH_SHORT).show();
				break;
			case SinaCommon.SEND_EXCEPTION:
				Toast.makeText(SendWeibo.this, "错误 ", Toast.LENGTH_SHORT)
						.show();
				break;
			default:
				break;
			}
		}
	};

	public static void handleAuth(Handler handler, Object obj, int msgWhat) {
		Message msg = new Message();
		msg.what = msgWhat;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_sendweibo);
		if (!AccountUtil.checkIsLogined(this)) {
			finish();
		} else {
			initView();
		}
	}

	private void initView() {
		btnBack = (ImageButton) findViewById(R.id.sendweibo_btn_back);
		btnSend = (ImageButton) findViewById(R.id.sendweibo_btn_send);
		btnDeleteImg = (ImageButton) findViewById(R.id.sendweibo_btn_delete_img);
		btnImg = (ImageButton) findViewById(R.id.sendweibo_btn_img);
		editText = (EditText) findViewById(R.id.sendweibo_content);

		btnBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		btnDeleteImg.setOnClickListener(this);
		btnImg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			finish();
		} else if (v == btnSend) {
			sendWeibo();
		} else if (v == btnImg) {
			chooseImg();
		}
	}

	private void chooseImg() {
	}

	private void sendWeibo() {
		String content = "";
		String img = null;
		Drawable d = btnImg.getBackground();
		if (d == getResources().getDrawable(R.drawable.img_none)) {
			img = null;
		}
		content = editText.getText().toString().trim();
		if ((content.length() == 0) && img == null) {
			return;
		}
		if (content.length() > 280) {
			Toast.makeText(SendWeibo.this, "微博文字内容不能超过140字", Toast.LENGTH_LONG)
					.show();
			return;
		}
		SinaCommon.sendWeibo(this, content, img, handler);
	}
}
