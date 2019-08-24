package com.cchao.insomnia.ui.global;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.cchao.insomnia.R;
import com.cchao.insomnia.global.Constants;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;

/**
 * fragment 容器
 *
 * @author cchao
 * @version 2019-08-07.
 */
public class FragmentContainerActivity extends BaseTitleBarActivity {
    Fragment mFragment;

    @Override
    protected int getLayout() {
        return R.layout.fragment_countainer_activity;
    }

    @Override
    protected void initEventAndData() {
        setTitleText(getIntent().getStringExtra(Constants.Extra.Title));
        Bundle bundle = new Bundle();
        switch (getIntent().getStringExtra(Constants.Extra.Fragment)) {
            case Constants.Container.AppointHistory:
                mFragment = new AppointHistoryListFragment();
                break;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_content, mFragment).commit();
    }

    @Override
    protected void onLoadData() {

    }
}
