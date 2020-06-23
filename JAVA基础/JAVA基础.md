---
title: JAVA基础
date: 2019-08-18 22:49:07
tags: [JAVA]
---
# JAVA基础 +高级+ 集合+IO

---

[toc]

---

## 1.Java和C++的区别
* Java 是纯粹的面向对象语言，所有的对象都继承自 java.lang.Object，C++ 为了兼容 C 即支持面向对象也支持面向过程。
* Java 通过虚拟机从而实现跨平台特性，但是 C++ 依赖于特定的平台。
* Java 没有指针，它的引用可以理解为安全指针，而 C++ 具有和 C 一样的指针。
* Java 支持自动垃圾回收，而 C++ 需要手动回收。（C++11 中引入智能指针，使用引用计数法垃圾回收）
* Java 不支持多重继承，只能通过实现多个接口来达到相同目的，而 C++ 支持多重继承。
* Java 不支持操作符重载，虽然可以对两个 String 对象支持加法运算，但是这是语言内置支持的操作，不属于操作符重载，而 C++ 可以。
* Java 内置了线程的支持，而 C++ 需要依靠第三方库。
* Java 的 goto 是保留字，但是不可用，C++ 可以使用 goto。
* Java 不支持条件编译，C++ 通过 #ifdef #ifndef 等预处理命令从而实现条件编译
简短：
Java 不提供指针来直接访问内存，程序内存更加安全
Java 的类是单继承的，C++ 支持多重继承；虽然 Java 的类不可以多继承，但是接口可以多继承。
Java 有自动内存管理机制，不需要程序员手动释放无用内存

## 2. 构造器 Constructor 是否可被 override
在讲继承的时候我们就知道父类的私有属性和构造方法并不能被继承，所以 Constructor 也就不能被 override（重写）,但是可以 overload（重载）,所以你可以看到一个类中有多个构造函数的情况。

## 3. Java程序初始化的顺序是怎么样的
　　在 Java 语言中，当实例化对象时，对象所在类的所有成员变量首先要进行初始化，只有当所有类成员完成初始化后，才会调用对象所在类的构造函数创建象。
初始化一般遵循3个原则：
1.静态对象（变量）优先于非静态对象（变量）初始化，静态对象（变量）只初始化一次，而非静态对象（变量）可能会初始化多次；
2.父类优先于子类进行初始化；
3.按照成员变量的定义顺序进行初始化。 即使变量定义散布于方法定义之中，它们依然在任何方法（包括构造函数）被调用之前先初始化；
加载顺序
- 父类（静态变量、静态语句块）
- 子类（静态变量、静态语句块）
- 父类（实例变量、普通语句块）
- 父类（构造函数）
- 子类（实例变量、普通语句块）
- 子类（构造函数）
实例

```
class Base {
    // 1.父类静态代码块
    static {
        System.out.println("Base static block!");
    }
    // 3.父类非静态代码块
    {
        System.out.println("Base block");
    }
    // 4.父类构造器
    public Base() {
        System.out.println("Base constructor!");
    }
}

public class Derived extends Base {
    // 2.子类静态代码块
    static{
        System.out.println("Derived static block!");
    }
    // 5.子类非静态代码块
    {
        System.out.println("Derived block!");
    }
    // 6.子类构造器
    public Derived() {
        System.out.println("Derived constructor!");
    }
    public static void main(String[] args) {
        new Derived();
    }
}
```

```
结果是：
Base static block!
Derived static block!
Base block
Base constructor!
Derived block!
Derived constructor!
```

## 4. 反射
什么是反射
　　反射 (Reflection) 是 Java 程序开发语言的特征之一，它允许运行中的 Java 程序获取自身的信息，并且可以操作类或对象的内部属性。通过 Class 获取 class 信息称之为反射（Reflection）
　　**简而言之，通过反射，我们可以在运行时获得程序或程序集中每一个类型的成员和成员的信息。**
　　**程序中一般的对象的类型都是在编译期就确定下来的，而 Java 反射机制可以动态地创建对象并调用其属性，这样的对象的类型在编译期是未知的。所以我们可以通过反射机制直接创建对象，即使这个对象的类型在编译期是未知的。**
　　反射的核心是 JVM 在运行时才动态加载类或调用方法/访问属性，它不需要事先（写代码的时候或编译期）知道运行对象是谁。
　　**Java 反射框架主要提供以下功能：**
　　**在运行时判断任意一个对象所属的类**
　　**在运行时构造任意一个类的对象**
　　**在运行时判断任意一个类所具有的成员变量和方法（通过反射甚至可以调用 private 方法）**
　　**在运行时调用任意一个对象的方法**
　　
　　==重点==：是运行时而不是编译时
主要用途
　　反射最重要的用途就是开发各种通用框架
　　很多框架（比如 Spring ）都是配置化的（比如通过 XML 文件配置 JavaBean,Action 之类的），为了保证框架的通用性，它们可能需要根据配置文件加载不同的对象或类，调用不同的方法，这个时候就必须用到反射——运行时动态加载需要加载的对象。
　　对与框架开发人员来说，反射虽小但作用非常大，它是各种容器实现的核心。而对于一般的开发者来说，不深入框架开发则用反射用的就会少一点，不过了解一下框架的底层机制有助于丰富自己的编程思想，也是很有益的。
获得Class对象
调用运行时类本身的 .class 属性

```
Class clazz1 = Person.class;
System.out.println(clazz1.getName());
```

通过运行时类的对象获取 getClass();

```
Person p = new Person();
Class clazz3 = p.getClass();
System.out.println(clazz3.getName());
```

使用 Class 类的 forName 静态方法

```
public static Class<?> forName(String className)
// 在JDBC开发中常用此方法加载数据库驱动:
Class.forName(driver);
```

（了解）通过类的加载器 ClassLoader

```
ClassLoader classLoader = this.getClass().getClassLoader();
Class clazz5 = classLoader.loadClass(className);
System.out.println(clazz5.getName());
```

## 5. Java的四个基本特性，对多态的理解，在项目中哪些地方用到多态
Java的四个基本特性
* ==抽象==：抽象是将一类对象的共同特征总结出来构造类的过程，包括数据抽象和行为抽象两方面。抽象只关注对象有哪些属性和行为，并不关注这些行为的细节是什么。 
* ==封装==：通常认为封装是把数据和操作数据的方法绑定起来，对数据的访问只能通过已定义的接口。面向对象的本质就是将现实世界描绘成一系列完全自治、封闭的对象。我们在类中编写的方法就是对实现细节的一种封装；我们编写一个类就是对数据和数据操作的封装。可以说，封装就是隐藏一切可隐藏的东西，只向外界提供最简单的编程接口。
* ==继承==：继承是从已有类得到继承信息创建新类的过程。提供继承信息的类被称为父类（超类、基类）；得到继承信息的类被称为子类（派生类）。继承让变化中的软件系统有了一定的延续性，同时继承也是封装程序中可变因素的重要手段。
* ==多态==：多态性是指允许不同子类型的对象对同一消息作出不同的响应。
多态的理解(多态的实现方式)
==方法重载==（overload）：实现的是编译时的多态性（也称为==前绑定==）。
==方法重写==（override）：实现的是运行时的多态性（也称为==后绑定==）。运行时的多态是面向对象最精髓的东西。
要实现**多态**需要做两件事：
**方法重写（子类继承父类并重写父类中已有的或抽象的方法）；**
**对象造型（用父类型引用引用子类型对象，这样同样的引用调用同样的方法就会根据子类对象的不同而表现出不同的行为）。**

项目中对多态的应用：
举一个简单的例子，在物流信息管理系统中，有两种用户：订购客户和卖房客户，两个客户都可以登录系统，他们有相同的方法 Login，但登陆之后他们会进入到不同的页面，也就是在登录的时候会有不同的操作，两种客户都继承父类的 Login 方法，但对于不同的对象，拥有不同的操作。
面相对象开发方式优点（B65）
较高的开发效率：可以把事物进行抽象，映射为开发的对象。
保证软件的鲁棒性：高重用性，可以重用已有的而且在相关领域经过长期测试的代码。
保证软件的高可维护性：代码的可读性非常好，设计模式也使得代码结构清晰，拓展性好。

## 6. 什么是重载和重写
重载：重载发生在同一个类中，同名的方法如果有不同的参数列表（参数类型不同、参数个数不同或者二者都不同）则视为重载。
重写：重写发生在子类与父类之间，重写要求子类被重写方法与父类被重写方法有相同的返回类型，比父类被重写方法更好访问，不能比父类被重写方法声明更多的异常（里氏代换原则）。根据不同的子类对象确定调用的那个方法。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/overloading-vs-overriding.png)

## 7. 面向对象和面向过程的区别？用面向过程可以实现面向对象吗？
**面向对象和面向过程的区别**
面向过程就像是一个细心的管家，事无具细的都要考虑到。而面向对象就像是个家用电器，你只需要知道他的功能，不需要知道它的工作原理。
面向过程是一种是“事件”为中心的编程思想。就是分析出解决问题所需的步骤，然后用函数把这些步骤实现，并按顺序调用。面向对象是以“对象”为中心的编程思想。

简单的举个例子：汽车发动、汽车到站
这对于 面向过程 来说，是两个事件，汽车启动是一个事件，汽车到站是另一个事件，面向过程编程的过程中我们关心的是事件，而不是汽车本身。针对上述两个事件，形成两个函数，之 后依次调用。（事件驱动，动词为主）
然而这对于面向对象来说，我们关心的是汽车这类对象，两个事件只是这类对象所具有的行为。而且对于这两个行为的顺序没有强制要求。（对象驱动，名词为主，将问题抽象出具体的对象，而这个对象有自己的属性和方法，在解决问题的时候是将不同的对象组合在一起使用）
用面向过程可以实现面向对象吗 ？
如果是 C 语言来展现出面向对象的思想，C 语言中是不是有个叫结构体的东西，这个里面有自己定义的变量 可以通过函数指针就可以实现对象


## 8. 面向对象开发的六个基本原则，在项目中用过哪些原则
六个基本原则（参考《设计模式之禅》）
* **单一职责**（Single Responsibility Principle 简称 SRP）：一个类应该仅有一个引起它变化的原因。在面向对象中，如果只让一个类完成它该做的事，而不涉及与它无关的领域就是践行了高内聚的原则，这个类就只有单一职责。
* **里氏替换**（Liskov Substitution Principle 简称 LSP）：任何时候子类型能够替换掉它们的父类型。子类一定是增加父类的能力而不是减少父类的能力，因为子类比父类的能力更多，把能力多的对象当成能力少的对象来用当然没有任何问题。
* **依赖倒置**（Dependence Inversion Principle 简称 DIP）：要依赖于抽象，不要依赖于具体类。要做到依赖倒置，应该做到：①高层模块不应该依赖底层模块，二者都应该依赖于抽象；②抽象不应该依赖于具体实现，具体实现应该依赖于抽象。
* **接口隔离**（Interface Segregation Principle 简称 ISP）：不应该强迫客户依赖于他们不用的方法 。接口要小而专，绝不能大而全。臃肿的接口是对接口的污染，既然接口表示能力，那么一个接口只应该描述一种能力，接口也应该是高度内聚的。
* **最少知识原则**（Least Knowledge Principle 简称 LKP）：只和你的朋友谈话。迪米特法则又叫最少知识原则，一个对象应当对其他对象有尽可能少的了解。
* **开闭原则**（Open Closed Principle 简称 OCP）：软件实体应当对扩展开放，对修改关闭。要做到开闭有两个要点：①抽象是关键，一个系统中如果没有抽象类或接口系统就没有扩展点；②封装可变性，将系统中的各种可变因素封装到一个继承结构中，如果多个可变因素混杂在一起，系统将变得复杂而换乱。

**其他原则**
合成聚和复用：优先使用聚合或合成关系复用代码
面向接口编程
优先使用组合，而非继承
一个类需要的数据应该隐藏在类的内部
类之间应该零耦合，或者只有传导耦合，换句话说，类之间要么没关系，要么只使用另一个类的接口提供的操作
在水平方向上尽可能统一地分布系统功能
项目中用到的原则
单一职责、开放封闭、合成聚合复用(最简单的例子就是String类)、接口隔离

## 9. 内部类有哪些
可以将一个类的定义放在另一个类的定义内部，这就是内部类。
在 Java 中内部类主要分为**成员内部类、局部内部类、匿名内部类、静态内部类**
### 9.1 成员内部类
成员内部类也是最普通的内部类，它是外围类的一个成员，所以他是可以无限制的访问外围类的所有成员属性和方法，尽管是private的，但是外围类要访问内部类的成员属性和方法则需要通过内部类实例来访问。

```
public class OuterClass {
    private String str;
   
    public void outerDisplay(){
        System.out.println("outerClass...");
    }
    
    public class InnerClass{
        public void innerDisplay(){
            str = "chenssy..."; //使用外围内的属性
            System.out.println(str);
            outerDisplay();  //使用外围内的方法
        }
    }
    
    // 推荐使用getxxx()来获取成员内部类，尤其是该内部类的构造函数无参数时
    public InnerClass getInnerClass(){
        return new InnerClass();
    }
    
    public static void main(String[] args) {
        OuterClass outer = new OuterClass();
        OuterClass.InnerClass inner = outer.getInnerClass();
        inner.innerDisplay();
    }
}

chenssy...
outerClass...
```

在成员内部类中要注意两点：
**成员内部类中不能存在static方法, 但是==可以存在static域==, ==前提是需要使用final关键字进行修饰.==**
**成员内部类是依附于外围类的，所以只有先创建了外围类才能够创建内部类。**

### 9.2 局部内部类
有这样一种内部类，它是嵌套在方法和作用于内的，对于这个类的使用主要是应用与解决比较复杂的问题，想创建一个类来辅助我们的解决方案，到那时又不希望这个类是公共可用的，所以就产生了局部内部类，局部内部类和成员内部类一样被编译，只是它的作用域发生了改变，它只能在该方法和属性中被使用，出了该方法和属性就会失效。
//定义在方法里：

```
public class Parcel5 {
    public Destionation destionation(String str){
        class PDestionation implements Destionation{
            private String label;
            private PDestionation(String whereTo){
                label = whereTo;
            }
            public String readLabel(){
                return label;
            }
        }
        return new PDestionation(str);
    }
    
    public static void main(String[] args) {
        Parcel5 parcel5 = new Parcel5();
        Destionation d = parcel5.destionation("chenssy");
    }
}

//定义在作用域内:
public class Parcel6 {
    private void internalTracking(boolean b){
        if(b){
            class TrackingSlip{
                private String id;
                TrackingSlip(String s) {
                    id = s;
                }
                String getSlip(){
                    return id;
                }
            }
            TrackingSlip ts = new TrackingSlip("chenssy");
            String string = ts.getSlip();
        }
    }
    
    public void track(){
        internalTracking(true);
    }
    
    public static void main(String[] args) {
        Parcel6 parcel6 = new Parcel6();
        parcel6.track();
    }
}
```

### 9.3 匿名内部类
匿名内部类也就是没有名字的内部类。正因为没有名字，所以匿名内部类只能使用一次，它通常用来简化代码编写。但使用匿名内部类还有个前提条件：**必须继承一个父类或实现一个接口**
实例1：不使用匿名内部类来实现抽象方法

```
abstract class Person {
    public abstract void eat();
}
 
class Child extends Person {
    public void eat() {
        System.out.println("eat something");
    }
}
 
public class Demo {
    public static void main(String[] args) {
        Person p = new Child();
        p.eat();
    }
}
```

运行结果：eat something
可以看到，我们用 Child 继承了 Person 类，然后实现了 Child 的一个实例，将其向上转型为 Person 类的引用
但是，如果此处的 Child 类只使用一次，那么将其编写为独立的一个类岂不是很麻烦？
这个时候就引入了匿名内部类
实例2：匿名内部类的基本实现

```
abstract class Person {
    public abstract void eat();
}
 
public class Demo {
    public static void main(String[] args) {
        Person p = new Person() {
            public void eat() {
                System.out.println("eat something");
            }
        };
        p.eat();
    }
}
```

运行结果：eat something
可以看到，我们直接将抽象类 Person 中的方法在大括号中实现了，这样便可以省略一个类的书写，并且，匿名内部类还能用于接口上。
实例3：在接口上使用匿名内部类

```
interface Person {
    public void eat();
}
 
public class Demo {
    public static void main(String[] args) {
        Person p = new Person() {
            public void eat() {
                System.out.println("eat something");
            }
        };
        p.eat();
    }
}
```

运行结果：eat something
由上面的例子可以看出，**只要一个类是抽象的或是一个接口，那么其子类中的方法都可以使用匿名内部类来实现**
最常用的情况就是在多线程的实现上，因为要实现多线程必须继承 Thread 类或是继承 Runnable 接口

实例4：Thread类的匿名内部类实现

```
public class Demo {
    public static void main(String[] args) {
        Thread t = new Thread() {
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.print(i + " ");
                }
            }
        };
        t.start();
    }
}
```

运行结果：1 2 3 4 5
实例5：Runnable接口的匿名内部类实现

```
public class Demo {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            public void run() {
                for (int i = 1; i <= 5; i++) {
                    System.out.print(i + " ");
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}
```

运行结果：1 2 3 4 5

### 9.4 静态内部类
关键字 static 中提到 static 可以修饰成员变量、方法、代码块，其他它还可以修饰内部类，使用 static 修饰的内部类我们称之为静态内部类，不过我们更喜欢称之为嵌套内部类。静态内部类与非静态内部类之间存在一个最大的区别，我们知道非静态内部类在编译完成之后会隐含地保存着一个引用，该引用是指向创建它的外围内，但是静态内部类却没有。
**它的创建是不需要依赖于外围类的。**
**它不能使用任何外围类的非 static 成员变量和方法。**

```
public class OuterClass {
    private String sex;
    public static String name = "chenssy";
    
    // 静态内部类 
    static class InnerClass1{
        // 在静态内部类中可以存在静态成员
        public static String _name1 = "chenssy_static";
        
        public void display(){ 
            // 静态内部类只能访问外围类的静态成员变量和方法
		   // 不能访问外围类的非静态成员变量和方法
            System.out.println("OutClass name :" + name);
        }
    }
    

    // 非静态内部类
    class InnerClass2{
        // 非静态内部类中不能存在静态成员
        public String _name2 = "chenssy_inner";
        // 非静态内部类中可以调用外围类的任何成员,不管是静态的还是非静态的
        public void display(){
            System.out.println("OuterClass name：" + name);
        }
    }
    
    // 外围类方法
    public void display(){
        // 外围类访问静态内部类：内部类
        System.out.println(InnerClass1._name1);
        // 静态内部类 可以直接创建实例不需要依赖于外围类
        new InnerClass1().display();
        
        // 非静态内部的创建需要依赖于外围类
        OuterClass.InnerClass2 inner2 = new OuterClass().new InnerClass2();
        // 方位非静态内部类的成员需要使用非静态内部类的实例
        System.out.println(inner2._name2);
        inner2.display();
    }
    
    public static void main(String[] args) {
        OuterClass outer = new OuterClass();
        outer.display();
    }
}
----------------
Output:
chenssy_static
OutClass name :chenssy
chenssy_inner
OuterClass name：chenssy
```

## 10. 组合、继承和代理的区别
定义
组合：在新类中 new 另外一个类的对象，以添加该对象的特性。
继承：从基类继承得到子类，获得父类的特性。
代理：在代理类中创建某功能的类，调用类的一些方法以获得该类的部分特性。
使用场合
组合：各部件之间没什么关系，只需要组合即可。例如组装电脑，需要 new CPU(), new RAM(), new Disk()

```
public class Computer {
    public Computer() {
        CPU cpu=new CPU();
        RAM ram=new RAM();
        Disk disk=new Disk();
    }
}
class CPU{}
class RAM{}
class Disk{}
```

继承：子类需要具有父类的功能，各子类之间有所差异。例如 Shape 类作为父类，子类有 Rectangle，CirCle，Triangle……。
代理：飞机控制类，我不想暴露太多飞机控制的功能，只需部分前进左右转的控制（而不需要暴露发射导弹功能）。**通过在代理类中 new 一个飞机控制对象，然后在方法中添加飞机控制类的各个需要暴露的功能。**

```
public class PlaneDelegation{    
    private PlaneControl planeControl;    //private外部不可访问
	
    // 飞行员权限代理类，普通飞行员不可以开火
    PlaneDelegation(){
        planeControl = new PlaneControl();
    }
    public void speed(){
        planeControl.speed();
    }
    public void left(){
        planeControl.left();
    }
    public void right(){
        planeControl.right();
    }
}

final class PlaneControl {// final表示不可继承，控制器都能继承那还得了
    protected void speed() {}
    protected void fire() {}
    protected void left() {}
    protected void right() {}
}
```

说明：
**继承：代码复用，引用不灵活；**
**组合：代码复用，**
**接口：引用灵活；**
推荐组合+接口使用，看 IO 中包装流 FilterInputStream 中的策略模式

