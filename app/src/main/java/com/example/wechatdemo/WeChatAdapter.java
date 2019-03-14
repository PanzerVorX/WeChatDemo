package com.example.wechatdemo;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class WeChatAdapter extends RecyclerView.Adapter<WeChatAdapter.ViewHolder> {

    List<WeChat>weChatList;

    public WeChatAdapter(List<WeChat> weChatList){
        this.weChatList=weChatList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View weChatView;
        ImageView imageView;
        TextView titile,outLine,week;

        ViewHolder(View view){
            super(view);
            weChatView=view;
        }
    }
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wechat_item_layout,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.imageView=(ImageView)view.findViewById(R.id.wechat_image);
        viewHolder.titile=(TextView)view.findViewById(R.id.weichat_title);
        viewHolder.outLine=(TextView)view.findViewById(R.id.wechat_outline);
        viewHolder.week=(TextView)view.findViewById(R.id.wechat_week);
        viewHolder.weChatView.setOnClickListener(new View.OnClickListener() {//给WeChatActivity中RecyclerView的每个子项视图设置监听器（跳转至聊天活动ChatActivity）

            public void onClick(View v) {
                /*
                    使用嵌套链表作为所有聊天记录，当点击消息列表界面的子项调转至聊天界面时，需传递该子项的位置
                    ①获取/更新对应子项的聊天消息链表
                    ②返回主活动后更改对应子项的提示消息（改为对应聊天消息链表末尾元素的内容成员）并刷新消息提示列表
                */
                ChatActivity.toChatActivity(parent.getContext(),viewHolder.getAdapterPosition(),1);//活动的最佳启动方法
            }
        });
        return viewHolder;

    }

    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(weChatList.get(position).imageId);
        viewHolder.titile.setText(weChatList.get(position).title);
        viewHolder.outLine.setText(weChatList.get(position).outLine);
        viewHolder.week.setText(weChatList.get(position).week);
    }

    public int getItemCount() {
        return weChatList.size();
    }

}
