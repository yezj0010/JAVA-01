package cc.yezj.config;


import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
//@Slf4j
public class DynamicDBConfig {

    @Value("${spring.datasource.write.url}")
    private String dbUrlWrite;

    @Value("${spring.datasource.write.username}")
    private String usernameWrite;

    @Value("${spring.datasource.write.password}")
    private String passwordWrite;

    @Value("${spring.datasource.read.username}")
    private String usernameRead;

    @Value("${spring.datasource.read.password}")
    private String passwordRead;

    @Value("${spring.datasource.read.url}")
    private String dbUrlRead;

    @Value("com.mysql.jdbc.Driver")
    private String driverClassName;

    @Value("5")
    private int initialSize;

    @Value("5")
    private int minIdle;

    @Value("20")
    private int maxActive;

    @Value("60000")
    private int maxWait;

    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    @Value("60000")
    private int timeBetweenEvictionRunsMillis;
    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    @Value("300000")
    private int minEvictableIdleTimeMillis;

    @Value("SELECT 1 FROM DUAL")
    private String validationQuery;

    @Value("true")
    private boolean testWhileIdle;

    @Value("false")
    private boolean testOnBorrow;

    @Value("false")
    private boolean testOnReturn;

    /**
     * 打开PSCache，并且指定每个连接上PSCache的大小
     */
    @Value("true")
    private boolean poolPreparedStatements;

    @Value("20")
    private int maxPoolPreparedStatementPerConnectionSize;
    /**
     * 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
     */
    @Value("stat,wall,log4j")
    private String filters;
//    /**
//     * 通过connectProperties属性来打开mergeSql功能；慢SQL记录
//     */
//    @Value("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500")
//    private String connectionProperties;

    @Bean
    public DataSource writeDataSource() {
        return getDruidDataSource(usernameWrite, passwordWrite, dbUrlWrite);
    }

    @Bean
    public DataSource readDataSource() {
        return getDruidDataSource(usernameRead, passwordRead, dbUrlRead);
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(DataSource writeDataSource, DataSource readDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>(5);
        targetDataSources.put("write", writeDataSource);
        targetDataSources.put("read", readDataSource);
        //TODO 如果有新增数据源，则直接targetDataSources.put继续添加即可。读、写数据库前缀固定，方便后期做负载均衡
        return new DynamicDataSource(writeDataSource, targetDataSources);
    }

    private DruidDataSource getDruidDataSource(String username, String password, String url) {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(url);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
//            log.error("druid configuration initialization filter : {0}", e);
        }
//        datasource.setConnectionProperties(connectionProperties);

        return datasource;
    }
}
