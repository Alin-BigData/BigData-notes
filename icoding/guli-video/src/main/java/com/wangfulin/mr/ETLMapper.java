package com.wangfulin.mr;

import com.wangfulin.util.ETLUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

/**
 * @projectName: guli-video
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-22 15:56
 **/
public class ETLMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
    Text text = new Text();

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 获取数据
        // 过滤数据
        String etlString = ETLUtil.oriString2ETLString(value.toString());

        if (etlString == null) {
            return;
        }
        text.set(etlString);
        // 写出
        context.write(NullWritable.get(), text);
    }

}
