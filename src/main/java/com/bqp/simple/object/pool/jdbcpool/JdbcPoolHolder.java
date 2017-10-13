package com.bqp.simple.object.pool.jdbcpool;


import com.bqp.simple.object.pool.Pool;

import java.sql.Connection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcPoolHolder {

    public static Map<String, Pool<Connection>> pools = new ConcurrentHashMap<>();

    public static Pool<Connection> getJdbcPool(String name, DataSource dataSource) {
        Pool pool = pools.get(name);
        if(pool == null) {
            if(dataSource == null) return null;
            synchronized (JdbcPoolHolder.class) {
                pool = pools.get(name);
                if(pool == null) {
                    // new 一个
                    JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(
                            dataSource.getDriverName(),
                            dataSource.getUrl(),
                            dataSource.getUserName(),
                            dataSource.getPassword());
                    //TODO size 取多少合适
                    JdbcConnectionValidator jdbcConnectionValidator = new JdbcConnectionValidator(dataSource);
                    pool = JdbcPoolFactory.newBlockingJDBCPool(5, jdbcConnectionFactory, jdbcConnectionValidator, name);
                    pools.put(name, pool);

                }
            }
        }
        return pool;
    }
}
