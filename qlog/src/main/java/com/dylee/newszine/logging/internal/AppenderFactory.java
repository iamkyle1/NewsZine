package com.dylee.newszine.logging.internal;

import java.util.HashMap;
import java.util.Map;

import com.dylee.newszine.logging.LogAppender;

public final class AppenderFactory {
    private static final Map<LogAppender, String> _map = new HashMap<LogAppender, String>();

    private static final String NONE_APPENDER = "com.dylee.newszine.logging.impl.DummyAppender";
    private static final String LOGCAT_APPENDER = "com.dylee.newszine.logging.impl.LogcatAppender";
    private static final String REMOTE_APPENDER = "com.dylee.newszine.logging.impl.RemoteAppender";

    private static LogAppender _logappender = null;
    private static Appender _appender = null;
    private static final Object _lock = new Object();

    static {
        _map.put(LogAppender.NONE, NONE_APPENDER);
        _map.put(LogAppender.LOGCAT, LOGCAT_APPENDER);
        _map.put(LogAppender.REMOTE, REMOTE_APPENDER);
    }

    private static final String findAppender(LogAppender logappender) {
        String name = _map.get(logappender);
        if (name == null || name.length() == 0) {
            return NONE_APPENDER;
        }

        return name;
    }

    public static Appender getAppender(LogAppender logappender,
            Map<String, String> props) {
        synchronized (_lock) {
            boolean create = (_appender == null)
                    || (logappender != _logappender);
            if (create) {
                _appender = newAppender(logappender, props);
                _logappender = logappender;
            }
        }
        return _appender;
    }

    private static Appender newAppender(LogAppender logappender,
            Map<String, String> props) {
        String name = findAppender(logappender);

        Class<?> clazz = null;
        Object obj = null;
        Appender appender = null;
        try {
            clazz = Class.forName(name);
            if (clazz != null) {
                obj = (Appender) clazz.newInstance();
            }

            if (obj != null && obj instanceof Appender) {
                appender = (Appender) obj;
                appender.properties(props);
            }
        } catch (Exception e) {
            System.out.println("[error] appender instance creation failed ");
            e.printStackTrace();
        } 
        
     
        return appender;
    }
}
