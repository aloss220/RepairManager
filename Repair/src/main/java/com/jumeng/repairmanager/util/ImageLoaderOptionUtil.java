package com.jumeng.repairmanager.util;

import com.jumeng.repairmanager.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.graphics.Bitmap;

/**
 * 配置图片加载及显示选项设置
 */
public class ImageLoaderOptionUtil {

	public static DisplayImageOptions option;

	public static DisplayImageOptions getImageDisplayOption(String type) {
		int loadIcon =0;
		if (type.equals("cardFront")) {
			loadIcon = R.mipmap.uploadpicturesmall;
		}else if(type.equals("certificate")){
			loadIcon = R.mipmap.certificate;
		}else if(type.equals("Product")){
			loadIcon = R.mipmap.pro_loading;
		}else if(type.equals("head")){
			loadIcon = R.mipmap.focus_mr;
		}else if(type.equals("ProductDetail")){
			loadIcon = R.mipmap.image_loader_err;
		}else if(type.equals("lunbo")){
			loadIcon = R.mipmap.image_loader_err2;
		}else if(type.equals("moren_fang")){
			loadIcon = R.mipmap.moren_fang;
		}else if(type.equals("moren_yuan")){
			loadIcon = R.mipmap.moren_yuan;
		}
		option = new DisplayImageOptions.Builder()
				.showStubImage(loadIcon) //在ImageView加载过程中显示图片 
				.showImageForEmptyUri(loadIcon)//image连接地址为空时 
				.showImageOnFail(loadIcon) //image加载失败 
				.cacheInMemory(true) //加载图片时会在内存中加载缓存 
				.cacheOnDisc(true) //加载图片时会在磁盘中加载缓存 
				.build();

		return option;
	}

}
