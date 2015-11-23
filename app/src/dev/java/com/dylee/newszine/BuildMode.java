package com.dylee.newszine;

import android.os.Build;


public final class BuildMode {

    public static final boolean ENABLE_LOG = true;

    public static final int BUILD_MASK = 0xffff;
    // LOG
    public static final int BUILD_ENABLE_LOG = 0x0001;
    // CrashLogger

    public static final int MODE;

    static {
        MODE = (ENABLE_LOG ? BUILD_ENABLE_LOG : 0);
    }

    public static boolean enable(int type) {
        return ((MODE & type) != 0);
    }

    private BuildMode() {
    }

}
