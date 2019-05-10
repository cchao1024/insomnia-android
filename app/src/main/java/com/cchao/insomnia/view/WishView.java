package com.cchao.insomnia.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.WishViewBinding;

/**
 * @author cchao
 * @version 2019-05-10.
 */
public class WishView extends RelativeLayout {
    WishViewBinding mBinding;

    public WishView(Context context) {
        this(context, null);
    }

    public WishView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WishView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.wish_view, this, true);
        mBinding.setClick(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected(!isSelected());
            }
        });
    }
}
