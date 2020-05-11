package com.wangfulin.ch1.waitandnotify;

/**
 * @projectName: concurrent
 * @description: 快递实体类
 * @author: Wangfulin
 * @create: 2020-04-17 17:03
 **/
public class Express {
    public final static String CITY = "ShangHai";
    private int km; // 快递运输里程数
    private String site; // 快递到达目的地

    public Express() {
    }

    public Express(int km, String site) {
        this.km = km;
        this.site = site;
    }

    /* 当变化公里数，通知处于wait状态并需要处理公里数的线程进行业务处理*/
    public synchronized void changeKm() {
        this.km = 101;
        notifyAll();
        //其他的业务代码
    }

    /* 当地点，通知处于wait状态并需要处理地点的线程进行业务处理*/
    public synchronized void changeSite() {
        this.site = "BeiJing";
        notify();
    }


    public synchronized void waitKm() {
        while (this.km <= 100) {
            try {
                wait();
                System.out.println("check km thread[" + Thread.currentThread().getId()
                        + "] is be notifed.");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("the km is" + this.km + ",I will change db.");

    }

    public synchronized void waitSite() {
        while (CITY.equals(this.site)) {
            try {
                wait();
                System.out.println("check site thread[" + Thread.currentThread().getId()
                        + "] is be notifed.");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("the site is" + this.site + ",I will call user.");
    }
}
