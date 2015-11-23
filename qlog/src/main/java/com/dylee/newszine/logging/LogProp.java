package com.dylee.newszine.logging;

import java.util.HashMap;
import java.util.Map;

public class LogProp {
    final Map<String, String> _map;

    public static LogProp obj() {
        return new LogProp();
    }

    public LogProp() {
        _map = new HashMap<String, String>();
    }

    public LogProp prop(String key, String value) {
        _map.put(key, value);
        return this;
    }

    public Map<String, String> map() {
        return _map;
    }
}
