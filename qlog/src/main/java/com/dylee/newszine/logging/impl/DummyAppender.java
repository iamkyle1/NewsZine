package com.dylee.newszine.logging.impl;

import com.dylee.newszine.logging.internal.Appender;
import com.dylee.newszine.logging.internal.Logger;

import java.util.Map;


public class DummyAppender implements Logger, Appender {

    public DummyAppender() {
        System.out.println("DummyAppender");
    }
    
    @Override
    public Logger logger() {
        return this;
    }

    @Override
    public void properties(Map<String, String> props) {
    }

    @Override
    public void v(String tag, String message) {
    }

    @Override
    public void v(String tag, String message, Throwable error) {

    }

    @Override
    public void v(String tag, Throwable error) {

    }

    @Override
    public void d(String tag, String message) {

    }

    @Override
    public void d(String tag, String message, Throwable error) {

    }

    @Override
    public void d(String tag, Throwable error) {

    }

    @Override
    public void i(String tag, String message) {

    }

    @Override
    public void i(String tag, String message, Throwable error) {

    }

    @Override
    public void i(String tag, Throwable error) {

    }

    @Override
    public void w(String tag, String message) {

    }

    @Override
    public void w(String tag, String message, Throwable error) {

    }

    @Override
    public void w(String tag, Throwable error) {

    }

    @Override
    public void e(String tag, String message) {

    }

    @Override
    public void e(String tag, String message, Throwable error) {

    }

    @Override
    public void e(String tag, Throwable error) {

    }

}