## 11. 什么是构造函数
构造函数是函数的一种特殊形式。特殊在哪里？构造函数中不需要定义返回类型（void 是无需返回值的意思，请注意区分两者），且构造函数的名称与所在的类名完全一致，其余的与函数的特性相同，可以带有参数列表，可以存在函数的重载现象。
一般用来初始化一些成员变量，当要生成一个类的对象（实例）的时候就会调用类的构造函数。如果不显示声明类的构造方法，会自动生成一个默认的不带参数的空的构造函数。

```
public class Demo{
    private int num=0;

    //无参构造函数
    Demo()
    {
        System.out.println("constractor_run");
    }

    //有参构造函数
    Demo(int num)
    {
        System.out.println("constractor_args_run");
    }

    //普通成员函数
    public void demoFunction()
    {
        System.out.println("function_run");
    }
}
```

如果在类中我们不声明构造函数，JVM 会帮我们**默认生成一个空参数的构造函数**；如果在类中我们声明了带参数列表的构造函数，JVM 就不会帮我们默认生成一个空参数的构造函数，我们想要使用空参数的构造函数就必须自己去显式的声明一个空参的构造函数。

构造函数的作用：
**创建对象**。任何一个对象创建时，都需要初始化才能使用，所以任何类想要创建实例对象就必须具有构造函数。
**对象初始化**。构造函数可以对对象进行初始化，并且是给与之格式（参数列表）相符合的对象初始化，是具有一定针对性的初始化函数。

## 12. 向上造型和向下造型
父类引用能指向子类对象，子类引用不能指向父类对象；
向上造型
父类引用指向子类对象，例如：

```
Father f1 = new Son();
```

向下造型
把指向子类对象的父类引用赋给子类引用，需要强制转换，例如：

```
Father f1 = new Son();
Son s1 = (Son)f1;
```

但有运行出错的情况：

```
Father f2 = new Father();
Son s2 = (Son)f2; //编译无错但运行会出现错误
```

在不确定父类引用是否指向子类对象时，可以用 instanceof 来判断：

```
if(f3 instanceof Son){
     Son s3 = (Son)f3;
}
```

## 12 关键字
### 12.1 final与static的区别
**final**
* 数据：
声明数据为常量，可以是编译时常量，也可以是在运行时被初始化后不能被改变的常量。
对于基本类型，final 使数值不变；
对于引用类型，final 使引用不变，也就不能引用其它对象，但是被引用的对象本身是可以修改的。

```
final int x = 1;
// x = 2;  // cannot assign value to final variable 'x'
final A y = new A();
y.a = 1;
```

*  方法
声明方法不能被子类覆盖。
private 方法隐式地被指定为 final，如果在子类中定义的方法和基类中的一个 private 方法签名相同，此时子类的方法不是覆盖基类方法，而是在子类中定义了一个新的方法。

*  类
声明类不允许被继承。

**static**
* 静态变量
	静态变量在内存中只存在一份，只在类初始化时赋值一次。
 - 静态变量：类所有的实例都共享静态变量，可以直接通过类名来访问它；
 - 实例变量：每创建一个实例就会产生一个实例变量，它与该实例同生共死。

```
public class A {
    private int x;        // 实例变量
    public static int y;  // 静态变量
}
```
***注意：不能在成员函数内部定义static变量。***

* 静态方法
静态方法在类加载的时候就存在了，它不依赖于任何实例，所以静态方法必须有实现，也就是说它不能是抽象方法（abstract）。

* 静态语句块
静态语句块在类初始化时运行一次。

* 静态内部类
内部类的一种，静态内部类不依赖外部类，且不能访问外部类的非静态的变量和方法。

* 静态导包
import static com.xxx.ClassName.*

　　在使用静态变量和方法时不用再指明 ClassName，从而简化代码，但可读性大大降低。

### 12.2 break、continue、return
break
跳出当前循环；但是如果是嵌套循环，则只能跳出当前的这一层循环，只有逐层 break 才能跳出所有循环。

```
for (int i = 0; i < 10; i++) {
    // 在执行i==6时强制终止循环，i==6不会被执行
    if (i == 6)
        break;
    System.out.println(i);  
}  
```

输出结果为 0 1 2 3 4 5 ；6以后的都不会输出

continue
终止当前循环，但是不跳出循环（在循环中 continue 后面的语句是不会执行了），继续往下根据循环条件执行循环。

```
for (int i = 0; i < 10; i++) {  
    // i==6不会被执行，而是被中断了    
    if (i == 6)
        continue;
    System.out.println(i);  
}
```

输出结果为0 1 2 3 4 5 7 8 9； 只有6没有输出

return
return 从当前的方法中退出，返回到该调用的方法的语句处，继续执行。
return **返回一个值给调用该方法的语句**，返回值的数据类型必须与方法的声明中的返回值的类型一致。
return 后面也可以不带参数，不带参数就是返回空，其实主要目的就是用于想中断函数执行，返回调用函数处。
**特别注意：返回值为 void 的方法，从某个判断中跳出，必须用 return。**

### 12.3 final、finally和finalize区别
**final**
- final 用于声明属性、方法和类，分别表示属性不可变、方法不可覆盖和类不可被继承。
- final 属性：被final修饰的变量不可变（引用不可变）
- final 方法：不允许任何子类重写这个方法，但子类仍然可以使用这个方法
- final 参数：用来表示这个参数在这个函数内部不允许被修改
- final 类：此类不能被继承，所有方法都不能被重写

**finally**
　　在异常处理的时候，提供 finally 块来执行任何的清除操作。如果抛出一个异常，那么相匹配的 catch 字句就会执行，然后控制就会进入 finally 块，前提是有 finally 块。例如：数据库连接关闭操作上
　　finally 作为异常处理的一部分，它只能用在 try/catch 语句中，并且附带一个语句块，表示这段语句最终一定会被执行（不管有没有抛出异常），经常被用在需要释放资源的情况下。（×）

异常情况说明：
在执行 try 语句块之前已经返回或抛出异常，所以 try 对应的 finally 语句并没有执行。
我们在 try 语句块中执行了 System.exit (0) 语句，终止了 Java 虚拟机的运行。那有人说了，在一般的 Java 应用中基本上是不会调用这个 System.exit(0) 方法的
当一个线程在执行 try 语句块或者 catch 语句块时被打断（interrupted）或者被终止（killed），与其相对应的 finally 语句块可能不会执行
还有更极端的情况，就是在线程运行 try 语句块或者 catch 语句块时，突然死机或者断电，finally 语句块肯定不会执行了。可能有人认为死机、断电这些理由有些强词夺理，没有关系，我们只是为了说明这个问题。

 **try语句在返回前，将其他所有的操作执行完，保留好要返回的值，而后转入执行finally中的语句，而后分为以下三种情况：**
 
**情况一：如果finally中有return语句，则会将try中的return语句”覆盖“掉，直接执行finally中的return语句，得到返回值，这样便无法得到try之前保留好的返回值。**

**情况二：如果finally中没有return语句，也没有改变要返回值，则执行完finally中的语句后，会接着执行try中的return语句，返回之前保留的值。**

**情况三：如果finally中没有return语句，但是改变了要返回的值，这里有点类似与引用传递和值传递的区别，分以下两种情况，：**
* 1）如果return的数据是基本数据类型或文本字符串，则在finally中对该基本数据的改变==不起作用==，try中的return语句依然会返回进入finally块之前保留的值。

* 2）如果return的数据是==引用数据类型==，而在finally中对该引用数据类型的属性值的==改变起作用==，try中的return语句返回的就是在finally中改变后的该属性的值。
参考：
[Java中try catch finally语句中含有return语句的执行情况（总结版）](https://blog.csdn.net/ns_code/article/details/17485221)

**finalize**
　　finalize() 是 Object 中的方法，当垃圾回收器将要回收对象所占内存之前被调用，即当一个对象被虚拟机宣告死亡时会先调用它 finalize() 方法，让此对象处理它生前的最后事情（这个对象可以趁这个时机挣脱死亡的命运）。要明白这个问题，先看一下虚拟机是如何判断一个对象该死的。
　　可以覆盖此方法来实现对其他资源的回收，例如关闭文件。
判定死亡
　　Java 采用可达性分析算法来判定一个对象是否死期已到。Java中以一系列 "GC Roots" 对象作为起点，如果一个对象的引用链可以最终追溯到 "GC Roots" 对象，那就天下太平。
　　否则如果只是A对象引用B，B对象又引用A，A B引用链均未能达到 "GC Roots" 的话，那它俩将会被虚拟机宣判符合死亡条件，具有被垃圾回收器回收的资格。
最后的救赎
上面提到了判断死亡的依据，但被判断死亡后，还有生还的机会。
如何自我救赎：
对象覆写了 finalize() 方法（这样在被判死后才会调用此方法，才有机会做最后的救赎）；
在 finalize() 方法中重新引用到 "GC Roots" 链上（如把当前对象的引用 this 赋值给某对象的类变量/成员变量，重新建立可达的引用）.
需要注意：
　　finalize() 只会在对象内存回收前被调用一次 (The finalize method is never invoked more than once by a Java virtual machine for any given object. )
　　finalize() 的调用**具有不确定性，只保证方法会调用，但不保证方法里的任务会被执行完**（比如一个对象手脚不够利索，磨磨叽叽，还在自救的过程中，被杀死回收了）。
　　
finalize()的作用
　　虽然以上以对象救赎举例，但 finalize() 的作用往往被认为是用来做最后的资源回收。 　　基于在自我救赎中的表现来看，此方法有很大的不确定性（不保证方法中的任务执行完）而且运行代价较高。所以用来回收资源也不会有什么好的表现。
　　综上：finalize() 方法并没有什么鸟用。
　　至于为什么会存在一个鸡肋的方法：书中说 “它不是 C/C++ 中的析构函数，而是 Java 刚诞生时为了使 C/C++ 程序员更容易接受它所做出的一个妥协”。

### 12.4 volatile
volatile 是一个类型修饰符（type specifier），它是被设计用来修饰被不同线程访问和修改的变量。在使用 volatile 修饰成员变量后，所有线程在任何时间所看到变量的值都是相同的。此外，使用 volatile 会组织编译器对代码的优化，因此会降低程序的执行效率。所以，除非迫不得已，否则，能不使用 volatile 就尽量不要使用 volatile。
每次访问变量时，总是获取主内存的最新值
每次修改变量后，立刻写回到主内存中

### 12.5 instanceof
instanceof 是 Java 的一个二元操作符，类似于 ==，>，< 等操作符。
instanceof 是 Java 的保留关键字。它的作用是测试它左边的对象是否是它右边的类的实例，返回 boolean 的数据类型。

```
public class Main {
    public static void main(String[] args) {
        Object testObject = new ArrayList();
        displayObjectClass(testObject);
    }
    public static void displayObjectClass(Object o) {
        if (o instanceof Vector)
            System.out.println("对象是 java.util.Vector 类的实例");
        else if (o instanceof ArrayList)
            System.out.println("对象是 java.util.ArrayList 类的实例");
        else
            System.out.println("对象是 " + o.getClass() + " 类的实例");
    }
}
```

### 12.6 transient
我们都知道一个对象只要实现了 Serilizable 接口，这个对象就可以被序列化，Java 的这种序列化模式为开发者提供了很多便利，我们可以不必关系具体序列化的过程，只要这个类实现了 Serilizable 接口，这个类的所有属性和方法都会自动序列化。
然而在实际开发过程中，我们常常会遇到这样的问题，这个类的有些属性需要序列化，而其他属性不需要被序列化，打个比方，**如果一个用户有一些敏感信息（如密码，银行卡号等），为了安全起见，不希望在网络操作（主要涉及到序列化操作，本地序列化缓存也适用）中被传输，这些信息对应的变量就可以加上 transient 关键字。换句话说，这个字段的生命周期仅存于调用者的内存中而不会写到磁盘里持久化。**
总之，Java 的 transient 关键字为我们提供了便利，你只需要实现 Serilizable 接口，将不需要序列化的属性前添加关键字transient，序列化对象的时候，这个属性就不会序列化到指定的目的地中。

## 13 深拷贝与浅拷贝
浅拷贝：被复制对象的所有变量都含有与原来的对象相同的值，而所有的对其他对象的引用仍然指向原来的对象。换言之，浅拷贝仅仅复制所拷贝的对象，而不复制它所引用的对象。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/shadow_copy2.jpg)

深拷贝：对基本数据类型进行值传递，对引用数据类型，创建一个新的对象，并复制其内容，此为深拷贝。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/deep_copy2.jpg)

## 14 字符串常量池
　　Java 中字符串对象创建有两种形式，一种为字面量形式，如 String str = "abc";，另一种就是使用 new 这种标准的构造对象的方法，如 String str = new String("abc");，这两种方式我们在代码编写时都经常使用，尤其是字面量的方式。然而这两种实现其实存在着一些性能和内存占用的差别。这一切都是源于 JVM 为了减少字符串对象的重复创建，其维护了一个特殊的内存，这段内存被成为字符串常量池或者字符串字面量池。
工作原理
　　当代码中出现字面量形式创建字符串对象时，JVM首先会对这个字面量进行检查，如果字符串常量池中存在相同内容的字符串对象的引用，则将这个引用返回，否则新的字符串对象被创建，然后将这个引用放入字符串常量池，并返回该引用。

```
public class Test {
    public static void main(String[] args) {

        String s1 = "abc";
        String s2 = "abc";

        // 以上两个局部变量都存在了常量池中
        System.out.println(s1 == s2); // true


        // new出来的对象不会放到常量池中,内存地址是不同的
        String s3 = new String();
        String s4 = new String();

        /**
     	* 字符串的比较不可以使用双等号,这样会比较内存地址
     	* 字符串比较应当用equals,可见String重写了equals
     	*/
        System.out.println(s3 == s4); // false
        System.out.println(s3.equals(s4)); // true
    }
}
```

## 15 解释型语言与编译型语言的区别
　　我们使用工具编写的字母加符号的代码，是我们能看懂的高级语言，计算机无法直接理解，计算机需要先对我们编写的代码翻译成计算机语言，才能执行我们编写的程序。
　　将高级语言翻译成计算机语言有编译，解释两种方式。两种方式只是翻译的时间不同。
* 编译型语言
　　编译型语言写得程序在执行之前，需要借助一个程序，将高级语言编写的程序翻译成计算机能懂的机器语言，然后，这个机器语言就能直接执行了，也就是我们常见的（exe文件）。
*  解释型语言
　　解释型语言的程序不需要编译，节省了一道工序，不过解释型的语言在运行的时候需要翻译，每个语句都是执行的时候才翻译，对比编译型语言，效率比较低。通俗来讲，就是借助一个程序，且这个程序能试图理解编写的代码，然后按照编写的代码中的要求执行。
* 脚本语言
　　脚本语言也是一种解释型语言，又被称为扩建的语言，或者动态语言不需要编译，可以直接使用，由解释器来负责解释。
脚本语言一般都是以文本形式存在，类似于一种命令。
* 通俗理解编译型语言和解释型语言
　　同行讨论编译型语言和解释型语言的时候，这么说过，编译型语言相当于做一桌子菜再吃，解释型语言就是吃火锅。解释型的语言执行效率低，类似火锅需要一边煮一边吃。

## 16 String StringBuffer 和 StringBuilder 的区别是什么 String 为什么是不可变的
**可变性** 　
简单的来说：String 类中使用 **final** 关键字字符数组保存字符串，**private final　char value[]**，所以 String 对象是不可变的。而StringBuilder 与 StringBuffer 都继承自 AbstractStringBuilder 类，在 AbstractStringBuilder 中也是使用字符数组保存字符串char[]value 但是没有用 final 关键字修饰，所以这两种对象都是可变的。
StringBuilder 与 StringBuffer 的构造方法都是调用父类构造方法也就是 AbstractStringBuilder 实现的。
AbstractStringBuilder.java

```
abstract class AbstractStringBuilder implements Appendable, CharSequence {
    char[] value;
    int count;
    AbstractStringBuilder() {
    }
    AbstractStringBuilder(int capacity) {
        value = new char[capacity];
    }
```

**线程安全性**
String 中的对象是不可变的，也就可以理解为常量，线程安全。AbstractStringBuilder 是 StringBuilder 与 StringBuffer 的公共父类，定义了一些字符串的基本操作，如 expandCapacity、append、insert、indexOf 等公共方法。StringBuffer 对方法加了同步锁或者对调用的方法加了同步锁，所以是线程安全的。StringBuilder 并没有对方法进行加同步锁，所以是非线程安全的。 　　

**性能**
每次对 String 类型进行改变的时候，都会生成一个新的 String 对象，然后将指针指向新的 String 对象。StringBuffer 每次都会对 StringBuffer 对象本身进行操作，而不是生成新的对象并改变对象引用。相同情况下使用 StringBuilder 相比使用 StringBuffer 仅能获得 10%~15% 左右的性能提升，但却要冒多线程不安全的风险。

**对于三者使用的总结：**
操作少量的数据 = String
单线程操作字符串缓冲区下操作大量数据 = StringBuilder
多线程操作字符串缓冲区下操作大量数据 = StringBuffer

## 17 基本数据类型与运算
### 17.1 Java的基本数据类型和引用类型，自动装箱和拆箱
4 类 8 种基本数据类型。4 整数型，2 浮点型，1 布尔型，1 字符型
类型 存储 取值范围 默认值 包装类
整数型    
byte 8 最大存储数据量是 255，最小 -27，最大 27-1，[-128~127] (byte) 0 Byte
short 16 最大数据存储量是 65536，[-215,215-1]，[-32768,32767]，±3万 (short) 0 Short
int 32 最大数据存储容量是 231-1，[-231,231-1]，±21亿，[ -2147483648, 2147483647] 0 Integer
long 64 最大数据存储容量是 264-1，[-263,263-1]， ±922亿亿（±（922+16个零）） 0L Long
浮点型    
float 32 数据范围在 3.4e-45~1.4e38，直接赋值时必须在数字后加上 f 或 F 0.0f Float
double 64 数据范围在 4.9e-324~1.8e308，赋值时可以加 d 或 D 也可以不加 0.0d Double
布尔型    
boolean 1 true / flase false Boolean
字符型    
char 16 存储 Unicode 码，用单引号赋值 '\u0000' (null) Character
引用数据类型
类（class）、接口（interface）、数组
自动装箱和拆箱
基本数据类型和它对应的封装类型之间可以相互转换。自动拆装箱是 jdk5.0 提供的新特特性，它可以自动实现类型的转换
装箱：从基本数据类型到封装类型叫做装箱
拆箱：从封装类型到基本数据类型叫拆箱

```
// jdk 1.5
public class TestDemo {
    public static void main(String[] args) {
        Integer m =10;
        int i = m;
    }
}
```

　　上面的代码在 jdk1.4 以后的版本都不会报错，它实现了自动拆装箱的功能，如果是 jdk1.4，就得这样写了

```
// jdk 1.4
public class TestDemo {
    public static void main(String[] args) {
        Integer b = new Integer(210);
        int c = b.intValue();
    }
}
```

### 17.2 ValueOf缓存池
　　new Integer(123) 与 Integer.valueOf(123) 的区别在于，new Integer(123) 每次都会新建一个对象，而 Integer.valueOf(123) 可能会使用缓存对象，因此多次使用 Integer.valueOf(123) 会取得同一个对象的引用。

```
Integer x = new Integer(123);
Integer y = new Integer(123);
System.out.println(x == y);    // false
Integer z = Integer.valueOf(123);
Integer k = Integer.valueOf(123);
System.out.println(z == k);   // true
```

　　编译器会在自动装箱过程调用 valueOf() 方法，因此多个 Integer 实例使用自动装箱来创建并且值相同，那么就会引用相同的对象。

```
Integer m = 123;
Integer n = 123;
System.out.println(m == n); // true
```

valueOf() 方法的实现比较简单，就是先判断值是否在缓存池中，如果在的话就直接使用缓存池的内容。
// valueOf 源码实现

```
public static Integer valueOf(int i) {
    if (i >= IntegerCache.low && i <= IntegerCache.high)
        return IntegerCache.cache[i + (-IntegerCache.low)];
    return new Integer(i);
}
```

在 Java 8 中，Integer 缓存池的大小默认为 -128~127。

```
static final int low = -128;
static final int high;
static final Integer cache[];

static {
    // high value may be configured by property
    int h = 127;
    String integerCacheHighPropValue =
        sun.misc.VM.getSavedProperty("java.lang.Integer.IntegerCache.high");
    if (integerCacheHighPropValue != null) {
        try {
            int i = parseInt(integerCacheHighPropValue);
            i = Math.max(i, 127);
            // Maximum array size is Integer.MAX_VALUE
            h = Math.min(i, Integer.MAX_VALUE - (-low) -1);
        } catch( NumberFormatException nfe) {
            // If the property cannot be parsed into an int, ignore it.
        }
    }
    high = h;

    cache = new Integer[(high - low) + 1];
    int j = low;
    for(int k = 0; k < cache.length; k++)
        cache[k] = new Integer(j++);

    // range [-128, 127] must be interned (JLS7 5.1.7)
    assert IntegerCache.high >= 127;
}
```

Java 还将一些其它基本类型的值放在缓冲池中，包含以下这些：

```
boolean values true and false
all byte values
short values between -128 and 127
int values between -128 and 127
char in the range \u0000 to \u007F
```

