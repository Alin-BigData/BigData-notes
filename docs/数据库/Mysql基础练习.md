# Mysql练习

学生表student: 学号 姓名 性别 出生日期 所在班级

```mysql
CREATE TABLE student(  
  s_no VARCHAR(20) PRIMARY KEY COMMENT'学生学号',  
  s_name VARCHAR(20) NOT NULL COMMENT'学生姓名 不能为空',  
  s_sex VARCHAR(10) NOT NULL COMMENT'学生性别',  
  s_birthday DATETIME COMMENT'学生生日',  
  s_class VARCHAR(20) COMMENT'学生所在的班级');
```

教师表teacher: 教师编号 教师名字 教师性别 出生日期 职称 所在部门

```mysql
CREATE TABLE teacher(
    t_no VARCHAR(20) PRIMARY KEY COMMENT'教师编号',
    t_name VARCHAR(20) NOT NULL COMMENT'教师姓名',
    t_sex VARCHAR(20) NOT NULL COMMENT'教师性别',
    t_birthday DATETIME COMMENT'教师生日',
    t_rof VARCHAR(20) NOT NULL COMMENT'教师职称',
    t_depart VARCHAR(20) NOT NULL COMMENT'教师所在的部门'
);
```

课程表course:课程号 课程名称 教师编号

```mysql
CREATE TABLE course(
    c_no VARCHAR(20) PRIMARY KEY COMMENT'课程号',
    c_name VARCHAR(20) NOT NULL COMMENT'课程名称',
    t_no VARCHAR(20) NOT NULL COMMENT'教师编号 外键关联teacher表',
    FOREIGN KEY(t_no) references teacher(t_no)
);
```

成绩表score:学号 课程号 成绩

```mysql
CREATE TABLE score (
    s_no VARCHAR(20) NOT NULL COMMENT'成绩表的编号 依赖学生学号',
    c_no VARCHAR(20)  NOT NULL COMMENT'课程号 依赖于课程表中的c_id',
    sc_degree decimal,
    foreign key(s_no) references student(s_no),
    foreign key(c_no) references course(c_no),
    PRIMARY KEY(s_no,c_no)
);
```

grade,等级表

```sql
CREATE TABLE grade(
    low INT(3),
    upp INT(3),
    grade CHAR(1)
);
INSERT INTO grade VALUES(90,100,'A');
INSERT INTO grade VALUES(80,89,'B');
INSERT INTO grade VALUES(70,79,'c');
INSERT INTO grade VALUES(60,69,'D');
INSERT INTO grade VALUES(0,59,'E');
```

插入数据

```mysql
--学生表数据
INSERT INTO student VALUES('101','曾华','男','1977-09-01','95033');
INSERT INTO student VALUES('102','匡明','男','1975-10-02','95031');
INSERT INTO student VALUES('103','王丽','女','1976-01-23','95033');
INSERT INTO student VALUES('104','李军','男','1976-02-20','95033');
INSERT INTO student VALUES('105','王芳','女','1975-02-10','95031');
INSERT INTO student VALUES('106','陆军','男','1974-06-03','95031');
INSERT INTO student VALUES('107','王尼玛','男','1976-02-20','95033');
INSERT INTO student VALUES('108','张全蛋','男','1975-02-10','95031');
INSERT INTO student VALUES('109','赵铁柱','男','1974-06-03','95031');

--教师表数据
INSERT INTO teacher VALUES('804','李诚','男','1958-12-02','副教授','计算机系');
INSERT INTO teacher VALUES('856','张旭','男','1969-03-12','讲师','电子工程系');
INSERT INTO teacher VALUES('825','王萍','女','1972-05-05','助教','计算机系');
INSERT INTO teacher VALUES('831','刘冰','女','1977-08-14','助教','电子工程系');

--添加课程表
INSERT INTO course VALUES('3-105','计算机导论','825');
INSERT INTO course VALUES('3-245','操作系统','804');
INSERT INTO course VALUES('6-166','数字电路','856');
INSERT INTO course VALUES('9-888','高等数学','831');

--添加成绩表
INSERT INTO score VALUES('103','3-245','86');
INSERT INTO score VALUES('105','3-245','75');
INSERT INTO score VALUES('109','3-245','68');
INSERT INTO score VALUES('103','3-105','92');

INSERT INTO score VALUES('105','3-105','88');
INSERT INTO score VALUES('109','3-105','76');
INSERT INTO score VALUES('103','6-166','85');

INSERT INTO score VALUES('105','6-166','79');
INSERT INTO score VALUES('109','6-166','81');
```

## 查询

- 查询student表中所有记录的s_name,s_sex和s_class列

```
mysql> SELECT s_no,s_name,s_class FROM  student;
+------+-----------+---------+
| s_no | s_name    | s_class |
+------+-----------+---------+
| 101  | 曾华      | 95033   |
| 102  | 匡明      | 95031   |
| 103  | 王丽      | 95033   |
| 104  | 李军      | 95033   |
| 105  | 王芳      | 95031   |
| 106  | 陆军      | 95031   |
| 107  | 王尼玛    | 95033   |
| 108  | 张全蛋    | 95031   |
| 109  | 赵铁柱    | 95031   |
+------+-----------+---------+
```

- 查询教师所有的单位但是不重复的t_depart列

```
mysql> SELECT DISTINCT (t_depart) FROM teacher;
+-----------------+
| t_depart        |
+-----------------+
| 计算机系        |
| 电子工程系      |
+-----------------+
```

- 查询score表中成绩在60-80之间所有的记录(sc_degree)

```
mysql> select * from score where sc_degree between 61 and 79;
+------+-------+-----------+
| s_no | c_no  | sc_degree |
+------+-------+-----------+
| 105  | 3-245 |        75 |
| 105  | 6-166 |        79 |
| 109  | 3-105 |        76 |
| 109  | 3-245 |        68 |
+------+-------+-----------+
4 rows in set (0.00 sec)
```

- 查询score表中成绩为85, 86, 或者88的记录(sc_degree)

```
mysql> select * from score where sc_degree in (85,86,88);
+------+-------+-----------+
| s_no | c_no  | sc_degree |
+------+-------+-----------+
| 103  | 3-245 |        86 |
| 103  | 6-166 |        85 |
| 105  | 3-105 |        88 |
+------+-------+-----------+
3 rows in set (0.00 sec)
```

- 查询student表中'95031'班或者性别为'女'的同学记录

