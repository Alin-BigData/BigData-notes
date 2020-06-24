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

- 11、查询没有学全所有课程的同学的信息

```mysql
mysql> SELECT
    -> * 
    -> FROM
    -> student 
    -> WHERE
    -> s_no IN ( SELECT s_no AS num FROM score GROUP BY s_no HAVING COUNT( c_no ) < ( SELECT COUNT( c_no ) FROM course ) );
+------+--------+------------+-------+
| s_no | s_name | s_birthday | s_sex |
+------+--------+------------+-------+
| 05   | 周梅   | 1991-12-01 | 女    |
| 06   | 吴兰   | 1992-03-01 | 女    |
| 07   | 郑竹   | 1989-07-01 | 女    |
+------+--------+------------+-------+
3 rows in set (0.00 sec)
```

- 12、查询至少有一门课与学号为"01"的同学所学相同的同学的信息

```mysql
mysql> SELECT
    -> s.* 
    -> FROM
    -> student s 
    -> WHERE
    -> s.s_no IN ( SELECT DISTINCT ( s_no ) FROM score WHERE c_no IN ( SELECT c_no FROM score WHERE s_no = '01' ) );
+------+--------+------------+-------+
| s_no | s_name | s_birthday | s_sex |
+------+--------+------------+-------+
| 01   | 赵雷   | 1990-01-01 | 男    |
| 02   | 钱电   | 1990-12-21 | 男    |
| 03   | 孙风   | 1990-05-20 | 男    |
| 04   | 李云   | 1990-08-06 | 男    |
| 05   | 周梅   | 1991-12-01 | 女    |
| 06   | 吴兰   | 1992-03-01 | 女    |
| 07   | 郑竹   | 1989-07-01 | 女    |
+------+--------+------------+-------+
7 rows in set (0.00 sec)
```

- 13、查询和"01"号的同学学习的课程完全相同的其他同学的信息



- 14、查询没学过"张三"老师讲授的任一门课程的学生姓名

查询t_no，t_no唯一，查询c_no，c_no不唯一，因此用in，可能某学生选了这个老师教授的C1，C2，所以s_no要用Distinct。最后用NOT IN

```mysql
mysql> SELECT
    -> s.s_name 
    -> FROM
    -> student s 
    -> WHERE
    -> s.s_no NOT IN ( SELECT DISTINCT ( s_no ) FROM score WHERE c_no IN ( SELECT c_no FROM course WHERE t_no = ( SELECT t_no FROM teacher WHERE t_name = '张三' ) ) );
+--------+
| s_name |
+--------+
| 吴兰   |
| 王菊   |
+--------+
2 rows in set (0.00 sec)
```

- 15、查询两门及其以上不及格课程的同学的学号，姓名及其平均成绩

on > where > having 程度

```mysql
mysql> SELECT
    -> s.s_no,
    -> s.s_name,
    -> AVG( sc.sc_degree ) 
    -> FROM
    -> student s
    -> LEFT JOIN score sc ON s.s_no = sc.s_no 
    -> WHERE
    -> sc.s_no IN ( SELECT s_no FROM score WHERE sc_degree < 60 GROUP BY s_no HAVING COUNT( c_no ) > 1 ) 
    -> GROUP BY
    -> sc.s_no;
+------+--------+---------------------+
| s_no | s_name | AVG( sc.sc_degree ) |
+------+--------+---------------------+
| 04   | 李云   |             33.3333 |
| 06   | 吴兰   |             32.5000 |
+------+--------+---------------------+
2 rows in set (0.00 sec)
```

* 16、检索"01"课程分数小于60，按分数降序排列的学生信息

```mysql
mysql> SELECT
    -> s.* 
    -> FROM
    -> student s 
    -> WHERE
    -> s_no IN ( SELECT s_no FROM score WHERE c_no = '01' AND sc_degree < 60 ORDER BY sc_degree DESC);
+------+--------+------------+-------+
| s_no | s_name | s_birthday | s_sex |
+------+--------+------------+-------+
| 04   | 李云   | 1990-08-06 | 男    |
| 06   | 吴兰   | 1992-03-01 | 女    |
+------+--------+------------+-------+
2 rows in set (0.01 sec)
```

```mysql
select a.*,b.c_no,b.sc_degree from 
	student a,score b 
	where a.s_no = b.s_no and b.c_no='01' and b.sc_degree<60 ORDER BY b.sc_degree DESC;
	
```

* 17、按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩

```mysql
mysql> SELECT sc.s_no ,(SELECT sc_degree FROM score  WHERE s_no = sc.s_no AND c_no = '01') AS yuwen,
    -> (SELECT sc_degree FROM score  WHERE s_no = sc.s_no AND c_no = '02') AS shuxue,
    -> (SELECT sc_degree FROM score WHERE s_no = sc.s_no AND c_no = '03') as yingyu,
    -> AVG(sc.sc_degree) AS avg_degree FROM score sc GROUP BY sc.s_no ORDER BY avg_degree DESC;
+------+-------+--------+--------+------------+
| s_no | yuwen | shuxue | yingyu | avg_degree |
+------+-------+--------+--------+------------+
| 07   |  NULL |     89 |     98 |    93.5000 |
| 01   |    80 |     90 |     99 |    89.6667 |
| 05   |    76 |     87 |   NULL |    81.5000 |
| 03   |    80 |     80 |     80 |    80.0000 |
| 02   |    70 |     60 |     80 |    70.0000 |
| 04   |    50 |     30 |     20 |    33.3333 |
| 06   |    31 |   NULL |     34 |    32.5000 |
+------+-------+--------+--------+------------+
7 rows in set (0.01 sec)
```

* 18.查询各科成绩最高分、最低分和平均分：以如下形式显示：课程ID，课程name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率--及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
* 19、按各科成绩进行排序，并显示排名
* 20、查询学生的总成绩并进行排名
* 21、查询不同老师所教不同课程平均分从高到低显示
* 22、查询所有课程的成绩第2名到第3名的学生信息及该课程成绩
* 23、统计各科成绩各分数段人数：课程编号,课程名称,[100-85],[85-70],[70-60],[0-60]及所占百分比
*  24、查询学生平均成绩及其名次
* 25、查询各科成绩前三名的记录		-- 1.选出b表比a表成绩大的所有组 		-- 2.选出比当前id成绩大的 小于三个的

```mysql
mysql> SELECT
    -> a.s_no,
    -> a.c_no,
    -> a.sc_degree 
    -> FROM
    -> score a
    -> LEFT JOIN score b ON a.c_no = b.c_no 
    -> AND a.sc_degree < b.sc_degree 
    -> GROUP BY
    -> a.s_no,
    -> a.c_no,
    -> a.sc_degree 
    -> HAVING
    -> COUNT( b.s_no ) < 3 
    -> ORDER BY
    -> a.c_no,
    -> a.sc_degree DESC;
+------+------+-----------+
| s_no | c_no | sc_degree |
+------+------+-----------+
| 03   | 01   |        80 |
| 01   | 01   |        80 |
| 05   | 01   |        76 |
| 01   | 02   |        90 |
| 07   | 02   |        89 |
| 05   | 02   |        87 |
| 01   | 03   |        99 |
| 07   | 03   |        98 |
| 02   | 03   |        80 |
| 03   | 03   |        80 |
+------+------+-----------+
10 rows in set (0.00 sec)
```