因此在使用这些基本类型对应的包装类型时，就可以直接使用缓冲池中的对象。

### 17.3 i++和++i有什么区别
i++
i++ 是在程序执行完毕后进行自增，而 ++i 是在程序开始执行前进行自增。
i++ 的操作分三步
栈中取出 i
i 自增 1
将 i 存到栈
三个阶段：内存到寄存器，寄存器自增，写回内存（这三个阶段中间都可以被中断分离开）
所以 i++ 不是原子操作，上面的三个步骤中任何一个步骤同时操作，都可能导致 i 的值不正确自增
++i
在多核的机器上，CPU 在读取内存 i 时也会可能发生同时读取到同一值，这就导致两次自增，实际只增加了一次。
i++ 和 ++i 都不是原子操作
原子性：指的是一个操作是不可中断的。即使是在多个线程一起执行的时候，一个操作一旦开始，就不会被其他线程打断。
JMM 三大特性：原子性，可见性，有序性。详情请阅读 Github 仓库：Java 并发编程 一文。

### 17.4 位运算符
Java 定义了位运算符，应用于整数类型 (int)，长整型 (long)，短整型 (short)，字符型 (char)，和字节型 (byte)等类型。
下表列出了位运算符的基本运算，假设整数变量A的值为60和变量B的值为13
A（60）：0011 1100
B（13）：0000 1101
操作符 名称 描述 例子
＆ 与 如果相对应位都是 1，则结果为 1，否则为 0 （A＆B）得到 12，即 0000 1100
| 或 如果相对应位都是 0，则结果为 0，否则为 1 （A|B）得到 61，即 0011 1101
^ 异或 如果相对应位值相同，则结果为 0，否则为 1 （A^B）得到49，即 0011 0001
〜 非 按位取反运算符翻转操作数的每一位，即 0 变成 1，1 变成 0 （〜A）得到-61，即1100 0011
<< 左移 （左移一位乘2）按位左移运算符。左操作数按位左移右操作数指定的位数。左移 n 位表示原来的值乘 2n A << 2得到240，即 1111 0000
>>  （右移一位除2）有符号右移，按位右移运算符。左操作数按位右移右操作数指定的位数 A >> 2得到15即 1111
>>> 无符号右移 无符号右移，按位右移补零操作符。左操作数的值按右操作数指定的位数右移，移动得到的空位以零填充 A>>>2得到15即0000 1111

### 17.5 原码、补码、反码是什么
机器数
　　一个数在计算机中的二进制表示形式，叫做这个数的机器数。机器数是带符号的，在计算机用一个数的最高位存放符号，正数为 0，负数为 1。
　　比如，十进制中的数 +3 ，计算机字长为 8 位，转换成二进制就是 00000011。如果是 -3 ，就是 10000011 。那么，这里的 00000011 和 10000011 就是机器数。
真值
　　因为第一位是符号位，所以机器数的形式值就不等于真正的数值。例如上面的有符号数 10000011，其最高位 1 代表负，其真正数值是 -3 而不是形式值 131（10000011 转换成十进制等于 131）。所以，为区别起见，将带符号位的机器数对应的真正数值称为机器数的真值。
例：0000 0001 的真值 = +000 0001 = +1，1000 0001 的真值 = –000 0001 = –1
原码
　　原码就是符号位加上真值的绝对值，即用第一位表示符号，其余位表示值。比如如果是 8 位二进制:
　　[+1]原 = 0000 0001
　　[-1]原 = 1000 0001
　　第一位是符号位。因为第一位是符号位，所以 8 位二进制数的取值范围就是：[1111 1111 , 0111 1111]，即：[-127 , 127]
　　原码是人脑最容易理解和计算的表示方式
反码
反码的表示方法是：
正数的反码是其本身；
负数的反码是在其原码的基础上，符号位不变，其余各个位取反。
[+1] = [00000001]原 = [00000001]反
[-1] = [10000001]原= [11111110]反
可见如果一个反码表示的是负数, 人脑无法直观的看出来它的数值. 通常要将其转换成原码再计算。
补码
补码的表示方法是：
正数的补码就是其本身；
负数的补码是在其原码的基础上，符号位不变，其余各位取反, 最后+1。(反码的基础上 +1)
[+1] = [0000 0001]原 = [0000 0001]反 = [0000 0001]补
[-1] = [1000 0001]原 = [1111 1110]反 = [1111 1111]补
对于负数，补码表示方式也是人脑无法直观看出其数值的。 通常也需要转换成原码在计算其数值。

### 17.6 不用额外变量交换两个整数的值
如果给定整数 a 和 b，用以下三行代码即可交换 a 和b 的值
a = a ^ b;
b = a ^ b;
a = a ^ b;

假设 a 异或 b 的结果记为 c，c 就是 a 整数位信息和 b 整数位信息的所有不同信息。
比如：a = 4 = 100，b = 3 = 011，a^b = c = 111
a 异或 c 的结果就是 b，比如：a = 4 = 100，c = 111，a^c = 011 = 3 = b
b 异或c 的结果就是 a，比如：b = 3 = 011，c = 111，b^c = 100 = 4 = a
说明：位运算的题目基本上都带有靠经验积累才会做的特征，也就是准备阶段需要做足够多的题，面试时才会有良好的感觉。

### 17.7 不使用运算符进行a+b操作
a^b; 得到不含进位之和
(a & b)<<1; 进位
只要进位不为零，则迭代；否则返回

```
#include <stdio.h>

int add(int a, int b)
{
    int c = a & b; // 进位
    int r = a ^ b; // 不带进位和
    if(c == 0){
        return r;
    }
    else{
        return add(r, c << 1);
    }
}

int main(int argn, char *argv[])
{
    printf("sum = %d\n", add(-10000, 56789));
    return 0;
}
```
例：
首先看十进制是如何做的： 5+7=12，三步走
第一步：相加各位的值，不算进位，得到2。
第二步：计算进位值，得到10. 如果这一步的进位值为0，那么第一步得到的值就是最终结果。

第三步：重复上述两步，只是相加的值变成上述两步的得到的结果2和10，得到12。

同样我们可以用三步走的方式计算二进制值相加： 5-101，7-111 第一步：相加各位的值，不算进位，得到010，二进制每位相加就相当于各位做异或操作，101^111。

第二步：计算进位值，得到1010，相当于各位做与操作得到101，再向左移一位得到1010，(101&111)<<1。

第三步重复上述两步， 各位相加 010^1010=1000，进位值为100=(010&1010)<<1。
     继续重复上述两步：1000^100 = 1100，进位值为0，跳出循环，1100为最终结果。

### 17.8 &和&& 、|和||的区别
（1）&& 和 & 都是表示与，区别是 && 只要第一个条件不满足，后面条件就不再判断。而 & 要对所有的条件都进行判断。
// 例如：

```
public static void main(String[] args) {  
    if((23!=23) && (100/0==0)){  
        System.out.println("运算没有问题。");  
    }else{  
        System.out.println("没有报错");  
    }  
}  
```

// 输出的是“没有报错”。而将 && 改为 & 就会如下错误：
// Exception in thread "main" java.lang.ArithmeticException: / by zero

原因：
&&时判断第一个条件为 false，后面的 100/0==0 这个条件就没有进行判断。
& 时要对所有的条件进行判断，所以会对后面的条件进行判断，所以会报错。
（2）|| 和 | 都是表示 “或”，区别是 || 只要满足第一个条件，后面的条件就不再判断，而 | 要对所有的条件进行判断。 看下面的程序：

```
public static void main(String[] args) {  
    if((23==23)||(100/0==0)){  
    	System.out.println("运算没有问题。");  
    }else{  
    	System.out.println("没有报错");  
    }  
}
```

// 此时输出“运算没有问题”。若将||改为|则会报错。

原因
|| 判断第一个条件为 true，后面的条件就没有进行判断就执行了括号中的代码
而 | 要对所有的条件进行判断，所以会报错


## 18 Object 通用方法
以下为 Object 中的通用方法

```
public final native Class<?> getClass()

public native int hashCode()

public boolean equals(Object obj)

protected native Object clone() throws CloneNotSupportedException

public String toString()

public final native void notify()

public final native void notifyAll()

public final native void wait(long timeout) throws InterruptedException

public final void wait(long timeout, int nanos) throws InterruptedException

public final void wait() throws InterruptedException

protected void finalize() throws Throwable {} // JVM内存回收之finalize()方法
equals()
```

### 18.1 equals() 与 == 的区别
对于基本类型，== 判断两个值是否相等，基本类型没有 equals() 方法。
对于引用类型，== 判断两个实例是否引用同一个对象，而 equals() 判断引用的对象是否等价。

```
Integer x = new Integer(1);
Integer y = new Integer(1);
System.out.println(x.equals(y)); // true
System.out.println(x == y);      // false
```

### 18.2 等价关系
（一）自反性
x.equals(x); // true

（二）对称性
x.equals(y) == y.equals(x); // true

（三）传递性
	if (x.equals(y) && y.equals(z))
    x.equals(z); // true;

（四）一致性
多次调用 equals() 方法结果不变
x.equals(y) == x.equals(y); // true

（五）与 null 的比较
对任何不是 null 的对象 x 调用 x.equals(null) 结果都为 false
x.euqals(null); // false;

### 18.3 实现
检查是否为同一个对象的引用，如果是直接返回 true；
检查是否是同一个类型，如果不是，直接返回 false；
将 Object 实例进行转型；
判断每个关键域是否相等。

```
public class EqualExample {
    private int x;
    private int y;
    private int z;

    public EqualExample(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EqualExample that = (EqualExample) o;

        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;
    }
}
```

## 19 hashCode()
　　hasCode() 返回散列值，而 equals() 是用来判断两个实例是否等价。**等价的两个实例散列值一定要相同，但是散列值相同的两个实例不一定等价。**
　　在覆盖 equals() 方法时应当总是覆盖 hashCode() 方法，保证等价的两个实例散列值也相等。
　　下面的代码中，新建了两个等价的实例，并将它们添加到 HashSet 中。我们希望将这两个实例当成一样的，只在集合中添加一个实例，但是因为 EqualExample 没有实现 hasCode() 方法，因此这两个实例的散列值是不同的，最终导致集合添加了两个等价的实例。

```
EqualExample e1 = new EqualExample(1, 1, 1);
EqualExample e2 = new EqualExample(1, 1, 1);
System.out.println(e1.equals(e2)); // true
HashSet<EqualExample> set = new HashSet<>();
set.add(e1);
set.add(e2);
System.out.println(set.size());   // 2
```

　
##20 == 与 equals
`==`  : 它的作用是判断两个对象的地址是不是相等。即，判断两个对象是不是同一个对象。(基本数据类型==比较的是值，引用数据类型==比较的是内存地址)
equals() : 它的作用也是判断两个对象是否相等。但它一般有两种使用情况：
情况1：类没有覆盖 equals() 方法。则通过 equals() 比较该类的两个对象时，等价于通过“==”比较这两个对象。
情况2：类覆盖了 equals() 方法。一般，我们都覆盖 equals() 方法来两个对象的内容相等；若它们的内容相等，则返回 true (即，认为这两个对象相等)。
举个例子：

```
public class test1 {
    public static void main(String[] args) {
        String a = new String("ab"); // a 为一个引用
        String b = new String("ab"); // b为另一个引用,对象的内容一样
        String aa = "ab"; // 放在常量池中
        String bb = "ab"; // 从常量池中查找
        if (aa == bb) // true
            System.out.println("aa==bb");
        if (a == b) // false，非同一对象
            System.out.println("a==b");
        if (a.equals(b)) // true
            System.out.println("aEQb");
        if (42 == 42.0) { // true
            System.out.println("true");
        }
    }
}
```

说明：
**String 中的 equals 方法是被重写过的，因为 object 的 equals 方法是比较的对象的内存地址，而 String 的 equals 方法比较的是对象的值。**
当创建 String 类型的对象时，虚拟机会在常量池中查找有没有已经存在的值和要创建的值相同的对象，如果有就把它赋给当前引用。如果没有就在常量池中重新创建一个 String 对象。

面试官可能会问你：“你重写过 hashcode 和 equals 么，**为什么重写equals时必须重写hashCode方法？**”
hashCode（）介绍
hashCode() 的作用是获取哈希码，也称为散列码；它实际上是返回一个int整数。这个**哈希码的作用是确定该对象在哈希表中的索引位置**。hashCode() 定义在JDK的Object.java中，这就意味着Java中的任何类都包含有hashCode() 函数。
散列表存储的是键值对(key-value)，它的特点是：能根据“键”快速的检索出对应的“值”。这其中就利用到了散列码！（可以快速找到所需要的对象）
为什么要有 hashCode
我们以“HashSet 如何检查重复”为例子来说明为什么要有 hashCode：
==**重要:**==
**当你把对象加入 HashSet 时，HashSet 会先计算对象的 hashcode 值来判断对象加入的位置，同时也会与其他已经加入的对象的 hashcode 值作比较，如果没有相符的hashcode，HashSet会假设对象没有重复出现。但是如果发现有相同 hashcode 值的对象，这时会调用 equals（）方法来检查 hashcode 相等的对象是否真的相同。如果两者相同，HashSet 就不会让其加入操作成功。如果不同的话，就会重新散列到其他位置。这样我们就大大减少了 equals 的次数，相应就大大提高了执行速度。**
hashCode（）与equals（）的相关规定
**如果两个对象相等，则hashcode一定也是相同的**
**两个对象相等,对两个对象分别调用equals方法都返回true**
**两个对象有相同的hashcode值，它们也不一定是相等的**

因此，equals 方法被覆盖过，则 hashCode 方法也必须被覆盖
hashCode() 的默认行为是对堆上的对象产生独特值。**如果没有重写 hashCode()，则该 class 的两个对象无论如何都不会相等（即使这两个对象指向相同的数据）**理想的散列函数应当具有均匀性，即不相等的实例应当均匀分布到所有可能的散列值上。这就要求了散列函数要把所有域的值都考虑进来，可以将每个域都当成 R 进制的某一位，然后组成一个 R 进制的整数。R 一般取 31，因为它是一个奇素数，如果是偶数的话，当出现乘法溢出，信息就会丢失，因为与 2 相乘相当于向左移一位。
　　一个数与 31 相乘可以转换成移位和减法：31\*x == (x<<5)-x，编译器会自动进行这个优化。

```
Override
public int hashCode() {
    int result = 17;
    result = 31 * result + x;
    result = 31 * result + y;
    result = 31 * result + z;
    return result;
}

toString()
```

默认返回 ToStringExample@4554617c 这种形式，其中 @ 后面的数值为散列码的无符号十六进制表示。


```
public class ToStringExample {
    private int number;

    public ToStringExample(int number) {
        this.number = number;
    }
}
```

ToStringExample example = new ToStringExample(123);
System.out.println(example.toString());
ToStringExample@4554617c 

## 21 clone()
### 21.1 cloneable
clone() 是 Object 的 protect 方法，它不是 public，一个类不显式去重写 clone()，其它类就不能直接去调用该类实例的 clone() 方法。

```
public class CloneExample {
    private int a;
    private int b;
}
CloneExample e1 = new CloneExample();
// CloneExample e2 = e1.clone(); // 'clone()' has protected access in 'java.lang.Object'
```

重写 clone() 得到以下实现：

```
public class CloneExample {
    private int a;
    private int b;

    @Override
    protected CloneExample clone() throws CloneNotSupportedException {
        return (CloneExample)super.clone();
    }
}
CloneExample e1 = new CloneExample();
try {
    CloneExample e2 = e1.clone();
} catch (CloneNotSupportedException e) {
    e.printStackTrace();
}
java.lang.CloneNotSupportedException: CloneTest
```

以上抛出了 CloneNotSupportedException，这是因为 CloneTest 没有实现 Cloneable 接口。

```
public class CloneExample implements Cloneable {
    private int a;
    private int b;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
```

应该注意的是，clone() 方法并不是 Cloneable 接口的方法，而是 Object 的一个 protected 方法。Cloneable 接口只是规定，如果一个类没有实现 Cloneable 接口又调用了 clone() 方法，就会抛出 CloneNotSupportedException。


## 22 接口和抽象类的区别是什么
* 接口的方法默认是 public，所有方法在接口中不能有实现(**Java 8 开始接口方法可以有默认实现**），抽象类可以有非抽象的方法
* 接口中的实例变量默认是 final 类型的，而抽象类中则不一定
* 一个类可以实现多个接口，但最多只能实现一个抽象类
* 一个类实现接口的话要实现接口的所有方法，而抽象类不一定
* 接口不能用 new 实例化，但可以声明，但是必须引用一个实现该接口的对象 从* 设计层面来说，抽象是对类的抽象，是一种模板设计，接口是行为的抽象，是一种行为的规范。

抽象类：
>1、抽象类不能被实例化，实例化的工作应该交由它的子类来完成，它只需要有一个引用即可。
>2、抽象方法必须由子类来进行重写。
>3、只要包含一个抽象方法的抽象类，该方法必须要定义成抽象类，不管是否还包含有其他方法。
>4、抽象类中可以包含具体的方法，当然也可以不包含抽象方法。
>5、子类中的抽象方法不能与父类的抽象方法同名。
>6、abstract不能与final并列修饰同一个类。
>7、abstract 不能与private、static、final或native并列修饰同一个方法。

接口：
>1、个Interface的方所有法访问权限自动被声明为public。确切的说只能为public，当然你可以显示的声明为protected、private，但是编译会出错！
>2、接口中可以定义“成员变量”，或者说是不可变的常量，因为接口中的“成员变量”会自动变为为public static final。可以通过类命名直接访问：ImplementClass.name。
>3、接口中不存在实现的方法。
>4、实现接口的非抽象类必须要实现该接口的所有方法。抽象类可以不用实现。
>5、不能使用new操作符实例化一个接口，但可以声明一个接口变量，该变量必须引用（refer to)一个实现该接口的类的对象。可以使用 instanceof 检查一个对象是否实现了某个特定的接口。例如：if(anObject instanceof Comparable){}。
>6、在实现多接口的时候一定要避免方法名的重复。


| 参数  | 抽象类 | 接口|
| ---  |  --- |  ---|
| 默认实现 |	它可以有默认的方法实现	|接口完全是抽象的。它根本不存在方法的实现| 
|实现|子类使用extends关键字来继承抽象类。如果子类不是抽象类的话，它需要提供抽象类中所有声明的方法的实现。	|子类使用关键字implements来实现接口。它需要提供接口中所有声明的方法的实现|
|构造器	|抽象类可以有构造器	|接口不能有构造器|
|与正常Java类的区别	|除了你不能实例化抽象类之外，它和普通Java类没有任何区别	|接口是完全不同的类型
|访问修饰符|	抽象方法可以有public、protected和default这些修饰符	接口方法默认修饰符是public。|你不可以使用其它修饰符。
|main方法|	抽象方法可以有main方法并且我们可以运行它|	接口没有main方法，因此我们不能运行它。
|多继承|	抽象方法可以继承一个类和实现多个接口	|接口只可以继承一个或多个其它接口
|速度|	它比接口速度要快	|接口是稍微有点慢的，因为它需要时间去寻找在类中实现的方法。
|添加新方法|	如果你往抽象类中添加新的方法，你可以给它提供默认的实现。因此你不需要改变你现在的代码。|	如果你往接口中添加方法，那么你必须改变实现该接口的类。

## 23 获取用键盘输入常用的的两种方法
方法1：通过 Scanner

```
Scanner input = new Scanner(System.in);
String s  = input.nextLine();
input.close();
```

方法2：通过 BufferedReader

```
BufferedReader input = new BufferedReader(new InputStreamReader(System.in)); 
String s = input.readLine(); 
```


## 24 Java 中的异常处理

在 Java 中，所有的异常都有一个共同的祖先java.lang包中的 Throwable类。Throwable： 有两个重要的子类：Exception（异常） 和 Error（错误） ，二者都是 Java 异常处理的重要子类，各自都包含大量子类。
* Error（错误）:是程序无法处理的错误，表示运行应用程序中较严重问题。大多数错误与代码编写者执行的操作无关，而表示代码运行时 JVM（Java 虚拟机）出现的问题。例如，Java虚拟机运行错误（Virtual MachineError），当 JVM 不再有继续执行操作所需的内存资源时，将出现 OutOfMemoryError。这些异常发生时，Java虚拟机（JVM）一般会选择线程终止。
这些错误表示故障发生于虚拟机自身、或者发生在虚拟机试图执行应用时，如Java虚拟机运行错误（Virtual MachineError）、类定义错误（NoClassDefFoundError）等。这些错误是不可查的，因为它们在应用程序的控制和处理能力之 外，而且绝大多数是程序运行时不允许出现的状况。对于设计合理的应用程序来说，即使确实发生了错误，本质上也不应该试图去处理它所引起的异常状况。在 Java中，错误通过Error的子类描述。
* Exception（异常）:是程序本身可以处理的异常。Exception 类有一个重要的子类 RuntimeException。RuntimeException 异常由Java虚拟机抛出。NullPointerException（要访问的变量没有引用任何对象时，抛出该异常）、ArithmeticException（算术运算异常，一个整数除以0时，抛出该异常）和 ArrayIndexOutOfBoundsException （下标越界异常）。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/exception_and_error.png)
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/exception_and_error.png)
* 注意：异常和错误的区别：**异常能被程序本身可以处理，错误是无法处理。**
Throwable类常用方法
public string getMessage():返回异常发生时的详细信息
public string toString():返回异常发生时的简要描述
public string getLocalizedMessage():返回异常对象的本地化信息。使用Throwable的子类覆盖这个方法，可以声称本地化信息。如果子类没有覆盖该方法，则该方法返回的信息与getMessage（）返回的结果相同
public void printStackTrace():在控制台上打印Throwable对象封装的异常信息
异常处理总结
**try 块：**用于捕获异常。其后可接零个或多个catch块，如果没有catch块，则必须跟一个finally块。
**catch 块：**用于处理try捕获到的异常。
**finally 块：**无论是否捕获或处理异常，finally块里的语句都会被执行。当在try块或catch块中遇到return语句时，finally语句块将在方法返回之前被执行。
在以下4种特殊情况下，finally块不会被执行：
在finally语句块第一行发生了异常。 因为在其他行，finally块还是会得到执行
在前面的代码中用了System.exit(int)已退出程序。 exit是带参函数 ；若该语句在异常语句之后，finally会执行
程序所在的线程死亡。
关闭CPU。

