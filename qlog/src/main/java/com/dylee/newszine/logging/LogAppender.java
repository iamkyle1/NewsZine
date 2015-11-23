package com.dylee.newszine.logging;

public enum LogAppender {
    NONE("appender.none"), LOGCAT("appender.logcat"), REMOTE("appender.remote");

    private final String type;

    LogAppender(String type) {
        this.type = type;
    }

    public final String type() {
        return this.type;
    }

    public static final String PROP_HOST = "qlog.host";
    public static final String PROP_PORT = "qlog.port";
}
