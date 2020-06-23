package com.wangfulin.dp.chain_of_responsibility;

/**
 * @projectName: desinepatterns
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-23 21:33
 **/
public class GeneralManager extends Handler {

    @Override
    public String handleFeeRequest(String user, double fee) {

        String res = "";
        //总经理的权限很大，只要请求到了这里，他都可以处理
        if (fee >= 1000) {
            //为了测试，简单点，只同意张三的请求
            if ("张三".equals(user)) {
                res = "成功：总经理同意【" + user + "】的聚餐费用，金额为" + fee + "元";
            } else {
                //其他人一律不同意
                res = "失败：总经理不同意【" + user + "】的聚餐费用，金额为" + fee + "元";
            }
        } else {
            //如果还有后继的处理对象，继续传递
            if (getSuccessor() != null) {
                return getSuccessor().handleFeeRequest(user, fee);
            }
        }
        return res;
    }

}
