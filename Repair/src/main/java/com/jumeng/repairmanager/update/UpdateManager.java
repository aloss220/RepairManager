package com.jumeng.repairmanager.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 *@author coolszy
 *@date 2012-4-26
 *@blog http://blog.92coding.com
 */

public class UpdateManager
{
	/* 下载中 */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath;
	/* 记录进度条数量 */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false;

	private Context mContext;
	/* 更新进度条 */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			// 正在下载
			case DOWNLOAD:
				// 设置进度条位置
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				// 安装文件
				installApk();
				break;
			default:
				break;
			}
		}
    };

	public UpdateManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate()
	{
		if (isUpdate())
		{
			// 显示提示对话框
			showNoticeDialog();
		} else
		{
			//Toast.makeText(mContext, R.string.soft_update_no, Toast.LENGTH_LONG).show();

		}
	}

	/**
	 * 检查软件是否有更新版本
	 * 
	 * @return
	 */
	private boolean isUpdate()
	{
		// 获取当前软件版本
		int versionCode =Tools.getVersionCode(mContext);
		int serviceCode =MyApplication.getInstance().getVersionCode();
		// 版本判断
        return serviceCode > versionCode;
    }

	/**
	 * 获取软件版本号
	 * 
	 * @param context
	 * @return
	 */
	private int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			// 获取软件版本号，对应AndroidManifest.xml下android:versionCode
			versionCode = context.getPackageManager().getPackageInfo("com.szy.update", 0).versionCode;
		} catch (NameNotFoundException e)
		{
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框
	 */
	private void showNoticeDialog()
	{

		View view=View.inflate(mContext,R.layout.dialog_update_layout, null);
		Button btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
		final Button btn_update=(Button) view.findViewById(R.id.btn_update);
		TextView tv_info=(TextView) view.findViewById(R.id.tv_info);
		tv_info.setText(MyApplication.getInstance().getUpdateInfo());
		//final Dialog dialog=new Dialog(context);
		//dialog.setContentView(view);
		final AlertDialog dialog=new AlertDialog.Builder(mContext).create();
		dialog.show();
		dialog.getWindow().setContentView(view);
		WindowManager wm=((Activity)mContext).getWindowManager();
		Display d=wm.getDefaultDisplay();
		WindowManager.LayoutParams params=dialog.getWindow().getAttributes();
		//params.height=(int)(d.getHeight()*0.4);
		params.width=(int)(d.getWidth()*0.8);
		dialog.getWindow().setAttributes(params);
		dialog.setCanceledOnTouchOutside(false);
		View.OnClickListener  listener=new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_cancel:

					dialog.dismiss();
					break;
				case R.id.btn_update:
					showDownloadDialog();
					dialog.dismiss();
					break;
				}

			}
		};
		btn_cancel.setOnClickListener(listener);
		btn_update.setOnClickListener(listener);
	}

	/**
	 * 显示软件下载对话框
	 */
	private void showDownloadDialog()
	{
		View view=View.inflate(mContext,R.layout.softupdate_progress, null);
		Button btn_cancel=(Button) view.findViewById(R.id.btn_cancel);
		mProgress = (ProgressBar)view.findViewById(R.id.update_progress);
		mDownloadDialog=new AlertDialog.Builder(mContext).create();
		mDownloadDialog.show();
		mDownloadDialog.getWindow().setContentView(view);
		WindowManager wm=((Activity)mContext).getWindowManager();
		Display d=wm.getDefaultDisplay();
		WindowManager.LayoutParams params=mDownloadDialog.getWindow().getAttributes();
		//params.height=(int)(d.getHeight()*0.4);
		params.width=(int)(d.getWidth()*0.8);
		mDownloadDialog.getWindow().setAttributes(params);
		mDownloadDialog.setCanceledOnTouchOutside(false);

		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDownloadDialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;

			}
		});


		// 下载文件
		downloadApk();


		/*// 构造软件下载对话框
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle(R.string.soft_updating);
		// 给下载对话框增加进度条
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		builder.setView(v);
		// 取消更新
		builder.setNegativeButton(R.string.soft_update_cancel, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				// 设置取消状态
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		// 下载文件
		downloadApk();*/
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk()
	{
		// 启动新线程下载软件
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 *@date 2012-4-26
	 *@blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					URL url = new URL(MyApplication.getInstance().getDownloadUrl());
					//URL url = new URL("http://gdown.baidu.com/data/wisegame/f98d235e39e29031/baiduxinwen.apk");
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					// 获取文件大小
					int length = conn.getContentLength();
					// 创建输入流
					InputStream is = conn.getInputStream();

					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists())
					{
						file.mkdir();
					}
					File apkFile = new File(mSavePath, Tools.getApplicationName(mContext));
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do
					{
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位置
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0)
						{
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						// 写入文件
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);// 点击取消就停止下载.
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e)
			{
				e.printStackTrace();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			// 取消下载对话框显示
			mDownloadDialog.dismiss();
		}
	}

    /**
	 * 安装APK文件
	 */
	private void installApk()
	{
		File apkfile = new File(mSavePath,Tools.getApplicationName(mContext));
		if (!apkfile.exists())
		{
			return;
		}
		// 通过Intent安装APK文件
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		//如果不加这句代码   更新成功后app不会自动打开
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		mContext.startActivity(i);
	}
}
