/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : selectTest

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 28/04/2020 22:34:31
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for card
-- ----------------------------
DROP TABLE IF EXISTS `card`;
CREATE TABLE `card` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of card
-- ----------------------------
BEGIN;
INSERT INTO `card` VALUES (1, '饭卡');
INSERT INTO `card` VALUES (2, '建行卡');
INSERT INTO `card` VALUES (3, '农行卡');
INSERT INTO `card` VALUES (4, '工商卡');
INSERT INTO `card` VALUES (5, '邮政卡');
COMMIT;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `c_no` varchar(20) NOT NULL COMMENT '课程号',
  `c_name` varchar(20) NOT NULL COMMENT '课程名称',
  `t_no` varchar(20) NOT NULL COMMENT '教师编号 外键关联teacher表',
  PRIMARY KEY (`c_no`),
  KEY `t_no` (`t_no`),
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`t_no`) REFERENCES `teacher` (`t_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of course
-- ----------------------------
BEGIN;
INSERT INTO `course` VALUES ('3-105', '计算机导论', '825');
INSERT INTO `course` VALUES ('3-245', '操作系统', '804');
INSERT INTO `course` VALUES ('6-166', '数字电路', '856');
INSERT INTO `course` VALUES ('9-888', '高等数学', '831');
COMMIT;

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade` (
  `low` int(3) DEFAULT NULL,
  `upp` int(3) DEFAULT NULL,
  `grade` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of grade
-- ----------------------------
BEGIN;
INSERT INTO `grade` VALUES (90, 100, 'A');
INSERT INTO `grade` VALUES (80, 89, 'B');
INSERT INTO `grade` VALUES (70, 79, 'c');
INSERT INTO `grade` VALUES (60, 69, 'D');
INSERT INTO `grade` VALUES (0, 59, 'E');
COMMIT;

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS `person`;
CREATE TABLE `person` (
  `id` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `cardId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of person
-- ----------------------------
BEGIN;
INSERT INTO `person` VALUES (1, '张三', 1);
INSERT INTO `person` VALUES (2, '李四', 3);
INSERT INTO `person` VALUES (3, '王五', 6);
COMMIT;

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `s_no` varchar(20) NOT NULL COMMENT '成绩表的编号 依赖学生学号',
  `c_no` varchar(20) NOT NULL COMMENT '课程号 依赖于课程表中的c_id',
  `sc_degree` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`s_no`,`c_no`),
  KEY `c_no` (`c_no`),
  CONSTRAINT `score_ibfk_1` FOREIGN KEY (`s_no`) REFERENCES `student` (`s_no`),
  CONSTRAINT `score_ibfk_2` FOREIGN KEY (`c_no`) REFERENCES `course` (`c_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of score
-- ----------------------------
BEGIN;
INSERT INTO `score` VALUES ('101', '3-105', 90);
INSERT INTO `score` VALUES ('102', '3-105', 91);
INSERT INTO `score` VALUES ('103', '3-105', 92);
INSERT INTO `score` VALUES ('103', '3-245', 86);
INSERT INTO `score` VALUES ('103', '6-166', 85);
INSERT INTO `score` VALUES ('104', '3-105', 89);
INSERT INTO `score` VALUES ('105', '3-105', 88);
INSERT INTO `score` VALUES ('105', '3-245', 75);
INSERT INTO `score` VALUES ('105', '6-166', 79);
INSERT INTO `score` VALUES ('109', '3-105', 76);
INSERT INTO `score` VALUES ('109', '3-245', 68);
INSERT INTO `score` VALUES ('109', '6-166', 81);
COMMIT;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `s_no` varchar(20) NOT NULL COMMENT '学生学号',
  `s_name` varchar(20) NOT NULL COMMENT '学生姓名 不能为空',
  `s_sex` varchar(10) NOT NULL COMMENT '学生性别',
  `s_birthday` datetime DEFAULT NULL COMMENT '学生生日',
  `s_class` varchar(20) DEFAULT NULL COMMENT '学生所在的班级',
  PRIMARY KEY (`s_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of student
-- ----------------------------
BEGIN;
INSERT INTO `student` VALUES ('101', '曾华', '男', '1977-09-01 00:00:00', '95033');
INSERT INTO `student` VALUES ('102', '匡明', '男', '1975-10-02 00:00:00', '95031');
INSERT INTO `student` VALUES ('103', '王丽', '女', '1976-01-23 00:00:00', '95033');
INSERT INTO `student` VALUES ('104', '李军', '男', '1976-02-20 00:00:00', '95033');
INSERT INTO `student` VALUES ('105', '王芳', '女', '1975-02-10 00:00:00', '95031');
INSERT INTO `student` VALUES ('106', '陆军', '男', '1974-06-03 00:00:00', '95031');
INSERT INTO `student` VALUES ('107', '王尼玛', '男', '1976-02-20 00:00:00', '95033');
INSERT INTO `student` VALUES ('108', '张全蛋', '男', '1975-02-10 00:00:00', '95031');
INSERT INTO `student` VALUES ('109', '赵铁柱', '男', '1974-06-03 00:00:00', '95031');
COMMIT;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `t_no` varchar(20) NOT NULL COMMENT '教师编号',
  `t_name` varchar(20) NOT NULL COMMENT '教师姓名',
  `t_sex` varchar(20) NOT NULL COMMENT '教师性别',
  `t_birthday` datetime DEFAULT NULL COMMENT '教师生日',
  `t_rof` varchar(20) NOT NULL COMMENT '教师职称',
  `t_depart` varchar(20) NOT NULL COMMENT '教师所在的部门',
  PRIMARY KEY (`t_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of teacher
-- ----------------------------
BEGIN;
INSERT INTO `teacher` VALUES ('804', '李诚', '男', '1958-12-02 00:00:00', '副教授', '计算机系');
INSERT INTO `teacher` VALUES ('825', '王萍', '女', '1972-05-05 00:00:00', '助教', '计算机系');
INSERT INTO `teacher` VALUES ('831', '刘冰', '女', '1977-08-14 00:00:00', '助教', '电子工程系');
INSERT INTO `teacher` VALUES ('856', '张旭', '男', '1969-03-12 00:00:00', '讲师', '电子工程系');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(20) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'a', 1000);
INSERT INTO `user` VALUES (2, 'b', 1000);
INSERT INTO `user` VALUES (3, '小明', 1000);
INSERT INTO `user` VALUES (4, '淘宝店', 1000);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
