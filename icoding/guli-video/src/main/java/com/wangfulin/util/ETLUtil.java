package com.wangfulin.util;

/**
 * @projectName: guli-video
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-22 20:32
 **/
public class ETLUtil {
    /**
     * 数据清洗
     * 1.过滤长度不够的
     * 2.去掉类别字段中的空格
     * 3.修改相关视频ID字段的分隔符，由'\t'转为'&'
     * @param ori
     * @return
     */
    public static String oriString2ETLString(String ori) {
        StringBuilder sb = new StringBuilder();
        String[] oriArr = ori.split("\t");
        // 个数不足9个
        if (oriArr.length < 9) {
            return null;
        }
        // 替换空格
        oriArr[3] = oriArr[3].replaceAll(" ","");
        for (int i = 0; i < oriArr.length; i++) {
            // 对非相关id处理 对前面9个字段处理
            if (i < 9) {
                // 如果恰好只有九个字段 则最后面不加'\t'
                if (i == oriArr.length - 1) {
                    sb.append(oriArr[i]);
                }else{
                    sb.append(oriArr[i] + "\t");
                }
            } else {// 处理相关的id
                if (i == oriArr.length - 1) {
                    sb.append(oriArr[i]);
                } else {
                    sb.append(oriArr[i] + '&');
                }
            }
        }
        return sb.toString();
    }
}