```
mysql> select * from student where s_class='95031' or s_sex='女';
+------+-----------+-------+---------------------+---------+
| s_no | s_name    | s_sex | s_birthday          | s_class |
+------+-----------+-------+---------------------+---------+
| 102  | 匡明      | 男    | 1975-10-02 00:00:00 | 95031   |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   |
| 106  | 陆军      | 男    | 1974-06-03 00:00:00 | 95031   |
| 108  | 张全蛋    | 男    | 1975-02-10 00:00:00 | 95031   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |
+------+-----------+-------+---------------------+---------+
6 rows in set (0.00 sec)
```

- 以class降序查询student表中所有的记录

```
mysql> select * from student order by s_class desc;
+------+-----------+-------+---------------------+---------+
| s_no | s_name    | s_sex | s_birthday          | s_class |
+------+-----------+-------+---------------------+---------+
| 101  | 曾华      | 男    | 1977-09-01 00:00:00 | 95033   |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   |
| 104  | 李军      | 男    | 1976-02-20 00:00:00 | 95033   |
| 107  | 王尼玛    | 男    | 1976-02-20 00:00:00 | 95033   |
| 102  | 匡明      | 男    | 1975-10-02 00:00:00 | 95031   |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   |
| 106  | 陆军      | 男    | 1974-06-03 00:00:00 | 95031   |
| 108  | 张全蛋    | 男    | 1975-02-10 00:00:00 | 95031   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |
+------+-----------+-------+---------------------+---------+
9 rows in set (0.00 sec)
```

- 以c_no升序.sc_degree降序查询score表中所有的数据

```
mysql> select * from score order by c_no ASC,sc_degree desc;
+------+-------+-----------+
| s_no | c_no  | sc_degree |
+------+-------+-----------+
| 103  | 3-105 |        92 |
| 105  | 3-105 |        88 |
| 109  | 3-105 |        76 |
| 103  | 3-245 |        86 |
| 105  | 3-245 |        75 |
| 109  | 3-245 |        68 |
| 103  | 6-166 |        85 |
| 109  | 6-166 |        81 |
| 105  | 6-166 |        79 |
+------+-------+-----------+
9 rows in set (0.00 sec)
```

- 查询'95031'班的学生人数

```
mysql> select count(*) from student where s_class = '95031';
+----------+
| count(*) |
+----------+
|        5 |
+----------+
1 row in set (0.00 sec)
```

- 查询score表中的最高分数的学生号和课程号.(子查询或者排序)

```
mysql> select s_no,c_no from score where sc_degree = (select max(sc_degree) from score);
+------+-------+
| s_no | c_no  |
+------+-------+
| 103  | 3-105 |
+------+-------+
1 row in set (0.00 sec)
```

- 查询每门课的平均成绩

```
mysql> select c_no , avg(sc_degree) from score group by c_no;
+-------+----------------+
| c_no  | avg(sc_degree) |
+-------+----------------+
| 3-105 |        85.3333 |
| 3-245 |        76.3333 |
| 6-166 |        81.6667 |
+-------+----------------+
3 rows in set (0.00 sec)
```

- 查询score表中至少有2名学生选修的,并且以3开头的课程的平均分

```
mysql> select c_no , avg(sc_degree) from score group by c_no having count(c_no) >= 2 and c_no like '3%';
+-------+----------------+
| c_no  | avg(sc_degree) |
+-------+----------------+
| 3-105 |        85.3333 |
| 3-245 |        76.3333 |
+-------+----------------+
2 rows in set (0.01 sec)
```

- 查询分数大于70但是小于90的s_no列:

```
mysql> select s_no,sc_degree from score where sc_degree between 71 and 89;
+------+-----------+
| s_no | sc_degree |
+------+-----------+
| 103  |        86 |
| 103  |        85 |
| 105  |        88 |
| 105  |        75 |
| 105  |        79 |
| 109  |        76 |
| 109  |        81 |
+------+-----------+
7 rows in set (0.00 sec)
```

- 查询所有的学生 s_name , c_no, sc_degree列

```
mysql> select s_name,c_no,sc_degree from student,score where student.s_no = score.s_no;
+-----------+-------+-----------+
| s_name    | c_no  | sc_degree |
+-----------+-------+-----------+
| 王丽      | 3-105 |        92 |
| 王丽      | 3-245 |        86 |
| 王丽      | 6-166 |        85 |
| 王芳      | 3-105 |        88 |
| 王芳      | 3-245 |        75 |
| 王芳      | 6-166 |        79 |
| 赵铁柱    | 3-105 |        76 |
| 赵铁柱    | 3-245 |        68 |
| 赵铁柱    | 6-166 |        81 |
+-----------+-------+-----------+
9 rows in set (0.01 sec)
```

```
mysql> select s_name,c_no,sc_degree from student left join score on student.s_no = score.s_no;
+-----------+-------+-----------+
| s_name    | c_no  | sc_degree |
+-----------+-------+-----------+
| 曾华      | NULL  |      NULL |
| 匡明      | NULL  |      NULL |
| 王丽      | 3-105 |        92 |
| 王丽      | 3-245 |        86 |
| 王丽      | 6-166 |        85 |
| 李军      | NULL  |      NULL |
| 王芳      | 3-105 |        88 |
| 王芳      | 3-245 |        75 |
| 王芳      | 6-166 |        79 |
| 陆军      | NULL  |      NULL |
| 王尼玛    | NULL  |      NULL |
| 张全蛋    | NULL  |      NULL |
| 赵铁柱    | 3-105 |        76 |
| 赵铁柱    | 3-245 |        68 |
| 赵铁柱    | 6-166 |        81 |
+-----------+-------+-----------+
15 rows in set (0.00 sec)
```

- 查询所有学生的s_no, c_name, sc_degree列

```
mysql> select student.s_no , course.c_name, score.sc_degree from student,course,score where student.s_no = score.s_no and score.c_no = course.c_no;
+------+-----------------+-----------+
| s_no | c_name          | sc_degree |
+------+-----------------+-----------+
| 103  | 计算机导论      |        92 |
| 103  | 操作系统        |        86 |
| 103  | 数字电路        |        85 |
| 105  | 计算机导论      |        88 |
| 105  | 操作系统        |        75 |
| 105  | 数字电路        |        79 |
| 109  | 计算机导论      |        76 |
| 109  | 操作系统        |        68 |
| 109  | 数字电路        |        81 |
+------+-----------------+-----------+
9 rows in set (0.00 sec)
```

- 查询所有的学生 s_name , c_name, sc_degree列

```
mysql> select s_name,c_name,sc_degree from student,course,score where student.s_no = score.s_no and course.c_no = score.c_no;
+-----------+-----------------+-----------+
| s_name    | c_name          | sc_degree |
+-----------+-----------------+-----------+
| 王丽      | 计算机导论      |        92 |
| 王丽      | 操作系统        |        86 |
| 王丽      | 数字电路        |        85 |
| 王芳      | 计算机导论      |        88 |
| 王芳      | 操作系统        |        75 |
| 王芳      | 数字电路        |        79 |
| 赵铁柱    | 计算机导论      |        76 |
| 赵铁柱    | 操作系统        |        68 |
| 赵铁柱    | 数字电路        |        81 |
+-----------+-----------------+-----------+
9 rows in set (0.00 sec)
```

