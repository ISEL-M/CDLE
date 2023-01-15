package cdle.wordcount.mr;

import java.io.IOException;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FindDocMapper
			extends Mapper<Object, Text, Text, IntWritable> {
	Log log;
	{
		Class<?> klass;
		klass = FindDocMapper.class;

		log = LogFactory.getLog(klass);
		MyLogUtils.showDebugLevel(log, klass );
	}

	private static final IntWritable count = new IntWritable(1);
	private final Text fileName = new Text();

	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		Configuration conf = context.getConfiguration();
		StringTokenizer itr = new StringTokenizer(value.toString());

		String searchWord = conf.get("searchWord", ""),
				caseSensitive = conf.get("case"),
				wholeWord = conf.get("wholeWord");

		if(searchWord.equals("")) return;


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

			if (caseSensitive==null) {
				searchWord = searchWord.toLowerCase();
				wordCompare = wordCompare.toLowerCase();
			}
			if(wholeWord==null) {
				if (wordCompare.contains(searchWord))
					context.write(fileName, count);
			} else {
				if (wordCompare.equals(searchWord))
					context.write(fileName, count);
			}
		}
	}
}
