package com.dylee.newszine.logging;

public enum LogLevel {
    NONE(0x06), ERROR(0x05), WARN(0x04), INFO(0x03), DEBUG(0x02), VERBOSE(0x01);

    private final int level;

    LogLevel(int v) {
        this.level = v;
    }

    public int level() {
        return this.level;
    }

    public boolean less(final LogLevel comp) {
        return (level() <= comp.level);
    }
    
    public boolean more(final LogLevel comp) {
        return (level() >= comp.level);
    }
}
