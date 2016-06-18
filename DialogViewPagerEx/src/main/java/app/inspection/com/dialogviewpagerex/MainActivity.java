package app.inspection.com.dialogviewpagerex;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Display display;

    private Dialog dialog;

    private FrameLayout frameLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private android.support.v4.app.FragmentManager fragmentManager;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        getService();

        findViews();

        initObject();

        setSupportActionBar(toolbar);



        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
////                viewPager.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.setContentView(viewPager);

                dialog.show();

            }
        });



    }

    public void getService() {
        fragmentManager = getSupportFragmentManager();
        display = getWindowManager().getDefaultDisplay();
    }


    public void findViews() {
        dialog = new Dialog(context);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);


        dialog.setContentView(R.layout.dialog_ir_transceiver);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        window.setAttributes(lp);

        tabLayout = (TabLayout) dialog.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) dialog.findViewById(R.id.viewPager);
        frameLayout = (FrameLayout) dialog.findViewById(R.id.frameLayout);

    }

    public void initObject() {

        fragmentList = new ArrayList<>();
        int i = 0;
        while (i < 5) {
            fragmentList.add(new IRtransceiver());
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
