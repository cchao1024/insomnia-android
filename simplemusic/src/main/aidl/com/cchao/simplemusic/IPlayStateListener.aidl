// IMusicPlayer.aidl
package com.cchao.simplemusic;


// Declare any non-default types here with import statements

interface IPlayStateListener {

    void onError(String msg);
    void onStateUpdate(int state,String content);
}
