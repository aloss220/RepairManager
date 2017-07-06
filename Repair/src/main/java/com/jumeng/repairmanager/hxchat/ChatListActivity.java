package com.jumeng.repairmanager.hxchat;

import java.util.HashMap;
import java.util.Map;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.jumeng.repairmanager.R;
import com.jumeng.repairmanager.hxchat.EaseConversationListFragment.EaseConversationListItemClickListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.TextView;


public class ChatListActivity extends EaseBaseActivity{
    private TextView unreadLabel;
    private Button[] mTabs;
    private EaseConversationListFragment conversationFragment;
    private Fragment[] fragments;
    private int index;
    private int currentTabIndex;
    private String nickName;
    private String userName;
    

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat_list);
        
        unreadLabel = (TextView) findViewById(R.id.unread_msg_number);
        mTabs = new Button[1];
//        mTabs[0] = (Button) findViewById(R.id.btn_conversation);
//       
//      
//        // 把第一个tab设为选中状态
//        mTabs[0].setSelected(true);
        
        conversationFragment = new EaseConversationListFragment();
        conversationFragment.setConversationListItemClickListener(new EaseConversationListItemClickListener() {
			
			@Override
			public void onListItemClicked(EMConversation conversation) {
				Intent intent = new Intent();
        		intent.setClass(ChatListActivity.this, ChatActivity.class);
        		intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.getUserName());
        		intent.putExtra("nickname", "我就是我");
        		startActivity(intent);
				
			}
		});
      
        fragments = new Fragment[] { conversationFragment};
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationFragment)
               .show(conversationFragment).commit();
                
        
    }
    
//    /**
//     * button点击事件
//     * 
//     * @param view
//     */
//    public void onTabClicked(View view) {
//        switch (view.getId()) {
//        case R.id.btn_conversation:
//            index = 0;
//            break;
//      
//        }
//        if (currentTabIndex != index) {
//            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
//            trx.hide(fragments[currentTabIndex]);
//            if (!fragments[index].isAdded()) {
//                trx.add(R.id.fragment_container, fragments[index]);
//            }
//            trx.show(fragments[index]).commit();
//        }
//        mTabs[currentTabIndex].setSelected(false);
//        // 把当前tab设为选中状态
//        mTabs[index].setSelected(true);
//        currentTabIndex = index;
//    }
    
    /**
     * 临时生成的数据，密码皆为123456，可以登录测试接发消息
     * @return
     */
    private Map<String, EaseUser> getContacts(){
        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        for(int i = 1; i <= 10; i++){
            EaseUser user = new EaseUser("easeuitest" + i);
            contacts.put("easeuitest" + i, user);
        }
        return contacts;
    }
}
