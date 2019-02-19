package com.cchao.sleeping.ui.account;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.cchao.simplelib.core.PrefHelper;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.simplelib.ui.adapter.AbstractPagerAdapter;
import com.cchao.simplelib.util.ExceptionCollect;
import com.cchao.simplelib.util.RegexUtils;
import com.cchao.simplelib.util.StringHelper;
import com.cchao.simplelib.util.UIUtils;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.LoginActivityBinding;
import com.cchao.sleeping.global.Constants;
import com.cchao.sleeping.manager.UserManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cchao
 * @version 2/18/19.
 */
public class LogInActivity extends BaseToolbarActivity<LoginActivityBinding> implements View.OnClickListener {
    public static final int LOG_IN = 0;
    public static final int SIGN_UP = 1;
    private int mLoginType = LOG_IN;

    TabLayout mTabLayout;
    ViewPager mViewPager;
    private final List<String> mViewPageTitles = new ArrayList<>();
    Map<Integer, View> mPagers = new HashMap<>();

    AutoCompleteTextView mEmailEdit;
    EditText mPasswordEdit;
    TextView mLoginSignupBtn;

    @Override
    protected int getLayout() {
        return R.layout.login_activity;
    }

    @Override
    protected void initEventAndData() {
        setToolBarAble(false);
        initTabLayout();
        findView();
    }

    private void findView() {
        mTabLayout = mDataBind.tabLayout;
        mViewPager = mDataBind.loginViewPager;
    }

    @Override
    protected void onLoadData() {

    }

    private void initTabLayout() {
        // 状态栏颜色
        UIUtils.setStatusBarTranslucent(getWindow());

        mViewPageTitles.add(getString(R.string.log_in));
        mViewPageTitles.add(getString(R.string.sign_up));

        mViewPager = (ViewPager) findViewById(R.id.login_view_pager);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new AbstractPagerAdapter<String>(mContext, R.layout.login_login_page, mViewPageTitles) {
            @Override
            public CharSequence getPageTitle(int position) {
                return mViewPageTitles.get(position);
            }

            @Override
            public void convert(View convertView, int position, String item) {
                mPagers.put(position, convertView);

                if (position == mLoginType) {
                    onTypeChange(convertView);
                }

                if (StringHelper.isNotEmpty(PrefHelper.getString(Constants.Prefs.USER_EMAIL))) {
                    mEmailEdit.setText(PrefHelper.getString(Constants.Prefs.USER_EMAIL));
                }
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mLoginType = position == 0 ? LOG_IN : SIGN_UP;
                // 如果已经初始化完成
                if (mPagers.size() == 2) {
                    onTypeChange(mPagers.get(mLoginType));
                }
                UiHelper.hideSoftInput(LogInActivity.this);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);

        // 修改ViewPager的滑动速度
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            field.set(mViewPager, new Scroller(mContext) {
                @Override
                public void startScroll(int startX, int startY, int dx, int dy) {
                    super.startScroll(startX, startY, dx, dy, 600);
                }

                @Override
                public void startScroll(int startX, int startY, int dx, int dy, int duration) {
                    super.startScroll(startX, startY, dx, dy, 600);
                }
            });
        } catch (Exception e) {
            ExceptionCollect.logException(e);
        }
    }

    private void onTypeChange(View currentPage) {
        mEmailEdit = currentPage.findViewById(R.id.log_in_email_edit);
        mPasswordEdit = currentPage.findViewById(R.id.log_in_password_edit);
        mLoginSignupBtn = currentPage.findViewById(R.id.login_signup_btn);
        mLoginSignupBtn.setOnClickListener(this);
        mEmailEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                mPasswordEdit.requestFocus();
                return true;
            }
            return false;
        });

        mPasswordEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mLoginSignupBtn.performClick();
                return false;
            }
            return true;
        });

        switch (mLoginType) {
            case SIGN_UP:
                mLoginSignupBtn.setText(R.string.sign_up);
                currentPage.findViewById(R.id.forgot_your_password).setVisibility(View.INVISIBLE);
                break;
            case LOG_IN:
                mLoginSignupBtn.setText(R.string.log_in);
                currentPage.findViewById(R.id.forgot_your_password).setVisibility(View.VISIBLE);
                currentPage.findViewById(R.id.forgot_your_password).setOnClickListener(LogInActivity.this);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forgot_your_password:
/*                Router.turnTo(mContext, ForgetPwdActivity.class)
                    .putExtra(Constants.Extra.E_MAIL, mEmailEdit.getText().toString())
                    .start();*/
                break;
            case R.id.login_signup_btn:
                String email = mEmailEdit.getText().toString();
                String password = mPasswordEdit.getText().toString();

                //为空
                if (TextUtils.isEmpty(email)) {
                    UiHelper.showSoftInput(mContext, mEmailEdit);
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    UiHelper.showSoftInput(mContext, mPasswordEdit);
                    return;
                }

                if (!RegexUtils.isEmail(email)) {
                    showText(R.string.invalid_email);
                    UiHelper.showSoftInput(mContext, mEmailEdit);
                    return;
                }
                PrefHelper.putString(Constants.Prefs.USER_EMAIL, email);

                onSendRequest(email, password);
                break;
            default:
                break;
        }
    }

    /**
     * 发送登录、注册请求
     */
    private void onSendRequest(String email, String password) {
        showProgress();
        String path = mLoginType == LOG_IN ? "login" : "signup";
        addSubscribe(RetrofitHelper.getApis().login(path, email, password)
            .compose(RxHelper.rxSchedulerTran())
            .subscribe(respBean -> {
                hideProgress();
                if (respBean.isCodeFail()) {
                    return;
                }
                showText(respBean.getMsg());
                UserManager.setUserBean(respBean.getData());
                finish();
            }, RxHelper.getErrorTextConsumer(this)));
    }
}
