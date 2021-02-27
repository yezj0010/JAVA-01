**这是第6周作业**  

### 第6周第2课作业2：（必做）：基于电商交易场景（用户、商品、订单），设计一套简单的表结构
简单电商交易场景的核心ddl语句见文件夹**week6class2work2_shop_sql**中的**shop.sql**  
包含的表如下：  

#### 用户
用户表 t_user  
用户收货地址表  t_user_address  

#### 商品
商品表 t_product  
商品图片表 t_product_img  
商品具体规格表（价格库存等信息在此表中） t_product_spec  
商品分类表 t_product_category  
商品分类与sku关系表 t_product_category_sku    
sku表  t_sku  
sku值列表  t_sku_item  

#### 订单
购物车表 t_shop_cart  
购物车项表 t_shop_cart_item  
订单表（含有总金额，等信息）  t_order  
订单项表（每个订单包括一个商品以及该商品数量等快照信息） t_order_item   



