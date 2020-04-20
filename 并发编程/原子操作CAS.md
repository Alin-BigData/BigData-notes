# 原子操作CAS(Compare and Swap)

[toc]

## 什么是原子操作？如何实现原子操作？ 

syn基于阻塞的锁的机制，存在的问题。

- 1、如果被阻塞的线程优先级很高，怎么办
- 2、拿到锁的线程一直不释放锁怎么办？
- 3、大量的竞争，消耗cpu，同时带来死锁或者其他安全。

## CAS的原理

CAS(Compare And Swap)，指令级别保证这是一个原子操作

三个运算符： 一个内存地址V，一个期望的值A，一个新值B

基本思路：如果地址V上的值和期望的值A相等，就给地址V赋给新值B，如果不是，不做任何操作。

循环（死循环，自旋）里不断的进行CAS操作

 

## CAS的问题

- ABA问题

A---》B----》A，解决办法：版本号:   A1B2-A3

- 开销问题

CAS操作长期不成功，cpu不断的循环

- 只能保证一个共享变量的原子操作





## Jdk中相关原子操作类的使用

- 更新基本类型类：AtomicBoolean，AtomicInteger，AtomicLong，AtomicReference

```JAVA
public class UseAtomicInt {

    static AtomicInteger ai = new AtomicInteger(10);

    public static void main(String[] args) {
        System.out.println(ai.getAndIncrement()); // 10--->11 先取旧值，再增
        System.out.println(ai.incrementAndGet()); // 11--->12--->out 先增，再取
        System.out.println(ai.get());
    }
}
```



- 更新数组类：AtomicIntegerArray，AtomicLongArray，AtomicReferenceArray

```JAVA
public class AtomicArray {
    static int[] value = new int[]{1, 2};

    static AtomicIntegerArray ai = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        // i是当前数组的下标
        ai.getAndSet(0, 3);
        System.out.println(ai.get(0));
        System.out.println(value[0]);

    }
}
```

```JAVA
3
1
```

- 更新引用类型：AtomicReference，AtomicMarkableReference，AtomicStampedReference

解决ABA问题的引用：

AtomicMarkableReference，关心的是，boolean **有没有动过**

AtomicStampedReference， 关心的是， 动过几次

```JAVA
public class UseAtomicReference {
    static AtomicReference<UserInfo> userRef = new AtomicReference<UserInfo>();

    public static void main(String[] args) {
        UserInfo user = new UserInfo("Mark", 15);//要修改的实体的实例
        userRef.set(user);

        UserInfo updateUser = new UserInfo("Bill", 17);//要变化的新实例
        userRef.compareAndSet(user, updateUser);
        // 改变包装的原子引用，不会改变原实体
        System.out.println(userRef.get().getName());
        System.out.println(userRef.get().getAge());
        System.out.println(user.getName());
        System.out.println(user.getAge());
    }

    //定义一个实体类
    static class UserInfo {
        private String name;
        private int age;

        public UserInfo(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
```

```JAVA
Bill
17
Mark
15
```

带版本戳的引用

```JAVA
public class UseAtomicStampedReference {
    static AtomicStampedReference<String> asr =
            new AtomicStampedReference<>("Mark", 0);

    public static void main(String[] args) throws InterruptedException {
        final int oldStamp = asr.getStamp(); //那初始的版本号
        final String oldReferenc = asr.getReference(); // 原值

        System.out.println(oldReferenc + "===========" + oldStamp);

        // 内部类
        Thread rightStampThread = new Thread(new Runnable() {


            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()
                        + "当前变量值：" + oldReferenc + "当前版本戳：" + oldStamp + "-"
                        + asr.compareAndSet(oldReferenc, oldReferenc + "Java",
                        oldStamp, oldStamp + 1));

            }
        });

        Thread errorStampThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // 虽然值是引用旧的值，但是版本号因为已经改变，但是这里依旧取了oldStamp，因此会出现错误
                String reference = asr.getReference();
                System.out.println(Thread.currentThread().getName()
                        + "当前变量值：" + reference + "当前版本戳：" + asr.getStamp() + "-"
                        + asr.compareAndSet(reference, reference + "C",
                        oldStamp, oldStamp + 1));

            }

        });

        // 先启动正确版本戳 运行完 因为正确版本运行完，版本号变了，因此错误版本会出现错误
        rightStampThread.start();
        rightStampThread.join();
        errorStampThread.start();
        errorStampThread.join();
        System.out.println(asr.getReference() + "===========" + asr.getStamp());
    }
}
```

- 原子更新字段类： AtomicReferenceFieldUpdater，AtomicIntegerFieldUpdater，AtomicLongFieldUpdater

作业：现在有一个残缺的AtomicInteger类只实现了线程安全的：get方法和compareAndSet()方法

```JAVA
public class HalfAtomicInt {
    private AtomicInteger atomicI = new AtomicInteger(0);

    public void increament() {
        // 为什么在for循环里面取值，是为了i值不变化
        for (; ; ) {
            int i = atomicI.get();
            boolean suc = atomicI.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    public int getCount() {
        return atomicI.get();
    }
}
```

**注**：[相关代码](../code/concurrent)

