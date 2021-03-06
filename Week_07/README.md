**这是第7周作业**  

### 第7周第1课作业2：（必做）按自己设计的表结构，插入100万订单模拟数据，测试不同方式的插入效率
代码见项目**week7class1work2_batch_insert**  
**测试了使用PrepareStatement不同方式的插入。每次插入数据前，都将之前的数据删除。**  
1.批量每次10000条，共100次，耗时144036ms  
2.批量每次100000条，共10次，耗时129397ms  
3.批量每次1000000条，共1次，耗时128450ms  
4.驱动连接url上rewriteBatchedStatements=true之后，批量每次10000条，共100次，耗时47679ms，明显加快  
5.驱动连接url上rewriteBatchedStatements=true之后，批量每次100000条，共10次，耗时42102ms，明显加快  
6.驱动连接url上rewriteBatchedStatements=true之后，批量每次1000000条，共1次，耗时46284ms，明显加快  

**使用多个连接,每个连接新开线程同时插入，使用CountDownLatch等待线程全部执行完计时**   
1.驱动连接url上rewriteBatchedStatements=true之后，批量每次10000条，共100次，耗时46948ms，差不多    
2.驱动连接url上rewriteBatchedStatements=true之后，批量每次100000条，共10次，耗时71542ms，变慢    
3.驱动连接url上rewriteBatchedStatements=true之后，批量每次1000000条，共1次，耗时51794ms，变慢，可能是误差     

**去掉订单表中的主键之后**   
1.单连接单线程批量每次10000条，共100次，耗时29312ms  
2.多连接多线程批量每次10000条，共100次，耗时34421ms  

**总结**  
插入速度最快的，是连接url加上了rewriteBatchedStatements=true之后，耗时40多秒即可；否则需要2分钟多。    
连接url上加上rewriteBatchedStatements=true之后，插入效率大大提升，快了好几倍。  
使用多个连接+线程 同时批量插入，插入速度没有提升，有的还变慢了。  
去过去掉表中的索引，插入效率也还会提升一些。但这样查询效率就将极慢，不推荐。    


### 第7周第2课作业2：（必做）读写分离-动态切换数据源版本1.0
代码见项目**week7class2work2_rw1**     
orm框架使用的是spring data jpa  
配置两个数据源，读操作的时候使用读库，写操作的时候使用写库  
使用AbstractRoutingDataSource实现动态切换数据源   

启动项目，会执行Test类中的方法，可以看日志打印，由于项目为演示方便，连接的是两个没有关联的不同库，通过查询出的数据，可以看出确实是动态切换到了不同数据库。  

具体做法：  
1.在DynamicDataSource中配置多个数据源，以及配置Primary主数据源DynamicDataSource，该数据源继承了AbstractRoutingDataSource。
重写determineCurrentLookupKey方法，每次执行sql之前，都会调用该方法选择数据源。  
2.使用切面，切点就是加了DynamicSwitch的方法，方法执行前，根据该注解上的配置，更改数据源。  
重点以上两步，实现了动态切换数据源。  

3.如果还想要增加新的读数据源，直接在DynamicDBConfig类中，新增配置新的读数据源即可，    
新增一个DataSource的bean定义，然后targetDataSources.put继续添加。读、写数据库前缀固定，方便后期做负载均衡    
```
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
        //TODO 如果有新增数据源，新增一个DataSource的bean定义，然后targetDataSources.put继续添加。读、写数据库前缀固定，方便后期做负载均衡
        return new DynamicDataSource(writeDataSource, targetDataSources);
    }
```
4.有多个读数据源之后，还可以在切换读数据源的时候，根据自定义的算法选择其中任意一个，即可实现读数据源负载均衡。负载均衡算法可以在切面中实现。      

### 第7周第2课作业3：（必做）读写分离-数据库框架版本2.0
代码见项目**week7class2work3_rw2**  
引入ShardingSphere JDBC包，配置文件如下：  
本来想和orm框架一起运行，但是一直报错，可能是版本不兼容，所有采用了使用jdbc测试的办法。  
重点是配置文件**ShardingConfig.java**  
启动项目，会执行Test的方法，该方法有一个查询语句，还有一条插入语句。  
通过查看日志结果，和数据库条数记录，可知：  
查询到的结果一直都是配置的读库，插入数据的一直都是配置的写库。（本项目测试时，实际连的仍然是两个独立的库，为了便于区分）。  

总结：  
1.使用框架比自己实现更加简洁，无需添加注解，自动判断使用哪个数据源。  
2.但是集成时存在兼容问题。本项目因时间原因，最终去掉了orm框架，否则一直报错奇怪的问题。  
