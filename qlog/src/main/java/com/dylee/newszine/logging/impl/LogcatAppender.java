package com.dylee.newszine.logging.impl;

import java.util.Map;


import android.util.Log;

import com.dylee.newszine.logging.internal.Appender;
import com.dylee.newszine.logging.internal.Logger;

public class LogcatAppender implements Appender, Logger {

    @Override
    public Logger logger() {
        return this;
    }

    @Override
    public void properties(Map<String, String> props) {
    }

    @Override
    public void v(String tag, String message) {
    	Log.v(tag, message);
    }

    @Override
    public void v(String tag, String message, Throwable error) {
    	Log.v(tag, message, error);
    }

    @Override
    public void v(String tag, Throwable error) {
    	Log.v(tag, "Throwable", error);
    }

    @Override
    public void d(String tag, String message) {
    	Log.d(tag, message);
    }

    @Override
    public void d(String tag, String message, Throwable error) {
    	Log.d(tag, message, error);
    }

    @Override
    public void d(String tag, Throwable error) {
    	Log.d(tag, "Throwable", error);
    }

    @Override
    public void i(String tag, String message) {
    	Log.i(tag, message);
    }

    @Override
    public void i(String tag, String message, Throwable error) {
    	Log.i(tag, message, error);
    }

    @Override
    public void i(String tag, Throwable error) {
    	Log.i(tag, "Throwable", error);
    }

    @Override
    public void w(String tag, String message) {
    	Log.w(tag, message);
    }

    @Override
    public void w(String tag, String message, Throwable error) {
    	Log.w(tag, message, error);
    }

    @Override
    public void w(String tag, Throwable error) {
    	Log.w(tag, "Throwable", error);
    }

    @Override
    public void e(String tag, String message) {
    	Log.e(tag, message);
    }

    @Override
    public void e(String tag, String message, Throwable error) {
    	Log.e(tag, message, error);
    }

    @Override
    public void e(String tag, Throwable error) {
    	Log.e(tag, "Throwable", error);
    }

}
