package com.example.wechatdemo;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    public static List<List<ChatMsg>> listList=new ArrayList<>();//使用嵌套链表存储对应各个子项的聊天消息记录链表（List<List<ChatMsg>>中的元素为List<ChatMsg>，代表对应子项的聊天信息）

    PopupMenu popupMenu;//顶部菜单
    public ImageView[]buttomImages=new ImageView[4];//底部选项控件数组
    public int buttomId[]={R.id.buttom_image1,R.id.buttom_image2,R.id.buttom_image3,R.id.buttom_image4};//底部选项控件数组（方便设置监听器时减少代码量（用循环代替数表））
    public int []resourceIds={R.drawable.buttom1,R.drawable.buttom2,R.drawable.buttom3,R.drawable.buttom4};//底部选项控件未被选时对应显示图片的ID
    public int []mresourceIds={R.drawable.buttom1m,R.drawable.buttom2m,R.drawable.buttom3m,R.drawable.buttom4m};//底部选项被按钮被选时时对应显示图片的ID
    ViewPager viewPager;
    List<Fragment>fragmentList= new ArrayList<>();//碎片页卡链表
    public int res=0;//使用res存取当前页卡的位置
    int returnedPosition;//聊天页面转为信息提示页面时，所回传的对应子项的位置信息
    boolean isReceive=false;//是否从聊天界面转到消息界面的判断变量（以便消息界面的碎片显示时执行OnResume()中判断是否更新列表数据）

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        if(getSupportActionBar()!=null){//取消系统自带标题栏
            getSupportActionBar().hide();
        }

        //设置弹出式菜单
        ImageView titleImageView=(ImageView)findViewById(R.id.title_image3);
        titleImageView.setOnClickListener(this);
        popupMenu=new PopupMenu(this,titleImageView);
        Menu menu=popupMenu.getMenu();
        popupMenu.getMenuInflater().inflate(R.menu.title_menu_item,menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        fragmentList.add(new Page1Fragment());//向Fragment链表添加Fragment元素
        fragmentList.add(new Page2Fragment());
        fragmentList.add(new Page3Fragment());
        fragmentList.add(new Page4Fragment());

        viewPager=findViewById(R.id.viewpager);
        MyViewPagerAdapter myViewPagerAdapter=new MyViewPagerAdapter(getSupportFragmentManager(),fragmentList);//创建ViewPager适配器
        viewPager.setAdapter(myViewPagerAdapter);//通过适配器对象将视图（碎片）绑定到到ViewPager
        viewPager.setOnPageChangeListener(this);//给ViewPager注册也页卡切换监听器OnPageChangeListener，通过响应方法onPageSelected()获得当前页卡位置

        //分别使用数组存储有对应关系的对象（两者随之改变的关系），方便做对应关系的判断
        buttomImages[0]=(ImageView)findViewById(R.id.buttom_image1);//初始化底部选项控件数组
        buttomImages[1]=(ImageView)findViewById(R.id.buttom_image2);
        buttomImages[2]=(ImageView)findViewById(R.id.buttom_image3);
        buttomImages[3]=(ImageView)findViewById(R.id.buttom_image4);
        buttomImages[res].setImageResource(R.drawable.buttom1m);//进入程序ViewPager默认显示首个页卡

        for(int i=0;i<buttomImages.length;i++){//为底部选项控件注册监听器
            buttomImages[i].setOnClickListener(this);
        }
    }

    public void onClick(View v) {
        for(int i=0;i<buttomId.length;i++){
            if (buttomId[i]==v.getId()){
                if(i!=res){//避免重复按下底部选项时改变状态
                    buttomImages[i].setImageResource(mresourceIds[i]);//被选中的底部选项控件显示对应被选中的图片
                    buttomImages[res].setImageResource(resourceIds[res]);//未被选中的底部选项控件显示对应未被选中的图片
                    viewPager.setCurrentItem(i);//ViewPager滑动切换至对应页卡
                    res=i;//当前页卡计数变量随之变化
                }
                return;
            }
        }
        if (v.getId()==R.id.title_image3){
            popupMenu.show();
        }
    }
    //OnPageChangeListener监听器需实现的响应方法，其中onPageSelected()获得当前页卡位置
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {//给ViewPager注册也页卡切换监听器OnPageChangeListener，通过响应方法onPageSelected()获得当前页卡位置

        buttomImages[position].setImageResource(mresourceIds[position]);//选中底部选项变深色
        buttomImages[res].setImageResource(resourceIds[res]);//上个底部选项变浅色
        res=position;//记录当前页卡数
    }

    public void onPageScrollStateChanged(int state) {

    }

    protected void onActivityResult(int requestCode,int resultCode,Intent data){//上个活动销毁时调用
        switch(requestCode){
            case 1://利用请求码判断从哪个活动回传来的信息（当从聊天界面回调消息界面时）
                if(resultCode==RESULT_OK){
                    isReceive=true;//代表是从聊天界面转到消息界面（以便消息界面的碎片显示时执行OnResume()中判断是否更新列表数据）
                    returnedPosition=data.getIntExtra("itemPosition",0);//聊天页面转为信息提示页面时，回传对应子项的位置信息
                }
                break;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        listList.clear();//静态变量生命周期：随着类的加载而加载，随类的消失而消失（活动被回收其类不一定被回收，防止重启程序时复用上次静态变量的值，需在活动的onDestroy()中清空处理）
    }
}
