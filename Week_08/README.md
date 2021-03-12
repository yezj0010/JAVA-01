**这是第8周作业**  

### 第8周第1课作业2：（必做）设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。并在新结构在演示常见的增删改查操作。
代码、sql 和配置文件，见项目**week8class1work2_sharding**，sql语句见项目中的**initSimpleShop.sql**，配置文件见**application.yaml**  
本项目为springboot1.5.17集成shardingSphereStarter 5.0.0-alpha   
springboot2.*与shardingSphere5.0.0-alpha存在兼容性问题。  


操作步骤  
####1.初始化数据库
建表sql见**initSimpleShop.sql**  
本建表语句使用了存储过程，批量创建分表。  
在每个mysql实例中，根据具体情况修改建表语句，然后运行initSimpleShop.sql，即可得到32张表，order和order_item各16张。  
建表sql基于第6周的建表语句,区别在于订单id,用户id改成了bigint类型，方便使用框架的分表算法。  
表名后缀加上数字递增。  

####2.配置文件
配置文件是**application.yaml**  
该配置文件中，分库分表字段都是user_id,分库算法是userId%2,分表算法时userId%16。  
这样就分成了2个库，每个库中目标表都分成了16张表。  

####3.演示分库分表的增删改查
启动项目
打开 http://localhost:8012/swagger-ui.html 页面。  
可以看到四个接口：  
/initOneData   【增】该接口初始化1条随机数据到mysql中，执行完之后，可以查看数据库中的数据。  
/listByUserId  【查】该接口根据用户id查询用户的所有订单列表，用户id必传。  
/updateState  【改】改接口为根据订单id,用户id修改订单状态。  
/deleteOrder   【删】该接口为根据订单id,用户id，删除订单。  

