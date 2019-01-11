package cn.edu.pku.wangtianrun.classroomreservation.viewPager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class MypagerAdapter extends PagerAdapter {
    private ArrayList<View> viewLists;

    public MypagerAdapter() {
    }
    //构造方法
    public MypagerAdapter(ArrayList<View> viewLists) {
        super();
        this.viewLists = viewLists;
    }
    //获得布局数目
    @Override
    public int getCount() {
        return viewLists.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    //实例化对象
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //在容器中加入所选位置处的布局
        container.addView(viewLists.get(position));
        //返回当前布局
        return viewLists.get(position);
    }
    //销毁元素
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewLists.get(position));
    }
}
