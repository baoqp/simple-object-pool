package com.bqp.simple.object.pool;


public interface Pool<T> {
    T get();

    void release(T t);

    void shutdown();

    interface Validator<T> {

        public boolean isValid(T t);

        public void invalidate(T t);
    }

}
