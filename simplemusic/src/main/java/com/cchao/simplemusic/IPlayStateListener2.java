package com.cchao.simplemusic;

/**
 * @author cchao
 * @version 8/21/18.
 */
public interface IPlayStateListener2 {

    void onError(String msg);

    void onStateUpdate(int state, Object content);

}
