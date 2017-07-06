package com.jumeng.repairmanager.view;

import com.jumeng.repairmanager.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
/**
 * 加载中 对话框
 * @author Administrator
 *
 */
public class LoadingDialog extends Dialog{

	private TextView tv;
	private String str;
	public LoadingDialog(Context context) {
		super(context,R.style.loadingDialogStyle);
	}

	public LoadingDialog(Context context,String str){
		super(context, R.style.loadingDialogStyle);
		this.str = str;
	}
	
	private LoadingDialog(Context context,int theme){
		super(context,theme);
	}
	
	private LoadingDialog(Context context,int theme,String str){
		super(context, theme);
		this.str = str;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_loading);
		tv = (TextView) this.findViewById(R.id.tv_dialog);
		tv.setText(str);
		setCanceledOnTouchOutside(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					dismiss();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}

