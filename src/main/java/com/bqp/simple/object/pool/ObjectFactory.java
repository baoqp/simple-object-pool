package com.bqp.simple.object.pool;

public interface ObjectFactory<T> {
    T createNew();
}