关于返回值：
 **try语句在返回前，将其他所有的操作执行完，保留好要返回的值，而后转入执行finally中的语句，而后分为以下三种情况：**
 
**情况一：如果finally中有return语句，则会将try中的return语句”覆盖“掉，直接执行finally中的return语句，得到返回值，这样便无法得到try之前保留好的返回值。**

**情况二：如果finally中没有return语句，也没有改变要返回值，则执行完finally中的语句后，会接着执行try中的return语句，返回之前保留的值。**

**情况三：如果finally中没有return语句，但是改变了要返回的值，这里有点类似与引用传递和值传递的区别，分以下两种情况，：**
* 1）如果return的数据是基本数据类型或文本字符串，则在finally中对该基本数据的改变不起作用，try中的return语句依然会返回进入finally块之前保留的值。

* 2）如果return的数据是引用数据类型，而在finally中对该引用数据类型的属性值的改变起作用，try中的return语句返回的就是在finally中改变后的该属性的值。
参考：
[Java中try catch finally语句中含有return语句的执行情况（总结版）](https://blog.csdn.net/ns_code/article/details/17485221)

---
华丽的分割线

---
#JAVA集合

------
## 一、概述
　　Java集合框架提供了数据持有对象的方式，提供了对数据集合的操作。Java 集合框架位于 java.util 包下，主要有三个大类：Collection(接口)、Map(接口)、集合工具类。
集合框架图

- Collection
   - ArrayList：线程不同步。默认初始容量为 10，当数组大小不足时容量扩大为 1.5 倍。为追求效率，ArrayList 没有实现同步（synchronized），如果需要多个线程并发访问，用户可以手动同步，也可使用 Vector 替代。
   
   - LinkedList：线程不同步。双向链接实现。LinkedList 同时实现了 List 接口和 Deque 接口，也就是说它既可以看作一个顺序容器，又可以看作一个队列（Queue），同时又可以看作一个栈（Stack）。这样看来，LinkedList 简直就是个全能冠军。当你需要使用栈或者队列时，可以考虑使用 LinkedList，一方面是因为 Java 官方已经声明不建议使用 Stack 类，更遗憾的是，Java 里根本没有一个叫做 Queue 的类（它是个接口名字）。关于栈或队列，现在的首选是 ArrayDeque，它有着比 LinkedList（当作栈或队列使用时）有着更好的性能。
Stack and Queue：Java 里有一个叫做 Stack 的类，却没有叫做 Queue 的类（它是个接口名字）。当需要使用栈时，Java 已不推荐使用 Stack，而是推荐使用更高效的 ArrayDeque；既然 Queue 只是一个接口，当需要使用队列时也就首选 ArrayDeque 了（次选是 LinkedList ）。

   - Vector：线程同步。默认初始容量为 10，当数组大小不足时容量扩大为 2 倍。它的同步是通过 Iterator 方法加 synchronized 实现的。

   - Stack：线程同步。继承自 Vector，添加了几个方法来完成栈的功能。现在已经不推荐使用 Stack，在栈和队列中有限使用 ArrayDeque，其次是 LinkedList。

   - TreeSet：线程不同步，内部使用 NavigableMap 操作。默认元素 “自然顺序” 排列，可以通过 Comparator 改变排序。TreeSet 里面有一个 TreeMap（适配器模式）

  - HashSet：线程不同步，内部使用 HashMap 进行数据存储，提供的方法基本都是调用 HashMap 的方法，所以两者本质是一样的。集合元素可以为 NULL。

   - Set：Set 是一种不包含重复元素的 Collection，Set 最多只有一个 null 元素。Set 集合通常可以通过 Map 集合通过适配器模式得到。

   - PriorityQueue：Java 中 PriorityQueue 实现了 Queue 接口，不允许放入 null 元素；其通过堆实现，具体说是通过完全二叉树（complete binary tree）实现的小顶堆（任意一个非叶子节点的权值，都不大于其左右子节点的权值），也就意味着可以通过数组来作为 PriorityQueue 的底层实现。
优先队列的作用是能保证每次取出的元素都是队列中权值最小的（Java 的优先队列每次取最小元素，C++ 的优先队列每次取最大元素）。这里牵涉到了大小关系，元素大小的评判可以通过元素本身的自然顺序（natural ordering），也可以通过构造时传入的比较器（Comparator，类似于 C++ 的仿函数）。

   - NavigableSet：添加了搜索功能，可以对给定元素进行搜索：小于、小于等于、大于、大于等于，放回一个符合条件的最接近给定元素的 key。
EnumSet：线程不同步。内部使用 Enum 数组实现，速度比 HashSet 快。只能存储在构造函数传入的枚举类的枚举值。

---
* Map
   - TreeMap：线程不同步，基于 红黑树 （Red-Black tree）的 NavigableMap 实现，能够把它保存的记录根据键排序，默认是按键值的升序排序，也可以指定排序的比较器，当用 Iterator 遍历 TreeMap 时，得到的记录是排过序的。
TreeMap 底层通过红黑树（Red-Black tree）实现，也就意味着 containsKey(), get(), put(), remove() 都有着 log(n) 的时间复杂度。其具体算法实现参照了《算法导论》。

   - HashTable：线程安全，HashMap 的迭代器 (Iterator) 是 fail-fast 迭代器。HashTable 不能存储 NULL 的 key 和 value。
HashMap：线程不同步。根据 key 的 hashcode 进行存储，内部使用静态内部类 Node 的数组进行存储，默认初始大小为 16，每次扩大一倍。当发生 Hash 冲突时，采用拉链法（链表）。JDK 1.8中：当单个桶中元素个数大于等于8时，链表实现改为红黑树实现；当元素个数小于6时，变回链表实现。由此来防止hashCode攻击。
Java HashMap 采用的是冲突链表方式。
HashMap 是 Hashtable 的轻量级实现，可以接受为 null 的键值 (key) 和值 (value)，而 Hashtable 不允许。
	- LinkedHashMap：保存了记录的插入顺序，在用 Iterator 遍历 LinkedHashMap 时，先得到的记录肯定是先插入的。也可以在构造时用带参数，按照应用次数排序。在遍历的时候会比 HashMap 慢，不过有种情况例外，当 HashMap 容量很大，实际数据较少时，遍历起来可能会比 LinkedHashMap 慢，因为 LinkedHashMap 的遍历速度只和实际数据有关，和容量无关，而 HashMap 的遍历速度和他的容量有关。
	- WeakHashMap：从名字可以看出它是某种 Map。它的特殊之处在于 WeakHashMap 里的 entry 可能会被 GC 自动删除，即使程序员没有调用 remove() 或者 clear() 方法。 WeakHashMap 的存储结构类似于HashMap
既然有 WeekHashMap，是否有 WeekHashSet 呢？答案是没有！不过 Java Collections 工具类给出了解决方案，Collections.newSetFromMap(Map<E,Boolean> map) 方法可以将任何 Map包装成一个Set。

---
* 工具类
	- Collections、Arrays：集合类的一个工具类帮助类，其中提供了一系列静态方法，用于对集合中元素进行排序、搜索以及线程安全等各种操作。
	- Comparable、Comparator：一般是用于对象的比较来实现排序，两者略有区别。
类设计者没有考虑到比较问题而没有实现 Comparable 接口。这是我们就可以通过使用 Comparator，这种情况下，我们是不需要改变对象的。
一个集合中，我们可能需要有多重的排序标准，这时候如果使用 Comparable 就有些捉襟见肘了，可以自己继承 Comparator 提供多种标准的比较器进行排序。
说明：线程不同步的时候可以通过，Collections.synchronizedList() 方法来包装一个线程同步方法

---
## 二、深入源码分析

### 2.1 ArrayList

#### 2.1.1 概览
实现了 RandomAccess 接口，因此支持随机访问，这是理所当然的，因为 ArrayList 是基于数组实现的。

```
public class ArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
```
数组的默认大小为 10。
```
private static final int DEFAULT_CAPACITY = 10;
```
#### 2.1.2 序列化
基于数组实现，保存元素的数组使用 transient 修饰，该关键字声明数组默认不会被序列化。ArrayList 具有动态扩容特性，因此保存元素的数组不一定都会被使用，那么就没必要全部进行序列化。ArrayList 重写了 writeObject() 和 readObject() 来控制只序列化数组中有元素填充那部分内容。
```
transient Object[] elementData; // non-private to simplify nested class access
```

#### 2.1.3 扩容
添加元素时使用 ensureCapacityInternal() 方法来保证容量足够，如果不够时，需要使用 grow() 方法进行扩容，新容量的大小为 oldCapacity + (oldCapacity >> 1)，也就是旧容量的 1.5 倍。**扩容操作需要调用 Arrays.copyOf() 把原数组整个复制到新数组中，这个操作代价很高，因此最好在创建 ArrayList 对象时就指定大概的容量大小，减少扩容操作的次数。**
// JDK 1.8
    
```
public boolean add(E e) {
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```

// 判断数组是否越界

```
private void ensureCapacityInternal(int minCapacity) {
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
    ensureExplicitCapacity(minCapacity);
}

private void ensureExplicitCapacity(int minCapacity) {
    modCount++;
    // overflow-conscious code
    if (minCapacity - elementData.length > 0)
        grow(minCapacity);
}
// 扩容
private void grow(int minCapacity) {
    // overflow-conscious code
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1); // 1.5倍
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}

private static int hugeCapacity(int minCapacity) {
    if (minCapacity < 0) // overflow
        throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
}
```

#### 2.1.4 删除元素
需要调用 System.arraycopy() 将 index+1 后面的元素都复制到 index 位置上。
```
public E remove(int index) {
    rangeCheck(index);
    modCount++;
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        System.arraycopy(elementData, index+1, elementData, index, numMoved);
    elementData[--size] = null; // clear to let GC do its work
    return oldValue;
}
```

#### 2.1.5 Fail-Fast
什么是 fail-fast 机制?
fail-fast 机制在遍历一个集合时，当集合结构被修改，会抛出 Concurrent Modification Exception。
fail-fast 会在以下两种情况下抛出 Concurrent Modification Exception
（1）单线程环境
集合被创建后，在遍历它的过程中修改了结构。
注意 remove() 方法会让 **expectModcount 和 modcount** 相等，所以是不会抛出这个异常。
（2）多线程环境
当一个线程在遍历这个集合，而另一个线程对这个集合的结构进行了修改。
在每次做add remove 。。。操作的时候 。 **modcount 记录修改次数** ，当构造一个迭代器取遍历这个集合的时候，每取一个值会先判断modCoumt和expectCount的值是否相等
modCount 用来记录 ArrayList 结构发生变化的次数。结构发生变化是指添加或者删除至少一个元素的所有操作，或者是调整内部数组的大小，仅仅只是设置元素的值不算结构发生变化。
在进行序列化或者迭代等操作时，需要比较操作前后 modCount 是否改变，如果改变了需要抛出 Concurrent Modification Exception。

```
private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
    // Write out element count, and any hidden stuff
    int expectedModCount = modCount;
    s.defaultWriteObject();

    // Write out size as capacity for behavioural compatibility with clone()
    s.writeInt(size);

    // Write out all elements in the proper order.
    for (int i=0; i<size; i++) {
        s.writeObject(elementData[i]);
    }

    if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
    }
}
```

### 2.2 Vector
#### 2.2.1 同步
它的实现与 ArrayList 类似，但是使用了 synchronized 进行同步。

```
public synchronized boolean add(E e) {
    modCount++;
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
}

public synchronized E get(int index) {
    if (index >= elementCount)
        throw new ArrayIndexOutOfBoundsException(index);
    return elementData(index);
}
```

#### 2.2.2 ArrayList 与 Vector
Vector 是同步的，因此开销就比 ArrayList 要大，访问速度更慢。最好使用 ArrayList 而不是 Vector，因为同步操作完全可以由程序员自己来控制；
Vector 每次扩容请求其大小的 2 倍空间，而 ArrayList 是 1.5 倍。

#### 2.2.3 Vector 替代方案
synchronizedList
为了获得线程安全的 ArrayList，可以使用 Collections.synchronizedList(); 得到一个线程安全的 ArrayList。

```
List<String> list = new ArrayList<>();
List<String> synList = Collections.synchronizedList(list);
```

* CopyOnWriteArrayList


也可以使用 concurrent 并发包下的 CopyOnWriteArrayList 类。

```
List<String> list = new CopyOnWriteArrayList<>();
```

CopyOnWrite 容器即写时复制的容器。**通俗的理解是当我们往一个容器添加元素的时候，不直接往当前容器添加，而是先将当前容器进行 Copy，复制出一个新的容器，然后新的容器里添加元素，添加完元素之后，再将原容器的引用指向新的容器。**这样做的好处是我们可以对 CopyOnWrite 容器进行并发的读，而不需要加锁，因为当前容器不会添加任何元素。所以 CopyOnWrite 容器也是一种读写分离的思想，读和写不同的容器。
```
public boolean add(T e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        // 复制出新数组
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        // 把新元素添加到新数组里
        newElements[len] = e;
        // 把原数组引用指向新数组
        setArray(newElements);
        return true;
    } finally {
        lock.unlock();
    }
}

final void setArray(Object[] a) {
    array = a;
}
```
读的时候不需要加锁，如果读的时候有多个线程正在向 ArrayList 添加数据，读还是会读到旧的数据，因为写的时候不会锁住旧的 ArrayList。

```
public E get(int index) {
    return get(getArray(), index);
}
```

CopyOnWrite的缺点
CopyOnWrite 容器有很多优点，但是同时也存在两个问题，即内存占用问题和数据一致性问题。所以在开发的时候需要注意一下。
内存占用问题。
因为 CopyOnWrite 的写时复制机制，所以在进行写操作的时候，**内存里会同时驻扎两个对象的内存**，旧的对象和新写入的对象（注意：在复制的时候只是复制容器里的引用，只是在写的时候会创建新对象添加到新容器里，而旧容器的对象还在使用，所以有两份对象内存）。如果这些对象占用的内存比较大，比如说 200M 左右，那么再写入 100M 数据进去，内存就会占用 300M，那么这个时候很有可能造成频繁的 Yong GC 和 Full GC。之前我们系统中使用了一个服务由于每晚使用 CopyOnWrite 机制更新大对象，造成了每晚 15 秒的 Full GC，应用响应时间也随之变长。
针对内存占用问题，可以通过压缩容器中的元素的方法来减少大对象的内存消耗，比如，如果元素全是 10 进制的数字，可以考虑把它压缩成 36 进制或 64 进制。或者不使用 CopyOnWrite 容器，而使用其他的并发容器，如 ConcurrentHashMap 。

**数据一致性问题。**
CopyOnWrite 容器只能保证数据的最终一致性，不能保证数据的实时一致性。所以如果你希望写入的的数据，马上能读到，请不要使用 CopyOnWrite 容器。

### 2.3 LinkedList
#### 2.3.1 概览
LinkedList 底层是基于双向链表实现的，也是实现了 List 接口，所以也拥有 List 的一些特点 (JDK1.7/8 之后取消了循环，修改为双向链表) 。
LinkedList 同时实现了 List 接口和 Deque 接口，也就是说它既可以看作一个顺序容器，又可以看作一个队列（Queue），同时又可以看作一个栈（Stack）。这样看来， LinkedList 简直就是个全能冠军。当你需要使用栈或者队列时，可以考虑使用 LinkedList ，一方面是因为 Java 官方已经声明不建议使用 Stack 类，更遗憾的是，Java里根本没有一个叫做 Queue 的类（它是个接口名字）。
关于栈或队列，现在的首选是 ArrayDeque，它有着比 LinkedList （当作栈或队列使用时）有着更好的性能。
基于双向链表实现，内部使用 Node 来存储链表节点信息。

```
private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;
}
```

每个链表存储了 Head 和 Tail 指针：

```
transient Node<E> first;
transient Node<E> last;
```
LinkedList 的实现方式决定了所有跟下标相关的操作都是线性时间，而在首段或者末尾删除元素只需要常数时间。为追求效率LinkedList没有实现同步（synchronized），如果需要多个线程并发访问，可以先采用 Collections.synchronizedList() 方法对其进行包装。

#### 2.3.2 add()
add() 方法有两个版本，一个是 add(E e)，该方法在 LinkedList 的末尾插入元素，因为有 last 指向链表末尾，在末尾插入元素的花费是常数时间。只需要简单修改几个相关引用即可；另一个是 add(int index, E element)，该方法是在指定下表处插入元素，需要先通过线性查找找到具体位置，然后修改相关引用完成插入操作。
// JDK 1.8
```
public boolean add(E e) {
    linkLast(e);
    return true;
}


/**
* Links e as last element.
*/


void linkLast(E e) {
    final Node<E> l = last;
    final Node<E> newNode = new Node<>(l, e, null);
    last = newNode;
    if (l == null)
        first = newNode;
    else
        l.next = newNode;
    size++;
    modCount++;
}
```

add(int index, E element) 的逻辑稍显复杂，可以分成两部分
先根据 index 找到要插入的位置；
修改引用，完成插入操作。

```
public void add(int index, E element) {
    checkPositionIndex(index);
    if (index == size)
        linkLast(element);
    else
        linkBefore(element, node(index));
}

private void checkPositionIndex(int index) {
    if (!isPositionIndex(index))
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

上面代码中的 node(int index) 函数有一点小小的 trick，因为链表双向的，可以从开始往后找，也可以从结尾往前找，具体朝那个方向找取决于条件 index < (size >> 1)，也即是 index 是靠近前端还是后端。
#### 2.3.3 remove()
remove() 方法也有两个版本，一个是删除跟指定元素相等的第一个元素 remove(Object o)，另一个是删除指定下标处的元素 remove(int index)。

两个删除操作都要：
先找到要删除元素的引用；
修改相关引用，完成删除操作。
在寻找被删元素引用的时候 remove(Object o) 调用的是元素的 equals 方法，而 remove(int index) 使用的是下标计数，两种方式都是线性时间复杂度。在步骤 2 中，两个 revome() 方法都是通过 unlink(Node<E> x) 方法完成的。这里需要考虑删除元素是第一个或者最后一个时的边界情况。
#### 2.3.4 get()

```
public E get(int index) {
	checkElementIndex(index);
	return node(index).item;
}
    
Node<E> node(int index) {
	// assert isElementIndex(index);
    if (index < (size >> 1)) {
        Node<E> x = first;
        for (int i = 0; i < index; i++)
            x = x.next;
        return x;
	} else {
        Node<E> x = last;
        for (int i = size - 1; i > index; i--)
            x = x.prev;
        return x;
	}
}
```

由此可以看出是使用二分查找来看 index 离 size 中间距离来判断是从头结点正序查还是从尾节点倒序查。
node() 会以 O(n/2) 的性能去获取一个结点
如果索引值大于链表大小的一半，那么将从尾结点开始遍历
这样的效率是非常低的，特别是当 index 越接近 size 的中间值时。

#### 2.3.5 总结
LinkedList 插入，删除都是移动指针效率很高。
查找需要进行遍历查询，效率较低。

#### 2.3.6 ArrayList 与 LinkedList
ArrayList 基于动态数组实现，LinkedList 基于双向链表实现；
ArrayList 支持随机访问，LinkedList 不支持；
LinkedList 在任意位置添加删除元素更快。


### 2.4 HashMap

面试官常问的几个问题去看源码：
了解底层如何存储数据的
**HashMap 的几个主要方法**
**HashMap 是如何确定元素存储位置的以及如何处理哈希冲突的**
**==HashMap 扩容机制是怎样的==**
**JDK 1.8 在扩容和解决哈希冲突上对 HashMap 源码做了哪些改动？有什么好处?**
**HashMap 的内部功能实现很多，主要从根据 key 获取哈希桶数组索引位置、put 方法的详细执行、扩容过程三个具有代表性的点深入展开讲解。**


#### 2.4.1 存储结构
JDK1.7 的存储结构
在 1.7 之前 JDK 采用「**拉链法**」来存储数据，即**数组和链表结合**的方式：

「拉链法」用专业点的名词来说叫做链地址法。简单来说，就是数组加链表的结合。在每个数组元素上存储的都是一个链表。
我们之前说到不同的 key 可能经过 hash 运算可能会得到相同的地址，但是一个数组单位上只能存放一个元素，采用链地址法以后，如果遇到相同的 hash 值的 key 的时候，我们可以将它放到作为数组元素的链表上。待我们去取元素的时候通过 hash 运算的结果找到这个链表，再在链表中找到与 key 相同的节点，就能找到 key 相应的值了。
JDK1.7 中新添加进来的元素总是放在数组相应的角标位置，而原来处于该角标的位置的节点作为 next 节点放到新节点的后边。
JDK1.8 的存储结构
对于 JDK1.8 之后的 HashMap 底层在解决哈希冲突的时候，就不单单是使用数组加上单链表的组合了，因为当处理如果 hash 值冲突较多的情况下，链表的长度就会越来越长，此时通过单链表来寻找对应 Key 对应的 Value 的时候就会使得时间复杂度达到 O(n)，因此在 JDK1.8 之后，在链表新增节点导致链表长度超过 TREEIFY_THRESHOLD = 8 的时候，就会在添加元素的同时将原来的单链表转化为红黑树。
对数据结构很在行的读者应该，知道红黑树是一种易于增删改查的二叉树，他对与数据的查询的时间复杂度是 O(logn) 级别，所以利用红黑树的特点就可以更高效的对 HashMap 中的元素进行操作。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashmap-rb-link.jpg)

从结构实现来讲，HashMap 是数组+链表+红黑树（JDK1.8增加了红黑树部分）实现的，如下如所示。

![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashMap-datastruct.png)

这里需要讲明白两个问题：数据底层具体存储的是什么？这样的存储方式有什么优点呢？
（1）从源码可知，HashMap 类中有一个非常重要的字段，就是 Node[] table，即哈希桶数组，明显它是一个 Node 的数组。我们来看 Node（ JDK1.8 中） 是何物。

```
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;    //用来定位数组索引位置
    final K key;
    V value;
    Node<K,V> next;   //链表的下一个node

    Node(int hash, K key, V value, Node<K,V> next) { ... }
    public final K getKey(){ ... }
    public final V getValue() { ... }
    public final String toString() { ... }
    public final int hashCode() { ... }
    public final V setValue(V newValue) { ... }
    public final boolean equals(Object o) { ... }
}
```

Node 是 HashMap 的一个内部类，实现了 Map.Entry 接口，本质是就是一个映射（键值对）。上图中的每个黑色圆点就是一个Node对象。

（2）HashMap 就是使用哈希表来存储的。哈希表为解决冲突，可以采用开放地址法和链地址法等来解决问题， Java 中 HashMap 采用了链地址法。链地址法，简单来说，就是数组加链表的结合。在每个数组元素上都一个链表结构，当数据被 Hash 后，得到数组下标，把数据放在对应下标元素的链表上。例如程序执行下面代码：

```
map.put("美团","小美");
```

系统将调用 "美团" 这个 key 的 hashCode() 方法得到其 hashCode 值（该方法适用于每个 Java 对象），然后再通过 Hash 算法的后两步运算（高位运算和取模运算，下文有介绍）来定位该键值对的存储位置，有时两个 key 会定位到相同的位置，表示发生了 Hash 碰撞。当然 Hash 算法计算结果越分散均匀，Hash 碰撞的概率就越小，map 的存取效率就会越高。
如果哈希桶数组很大，即使较差的 Hash 算法也会比较分散，如果哈希桶数组数组很小，即使好的 Hash 算法也会出现较多碰撞，所以就需要在空间成本和时间成本之间权衡，其实就是在根据实际情况确定哈希桶数组的大小，并在此基础上设计好的 hash 算法减少 Hash 碰撞。
那么通过什么方式来控制 map 使得 Hash 碰撞的概率又小，哈希桶数组（Node[] table）占用空间又少呢？
答案就是好的 Hash 算法和扩容机制。
在理解 Hash 和扩容流程之前，我们得先了解下 HashMap 的几个字段。从 HashMap 的默认构造函数源码可知，构造函数就是对下面几个字段进行初始化，源码如下：

```
int threshold;             // 所能容纳的key-value对极限 
final float loadFactor;    // 负载因子
int modCount;  
int size;
```

首先，Node[] table的初始化长度 length (默认值是16)，Load factor 为负载因子(默认值是0.75)，threshold 是 HashMap 所能容纳的最大数据量的 Node (键值对)个数。threshold = length * Load factor。也就是说，在数组定义好长度之后，负载因子越大，所能容纳的键值对个数越多。

结合负载因子的定义公式可知，threshold 就是在此 Load factor 和 length (数组长度)对应下允许的最大元素数目，超过这个数目就重新 resize(扩容)，**扩容后的 HashMap 容量是之前容量的两倍**。默认的负载因子 0.75 是对空间和时间效率的一个平衡选择，建议大家不要修改，除非在时间和空间比较特殊的情况下，如果内存空间很多而又对时间效率要求很高，可以降低负载因子 Load factor 的值；相反，如果内存空间紧张而对时间效率要求不高，可以增加负载因子 loadFactor 的值，这个值可以大于1。
size 这个字段其实很好理解，就是 HashMap 中实际存在的键值对数量。注意和 table 的长度 length、容纳最大键值对数量 threshold 的区别。而 **modCount 字段主要用来记录 HashMap 内部结构发生变化的次数**，主要用于迭代的快速失败。强调一点，内部结构发生变化指的是结构发生变化，例如 put 新键值对，但是某个 key 对应的 value 值被覆盖不属于结构变化。
在 HashMap 中，哈希桶数组 table 的长度 length 大小必须为 2n（一定是合数），这是一种非 常规的设计，常规的设计是把桶的大小设计为素数。**相对来说素数导致冲突的概率要小于合数**，具体证明可以参考 [为什么一般hashtable的桶数会取一个素数](https://blog.csdn.net/liuqiyao_01/article/details/14475159)？ ，Hashtable 初始化桶大小为 11，就是桶大小设计为素数的应用（Hashtable 扩容后不能保证还是素数），**hashtable每次扩容为2n+1**。HashMap 采用这种非常规设计，主要是为了在取模和扩容时做优化，同时为了减少冲突，HashMap 定位哈希桶索引位置时，也加入了高位参与运算的过程。
在取模的过程中 使用 （length - 1)&hash  == hash%length 
扩容的高位运算  jdk1.8扩容优化有解释
这里存在一个问题，即使负载因子和 Hash 算法设计的再合理，也免不了会出现拉链过长的情况，一旦出现拉链过长，则会严重影响 HashMap 的性能。于是，在 JDK1.8 版本中，对数据结构做了进一步的优化，引入了红黑树。**而当链表 长度太长（默认超过8）时，链表就转换为红黑树，利用红黑树快速增删改查的特点提高 HashMap 的性能，其中会用到红黑树的插入、删除、查找等算法。**参考：[教你初步了解红黑树。](https://blog.csdn.net/v_july_v/article/details/6105630)

#### 2.4.2 重要参数
参数 说明
buckets 在 HashMap 的注释里使用哈希桶来形象的表示数组中每个地址位置。注意这里并不是数组本身，数组是装哈希桶的，他可以被称为哈希表。
capacity table 的容量大小，默认为 16。需要注意的是 capacity 必须保证为 2 的 n 次方。
size table 的实际使用量。
threshold size 的临界值，size 必须小于 threshold，如果大于等于，就必须进行扩容操作。
loadFactor 装载因子，table 能够使用的比例，threshold = capacity * loadFactor。
TREEIFY_THRESHOLD 树化阀值，哈希桶中的节点个数大于该值（默认为8）的时候将会被转为红黑树行存储结构。
UNTREEIFY_THRESHOLD 非树化阀值，小于该值（默认为 6）的时候将再次改为单链表的格式存储
#### 2.4.3 确定哈希桶数组索引位置
很多操作都需要先确定一个键值对所在的桶下标。
int hash = hash(key);
int i = indexFor(hash, table.length);

（一）计算 hash 值

```
final int hash(Object k) {
    int h = hashSeed;
    if (0 != h && k instanceof String) {
        return sun.misc.Hashing.stringHash32((String) k);
    }

    h ^= k.hashCode();

    // This function ensures that hashCodes that differ only by
    // constant multiples at each bit position have a bounded
    // number of collisions (approximately 8 at default load factor).
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
}
public final int hashCode() {
    return Objects.hashCode(key) ^ Objects.hashCode(value);
}
```

（二）取模
令 x = 1<<4，即 x 为 2 的 4 次方，它具有以下性质：
x   : 00010000
x-1 : 00001111

令一个数 y 与 x-1 做与运算，可以去除 y 位级表示的第 4 位以上数：
y       : 10110010
x-1     : 00001111
y&(x-1) : 00000010

这个性质和 y 对 x 取模效果是一样的：
y   : 10110010
x   : 00010000
y%x : 00000010

位运算的代价比求模运算小的多，因此在进行这种计算时用位运算的话能带来更高的性能。
确定桶下标的最后一步是将 key 的 hash 值对桶个数取模：hash%capacity，如果能保证 capacity 为 2 的 n 次方，那么就可以将这个操作转换为位运算。
static int indexFor(int h, int length) {
    return h & (length-1);
}

#### 2.4.4 分析HashMap的put方法
　　HashMap 的 put 方法执行过程可以通过下图来理解，自己有兴趣可以去对比源码更清楚地研究学习。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashmap-put.png)

①.判断键值对数组 table[i] 是否为空或为 null，否则执行 resize() 进行扩容；
②.根据键值 key 计算 hash 值得到插入的数组索引i，如果 table[i]==null，直接新建节点添加，转向 ⑥，如果table[i] 不为空，转向 ③；
③.判断 table[i] 的首个元素是否和 key 一样，如果相同直接覆盖 value，否则转向 ④，这里的相同指的是 hashCode 以及 equals；
④.判断table[i] 是否为 treeNode，即 table[i] 是否是红黑树，如果是红黑树，则直接在树中插入键值对，否则转向 ⑤；
⑤.遍历 table[i]，判断链表长度是否大于 8，大于 8 的话把链表转换为红黑树，在红黑树中执行插入操作，否则进行链表的插入操作；遍历过程中若发现 key 已经存在直接覆盖 value 即可；
⑥.插入成功后，判断实际存在的键值对数量 size 是否超多了最大容量 threshold，如果超过，进行扩容。
JDK1.8 HashMap 的 put 方法源码如下:

```
public V put(K key, V value) {
    // 对key的hashCode()做hash
    return putVal(hash(key), key, value, false, true);
}

