package com.cchao.sleeping;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.cchao.simplelib.ui.activity.BaseActivity;
import com.cchao.simplelib.ui.fragment.BaseFragment;
import com.cchao.sleeping.databinding.MainActivityBinding;
import com.cchao.sleeping.ui.positive.NegativeFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    final int[] mTabTitleArr = {R.string.tab_name_0, R.string.tab_name_1};
    List<BaseFragment> mFragments = new ArrayList<>();
    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        findViews();
        initTabLayout();
    }

    private void findViews() {
        mTabLayout = mBinding.tabLayout;
        mViewPager = mBinding.viewPager;
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
}
