package com.cchao.insomnia.view;

import android.databinding.DataBindingUtil;
import android.support.design.widget.BottomSheetDialog;
import android.view.LayoutInflater;

import com.cchao.insomnia.R;
import com.cchao.insomnia.databinding.MusicItemMenuListBinding;
import com.cchao.insomnia.manager.UserManager;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.interfaces.BaseView;

/**
 * @author cchao
 * @version 2019-05-23.
 */
public class Dialogs {
    /**
     * 弹出更多的选项
     */
    public static void showMusicItemMenu(LayoutInflater layoutInflater, FallMusic item, BaseView baseView) {
        BottomSheetDialog dialog = new BottomSheetDialog(layoutInflater.getContext());
        MusicItemMenuListBinding binding = DataBindingUtil.inflate(layoutInflater
            , R.layout.music_item_menu_list, null, false);
        binding.name.setText("歌曲：" + item.getName());
        binding.setClicker(click -> {
            switch (click.getId()) {
                case R.id.next_play:
                    UiHelper.showToast(R.string.developing);
                    dialog.dismiss();
                    break;
                case R.id.wish:
                    UserManager.addWish(item.getId(), baseView);
                    dialog.dismiss();
                    break;
                case R.id.download:
                    UiHelper.showToast(R.string.developing);
                    dialog.dismiss();
                    break;
                case R.id.share:
                    UiHelper.showToast(R.string.developing);
                    dialog.dismiss();
                    break;
            }
        });

        dialog.setContentView(binding.getRoot());
        dialog.show();
    }
}