final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 步骤①：tab为空则创建
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 步骤②：计算index，并对null做处理 
    if ((p = tab[i = (n - 1) & hash]) == null) 
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        // 步骤③：节点key存在，直接覆盖value
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 步骤④：判断该链为红黑树
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        // 步骤⑤：该链为链表
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key,value,null);
                     //链表长度大于8转换为红黑树进行处理
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st  
                        treeifyBin(tab, hash);
                    break;
                }
                 // key已经存在直接覆盖value
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k)))) 
                           break;
                p = e;
            }
        }
        
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    // 步骤⑥：超过最大容量 就扩容
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

#### 2.4.5 扩容机制
扩容 (resize) 就是重新计算容量，向 HashMap 对象里不停的添加元素，而 HashMap 对象内部的数组无法装载更多的元素时，对象就需要扩大数组的长度，以便能装入更多的元素。当然 Java 里的数组是无法自动扩容的，方法是使用一个新的数组代替已有的容量小的数组，就像我们用一个小桶装水，如果想装更多的水，就得换大水桶。
我们分析下 resize 的源码，鉴于 JDK1.8 融入了红黑树，较复杂，为了便于理解我们仍然使用 JDK1.7 的代码，好理解一些，本质上区别不大，具体区别后文再说。

```
void resize(int newCapacity) {   //传入新的容量
    Entry[] oldTable = table;    //引用扩容前的Entry数组
    int oldCapacity = oldTable.length;         
    if (oldCapacity == MAXIMUM_CAPACITY) {  //扩容前的数组大小如果已经达到最大(2^30)了
        threshold = Integer.MAX_VALUE; //修改阈值为int的最大值(2^31-1)，这样以后就不会扩容了
        return;
    }
 
    Entry[] newTable = new Entry[newCapacity];  //初始化一个新的Entry数组
    transfer(newTable);                         //！！将数据转移到新的Entry数组里
    table = newTable;                           //HashMap的table属性引用新的Entry数组
    threshold = (int)(newCapacity * loadFactor);//修改阈值
}
```

这里就是使用一个容量更大的数组来代替已有的容量小的数组，transfer() 方法将原有 Entry 数组的元素拷贝到新的 Entry 数组里。

```
void transfer(Entry[] newTable) {
    Entry[] src = table;                   //src引用了旧的Entry数组
    int newCapacity = newTable.length;
    for (int j = 0; j < src.length; j++) { //遍历旧的Entry数组
        Entry<K,V> e = src[j];             //取得旧Entry数组的每个元素
        if (e != null) {
            src[j] = null;//释放旧Entry数组的对象引用（for循环后，旧的Entry数组不再引用任何对象）
            do {
                Entry<K,V> next = e.next;
                int i = indexFor(e.hash, newCapacity); //！！重新计算每个元素在数组中的位置
                e.next = newTable[i]; //标记[1]
                newTable[i] = e;      //将元素放在数组上
                e = next;             //访问下一个Entry链上的元素
            } while (e != null);
        }
    }
}
```

newTable[i] 的引用赋给了 e.next，也就是使用了单链表的头插入方式，同一位置上新元素总会被放在链表的头部位置；这样先放在一个索引上的元素终会被放到 Entry 链的尾部(如果发生了 hash 冲突的话），这一点和 Jdk1.8 有区别。在旧数组中同一条 Entry 链上的元素，通过重新计算索引位置后，有可能被放到了新数组的不同位置上。
下面举个例子说明下扩容过程。假设了我们的 hash 算法就是简单的用 key mod 一下表的大小（也就是数组的长度）。其中的哈希桶数组 table 的 size=2， 所以 key = 3、7、5，put 顺序依次为 5、7、3。在 mod 2 以后都冲突在 table[1] 这里了。这里假设负载因子 loadFactor=1，即当键值对的实际大小 size 大于 table 的实际大小时进行扩容。接下来的三个步骤是哈希桶数组 resize 成 4，然后所有的 Node 重新 rehash 的过程。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/jdk1.7-resize.png)
由上，可以看出，在多线程情况下为什么会产生环链
首先获取新表的长度，之后遍历新表的每一个entry，然后每个ertry中的链表，以反转的形式，形成rehash之后的链表
并发问题：
若当前线程此时获得ertry节点，但是被线程中断无法继续执行，此时线程二进入transfer函数，并把函数顺利执行，此时新表中的某个位置有了节点，之后线程一获得执行权继续执行，因为并发transfer，所以两者都是扩容的同一个链表，当线程一执行到e.next = new table[i] 的时候，由于线程二之前数据迁移的原因导致此时new table[i] 上就有ertry存在，所以线程一执行的时候，会将next节点，设置为自己，导致自己互相使用next引用对方，因此产生链表，导致死循环。
下面我们讲解下 JDK1.8 做了哪些优化。经过观测可以发现，我们使用的是 2 次幂的扩展 (指长度扩为原来 2 倍)，所以，元素的位置要么是在原位置，要么是在原位置再移动 2 次幂的位置。看下图可以明白这句话的意思，n 为 table 的长度，图（a）表示扩容前的 key1 和 key2 两种 key 确定索引位置的示例，图（b）表示扩容后 key1 和 key2 两种 key 确定索引位置的示例，其中 hash1 是 key1 对应的哈希与高位运算结果。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashMap-1.8-hash1.png)

元素在重新计算 hash 之后，因为 n 变为 2 倍，那么 n-1 的 mask 范围在高位多 1bit (红色)，因此新的 index 就会发生这样的变化：
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashMap-1.8-hash2.png)
因此，我们在扩充 HashMap 的时候，不需要像 JDK1.7 的实现那样重新计算 hash，只需要看看原来的 hash 值新增的那个 bit 是 1 还是 0 就好了，是 0 的话索引没变，是 1 的话索引变成“原索引+oldCap”，可以看看下图为 16 扩充为 32 的 resize 示意图：
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/jdk1.8-resize.png)

这个设计确实非常的巧妙，既省去了重新计算 hash 值的时间，而且同时，由于新增的 1bit 是 0 还是 1 可以认为是随机的，因此 resize 的过程，均匀的把之前的冲突的节点分散到新的 bucket 了。这一块就是 JDK1.8 新增的优化点。有一点注意区别，JDK1.7 中 rehash 的时候，旧链表迁移新链表的时候，如果在新表的数组索引位置相同，则链表元素会倒置，但是从上图可以看出，JDK1.8 不会倒置如下:

```
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        // 超过最大值就不再扩充了，就只好随你碰撞去吧
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 没超过最大值，就扩充为原来的2倍
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    // 计算新的resize上限
    if (newThr == 0) {

        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes"，"unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        // 把每个bucket都移动到新的buckets中
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // 链表优化重hash的代码块
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        // 原索引
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        // 原索引+oldCap
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    // 原索引放到bucket里
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    // 原索引+oldCap放到bucket里
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

#### 2.4.6 线程安全性
在多线程使用场景中，应该尽量避免使用线程不安全的 HashMap，而使用线程安全的 ConcurrentHashMap。那么为什么说 HashMap 是线程不安全的，下面举例子说明在并发的多线程使用场景中使用 HashMap 可能造成死循环。代码例子如下(便于理解，仍然使用 JDK1.7 的环境)：

```
public class HashMapInfiniteLoop {  
    private static HashMap<Integer,String> map = new HashMap<Integer,String>(2，0.75f);  
    public static void main(String[] args) {  
        map.put(5， "C");  

        new Thread("Thread1") {  
            public void run() {  
                map.put(7, "B");  
                System.out.println(map);  
            };  
        }.start();  
        new Thread("Thread2") {  
            public void run() {  
                map.put(3, "A);  
                System.out.println(map);  
            };  
        }.start();        
    }  
}
```

其中，map初始化为一个长度为2的数组，loadFactor=0.75，threshold=2*0.75=1，也就是说当put第二个key的时候，map就需要进行resize。
通过设置断点让线程1和线程2同时debug到transfer方法(3.3小节代码块)的首行。注意此时两个线程已经成功添加数据。放开thread1的断点至transfer方法的“Entry next = e.next;” 这一行；然后放开线程2的的断点，让线程2进行resize。结果如下图。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/jdk1.7-drop-dead-1.png)
注意，Thread1的 e 指向了key(3)，而next指向了key(7)，其在线程二rehash后，指向了线程二重组后的链表。
线程一被调度回来执行，先是执行 newTalbe[i] = e， 然后是e = next，导致了e指向了key(7)，而下一次循环的next = e.next导致了next指向了key(3)。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/jdk1.7-drop-dead-2.png)

e.next = newTable[i] 导致 key(3).next 指向了 key(7)。注意：此时的key(7).next 已经指向了key(3)， 环形链表就这样出现了。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/jdk1.7-drop-dead-5.png)
于是，当我们用线程一调用map.get(11)时，悲剧就出现了——Infinite Loop。
#### 2.4.7 JDK1.8与JDK1.7的性能对比
HashMap中，如果key经过hash算法得出的数组索引位置全部不相同，即Hash算法非常好，那样的话，getKey方法的时间复杂度就是O(1)，如果Hash算法技术的结果碰撞非常多，假如Hash算极其差，所有的Hash算法结果得出的索引位置一样，那样所有的键值对都集中到一个桶中，或者在一个链表中，或者在一个红黑树中，时间复杂度分别为O(n)和O(lgn)。 鉴于JDK1.8做了多方面的优化，总体性能优于JDK1.7，下面我们从两个方面用例子证明这一点。
#### 2.4.8 Hash较均匀的情况
为了便于测试，我们先写一个类Key，如下：

```
class Key implements Comparable<Key> {

    private final int value;

