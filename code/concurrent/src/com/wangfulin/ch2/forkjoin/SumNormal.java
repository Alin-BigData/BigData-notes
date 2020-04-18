package com.wangfulin.ch2.forkjoin;

/**
 * @projectName: concurrent
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-04-18 01:18
 **/
public class SumNormal {

    public static void main(String[] args) {
        int count = 0;
        int[] src = MakeArray.makeArray();

        long start = System.currentTimeMillis();
        for(int i= 0;i<src.length;i++){
            //SleepTools.ms(1);
            count = count + src[i];
        }
        System.out.println("The count is "+count
                +" spend time:"+(System.currentTimeMillis()-start)+"ms");
    }

}
