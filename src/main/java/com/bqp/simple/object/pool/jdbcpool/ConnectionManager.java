package com.bqp.simple.object.pool.jdbcpool;

import com.bqp.simple.object.pool.Pool;

import java.sql.Connection;

public class ConnectionManager {

    public static Connection getConnection(DataSource dataSource) {
        return JdbcPoolHolder.getJdbcPool(dataSource.getName(), dataSource).get();
    }

    public static void close(DataSource dataSource, Connection connection) throws Exception {
        Pool pool = JdbcPoolHolder.getJdbcPool(dataSource.getName(), dataSource);
        if (pool == null) throw new Exception("pool不存在");
        pool.release(connection);
    }
}
