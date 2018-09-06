package com.cchao.sleeping.ui.fall;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseToolbarActivity;
import com.cchao.sleeping.BR;
import com.cchao.sleeping.R;
import com.cchao.sleeping.api.RetrofitHelper;
import com.cchao.sleeping.databinding.CommonRecyclerBinding;
import com.cchao.sleeping.model.javabean.RespListBean;
import com.cchao.sleeping.model.javabean.fall.FallMusic;
import com.cchao.sleeping.ui.music.MusicPlayerActivity;
import com.cchao.sleeping.view.adapter.PageAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lzx.musiclibrary.aidl.model.SongInfo;
import com.lzx.musiclibrary.manager.MusicManager;

import io.reactivex.Observable;

/**
 * @author cchao
 * @version 8/12/18.
 */
public class FallMusicActivity extends BaseToolbarActivity<CommonRecyclerBinding> {

    RecyclerView mRecycler;
    PageAdapter<FallMusic> mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.common_recycler;
    }

    @Override
    protected void initEventAndData() {
        initAdapter();
        onLoadData();
    }

    private void initAdapter() {
        mRecycler = mDataBind.recyclerView;
        mDataBind.refreshLayout.setEnabled(false);

        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));

        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(UiHelper.getDrawable(R.drawable.music_list_divider));
        mRecycler.addItemDecoration(divider);

        mRecycler.setAdapter(mAdapter = new PageAdapter<FallMusic>
            (R.layout.music_item, mDisposable, this) {

            @Override
            protected Observable<RespListBean<FallMusic>> getLoadObservable(int page) {
                return RetrofitHelper.getApis().getMusicList(page);
            }

            @Override
            protected void convert(DataBindViewHolder helper, FallMusic item) {
                helper.getBinding().setVariable(BR.item,item);
            }
        });

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                FallMusic item = mAdapter.getItem(position);

                SongInfo songInfo = new SongInfo();
                songInfo.setSongId(String.valueOf(item.getId()));
                songInfo.setSongUrl(item.getSrc());
                MusicManager.get().playMusicByInfo(songInfo);

                Router.turnTo(mContext, MusicPlayerActivity.class)
                    .start();
            }
        });

//        mAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    protected void onLoadData() {
        switchView(LOADING);
        mAdapter.onLoadData(1);

    }
}
