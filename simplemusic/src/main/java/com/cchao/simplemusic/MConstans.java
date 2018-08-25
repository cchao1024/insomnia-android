package com.cchao.simplemusic;

/**
 * @author cchao
 * @version 8/21/18.
 */
public interface MConstans {

    interface State {
        int Playing = 1002;
        int Pause = 1003;
        int Stop = 1004;
        int Loading = 1005;
        int Prepared = 1006;
    }
    interface PlayMode {

        int Random = 2001;
        int Order = 2002;
        int Repeat = 2003;
    }
}
