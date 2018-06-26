import os
import sys
import json
from pyspark import SparkContext

def get_fromads(line):
    entry = json.loads(line.strip())
    adId = entry['adId']
    bidPrice = entry['bidPrice']
    campaignId = entry['campaignId']
    val = (campaignId, (adId, bidPrice))
    return val

def ger_frombudget(line):
    entry = json.loads(line.strip())
    campaignId = entry['campaignId']
    budget = entry['budget']
    val = (campaignId, budget)
    return val

if __name__ == "__main__":
    input_file1 = "ads_0502.txt"  # ads data
    input_file2 = "budget.txt"
    output_file = "avgbidresult.txt"

    sc = SparkContext(appName="ratio")


    data1 = sc.textFile(input_file1).map(lambda line: get_fromads(line))
    data2 = sc.textFile(input_file2).map(lambda line: ger_frombudget(line))
    data3 = data1.join(data2).map(lambda fields: (fields[1][0][0], fields[1][1] / fields[1][0][1])) \
            .sortBy(lambda (k, values): values, False)
    data3.saveAsTextFile("ratioresult")
    sc.stop()