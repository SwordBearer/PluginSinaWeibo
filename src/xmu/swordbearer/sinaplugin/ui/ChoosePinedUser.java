package xmu.swordbearer.sinaplugin.ui;

import xmu.swordbearer.sinaplugin.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class ChoosePinedUser extends Activity implements OnClickListener, OnCheckedChangeListener {
	private ImageButton btnBack;

	private View inputBox;
	private EditText etInput;
	private ImageButton btnInputSearch;

	private RadioGroup radioGroup;
	private RadioButton radioFriends;
	private RadioButton radioFollowers;
	private RadioButton radioSearch;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choose_pineduser);
		//
		initViews();
	}

	private void initViews() {
		btnBack = (ImageButton) findViewById(R.id.choose_pineduser_btn_back);
		inputBox = findViewById(R.id.choose_pineduser_inputbox);
		etInput = (EditText) findViewById(R.id.choose_pineduser_input);
		btnInputSearch = (ImageButton) findViewById(R.id.choose_pineduser_input_search);

		radioGroup = (RadioGroup) findViewById(R.id.choose_pineduser_radiogroup);
		radioFriends = (RadioButton) findViewById(R.id.choose_pineduser_radio_1);
		radioFollowers = (RadioButton) findViewById(R.id.choose_pineduser_radio_2);
		radioSearch = (RadioButton) findViewById(R.id.choose_pineduser_radio_3);
		listView = (ListView) findViewById(R.id.choose_pineduser_listview);

		btnBack.setOnClickListener(this);
		btnInputSearch.setOnClickListener(this);

		radioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
	}

	private void getFollowersList() {}

	private void getFriendsList() {}

	private void goToSearch() {
		String txt = etInput.getText().toString().trim();
		if (txt.equals(""))
			return;
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
	}

	private void showInputBox(boolean flag) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (flag) {
			inputBox.setVisibility(View.VISIBLE);
			etInput.requestFocus();
			imm.showSoftInput(etInput, InputMethodManager.RESULT_SHOWN);
		} else {
			inputBox.setVisibility(View.GONE);
			imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
		}
	}

	@Override
	public void onClick(View view) {
		if (view == btnBack) {
			finish();
		} else if (view == btnInputSearch) {
			goToSearch();
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (group == radioGroup) {
			if (checkedId == radioFriends.getId()) {
				getFriendsList();
				showInputBox(false);
			} else if (checkedId == radioFollowers.getId()) {
				getFollowersList();
				showInputBox(false);
			} else if (checkedId == radioSearch.getId()) {
				showInputBox(true);
			}
		}
	}
}
