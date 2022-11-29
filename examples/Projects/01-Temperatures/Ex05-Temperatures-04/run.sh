#!/bin/bash

clear

source ./usage.sh

CFG=""
for conf in "$@"; do
	CFG="${CFG} ${conf}"
done

INPUT_FILE_SYSTEM_TYPE=file://
OUTPUT_FILE_SYSTEM_TYPE=file://

NUMBER_DISPLAY_LINES=3

INPUT_DIRECTORY=${HOME}/examples/input/temperatures/s1-s2/
OUTPUT_DIRECTORY=${HOME}/examples/output/temperatures/s1-s2

echo -e "Removing previous output..."
CMD="rm -rf ${OUTPUT_DIRECTORY}"
echo -e "${CMD}"
${CMD}

#
# Job jar
#
LOCAL_JOB_JAR=${JAR_FILE}

INPUT=${INPUT_FILE_SYSTEM_TYPE}${INPUT_DIRECTORY}
OUTPUT=${OUTPUT_FILE_SYSTEM_TYPE}${OUTPUT_DIRECTORY}

OPT1="-Dmapreduce.job.outputformat.class=org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat"
OPT2="-Dmapreduce.output.fileoutputformat.compress=true"
OPT3="-Dmapreduce.output.fileoutputformat.compress.codec=org.apache.hadoop.io.compress.GzipCodec"
OPT4="-Dmapreduce.output.fileoutputformat.compress.type=BLOCK"

JOB_CONF="${CFG} ${OPT1} ${OPT2} ${OPT3} ${OPT4}"

ARGS="${JOB_CONF} ${INPUT} ${OUTPUT}"

echo -e "\nUnsetting HADOOP classpath..."
CMD="unset HADOOP_CLASSPATH"
echo -e "${CMD}"
${CMD}

echo -e "\nRunning..."
CMD="hadoop jar ${LOCAL_JOB_JAR} ${ARGS}"
echo -e "${CMD}\n"

${CMD}

OUT_FILES=`hadoop fs -ls ${OUTPUT_FILE_SYSTEM_TYPE}${OUTPUT_DIRECTORY}/part-m-* | tr -s ' ' | cut -d' ' -f8`

NUMBER_TOTAL_ENTRIES=0

for file in ${OUT_FILES}; do
	
	NUMBER_ENTRIES=`hadoop fs -text ${file} 2>/dev/null | wc -l`
	let NUMBER_TOTAL_ENTRIES=${NUMBER_TOTAL_ENTRIES}+${NUMBER_ENTRIES}
	
	echo -e "\nContents of ${file} (${NUMBER_ENTRIES} entries):"
	
	hadoop fs -text ${file}  2>/dev/null | head -n ${NUMBER_DISPLAY_LINES}
	echo "..."
	hadoop fs -text ${file}  2>/dev/null | tail -n ${NUMBER_DISPLAY_LINES}
done

echo -e "\nTotal number of entries: ${NUMBER_TOTAL_ENTRIES}\n" 