    Key(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(Key o) {
        return Integer.compare(this.value, o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Key key = (Key) o;
        return value == key.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
```

这个类复写了equals方法，并且提供了相当好的hashCode函数，任何一个值的hashCode都不会相同，因为直接使用value当做hashcode。为了避免频繁的GC，我将不变的Key实例缓存了起来，而不是一遍一遍的创建它们。代码如下：

```
public class Keys {

    public static final int MAX_KEY = 10_000_000;
    private static final Key[] KEYS_CACHE = new Key[MAX_KEY];

    static {
        for (int i = 0; i < MAX_KEY; ++i) {
            KEYS_CACHE[i] = new Key(i);
        }
    }

    public static Key of(int value) {
        return KEYS_CACHE[value];
    }
}
```

现在开始我们的试验，测试需要做的仅仅是，创建不同size的HashMap（1、10、100、......10000000），屏蔽了扩容的情况，代码如下：

```
static void test(int mapSize) {

    HashMap<Key, Integer> map = new HashMap<Key,Integer>(mapSize);
    for (int i = 0; i < mapSize; ++i) {
        map.put(Keys.of(i), i);
    }

    long beginTime = System.nanoTime(); //获取纳秒
    for (int i = 0; i < mapSize; i++) {
        map.get(Keys.of(i));
    }
    long endTime = System.nanoTime();
    System.out.println(endTime - beginTime);
}

public static void main(String[] args) {
    for(int i=10;i<= 1000 0000;i*= 10){
        test(i);
    }
}
```

在测试中会查找不同的值，然后度量花费的时间，为了计算getKey的平均时间，我们遍历所有的get方法，计算总的时间，除以key的数量，计算一个平均值，主要用来比较，绝对值可能会受很多环境因素的影响。结果如下：


通过观测测试结果可知，JDK1.8的性能要高于JDK1.7 15%以上，在某些size的区域上，甚至高于100%。由于Hash算法较均匀，JDK1.8引入的红黑树效果不明显，下面我们看看Hash不均匀的的情况。
#### 2.4.9 Hash极不均匀的情况
假设我们又一个非常差的Key，它们所有的实例都返回相同的hashCode值。这是使用HashMap最坏的情况。代码修改如下：

```
class Key implements Comparable<Key> {

    //...

    @Override
    public int hashCode() {
        return 1;
    }
}
```

仍然执行main方法，得出的结果如下表所示：


从表中结果中可知，随着size的变大，JDK1.7的花费时间是增长的趋势，而JDK1.8是明显的降低趋势，并且呈现对数增长稳定。当一个链表太长的时候，HashMap会动态的将它替换成一个红黑树，这话的话会将时间复杂度从O(n)降为O(logn)。hash算法均匀和不均匀所花费的时间明显也不相同，这两种情况的相对比较，可以说明一个好的hash算法的重要性。
测试环境：处理器为2.2 GHz Intel Core i7，内存为16 GB 1600 MHz DDR3，SSD硬盘，使用默认的JVM参数，运行在64位的OS X 10.10.1上。
10. HashMap与HashTable
HashTable 使用 synchronized 来进行同步。
HashMap 可以插入键为 null 的 Entry。
HashMap 的迭代器是 fail-fast 迭代器。
HashMap 不能保证随着时间的推移 Map 中的元素次序是不变的。
11. 小结
扩容是一个特别耗性能的操作，所以当程序员在使用 HashMap 的时候，估算 map 的大小，初始化的时候给一个大致的数值，避免 map 进行频繁的扩容。
负载因子是可以修改的，也可以大于1，但是建议不要轻易修改，除非情况非常特殊。
HashMap 是线程不安全的，不要在并发的环境中同时操作 HashMap，建议使用 

---
* ConcurrentHashMap。
JDK1.8 引入红黑树大程度优化了 HashMap 的性能。

- 1.概述　　
　　众所周知，哈希表是中非常高效，复杂度为 O(1) 的数据结构，在 Java 开发中，我们最常见到最频繁使用的就是 HashMap 和 HashTable，但是在线程竞争激烈的并发场景中使用都不够合理。
　　HashMap ：先说 HashMap，HashMap 是线程不安全的，在并发环境下，可能会形成环状链表（扩容时可能造成），导致 get 操作时，cpu 空转，所以，在并发环境中使 用HashMap 是非常危险的。
　　HashTable ： HashTable 和 HashMap的实现原理几乎一样，差别无非是：（1）HashTable不允许key和value为null；（2）HashTable是线程安全的。
　　但是 HashTable 线程安全的策略实现代价却太大了，简单粗暴，get/put 所有相关操作都是 synchronized 的，这相当于给整个哈希表加了一把大锁，多线程访问时候，只要有一个线程访问或操作该对象，那其他线程只能阻塞，相当于将所有的操作串行化，在竞争激烈的并发场景中性能就会非常差。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashtable-ds.png)
　　HashTable 性能差主要是由于所有操作需要竞争同一把锁，而如果容器中有多把锁，每一把锁锁一段数据，这样在多线程访问时不同段的数据时，就不会存在锁竞争了，这样便可以有效地提高并发效率。这就是ConcurrentHashMap 所采用的 "分段锁" 思想。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashmap-ds.png)

- 2.存储结构
ConcurrentHashMap 采用了非常精妙的"分段锁"策略，ConcurrentHashMap 的主干是个 Segment 数组。

```
 final Segment<K,V>[] segments;
```

　　Segment 继承了 ReentrantLock，所以它就是一种可重入锁（ReentrantLock)。在 ConcurrentHashMap，一个 Segment 就是一个子哈希表，Segment 里维护了一个 HashEntry 数组，并发环境下，对于不同 Segment 的数据进行操作是不用考虑锁竞争的。（就按默认的 ConcurrentLeve 为16来讲，理论上就允许 16 个线程并发执行，有木有很酷）
　　所以，对于同一个 Segment 的操作才需考虑线程同步，不同的 Segment 则无需考虑。
Segment 类似于 HashMap，一个 Segment 维护着一个 HashEntry 数组
transient volatile HashEntry<K,V>[] table;

HashEntry 是目前我们提到的最小的逻辑处理单元了。一个 ConcurrentHashMap 维护一个 Segment 数组，一个 Segment 维护一个 HashEntry 数组。

```
static final class HashEntry<K,V> {
    final int hash;
    final K key;
    volatile V value;
    volatile HashEntry<K,V> next;
}
```

　　ConcurrentHashMap 和 HashMap 实现上类似，最主要的差别是 ConcurrentHashMap 采用了分段锁（Segment），每个分段锁维护着几个桶（HashEntry），多个线程可以同时访问不同分段锁上的桶，从而使其并发度更高（并发度就是 Segment 的个数）。
Segment 继承自 ReentrantLock。

```
static final class Segment<K,V> extends ReentrantLock implements Serializable {

    private static final long serialVersionUID = 2249069246763182397L;

    static final int MAX_SCAN_RETRIES =
        Runtime.getRuntime().availableProcessors() > 1 ? 64 : 1;

    transient volatile HashEntry<K,V>[] table;

    transient int count;

    transient int modCount;

    transient int threshold;

    final float loadFactor;
}
```


```
final Segment<K,V>[] segments;
```

默认的并发级别为 16，也就是说默认创建 16 个 Segment。

```
static final int DEFAULT_CONCURRENCY_LEVEL = 16;
```

2. size 操作
每个 Segment 维护了一个 count 变量来统计该 Segment 中的键值对个数。

```
/**
 * The number of elements. Accessed only either within locks
 * or among other volatile reads that maintain visibility.
 */
transient int count;
```

在执行 size 操作时，需要遍历所有 Segment 然后把 count 累计起来。
ConcurrentHashMap 在执行 size 操作时先尝试不加锁，如果连续两次不加锁操作得到的结果一致，那么可以认为这个结果是正确的。
尝试次数使用 RETRIES_BEFORE_LOCK 定义，该值为 2，retries 初始值为 -1，因此尝试次数为 3。
如果尝试的次数超过 3 次，就需要对每个 Segment 加锁。

```
/**
 * Number of unsynchronized retries in size and containsValue
 * methods before resorting to locking. This is used to avoid
 * unbounded retries if tables undergo continuous modification
 * which would make it impossible to obtain an accurate result.
 */
static final int RETRIES_BEFORE_LOCK = 2;

public int size() {
    // Try a few times to get accurate count. On failure due to
    // continuous async changes in table, resort to locking.
    final Segment<K,V>[] segments = this.segments;
    int size;
    boolean overflow; // true if size overflows 32 bits
    long sum;         // sum of modCounts
    long last = 0L;   // previous sum
    int retries = -1; // first iteration isn't retry
    try {
        for (;;) {
            // 超过尝试次数，则对每个 Segment 加锁
            if (retries++ == RETRIES_BEFORE_LOCK) {
                for (int j = 0; j < segments.length; ++j)
                    ensureSegment(j).lock(); // force creation
            }
            sum = 0L;
            size = 0;
            overflow = false;
            for (int j = 0; j < segments.length; ++j) {
                Segment<K,V> seg = segmentAt(segments, j);
                if (seg != null) {
                    sum += seg.modCount;
                    int c = seg.count;
                    if (c < 0 || (size += c) < 0)
                        overflow = true;
                }
            }
            // 连续两次得到的结果一致，则认为这个结果是正确的
            if (sum == last)
                break;
            last = sum;
        }
    } finally {
        if (retries > RETRIES_BEFORE_LOCK) {
            for (int j = 0; j < segments.length; ++j)
                segmentAt(segments, j).unlock();
        }
    }
    return overflow ? Integer.MAX_VALUE : size;
}
```

3. 同步方式
Segment 继承自 ReentrantLock，所以我们可以很方便的对每一个 Segment 上锁。
对于读操作，获取 Key 所在的 Segment 时，需要保证可见性。具体实现上可以使用 volatile 关键字，也可使用锁。但使用锁开销太大，而使用 volatile 时每次写操作都会让所有 CPU 内缓存无效，也有一定开销。ConcurrentHashMap 使用如下方法保证可见性，取得最新的 Segment。

```
Segment<K,V> s = (Segment<K,V>)UNSAFE.getObjectVolatile(segments, u)
```

获取 Segment 中的 HashEntry 时也使用了类似方法

```
HashEntry<K,V> e = (HashEntry<K,V>) UNSAFE.getObjectVolatile
  (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE)
```

对于写操作，并不要求同时获取所有 Segment 的锁，因为那样相当于锁住了整个 Map。它会先获取该 Key-Value 对所在的 Segment 的锁，获取成功后就可以像操作一个普通的 HashMap 一样操作该 Segment，并保证该Segment 的安全性。 同时由于其它 Segment 的锁并未被获取，因此理论上可支持 concurrencyLevel（等于 Segment 的个数）个线程安全的并发读写。
获取锁时，并不直接使用 lock 来获取，因为该方法获取锁失败时会挂起。事实上，它使用了自旋锁，如果 tryLock 获取锁失败，说明锁被其它线程占用，此时通过循环再次以 tryLock 的方式申请锁。如果在循环过程中该 Key 所对应的链表头被修改，则重置 retry 次数。如果 retry 次数超过一定值，则使用 lock 方法申请锁。
这里使用自旋锁是因为自旋锁的效率比较高，但是它消耗 CPU 资源比较多，因此在自旋次数超过阈值时切换为互斥锁。
4. JDK 1.8 的改动
JDK 1.7 使用分段锁机制来实现并发更新操作，核心类为 Segment，它继承自重入锁 ReentrantLock，并发程度与 Segment 数量相等。
JDK 1.8 使用了 CAS 操作来支持更高的并发度，在 CAS 操作失败时使用内置锁 synchronized。
![enter image description here](https://camo.githubusercontent.com/b823c5f2cf18e7e27da70409d2b5e18fed820364/68747470733a2f2f6d792d626c6f672d746f2d7573652e6f73732d636e2d6265696a696e672e616c6979756e63732e636f6d2f323031392d362f4a444b312e382d436f6e63757272656e74486173684d61702d5374727563747572652e6a7067)
并且 JDK 1.8 的实现也在链表过长时会转换为红黑树。
ConcurrentHashMap取消了Segment分段锁，采用CAS和synchronized来保证并发安全。数据结构跟HashMap1.8的结构类似，数组+链表/红黑二叉树。
synchronized只锁定当前链表或红黑二叉树的首节点，这样只要hash不冲突，就不会产生并发，效率又提升N倍。


####HashSet
　　前面已经说过 HashSet 是对 HashMap 的简单包装，对 HashSet 的函数调用都会转换成合适的 HashMap 方法，因此 HashSet 的实现非常简单，只有不到 300 行代码（适配器模式）。这里不再赘述。
//HashSet是对HashMap的简单包装

```
public class HashSet<E>
{
	......
	private transient HashMap<E,Object> map;//HashSet里面有一个HashMap
    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
    public HashSet() {
        map = new HashMap<>();
    }
    ......
    public boolean add(E e) {//简单的方法转换
        return map.put(e, PRESENT)==null;
    }
    ......
}
```

1. 成员变量
首先了解下 HashSet 的成员变量:
 

```
   private transient HashMap<E,Object> map;

    // Dummy value to associate with an Object in the backing Map
    private static final Object PRESENT = new Object();
```

发现主要就两个变量:
map ：用于存放最终数据的。
PRESENT ：是所有写入 map 的 value 值。
2. 构造函数
 

```
   public HashSet() {
        map = new HashMap<>();
    }
    
    public HashSet(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }    
```

构造函数很简单，利用了 HashMap 初始化了 map 。
3. add()
```
   public boolean add(E e) {
        return map.put(e, PRESENT)==null;
    }
```

比较关键的就是这个 add() 方法。 可以看出它是将存放的对象当做了 HashMap 的健，value 都是相同的 PRESENT 。由于 HashMap 的 key 是不能重复的，所以每当有重复的值写入到 HashSet 时，value 会被覆盖，但 key 不会收到影响，这样就保证了 HashSet 中只能存放不重复的元素。
4. 总结
HashSet 的原理比较简单，几乎全部借助于 HashMap 来实现的。
所以 HashMap 会出现的问题 HashSet 依然不能避免。

####LinkedHashSet and LinkedHashMap
1.概览
　　如果你已看过前面关于 HashSet 和 HashMap，的讲解，一定能够想到本文将要讲解的 LinkedHashSet 和 LinkedHashMap 其实也是一回事。 LinkedHashSet 和 LinkedHashMap 在 Java 里也有着相同的实现，前者仅仅是对后者做了一层包装，也就是说 LinkedHashSet 里面有一个 LinkedHashMap（适配器模式）。因此本文将重点分析 LinkedHashMap。
　　LinkedHashMap 实现了 Map 接口，即允许放入 key 为 null 的元素，也允许插入 value 为 null 的元素。从名字上可以看出该容器是 LinkedList 和 HashMap 的混合体，也就是说它同时满足 HashMap 和 LinkedList 的某些特性。可将 **LinkedHashMap 看作采用 LinkedList 增强的 HashMap**。
　　![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/LinkedHashMap_base.png)


事实上 LinkedHashMap 是 HashMap 的直接子类，二者唯一的区别是 LinkedHashMap 在 HashMap 的基础上，采用双向链表（doubly-linked list）的形式将所有 entry 连接起来，这样是为保证元素的迭代顺序跟插入顺序相同。上图给出了 LinkedHashMap 的结构图，主体部分跟 HashMap 完全一样，多了 header 指向双向链表的头部（是一个哑元），该双向链表的迭代顺序就是 entry 的插入顺序。
除了可以保迭代历顺序，这种结构还有一个好处：迭代 LinkedHashMap 时不需要像 HashMap 那样遍历整个table，而只需要直接遍历 header 指向的双向链表即可，也就是说 LinkedHashMap 的迭代时间就只跟entry的个数相关，而跟table的大小无关。
有两个参数可以影响 LinkedHashMap 的性能：初始容量（inital capacity）和负载系数（load factor）。初始容量指定了初始table的大小，负载系数用来指定自动扩容的临界值。当entry的数量超过capacity*load_factor时，容器将自动扩容并重新哈希。对于插入元素较多的场景，将初始容量设大可以减少重新哈希的次数。
将对象放入到 LinkedHashMap 或 LinkedHashSet 中时，有两个方法需要特别关心：hashCode() 和 equals()。hashCode() 方法决定了对象会被放到哪个 bucket 里，当多个对象的哈希值冲突时，equals() 方法决定了这些对象是否是“同一个对象”。所以，如果要将自定义的对象放入到 LinkedHashMap 或 LinkedHashSet 中，需要 @Override  hashCode() 和 equals() 方法。
通过如下方式可以得到一个跟源 Map 迭代顺序 一样的 LinkedHashMap：

```
void foo(Map m) {
    Map copy = new LinkedHashMap(m);
    ...
}
```

出于性能原因，LinkedHashMap 是非同步的（not synchronized），如果需要在多线程环境使用，需要程序员手动同步；或者通过如下方式将 LinkedHashMap 包装成（wrapped）同步的：
Map m = Collections.synchronizedMap(new LinkedHashMap(...));

2. get()
get(Object key) 方法根据指定的 key 值返回对应的 value。该方法跟HashMap.get()方法的流程几乎完全一样，读者可自行参考前文，这里不再赘述。
3. put()
put(K key, V value) 方法是将指定的 key, value 对添加到 map 里。该方法首先会对 map 做一次查找，看是否包含该元组，如果已经包含则直接返回，查找过程类似于get()方法；如果没有找到，则会通过 addEntry(int hash, K key, V value, int bucketIndex) 方法插入新的 entry。
注意，这里的插入有两重含义：
从 table 的角度看，新的 entry 需要插入到对应的 bucket 里，当有哈希冲突时，采用头插法将新的 entry 插入到冲突链表的头部。
从 header 的角度看，新的 entry 需要插入到双向链表的尾部。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/LinkedHashMap_addEntry.png)
addEntry()代码如下：
// LinkedHashMap.addEntry()

```
void addEntry(int hash, K key, V value, int bucketIndex) {
    if ((size >= threshold) && (null != table[bucketIndex])) {
        resize(2 * table.length);// 自动扩容，并重新哈希
        hash = (null != key) ? hash(key) : 0;
        bucketIndex = hash & (table.length-1);// hash%table.length
    }
    // 1.在冲突链表头部插入新的entry
    HashMap.Entry<K,V> old = table[bucketIndex];
    Entry<K,V> e = new Entry<>(hash, key, value, old);
    table[bucketIndex] = e;
    // 2.在双向链表的尾部插入新的entry
    e.addBefore(header);
    size++;
}
```

上述代码中用到了 addBefore()方 法将新 entry e 插入到双向链表头引用 header 的前面，这样 e 就成为双向链表中的最后一个元素。addBefore() 的代码如下：
// LinkedHashMap.Entry.addBefor()，将this插入到existingEntry的前面

```
private void addBefore(Entry<K,V> existingEntry) {
    after  = existingEntry;
    before = existingEntry.before;
    before.after = this;
    after.before = this;
}
```

上述代码只是简单修改相关 entry 的引用而已。
4. remove()
remove(Object key)的作用是删除key值对应的entry，该方法的具体逻辑是在removeEntryForKey(Object key)里实现的。removeEntryForKey()方法会首先找到key值对应的entry，然后删除该entry（修改链表的相应引用）。查找过程跟get()方法类似。
注意，这里的删除也有两重含义：
1 从table的角度看，需要将该entry从对应的bucket里删除，如果对应的冲突链表不空，需要修改冲突链表的相应引用。
2 从header的角度来看，需要将该entry从双向链表中删除，同时修改链表中前面以及后面元素的相应引用。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/LinkedList_remove.png)

removeEntryForKey() 对应的代码如下：
// LinkedHashMap.removeEntryForKey()，删除key值对应的entry

```
final Entry<K,V> removeEntryForKey(Object key) {
	......
	int hash = (key == null) ? 0 : hash(key);
    int i = indexFor(hash, table.length);// hash&(table.length-1)
    Entry<K,V> prev = table[i];// 得到冲突链表
    Entry<K,V> e = prev;
    while (e != null) {// 遍历冲突链表
        Entry<K,V> next = e.next;
        Object k;
        if (e.hash == hash &&
            ((k = e.key) == key || (key != null && key.equals(k)))) {// 找到要删除的entry
            modCount++; size--;
            // 1. 将e从对应bucket的冲突链表中删除
            if (prev == e) table[i] = next;
            else prev.next = next;
            // 2. 将e从双向链表中删除
            e.before.after = e.after;
            e.after.before = e.before;
            return e;
        }
        prev = e; e = next;
    }
    return e;
}
```

5. LinkedHashSet
前面已经说过LinkedHashSet是对LinkedHashMap的简单包装，对LinkedHashSet的函数调用都会转换成合适的LinkedHashMap方法，因此LinkedHashSet的实现非常简单，这里不再赘述。

```
public class LinkedHashSet<E>
    extends HashSet<E>
    implements Set<E>, Cloneable, java.io.Serializable {
    ......
    // LinkedHashSet里面有一个LinkedHashMap
    public LinkedHashSet(int initialCapacity, float loadFactor) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
    }
	......
    public boolean add(E e) {//简单的方法转换
        return map.put(e, PRESENT)==null;
    }
    ......
}
```

6. LinkedHashMap经典用法
LinkedHashMap 除了可以保证迭代顺序外，还有一个非常有用的用法：可以轻松实现一个采用了FIFO替换策略的缓存。具体说来，LinkedHashMap 有一个子类方法 protected boolean removeEldestEntry(Map.Entry<K,V> eldest)，该方法的作用是告诉 Map 是否要删除“最老”的 Entry，所谓最老就是当前 Map 中最早插入的 Entry，如果该方法返回 true，最老的那个元素就会被删除。在每次插入新元素的之后 LinkedHashMap 会自动询问 removeEldestEntry() 是否要删除最老的元素。这样只需要在子类中重载该方法，当元素个数超过一定数量时让 removeEldestEntry() 返回 true，就能够实现一个固定大小的 FIFO 策略的缓存。示例代码如下：
/** 一个固定大小的FIFO替换策略的缓存 */

```
class FIFOCache<K, V> extends LinkedHashMap<K, V>{
    private final int cacheSize;
    public FIFOCache(int cacheSize){
        this.cacheSize = cacheSize;
    }

    // 当Entry个数超过cacheSize时，删除最老的Entry
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
       return size() > cacheSize;
    }
}
```

####三、容器中的设计模式
迭代器模式
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/Iterator-1.jpg)

Collection 实现了 Iterable 接口，其中的 iterator() 方法能够产生一个 Iterator 对象，通过这个对象就可以迭代遍历 Collection 中的元素。
从 JDK 1.5 之后可以使用 foreach 方法来遍历实现了 Iterable 接口的聚合对象。

```
List<String> list = new ArrayList<>();
list.add("a");
list.add("b");
for (String item : list) {
    System.out.println(item);
}
```

适配器模式

```
java.util.Arrays.asList() 可以把数组类型转换为 List 类型。
SafeVarargs
public static <T> List<T> asList(T... a)
```

如果要将数组类型转换为 List 类型，应该注意的是 asList() 的参数为泛型的变长参数，因此不能使用基本类型数组作为参数，只能使用相应的包装类型数组。

```
Integer[] arr = {1, 2, 3};
List list = Arrays.asList(arr);
```

也可以使用以下方式生成 List。

```
List list = Arrays.asList(1,2,3);
```

####四、面试指南
**1.ArrayList和LinkedList区别**
ArrayList 和 LinkedList 可想从名字分析，它们一个是 Array (动态数组) 的数据结构，一个是 Link (链表) 的数据结构，此外，它们两个都是对 List 接口的实现。前者是数组队列，相当于动态数组；后者为双向链表结构，也可当作堆栈、队列、双端队列；
当随机访问 List 时（get和set操作），ArrayList 比 LinkedList的效率更高，因为 LinkedList 是线性的数据存储方式，所以需要移动指针从前往后依次查找；
当对数据进行增加和删除的操作时（add 和 remove 操作），LinkedList 比 ArrayList 的效率更高，因为 ArrayList 是数组，所以在其中进行增删操作时，会对操作点之后所有数据的下标索引造成影响，需要进行数据的移动；
从利用效率来看，ArrayList 自由性较低，因为它需要手动的设置固定大小的容量，但是它的使用比较方便，只需要创建，然后添加数据，通过调用下标进行使用；而 LinkedList 自由性较高，能够动态的随数据量的变化而变化，但是它不便于使用；
ArrayList 主要空间开销在于需要在 List 列表预留一定空间；而 LinkList 主要控件开销在于需要存储结点信息以及结点指针信息。
ArrayList、LinkedList 和 Vector如何选择？
当对数据的主要操作为索引或只在集合的末端增加、删除元素时，使用 ArrayList 或 Vector 效率比较高；
当对数据的操作主要为制定位置的插入或删除操作时，使用 LinkedList 效率比较高；
当在多线程中使用容器时（即多个线程会同时访问该容器），选用 Vector 较为安全；

**2.HashMap和HashTable区别，HashMap的key类型**
Hash Map和HashTable的区别
Hashtable 的方法是同步的，HashMap 非同步，所以在多线程场合要手动同步
Hashtable 不允许 null 值 (key 和 value 都不可以)，HashMap 允许 null 值( key 和 value 都可以)。
两者的遍历方式大同小异，Hashtable 仅仅比 HashMap 多一个 elements 方法。
Hashtable 和 HashMap 都能通过 values() 方法返回一个 Collection ，然后进行遍历处理。
两者也都可以通过 entrySet() 方法返回一个 Set ， 然后进行遍历处理。
HashTable 使用 Enumeration，HashMap 使用 Iterator。
哈希值的使用不同，Hashtable 直接使用对象的 hashCode。而 HashMap 重新计算hash值，而且用于代替求模。
Hashtable 中 hash 数组默认大小是11，增加的方式是 old*2+1。HashMap 中 hash 数组的默认大小是16，而且一定是 2 的指数。
HashTable 基于 Dictionary 类，而 HashMap 基于 AbstractMap 类
HashMap中的key可以是任何对象或数据类型吗
可以为null，但不能是可变对象，如果是可变对象的话，对象中的属性改变，则对象 HashCode 也进行相应的改变，导致下次无法查找到已存在Map中的数据。
如果可变对象在 HashMap 中被用作键，那就要小心在改变对象状态的时候，不要改变它的哈希值了。我们只需要保证成员变量的改变能保证该对象的哈希值不变即可。
HashTable是线程安全的么
HashTable 是线程安全的，其实现是在对应的方法上添加了 synchronized 关键字进行修饰，由于在执行此方法的时候需要获得对象锁，则执行起来比较慢。所以现在如果为了保证线程安全的话，使用 CurrentHashMap。

**3. HashMap和ConcurrentHashMap**
HashMap和Concurrent HashMap区别？
HashMap是非线程安全的，CurrentHashMap 是线程安全的。
ConcurrentHashMap 将整个 Hash 桶进行了分段 segment，也就是将这个大的数组分成了几个小的片段segment，而且每个小的片段 segment 上面都有锁存在，那么在插入元素的时候就需要先找到应该插入到哪一个片段 segment，然后再在这个片段上面进行插入，而且这里还需要获取 segment 锁。
ConcurrentHashMap 让锁的粒度更精细一些，并发性能更好。
ConcurrentHashMap 线程安全吗， ConcurrentHashMap如何保证 线程安全？
HashTable 容器在竞争激烈的并发环境下表现出效率低下的原因是所有访问 HashTable 的线程都必须竞争同一把锁，那假如容器里有多把锁，每一把锁用于锁容器其中一部分数据，那么当多线程访问容器里不同数据段的数据时，线程间就不会存在锁竞争，从而可以有效的提高并发访问效率，这就是 ConcurrentHashMap 所使用的分段锁，首先将数据分成一段一段的存储，然后给每一段数据配一把锁，当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问。
get 操作的高效之处在于整个 get 过程不需要加锁，除非读到的值是空的才会加锁重读。get 方法里将要使用的共享变量都定义成 volatile，如用于统计当前 Segement 大小的 count 字段和用于存储值的 HashEntry 的 value。定义成 volatile 的变量，能够在线程之间保持可见性，能够被多线程同时读，并且保证不会读到过期的值，但是只能被单线程写（有一种情况可以被多线程写，就是写入的值不依赖于原值），在 get 操作里只需要读不需要写共享变量 count 和 value，所以可以不用加锁。
put 方法首先定位到 Segment，然后在 Segment 里进行插入操作。
插入操作需要经历两个步骤：（1）判断是否需要对 Segment 里的 HashEntry 数组进行扩容；（2）定位添加元素的位置然后放在HashEntry数组里。
4. Hashtable的原理
Hashtable 使用链地址法进行元素存储，通过一个实际的例子来演示一下插入元素的过程：
假设我们现在 Hashtable 的容量为 5，已经存在了 (5,5)，(13,13)，(16,16)，(17,17)，(21,21) 这 5 个键值对，目前他们在 Hashtable 中的位置如下：

![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashtable1.png)



现在，我们插入一个新的键值对，put(16,22)，假设 key=16 的索引为 1.但现在索引 1 的位置有两个 Entry 了，所以程序会对链表进行迭代。迭代的过程中，发现其中有一个 Entry 的 key 和我们要插入的键值对的 key 相同，所以现在会做的工作就是将 newValue=22 替换 oldValue=16，然后返回 oldValue = 16.

![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashtable2.png)
然后我们现在再插入一个，put(33,33)，key=33 的索引为 3，并且在链表中也不存在 key=33 的 Entry，所以将该节点插入链表的第一个位置。
![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hashtable3.png)
Hashtable 与 HashMap 的简单比较

HashTable 基于 Dictionary 类，而 HashMap 是基于 AbstractMap。Dictionary 是任何可将键映射到相应值的类的抽象父类，而 AbstractMap 是基于 Map 接口的实现，它以最大限度地减少实现此接口所需的工作。
HashMap 的 key 和 value 都允许为 null，而 Hashtable 的 key 和 value 都不允许为 null。HashMap 遇到 key 为 null 的时候，调用 putForNullKey 方法进行处理，而对 value 没有处理；Hashtable遇到 null，直接返回 NullPointerException。
Hashtable 方法是同步，而HashMap则不是。我们可以看一下源码，Hashtable 中的几乎所有的 public 的方法都是 synchronized 的，而有些方法也是在内部通过 synchronized 代码块来实现。所以有人一般都建议如果是涉及到多线程同步时采用 HashTable，没有涉及就采用 HashMap，但是在 Collections 类中存在一个静态方法：synchronizedMap()，该方法创建了一个线程安全的 Map 对象，并把它作为一个封装的对象来返回。
参考资料：

Hashtable 的实现原理 - Java 集合学习指南 - 极客学院Wiki

5. Hash冲突的解决办法
链地址法
开放地址法（向后一位）
线性探测
平方探测
二次哈希
再哈希法
6. 什么是迭代器
　　Java 集合框架的集合类，我们有时候称之为容器。容器的种类有很多种，比如 ArrayList、LinkedList、HashSet...，每种容器都有自己的特点，ArrayList 底层维护的是一个数组；LinkedList 是链表结构的；HashSet 依赖的是哈希表，每种容器都有自己特有的数据结构。

　　因为容器的内部结构不同，很多时候可能不知道该怎样去遍历一个容器中的元素。所以为了使对容器内元素的操作更为简单，Java 引入了迭代器模式！

　　把访问逻辑从不同类型的集合类中抽取出来，从而避免向外部暴露集合的内部结构。

　　迭代器模式：就是提供一种方法对一个容器对象中的各个元素进行访问，而又不暴露该对象容器的内部细。

```
public static void main(String[] args) {
    // 使用迭代器遍历ArrayList集合
    Iterator<String> listIt = list.iterator();
    while(listIt.hasNext()){
        System.out.println(listIt.hasNext());
    }
    // 使用迭代器遍历Set集合
    Iterator<String> setIt = set.iterator();
    while(setIt.hasNext()){
        System.out.println(listIt.hasNext());
    }
    // 使用迭代器遍历LinkedList集合
    Iterator<String> linkIt = linkList.iterator();
    while(linkIt.hasNext()){
        System.out.println(listIt.hasNext());
    }
}
```



深入理解Java中的迭代器 - Mr·Dragon - 博客园
7. 构造相同hash的字符串进行攻击，这种情况应该怎么处理？JDK7如何处理
攻击原理：

　　当客户端发送一个请求到服务器，如果该请求中带有参数，服务器端会将 参数名-参数值 作为 key-value 保存在 HashMap 中。如果有人恶意构造请求，在请求中加入大量相同 hash 值的 String 参数名（key），那么在服务器端用于存储这些 key-value 对的 HashMap 会被强行退化成链表，如图：

![enter image description here](https://github.com/frank-lam/fullstack-tutorial/raw/master/notes/JavaArchitecture/assets/hash-to-badlink.png)
如果数据量足够大，那么在查找，插入时会占用大量 CPU，达到拒绝服务攻击的目的。

怎么处理

限制 POST 和 GET 请求的参数个数
限制 POST 请求的请求体大小
Web Application FireWall（WAF）
JDK7如何处理

HashMap 会动态的使用一个专门 TreeMap 实现来替换掉它。

8. Hashmap为什么大小是2的幂次
首先来看一下 hashmap 的 put 方法的源码

```
public V put(K key, V value) {
    if (key == null)                
        return putForNullKey(value);  //将空key的Entry加入到table[0]中
    int hash = hash(key.hashCode());  //计算key.hashcode()的hash值，hash函数由hashmap自己实现
    int i = indexFor(hash, table.length);  //获取将要存放的数组下标
    /*
     * for中的代码用于：当hash值相同且key相同的情况下，使用新值覆盖旧值（其实就是修改功能）
     */
    //注意：for循环在第一次执行时就会先判断条件
    for (Entry<K, V> e = table[i]; e != null; e = e.next) {
        Object k;
        //hash值相同且key相同的情况下，使用新值覆盖旧值
        if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
            V oldValue = e.value;
            e.value = value;
            //e.recordAccess(this);
            return oldValue;//返回旧值
        }
    }
    modCount++;
    addEntry(hash, key, value, i);//增加一个新的Entry到table[i]
    return null;//如果没有与传入的key相等的Entry，就返回null
}
```

```
/**
 * "按位与"来获取数组下标
 */
static int indexFor(int h, int length) {
    return h & (length - 1);
}
```

**hashmap 始终将自己的桶保持在2n，这是为什么？indexFor这个方法解释了这个问题**

大家都知道计算机里面位运算是基本运算，位运算的效率是远远高于取余 % 运算的

举个例子：2n 转换成二进制就是 1+n 个 0，减 1 之后就是 0+n个1，如16 -> 10000，15 -> 01111

那么根据 & 位运算的规则，都为 1 (真)时，才为 1，那 0≤运算后的结果≤15，假设 h <= 15，那么运算后的结果就是 h 本身，h >15，运算后的结果就是最后四位二进制做 & 运算后的值，最终，就是 % 运算后的余数。

当容量一定是 2n 时，h & (length - 1) == h % length

9 说说List,Set,Map三者的区别？
List(对付顺序的好帮手)： List接口存储一组不唯一（可以有多个元素引用相同的对象），有序的对象
Set(注重独一无二的性质): 不允许重复的集合。不会有多个元素引用相同的对象。
Map(用Key来搜索的专家): 使用键值对存储。Map会维护与Key有关联的值。两个Key可以引用相同的对象，但Key不能重复，典型的Key是String类型，但也可以是任何对象。

10 comparable 和 Comparator的区别
comparable接口实际上是出自java.lang包 它有一个 compareTo(Object obj)方法用来排序
comparator接口实际上是出自 java.util 包它有一个compare(Object obj1, Object obj2)方法用来排序
一般我们需要对一个集合使用自定义排序时，我们就要重写compareTo()方法或compare()方法，当我们需要对某一个集合实现两种排序方式，比如一个song对象中的歌名和歌手名分别采用一种排序方法的话，我们可以重写compareTo()方法和使用自制的Comparator方法或者以两个Comparator来实现歌名排序和歌星名排序，第二种代表我们只能使用两个参数版的 Collections.sort().

Comparator定制排序
 

```
       ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(-1);
        arrayList.add(3);
        arrayList.add(3);
        arrayList.add(-5);
        arrayList.add(7);
        arrayList.add(4);
        arrayList.add(-9);
        arrayList.add(-7);
        System.out.println("原始数组:");
        System.out.println(arrayList);
        // void reverse(List list)：反转
        Collections.reverse(arrayList);
        System.out.println("Collections.reverse(arrayList):");
        System.out.println(arrayList);

