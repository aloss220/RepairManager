package com.jumeng.repairmanager.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.igexin.sdk.PushManager;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.util.Consts;
import com.jumeng.repairmanager.util.Tools;

public class WelcomeActivity extends BaseActivity {

    private SharedPreferences sp;
    private int userId;
    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//		isTaskRoot() 判断当前的 activity 是否为 程序的第一个activity 即为 入口
        if (!isTaskRoot()) {
            finish();
            return;
        }

        imageView1 = (ImageView) findViewById(R.id.imageView1);
        imageView1.setImageResource(R.mipmap.welcome);
        //ImageLoader.getInstance().displayImage(Consts.WELCOME_PAGE, imageView1);
        PushManager.getInstance().initialize(this.getApplicationContext());
        sp = getSharedPreferences(Consts.USER_FILE_NAME, Context.MODE_PRIVATE);
        userId = sp.getInt(Consts.USER_ID, -1);
        goNextPage();
    }


    private void goNextPage() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startNextActivity();
            }
        }, 2000);

    }


    protected void startNextActivity() {
        SharedPreferences sp = getSharedPreferences(Consts.GUIDE_FILE_NAME, MODE_PRIVATE);
        boolean isUsed = sp.getBoolean("isUsed", true);
        if (isUsed) {//第一次访问进入新手指导
            Tools.StartActivitytoOther(this, GuideActivity.class);
            sp.edit().putBoolean("isUsed", false).commit();
        } else {
            if (userId == -1) {
                Tools.StartActivitytoOther(this, LoginActivity.class);

            } else {
                Tools.StartActivitytoOther(this, MainActivity.class);
            }
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyDown(keyCode, event);
    }

}
