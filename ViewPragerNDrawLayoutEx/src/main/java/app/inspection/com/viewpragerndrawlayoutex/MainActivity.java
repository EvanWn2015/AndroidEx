package app.inspection.com.viewpragerndrawlayoutex;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private Context context;
    private Display display;
    private LayoutInflater inflater;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private FrameLayout frameLayout;
    private TabLayout tabLayout;

    private PagerAdapter pagerAdapter;
    private ArrayList<View> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        getServices();

        findViews();

        initObjcet();

        setSupportActionBar(toolbar);

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

    }

    public void getServices() {
        display = getWindowManager().getDefaultDisplay();
        inflater = getLayoutInflater().from(context);

    }


    public void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    }

    public void initObjcet() {

        list = new ArrayList<>();
        pagerAdapter = new PagerAdapter(list, context);

        View overView = inflater.inflate(R.layout.activity_over_view, null);

        tabLayout.addTab(tabLayout.newTab().setText("OverView"));

        View overView2 = inflater.inflate(R.layout.activity_over_view, null);
        tabLayout.addTab(tabLayout.newTab().setText("OverView2"));

        list.add(overView);
        list.add(overView2);

        viewPager.setAdapter(pagerAdapter);
    }


}