- 查询班级是'95031'班学生每门课的平均分

```
mysql> select sc.c_no , avg(sc.sc_degree) from student s, score sc where s.s_no = sc.s_no and s.s_class = '95031' group by sc.c_no;
+-------+-------------------+
| c_no  | avg(sc.sc_degree) |
+-------+-------------------+
| 3-105 |           82.0000 |
| 3-245 |           71.5000 |
| 6-166 |           80.0000 |
+-------+-------------------+
3 rows in set (0.01 sec)
```

- 查询选修"3-105"课程的成绩高于'109'号同学'3-105'成绩 的所有同学的记录

```
mysql> select * from student s, score sc where s.s_no = sc.s_no and sc.c_no = '3-105' and sc.sc_degree > (select sc_degree from score where s_no = '109' and c_no = '3-105');
+------+--------+-------+---------------------+---------+------+-------+-----------+
| s_no | s_name | s_sex | s_birthday          | s_class | s_no | c_no  | sc_degree |
+------+--------+-------+---------------------+---------+------+-------+-----------+
| 103  | 王丽   | 女    | 1976-01-23 00:00:00 | 95033   | 103  | 3-105 |        92 |
| 105  | 王芳   | 女    | 1975-02-10 00:00:00 | 95031   | 105  | 3-105 |        88 |
+------+--------+-------+---------------------+---------+------+-------+-----------+
2 rows in set (0.00 sec)
```

- 查询成绩高于学号为'109',课程号为'3-105'的成绩的所有记录

```
mysql> select * from student s , score sc where s.s_no = sc.s_no  and sc.sc_degree > (select sc_degree from score where c_no = '3-105' and s_no='109');
+------+-----------+-------+---------------------+---------+------+-------+-----------+
| s_no | s_name    | s_sex | s_birthday          | s_class | s_no | c_no  | sc_degree |
+------+-----------+-------+---------------------+---------+------+-------+-----------+
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   | 103  | 3-105 |        92 |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   | 103  | 3-245 |        86 |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   | 103  | 6-166 |        85 |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   | 105  | 3-105 |        88 |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   | 105  | 6-166 |        79 |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   | 109  | 6-166 |        81 |
+------+-----------+-------+---------------------+---------+------+-------+-----------+
6 rows in set (0.00 sec)
```

- 查询所有学号为108.101的同学同年出生的所有学生的s_no,s_name和s_birthday

```
mysql> select s_no , s_name, s_birthday from  student where year(s_birthday) in (select year(s_birthday) from student where s_no in('108','101'));
+------+-----------+---------------------+
| s_no | s_name    | s_birthday          |
+------+-----------+---------------------+
| 101  | 曾华      | 1977-09-01 00:00:00 |
| 102  | 匡明      | 1975-10-02 00:00:00 |
| 105  | 王芳      | 1975-02-10 00:00:00 |
| 108  | 张全蛋    | 1975-02-10 00:00:00 |
+------+-----------+---------------------+
4 rows in set (0.00 sec)
```

- 查询 张旭 教师任课的学生的成绩

```
mysql> select sc.s_no, sc.sc_degree from score sc , teacher t, course c  where c.t_no = t.t_no and sc.c_no = c.c_no and t.t_name = '张旭';
+------+-----------+
| s_no | sc_degree |
+------+-----------+
| 103  |        85 |
| 105  |        79 |
| 109  |        81 |
+------+-----------+
3 rows in set (0.01 sec)
```

- 查询选修课程的同学人数多余 5 人的教师姓名

```
mysql> select t_name from teacher where t_no = (select t_no from course where c_no in (select c_no from score group by c_no having count(s_no) > 5));
+--------+
| t_name |
+--------+
| 王萍   |
+--------+
1 row in set (0.00 sec)
```

in 符合条件的。。（符合条件的非唯一）

- 查询95033班和95031班全体学生的记录

```
mysql> select * from student where s_class in ('95031','95033') ORDER BY s_class ;
+------+-----------+-------+---------------------+---------+
| s_no | s_name    | s_sex | s_birthday          | s_class |
+------+-----------+-------+---------------------+---------+
| 102  | 匡明      | 男    | 1975-10-02 00:00:00 | 95031   |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   |
| 106  | 陆军      | 男    | 1974-06-03 00:00:00 | 95031   |
| 108  | 张全蛋    | 男    | 1975-02-10 00:00:00 | 95031   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |
| 101  | 曾华      | 男    | 1977-09-01 00:00:00 | 95033   |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   |
| 104  | 李军      | 男    | 1976-02-20 00:00:00 | 95033   |
| 107  | 王尼玛    | 男    | 1976-02-20 00:00:00 | 95033   |
+------+-----------+-------+---------------------+---------+
9 rows in set (0.00 sec)
```

- 查询95033班和95031班全体学生每门课的成绩以及负责该课程的老师,最后以class来排序

```
mysql> select s.* , sc.sc_degree, t.t_name from student s, score sc, course c, teacher t where s.s_class in ('95033','95031') and s.s_no = sc.s_no and sc.c_no = c.c_no and c.t_no = t.t_no order by s.s_class;
+------+-----------+-------+---------------------+---------+-----------+--------+
| s_no | s_name    | s_sex | s_birthday          | s_class | sc_degree | t_name |
+------+-----------+-------+---------------------+---------+-----------+--------+
| 102  | 匡明      | 男    | 1975-10-02 00:00:00 | 95031   |        91 | 王萍   |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   |        88 | 王萍   |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   |        75 | 李诚   |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   |        79 | 张旭   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |        76 | 王萍   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |        68 | 李诚   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |        81 | 张旭   |
| 101  | 曾华      | 男    | 1977-09-01 00:00:00 | 95033   |        90 | 王萍   |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   |        92 | 王萍   |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   |        86 | 李诚   |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   |        85 | 张旭   |
| 104  | 李军      | 男    | 1976-02-20 00:00:00 | 95033   |        89 | 王萍   |
+------+-----------+-------+---------------------+---------+-----------+--------+
12 rows in set (0.00 sec)
```

- 查询存在85分以上成绩的课程c_no,老师名称

