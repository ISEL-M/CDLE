package cdle.wordcount.mr.recordReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class TXTRecordReader extends RecordReader<LongWritable,Text> {

    private final ArrayList<String> lines = new ArrayList<>();
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

        BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(file)));

        String line = br.readLine();
        while (line != null){
            line = line.replaceAll("\\p{Punct}","");
            lines.add(line);

            line = br.readLine();
        }
    }

    @Override
    public boolean nextKeyValue() throws IOException {
        if (key == null) {
            key = new LongWritable();
            key.set(0);
            value = new Text();
            value.set(fileName);
            return true;
        } else {
            int temp = (int) key.get();
            if (temp < (lines.size())) {
                int count = (int) key.get();
                value.set(lines.get(count));
                count = count + 1;
                key = new LongWritable(count);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public LongWritable getCurrentKey(){
        return key;
    }

    @Override
    public Text getCurrentValue(){
        return value;
    }

    @Override
    public float getProgress() {
        return (float) key.get() / lines.size();
    }

    @Override
    public void close() {
    }
}
