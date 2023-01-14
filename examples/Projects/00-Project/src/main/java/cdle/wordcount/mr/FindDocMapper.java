package cdle.wordcount.mr;

import java.io.IOException;
import java.net.URI;
import java.util.*;

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
		klass = FindDocMapper.class;

		log = LogFactory.getLog( klass );
		MyLogUtils.showDebugLevel( log, klass );
	}

	private static final IntWritable one = new IntWritable(1);
	private final Text fileName = new Text();

	@Override
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException {

		Configuration conf = context.getConfiguration();
		StringTokenizer itr = new StringTokenizer(value.toString());

		String searchWord = conf.get("searchWord");
		int wordNumber = searchWord.split(" ").length;

		List<String> words = new ArrayList<>();

		while (itr.hasMoreTokens()) {
			if (fileName.toString().equals("")){
				fileName.set(itr.nextToken());
				continue;
			}

			if ( words.size() == wordNumber ) words.remove(0);
			words.add( itr.nextToken());

			String wordCompare = String.join(" ", words);

			if (wordCompare.equals(searchWord))
				context.write(fileName, one);
		}
	}
}
