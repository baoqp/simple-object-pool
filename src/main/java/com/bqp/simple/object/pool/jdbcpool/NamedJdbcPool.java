package com.bqp.simple.object.pool.jdbcpool;


import com.bqp.simple.object.pool.BoundedBlockingPool;
import com.bqp.simple.object.pool.ObjectFactory;

public class NamedJdbcPool extends BoundedBlockingPool {

    //数据库类别
    public static final String DATABASE_CATEGORY_MYSQL = "mysql";
    public static final String DATABASE_CATEGORY_HIVE = "hive";
    public static final String DATABASE_CATEGORY_HANA = "hana";

    /**
     * poolName是库名
     */
    private String poolName;

    private String category;

    public NamedJdbcPool(int size, Validator validator, ObjectFactory objectFactory, String poolName) {
        super(size, validator, objectFactory);
        this.poolName = poolName;
        this.category = ((JdbcConnectionFactory)objectFactory).getCategory();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
}
