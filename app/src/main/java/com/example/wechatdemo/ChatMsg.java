package com.example.wechatdemo;

public class ChatMsg {//聊天消息类
    public static final int TYPE_RECEIVED=0;
    public static final int TYPE_SENT=1;
    int type;
    int headImageId;
    String content;
    public ChatMsg(int type,int headImageId,String content){
        this.type=type;
        this.headImageId=headImageId;
        this.content=content;
    }
}
