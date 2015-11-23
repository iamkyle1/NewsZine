package com.dylee.newszine.logging.internal;

public interface Logger {

//	public void tag(String tag);
	
    public void v(String tag, String message);

    public void v(String tag, String message, Throwable error);

    public void v(String tag, Throwable error);

    public void d(String tag, String message);

    public void d(String tag, String message, Throwable error);

    public void d(String tag, Throwable error);

    public void i(String tag, String message);

    public void i(String tag, String message, Throwable error);

    public void i(String tag, Throwable error);

    public void w(String tag, String message);

    public void w(String tag, String message, Throwable error);

    public void w(String tag, Throwable error);

    public void e(String tag, String message);

    public void e(String tag, String message, Throwable error);

    public void e(String tag, Throwable error);

}
