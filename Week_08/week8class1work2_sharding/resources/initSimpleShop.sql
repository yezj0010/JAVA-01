-- 先使用docker启动好两个mysql实例，每个实例执行建库语句
create database if not exists shop default character set = 'utf8mb4';

use shop;

-- 使用存储过程，在每个库中单独运行，创建出16张t_order表和16张t_order_item表
CREATE DEFINER=`root`@`%` PROCEDURE `createTablesWithIndex`()
BEGIN
		DECLARE `@i` INT(11);
		DECLARE `@createOrderSql` VARCHAR(2560);
		DECLARE `@createOrderImteSql` VARCHAR(2560);
		SET `@i`=0;
		WHILE  `@i`< 16 DO
			SET @createOrderSql = CONCAT('CREATE TABLE IF NOT EXISTS t_order_', `@i` ,'(
  `id` bigint(0) NOT NULL,
  `user_id` bigint(0) DEFAULT NULL,
  `total_amount` decimal(10, 2) DEFAULT NULL,
  `total_num` int(0) DEFAULT NULL,
  `receive_name` varchar(50) DEFAULT NULL,
  `receive_mobile` varchar(20) DEFAULT NULL,
  `province` varchar(50) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `detail` varchar(255) DEFAULT NULL,
  `state` tinyint(0) DEFAULT NULL,
  `create_time` bigint(0) DEFAULT NULL,
  `update_time` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`)
) ENGINE = InnoDB');
			PREPARE stmt FROM @createOrderSql;
			EXECUTE stmt;
--
			set @createOrderImteSql = concat('CREATE TABLE `t_order_item_',`@i`,'`  (
  `id` bigint(0) NOT NULL,
  `order_id` bigint(0) DEFAULT NULL,
  `user_id` bigint(0) DEFAULT NULL,
  `product_id` varchar(24) DEFAULT NULL,
  `product_spec_id` varchar(24) DEFAULT NULL,
  `merchant_id` varchar(24) DEFAULT NULL,
  `product_price` decimal(10, 2) DEFAULT NULL,
  `product_img_url` varchar(255) DEFAULT NULL,
  `product_num` int(0) DEFAULT NULL,
  `state` tinyint(0) DEFAULT NULL,
  `create_time` bigint(0) DEFAULT NULL,
  `update_time` bigint(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `user_id` (`user_id`),
  KEY `order_id` (`order_id`)
) ENGINE = InnoDB');
			prepare stmt from @createOrderImteSql;
			execute stmt;
			SET `@i`= `@i`+1;
		END WHILE;
END;

-- 执行存储过程
call createTablesWithIndex();