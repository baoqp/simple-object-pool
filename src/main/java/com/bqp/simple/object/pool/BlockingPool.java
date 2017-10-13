package com.bqp.simple.object.pool;


import java.util.concurrent.TimeUnit;

public interface BlockingPool<T> extends Pool<T> {
    T get(long time, TimeUnit unit) throws InterruptedException;
}