        // void sort(List list),按自然排序的升序排序
        Collections.sort(arrayList);
        System.out.println("Collections.sort(arrayList):");
        System.out.println(arrayList);
        // 定制排序的用法
        Collections.sort(arrayList, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return o2.compareTo(o1);
            }
        });
        System.out.println("定制排序后：");
        System.out.println(arrayList);

Output:
原始数组:
[-1, 3, 3, -5, 7, 4, -9, -7]
Collections.reverse(arrayList):
[-7, -9, 4, 7, -5, 3, 3, -1]
Collections.sort(arrayList):
[-9, -7, -5, -1, 3, 3, 4, 7]
定制排序后：
[7, 4, 3, 3, -1, -5, -7, -9]
```

重写compareTo方法实现按年龄来排序
// person对象没有实现Comparable接口，所以必须实现，这样才不会出错，才可以使treemap中的数据按顺序排列
// 前面一个例子的String类已经默认实现了Comparable接口，详细可以查看String类的API文档，另外其他
// 像Integer类等都已经实现了Comparable接口，所以不需要另外实现了

```
public  class Person implements Comparable<Person> {
    private String name;
    private int age;

    public Person(String name, int age) {
        super();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * TODO重写compareTo方法实现按年龄来排序
     */
    @Override
    public int compareTo(Person o) {
        // TODO Auto-generated method stub
        if (this.age > o.getAge()) {
            return 1;
        } else if (this.age < o.getAge()) {
            return -1;
        }
        return age;
    }
}
    public static void main(String[] args) {
        TreeMap<Person, String> pdata = new TreeMap<Person, String>();
        pdata.put(new Person("张三", 30), "zhangsan");
        pdata.put(new Person("李四", 20), "lisi");
        pdata.put(new Person("王五", 10), "wangwu");
        pdata.put(new Person("小红", 5), "xiaohong");
        // 得到key的值的同时得到key所对应的值
        Set<Person> keys = pdata.keySet();
        for (Person key : keys) {
            System.out.println(key.getAge() + "-" + key.getName());

        }
    }
    
Output：
5-小红
10-王五
20-李四
30-张三
```

---
华丽的分割线

---
#### javaIO
Java IO
1、磁盘操作（File）
2、字节操作（*Stream）
3、字符操作（*Reader | *Writer）
4、Java序列化，如何实现序列化和反序列化，常见的序列化协议有哪些？
Java序列化定义
如何实现序列化和反序列化，底层怎么实现
相关注意事项
常见的序列化协议有哪些
5、同步和异步
6、Java中的NIO，BIO，AIO分别是什么
BIO
NIO
AIO (NIO.2)
总结
7、BIO，NIO，AIO区别
8、Stock通信的伪代码实现流程
9、网络操作
InetAddress
URL
Sockets
Datagram
什么是Socket？
Java IO
Java 的 I/O 大概可以分成以下几类：
磁盘操作：File
字节操作：InputStream 和 OutputStream
字符操作：Reader 和 Writer
对象操作：Serializable
网络操作：Socket
新的输入/输出：NIO
1、磁盘操作（File）
File 类可以用于表示文件和目录的信息，但是它不表示文件的内容。
递归地输出一个目录下所有文件：

```
public static void listAllFiles(File dir)
{
    if (dir == null || !dir.exists()) {
        return;
    }
    if (dir.isFile()) {
        System.out.println(dir.getName());
        return;
    }
    for (File file : dir.listFiles()) {
        listAllFiles(file);
    }
}
```

2、字节操作（*Stream）
使用字节流操作进行文件复制：

```
public static void copyFile(String src, String dist) throws IOException
{
    FileInputStream in = new FileInputStream(src);
    FileOutputStream out = new FileOutputStream(dist);
    byte[] buffer = new byte[20 * 1024];
    // read() 最多读取 buffer.length 个字节
    // 返回的是实际读取的个数
    // 返回 -1 的时候表示读到 eof，即文件尾
    while (in.read(buffer, 0, buffer.length) != -1) {
        out.write(buffer);
    }
    in.close();
    out.close();
}
```



Java I/O 使用了装饰者模式来实现。以 InputStream 为例，InputStream 是抽象组件，FileInputStream 是 InputStream 的子类，属于具体组件，提供了字节流的输入操作。FilterInputStream 属于抽象装饰者，装饰者用于装饰组件，为组件提供额外的功能，例如 BufferedInputStream 为 FileInputStream 提供缓存的功能。
实例化一个具有缓存功能的字节流对象时，只需要在 FileInputStream 对象上再套一层 BufferedInputStream 对象即可。

```
FileInputStream fileInputStream = new FileInputStream(filePath);
BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
```

DataInputStream 装饰者提供了对更多数据类型进行输入的操作，比如 int、double 等基本类型。

3、字符操作（*Reader | *Writer）
不管是磁盘还是网络传输，最小的存储单元都是字节，而不是字符。但是在程序中操作的通常是字符形式的数据，因此需要提供对字符进行操作的方法。
InputStreamReader 实现从字节流解码成字符流；
OutputStreamWriter 实现字符流编码成为字节流。
逐行输出文本文件的内容：

```
public static void readFileContent(String filePath) throws IOException
{
    FileReader fileReader = new FileReader(filePath);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String line;
    while ((line = bufferedReader.readLine()) != null) {
        System.out.println(line);
    }
    // 装饰者模式使得 BufferedReader 组合了一个 Reader 对象
    // 在调用 BufferedReader 的 close() 方法时会去调用 fileReader 的 close() 方法
    // 因此只要一个 close() 调用即可
    bufferedReader.close();
}
```

编码就是把字符转换为字节，而解码是把字节重新组合成字符。
如果编码和解码过程使用不同的编码方式那么就出现了乱码。
GBK 编码中，中文字符占 2 个字节，英文字符占 1 个字节；
UTF-8 编码中，中文字符占 3 个字节，英文字符占 1 个字节；
UTF-16be 编码中，中文字符和英文字符都占 2 个字节。
UTF-16be 中的 be 指的是 Big Endian，也就是大端。相应地也有 UTF-16le，le 指的是 Little Endian，也就是小端。
Java 使用双字节编码 UTF-16be，这不是指 Java 只支持这一种编码方式，而是说 char 这种类型使用 UTF-16be 进行编码。char 类型占 16 位，也就是两个字节，Java 使用这种双字节编码是为了让一个中文或者一个英文都能使用一个 char 来存储。
String 可以看成一个字符序列，可以指定一个编码方式将它转换为字节序列，也可以指定一个编码方式将一个字节序列转换为 String。
String str1 = "中文";
byte[] bytes = str1.getBytes("UTF-8");
String str2 = new String(bytes, "UTF-8");
System.out.println(str2);

在调用无参数 getBytes() 方法时，默认的编码方式不是 UTF-16be。双字节编码的好处是可以使用一个 char 存储中文和英文，而将 String 转为 bytes[] 字节数组就不再需要这个好处，因此也就不再需要双字节编码。getBytes() 的默认编码方式与平台有关，一般为 UTF-8。
byte[] bytes = str1.getBytes();

4、Java序列化，如何实现序列化和反序列化，常见的序列化协议有哪些？
Java序列化定义
（1）Java序列化是指把Java对象转换为字节序列的过程，而Java反序列化是指把字节序列恢复为Java对象的过程；
（2）序列化：对象序列化的最主要的用处就是在传递和保存对象的时候，保证对象的完整性和可传递性。序列化是把对象转换成有序字节流，以便在网络上传输或者保存在本地文件中。序列化后的字节流保存了Java对象的状态以及相关的描述信息。序列化机制的核心作用就是对象状态的保存与重建。
（3）反序列化：客户端从文件中或网络上获得序列化后的对象字节流后，根据字节流中所保存的对象状态及描述信息，通过反序列化重建对象。
（4）本质上讲，序列化就是把实体对象状态按照一定的格式写入到有序字节流，反序列化就是从有序字节流重建对象，恢复对象状态。


如何实现序列化和反序列化，底层怎么实现
1、JDK类库中序列化和反序列化API
（1）java.io.ObjectOutputStream：表示对象输出流；
它的writeObject(Object obj)方法可以对参数指定的obj对象进行序列化，把得到的字节序列写到一个目标输出流中；
（2）java.io.ObjectInputStream：表示对象输入流；
它的readObject()方法源输入流中读取字节序列，再把它们反序列化成为一个对象，并将其返回；
2、实现序列化的要求
只有实现了 Serializable 或 Externalizable 接口的类的对象才能被序列化，否则抛出异常！
3、实现Java对象序列化与反序列化的方法
　　假定一个User类，它的对象需要序列化，可以有如下三种方法：
若 User 类仅仅实现了 Serializable 接口，则可以按照以下方式进行序列化和反序列化
ObjectOutputStream 采用默认的序列化方式，对 User 对象的非 transient 的实例变量进行序列化。
ObjcetInputStream 采用默认的反序列化方式，对对 User 对象的非 transient 的实例变量进行反序列化。
若User类仅仅实现了Serializable接口，并且还定义了 readObject(ObjectInputStream in) 和writeObject(ObjectOutputSteam out)，则采用以下方式进行序列化与反序列化。
ObjectOutputStream 调用 User 对象的 writeObject(ObjectOutputStream out) 的方法进行序列化。
ObjectInputStream 会调用 User 对象的 readObject(ObjectInputStream in) 的方法进行反序列化。
若User类实现了 Externalnalizable 接口，且 User 类必须实现 readExternal(ObjectInput in) 和
 writeExternal(ObjectOutput out) 方法，则按照以下方式进行序列化与反序列化。
ObjectOutputStream 调用 User 对象的 writeExternal(ObjectOutput out)) 的方法进行序列化。
ObjectInputStream 会调用User对象的 readExternal(ObjectInput in) 的方法进行反序列化。
4、JDK类库中序列化的步骤
步骤一：创建一个对象输出流，它可以包装一个其它类型的目标输出流，如文件输出流：

```
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("D:\\object.out"));
```

步骤二：通过对象输出流的writeObject()方法写对象：
oos.writeObject(new User("xuliugen", "123456", "male"));

5、JDK类库中反序列化的步骤
步骤一：创建一个对象输入流，它可以包装一个其它类型输入流，如文件输入流：

```
ObjectInputStream ois= new ObjectInputStream(new FileInputStream("object.out"));
```

步骤二：通过对象输出流的readObject()方法读取对象：

```
User user = (User) ois.readObject();
```

说明：为了正确读取数据，完成反序列化，必须保证向对象输出流写对象的顺序与从对象输入流中读对象的顺序一致。
6、序列化和反序列化的示例
为了更好地理解Java序列化与反序列化，举一个简单的示例如下：

```
public class SerialDemo {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //序列化
        FileOutputStream fos = new FileOutputStream("object.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        User user1 = new User("xuliugen", "123456", "male");
        oos.writeObject(user1);
        oos.flush();
        oos.close();
        
        //反序列化
        FileInputStream fis = new FileInputStream("object.out");
        ObjectInputStream ois = new ObjectInputStream(fis);
        User user2 = (User) ois.readObject();
        System.out.println(user2.getUserName()+ " " + 
            user2.getPassword() + " " + user2.getSex());
        //反序列化的输出结果为：xuliugen 123456 male
    }
}

