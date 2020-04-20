# 显式锁和AQS  

## 显式锁

Lock接口和核心方法

 

Lock接口和synchronized的比较

synchronized代码简洁，Lock：获取锁可以被中断，超时获取锁，尝试获取锁，读多写少用读写锁

**可重入锁ReentrantLock****、所谓锁的公平和非公平**

如果在时间上，先对锁进行获取的请求，一定先被满足，这个锁就是公平的，不满足，就是非公平的

非公平的效率一般来讲更高

**ReadWriteLock****接口和读写锁ReentrantReadWriteLock**

ReentrantLock和Syn关键字，都是排他锁，

读写锁：同一时刻允许多个读线程同时访问，但是写线程访问的时候，所有的读和写都被阻塞，最适宜与读多写少的情况

**Condition****接口**

 

**用Lock****和Condition****实现等待通知**

 

## 了解LockSupport工具

 

**park****开头的方法**

 

**unpark(Thread thread)****方法**

 

## AbstractQueuedSynchronizer深入分析

#### 什么是AQS？学习它的必要性

**AQS****使用方式和其中的设计模式**

 

**了解其中的方法**

 

#### AQS中的数据结构-节点和同步队列

 

 

#### 节点在同步队列中的增加和移出

 

#### 独占式同步状态获取与释放

 

#### 其他同步状态获取与释放 

 

#### Condition分析