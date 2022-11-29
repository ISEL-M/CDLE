package cdle.wordcount.mr.pdf;

import cdle.wordcount.mr.FindDoc;
import cdle.wordcount.mr.MyLogUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

public class PDFInputFormat extends FileInputFormat<LongWritable, Text> {

    private final Class<?> klass = PDFInputFormat.class;
    private final Log log = LogFactory.getLog(FindDoc.class);

    @Override
    public RecordReader<LongWritable,Text> createRecordReader( InputSplit split, TaskAttemptContext context){

        FileSplit fileSplit = (FileSplit)split;
        Path ok = fileSplit.getPath();
        MyLogUtils.debug(log,ok.getName());
        if (ok.getName().contains(".pdf"))
            return new PDFRecordReader();
        else
            return new LineRecordReader();
    }
}