public class User implements Serializable {
    private String userName;
    private String password;
    private String sex;
    //全参构造方法、get和set方法省略
}
```

相关注意事项
1、序列化时，只对对象的状态进行保存，而不管对象的方法；
2、当一个父类实现序列化，子类自动实现序列化，不需要显式实现 Serializable 接口；
3、当一个对象的实例变量引用其他对象，序列化该对象时也把引用对象进行序列化；
4、并非所有的对象都可以序列化，至于为什么不可以，有很多原因了，比如：
安全方面的原因，比如一个对象拥有private，public等field，对于一个要传输的对象，比如写到文件，或者进行RMI传输等等，在序列化进行传输的过程中，这个对象的private等域是不受保护的；
资源分配方面的原因，比如socket，thread类，如果可以序列化，进行传输或者保存，也无法对他们进行重新的资源分配，而且，也是没有必要这样实现；
5、声明为static和transient类型的成员数据不能被序列化。因为static代表类的状态，transient代表对象的临时数据。
6、序列化运行时使用一个称为 serialVersionUID 的版本号与每个可序列化类相关联，该序列号在反序列化过程中用于验证序列化对象的发送者和接收者是否为该对象加载了与序列化兼容的类。为它赋予明确的值。显式地定义serialVersionUID有两种用途：
在某些场合，希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有相同的serialVersionUID；
在某些场合，不希望类的不同版本对序列化兼容，因此需要确保类的不同版本具有不同的serialVersionUID。
7、Java有很多基础类已经实现了serializable接口，比如String , Vector等。但是也有一些没有实现serializable接口的；
8、如果一个对象的成员变量是一个对象，那么这个对象的数据成员也会被保存！这是能用序列化解决深拷贝的重要原因；
ArrayList 序列化和反序列化的实现 ：ArrayList 中存储数据的数组是用 transient 修饰的，因为这个数组是动态扩展的，并不是所有的空间都被使用，因此就不需要所有的内容都被序列化。通过重写序列化和反序列化方法，使得可以只序列化数组中有内容的那部分数据。
private transient Object[] elementData;


COM主要用于Windows平台，并没有真正实现跨平台，另外COM的序列化的原理利用了编译器中虚表，使得其学习成本巨大。
CORBA是早期比较好的实现了跨平台，跨语言的序列化协议。COBRA的主要问题是参与方过多带来的版本过多，版本之间兼容性较差，以及使用复杂晦涩。
XML & SOAP
XML是一种常用的序列化和反序列化协议，具有跨机器，跨语言等优点。
SOAP（Simple Object Access protocol） 是一种被广泛应用的，基于XML为序列化和反序列化协议的结构化消息传递协议。SOAP具有安全、可扩展、跨语言、跨平台并支持多种传输层协议。
JSON（JavaScript Object Notation）
这种Associative array格式非常符合工程师对对象的理解。
它保持了XML的人眼可读（Human-readable）的优点。
相对于XML而言，序列化后的数据更加简洁。
它具备javascript的先天性支持，所以被广泛应用于Web browser的应用常景中，是Ajax的事实标准协议。
与XML相比，其协议比较简单，解析速度比较快。
松散的Associative array使得其具有良好的可扩展性和兼容性。
Thrift是Facebook开源提供的一个高性能，轻量级RPC服务框架，其产生正是为了满足当前大数据量、分布式、跨语言、跨平台数据通讯的需求。Thrift在空间开销和解析性能上有了比较大的提升，对于对性能要求比较高的分布式系统，它是一个优秀的RPC解决方案；但是由于Thrift的序列化被嵌入到Thrift框架里面，Thrift框架本身并没有透出序列化和反序列化接口，这导致其很难和其他传输层协议共同使用
Protobuf具备了优秀的序列化协议的所需的众多典型特征
标准的IDL和IDL编译器，这使得其对工程师非常友好。
序列化数据非常简洁，紧凑，与XML相比，其序列化之后的数据量约为1/3到1/10。
解析速度非常快，比对应的XML快约20-100倍。
提供了非常友好的动态库，使用非常简介，反序列化只需要一行代码。由于其解析性能高，序列化后数据量相对少，非常适合应用层对象的持久化场景
Avro的产生解决了JSON的冗长和没有IDL的问题，Avro属于Apache Hadoop的一个子项目。 Avro提供两种序列化格式：JSON格式或者Binary格式。Binary格式在空间开销和解析性能方面可以和Protobuf媲美，JSON格式方便测试阶段的调试。适合于高性能的序列化服务。
几种协议的对比
XML序列化（Xstream）无论在性能和简洁性上比较差；
Thrift与Protobuf相比在时空开销方面都有一定的劣势；
Protobuf和Avro在两方面表现都非常优越。
5、同步和异步
同步IO：
读写IO时代码等数据返回后才继续执行后续代码
代码编写简单，CPU执行效率低
JDK提供的java.io是同步IO
异步IO：
读写IO时仅发出请求，然后立即执行后续代码
代码编写复杂，CPU执行效率高
JDK提供的java.nio是异步IO
6、Java中的NIO，BIO，AIO分别是什么
同步阻塞IO（BIO）：用户进程发起一个IO操作以后，必须等待IO操作的真正完成后，才能继续运行；
同步非阻塞IO（NIO）：用户进程发起一个IO操作以后，可做其它事情，但用户进程需要经常询问IO操作是否完成，这样造成不必要的CPU资源浪费；
异步非阻塞IO（AIO）：用户进程发起一个IO操作然后，立即返回，等IO操作真正的完成以后，应用程序会得到IO操作完成的通知。类比Future模式。
先来个例子理解一下概念，以银行取款为例：
同步 ： 自己亲自出马持银行卡到银行取钱（使用同步IO时，Java自己处理IO读写）。
异步 ： 委托一小弟拿银行卡到银行取钱，然后给你（使用异步IO时，Java将IO读写委托给OS处理，需要将数据缓冲区地址和大小传给OS(银行卡和密码)，OS需要支持异步IO操作API）。
阻塞 ： ATM排队取款，你只能等待（使用阻塞IO时，Java调用会一直阻塞到读写完成才返回）。
非阻塞 ： 柜台取款，取个号，然后坐在椅子上做其它事，等号广播会通知你办理，没到号你就不能去，你可以不断问大堂经理排到了没有，大堂经理如果说还没到你就不能去（使用非阻塞IO时，如果不能读写Java调用会马上返回，当IO事件分发器会通知可读写时再继续进行读写，不断循环直到读写完成）。


**BIO**
定义：BIO 全称Block-IO 是一种阻塞同步的通信模式。我们常说的Stock IO 一般指的是BIO。是一个比较传统的通信方式，模式简单，使用方便。但并发处理能力低，通信耗时，依赖网速。
BIO 设计原理：
服务器通过一个 Acceptor 线程负责监听客户端请求和为每个客户端创建一个新的线程进行链路处理。典型的一请求一应答模式。若客户端数量增多，频繁地创建和销毁线程会给服务器打开很大的压力。后改良为用线程池的方式代替新增线程，被称为伪异步IO。
服务器提供IP地址和监听的端口，客户端通过TCP的三次握手与服务器连接，连接成功后，双放才能通过套接字(Stock)通信。
小结：
BIO模型中通过 Socket 和 ServerSocket 完成套接字通道的实现。阻塞，同步，建立连接耗时。


为了改进这种一连接一线程的模型，我们可以使用线程池来管理这些线程（需要了解更多请参考前面提供的文章），实现1个或多个线程处理N个客户端的模型（但是底层还是使用的同步阻塞I/O），通常被称为“伪异步I/O模型“。

实现很简单，我们只需要将新建线程的地方，交给线程池管理即可。
我们知道，如果使用 CachedThreadPool 线程池（不限制线程数量，如果不清楚请参考文首提供的文章），其实除了能自动帮我们管理线程（复用），看起来也就像是1:1的客户端：线程数模型，而使用 FixedThreadPool 我们就有效的控制了线程的最大数量，保证了系统有限的资源的控制，实现了N:M的伪异步 I/O 模型。
但是，正因为限制了线程数量，如果发生大量并发请求，超过最大数量的线程就只能等待，直到线程池中的有空闲的线程可以被复用。而对 Socket 的输入流就行读取时，会一直阻塞，直到发生：
有数据可读
可用数据以及读取完毕
发生空指针或 I/O 异常
所以在读取数据较慢时（比如数据量大、网络传输慢等），大量并发的情况下，其他接入的消息，只能一直等待，这就是最大的弊端。
而后面即将介绍的NIO，就能解决这个难题。
NIO
NIO（官方：New IO），也叫Non-Block IO 是一种同步非阻塞的通信模式。
NIO 设计原理：
NIO相对于BIO来说一大进步。客户端和服务器之间通过Channel通信。NIO可以在Channel进行读写操作。这些Channel都会被注册在Selector多路复用器上。Selector通过一个线程不停的轮询这些Channel。找出已经准备就绪的Channel执行IO操作。 NIO 通过一个线程轮询，实现千万个客户端的请求，这就是非阻塞NIO的特点。
1）缓冲区Buffer：它是NIO与BIO的一个重要区别。BIO是将数据直接写入或读取到Stream对象中。而NIO的数据操作都是在缓冲区中进行的。缓冲区实际上是一个数组。Buffer最常见的类型是ByteBuffer，另外还有CharBuffer，ShortBuffer，IntBuffer，LongBuffer，FloatBuffer，DoubleBuffer。
2）通道Channel：和流不同，通道是双向的。NIO可以通过Channel进行数据的读，写和同时读写操作。通道分为两大类：一类是网络读写（SelectableChannel），一类是用于文件操作（FileChannel），我们使用的SocketChannel和ServerSocketChannel都是SelectableChannel的子类。
3）多路复用器Selector：NIO编程的基础。多路复用器提供选择已经就绪的任务的能力。就是Selector会不断地轮询注册在其上的通道（Channel），如果某个通道处于就绪状态，会被Selector轮询出来，然后通过SelectionKey可以取得就绪的Channel集合，从而进行后续的IO操作。服务器端只要提供一个线程负责Selector的轮询，就可以接入成千上万个客户端，这就是JDK NIO库的巨大进步。
小结：NIO模型中通过SocketChannel和ServerSocketChannel完成套接字通道的实现。非阻塞/阻塞，同步，避免TCP建立连接使用三次握手带来的开销。

AIO (NIO.2)
异步非阻塞，服务器实现模式为一个有效请求一个线程，客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理.
AIO方式使用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持。
AIO 并没有采用NIO的多路复用器，而是使用异步通道的概念。其read，write方法的返回类型都是Future对象。而Future模型是异步的，其核心思想是：去主函数等待时间。
小结：AIO模型中通过AsynchronousSocketChannel和AsynchronousServerSocketChannel完成套接字通道的实现。非阻塞，异步。
总结
BIO模型中通过Socket和ServerSocket完成套接字通道实现。阻塞，同步，连接耗时。
NIO模型中通过SocketChannel和ServerSocketChannel完成套接字通道实现。非阻塞/阻塞，同步，避免TCP建立连接使用三次握手带来的开销。
AIO模型中通过AsynchronousSocketChannel和AsynchronousServerSocketChannel完成套接字通道实现。非阻塞，异步。


**另外，**I/O属于底层操作，需要操作系统支持，并发也需要操作系统的支持，所以性能方面不同操作系统差异会比较明显。
参考：
Java BIO、NIO、AIO 学习-力量来源于赤诚的爱！-51CTO博客
Netty序章之BIO NIO AIO演变 - JavaEE教程 - SegmentFault 思否
Java 网络IO编程总结（BIO、NIO、AIO均含完整实例代码） - CSDN博客
Java IO Tutorial
7、BIO，NIO，AIO区别
BIO（同步阻塞）：客户端和服务器连接需要三次握手，使用简单，但吞吐量小
NIO（同步非阻塞）：客户端与服务器通过Channel连接，采用多路复用器轮询注册的Channel。提高吞吐量和可靠性。
AIO（异步非阻塞）：NIO的升级版，采用异步通道实现异步通信，其read和write方法均是异步方法。
8、Stock通信的伪代码实现流程
服务器绑定端口：server = new ServerSocket(PORT)
服务器阻塞监听：socket = server.accept()
服务器开启线程：new Thread(Handle handle)
服务器读写数据：BufferedReader PrintWriter
客户端绑定IP和PORT：new Socket(IP_ADDRESS, PORT)
客户端传输接收数据：BufferedReader PrintWriter
9、网络操作
Java 中的网络支持：
InetAddress：用于表示网络上的硬件资源，即 IP 地址；
URL：统一资源定位符；
Sockets：使用 TCP 协议实现网络通信；
Datagram：使用 UDP 协议实现网络通信。
InetAddress
没有公有构造函数，只能通过静态方法来创建实例。
InetAddress.getByName(String host);
InetAddress.getByAddress(byte[] address);
URL
可以直接从 URL 中读取字节流数据。
public static void main(String[] args) throws IOException
{
    URL url = new URL("http://www.baidu.com");
    // 字节流
    InputStream is = url.openStream();
    // 字符流
    InputStreamReader isr = new InputStreamReader(is, "utf-8");
    BufferedReader br = new BufferedReader(isr);
    String line = br.readLine();
    while (line != null) {
        System.out.println(line);
        line = br.readLine();
    }
    br.close();
}
Sockets

ServerSocket：服务器端类
Socket：客户端类
服务器和客户端通过 InputStream 和 OutputStream 进行输入输出。


参考资料：
使用TCP/IP的套接字（Socket）进行通信 - alps_01 - 博客园
Datagram
DatagramPacket：数据包类
DatagramSocket：通信类
什么是Socket？
TCP用主机的IP地址加上主机上的端口号作为TCP连接的端点，这种端点就叫做套接字（socket）或插口。
套接字用（IP地址：端口号）表示。
Socket是进程通讯的一种方式，即调用这个网络库的一些API函数实现分布在不同主机的相关进程之间的数据交换。
socket是网络编程的基础，本文用打电话来类比socket通信中建立TCP连接的过程。
socket函数：表示你买了或者借了一部手机。 bind函数：告诉别人你的手机号码，让他们给你打电话。 listen函数：打开手机的铃声，而不是静音，这样有电话时可以立马反应。listen函数的第二个参数，最大连接数，表示最多有几个人可以同时拨打你的号码。不过我们的手机，最多只能有一个人打进来，要不然就提示占线。 connect函数：你的朋友知道了你的号码，通过这个号码来联系你。在他等待你回应的时候，不能做其他事情，所以connect函数是阻塞的。 accept函数：你听到了电话铃声，接电话，accept it！然后“喂”一声，你的朋友听到你的回应，知道电话已经打进去了。至此，一个TCP连接建立了。 read/write函数：连接建立后，TCP的两端可以互相收发消息，这时候的连接是全双工的。对应打电话中的电话煲。 close函数：通话完毕，一方说“我挂了”，另一方回应"你挂吧"，然后将连接终止。实际的close(sockfd)有些不同，它不止是终止连接，还把手机也归还，不在占有这部手机，就当是公用电话吧。
注意到，上述连接是阻塞的，你一次只能响应一个用户的连接请求，但在实际网络编程中，一个服务器服务于多个客户，上述方案也就行不通了，怎么办？想一想10086，移动的声讯服务台，也是只有一个号码，它怎么能同时服务那么多人呢？可以这样理解，在你打电话到10086时，总服务台会让一个接线员来为你服务，而它自己却继续监听有没有新的电话接入。在网络编程中，这个过程类似于fork一个子进程，建立实际的通信连接，而主进程继续监听。10086的接线员是有限的，所以当连接的人数达到上线时，它会放首歌给你听，忙等待，直到有新的空闲接线员为止。 实际网络编程中，处理并发的方式还有select/poll/epoll等。
下面是一个实际的socket通信过程：


Socket的特点
Socket基于TCP链接，数据传输有保障
Socket适用于建立长时间链接
Socket编程通常应用于即时通讯

--- 

---

#### Java高级
[挑战10个最难回答的Java面试题](https://juejin.im/post/5d5033c86fb9a06b031fefca?utm_source=gold_browser_extension)
1. 为什么等待和通知是在 Object 类而不是 Thread 中声明的？
2. 为什么Java中不支持多重继承？
3. 为什么Java不支持运算符重载？
4. 为什么 String 在 Java 中是不可变的？
5. 为什么 char 数组比 Java 中的 String 更适合存储密码？
6. 如何使用双重检查锁定在 Java 中创建线程安全的单例？
7. 编写 Java 程序时, 如何在 Java 中创建死锁并修复它？
8. 如果你的Serializable类包含一个不可序列化的成员，会发生什么？你是如何解决的？
9. 为什么Java中 wait 方法需要在 synchronized 的方法中调用？
10. 你能用Java覆盖静态方法吗？如果我在子类中创建相同的方法是编译时错误？


















