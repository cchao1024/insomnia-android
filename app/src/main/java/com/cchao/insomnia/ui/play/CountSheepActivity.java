package com.cchao.insomnia.ui.play;

import android.view.View;

import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.CountSheepActivityBinding;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;

import org.apache.commons.lang3.RandomUtils;

/**
 * @author cchao
 * @version 2019-05-22.
 */
public class CountSheepActivity extends BaseTitleBarActivity<CountSheepActivityBinding> implements View.OnClickListener {
    int mCountNum;
    long mStartTimeStamp = System.currentTimeMillis();

    @Override
    protected int getLayout() {
        return R.layout.count_sheep_activity;
    }

    @Override
    protected void initEventAndData() {
        setTitleText("数绵羊");
        mDataBind.setClick(this);
        changeLayout();
    }

    @Override
    protected void onLoadData() {

    }

    @Override
    public void onBackPressed() {
        int second = (int) (System.currentTimeMillis() - mStartTimeStamp) / 1000;
        String averSecond = String.format("0.2f%", second * 1.0f / mCountNum);
        String msg = "你一共数了" + mCountNum + "只绵羊\n共耗时 " + second + " 秒" + "\n平均每只耗时 " + averSecond + " 秒";
        UiHelper.showConfirmDialog(mContext, msg, (dialogInterface, i) -> {
            dialogInterface.dismiss();
            super.onBackPressed();
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sheep:
                changeLayout();
                if (mCountNum == 0) {
                    mStartTimeStamp = System.currentTimeMillis();
                }
                mCountNum++;
                break;
        }
    }

    void changeLayout() {
        int viewWidth = UiHelper.dp2px(60);
        int x = RandomUtils.nextInt(viewWidth, UiHelper.getScreenWidth() - viewWidth);
        int y = RandomUtils.nextInt(viewWidth, UiHelper.getScreenHeight() - viewWidth - UiHelper.dp2px(100));
        mDataBind.sheep.layout(x, y, x + viewWidth, y + viewWidth);
    }
}
