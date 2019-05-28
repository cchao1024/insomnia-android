package com.cchao.insomnia.view.wish;

/**
 * @author cchao
 * @version 2019-05-27.
 */
public class BindingAdapter {

    @android.databinding.BindingAdapter(value = {"wish_num"})
    public static void setWishNum(WishView view, int num) {
        view.setNum(num);
    }

}
