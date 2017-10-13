package com.bqp.simple.object.pool.jdbcpool;

import com.bqp.simple.object.pool.Pool;

import java.sql.Connection;
import java.sql.SQLException;


// TODO  hive不支持isValid，可能存在conn不为null，但是连接不可用，可以在idValid中把conn只想一个新建的Connection

public final class JdbcConnectionValidator implements Pool.Validator<Connection> {

    private DataSource dataSource;

    public JdbcConnectionValidator() {
    }

    public JdbcConnectionValidator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isValid(Connection conn) {
        if (conn == null) {
            return false;
        }

        try {
            // hive 不支持 isValid方法
            if (conn instanceof HiveConnection) {
                return true;
            }
            return conn.isValid(3);
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;

    }

    public void invalidate(Connection conn) {
        try {
            conn.close();
            conn = null;
        } catch (SQLException se) {
        }
    }

}