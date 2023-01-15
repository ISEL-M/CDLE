package cdle.wordcount.mr;

import cdle.wordcount.mr.recordReader.PDFRecordReader;
import cdle.wordcount.mr.recordReader.TXTRecordReader;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class FindDocInputFormat extends FileInputFormat<LongWritable, Text> {
    @Override
    public RecordReader<LongWritable,Text> createRecordReader( InputSplit split, TaskAttemptContext context){

        FileSplit fileSplit = (FileSplit)split;
        Path ok = fileSplit.getPath();

        if (ok.getName().contains(".pdf")) //pdf files
            return new PDFRecordReader();
        else {  //txt files
            return new TXTRecordReader();
        }
    }
}