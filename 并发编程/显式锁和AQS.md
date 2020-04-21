# 显式锁和AQS  

[toc]

## 显式锁

### Lock接口和核心方法

 lock()  

unlock()

tryLock()

### Lock接口和synchronized的比较

synchronized代码简洁，Lock：获取锁可以被中断，超时获取锁，尝试获取锁，读多写少用读写锁

```JAVA
public class BusiApp {
    static final int readWriteRatio = 10;//读写线程的比例
    static final int minthreadCount = 3;//最少线程数
    //static CountDownLatch latch= new CountDownLatch(1);

    //读操作
    private static class GetThread implements Runnable {

        private GoodsService goodsService;

        public GetThread(GoodsService goodsService) {
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
//            try {
//                latch.await();//让读写线程同时运行
//            } catch (InterruptedException e) {
//            }
            long start = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {//操作100次
                goodsService.getNum();
            }
            System.out.println(Thread.currentThread().getName() + "读取商品数据耗时："
                    + (System.currentTimeMillis() - start) + "ms");

        }
    }

    //写操做
    private static class SetThread implements Runnable {

        private GoodsService goodsService;

        public SetThread(GoodsService goodsService) {
            this.goodsService = goodsService;
        }

        @Override
        public void run() {
//            try {
//                latch.await();//让读写线程同时运行
//            } catch (InterruptedException e) {
//            }
            long start = System.currentTimeMillis();
            Random r = new Random();
            for (int i = 0; i < 10; i++) {//操作10次
                SleepTools.ms(50);
                goodsService.setNum(r.nextInt(10));
            }
            System.out.println(Thread.currentThread().getName()
                    + "写商品数据耗时：" + (System.currentTimeMillis() - start) + "ms---------");

        }
    }

    public static void main(String[] args) throws InterruptedException {
        GoodsInfo goodsInfo = new GoodsInfo("Cup", 100000, 10000);
        GoodsService goodsService = new UseRwLock(goodsInfo);//new UseSyn(goodsInfo);
        for (int i = 0; i < minthreadCount; i++) {
            Thread setT = new Thread(new SetThread(goodsService));
            for (int j = 0; j < readWriteRatio; j++) {
                Thread getT = new Thread(new GetThread(goodsService));
                getT.start();
            }
            SleepTools.ms(100);
            setT.start();
        }
        //latch.countDown();
    }
}
```

```JAVA
public class UseRwLock implements GoodsService{

    private GoodsInfo goodsInfo;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock getLock = lock.readLock();//读锁
    private final Lock setLock = lock.writeLock();//写锁

    public UseRwLock(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public GoodsInfo getNum() {
        getLock.lock();
        try {
            SleepTools.ms(5);
            return this.goodsInfo;
        }finally {
            getLock.unlock();
        }

    }

    @Override
    public void setNum(int number) {
        setLock.lock();
        try {
            SleepTools.ms(5);
            goodsInfo.changeNumber(number);
        }finally {
            setLock.unlock();
        }
    }
}
```

```java
public class UseSyn implements GoodsService {

    private GoodsInfo goodsInfo;

    public UseSyn(GoodsInfo goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    @Override
    public synchronized GoodsInfo getNum() {
        SleepTools.ms(5);
        return this.goodsInfo;
    }

    @Override
    public synchronized void setNum(int number) {
        SleepTools.ms(5);
        goodsInfo.changeNumber(number);

    }
}
```

### 可重入锁ReentrantLock、所谓锁的公平和非公平

如果在时间上，**先对锁进行获取的请求，一定先被满足，这个锁就是公平的**，不满足，就是非公平的

非公平的效率一般来讲更高：因为利用了线程挂起到运行的这段时间

### ReadWriteLock接口和读写锁ReentrantReadWriteLock

- ReentrantLock和Syn关键字，都是排他锁，同一时刻，仅运行一个线程获取锁

- 读写锁：同一时刻允许多个读线程同时访问，但是写线程访问的时候，所有的读和写都被阻塞，**最适宜与读多写少的情况**

ReentrantReadWriteLock实现了ReadWriteLock，同时ReentrantReadWriteLock内部的读锁和写锁，都分别实现了Lock接口。

**Condition****接口**

 

**用Lock****和Condition****实现等待通知**

 

## 了解LockSupport工具

 

**park****开头的方法**

 

**unpark(Thread thread)****方法**



## 模版方法

父类定义框架

由子类实现

```JAVA
public abstract class SendCustom {

    public abstract void to();

    public abstract void from();

    public abstract void content();

    public void date() {
        System.out.println(new Date());
    }

    public abstract void send();

    // 框架方法
    public void sendMessage() {
        to();
        from();
        content();
        send();
    }
}
```

```JAVA
public class SendSms extends SendCustom {
    @Override
    public void to() {
        System.out.println("Mark");

    }

    @Override
    public void from() {
        System.out.println("Bill");

    }

    @Override
    public void content() {
        System.out.println("Hello world");

    }

    @Override
    public void send() {
        System.out.println("Send sms");

    }

    public static void main(String[] args) {
        SendCustom sendC = new SendSms();
        sendC.sendMessage();
    }

}
```

```JAVA
public class SendMail extends SendCustom{
    @Override
    public void to() {
        System.out.println("Mark");

    }

    @Override
    public void from() {
        System.out.println("Bill");

    }

    @Override
    public void content() {
        System.out.println("Hello world");

    }

    @Override
    public void send() {
        System.out.println("Send mail");

    }

    public static void main(String[] args) {
        // 向上造型
        SendCustom sendC = new SendMail();
        sendC.sendMessage();
    }
}
```

 

## AbstractQueuedSynchronizer深入分析

#### 什么是AQS？学习它的必要性

**AQS****使用方式和其中的设计模式**

 

**了解其中的方法**

 

#### AQS中的数据结构-节点和同步队列

 

 

#### 节点在同步队列中的增加和移出

 

#### 独占式同步状态获取与释放

 

#### 其他同步状态获取与释放 

 

#### Condition分析