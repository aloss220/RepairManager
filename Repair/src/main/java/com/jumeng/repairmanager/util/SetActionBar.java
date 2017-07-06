package com.jumeng.repairmanager.util;

import com.jumeng.repairmanager.R;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SetActionBar {
	/** 标题View */
	public static RelativeLayout rl_titlebar;
	/** 左侧文字 */
	public static TextView tv_left;
	/** 左侧图片 */
	public static ImageView iv_left;
	/** 中间标题 */
	public static TextView tv_center;
	/** 右边文字 */
	public static TextView tv_right;
	/** 右边标题 */
	public static ImageView iv_right;
	/** 底部描边 */
	public static View border;
	
	
	private static int g=View.GONE;
	private static int v=View.VISIBLE;
	
	public static void initActionBar(Activity activity){
		rl_titlebar=(RelativeLayout)activity.findViewById(R.id.rl_titlebar);
		iv_left=(ImageView)activity.findViewById(R.id.iv_left);
		iv_right=(ImageView)activity.findViewById(R.id.iv_right);
		tv_center=(TextView)activity.findViewById(R.id.tv_center);
		tv_left=(TextView)activity.findViewById(R.id.tv_left);
		tv_right=(TextView)activity.findViewById(R.id.tv_right);
		border= activity.findViewById(R.id.border);
	}
	
	/**
	 * 设置头部样式是否显示
	 * 
	 * @param Tl
	 *            左侧文本
	 * @param Il
	 *            左侧图片
	 * @param Cr
	 *            中间标题
	 * @param Tr
	 *            右边文本
	 * @param Ir
	 *            右边标题
	 */
	public static void setViewShow(int Tl, int Il, int Cr, int Tr, int Ir) {
		if (Tl == 1){
			tv_left.setVisibility(View.VISIBLE);
		}else{
			tv_left.setVisibility(View.INVISIBLE);
		}
		if (Il == 1){
			iv_left.setVisibility(View.VISIBLE);
		}else{
			iv_left.setVisibility(View.INVISIBLE);
		}
		if (Cr == 1){
			tv_center.setVisibility(View.VISIBLE);
		}else{
			tv_center.setVisibility(View.INVISIBLE);
		}
		if (Tr == 1){
			tv_right.setVisibility(View.VISIBLE);
		}else{
			tv_right.setVisibility(View.INVISIBLE);
		}
		if (Ir == 1){
			iv_right.setVisibility(View.VISIBLE);
		}else{
			iv_right.setVisibility(View.INVISIBLE);
		}
		
	}
	
	
	/**
	 * 设置标题样式
	 * 
	 * @param TlText
	 *            左侧文字
	 * @param IlImage
	 *            左侧图片
	 * @param CText
	 *            中间文字
	 * @param TrText
	 *            右边文字
	 * @param IrImage
	 *            右边图片
	 */
	public static void setViewContent(String TlText, int IlImage, String CText, String TrText, int IrImage) {
		if (isVisib(tv_left))
			tv_left.setText(TlText);
		if (isVisib(iv_left))
			iv_left.setImageResource(IlImage);
		if (isVisib(tv_center))
			tv_center.setText(CText);
		if (isVisib(tv_right))
			tv_right.setText(TrText);
		if (isVisib(iv_right))
			iv_right.setImageResource(IrImage);

	}
	/**
	 * 验证是否为隐藏状态
	 * 
	 * @param v
	 * @return
	 */
	private static Boolean isVisib(View v) {
        return v.getVisibility() != View.INVISIBLE;
    }

}

