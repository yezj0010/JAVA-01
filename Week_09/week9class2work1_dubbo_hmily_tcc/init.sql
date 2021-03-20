create database if not EXISTS account_cny;
use account_cny;

DROP TABLE IF EXISTS `t_account_cny`;
CREATE TABLE `t_account_cny`  (
  `user_id` bigint(0) NOT NULL,
  `amt` decimal(15, 2) NOT NULL COMMENT '可用总金额',
  `state` tinyint(0) NULL DEFAULT NULL COMMENT '用户状态 0-异常 1-正常',
  `version` int(0) NULL DEFAULT 0 COMMENT '版本号 初始0',
  `create_time` bigint(0) NULL DEFAULT NULL,
  `update_time` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


INSERT INTO `t_account_cny` VALUES (1, 1000.00, 1, 0, NULL, NULL);
INSERT INTO `t_account_cny` VALUES (2, 0.00, 1, 0, NULL, NULL);


DROP TABLE IF EXISTS `t_account_cny_frozen`;
CREATE TABLE `t_account_cny_frozen`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `amt` decimal(15, 2) NOT NULL COMMENT '冻结金额',
  `state` tinyint(0) NULL DEFAULT NULL COMMENT '冻结状态 0-解冻 1-冻结',
  `version` int(0) NULL DEFAULT 0 COMMENT '版本号 初始0',
  `create_time` bigint(0) NULL DEFAULT NULL,
  `update_time` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;


create database if not EXISTS account_usd;
use account_usd;

DROP TABLE IF EXISTS `t_account_usd`;
CREATE TABLE `t_account_usd`  (
  `user_id` bigint(0) NOT NULL,
  `amt` decimal(15, 2) NOT NULL COMMENT '可用总金额',
  `state` tinyint(0) NULL DEFAULT NULL COMMENT '用户状态 0-异常 1-正常',
  `version` int(0) NULL DEFAULT 0 COMMENT '版本号 初始0',
  `create_time` bigint(0) NULL DEFAULT NULL,
  `update_time` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `t_account_usd` VALUES (1, 0.00, 1, 0, NULL, NULL);
INSERT INTO `t_account_usd` VALUES (2, 200.00, 1, 0, NULL, NULL);

DROP TABLE IF EXISTS `t_account_usd_frozen`;
CREATE TABLE `t_account_usd_frozen`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(0) NULL DEFAULT NULL,
  `amt` decimal(15, 2) NOT NULL COMMENT '冻结金额',
  `state` tinyint(0) NULL DEFAULT NULL COMMENT '冻结状态 0-解冻 1-冻结',
  `version` int(0) NULL DEFAULT 0 COMMENT '版本号 初始0',
  `create_time` bigint(0) NULL DEFAULT NULL,
  `update_time` bigint(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


create database if not EXISTS hmily;