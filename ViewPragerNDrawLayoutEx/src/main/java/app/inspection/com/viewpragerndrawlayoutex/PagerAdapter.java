package app.inspection.com.viewpragerndrawlayoutex;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EVAN on 2016/6/23.
 */
public class PagerAdapter extends android.support.v4.view.PagerAdapter {
    private List<View> list;
    private Context context;

    public PagerAdapter(ArrayList<View> list , Context context) {
        this.list = list;
        this.context = context;
    }


    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //这个方法用来实例化页卡

        container.addView(list.get(position), 0);//添加页卡
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();//返回页卡的数量
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
