package com.nexuslink.alphrye.helper;

import android.util.Log;

public class MyLogUtil {

    private static final int NONE = 0;
    private static final int V =  1;
    private static final int D =  2;
    private static final int I =  3;
    private static final int W =  4;
    private static final int E =  5;
    private static final int A =  6;

    private static int level = A;

    public static void d (String tag, String log) {
        if (level >= D) {
            Log.d(tag, log);
        }
    }
}
