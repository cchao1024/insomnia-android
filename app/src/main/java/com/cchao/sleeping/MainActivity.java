package com.cchao.sleeping;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.cchao.simplelib.ui.activity.BaseActivity;
import com.cchao.simplelib.ui.fragment.BaseFragment;
import com.cchao.sleeping.databinding.MainActivityBinding;
import com.cchao.sleeping.ui.positive.NegativeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;

    final int[] mTabTitleArr = {R.string.tab_name_0, R.string.tab_name_1};
    List<BaseFragment> mFragments = new ArrayList<>();
    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        findViews();
        initToolbar();
        initTabLayout();
        initDrawerLayout();
    }

    private void findViews() {
        mTabLayout = mBinding.tabLayout;
        mViewPager = mBinding.viewPager;
        mDrawerLayout = mBinding.drawerLayout;
        mToolbar= mBinding.mainToolbar;
    }

    /**
     * tabLayout绑定ViewPager 懒加载Fragment
     */
    private void initTabLayout() {
        mFragments.add(new NegativeFragment());
        mFragments.add(new NegativeFragment());

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mTabTitleArr.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getString(mTabTitleArr[position]);
            }

        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initToolbar() {
        //实现侧滑菜单状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        mToolbar.setLogo(R.drawable.ic_main_nav_logo);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_coupon:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDrawerLayout() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        StatusBarCompat.setStatusBarColor(this, R.color.transparent_color);
//        mDrawerLinear = findViewById(R.id.home_drawer_linear);
//        initMenu();
    }
}
