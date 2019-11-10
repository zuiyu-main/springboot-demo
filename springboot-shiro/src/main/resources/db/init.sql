/*
 Navicat Premium Data Transfer

 Source Server         : docker_mysql
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : 127.0.0.1:3306
 Source Schema         : springboot-demo

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 10/11/2019 16:26:19
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
                                  `id` bigint(20) NOT NULL,
                                  `parent_id` bigint(20) DEFAULT NULL,
                                  `res_name` varchar(50) DEFAULT NULL,
                                  `res_type` varchar(10) DEFAULT NULL,
                                  `permission` varchar(20) DEFAULT NULL,
                                  `url` varchar(100) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` VALUES (1, NULL, '系统管理', 'menu', 'system', NULL);
INSERT INTO `sys_permission` VALUES (2, 1, '用户管理', 'menu', 'systemUserList', '/user/userList');
INSERT INTO `sys_permission` VALUES (3, 1, '角色管理', 'menu', 'systemRole', '/roles');
INSERT INTO `sys_permission` VALUES (4, NULL, '一级菜单', 'menu', 'menu', NULL);
INSERT INTO `sys_permission` VALUES (5, 4, '二级菜单1', 'menu', 'menuXxx', '/xxx');
INSERT INTO `sys_permission` VALUES (6, 4, '二级菜单2', 'menu', 'menuYyy', '/yyy');
INSERT INTO `sys_permission` VALUES (7, 2, '用户添加', 'button', 'systemUserAdd', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `role_id` bigint(20) NOT NULL,
                            `role_name` varchar(30) DEFAULT NULL,
                            PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '用户管理员');
INSERT INTO `sys_role` VALUES (2, '普通用户');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
                                       `role_id` bigint(20) NOT NULL,
                                       `permission_id` bigint(20) NOT NULL,
                                       PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色权限表';

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_permission` VALUES (1, 1);
INSERT INTO `sys_role_permission` VALUES (1, 2);
INSERT INTO `sys_role_permission` VALUES (1, 3);
INSERT INTO `sys_role_permission` VALUES (1, 4);
INSERT INTO `sys_role_permission` VALUES (1, 5);
INSERT INTO `sys_role_permission` VALUES (1, 6);
INSERT INTO `sys_role_permission` VALUES (1, 7);
INSERT INTO `sys_role_permission` VALUES (2, 1);
INSERT INTO `sys_role_permission` VALUES (2, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `user_id` bigint(20) NOT NULL,
                            `user_name` varchar(50) DEFAULT NULL,
                            `full_name` varchar(20) DEFAULT NULL,
                            `password` varchar(32) DEFAULT NULL,
                            `salt` varchar(20) DEFAULT NULL,
                            PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='当前系统用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', 'admin', '90c75b705a6931c66d9e8660062bb625', '4');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                                 `user_id` bigint(20) NOT NULL,
                                 `role_id` bigint(20) NOT NULL,
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
COMMIT;

-- ----------------------------
