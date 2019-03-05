package com.cchao.insomnia;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cchao.simplelib.ui.activity.BaseActivity;

public class MainActivityV1 extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_v1);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.main_toolbar);
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

}
