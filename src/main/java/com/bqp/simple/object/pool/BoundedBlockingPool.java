package com.bqp.simple.object.pool;

import java.util.concurrent.*;

public class BoundedBlockingPool<T> extends AbstractPool<T> implements BlockingPool<T> {
    private int size;
    private BlockingQueue<T> objects;
    private Validator validator;
    private ObjectFactory objectFactory;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private volatile boolean shutdownCalled;

    public BoundedBlockingPool(int size, Validator validator, ObjectFactory objectFactory) {
        super();
        this.objectFactory = objectFactory;
        this.size = size;
        this.validator = validator;
        objects = new LinkedBlockingQueue(size);
        initializeObjects();
        shutdownCalled = false;
    }

    public T get() {
        if (!shutdownCalled) {
            T t = null;
            try {
                while(true) {
                    t = objects.take();
                    if(validator.isValid(t)) {
                        return t;
                    } else {
                        release(t);
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return t;
        }
        throw new IllegalStateException("Object pool is already shutdown");
    }

    @Override
    public T get(long timeOut, TimeUnit timeUnit) {
        if (!shutdownCalled) {
            long timeOutMills = timeUnit.toMillis(timeOut);
            while(true && timeOutMills > 0) {
                long start = System.currentTimeMillis();
                T t = null;
                try {
                    t = objects.poll(timeOutMills, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if(validator.isValid(t)){
                    return t;
                } else {
                    timeOutMills -= (System.currentTimeMillis()-start);
                    release(t);
                }
            }
            return null;
        }
        throw new IllegalStateException("Object pool is already shutdown");
    }


    private void initializeObjects() {
        for (int i = 0; i < size; i++) {
            objects.add((T) objectFactory.createNew());
        }

    }

    @Override
    public void shutdown() {
        shutdownCalled = true;
        executor.shutdownNow();
        clearResource();
    }

    private void clearResource() {
        for (T t : objects) {
            validator.invalidate(t);
        }
    }

    //如果是无效的，则抛弃无效的对象，并创建一个新的对象加入到池中
    @Override
    protected void handleInvalidReturn(T t) {
        t = null;
        objects.add((T) objectFactory.createNew());
    }

    @Override
    protected void returnToPool(T t) {
        if (validator.isValid(t)) {
            executor.submit(new ObjectReturner<T>(objects, t));
        }
    }

    @Override
    protected boolean isValid(T t) {
        return validator.isValid(t);
    }

    private class ObjectReturner<E> implements Callable {
        private BlockingQueue queue;
        private E e;

        public ObjectReturner(BlockingQueue queue, E e) {
            this.queue = queue;
            this.e = e;
        }

        public Void call() {
            while (true) {
                try {
                    queue.put(e);
                    break;
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
            return null;
        }
    }


}
