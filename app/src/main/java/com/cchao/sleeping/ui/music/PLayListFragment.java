package com.cchao.sleeping.ui.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cchao.simplelib.core.UiHelper;
import com.cchao.sleeping.BR;
import com.cchao.sleeping.R;
import com.cchao.sleeping.view.adapter.DataBindQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

/**
 * @author cchao
 * @version 9/8/18.
 */
public class PLayListFragment extends BottomSheetDialogFragment {

    RecyclerView mRecycler;
    DataBindQuickAdapter<SongInfo> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater
        , @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.music_play_list_fragment, container,
            false);
        mRecycler = view.findViewById(R.id.recycler_view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initRecycler();
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
