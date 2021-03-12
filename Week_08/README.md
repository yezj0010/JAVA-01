**这是第8周作业**  

### 第8周第1课作业2：（必做）设计对前面的订单表数据进行水平分库分表，拆分2个库，每个库16张表。并在新结构在演示常见的增删改查操作。
代码、sql 和配置文件，见项目**week8class1work2_sharding**，sql语句见项目中的**initSimpleShop.sql**，配置文件见**application.yaml**  
本项目为springboot1.5.17集成shardingSphereStarter 5.0.0-alpha   
springboot2.*与shardingSphere5.0.0-alpha存在兼容性问题。  


操作步骤
####1.初始化数据库
建表sql见**initSimpleShop.sql**  
在每个mysql实例中，直接运行initSimpleShop.sql，即可得到32张表，order和order_item各16张。  
建表sql基于第6周的建表语句,区别在于订单id,用户id改成了bigint类型，方便使用框架的分表算法。表名后缀加上数字递增。  

####2.演示分库分表的增删改查
启动项目
打开 http://localhost:8012/swagger-ui.html 页面。  
可以看到四个接口：  
/init1wData   【增】该接口初始化1万条随机数据到mysql中，执行完之后，可以查看数据库中的数据。  
/listByUserId  【查】该接口根据用户id查询用户的所有订单列表，用户id必传。  
/updateState  【改】改接口为根据订单id,用户id修改订单状态。  
/deleteOrder   【删】该接口为根据订单id,用户id，删除订单。  






  