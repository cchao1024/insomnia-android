package com.cchao.insomnia.ui.account;

import android.support.v7.widget.GridLayoutManager;

import com.cchao.insomnia.BR;
import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.databinding.CommonRecyclerBinding;
import com.cchao.insomnia.global.Constants;
import com.cchao.insomnia.manager.MusicPlayer;
import com.cchao.insomnia.model.javabean.fall.FallImage;
import com.cchao.insomnia.model.javabean.fall.FallMusic;
import com.cchao.insomnia.model.javabean.user.WishItem;
import com.cchao.insomnia.ui.global.ImageShowActivity;
import com.cchao.insomnia.util.ImageHelper;
import com.cchao.insomnia.view.Dialogs;
import com.cchao.insomnia.view.GridSpaceDividerDecoration;
import com.cchao.insomnia.view.adapter.DataBindMultiQuickAdapter;
import com.cchao.simplelib.core.GsonUtil;
import com.cchao.simplelib.core.ImageLoader;
import com.cchao.simplelib.core.Router;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;
import com.cchao.simplelib.ui.activity.BaseTitleBarActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cchao
 * @version 2019-05-23.
 */
public class WishListActivity extends BaseTitleBarActivity<CommonRecyclerBinding> {

    Adapter mAdapter;
    int mCurPage = 1;

    @Override

    protected int getLayout() {
        return R.layout.common_recycler;
    }

    @Override
    protected void initEventAndData() {
        setTitleText("收藏列表");
        initAdapter();
        mDataBind.refreshLayout.setEnabled(false);
        onLoadData();
    }

    private void initAdapter() {
        mDataBind.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mDataBind.recyclerView.addItemDecoration(new GridSpaceDividerDecoration(UiHelper.dp2px(10), 2));
        mDataBind.recyclerView.setAdapter(mAdapter = new Adapter(null));
        mAdapter.setLoadMoreView(new SimpleLoadMoreView());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadPage(mCurPage + 1);
            }
        });
    }

    @Override
    protected void onLoadData() {
        loadPage(1);
    }

    private void loadPage(int page) {
        if (page == 1) {
            switchView(LOADING);
        }
        addSubscribe(RetrofitHelper.getApis().wishList(page)
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                switchView(CONTENT);
                if (respBean.isCodeFail()) {
                    showText(respBean.getMsg());
                    switchView(NET_ERROR);
                    return;
                }
                switchView(CONTENT);
                // 处理分页
                if (page == 1) {
                    mAdapter.setNewData(respBean.getData());
                } else {
                    mAdapter.addData(respBean.getData());
                }

                mCurPage = respBean.getCurPage();

                if (respBean.getTotalPage() == mCurPage) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
                // 为空
                if (page == 1 && respBean.getData().size() == 0) {
                    switchView(EMPTY);
                }
            }, RxHelper.getSwitchErrorConsumer(this)));
    }

    class Adapter extends DataBindMultiQuickAdapter<WishItem> {
        int itemWidth = UiHelper.getScreenWidth() / 2;

        public Adapter(List<WishItem> data) {
            super(data);
        }

        @Override
        public Map<Integer, Integer> getTypeLayoutMap() {
            Map<Integer, Integer> map = new HashMap<>(8);
            map.put(1, R.layout.fall_image_item);
            map.put(2, R.layout.fall_music_item);
            return map;
        }

        @Override
        protected void convert(DataBindViewHolder helper, WishItem wishItem) {
            switch (wishItem.getType()) {
                case Constants.Type.FALL_IMAGE:
                    FallImage item = GsonUtil.fromJson(GsonUtil.toJson(wishItem.getContent()), FallImage.class);
                    helper.itemView.getLayoutParams().width = itemWidth;
                    helper.itemView.getLayoutParams().height = ImageHelper
                        .getScaleHeight(itemWidth, item.getWidth(), item.getHeight());
                    ImageLoader.loadImage(helper.getView(R.id.image), item.getSrc());
                    // click
                    helper.itemView.setOnClickListener(click -> {
                        Router.turnTo(mContext, ImageShowActivity.class)
                            .putExtra(Constants.Extra.IMAGE_URL, item.getSrc())
                            .putExtra(Constants.Extra.ID, item.getId())
                            .start();
                    });
                    break;
                case Constants.Type.FALL_MUSIC:
                    FallMusic itemMusic = GsonUtil.fromJson(GsonUtil.toJson(wishItem.getContent()), FallMusic.class);
                    if (itemMusic == null) {
                        return;
                    }
                    helper.getBinding().setVariable(BR.item, itemMusic);
                    helper.getView(R.id.more)
                        .setOnClickListener(click -> {
                            Dialogs.showMusicItemMenu(mLayoutInflater, itemMusic, WishListActivity.this);
                        });

                    helper.getConvertView().setOnClickListener(click -> {
                        MusicPlayer.playOrPause(itemMusic);
                    });
                    break;
            }
        }
    }
}
