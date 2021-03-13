/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.yezj.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.driver.api.ShardingSphereDataSourceFactory;
import org.apache.shardingsphere.infra.config.algorithm.ShardingSphereAlgorithmConfiguration;
import org.apache.shardingsphere.sharding.api.config.ShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.keygen.KeyGenerateStrategyConfiguration;
import org.apache.shardingsphere.sharding.api.config.strategy.sharding.StandardShardingStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
public class ShardingDatabasesAndTablesConfigurationPrecise {

    @Bean
    public DataSource getDataSource() throws SQLException {
        return ShardingSphereDataSourceFactory.createDataSource(createDataSourceMap(), Collections.singleton(createShardingRuleConfiguration()), getProperties());
    }
    
    private ShardingRuleConfiguration createShardingRuleConfiguration() {
        ShardingRuleConfiguration result = new ShardingRuleConfiguration();
        result.getTables().add(getOrderTableRuleConfiguration());
        result.getTables().add(getOrderItemTableRuleConfiguration());
//        result.getBindingTableGroups().add("t_order, t_order_item");
        result.setDefaultDatabaseShardingStrategy(new StandardShardingStrategyConfiguration("user_id", "database_inline"));

        Properties dbprops = new Properties();
        dbprops.setProperty("algorithm-expression", "ds${user_id % 2}");
        result.getShardingAlgorithms().put("database_inline", new ShardingSphereAlgorithmConfiguration("INLINE", dbprops));

        Properties orderprops = new Properties();
        orderprops.setProperty("algorithm-expression", "t_order_$->{user_id % 16}");
        result.getShardingAlgorithms().put("t_order_inline", new ShardingSphereAlgorithmConfiguration("INLINE", orderprops));

        Properties orderitemprops = new Properties();
        orderitemprops.setProperty("algorithm-expression", "t_order_item_$->{user_id % 16}");
        result.getShardingAlgorithms().put("t_order_item_inline", new ShardingSphereAlgorithmConfiguration("INLINE", orderitemprops));



        return result;
    }
    
    private static ShardingTableRuleConfiguration getOrderTableRuleConfiguration() {
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("t_order", "ds$->{0..1}.t_order_$->{0..15}");
        result.setTableShardingStrategy(new StandardShardingStrategyConfiguration("user_id","t_order_inline"));
//        result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("order_id", "snowflake"));
        return result;
    }
    
    private static ShardingTableRuleConfiguration getOrderItemTableRuleConfiguration() {
        ShardingTableRuleConfiguration result = new ShardingTableRuleConfiguration("t_order_item", "ds$->{0..1}.t_order_item_$->{0..15}");
        result.setTableShardingStrategy(new StandardShardingStrategyConfiguration("user_id","t_order_item_inline"));
//        result.setKeyGenerateStrategy(new KeyGenerateStrategyConfiguration("order_item_id", "snowflake"));
        return result;
    }
    
    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>();

        HikariDataSource result1 = new HikariDataSource();
        result1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        result1.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8", "localhost", 9316, "shop"));
        result1.setUsername("root");
        result1.setPassword("123456");

        HikariDataSource result2 = new HikariDataSource();
        result2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        result2.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&useUnicode=true&characterEncoding=UTF-8", "localhost", 9317, "shop"));
        result2.setUsername("root");
        result2.setPassword("123456");

        result.put("ds0", result1);
        result.put("ds1", result2);
        return result;
    }
    
    private static Properties getProperties() {
        Properties result = new Properties();
        result.setProperty("sql-show", "true");
        return result;
    }
}
