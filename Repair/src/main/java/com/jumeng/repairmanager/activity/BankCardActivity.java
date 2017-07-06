package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bumptech.glide.Glide;
import static com.jumeng.repairmanager.MyApplication.*;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.R.string;
import com.jumeng.repairmanager.receiver.GlobleController;
import com.jumeng.repairmanager.receiver.GlobleController.MyListener;
import com.jumeng.repairmanager.util.BitmapUtils;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.HttpUtil;
import com.jumeng.repairmanager.util.ImageTools;
import com.jumeng.repairmanager.util.MyJsonHttpResponseHandler;
import com.jumeng.repairmanager.util.Sign;
import com.jumeng.repairmanager.util.Tools;
import com.jumeng.repairmanager.view.LoadingDialog;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tarena.utils.ImageCircleView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BankCardActivity extends BaseActivity implements OnClickListener{
	private SharedPreferences sp;
	private int userId;
	private LoadingDialog loadingDialog;
	private EditText et_name;
	private EditText et_bank;
	private EditText et_card;
	private String bankCard;
		private String accountName;
		private String bank;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);

			setContentView(R.layout.activity_bank_card);
			MyApplication.getInstance().addActivities(this);
			sp=getSharedPref();
			userId=sp.getInt(Consts.USER_ID, -1);

			accountName=getIntent().getStringExtra("accountName");
			bank=getIntent().getStringExtra("bank");
			bankCard=getIntent().getStringExtra("bankCard");

			initTitleBar();
			setViews();


		}

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			//getInfo();


		}

	private void initTitleBar() {
		initActionBar(BankCardActivity.this);
		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "绑定银行卡", null, 0);


	}
	private void setViews() {
		et_name=(EditText)findViewById(R.id.et_name);
		et_bank=(EditText)findViewById(R.id.et_bank);
		et_card=(EditText)findViewById(R.id.et_card);

		if(accountName==null||accountName.equals("null")){
			et_name.setText("");
		}else{
			et_name.setText(accountName);
			et_name.setSelection(et_name.getText().length());
		}
		if(bank==null||bank.equals("null")){
			et_bank.setText("");
		}else{
			et_bank.setText(bank);
			et_bank.setSelection(et_bank.getText().length());
		}
		if(bankCard==null||bankCard.equals("null")){
			et_card.setText("");
		}else{
			et_card.setText(bankCard);
			et_card.setSelection(et_card.getText().length());
		}

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_left:
				finish();
				break;
			case R.id.btn_bind:
				bindBankCard();
				break;

		}

	}



	/**
	 * 绑定银行卡
	 */
	public void bindBankCard(){
		String name=et_name.getText().toString();
		String bank=et_bank.getText().toString();
		String card=et_card.getText().toString();
		if(name.isEmpty()){
			Tools.toast(this, "请输入开户人姓名!");
			return;
		}
		if(bank.isEmpty()){
			Tools.toast(this, "请输入开户银行!");
			return;
		}
		if(card.isEmpty()){
			Tools.toast(this, "请输入银行卡号!");
			return;
		}

		loadingDialog=new LoadingDialog(this,"正在绑定银行卡...");
		loadingDialog.show();
		RequestParams params=new RequestParams();
		params.put("code",Consts.GETBANGDING);
		params.put("workerid", userId);
		params.put("kaihuname", name);
		params.put("kaihuhank", bank);
		params.put("kahao", card);
		HttpUtil.post(params, new MyJsonHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					switch (response.getInt("state")) {
						case 1:
							loadingDialog.dismiss();
							Tools.toast(BankCardActivity.this, "绑定成功！");
						/*Intent intent = new Intent(Sign.UPDATE_INFO);
						sendBroadcast(intent);*/
							finish();
							break;
						default:
							loadingDialog.dismiss();
							Tools.toast(BankCardActivity.this, response.getString("msg"));
							break;

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});

	}






}
