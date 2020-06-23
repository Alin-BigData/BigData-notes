package com.wangfulin.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @projectName: guli-video
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-22 15:57
 **/
public class ETLDriver implements Tool {
    private Configuration conf = null;

    public int run(String[] args) throws Exception {
        String basePath = "/Users/wangfulin/bigdata/data/hive/";
        args = new String[]{basePath + "guiliVideo/video/2008/0222" , basePath + "gulioutput"};

        // 1 获取配置信息以及封装任务

        // 1.job对象
        Job job = Job.getInstance(conf);
        // 2.设置jar路径
        job.setJarByClass(ETLDriver.class);
        // 设置mapper输出的kv类型
        job.setMapperClass(ETLMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);
        // 设置输入输出路径
        // 5 设置最终输出kv类型
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);
        // 6 设置输入和输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 7 提交
        boolean result = job.waitForCompletion(true);

        return result ? 0 : 1;

    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public Configuration getConf() {
        return this.conf;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();
        ToolRunner.run(configuration,new ETLDriver(),args);
    }
}