```
mysql> select c.c_no,c.c_name,t.t_name from course c, teacher t,score sc where c.t_no = t.t_no and c.c_no = sc.c_no and sc.c_no in (SELECT c_no FROM score where sc_degree > 85);
+-------+-----------------+--------+
| c_no  | c_name          | t_name |
+-------+-----------------+--------+
| 3-105 | 计算机导论      | 王萍   |
| 3-105 | 计算机导论      | 王萍   |
| 3-105 | 计算机导论      | 王萍   |
| 3-105 | 计算机导论      | 王萍   |
| 3-105 | 计算机导论      | 王萍   |
| 3-105 | 计算机导论      | 王萍   |
| 3-245 | 操作系统        | 李诚   |
| 3-245 | 操作系统        | 李诚   |
| 3-245 | 操作系统        | 李诚   |
+-------+-----------------+--------+
9 rows in set (0.00 sec)
```

- 查出所有'计算机系' 教师所教课程的成绩表

```
mysql> SELECT * FROM score WHERE c_no IN (SELECT c_no FROM course WHERE t_no IN (SELECT t_no FROM teacher WHERE t_depart = '计算机系'));
+------+-------+-----------+
| s_no | c_no  | sc_degree |
+------+-------+-----------+
| 103  | 3-245 |        86 |
| 105  | 3-245 |        75 |
| 109  | 3-245 |        68 |
| 101  | 3-105 |        90 |
| 102  | 3-105 |        91 |
| 103  | 3-105 |        92 |
| 104  | 3-105 |        89 |
| 105  | 3-105 |        88 |
| 109  | 3-105 |        76 |
+------+-------+-----------+
9 rows in set (0.01 sec)
```

```
mysql> select t.t_name,t.t_depart,c.c_name,s.s_name,s.s_class,sc.sc_degree from student s,score sc, teacher t,course c where c.t_no in (select t_no from teacher where t_depart = ' 计算机系') and c.t_no = t.t_no and c.c_no = sc.c_no and sc.s_no = s.s_no;
+--------+--------------+-----------------+-----------+---------+-----------+
| t_name | t_depart     | c_name          | s_name    | s_class | sc_degree |
+--------+--------------+-----------------+-----------+---------+-----------+
| 李诚   | 计算机系     | 操作系统        | 王丽      | 95033   |        86 |
| 李诚   | 计算机系     | 操作系统        | 王芳      | 95031   |        75 |
| 李诚   | 计算机系     | 操作系统        | 赵铁柱    | 95031   |        68 |
| 王萍   | 计算机系     | 计算机导论      | 曾华      | 95033   |        90 |
| 王萍   | 计算机系     | 计算机导论      | 匡明      | 95031   |        91 |
| 王萍   | 计算机系     | 计算机导论      | 王丽      | 95033   |        92 |
| 王萍   | 计算机系     | 计算机导论      | 李军      | 95033   |        89 |
| 王萍   | 计算机系     | 计算机导论      | 王芳      | 95031   |        88 |
| 王萍   | 计算机系     | 计算机导论      | 赵铁柱    | 95031   |        76 |
+--------+--------------+-----------------+-----------+---------+-----------+
9 rows in set (0.01 sec)
```

- 查询'计算机系'与'电子工程系' 不同职称的教师的name和rof

```
mysql> select * from teacher where t_depart = '计算机系' and t_rof not in (select t_rof from teacher where t_depart = '电子工程系') union select * from teacher where t_depart = '电子工程系' and t_rof not in (select t_rof from teacher where t_depart = '计算机系');
+------+--------+-------+---------------------+-----------+-----------------+
| t_no | t_name | t_sex | t_birthday          | t_rof     | t_depart        |
+------+--------+-------+---------------------+-----------+-----------------+
| 804  | 李诚   | 男    | 1958-12-02 00:00:00 | 副教授    | 计算机系        |
| 856  | 张旭   | 男    | 1969-03-12 00:00:00 | 讲师      | 电子工程系      |
+------+--------+-------+---------------------+-----------+-----------------+
2 rows in set (0.00 sec)
```

- 查询选修编号为"3-105"课程且成绩至少高于选修编号为'3-245'同学的c_no,s_no和sc_degree,并且按照sc_degree从高到地次序排序

```
mysql> select s_no , sc_degree from score where c_no = '3-105' and sc_degree > any(select sc_degree from score where c_no = '3-245') order by sc_degree desc;
+------+-----------+
| s_no | sc_degree |
+------+-----------+
| 103  |        92 |
| 102  |        91 |
| 101  |        90 |
| 104  |        89 |
| 105  |        88 |
| 109  |        76 |
+------+-----------+
6 rows in set (0.00 sec)
```

- 查询选修编号为"3-105"且成绩高于选修编号为"3-245"课程的同学c_no.s_no和sc_degree

```
mysql> select s_no , sc_degree from score where c_no = '3-105' and sc_degree > all(select sc_degree from score where c_no = '3-245');
+------+-----------+
| s_no | sc_degree |
+------+-----------+
| 101  |        90 |
| 102  |        91 |
| 103  |        92 |
| 104  |        89 |
| 105  |        88 |
+------+-----------+
5 rows in set (0.00 sec)
```

总结: ANY 和 ALL

ANY:表示任何一个就行了,如;数组A中的值比数组B中任何一个都要大,那么只要A和B中最小的比较就行了.

ALL:表示所有都要比较,如:数组A中的值比数组B中所有的数都要大,那么A要和B中最大的值比较才行.

- 查询所有教师和同学的 name ,sex, birthday

```
mysql> SELECT s_name AS name, s_sex AS sex, s_birthday AS birthday  FROM student
    -> UNION
    -> SELECT t_name AS name, t_sex AS sex, t_birthday AS birthday FROM teacher;
+-----------+-----+---------------------+
| name      | sex | birthday            |
+-----------+-----+---------------------+
| 曾华      | 男  | 1977-09-01 00:00:00 |
| 匡明      | 男  | 1975-10-02 00:00:00 |
| 王丽      | 女  | 1976-01-23 00:00:00 |
| 李军      | 男  | 1976-02-20 00:00:00 |
| 王芳      | 女  | 1975-02-10 00:00:00 |
| 陆军      | 男  | 1974-06-03 00:00:00 |
| 王尼玛    | 男  | 1976-02-20 00:00:00 |
| 张全蛋    | 男  | 1975-02-10 00:00:00 |
| 赵铁柱    | 男  | 1974-06-03 00:00:00 |
| 李诚      | 男  | 1958-12-02 00:00:00 |
| 王萍      | 女  | 1972-05-05 00:00:00 |
| 刘冰      | 女  | 1977-08-14 00:00:00 |
| 张旭      | 男  | 1969-03-12 00:00:00 |
+-----------+-----+---------------------+
13 rows in set (0.00 sec)
```

- 查询所有'女'教师和'女'学生的name,sex,birthday

