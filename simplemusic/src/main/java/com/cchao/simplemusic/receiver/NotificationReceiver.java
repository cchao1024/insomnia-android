package com.cchao.simplemusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import com.cchao.simplemusic.App;
import com.cchao.simplemusic.service.MusicService;

import static com.cchao.simplemusic.service.MusicService.MUSIC_ACTION_PLAY;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String ACTION_MUSIC_PLAY = "com.cchao.simplemusic.action.music.play";
    public static final String ACTION_MUSIC_NEXT = "com.cchao.simplemusic.action.music.next";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (App.app.getMusicPlayerService() == null) {
            return;
        }
        String action = intent.getAction();
        try {
            switch (action) {
                case ACTION_MUSIC_PLAY:
                    if (MusicService.MUSIC_CURRENT_ACTION == MUSIC_ACTION_PLAY) {
                        App.app.getMusicPlayerService().action(MusicService.MUSIC_ACTION_PAUSE, "");
                    } else {
                        App.app.getMusicPlayerService().action(MusicService.MUSIC_ACTION_CONTINUE_PLAY, "");
                    }
                    break;
                case ACTION_MUSIC_NEXT:
                    App.app.getMusicPlayerService().action(MusicService.MUSIC_ACTION_NEXT, "");
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