以下是按照以上接口顺序，依次执行，打印出的真实执行sql的日志  
```
【新增一个订单和订单项】根据配置文件配置的规则，userId为13的用户，确定了库下标为1，表下标为13,
2021-03-13 00:43:15.838  INFO 11212 --- [nio-8012-exec-9] ShardingSphere-SQL                       : Logic SQL: insert into t_order(id,user_id,total_amount,state,create_time) values(?,?,?,?,?)
2021-03-13 00:43:15.838  INFO 11212 --- [nio-8012-exec-9] ShardingSphere-SQL                       : SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
2021-03-13 00:43:15.838  INFO 11212 --- [nio-8012-exec-9] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: insert into t_order_13(id,user_id,total_amount,state,create_time) values(?, ?, ?, ?, ?) ::: [1615567395814, 13, 98.93, 0, 1615567395814]
2021-03-13 00:43:15.926  INFO 11212 --- [nio-8012-exec-9] cc.yezj.service.WriteAndReadService      : 新生成的订单，orderId=1615567395814,userId=13,所在库下标1，所在表下标13
2021-03-13 00:43:15.928  INFO 11212 --- [nio-8012-exec-9] ShardingSphere-SQL                       : Logic SQL: insert into t_order_item(id,order_id,user_id,product_id,product_price,product_num,state,create_time) values(?,?,?,?,?,?,?,?)
2021-03-13 00:43:15.928  INFO 11212 --- [nio-8012-exec-9] ShardingSphere-SQL                       : SQLStatement: MySQLInsertStatement(setAssignment=Optional.empty, onDuplicateKeyColumns=Optional.empty)
2021-03-13 00:43:15.928  INFO 11212 --- [nio-8012-exec-9] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: insert into t_order_item_13(id,order_id,user_id,product_id,product_price,product_num,state,create_time) values(?, ?, ?, ?, ?, ?, ?, ?) ::: [1615567395815, 1615567395814, 13, 23912, 98.93, 1, 0, 1615567395814]
2021-03-13 00:43:15.977  INFO 11212 --- [nio-8012-exec-9] cc.yezj.service.WriteAndReadService      : 新生成的订单项id=1615567395815,orderId=1615567395814,userId=13,所在库下标1，所在表下标13

【根据用户id查找用户订单列表】根据用户id=13，直接在tb_order_13中查询到该用户的订单
2021-03-13 00:43:29.046  INFO 11212 --- [nio-8012-exec-1] ShardingSphere-SQL                       : Logic SQL: select * from t_order where user_id=?
2021-03-13 00:43:29.046  INFO 11212 --- [nio-8012-exec-1] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty)
2021-03-13 00:43:29.046  INFO 11212 --- [nio-8012-exec-1] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: select * from t_order_13 where user_id=? ::: [13]
2021-03-13 00:43:29.048  INFO 11212 --- [nio-8012-exec-1] ShardingSphere-SQL                       : Logic SQL: select * from t_order_item where user_id=?
2021-03-13 00:43:29.048  INFO 11212 --- [nio-8012-exec-1] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty)
2021-03-13 00:43:29.048  INFO 11212 --- [nio-8012-exec-1] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: select * from t_order_item_13 where user_id=? ::: [13]

【更新操作】先查询一遍是否存在，然后更新
2021-03-13 00:43:42.430  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Logic SQL: select * from t_order where id=? and user_id=?
2021-03-13 00:43:42.430  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty)
2021-03-13 00:43:42.430  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: select * from t_order_13 where id=? and user_id=? ::: [1615567395814, 13]
2021-03-13 00:43:42.432  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Logic SQL: select * from t_order_item where order_id=? and user_id=?
2021-03-13 00:43:42.432  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty)
2021-03-13 00:43:42.432  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: select * from t_order_item_13 where order_id=? and user_id=? ::: [1615567395814, 13]
2021-03-13 00:43:42.436  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Logic SQL: update t_order set state=? where id=? and user_id=?
2021-03-13 00:43:42.436  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : SQLStatement: MySQLUpdateStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-03-13 00:43:42.436  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: update t_order_13 set state=? where id=? and user_id=? ::: [7, 1615567395814, 13]
2021-03-13 00:43:42.476  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Logic SQL: update t_order_item set state=? where order_id=? and user_id=?
2021-03-13 00:43:42.476  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : SQLStatement: MySQLUpdateStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-03-13 00:43:42.476  INFO 11212 --- [nio-8012-exec-3] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: update t_order_item_13 set state=? where order_id=? and user_id=? ::: [7, 1615567395814, 13]

【删除操作】先查询一遍是否存在，然后删除
2021-03-13 00:43:55.024  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Logic SQL: select * from t_order where id=? and user_id=?
2021-03-13 00:43:55.024  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty)
2021-03-13 00:43:55.024  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: select * from t_order_13 where id=? and user_id=? ::: [1615567395814, 13]
2021-03-13 00:43:55.025  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Logic SQL: select * from t_order_item where order_id=? and user_id=?
2021-03-13 00:43:55.025  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : SQLStatement: MySQLSelectStatement(limit=Optional.empty, lock=Optional.empty)
2021-03-13 00:43:55.025  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: select * from t_order_item_13 where order_id=? and user_id=? ::: [1615567395814, 13]
2021-03-13 00:43:55.027  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Logic SQL: delete from t_order where id=? and user_id=?
2021-03-13 00:43:55.027  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : SQLStatement: MySQLDeleteStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-03-13 00:43:55.027  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: delete from t_order_13 where id=? and user_id=? ::: [1615567395814, 13]
2021-03-13 00:43:55.092  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Logic SQL: delete from t_order_item where order_id=? and user_id=?
2021-03-13 00:43:55.093  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : SQLStatement: MySQLDeleteStatement(orderBy=Optional.empty, limit=Optional.empty)
2021-03-13 00:43:55.093  INFO 11212 --- [nio-8012-exec-5] ShardingSphere-SQL                       : Actual SQL: ds_1 ::: delete from t_order_item_13 where order_id=? and user_id=? ::: [1615567395814, 13]
```
从上可以看出，做增删改查之前，框架根据用户id,确定所属库和表，自动重写了sql，然后执行，从而实现了分库分表操作。    

PS：  
本作业完成时，最开始使用的是JPA，但在修改的时候，一直报错Can not update sharding key, logic table: [t_order], column: [user_id].    
原因是JPA执行update操作时，会重写sql，将每个字段都set一遍，where是主键，但是我使用的是user_id作为分片字段，所以报该错误（不可以修改分片字段的值）。      
解决办法：将JPA替换成mybatis，解决了该问题。（多次尝试不同的写法，想禁止update语句重写，但没效果）。       

### 第8周第2课作业2：（必做）基于 hmily TCC 或 ShardingSphere 的 Atomikos XA 实现一个简单的分布式事务应用 demo（二选一）。  

