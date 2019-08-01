package com.cchao.insomnia.manager;

import android.content.Context;
import android.content.Intent;

import com.cchao.insomnia.R;
import com.cchao.insomnia.api.RetrofitHelper;
import com.cchao.insomnia.model.javabean.RespBean;
import com.cchao.insomnia.model.javabean.compose.AudioBean;
import com.cchao.insomnia.model.javabean.global.AppLaunch;
import com.cchao.simplelib.core.RxHelper;
import com.cchao.simplelib.core.UiHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 处理全局的业务任务
 *
 * @author : cchao
 * @version 2019-04-14
 */
public class GlobalHelper {
    public static List<List<AudioBean>> mAudioList = new ArrayList<>();

    /**
     * 启动同步获取app信息
     */
    public static void syncAppInfo(Consumer<RespBean<AppLaunch>> consumer, Consumer<Throwable> throwableConsumer) {
        Disposable disposable = RetrofitHelper.getApis().appLaunch()
            .compose(RxHelper.toMain())
            .subscribe(respBean -> {
                UserManager.setUserBean(respBean.getData().getUserInfo());
                if (consumer != null) {
                    consumer.accept(respBean);
                }
            }, throwableConsumer);
    }

    public static void shareImage(Context context, String uri) {
        String title = UiHelper.getString(R.string.share_image_title);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void shareMusic(Context context, String uri) {
        String title = UiHelper.getString(R.string.share_image_title);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, uri);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static List<List<AudioBean>> getAudioList() {
        if (!mAudioList.isEmpty()) {
            return Collections.unmodifiableList(mAudioList);
        }

        // rain
        List<AudioBean> list0 = new ArrayList<>();
        list0.add(AudioBean.of(R.string.rain_1, R.raw.rain_normal, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_2, R.raw.rain_light, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_3, R.raw.rain_ocean, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_4, R.raw.rain_on_leaves, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_5, R.raw.rain_on_roof, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_6, R.raw.rain_on_window, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_7, R.raw.rain_thunders, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_8, R.raw.rain_water, R.drawable.ic_rain));
        list0.add(AudioBean.of(R.string.rain_9, R.raw.rain_under_umbrella, R.drawable.ic_rain));
        mAudioList.add(list0);

        // forest
        List<AudioBean> list1 = new ArrayList<>();
        list1.add(AudioBean.of(R.string.forest_1, R.raw.forest_birds, R.drawable.ic_rain));
        list1.add(AudioBean.of(R.string.forest_2, R.raw.forest_creek, R.drawable.ic_rain));
        list1.add(AudioBean.of(R.string.forest_3, R.raw.forest_fire, R.drawable.ic_rain));
        list1.add(AudioBean.of(R.string.forest_4, R.raw.forest_frogs, R.drawable.ic_rain));
        list1.add(AudioBean.of(R.string.forest_5, R.raw.forest_grasshoppers, R.drawable.ic_rain));
        list1.add(AudioBean.of(R.string.forest_6, R.raw.forest_leaves, R.drawable.ic_rain));
        list1.add(AudioBean.of(R.string.forest_7, R.raw.forest_waterfall, R.drawable.ic_rain));
        list1.add(AudioBean.of(R.string.forest_8, R.raw.forest_wind, R.drawable.ic_rain));
        mAudioList.add(list1);

        // meditation
        List<AudioBean> list2 = new ArrayList<>();
        list2.add(AudioBean.of(R.string.meditation_1, R.raw.meditation_bells, R.drawable.ic_rain));
        list2.add(AudioBean.of(R.string.meditation_2, R.raw.meditation_bowl, R.drawable.ic_rain));
        list2.add(AudioBean.of(R.string.meditation_3, R.raw.meditation_brown_noise, R.drawable.ic_rain));
        list2.add(AudioBean.of(R.string.meditation_5, R.raw.meditation_flute, R.drawable.ic_rain));
        list2.add(AudioBean.of(R.string.meditation_4, R.raw.meditation_piano, R.drawable.ic_rain));
        list2.add(AudioBean.of(R.string.meditation_6, R.raw.meditation_pink_noise, R.drawable.ic_rain));
        list2.add(AudioBean.of(R.string.meditation_7, R.raw.meditation_stones, R.drawable.ic_rain));
        list2.add(AudioBean.of(R.string.meditation_8, R.raw.meditation_white_noise, R.drawable.ic_rain));
        mAudioList.add(list2);

        // city
        List<AudioBean> list3 = new ArrayList<>();
        list3.add(AudioBean.of(R.string.city_1, R.raw.city_airplane, R.drawable.ic_rain));
        list3.add(AudioBean.of(R.string.city_2, R.raw.city_car, R.drawable.ic_rain));
        list3.add(AudioBean.of(R.string.city_3, R.raw.city_fan, R.drawable.ic_rain));
        list3.add(AudioBean.of(R.string.city_5, R.raw.city_keyboard, R.drawable.ic_rain));
        list3.add(AudioBean.of(R.string.city_4, R.raw.city_rails, R.drawable.ic_rain));
        list3.add(AudioBean.of(R.string.city_6, R.raw.city_restaurant, R.drawable.ic_rain));
        list3.add(AudioBean.of(R.string.city_7, R.raw.city_subway, R.drawable.ic_rain));
        list3.add(AudioBean.of(R.string.city_8, R.raw.city_washing_machine, R.drawable.ic_rain));
        mAudioList.add(list3);
        return Collections.unmodifiableList(mAudioList);
    }
}