```
mysql> SELECT s_name AS name, s_sex AS sex, s_birthday AS birthday  FROM student where s_sex = '女' UNION SELECT t_name AS name, t_sex AS sex, t_birthday AS birthday FROM teacher where t_sex = '女';
+--------+-----+---------------------+
| name   | sex | birthday            |
+--------+-----+---------------------+
| 王丽   | 女  | 1976-01-23 00:00:00 |
| 王芳   | 女  | 1975-02-10 00:00:00 |
| 王萍   | 女  | 1972-05-05 00:00:00 |
| 刘冰   | 女  | 1977-08-14 00:00:00 |
+--------+-----+---------------------+
4 rows in set (0.00 sec)
```

- 查询成绩比该课程平均成绩低的同学的成绩表

```
select avg(sc_degree) from score sc2 where sc1.c_no = sc2.c_no // 计算该课程成绩
```

```
mysql> select * from score sc1 where sc_degree < (select avg(sc_degree) from score sc2 where sc1.c_no = sc2.c_no);
+------+-------+-----------+
| s_no | c_no  | sc_degree |
+------+-------+-----------+
| 105  | 3-245 |        75 |
| 105  | 6-166 |        79 |
| 109  | 3-105 |        76 |
| 109  | 3-245 |        68 |
| 109  | 6-166 |        81 |
+------+-------+-----------+
5 rows in set (0.01 sec)
```

- 查询所有任课教师的t_name 和 t_depart(要在分数表中可以查得到)

```
mysql> select * from teacher where t_no in (select t_no from course where c_no in (select c_no from score));
+------+--------+-------+---------------------+-----------+-----------------+
| t_no | t_name | t_sex | t_birthday          | t_rof     | t_depart        |
+------+--------+-------+---------------------+-----------+-----------------+
| 804  | 李诚   | 男    | 1958-12-02 00:00:00 | 副教授    | 计算机系        |
| 825  | 王萍   | 女    | 1972-05-05 00:00:00 | 助教      | 计算机系        |
| 856  | 张旭   | 男    | 1969-03-12 00:00:00 | 讲师      | 电子工程系      |
+------+--------+-------+---------------------+-----------+-----------------+
3 rows in set (0.01 sec)
```

- 查出至少有2名男生的班号

```
mysql> select s_class from student where s_sex = '男' group by s_class having count(s_no)>1;
+---------+
| s_class |
+---------+
| 95033   |
| 95031   |
+---------+
2 rows in set (0.01 sec)
```

- 查询student 表中 不姓"王"的同学的记录

```
mysql> select * from student where s_name not like '王%';
+------+-----------+-------+---------------------+---------+
| s_no | s_name    | s_sex | s_birthday          | s_class |
+------+-----------+-------+---------------------+---------+
| 101  | 曾华      | 男    | 1977-09-01 00:00:00 | 95033   |
| 102  | 匡明      | 男    | 1975-10-02 00:00:00 | 95031   |
| 104  | 李军      | 男    | 1976-02-20 00:00:00 | 95033   |
| 106  | 陆军      | 男    | 1974-06-03 00:00:00 | 95031   |
| 108  | 张全蛋    | 男    | 1975-02-10 00:00:00 | 95031   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |
+------+-----------+-------+---------------------+---------+
6 rows in set (0.01 sec)
```

- 查询student 中每个学生的姓名和年龄(当前时间 - 出生年份)

```
| s_name    | age  |
+-----------+------+
| 曾华      |   43 |
| 匡明      |   45 |
| 王丽      |   44 |
| 李军      |   44 |
| 王芳      |   45 |
| 陆军      |   46 |
| 王尼玛    |   44 |
| 张全蛋    |   45 |
| 赵铁柱    |   46 |
+-----------+------+
9 rows in set (0.00 sec)
```

- 查询student 中每个学生的姓名和年龄(当前时间 - 出生年份)

```
mysql> select max(s_birthday),min(s_birthday) from student;
+---------------------+---------------------+
| max(s_birthday)     | min(s_birthday)     |
+---------------------+---------------------+
| 1977-09-01 00:00:00 | 1974-06-03 00:00:00 |
+---------------------+---------------------+
1 row in set (0.00 sec)
```

- 以班级号和年龄从大到小的顺序查询student表中的全部记录

```
mysql> select * from student order by s_class desc , s_birthday desc;
+------+-----------+-------+---------------------+---------+
| s_no | s_name    | s_sex | s_birthday          | s_class |
+------+-----------+-------+---------------------+---------+
| 101  | 曾华      | 男    | 1977-09-01 00:00:00 | 95033   |
| 104  | 李军      | 男    | 1976-02-20 00:00:00 | 95033   |
| 107  | 王尼玛    | 男    | 1976-02-20 00:00:00 | 95033   |
| 103  | 王丽      | 女    | 1976-01-23 00:00:00 | 95033   |
| 102  | 匡明      | 男    | 1975-10-02 00:00:00 | 95031   |
| 105  | 王芳      | 女    | 1975-02-10 00:00:00 | 95031   |
| 108  | 张全蛋    | 男    | 1975-02-10 00:00:00 | 95031   |
| 106  | 陆军      | 男    | 1974-06-03 00:00:00 | 95031   |
| 109  | 赵铁柱    | 男    | 1974-06-03 00:00:00 | 95031   |
+------+-----------+-------+---------------------+---------+
9 rows in set (0.01 sec)
```

- 查询"男"教师 及其所上的课

```
mysql> select t.t_name , c.c_name from teacher t,course c  where t.t_no in (select t_no from teacher where t_sex = '男') and t.t_no = c.t_no;
+--------+--------------+
| t_name | c_name       |
+--------+--------------+
| 李诚   | 操作系统       |
| 张旭   | 数字电路       |
+--------+--------------+
2 rows in set (0.00 sec)
```

- 查询最高分同学的s_no c_no 和 sc_degree

```
mysql> select sc.* from score sc where sc.sc_degree = (select max(sc_degree) from score limit 1);
+------+-------+-----------+
| s_no | c_no  | sc_degree |
+------+-------+-----------+
| 103  | 3-105 |        92 |
+------+-------+-----------+
1 row in set (0.01 sec)
```

- 查询和"李军"同性别的所有同学的s_name

```
mysql> select s_name from student where s_sex = (select s_sex from student where s_name = '李军');
+-----------+
| s_name    |
+-----------+
| 曾华      |
| 匡明      |
| 李军      |
| 陆军      |
| 王尼玛    |
| 张全蛋    |
| 赵铁柱    |
+-----------+
7 rows in set (0.00 sec)
```

- 查询所有选修'计算机导论'课程的'男'同学的成绩表

