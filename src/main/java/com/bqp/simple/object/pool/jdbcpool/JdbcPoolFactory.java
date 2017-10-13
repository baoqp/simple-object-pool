package com.bqp.simple.object.pool.jdbcpool;


import com.bqp.simple.object.pool.BoundedBlockingPool;
import com.bqp.simple.object.pool.Pool;

import java.sql.Connection;

public class JdbcPoolFactory {

    private JdbcPoolFactory() {
    }


    public static Pool<Connection> newBlockingJDBCPool(int size, JdbcConnectionFactory jdbcConnectionFactory,
                                                       JdbcConnectionValidator jdbcConnectionValidator, String poolName) {

        BoundedBlockingPool<Connection> pool = new NamedJdbcPool(size, jdbcConnectionValidator,
                jdbcConnectionFactory, poolName);
        return pool;
    }
}
