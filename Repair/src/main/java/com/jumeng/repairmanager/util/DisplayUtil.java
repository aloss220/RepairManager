package com.jumeng.repairmanager.util;


import android.content.Context;
import android.util.TypedValue;

/**
 * 尺寸转换工具类
 * dp、sp 转换为 px 的工具类
 */ 
public class DisplayUtil { 
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 * @return
	 */ 
	public static int px2dip(Context context, float pxValue) { 
		//获取手机屏幕密度
		final float scale = context.getResources().getDisplayMetrics().density; 
		return (int) (pxValue / scale + 0.5f*(pxValue>=0?1:-1)); 

	} 

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 * @return
	 */ 
	public static int dip2px(Context context, float dipValue) { 
		/* final float scale = context.getResources().getDisplayMetrics().density; 
        return (int) (dipValue * scale + 0.5f*(dipValue>=0?1:-1)); */

		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());


	} 

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 * @return
	 */ 
	public static int px2sp(Context context, float pxValue) { 
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
		return (int) (pxValue / fontScale + 0.5f); 
	} 

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 * @return
	 */ 
	public static int sp2px(Context context, float spValue) { 
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
		return (int) (spValue * fontScale + 0.5f); 
	} 

	
	
	public static int dp2sp(Context context,float dpVal){
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal,
				context.getResources().getDisplayMetrics());
	}

	
	public static int sp2dp(Context context,float spVal){
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
				context.getResources().getDisplayMetrics());
	}



}