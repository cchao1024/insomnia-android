package com.cchao.insomnia.ui.music;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cchao.simplelib.core.UiHelper;
import com.cchao.insomnia.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * @author cchao
 * @version 9/8/18.
 */
public class PLayListFragment extends DialogFragment {

    RecyclerView mRecycler;
    DataBindQuickAdapter<SongInfo> mAdapter;

    @Override
    public void onStart() {
        super.onStart();
        initRecycler();
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.music_play_list_fragment, null);
        mRecycler = view.findViewById(R.id.recycler_view);


        // 不带style的构建的dialog宽度无法铺满屏幕
        //     Dialog dialog = new Dialog(getActivity());
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);

        // 设置弹出框布局参数，宽度铺满全屏，底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        return dialog;
    }
    private void initRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration divider = new DividerItemDecoration(getActivity()
            , DividerItemDecoration.VERTICAL);
        divider.setDrawable(UiHelper.getDrawable(R.drawable.music_list_divider));
        mRecycler.addItemDecoration(divider);

        mRecycler.setAdapter(mAdapter = new DataBindQuickAdapter<SongInfo>
            (R.layout.play_list_item, MusicManager.get().getPlayList()) {

            @Override
            protected void convert(DataBindViewHolder helper, SongInfo item) {
                helper.getBinding().setVariable(BR.item, item);
                helper.getView(R.id.remove).setOnClickListener(v -> {
                    MusicManager.get().deleteSongInfoOnPlayList(item, false);
                });
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MusicManager.get().playMusicByInfo(mAdapter.getItem(position));
            }
        });
    }
}
