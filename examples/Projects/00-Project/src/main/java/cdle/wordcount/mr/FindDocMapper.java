package cdle.wordcount.mr;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

public class FindDocMapper
			extends Mapper<Object, Text, Text, IntWritable> {
	private static Log log;

	static {
		Class<?> klass;
		klass = org.apache.hadoop.mapreduce.Mapper.class;

		log = LogFactory.getLog( klass );
		MyLogUtils.showDebugLevel( log, klass );

		klass = FindDocMapper.class;

		log = LogFactory.getLog( klass );
		MyLogUtils.showDebugLevel( log, klass );
	}

	private static final IntWritable one = new IntWritable(1);
	private final Text word = new Text();

	@Override
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException {
		Configuration conf = context.getConfiguration();
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			word.set( itr.nextToken() );
			MyLogUtils.debug(log, word.toString());
			if (word.toString().equals(conf.get("searchWord")))
				context.write(word, one);
		}
	}
}
