package com.cchao.insomnia;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cchao.insomnia.databinding.HomeDrawerMenuItemBinding;
import com.cchao.insomnia.databinding.MainActivityBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.manager.UserManager;
import com.cchao.insomnia.model.javabean.home.NavItem;
import com.cchao.insomnia.model.javabean.user.UserBean;
import com.cchao.insomnia.ui.account.EditUserInfoActivity;
import com.cchao.insomnia.ui.account.UserInfoActivity;
import com.cchao.insomnia.ui.fall.FallFragment;
import com.cchao.insomnia.ui.post.PostBoxFragment;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxBus;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseActivity;
import com.cchao.simplelib.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private LinearLayout mDrawerLinear;
    private ImageView mUserPhotoImage;
    //</editor-fold>

    final int[] mTabTitleArr = {R.string.tab_name_0, R.string.tab_name_1};
    List<BaseFragment> mFragments = new ArrayList<>();
    private MainActivityBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mBinding.setClick(this);

        initEvent();
        findViews();
        initToolbar();
        initTabLayout();
        initDrawerLayout();
        initMenu();
    }

    private void initEvent() {
        addSubscribe(RxBus.getDefault().toObservable(UserBean.class)
            .compose(RxHelper.toMain())
            .subscribe(userBean -> {
                updateUserViews();
            }));
    }

    private void findViews() {
        mTabLayout = mBinding.tabLayout;
        mViewPager = mBinding.viewPager;
        mDrawerLayout = mBinding.drawerLayout;
        mToolbar = mBinding.mainToolbar;
    }

    /**
     * tabLayout绑定ViewPager 懒加载Fragment
     */
    private void initTabLayout() {
        mFragments.add(new FallFragment());
        mFragments.add(new PostBoxFragment());

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
            getWindow().setStatusBarColor(UiHelper.getColor(R.color.colorAccent));
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
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initMenu() {
        mDrawerLinear = findViewById(R.id.home_drawer_linear);
        mUserPhotoImage = findViewById(R.id.icon_portrait);
        updateUserViews();

        // 填充左侧item实体
        List<NavItem> menus = new ArrayList<>();
        menus.add(NavItem.of(Constants.Drawer.Wish, R.string.wish_list, R.drawable.drawer_wish, NavItem.Margin.top));
        menus.add(NavItem.of(Constants.Drawer.TimeDown, R.string.time_down, R.drawable.drawer_time, NavItem.Margin.bottom));
        // divier
        menus.add(NavItem.of(Constants.Drawer.Divider, "", 0, NavItem.Margin.none));

        menus.add(NavItem.of(Constants.Drawer.AboutUs, R.string.about_us, R.drawable.drawer_about, NavItem.Margin.top));
        menus.add(NavItem.of(Constants.Drawer.FeedBack, R.string.feed_back, R.drawable.drawer_feedback, NavItem.Margin.none));
        menus.add(NavItem.of(Constants.Drawer.Settings, R.string.settings, R.drawable.drawer_settings, NavItem.Margin.bottom));

        // 加入到linearLayout
        for (int i = 0; i < menus.size(); i++) {
            NavItem item = menus.get(i);
            View itemView;
            // 分割线
            if (item.ID == Constants.Drawer.Divider) {
                itemView = new View(mContext);
                itemView.setBackgroundColor(UiHelper.getColor(R.color.divider_e5));
                itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , UiHelper.dp2px(10)));
                mDrawerLinear.addView(itemView);
                continue;
            } else {
                HomeDrawerMenuItemBinding binding = DataBindingUtil.inflate(mLayoutInflater
                    , R.layout.home_drawer_menu_item, mDrawerLinear, false);
                binding.menuIcon.setImageResource(item.mIconRes);
                binding.menuText.setText(item.mLabelText);

                itemView = binding.getRoot();
                mDrawerLinear.addView(binding.getRoot());

                LinearLayout.LayoutParams layoutParams = ((LinearLayout.LayoutParams)
                    binding.getRoot().getLayoutParams());

                if (item.getMargin() == NavItem.Margin.top) {
                    layoutParams.setMargins(0, UiHelper.dp2px(8), 0, 0);
                } else if (item.getMargin() == NavItem.Margin.bottom) {
                    layoutParams.setMargins(0, 0, 0, UiHelper.dp2px(8));
                }
            }

            itemView.setOnClickListener(v -> clickMenuItem(item.ID));
        }
    }

    private void clickMenuItem(int menu) {
        switch (menu) {
            case Constants.Drawer.Wish:
                showText(R.string.developing);
                break;
            case Constants.Drawer.FeedBack:
                showText(R.string.developing);
                break;
            case Constants.Drawer.AboutUs:
                showText(R.string.developing);
                break;
            case Constants.Drawer.TimeDown:
                showText(R.string.developing);
                break;
            case Constants.Drawer.Settings:
                showText(R.string.developing);
                break;
            default:
                break;
        }
        closeDrawer();
    }

    private void closeDrawer() {
        RxHelper.timerConsumer(250, aLong -> {
            if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void updateUserViews() {
        UserBean userInfoBean = UserManager.getUserBean();

        ImageLoader.loadImageCircle(mUserPhotoImage, userInfoBean.getAvatar(), R.drawable.default_portrait);

        mBinding.userName.setText(userInfoBean.getNickName());
        mBinding.userEmail.setText(userInfoBean.getEmail());
        if (UserManager.isVisitor()) {
            mBinding.userEmail.setText(R.string.no_bind_email);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_field:
                if (UserManager.isVisitor()) {
                    Router.turnTo(mContext, EditUserInfoActivity.class).start();
                } else {
                    Router.turnTo(mContext, UserInfoActivity.class).start();
                }
                break;
        }
    }
}
