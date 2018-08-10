package com.cchao.sleeping.ui.positive;

import com.cchao.simplelib.ui.fragment.SimpleLazyFragment;
import com.cchao.sleeping.R;
import com.cchao.sleeping.databinding.NegativeFragmentBinding;

/**
 * @author cchao
 * @version 8/10/18.
 */
public class NegativeFragment extends SimpleLazyFragment<NegativeFragmentBinding> {
    @Override
    public void onFirstUserVisible() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.negative_fragment;
    }

    @Override
    protected void onLoadData() {

    }
}
