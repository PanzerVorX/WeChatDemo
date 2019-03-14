package com.example.wechatdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Page1Fragment extends Fragment {
    List<WeChat> weChatList = new ArrayList<>();//提示消息界面列表
    WeChat weChats[] = new WeChat[11];
    WeChatAdapter adapter;
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page1_layout, container, false);

        //适配消息提示列表
        recyclerView = view.findViewById(R.id.wechat_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WeChatAdapter(weChatList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void onCreate(Bundle savedInstanceState) {//不能在onCreateView()中初始化ViewPager的数据链表，当ViewPager再次滑动到该碎片时，还会执行onCreateView()，导致数据链表会重复存取多次
        super.onCreate(savedInstanceState);
        initWechatList(weChatList);
        initListList();//初始化聊天记录链表（首次点击聊天界面时初始化聊天记录与消息列表的提示消息预与之对应）
    }


    public void onResume() {//碎片所在活动并未被销毁，从聊天界面回到消息提示列表界面只会调用onResume()，该在其中判断是否更新消息列表显示
        super.onResume();
        if (((FirstActivity) getActivity()).isReceive) {//如果从聊天活动跳转到消息列表所在的活动
            ((FirstActivity) getActivity()).isReceive = false;//改变判断变量，防止下次在ViewPager翻页重新获得焦点时消息提示列表作不必要的刷新

            //获取聊天记录链表中对应子项的聊天记录
            int itemPosition = ((FirstActivity) getActivity()).returnedPosition;
            String outLine = FirstActivity.listList.get(itemPosition).get(FirstActivity.listList.get(itemPosition).size() - 1).content;
            weChatList.get(itemPosition).outLine=outLine;//将对应聊天记录链表的最后一个聊天记录作为提示消息
            //更新WeCaht中RecyclerView的显示
            adapter.notifyDataSetChanged();//通过适配器重新绑定所有子项的数据
        }
    }

    public void initWechatList(List<WeChat> weChatList) {//初始化微信消息提示列表

        weChats[0] = new WeChat(R.drawable.wechat_image1, "老师", "我是老师", "今天");
        weChats[1] = new WeChat(R.drawable.wechat_image2, "同学A", "我是同学A", "今天");
        weChats[2] = new WeChat(R.drawable.wechat_image3, "同学B", "我是同学B", "今天");
        weChats[3] = new WeChat(R.drawable.wechat_image4, "同学C", "我是同学C", "今天");
        weChats[4] = new WeChat(R.drawable.wechat_image5, "同学D", "我是同学D", "今天");
        weChats[5] = new WeChat(R.drawable.wechat_image6, "同学E", "我是同学E", "今天");
        weChats[6] = new WeChat(R.drawable.wechat_image7, "同学F", "我是同学F", "今天");
        weChats[7] = new WeChat(R.drawable.wechat_image8, "同学G", "我是同学G", "今天");
        weChats[8] = new WeChat(R.drawable.wechat_image9, "同学H", "我是同学H", "今天");
        weChats[9] = new WeChat(R.drawable.wechat_image10, "同学I", "我是同学I", "今天");
        weChats[10] = new WeChat(R.drawable.wechat_image11, "同学J", "我是同学J", "今天");

        for (int i = 0; i < weChats.length; i++) {
            weChatList.add(weChats[i]);
        }
    }

    public void initListList() {
        for (int i = 0; i < weChats.length; i++) {
            List<ChatMsg>chatMsgList=new ArrayList<>();
            chatMsgList.add(new ChatMsg(ChatMsg.TYPE_RECEIVED,weChats[i].imageId,weChats[i].outLine));//首次点击聊天界面时初始化聊天记录与消息列表的提示消息预与之对应
            FirstActivity.listList.add(chatMsgList);
        }
    }
}
