# Mysql练习2

## 建表

```mysql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course` (
  `c_no` varchar(20) NOT NULL,
  `c_name` varchar(20) NOT NULL DEFAULT '',
  `t_no` varchar(20) NOT NULL,
  PRIMARY KEY (`c_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of course
-- ----------------------------
BEGIN;
INSERT INTO `course` VALUES ('01', '语文', '02');
INSERT INTO `course` VALUES ('02', '数学', '01');
INSERT INTO `course` VALUES ('03', '英语', '03');
COMMIT;

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `s_no` varchar(20) NOT NULL,
  `c_no` varchar(20) NOT NULL,
  `s_degree` int(3) DEFAULT NULL,
  PRIMARY KEY (`s_no`,`c_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of score
-- ----------------------------
BEGIN;
INSERT INTO `score` VALUES ('01', '01', 80);
INSERT INTO `score` VALUES ('01', '02', 90);
INSERT INTO `score` VALUES ('01', '03', 99);
INSERT INTO `score` VALUES ('02', '01', 70);
INSERT INTO `score` VALUES ('02', '02', 60);
INSERT INTO `score` VALUES ('02', '03', 80);
INSERT INTO `score` VALUES ('03', '01', 80);
INSERT INTO `score` VALUES ('03', '02', 80);
INSERT INTO `score` VALUES ('03', '03', 80);
INSERT INTO `score` VALUES ('04', '01', 50);
INSERT INTO `score` VALUES ('04', '02', 30);
INSERT INTO `score` VALUES ('04', '03', 20);
INSERT INTO `score` VALUES ('05', '01', 76);
INSERT INTO `score` VALUES ('05', '02', 87);
INSERT INTO `score` VALUES ('06', '01', 31);
INSERT INTO `score` VALUES ('06', '03', 34);
INSERT INTO `score` VALUES ('07', '02', 89);
INSERT INTO `score` VALUES ('07', '03', 98);
COMMIT;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `s_no` varchar(20) NOT NULL,
  `s_name` varchar(20) NOT NULL DEFAULT '',
  `s_birthday` varchar(20) NOT NULL DEFAULT '',
  `s_sex` varchar(10) NOT NULL DEFAULT '',
  PRIMARY KEY (`s_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student
-- ----------------------------
BEGIN;
INSERT INTO `student` VALUES ('01', '赵雷', '1990-01-01', '男');
INSERT INTO `student` VALUES ('02', '钱电', '1990-12-21', '男');
INSERT INTO `student` VALUES ('03', '孙风', '1990-05-20', '男');
INSERT INTO `student` VALUES ('04', '李云', '1990-08-06', '男');
INSERT INTO `student` VALUES ('05', '周梅', '1991-12-01', '女');
INSERT INTO `student` VALUES ('06', '吴兰', '1992-03-01', '女');
INSERT INTO `student` VALUES ('07', '郑竹', '1989-07-01', '女');
INSERT INTO `student` VALUES ('08', '王菊', '1990-01-20', '女');
COMMIT;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `t_no` varchar(20) NOT NULL,
  `t_name` varchar(20) NOT NULL DEFAULT '',
  PRIMARY KEY (`t_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of teacher
-- ----------------------------
BEGIN;
INSERT INTO `teacher` VALUES ('01', '张三');
INSERT INTO `teacher` VALUES ('02', '李四');
INSERT INTO `teacher` VALUES ('03', '王五');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
```

## 查询

- 1、查询"01"课程比"02"课程成绩高的学生的信息及课程分数

```mysql
mysql> SELECT s.* , sc1.sc_degree from student s, score sc1, score sc2 
    -> where s.s_no = sc1.s_no
    -> AND  s.s_no = sc2.s_no
    -> AND sc1.c_no = '01'
    -> AND sc2.c_no = '02'
    -> AND sc1.sc_degree > sc2.sc_degree;
+------+--------+------------+-------+-----------+
| s_no | s_name | s_birthday | s_sex | sc_degree |
+------+--------+------------+-------+-----------+
| 02   | 钱电   | 1990-12-21 | 男    |        70 |
| 04   | 李云   | 1990-08-06 | 男    |        50 |
+------+--------+------------+-------+-----------+
2 rows in set (0.00 sec)
```

- 2、查询"01"课程比"02"课程成绩低的学生的信息及课程分数

```mysql
mysql> SELECT s.* , sc1.sc_degree from student s, score sc1, score sc2 
    -> where s.s_no = sc1.s_no
    -> AND s.s_no = sc2.s_no
    -> AND sc1.c_no = '01'
    -> AND sc2.c_no = '02'
    -> and sc1.sc_degree < sc2.sc_degree;
+------+--------+------------+-------+-----------+
| s_no | s_name | s_birthday | s_sex | sc_degree |
+------+--------+------------+-------+-----------+
| 01   | 赵雷   | 1990-01-01 | 男    |        80 |
| 05   | 周梅   | 1991-12-01 | 女    |        76 |
+------+--------+------------+-------+-----------+
2 rows in set (0.00 sec)
```

- 3、查询平均成绩大于等于60分的同学的学生信息和平均成绩

```mysql
mysql> SELECT s.* , AVG(sc.sc_degree) AS avg_degree FROM student s, score sc
    -> WHERE s.s_no = sc.s_no
    -> GROUP BY s_no
    -> HAVING AVG(sc.sc_degree) >= 60;
+------+--------+------------+-------+------------+
| s_no | s_name | s_birthday | s_sex | avg_degree |
+------+--------+------------+-------+------------+
| 01   | 赵雷   | 1990-01-01 | 男    |    89.6667 |
| 02   | 钱电   | 1990-12-21 | 男    |    70.0000 |
| 03   | 孙风   | 1990-05-20 | 男    |    80.0000 |
| 05   | 周梅   | 1991-12-01 | 女    |    81.5000 |
| 07   | 郑竹   | 1989-07-01 | 女    |    93.5000 |
+------+--------+------------+-------+------------+
5 rows in set (0.01 sec)
```

* 4、查询平均成绩小于60分的同学的学生编号和学生姓名和平均成绩(包括有成绩的和无成绩的)

```mysql
mysql> SELECT s.* , ROUND(AVG(IFNULL(sc.sc_degree,0)),2) AS avg_degree 
    -> FROM student s 
    -> LEFT JOIN score sc on s.s_no = sc.s_no
    -> GROUP BY s.s_no 
    -> HAVING avg_degree < 60;
+------+--------+------------+-------+------------+
| s_no | s_name | s_birthday | s_sex | avg_degree |
+------+--------+------------+-------+------------+
| 04   | 李云   | 1990-08-06 | 男    |      33.33 |
| 06   | 吴兰   | 1992-03-01 | 女    |      32.50 |
| 08   | 王菊   | 1990-01-20 | 女    |       0.00 |
+------+--------+------------+-------+------------+
3 rows in set (0.00 sec)
```

* 5、查询所有同学的学生编号、学生姓名、选课总数、所有课程的总成绩

```mysql
mysql> SELECT s.s_no , s.s_name,count(sc.c_no),SUM(sc.sc_degree) 
    -> FROM student s, score sc
    -> WHERE s.s_no = sc.s_no
    -> GROUP BY sc.s_no;
+------+--------+----------------+-------------------+
| s_no | s_name | count(sc.c_no) | SUM(sc.sc_degree) |
+------+--------+----------------+-------------------+
| 01   | 赵雷   |              3 |               269 |
| 02   | 钱电   |              3 |               210 |
| 03   | 孙风   |              3 |               240 |
| 04   | 李云   |              3 |               100 |
| 05   | 周梅   |              2 |               163 |
| 06   | 吴兰   |              2 |                65 |
| 07   | 郑竹   |              2 |               187 |
+------+--------+----------------+-------------------+
7 rows in set (0.00 sec)
```

```mysql
mysql> select a.s_no,a.s_name,count(b.c_no) as sum_course,sum(b.sc_degree) as sum_score from 
    -> student a 
    -> left join score b on a.s_no=b.s_no
    -> GROUP BY a.s_no,a.s_name;
+------+--------+------------+-----------+
| s_no | s_name | sum_course | sum_score |
+------+--------+------------+-----------+
| 01   | 赵雷   |          3 |       269 |
| 02   | 钱电   |          3 |       210 |
| 03   | 孙风   |          3 |       240 |
| 04   | 李云   |          3 |       100 |
| 05   | 周梅   |          2 |       163 |
| 06   | 吴兰   |          2 |        65 |
| 07   | 郑竹   |          2 |       187 |
| 08   | 王菊   |          0 |      NULL |
+------+--------+------------+-----------+
8 rows in set (0.00 sec)
```

- 6、查询"李"姓老师的数量

```mysql
mysql> SELECT COUNT(t_no) FROM teacher WHERE t_name LIKE '李%';
+-------------+
| COUNT(t_no) |
+-------------+
|           1 |
+-------------+
1 row in set (0.00 sec)
```

- 7、查询学过"张三"老师授课的同学的信息

```mysql
mysql> SELECT s.*FROM student s WHERE s.s_no IN (
    -> SELECT s_no FROM score sc WHERE sc.c_no=(
    -> SELECT c_no FROM course WHERE t_no=(
    -> SELECT t_no FROM teacher WHERE t_name='张三')));
+------+--------+------------+-------+
| s_no | s_name | s_birthday | s_sex |
+------+--------+------------+-------+
| 01   | 赵雷   | 1990-01-01 | 男    |
| 02   | 钱电   | 1990-12-21 | 男    |
| 03   | 孙风   | 1990-05-20 | 男    |
| 04   | 李云   | 1990-08-06 | 男    |
| 05   | 周梅   | 1991-12-01 | 女    |
| 07   | 郑竹   | 1989-07-01 | 女    |
+------+--------+------------+-------+
6 rows in set (0.00 sec)
```

```mysql
SELECT
	a.* 
FROM
	student a
	JOIN score b ON a.s_no = b.s_no 
WHERE
	b.c_no IN ( SELECT c_no FROM course WHERE t_no = ( SELECT t_no FROM teacher WHERE t_name = '张三' ) );
```

- 8、查询没学过"张三"老师授课的同学的信息

```mysql
mysql> SELECT
    -> s.* 
    -> FROM
    -> student s 
    -> WHERE
    -> s.s_no NOT IN ( SELECT s_no FROM score sc WHERE sc.c_no = ( SELECT c_no FROM course WHERE t_no = ( SELECT t_no FROM teacher WHERE t_name = '张三' ) ) );
+------+--------+------------+-------+
| s_no | s_name | s_birthday | s_sex |
+------+--------+------------+-------+
| 06   | 吴兰   | 1992-03-01 | 女    |
| 08   | 王菊   | 1990-01-20 | 女    |
+------+--------+------------+-------+
2 rows in set (0.00 sec)
```

```mysql
SELECT
	a.* 
FROM
	student a 
WHERE
	a.s_no NOT IN (
	SELECT
		b.s_no 
	FROM
		score b 
	WHERE
	b.c_no IN ( SELECT c_no FROM course WHERE t_no = ( SELECT t_no FROM teacher WHERE t_name = '张三' ) ) 
	);
```

- 9、查询学过编号为"01"并且也学过编号为"02"的课程的同学的信息

```mysql
mysql> SELECT s.* 
    -> FROM student s, score sc1,score sc2 
    -> WHERE s.s_no = sc1.s_no 
    -> AND s.s_no = sc2.s_no
    -> AND sc1.c_no = '01'
    -> AND sc2.c_no = '02';
+------+--------+------------+-------+
| s_no | s_name | s_birthday | s_sex |
+------+--------+------------+-------+
| 01   | 赵雷   | 1990-01-01 | 男    |
| 02   | 钱电   | 1990-12-21 | 男    |
| 03   | 孙风   | 1990-05-20 | 男    |
| 04   | 李云   | 1990-08-06 | 男    |
| 05   | 周梅   | 1991-12-01 | 女    |
+------+--------+------------+-------+
5 rows in set (0.00 sec)
```

- 10、查询学过编号为"01"但是没有学过编号为"02"的课程的同学的信息

```mysql
mysql> SELECT s.* 
    -> FROM student s
    -> WHERE s.s_no in (SELECT s_no FROM score WHERE c_no = '01')
    -> AND s.s_no NOT in (SELECT s_no FROM score WHERE c_no = '02');
+------+--------+------------+-------+
| s_no | s_name | s_birthday | s_sex |
+------+--------+------------+-------+
| 06   | 吴兰   | 1992-03-01 | 女    |
+------+--------+------------+-------+
1 row in set (0.00 sec)

```

