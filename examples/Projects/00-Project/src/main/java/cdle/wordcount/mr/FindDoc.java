package cdle.wordcount.mr;

import cdle.wordcount.mr.pdf.SelectInputFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;



public class FindDoc
		extends Configured
		implements Tool {
	Log log;
	{
		Class<?> klass;
		klass = FindDoc.class;

		log = LogFactory.getLog(klass);
		MyLogUtils.showDebugLevel(log, klass );
	}

	public static void main(String[] args) throws Exception {
		System.exit( ToolRunner.run( new FindDoc(), args) );
	}
	@Override
	public int run(String[] args) throws Exception {
		if ( args.length<2 ) {
			System.err.println( "hadoop ... <input path> <output path> [number of reducers]" );
			System.exit(-1);
		}

		Configuration conf = getConf();
		Job job = Job.getInstance( conf );
		
		job.setJarByClass( FindDoc.class );
		job.setJobName( "Find Doc" );
		
		FileInputFormat.addInputPath(job, new Path(args[0]) );
		FileOutputFormat.setOutputPath(job, new Path(args[1]) );
		
		job.setMapperClass( FindDocMapper.class );
		job.setCombinerClass( FindDocReducer.class );
		job.setReducerClass( FindDocReducer.class );
		job.setInputFormatClass(SelectInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		//MyLogUtils.debug(this.log, conf.get("searchWord"));
		//TextInputFormat

		
		// Output types of map function
		job.setMapOutputKeyClass( Text.class );
		job.setMapOutputValueClass( IntWritable.class );
		
		try {
			int numberOfReducers;
			numberOfReducers = Integer.parseInt( args[2] );
			job.setNumReduceTasks( numberOfReducers );
			System.out.printf( "Setting number of reducers to %d\n", numberOfReducers );
		}
		catch (Exception e) {
			System.out.println( "Using default number (1) of reducers" );
		}
		
		// Output types of reduce function
		job.setOutputKeyClass( Text.class );
		job.setOutputValueClass( IntWritable.class );

		return job.waitForCompletion(true) ? 0 : 1;
	}
}
