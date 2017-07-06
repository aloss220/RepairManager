package com.jumeng.repairmanager.activity;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class GuideActivity extends Activity implements OnPageChangeListener{

	private ViewPager viewPager;
	private int imgs[]={R.mipmap.guide1,R.mipmap.guide2,R.mipmap.guide3};
	private boolean misScrolled;
	private Button btn_into;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);

		viewPager=(ViewPager)findViewById(R.id.viewPager);
		viewPager.setAdapter(new MyPagerAdapter());
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		/*switch (state) {
		case ViewPager.SCROLL_STATE_DRAGGING:			
			misScrolled = false;			
			break;		
		case ViewPager.SCROLL_STATE_SETTLING:			
			misScrolled = true;			
			break;		
		case ViewPager.SCROLL_STATE_IDLE:			
			if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !misScrolled) {
				Intent intent=new Intent();
				intent.setClass(GuideActivity.this,LoginActivity.class);
				startActivity(intent);
				finish();		
			}			
			misScrolled = true;			
			break;		
		}*/

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int pos) {
		btn_into=(Button)findViewById(R.id.btn_into);
		if(pos==imgs.length-1){
			btn_into.setVisibility(View.VISIBLE);
		}else{
			btn_into.setVisibility(View.INVISIBLE);
		}

	}
	
	 public void onClick(View v) {  
	    	switch (v.getId()) {
			case R.id.btn_into:
				Tools.StartActivitytoOther(this, LoginActivity.class);
				finish();
				break;
			}
	    }  

	class MyPagerAdapter extends PagerAdapter{

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(GuideActivity.this);  
			imageView.setScaleType(ScaleType.FIT_XY);  
			imageView.setImageResource(imgs[position]);  
			container.addView(imageView);
			return imageView;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView imageView = new ImageView(GuideActivity.this);  
			imageView.setScaleType(ScaleType.FIT_XY);  
			imageView.setImageResource(imgs[position]);  
			container.removeView(imageView);
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgs==null?0:imgs.length;
		}
		//判断是否由对象生成界面 
		@Override
		public boolean isViewFromObject(View v, Object obj) {
			// TODO Auto-generated method stub
			return v== obj;
		}


	}

}
