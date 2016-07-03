package app.inspection.com.locationsframelayout;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * Created by EVAN on 2016/6/23.
 */
public class LocationsFrameLayout extends FrameLayout {
    ListView listview;
    ProgressBar progressBar;
    View header;

    public LocationsFrameLayout(Context context) {
        super(context);
    }

    public LocationsFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //先创建LayoutInflater 用于获得布局
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //添加顶部布局
        header = inflater.inflate(R.layout.header, null);

        progressBar = (ProgressBar) header.findViewById(R.id.progressBar);
        //向布局中添加header（下拉加载）
        addView(header);
        //ListView的Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n"});

        listview = (ListView) inflater.inflate(R.layout.listview, null);
        listview.setAdapter(adapter);
        //向布局中添加ListView
        addView(listview);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("", position + " " + id);
            }
        });

    }

    //拦截事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

        }
        if (listview.getFirstVisiblePosition() == 0) {
            View firstview = listview.getChildAt(listview.getFirstVisiblePosition());
            if (firstview.getY() >= 0) {
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    //设置动画时使用
    private float oldy;
    private float ev;


    //处理事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("onTouchEvent", "" + super.onTouchEvent(event));
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //手指落下监听
                oldy = event.getY();
                ev = event.getY();
                Log.i("oldy", "oldy" + oldy);

            case MotionEvent.ACTION_MOVE:

                float y = event.getY();
                float distance = y - oldy;

                //设置listview移动
                listview.setTranslationY(listview.getTranslationY() + distance);
                progressBar.setVisibility(GONE);
                oldy = y;
                invalidate();

                return true;


            case MotionEvent.ACTION_UP:
                //手指抬起时，设置动画效果，使ListView回到顶部
                progressBar.setVisibility(VISIBLE);
                header.getHeight();

                if (event.getY() > ev) {
                    ObjectAnimator.ofFloat(listview, "translationY", listview.getTranslationY(), progressBar.getHeight()).setDuration(300).start();
                } else {
                    ObjectAnimator.ofFloat(listview, "translationY", listview.getTranslationY(), 0).setDuration(300).start();
                }


                // 假更新完畢
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ObjectAnimator.ofFloat(listview, "translationY", listview.getTranslationY(), 0).setDuration(300).start();
                    }
                }, 2000);

                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}