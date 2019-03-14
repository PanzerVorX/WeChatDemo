package com.example.wechatdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<ChatMsg>chatMsgList;

    public ChatAdapter(List<ChatMsg> chatMsgList){
        this.chatMsgList=chatMsgList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        ImageView leftHeadImage;
        ImageView rightHeadImage;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View view){
            super(view);
            leftLayout=(LinearLayout)view.findViewById(R.id.left_layout);
            rightLayout=(LinearLayout)view.findViewById(R.id.right_layout);
            leftHeadImage=(ImageView)view.findViewById(R.id.left_headimage);
            rightHeadImage=(ImageView)view.findViewById(R.id.right_headimage);
            leftMsg=(TextView)view.findViewById(R.id.left_msg);
            rightMsg=(TextView)view.findViewById(R.id.right_msg);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout,parent,false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMsg msg=chatMsgList.get(position);
        if (msg.type==ChatMsg.TYPE_RECEIVED){//当聊天消息的类型为发送时
            holder.leftLayout.setVisibility(View.VISIBLE);//左边可见
            holder.rightLayout.setVisibility(View.GONE);//右边不可见
            holder.leftHeadImage.setImageResource(msg.headImageId);
            holder.leftMsg.setText(msg.content);
        }

        else if(msg.type==ChatMsg.TYPE_SENT){
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightHeadImage.setImageResource(R.drawable.wechat_imageme);
            holder.rightMsg.setText(msg.content);
        }
    }

    public int getItemCount() {
        return chatMsgList.size();
    }
}
