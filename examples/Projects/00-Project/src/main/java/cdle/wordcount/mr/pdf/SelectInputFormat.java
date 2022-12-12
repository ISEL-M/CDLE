package cdle.wordcount.mr.pdf;

import cdle.wordcount.mr.FindDoc;
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
    @Override
    public RecordReader<LongWritable,Text> createRecordReader( InputSplit split, TaskAttemptContext context){

        FileSplit fileSplit = (FileSplit)split;
        Path ok = fileSplit.getPath();

        if (ok.getName().contains(".pdf"))
            return new PDFRecordReader();
        else {
            String delimiter = context.getConfiguration().get("textinputformat.record.delimiter");
            byte[] recordDelimiterBytes = null;
            if (null != delimiter) {
                recordDelimiterBytes = delimiter.getBytes(Charsets.UTF_8);
            }

            return new LineRecordReader(recordDelimiterBytes);
        }
    }

    @Override
    protected boolean isSplitable(JobContext context, Path file) {
        CompressionCodec codec = (new CompressionCodecFactory(context.getConfiguration())).getCodec(file);
        return null == codec ? true : codec instanceof SplittableCompressionCodec;
    }
}