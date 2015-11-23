package com.dylee.newszine.logging;


import com.dylee.newszine.logging.impl.DummyAppender;
import com.dylee.newszine.logging.internal.Appender;
import com.dylee.newszine.logging.internal.AppenderFactory;
import com.dylee.newszine.logging.internal.Logger;

public final class Qlog {
    private static LogLevel _level = LogLevel.NONE;
    private static Appender _appender = new DummyAppender();
    private static Logger _logger = _appender.logger();

    private static boolean _v, _d, _i, _w, _e;

    public static final void initialize(final LogLevel level,
            final LogAppender logappender) {
        initialize(level, logappender, null);

//        LogAppender.LOGCAT
    }

    public static final void initialize(final LogLevel level,
            final LogAppender logappender, final LogProp props) {

        Appender appender = AppenderFactory.getAppender(logappender,
                (props != null) ? props.map() : null);

        if (appender != null) {
            _appender = appender;
            _logger = appender.logger();
         }
          _level = level;


         _v = (LogLevel.VERBOSE.more(level));
         _d = (LogLevel.DEBUG.more(level));
         _i = (LogLevel.INFO.more(level));
         _w = (LogLevel.WARN.more(level));
         _e = (LogLevel.ERROR.more(level));
           
     
    }

    public static final LogLevel level() {
        final LogLevel lv = _level;
        return lv;
    }

    public static final boolean enabled() {
        return !(_level == LogLevel.NONE);
    }

    public static void v(String tag, String message) {
        if (_v) {
            _logger.v(tag, message);
        }
    }

    public static void v(String tag, String message, Throwable error) {
        if (_v) {
            _logger.v(tag, message, error);
        }
    }

    public static void v(String tag, Throwable error) {
        if (_v) {
            _logger.v(tag, error);
        }
    }

    public static void d(String tag, String message) {
        if (_d) {
            _logger.d(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable error) {
        if (_d) {
            _logger.d(tag, message, error);
        }
    }

    public static void d(String tag, Throwable error) {
        if (_d) {
            _logger.d(tag, error);
        }
    }

    public static void i(String tag, String message) {
        if (_i) {
            _logger.i(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable error) {
        if (_i) {
            _logger.i(tag, message, error);
        }
    }

    public static void i(String tag, Throwable error) {
        if (_i) {
            _logger.i(tag, error);
        }
    }

    public static void w(String tag, String message) {
        if (_w) {
            _logger.w(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable error) {
        if (_w) {
            _logger.w(tag, message, error);
        }
    }

    public static void w(String tag, Throwable error) {
        if (_w) {
            _logger.w(tag, error);
        }
    }

    public static void e(String tag, String message) {
        if (_e) {
            _logger.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable error) {
        if (_e) {
            _logger.e(tag, message, error);
        }
    }

    public static void e(String tag, Throwable error) {
        if (_e) {
            _logger.e(tag, error);
        }
    }
    
//    public static void tag(String tag) {
//        _logger.tag(tag);
//    }
}
