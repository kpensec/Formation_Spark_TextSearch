import os
import sys
import re
from pyspark.sql import SparkSession, Row
from operator import add

def usage():
    print("Usage", sys.argv[0], "<inputDirname>", file=stderr)

def extractWords(line):
    filteredWords = []
    for word in line.split():
        word = word.strip('`,.;:?!*"-\'"')
        if not any(s in word for s in "@/"):
            filteredWords.append((word.lower(), 1))
    return filteredWords

QUERY_MOST_FREQUENT_NSIZED_WORD = """
    SELECT value, number, length
    FROM wordList
    WHERE length = {size}
    ORDER BY number DESC
    LIMIT {limit}
"""
QUERY_LONGEST_MOST_FEQUENT_WORD = """
    SELECT value, length
    FROM wordList
    ORDER BY length
    DESC LIMIT {limit}
"""

def prepareForDataframe(elem):
    return Row(value=elem[0], number=elem[1], length=len(elem[0]))

if __name__ == "__main__":
    if len(sys.argv) < 2:
        usage()
        sys.exit(1)
    inputDirname = sys.argv[1]

    sparkSession = SparkSession.builder.appName(
        "textSearch"
    ).master(
        "local[2]"
    ).getOrCreate()

    rdd = sparkSession.sparkContext\
        .textFile(inputDirname)\
        .flatMap(extractWords)\
        .reduceByKey(add)\
        .map(prepareForDataframe)

    schema = [
        "value",
        "number",
        "length"
    ]

    df = sparkSession.createDataFrame(rdd, schema).persist()
    df.createTempView("wordList")

    queryParameter = {
        "limit": 10,
        "size": 4
    }

    # find largetWord:
    sparkSession.sql(QUERY_LONGEST_MOST_FEQUENT_WORD.format(**queryParameter)).show()
    # find most frequent n letter word:
    sparkSession.sql(QUERY_MOST_FREQUENT_NSIZED_WORD.format(**queryParameter)).show()
    queryParameter['size'] = 15
    sparkSession.sql(QUERY_MOST_FREQUENT_NSIZED_WORD.format(**queryParameter)).show()
