package cdle.wordcount.mr.pdf;

import cdle.wordcount.mr.FindDoc;
import cdle.wordcount.mr.FindDocMapper;
import cdle.wordcount.mr.MyLogUtils;
import com.google.common.base.Charsets;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;
import org.apache.hadoop.io.compress.SplittableCompressionCodec;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

public class SelectInputFormat extends FileInputFormat<LongWritable, Text> {
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
    @Override
    public RecordReader<LongWritable,Text> createRecordReader( InputSplit split, TaskAttemptContext context){

        FileSplit fileSplit = (FileSplit)split;
        Path ok = fileSplit.getPath();

        MyLogUtils.debug(log, ok.getName());

        if (ok.getName().contains(".pdf")) //pdf files
            return new PDFRecordReader();
        else {  //txt files
            String delimiter = context.getConfiguration().get("textinputformat.record.delimiter");
            byte[] recordDelimiterBytes = null;
            if (null != delimiter) {
                recordDelimiterBytes = delimiter.getBytes(Charsets.UTF_8);
            }

            return new LineRecordReader(recordDelimiterBytes);
        }
    }
}