package app.inspection.com.dialogviewpagerex;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EVAN on 2016/6/20.
 * IR 控制器畫面 ， 多頁面
 */
public class IRTransceiver extends AppCompatActivity {
    private final static String TAG = "IRTransceiver";
    private Context context;

    private Display display;

    private Toolbar toolbar;
    private ViewGroup layout;
    private RelativeLayout relativeLayout;

    private FrameLayout frameLayout;
    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView iv_ir_bk_switch;

    private IRPagerAdapter irPagerAdapter;
    private List<View> views;

    private LayoutInflater inflater;

    private PageClick pageClick;
    private TextView tv_air, tv_tv, tv_customize;
    private ImageView iv_add_new_control_icon;

    private Object object;


    IRTransceiver(final Context context, Object object, Display display) {
        this.context = context;
        this.object = object;
        this.display = display;

        getService();

        findViews();

        initObject();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //  註冊 畫面的所有 click 監聽器
        iv_ir_bk_switch.setOnClickListener(pageClick);
        tv_air.setOnClickListener(pageClick);
        tv_tv.setOnClickListener(pageClick);
        tv_customize.setOnClickListener(pageClick);
        iv_add_new_control_icon.setOnClickListener(pageClick);

    }

    public void getService() {
        inflater = ((Activity) context).getLayoutInflater().from(context);
    }

    public void findViews() {

        layout = (ViewGroup) ((Activity) context).findViewById(android.R.id.content).getRootView();
//        layout.setLayoutParams(params);
        view = inflater.inflate(R.layout.ir_conten, null);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        tv_air = (TextView) view.findViewById(R.id.tv_air);
        tv_tv = (TextView) view.findViewById(R.id.tv_tv);
        tv_customize = (TextView) view.findViewById(R.id.tv_customize);
        iv_add_new_control_icon = (ImageView) view.findViewById(R.id.iv_add_new_control_icon);

        iv_ir_bk_switch = (ImageView) view.findViewById(R.id.iv_ir_bk_switch);


        layout.addView(view);
        view.setVisibility(View.GONE);

    }

    public void initObject() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.width = display.getWidth() / 8;

        views = new ArrayList<>();
        // 動態產生 tab 不可超過 6 組
        for (int i = 0; i < 6; i++) {
            String lowerCountryCode = "icon_air_conditioner_number" + (i + 1);
            int imageId = context.getResources().getIdentifier(lowerCountryCode, "drawable", context.getPackageName());
            Log.i(TAG, "  initObject id  : " + imageId);

            views.add(new IRTransceiverContent(context, display, i).getView());
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_END);
            imageView.setImageResource(imageId);
            imageView.setLayoutParams(lp);
            tabLayout.addTab(tabLayout.newTab().setText(i + 1 + "").setCustomView(imageView));
        }

        irPagerAdapter = new IRPagerAdapter(views);

        viewPager.setAdapter(irPagerAdapter);
//        viewPager.setCurrentItem(0);

        pageClick = new PageClick();
    }


    class IRPagerAdapter extends PagerAdapter {
        private List<View> views;


        IRPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    public void show() {
        view.setVisibility(View.VISIBLE);
        layout.setClickable(false);
    }

    public void hide() {
        view.setVisibility(View.GONE);
        layout.setClickable(false);

    }

    // Page Click Listener
    public class PageClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_air:
                    Toast.makeText(context, "" + ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_tv:
                    Toast.makeText(context, "" + ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_customize:
                    Toast.makeText(context, "" + ((TextView) v).getText().toString(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_add_new_control_icon:
                    Toast.makeText(context, "" + v.getId(), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_ir_bk_switch:
                    hide();
                    break;
            }

        }
    }


}






