package com.jumeng.repairmanager.activity;

import static com.jumeng.repairmanager.util.SetActionBar.initActionBar;
import static com.jumeng.repairmanager.util.SetActionBar.setViewContent;
import static com.jumeng.repairmanager.util.SetActionBar.setViewShow;

import java.util.ArrayList;

import com.jumeng.repairmanager.MyApplication;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.fragment.StoreFragment;
import com.jumeng.repairmanager.fragment.WorkFragment;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Tools;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class OrderActivity extends FragmentActivity implements OnPageChangeListener,OnCheckedChangeListener{

	private ImageView iv_back;
	private TextView tv_title;
	private Button bt_done;
	private TextView tv_something;
	private RadioButton rb0;
	private RadioButton rb1;
	private ViewPager viewpager;
	private ArrayList<Fragment> fragmentsList;
	private PopupWindow pop;
	private LinearLayout ll_popup;
	private int selected=0;
	private Drawable drawable2;
	private Drawable drawable3;
	private Drawable drawable4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		MyApplication.getInstance().addActivities(this);
		initTitleBar();
		setViews();
		setViewPager();
		sortWindow(selected);
	}
	private void initTitleBar() {
		initActionBar(OrderActivity.this);

		setViewShow(0, 1, 1, 0, 0);
		setViewContent(null, R.mipmap.left_arrow, "我的订单", null, 0);


	}

	private void setViews() {

		rb0=(RadioButton)findViewById(R.id.rb0);
		rb1=(RadioButton)findViewById(R.id.rb1);
		drawable2= getResources().getDrawable(R.mipmap.arrow_g_d);
		drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
		drawable3= getResources().getDrawable(R.mipmap.arrow_b_d);
		drawable3.setBounds(0, 0, drawable3.getMinimumWidth(), drawable3.getMinimumHeight());
		drawable4= getResources().getDrawable(R.mipmap.arrow_b_u);
		drawable4.setBounds(0, 0, drawable4.getMinimumWidth(), drawable4.getMinimumHeight());
	}
	

    @Override  
    public Resources getResources() {  
        Resources res = super.getResources();    
        Configuration config=new Configuration();    
        config.setToDefaults();    
        res.updateConfiguration(config,res.getDisplayMetrics() );  
        return res;  
    }  

	private void setViewPager() {
		viewpager=(ViewPager)findViewById(R.id.viewpager);
		fragmentsList = new ArrayList<Fragment>();
		WorkFragment workFragment=new WorkFragment();
		StoreFragment storeFragment=new StoreFragment();
		fragmentsList.add(workFragment);
		fragmentsList.add(storeFragment);
		MyPagerAdapter adapter=new MyPagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(0);
		viewpager.setOnPageChangeListener(this);
		viewpager.setOffscreenPageLimit(2);
		
		
	}

	public void onClick(View v){
		switch (v.getId()) {
		case R.id.iv_left:
			finish();
			break;
		case R.id.rb0:
			viewpager.setCurrentItem(0);
			//sortWindow(selected);
			if(pop.isShowing()){
				pop.dismiss();
				rb0.setCompoundDrawables(null,null,drawable3,null);
			}else{
				pop.showAsDropDown(findViewById(R.id.rb0), 0, 0);
				rb0.setCompoundDrawables(null,null,drawable4,null);
			}
			break;
		case R.id.rb1:
			viewpager.setCurrentItem(1);
			//Tools.toast(this, "暂未开通，敬请期待");
			if(pop.isShowing()){
				pop.dismiss();
			}
			rb0.setCompoundDrawables(null,null,drawable2,null);
			break;
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int pos) {
		switch (pos) {
		case 0:
			rb0.setChecked(true);
			rb0.setCompoundDrawables(null,null,drawable3,null);
			break;
		case 1:
			rb1.setChecked(true);
			rb0.setCompoundDrawables(null,null,drawable2,null);
			break;
		}
	}

	public class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int pos) {
			// TODO Auto-generated method stub
			return fragmentsList.get(pos);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragmentsList.size();
		}


	}

	// 条件弹出框
	public void sortWindow(int select2) {
		View view = getLayoutInflater().inflate(R.layout.layout_sort2, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		RadioGroup rg=(RadioGroup)view.findViewById(R.id.rg);
		RadioButton r1 = (RadioButton)view.findViewById(R.id.r1);
		RadioButton r2 = (RadioButton)view.findViewById(R.id.r2);
		RadioButton r3 = (RadioButton)view.findViewById(R.id.r3);

		switch (select2) {
			case 0:
				rg.check(R.id.r1);
				break;
			case 1:
				rg.check(R.id.r2);
				break;
			case 2:
				rg.check(R.id.r3);
				break;
			}

		pop = new PopupWindow(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		//pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(false);
		pop.setOutsideTouchable(false);
		pop.setContentView(view);
		//pop.showAsDropDown(findViewById(R.id.rb0), 0, 0);
		
		view.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				int height = ll_popup.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y >height) {
						pop.dismiss();
						rb0.setCompoundDrawables(null,null,drawable3,null);
					}
				}
				return true;
			}
		});

		OnClickListener listener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(Consts.SEARCH_TYPE);
				switch (v.getId()) {
				case R.id.r1:
					intent.putExtra("searchType", 0);
					selected=0;
					pop.dismiss();
					break;
				case R.id.r2:
					intent.putExtra("searchType", 1);
					selected=1;
					pop.dismiss();
					break;
				case R.id.r3:
					intent.putExtra("searchType", 2);
					selected=2;
					pop.dismiss();
					break;
				}
				intent.putExtra("page", 1);
				sendBroadcast(intent);
				rb0.setCompoundDrawables(null,null,drawable3,null);
				
			}
		};
		r1.setOnClickListener(listener);
		r2.setOnClickListener(listener);
		r3.setOnClickListener(listener);
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb0:
			viewpager.setCurrentItem(0);
			break;
		case R.id.rb1:
			viewpager.setCurrentItem(1);
			break;

		}

	}
}
