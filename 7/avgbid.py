import os
import sys
import json
from pyspark import SparkContext

def get_terms_bidprice(line):
    entry = json.loads(line.strip())
    bidPrice = entry['bidPrice']
    terms_bidprice = []
    #print entry['keyWords']
    for term in entry['keyWords']:
        val = (term, bidPrice)
        terms_bidprice.append(val)
    return terms_bidprice

if __name__ == "__main__":
    input_file = "ads_0502.txt"  # ads data

    sc = SparkContext(appName="avgbid")
    data = sc.textFile(input_file).flatMap(lambda line: get_terms_bidprice(line))\
        .groupByKey().map(lambda (k, values): (k, sum(values)/len(values)))\
        .sortBy(lambda (k, values):values,False)
    data.saveAsTextFile("avgbidresult")
    sc.stop()
