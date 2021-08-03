/*
 Navicat MySQL Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 50551
 Source Host           : localhost:3306
 Source Schema         : yulin-bookcity

 Target Server Type    : MySQL
 Target Server Version : 50551
 File Encoding         : 65001

 Date: 03/08/2021 22:16:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `bookName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `surplus` int(255) NULL DEFAULT 0,
  `bookCover` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `price` double NULL DEFAULT 9999,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES (1, '浪潮之巅', 5, '/upload/book/浪潮之巅.jpg', 100);
INSERT INTO `book` VALUES (2, 'C程序设计', 1, '/upload/book/C程序设计.jpg', 60);
INSERT INTO `book` VALUES (3, 'JAVA程序设计', 13, '', 40);
INSERT INTO `book` VALUES (4, 'ES6快速入门', 10, '', 60);
INSERT INTO `book` VALUES (5, '微机原理与汇编', 4, '', 40);
INSERT INTO `book` VALUES (6, '高等数学', 16, '', 30);
INSERT INTO `book` VALUES (7, '线性代数', 10, '', 25);
INSERT INTO `book` VALUES (9, '施瓦辛格健身全书', 5, '', 60);
INSERT INTO `book` VALUES (10, '运动解剖学', 8, '', 70);
INSERT INTO `book` VALUES (11, '苏东坡传', 10, '', 60);
INSERT INTO `book` VALUES (12, '李白传', 10, '', 55);
INSERT INTO `book` VALUES (43, '力量训练基础', 15, NULL, 60);
INSERT INTO `book` VALUES (50, 'aa', 112, '/upload/book/aa.png', 133);

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userId` int(10) NOT NULL,
  `bookId` int(10) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of collection
-- ----------------------------
INSERT INTO `collection` VALUES (64, 3, 1);
INSERT INTO `collection` VALUES (65, 3, 50);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `balance` double NULL DEFAULT 0,
  `context` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '',
  `isAdmin` tinyint(1) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (3, 'admin', '111', '/upload/avatar/3/hd.png', 100, NULL, 1);
INSERT INTO `user` VALUES (6, 'sb', '333', '', 0, '', 0);

SET FOREIGN_KEY_CHECKS = 1;