```
mysql> select * from score where c_no = (select c_no from course where c_name = '计算机导论') and s_no in (select s_no from  student where s_sex = '男');
+------+-------+-----------+
| s_no | c_no  | sc_degree |
+------+-------+-----------+
| 101  | 3-105 |        90 |
| 102  | 3-105 |        91 |
| 104  | 3-105 |        89 |
| 109  | 3-105 |        76 |
+------+-------+-----------+
4 rows in set (0.01 sec)
```

- 查询所有同学的s_no , c_no 和grade列

```
mysql> select s_no , c_no , grade from score , grade where sc_degree between low and upp; 
+------+-------+-------+
| s_no | c_no  | grade |
+------+-------+-------+
| 101  | 3-105 | A     |
| 102  | 3-105 | A     |
| 103  | 3-105 | A     |
| 103  | 3-245 | B     |
| 103  | 6-166 | B     |
| 104  | 3-105 | B     |
| 105  | 3-105 | B     |
| 105  | 3-245 | c     |
| 105  | 6-166 | c     |
| 109  | 3-105 | c     |
| 109  | 3-245 | D     |
| 109  | 6-166 | B     |
+------+-------+-------+
12 rows in set (0.01 sec)
```

## 连接查询

```sql
CREATE TABLE person (
    id INT,
    name VARCHAR(20),
    cardId INT
);

CREATE TABLE card (
    id INT,
    name VARCHAR(20)
);

INSERT INTO card VALUES (1, '饭卡'), (2, '建行卡'), (3, '农行卡'), (4, '工商卡'), (5, '邮政卡');
SELECT * FROM card;
+------+-----------+
| id   | name      |
+------+-----------+
|    1 | 饭卡      |
|    2 | 建行卡    |
|    3 | 农行卡    |
|    4 | 工商卡    |
|    5 | 邮政卡    |
+------+-----------+

INSERT INTO person VALUES (1, '张三', 1), (2, '李四', 3), (3, '王五', 6);
SELECT * FROM person;
+------+--------+--------+
| id   | name   | cardId |
+------+--------+--------+
|    1 | 张三   |      1 |
|    2 | 李四   |      3 |
|    3 | 王五   |      6 |
+------+--------+--------+
```

### 内连接

内连接查询，就是通过某个字段相等，查询这两张表中有关系的数据，可以使用 `INNER JOIN` ( 内连接 ) 将它们连接在一起。

将 INNER 关键字省略掉，结果也是一样的。

```
mysql> SELECT * FROM person INNER JOIN card on person.cardId = card.id;
+------+--------+--------+------+-----------+
| id   | name   | cardId | id   | name      |
+------+--------+--------+------+-----------+
|    1 | 张三   |      1 |    1 | 饭卡      |
|    2 | 李四   |      3 |    3 | 农行卡    |
+------+--------+--------+------+-----------+
2 rows in set (0.00 sec)
```

### 左外连接

完整显示左边的表 ( `person` ) ，右边的表如果符合条件就显示，不符合则补 `NULL` 。

```
mysql> select * from person left join card on person.cardId = card.id;
+------+--------+--------+------+-----------+
| id   | name   | cardId | id   | name      |
+------+--------+--------+------+-----------+
|    1 | 张三   |      1 |    1 | 饭卡      |
|    2 | 李四   |      3 |    3 | 农行卡    |
|    3 | 王五   |      6 | NULL | NULL      |
+------+--------+--------+------+-----------+
3 rows in set (0.01 sec)
```

### 右外连接

完整显示右边的表 ( `card` ) ，左边的表如果符合条件就显示，不符合则补 `NULL` 。

```
mysql> SELECT * FROM person RIGHT JOIN card on person.cardId = card.id;
+------+--------+--------+------+-----------+
| id   | name   | cardId | id   | name      |
+------+--------+--------+------+-----------+
|    1 | 张三   |      1 |    1 | 饭卡      |
|    2 | 李四   |      3 |    3 | 农行卡    |
| NULL | NULL   |   NULL |    2 | 建行卡    |
| NULL | NULL   |   NULL |    4 | 工商卡    |
| NULL | NULL   |   NULL |    5 | 邮政卡    |
+------+--------+--------+------+-----------+
5 rows in set (0.00 sec)
```

### 全链接

完整显示两张表的全部数据。

```sql
-- MySQL 不支持这种语法的全外连接
-- SELECT * FROM person FULL JOIN card on person.cardId = card.id;
-- 出现错误：
-- ERROR 1054 (42S22): Unknown column 'person.cardId' in 'on clause'

-- MySQL全连接语法，使用 UNION 将两张表合并在一起。
SELECT * FROM person LEFT JOIN card on person.cardId = card.id
UNION
SELECT * FROM person RIGHT JOIN card on person.cardId = card.id;

mysql> SELECT * FROM person LEFT JOIN card on person.cardId = card.id
    -> UNION
    -> SELECT * FROM person RIGHT JOIN card on person.cardId = card.id;
+------+--------+--------+------+-----------+
| id   | name   | cardId | id   | name      |
+------+--------+--------+------+-----------+
|    1 | 张三   |      1 |    1 | 饭卡      |
|    2 | 李四   |      3 |    3 | 农行卡    |
|    3 | 王五   |      6 | NULL | NULL      |
| NULL | NULL   |   NULL |    2 | 建行卡    |
| NULL | NULL   |   NULL |    4 | 工商卡    |
| NULL | NULL   |   NULL |    5 | 邮政卡    |
+------+--------+--------+------+-----------+
6 rows in set (0.02 sec)
```

## 事务

在 MySQL 中，事务其实是一个最小的不可分割的工作单元。事务能够**保证一个业务的完整性**。

比如我们的银行转账：

```sql
-- a -> -100
UPDATE user set money = money - 100 WHERE name = 'a';

-- b -> +100
UPDATE user set money = money + 100 WHERE name = 'b';
```

在执行多条有关联 SQL 语句时，**事务**可能会要求这些 SQL 语句要么同时执行成功，要么就都执行失败。

## 如何控制事务 - COMMIT / ROLLBACK

在 MySQL 中，事务的**自动提交**状态默认是开启的。

```
-- 查询事务的自动提交状态
SELECT @@AUTOCOMMIT;
+--------------+
| @@AUTOCOMMIT |
+--------------+
|            1 |
+--------------+
```

**自动提交的作用**：当我们执行一条 SQL 语句的时候，其产生的效果就会立即体现出来，且不能**回滚**。

```sql
CREATE DATABASE bank;

USE bank;

CREATE TABLE user (
    id INT PRIMARY KEY,
    name VARCHAR(20),
    money INT
);

INSERT INTO user VALUES (1, 'a', 1000);

SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
+----+------+-------+
```

