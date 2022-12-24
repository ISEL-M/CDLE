package cdle.wordcount.mr.pdf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;


public class PDFRecordReader extends RecordReader<LongWritable,Text> {
    private String[] lines = null;
    private LongWritable key = null;
    private Text value = null;
    private String fileName;

    @Override
    public void initialize(InputSplit genericSplit, TaskAttemptContext context)
            throws IOException {
        FileSplit split = (FileSplit)genericSplit;
        Configuration job = context.getConfiguration();


        Path file = split.getPath();
        fileName = file.getName();
        FileSystem fs = file.getFileSystem(job);
        FSDataInputStream fileIn = fs.open(file);

        PDDocument pdf = Loader.loadPDF(fileIn);
        PDFTextStripper stripper = new PDFTextStripper();
        String parsedText = stripper.getText(pdf);
        this.lines = parsedText.split("\n");
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {

        if (key == null) {
            key = new LongWritable();
            key.set(1);
            value = new Text();
            value.set(lines[0]);
            return true;
        } else {
            int temp = (int) key.get();
            if (temp < (lines.length - 1)) {
                int count = (int) key.get();
                value = new Text();
                value.set(lines[count]);
                count = count + 1;
                key = new LongWritable(count);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public LongWritable getCurrentKey() throws IOException,
            InterruptedException {
        return key;
    }

    @Override
    public Text getCurrentValue() throws IOException, InterruptedException {
        return value;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {

    }
}
