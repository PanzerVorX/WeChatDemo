package com.example.wechatdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    public List<ChatMsg> chatMsgList = new ArrayList<>();
    EditText input;
    ImageView send,fallback;
    boolean sendState = false;//按钮状态判断变量
    RecyclerView chatRecyclerview;//聊天界面链表
    ChatAdapter adapter;//聊天列表适配器
    int itemPosition;//所对应的消息列表的子项

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatactivity_layout);

        if(getSupportActionBar()!=null){//取消系统自带标题栏
            getSupportActionBar().hide();
        }

        Intent intent1=getIntent();//获取上个活动传来的intent
        itemPosition=intent1.getIntExtra("itemPosition",0);//获取intent中存取的子项位置
        chatMsgList=FirstActivity.listList.get(itemPosition);//当前聊天数据链表为嵌套链表中对应位置的元素

        //获取控件并注册监听器
        input = (EditText)findViewById(R.id.input);
        send = (ImageView)findViewById(R.id.send);
        fallback=(ImageView)findViewById(R.id.fallback_image);
        input.addTextChangedListener(new inputListener());//为Edittext注册监听器
        send.setOnClickListener(new sendListener());//为发送按钮注册监听器
        fallback.setOnClickListener(new sendListener());

        chatRecyclerview =findViewById(R.id.chat_recyclerview);//适配滑动列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerview.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(chatMsgList);
        chatRecyclerview.setAdapter(adapter);
    }


    public static void toChatActivity(Context context,int itemPosition,int requestCode) {//使用嵌套链表作为所有聊天记录，当点击消息列表界面的子项调转至聊天界面时，需传递该子项的位置以便①从嵌套链表中获取对应元素作为聊天数据链表 ②当回要消息提示界面时，将聊天数据链表赋值到嵌套链表对应的元素（set()），来更新聊天记录 ③当回要消息提示界面时，回传子项位置，以便更新消息提示列表中对应子项的的提示消息（即末尾聊天记录）
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("itemPosition",itemPosition);
        ((Activity) context).startActivityForResult(intent, requestCode);//使用startActivityForResult开启的活动以便销毁时向前个页面传递喜消息列表的子项位置
    }

    //EditText监听器TextWatcher的监听方法：before/onText/afterTextChanged()值改变前/后调用
    class inputListener implements TextWatcher {
        //对EditText进行监听：避免文本为空/空白符，在值改变后每个判断是否为空，再遍历字符数组判断是否全为空白符

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {//EditText的值改前调用

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {//EditText的值改变后调用
            if (!input.getText().toString().equals("")){//判断是否为空，为空则发送按钮状态为不可发送，否则进行判断是否为空白符
                for (int i = 0; i < input.getText().toString().length(); i++) {//遍历输入框中内容，判断值改变后，输入框中的字符是否全部都为空白符（都为空白符发送按钮状态为不可发送，否则为可发送）
                    if ((input.getText().toString().charAt(i)) != ' ') {
                        sendState = true;
                        send.setImageResource(R.drawable.chat_buttom3m);
                        break;
                    }
                    if (i==input.getText().toString().length()-1){
                        sendState = false;
                        send.setImageResource(R.drawable.chat_buttom3);
                    }
                }
            }
            else {
                sendState = false;
                send.setImageResource(R.drawable.chat_buttom3);
            }

        }

        public void afterTextChanged(Editable s) {//EditText的值改变后调用

        }
    }

    class sendListener implements View.OnClickListener {//点击send发送消息

        public void onClick(View v) {
            switch (v.getId()){
                case R.id.send://点击发送按钮
                    if (sendState) {//判断send状态是否变为发送状态
                        String content = input.getText().toString();//获取EditText的内容
                        ChatMsg chatMsg = new ChatMsg(ChatMsg.TYPE_SENT, R.drawable.wechat_imageme, content);//创建消息数据实例
                        chatMsgList.add(chatMsg);//数据链表添加消息数据实例
                        adapter.notifyItemInserted(chatMsgList.size() - 1);//通过适配其刷新Recycler的显示
                        chatRecyclerview.scrollToPosition(chatMsgList.size() - 1);//将消息定位到最后一行
                        input.setText("");//清空输入框
                        // 发送消息后，输入框的值改变，会作相应处理，不用手动改变按钮状态
                    }
                    break;
                case R.id.fallback_image://点击回退按钮，效果back键一致
                    FirstActivity.listList.set(itemPosition,chatMsgList);//将消息链表赋值给嵌套数组对应元素存储
                    Intent intent=new Intent();
                    intent.putExtra("itemPosition",itemPosition);
                    setResult(RESULT_OK,intent);
                    finish();
                    break;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            FirstActivity.listList.set(itemPosition,chatMsgList);
            Intent intent=new Intent();
            intent.putExtra("itemPosition",itemPosition);
            setResult(RESULT_OK,intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}