可以看到，在执行插入语句后数据立刻生效，原因是 MySQL 中的事务自动将它**提交**到了数据库中。那么所谓**回滚**的意思就是，撤销执行过的所有 SQL 语句，使其回滚到**最后一次提交**数据时的状态。

在 MySQL 中使用 `ROLLBACK` 执行回滚：

```sql
-- 回滚到最后一次提交
ROLLBACK;

SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
+----+------+-------+
```

由于所有执行过的 SQL 语句都已经被提交过了，所以数据并没有发生回滚。那如何让数据可以发生回滚？

```sql
-- 关闭自动提交
SET AUTOCOMMIT = 0;

-- 查询自动提交状态
SELECT @@AUTOCOMMIT;
+--------------+
| @@AUTOCOMMIT |
+--------------+
|            0 |
+--------------+
```

将自动提交关闭后，测试数据回滚：

```sql
INSERT INTO user VALUES (2, 'b', 1000);

-- 关闭 AUTOCOMMIT 后，数据的变化是在一张虚拟的临时数据表中展示，
-- 发生变化的数据并没有真正插入到数据表中。
SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
|  2 | b    |  1000 |
+----+------+-------+

-- 数据表中的真实数据其实还是：
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
+----+------+-------+

-- 由于数据还没有真正提交，可以使用回滚
ROLLBACK;

-- 再次查询
SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
+----+------+-------+
```

那如何将虚拟的数据真正提交到数据库中？使用 `COMMIT` : 

```sql
INSERT INTO user VALUES (2, 'b', 1000);
-- 手动提交数据（持久性），
-- 将数据真正提交到数据库中，执行后不能再回滚提交过的数据。
COMMIT;

-- 提交后测试回滚
ROLLBACK;

-- 再次查询（回滚无效了）
SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
|  2 | b    |  1000 |
+----+------+-------+
```

**总结**

1. **自动提交**

   - 查看自动提交状态：`SELECT @@AUTOCOMMIT` ；

   - 设置自动提交状态：`SET AUTOCOMMIT = 0` 。

2. **手动提交**

   `@@AUTOCOMMIT = 0` 时，使用 `COMMIT` 命令提交事务。

3. **事务回滚**

   `@@AUTOCOMMIT = 0` 时，使用 `ROLLBACK` 命令回滚事务。

### 手动开启事务 - BEGIN / START TRANSACTION

事务的默认提交被开启 ( `@@AUTOCOMMIT = 1` ) 后，此时就不能使用事务回滚了。但是我们还可以手动开启一个事务处理事件，使其可以发生回滚：

```sql
-- 使用 BEGIN 或者 START TRANSACTION 手动开启一个事务
-- START TRANSACTION;
BEGIN;
UPDATE user set money = money - 100 WHERE name = 'a';
UPDATE user set money = money + 100 WHERE name = 'b';

-- 由于手动开启的事务没有开启自动提交，
-- 此时发生变化的数据仍然是被保存在一张临时表中。
SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |   900 |
|  2 | b    |  1100 |
+----+------+-------+

-- 测试回滚
ROLLBACK;

SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |  1000 |
|  2 | b    |  1000 |
+----+------+-------+
```

仍然使用 `COMMIT` 提交数据，提交后无法再发生本次事务的回滚。

```sql
BEGIN;
UPDATE user set money = money - 100 WHERE name = 'a';
UPDATE user set money = money + 100 WHERE name = 'b';

SELECT * FROM user;
+----+------+-------+
| id | name | money |
+----+------+-------+
|  1 | a    |   900 |
|  2 | b    |  1100 |
+----+------+-------+

-- 提交数据
COMMIT;

-- 测试回滚（无效，因为表的数据已经被提交）
ROLLBACK;
```

### 事务的 ACID 特征与使用

**事务的四大特征：**

- **A 原子性**：事务是最小的单位，不可以再分割；
- **C 一致性**：要求同一事务中的 SQL 语句，必须保证同时成功或者失败；
- **I 隔离性**：事务1 和 事务2 之间是具有隔离性的；
- **D 持久性**：事务一旦结束 ( `COMMIT` ) ，就不可以再返回了 ( `ROLLBACK` ) 。

### 事务的隔离性

**事务的隔离性可分为四种 ( 性能从低到高 )** ：

1. **READ UNCOMMITTED ( 读取未提交 )**

   如果有多个事务，那么任意事务都可以看见其他事务的**未提交数据**。

2. **READ COMMITTED ( 读取已提交 )**

   只能读取到其他事务**已经提交的数据**。

3. **REPEATABLE READ ( 可被重复读 )**

   如果有多个连接都开启了事务，那么事务之间不能共享数据记录，否则只能共享已提交的记录。

4. **SERIALIZABLE ( 串行化 )**

   所有的事务都会按照**固定顺序执行**，执行完一个事务后再继续执行下一个事务的**写入操作**。

查看当前数据库的默认隔离级别：

```sql
-- MySQL 8.x, GLOBAL 表示系统级别，不加表示会话级别。
SELECT @@GLOBAL.TRANSACTION_ISOLATION;
SELECT @@TRANSACTION_ISOLATION;
+--------------------------------+
| @@GLOBAL.TRANSACTION_ISOLATION |
+--------------------------------+
| REPEATABLE-READ                | -- MySQL的默认隔离级别，可以重复读。
+--------------------------------+

-- MySQL 5.x
SELECT @@GLOBAL.TX_ISOLATION;
SELECT @@TX_ISOLATION;
```

修改隔离级别：

```sql
-- 设置系统隔离级别，LEVEL 后面表示要设置的隔离级别 (READ UNCOMMITTED)。
SET GLOBAL TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

-- 查询系统隔离级别，发现已经被修改。
SELECT @@GLOBAL.TRANSACTION_ISOLATION;
+--------------------------------+
| @@GLOBAL.TRANSACTION_ISOLATION |
+--------------------------------+
| READ-UNCOMMITTED               |
+--------------------------------+
```



#### 脏读

测试 **READ UNCOMMITTED ( 读取未提交 )** 的隔离性：

```sql
INSERT INTO user VALUES (3, '小明', 1000);
INSERT INTO user VALUES (4, '淘宝店', 1000);

SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |  1000 |
|  4 | 淘宝店    |  1000 |
+----+-----------+-------+

-- 开启一个事务操作数据
-- 假设小明在淘宝店买了一双800块钱的鞋子：
START TRANSACTION;
UPDATE user SET money = money - 800 WHERE name = '小明';
UPDATE user SET money = money + 800 WHERE name = '淘宝店';

-- 然后淘宝店在另一方查询结果，发现钱已到账。
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |   200 |
|  4 | 淘宝店    |  1800 |
+----+-----------+-------+
```

