# Flink 入门程序 WordCount 和 SQL 实现

### DataSet WordCount

```java
public class BatchJob {


	public static void main(String[] args) throws Exception {

		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(5);
		// get input data
		DataSet<String> text = env.fromElements(
				"Flink Spark Storm",
				"Flink Flink Flink",
				"Spark Spark Spark",
				"Storm Storm Storm"
		);


		DataSet<Tuple2<String, Integer>> counts =
				text.flatMap(new LineSplitter())
						.groupBy(0)
						.sum(1).setParallelism(1);

		counts.printToErr();

	}


	public static final class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {

		@Override
		public void flatMap(String value, Collector<Tuple2<String, Integer>> out) {
			// normalize and split the line
			String[] tokens = value.toLowerCase().split("\\W+");

			for (String token : tokens) {
				if (token.length() > 0) {
					out.collect(new Tuple2<String, Integer>(token, 1));
				}
			}
		}
	}
}
```

### DataStream WordCount

```
nc -lk 9000
```



```java
public class StreamingJob {

    public static void main(String[] args) throws Exception {

        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // get input data by connecting to the socket
        DataStream<String> text = env.socketTextStream("127.0.0.1", 9000, "\n");

        // parse the data, group it, window it, and aggregate the counts
        DataStream<WordWithCount> windowCounts = text
                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) {
                        for (String word : value.split("\\s")) {
                            out.collect(new WordWithCount(word, 1L));
                        }
                    }
                })
                .keyBy("word")
                .timeWindow(Time.seconds(5), Time.seconds(1))
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, a.count + b.count);
                    }
                });

        // print the results with a single thread, rather than in parallel
        windowCounts.print().setParallelism(1);

        env.execute("Socket Window WordCount");
    }

    // Data type for words with count
    public static class WordWithCount {

        public String word;
        public long count;

        public WordWithCount() {}

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}
```

### Flink Table & SQL WordCount

Flink SQL 是 Flink 实时计算为简化计算模型，降低用户使用实时计算门槛而设计的一套符合标准 SQL 语义的开发语言。

一个完整的 Flink SQL 编写的程序包括如下三部分。

- **Source Operator：**是对外部数据源的抽象, 目前 Apache Flink 内置了很多常用的数据源实现，比如 MySQL、Kafka 等。
- **Transformation Operators：**算子操作主要完成比如查询、聚合操作等，目前 Flink SQL 支持了 Union、Join、Projection、Difference、Intersection 及 window 等大多数传统数据库支持的操作。
- **Sink Operator：**是对外结果表的抽象，目前 Apache Flink 也内置了很多常用的结果表的抽象，比如 Kafka Sink 等。



```java
public class WordCountSQL {
    public static void main(String[] args) throws Exception {
        //获取运行环境
        ExecutionEnvironment fbEnv = ExecutionEnvironment.getExecutionEnvironment();

        //创建一个tableEnvironment
        BatchTableEnvironment fbTableEnv = BatchTableEnvironment.create(fbEnv);

        String text = "hello flink hello lagou";
        String[] words = text.split("\\W+");
        ArrayList<WC> wordList = new ArrayList<>();

        for (String word : words) {
            WC wc = new WC(word, 1);
            wordList.add(wc);
        }

        DataSet<WC> input = fbEnv.fromCollection(wordList);

        //DataSet 转sql, 指定字段名
        Table table = fbTableEnv.fromDataSet(input, "word,frequency");
        table.printSchema();

        // 注册为一个表
        fbTableEnv.createTemporaryView("WordCount", table);

        // 查询
        Table queryTable = fbTableEnv.sqlQuery("select word as word, sum(frequency) as frequency from WordCount GROUP BY word");

        //将表转换DataSet
        DataSet<WC> ds3 = fbTableEnv.toDataSet(queryTable, WC.class);
        ds3.printToErr();

    }


    public static class WC {
        public String word;
        public long frequency;

        public WC() {
        }

        public WC(String word, long frequency) {
            this.word = word;
            this.frequency = frequency;
        }

        @Override
        public String toString() {
            return word + ", " + frequency;
        }
    }
}

```

