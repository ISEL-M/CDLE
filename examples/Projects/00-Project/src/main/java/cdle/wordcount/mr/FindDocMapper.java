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
	private Text word = new Text();

	/* private boolean caseSensitive;
	private Set<String> patternsToSkip = new HashSet<String>();


	@Override
	public void setup(Context context)
			throws IOException, InterruptedException {

		if ( log.isInfoEnabled() ) {
			String msg = String.format( "%s#setup(%s) called", FindDocMapper.class.getSimpleName(), context.getJobName() );

			log.info( msg );
			System.out.printf( "[INFO] %s\n", msg );
		}

		Configuration conf;
		conf = context.getConfiguration();

		this.caseSensitive = conf.getBoolean( "wordcount.case.sensitive", true );
		if ( log.isDebugEnabled() ) {
			String msg = String.format( "wordcount.case.sensitive=%b", this.caseSensitive );

			log.debug( msg );
			System.out.printf( "[DEBUG] %s\n", msg );
		}

		boolean skipPatterns;
		skipPatterns = conf.getBoolean( "wordcount.skip.patterns", false);
		if ( log.isDebugEnabled() ) {
			String msg = String.format( "wordcount.skip.patterns=%b", skipPatterns );

			log.debug( msg );
			System.out.printf( "[DEBUG] %s\n", msg );
		}

		if ( skipPatterns==true ) {
			URI[] patternsURIs = Job.getInstance( conf ).getCacheFiles();

			for (URI patternsURI : patternsURIs) {
				if ( log.isDebugEnabled() ) {
					String msg = String.format( "Current pattern file: %s", patternsURI.getPath() );

					log.debug( msg );
					System.out.printf( "[DEBUG] %s\n", msg );
				}

				Path patternsPath;
				patternsPath = new Path( patternsURI.getPath() );
				String patternsFileName;
				patternsFileName = patternsPath.getName().toString();

				if ( patternsFileName.endsWith( ".txt" ) ) {
					if ( log.isDebugEnabled() ) {
						String msg = String.format( "Parsing %s", patternsFileName );

						log.debug( msg );
						System.out.printf( "[DEBUG] %s\n", msg );
					}
					//WordCountUtils.parseSkipFile( this.patternsToSkip, patternsFileName);
				}

			}
		}
	}*/
	
	@Override
	public void map(Object key, Text value, Context context) 
			throws IOException, InterruptedException {
		
		StringTokenizer itr = new StringTokenizer(value.toString());
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
		}
	}
}