由于小明的转账是在新开启的事务上进行操作的，而该操作的结果是可以被其他事务（另一方的淘宝店）看见的，因此淘宝店的查询结果是正确的，淘宝店确认到账。但就在这时，如果小明在它所处的事务上又执行了 `ROLLBACK` 命令，会发生什么？

```sql
-- 小明所处的事务
ROLLBACK;

-- 此时无论对方是谁，如果再去查询结果就会发现：
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |  1000 |
|  4 | 淘宝店    |  1000 |
+----+-----------+-------+
```

这就是所谓的**脏读**，一个事务读取到另外一个事务还未提交的数据。这在实际开发中是不允许出现的。

#### 读取已提交

把隔离级别设置为 **READ COMMITTED** ：

```sql
SET GLOBAL TRANSACTION ISOLATION LEVEL READ COMMITTED;
SELECT @@GLOBAL.TRANSACTION_ISOLATION;
+--------------------------------+
| @@GLOBAL.TRANSACTION_ISOLATION |
+--------------------------------+
| READ-COMMITTED                 |
+--------------------------------+
```

这样，再有新的事务连接进来时，它们就只能查询到已经提交过的事务数据了。但是对于当前事务来说，它们看到的还是未提交的数据，例如：

```sql
-- 正在操作数据事务（当前事务）
START TRANSACTION;
UPDATE user SET money = money - 800 WHERE name = '小明';
UPDATE user SET money = money + 800 WHERE name = '淘宝店';

-- 虽然隔离级别被设置为了 READ COMMITTED，但在当前事务中，
-- 它看到的仍然是数据表中临时改变数据，而不是真正提交过的数据。
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |   200 |
|  4 | 淘宝店    |  1800 |
+----+-----------+-------+


-- 假设此时在远程开启了一个新事务，连接到数据库。
$ mysql -u root -p12345612

-- 此时远程连接查询到的数据只能是已经提交过的
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |  1000 |
|  4 | 淘宝店    |  1000 |
+----+-----------+-------+
```

但是这样还有问题，那就是假设一个事务在操作数据时，其他事务干扰了这个事务的数据。例如：

```sql
-- 小张在查询数据的时候发现：
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |   200 |
|  4 | 淘宝店    |  1800 |
+----+-----------+-------+

-- 在小张求表的 money 平均值之前，小王做了一个操作：
START TRANSACTION;
INSERT INTO user VALUES (5, 'c', 100);
COMMIT;

-- 此时表的真实数据是：
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |  1000 |
|  4 | 淘宝店    |  1000 |
|  5 | c         |   100 |
+----+-----------+-------+

-- 这时小张再求平均值的时候，就会出现计算不相符合的情况：
SELECT AVG(money) FROM user;
+------------+
| AVG(money) |
+------------+
|  820.0000  |
+------------+
```

虽然 **READ COMMITTED** 让我们只能读取到其他事务已经提交的数据，但还是会出现问题，就是**在读取同一个表的数据时，可能会发生前后不一致的情况。**这被称为**不可重复读现象 ( READ COMMITTED )** 。

#### 幻读

将隔离级别设置为 **REPEATABLE READ ( 可被重复读取 )** :

```mysql
SET GLOBAL TRANSACTION ISOLATION LEVEL REPEATABLE READ;
SELECT @@GLOBAL.TRANSACTION_ISOLATION;
+--------------------------------+
| @@GLOBAL.TRANSACTION_ISOLATION |
+--------------------------------+
| REPEATABLE-READ                |
+--------------------------------+
```

测试 **REPEATABLE READ** ，假设在两个不同的连接上分别执行 `START TRANSACTION` :

```mysql
-- 小张 - 成都
START TRANSACTION;
INSERT INTO user VALUES (6, 'd', 1000);

-- 小王 - 北京
START TRANSACTION;

-- 小张 - 成都
COMMIT;
```

当前事务开启后，没提交之前，查询不到，提交后可以被查询到。但是，在提交之前其他事务(小王这边)被开启了，那么在这条事务线上，就不会查询到当前有操作事务的连接。相当于开辟出一条单独的线程。

无论小张是否执行过 `COMMIT` ，在小王这边，都不会查询到小张的事务记录，而是只会查询到自己所处事务的记录：

```mysql
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |  1000 |
|  4 | 淘宝店    |  1000 |
|  5 | c         |   100 |
+----+-----------+-------+
```

这是**因为小王在此之前开启了一个新的事务 ( `START TRANSACTION` ) **，那么**在他的这条新事务的线上，跟其他事务是没有联系的**，也就是说，此时如果其他事务正在操作数据，它是不知道的。

然而事实是，在真实的数据表中，小张已经插入了一条数据。但是小王此时并不知道，也插入了同一条数据，会发生什么呢？

```mysql
INSERT INTO user VALUES (6, 'd', 1000);
-- ERROR 1062 (23000): Duplicate entry '6' for key 'PRIMARY'
```

报错了，操作被告知已存在主键为 `6` 的字段。这种现象也被称为**幻读，一个事务提交的数据，不能被其他事务读取到**。

### 串行化

顾名思义，就是所有事务的**写入操作**全都是串行化的。什么意思？把隔离级别修改成 **SERIALIZABLE** :

```mysql
SET GLOBAL TRANSACTION ISOLATION LEVEL SERIALIZABLE;
SELECT @@GLOBAL.TRANSACTION_ISOLATION;
+--------------------------------+
| @@GLOBAL.TRANSACTION_ISOLATION |
+--------------------------------+
| SERIALIZABLE                   |
+--------------------------------+
```

还是拿小张和小王来举例：

```mysql
-- 小张 - 成都
START TRANSACTION;

-- 小王 - 北京
START TRANSACTION;

-- 开启事务之前先查询表，准备操作数据。
SELECT * FROM user;
+----+-----------+-------+
| id | name      | money |
+----+-----------+-------+
|  1 | a         |   900 |
|  2 | b         |  1100 |
|  3 | 小明      |  1000 |
|  4 | 淘宝店    |  1000 |
|  5 | c         |   100 |
|  6 | d         |  1000 |
+----+-----------+-------+

-- 发现没有 7 号王小花，于是插入一条数据：
INSERT INTO user VALUES (7, '王小花', 1000);
```

此时会发生什么呢？由于现在的隔离级别是 **SERIALIZABLE ( 串行化 )** ，串行化的意思就是：假设把所有的事务都放在一个串行的队列中，那么所有的事务都会按照**固定顺序执行**，执行完一个事务后再继续执行下一个事务的**写入操作** ( **这意味着队列中同时只能执行一个事务的写入操作** ) 。

根据这个解释，小王在插入数据时，会出现等待状态，直到小张执行 `COMMIT` 结束它所处的事务，或者出现等待超时。