package idv.evan.android;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Display display;

    private FrameLayout frameLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textView;

    private android.support.v4.app.FragmentManager fragmentManager;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        getService();

        findViews();

        initObject();

        viewPager.setAdapter(pagerAdapter);
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

    public void getService() {
        fragmentManager = getSupportFragmentManager();
        display = getWindowManager().getDefaultDisplay();
    }

    public void findViews() {
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        textView = (TextView) findViewById(R.id.textView);


    }

    public void initObject() {
        fragmentList = new ArrayList<>();
        int i = 0;
        while (i < 16) {
            fragmentList.add(new FragmenEx(i));
            TabLayout.Tab tab = tabLayout.newTab().setText(i + 1 + "");
            tabLayout.addTab(tab);
            i++;
        }

        pagerAdapter = new PagerAdapter(fragmentManager, fragmentList);
    }


    class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public PagerAdapter(FragmentManager fm, List fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }



}



