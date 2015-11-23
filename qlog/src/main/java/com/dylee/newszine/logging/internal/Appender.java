package com.dylee.newszine.logging.internal;

import java.util.Map;

public interface Appender {
    public Logger logger();

    public void properties(Map<String, String> props);
}
